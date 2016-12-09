package comalexpolyanskyi.github.foodandhealth.dao;

import android.content.ContentValues;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import comalexpolyanskyi.github.foodandhealth.dao.database.DBHelper;
import comalexpolyanskyi.github.foodandhealth.dao.database.DbOperations;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.ArticleDescription;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Favorites;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.utils.AppHttpClient;
import comalexpolyanskyi.github.foodandhealth.utils.holders.ContextHolder;

public class DAButtonDAO implements InteractionContract.DAO<String> {

    private static final String DATA_NAME = "0";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String SUCCESSFULLY = "successfully";
    private ExecutorService executorService;
    private InteractionContract.RequiredPresenter<Void> presenter;
    private Handler handler;
    private AppHttpClient httpClient;
    private DbOperations operations;

    public DAButtonDAO(@NonNull InteractionContract.RequiredPresenter<Void> presenter) {
        handler = new Handler(Looper.getMainLooper());
        this.presenter = presenter;
        executorService = Executors.newSingleThreadExecutor();
        httpClient = AppHttpClient.getAppHttpClient();
        operations = new DBHelper(ContextHolder.getContext(), DbOperations.FOOD_AND_HEAL, DbOperations.VERSION);
    }

    @Override
    public void get(final String parameters, final boolean forceUpdate) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final byte[] requestBytes = httpClient.loadDataFromHttp(parameters, false);

                if (requestBytes != null) {
                    final String requestString = new String(requestBytes, Charset.forName(CHARSET_NAME));

                    try {
                        final JSONObject object = new JSONObject(requestString);
                        final boolean isSuccess = object.getBoolean(SUCCESSFULLY);

                        if (!isSuccess) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    presenter.onError();
                                }
                            });
                        } else {
                            updateData(object.getJSONObject(DATA_NAME));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                presenter.onError();
                            }
                        });
                    }
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            presenter.onError();
                        }
                    });
                }
            }
        });
    }

    private void updateData(JSONObject data) throws JSONException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Favorites.ID, data.getString("uid"));
        contentValues.put(Favorites.ISFAVORITES, data.getString("isRepost"));
        contentValues.put(Favorites.ISLIKE, data.getString("isLike"));
        String where = Favorites.ART_ID + " = ? and " + Favorites.USER_ID + " = ?";
        String[] arg = new String[]{data.getString("recepte_id"), data.getString("user_id")};
        operations.updateForParam(Favorites.class, contentValues, where, arg);

        contentValues.clear();
        contentValues.put(ArticleDescription.REPOST_COUNT, data.getString("repost_count"));
        contentValues.put(ArticleDescription.LIKE_COUNT, data.getString("like"));
        where = ArticleDescription.ID + " = ?";
        arg = new String[]{data.getString("recepte_id")};
        operations.updateForParam(ArticleDescription.class, contentValues, where, arg);
    }
}
