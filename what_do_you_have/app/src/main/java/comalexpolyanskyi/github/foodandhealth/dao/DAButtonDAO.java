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

    private static final String REPOST_COUNT = "repost_count";
    private static final String DATA_NAME = "0";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String SUCCESSFULLY = "successfully";
    private static final String UID = "uid";
    private static final String IS_REPOST = "isRepost";
    private static final String IS_LIKE = "isLike";
    private static final String AND = " = ? and ";
    private static final String RECEPTE_ID = "recepte_id";
    private static final String USER_ID = "user_id";
    private static final String EQUALLY = " = ?";
    private static final String LIKE = "like";
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
    public void get(final String parameters, final boolean forceUpdate, final boolean noUpdate) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final byte[] requestBytes = httpClient.loadDataFromHttp(parameters, false);

                if (requestBytes != null) {
                    final String requestString = new String(requestBytes, Charset.forName(CHARSET_NAME));

                    try {
                        final JSONObject object = new JSONObject(requestString);

                        if (object.getBoolean(SUCCESSFULLY)) {
                            final JSONObject data = object.getJSONObject(DATA_NAME);
                            toPrepareFavoriteData(data);
                            toPrepareDescriptionData(data);
                        } else {
                            showError();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        showError();
                    }
                } else {
                    showError();
                }
            }
        });
    }

    private void showError() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                presenter.onError();
            }
        });
    }

    ////android палит
    private void toPrepareDescriptionData(JSONObject data) throws JSONException {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(Favorites.ID, data.getString(UID));
        contentValues.put(Favorites.ISFAVORITES, data.getString(IS_REPOST));
        contentValues.put(Favorites.ISLIKE, data.getString(IS_LIKE));
        final String where = Favorites.ART_ID + AND + Favorites.USER_ID + EQUALLY;
        operations.updateForParam(Favorites.class, contentValues, where, data.getString(RECEPTE_ID), data.getString(USER_ID));
    }

    private void toPrepareFavoriteData(JSONObject data) throws JSONException {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(ArticleDescription.REPOST_COUNT, data.getString(REPOST_COUNT));
        contentValues.put(ArticleDescription.LIKE_COUNT, data.getString(LIKE));
        final String where = ArticleDescription.ID + EQUALLY;

        operations.updateForParam(ArticleDescription.class, contentValues, where, data.getString(RECEPTE_ID));
    }
}
