package comalexpolyanskyi.github.foodandhealth.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import comalexpolyanskyi.github.foodandhealth.App;
import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ArticleDO;
import comalexpolyanskyi.github.foodandhealth.presenter.DescriptionActivityPresenter;
import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;
import comalexpolyanskyi.github.foodandhealth.ui.views.VectorImageTextView;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;
import comalexpolyanskyi.github.foodandhealth.utils.holders.AppStyleHolder;
import comalexpolyanskyi.github.foodandhealth.utils.imageloader.MySimpleImageLoader;


public class DescriptionActivity extends AppCompatActivity implements MVPContract.RequiredView<ArticleDO> {

    public static final String EXTRA_IMAGE = "DescriptionActivity:image";
    public static final String ACTION = "Action";
    public static final String DATA_KEY = "data";
    private ImageView imageView;
    private View progressBar;
    private ArticleDO data;
    private TextView descriptionText;
    private VectorImageTextView favButton, likeButton;
    private MVPContract.Presenter<String, ArticleDO> presenter;
    private boolean waitFavoritState = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(AppStyleHolder.initialize().getTheme());
        setContentView(R.layout.desctiption_activity_scrolling);
        bindView();
        bindPresenter(savedInstanceState);
    }

    private void bindPresenter(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            this.presenter = new DescriptionActivityPresenter(this);
            presenter.loadData(intent.getStringExtra(MainActivity.TITLE_KEY), intent.getStringExtra(AuthConstant.TOKEN), null);
            waitFavoritState = false;
        } else {
            returnData((ArticleDO) savedInstanceState.getSerializable(DATA_KEY));
            //here need to save presenter
            this.presenter = new DescriptionActivityPresenter(this);
        }
    }

    private void bindView() {
        imageView = (ImageView) findViewById(R.id.imageView);
        ViewCompat.setTransitionName(imageView, EXTRA_IMAGE);
        descriptionText = (TextView) findViewById(R.id.description);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.description_tollbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressBar = findViewById(R.id.description_progress);
        likeButton = (VectorImageTextView) findViewById(R.id.like_count);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = getIntent();
                final String param = "&act=like";
                waitFavoritState = false;
                presenter.loadData(intent.getStringExtra(MainActivity.TITLE_KEY), intent.getStringExtra(AuthConstant.TOKEN), param);
            }
        });

        favButton = (VectorImageTextView) findViewById(R.id.fav_count);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = getIntent();
                final String param = "&act=repost";
                waitFavoritState = true;
                presenter.loadData(intent.getStringExtra(MainActivity.TITLE_KEY), intent.getStringExtra(AuthConstant.TOKEN), param);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(DATA_KEY, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ActivityCompat.finishAfterTransition(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    private void bindHeader() {
        final MySimpleImageLoader imageLoader = App.getImageLoader();
        imageLoader.loadImageFromUrl(data.getPhotoUrl(), imageView);
        descriptionText.setText(data.getDescription());
        likeButton.setText(Integer.toString(data.getLikeCount()));
        favButton.setText(Integer.toString(data.getFavCount()));
        if (data.isLike()) {
            likeButton.setRightDrawable(R.drawable.ic_favorite_blue_24dp);
        } else {
            likeButton.setRightDrawable(R.drawable.ic_favorite_black_24dp);
        }
        if (data.isRepost()) {
            favButton.setRightDrawable(R.drawable.ic_repeat_blue_24dp);
        } else {
            favButton.setRightDrawable(R.drawable.ic_repeat_black_24dp);
        }
        if(waitFavoritState){
            makeActRepostMessage(data.isRepost());
        }
    }

    private void makeActRepostMessage(boolean isRepost){
        int str = R.string.favoritesDeleteMessage;
        if(isRepost){
            str = R.string.favoritesAddMessage;
        }
        final String message = getString(str);
        Snackbar.make(favButton, message, Snackbar.LENGTH_LONG)
                .setAction(ACTION, null).show();
    }

    @Override
    public void returnData(ArticleDO response) {
        data = response;
        if(data != null) {
            bindHeader();
        }
    }

    @Override
    public void returnError(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).setAction(ACTION, null).show();
    }

    @Override
    public void showProgress(boolean isInProgress) {
        if (isInProgress) {
            progressBar.setVisibility(View.VISIBLE);
            findViewById(R.id.app_bar).setVisibility(View.GONE);
            descriptionText.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            findViewById(R.id.app_bar).setVisibility(View.VISIBLE);
            descriptionText.setVisibility(View.VISIBLE);
        }
    }
}