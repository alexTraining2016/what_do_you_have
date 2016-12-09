package comalexpolyanskyi.github.foodandhealth.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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
import comalexpolyanskyi.github.foodandhealth.mediators.DescriptionActivityMediator;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.ui.activities.buttonManagers.abstractManagers.AbstractButtonManager;
import comalexpolyanskyi.github.foodandhealth.ui.activities.buttonManagers.FavoritesButtonManager;
import comalexpolyanskyi.github.foodandhealth.ui.activities.buttonManagers.LikeButtonManager;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;
import comalexpolyanskyi.github.foodandhealth.utils.holders.AppStyleHolder;
import comalexpolyanskyi.github.foodandhealth.utils.imageloader.MySimpleImageLoader;


public class DescriptionActivity extends AppCompatActivity implements InteractionContract.RequiredView<ArticleDO>, AbstractButtonManager.DataUpdateCallback {

    public static final String EXTRA_IMAGE = "DescriptionActivity:image";
    public static final String ACTION = "Action";
    public static final String DATA_KEY = "data";
    private ImageView imageView;
    private View progressBar;
    private ArticleDO data;
    private TextView descriptionText;
    private InteractionContract.Mediator<String> mediator;

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
            this.mediator = new DescriptionActivityMediator(this);
            mediator.loadData(intent.getStringExtra(MainActivity.TITLE_KEY), intent.getStringExtra(AuthConstant.TOKEN), intent.getStringExtra(AuthConstant.ID));
        } else {
            returnData((ArticleDO) savedInstanceState.getSerializable(DATA_KEY));
            this.mediator = new DescriptionActivityMediator(this);
            if (data == null) {
                Intent intent = getIntent();
                mediator.loadData(intent.getStringExtra(MainActivity.TITLE_KEY), intent.getStringExtra(AuthConstant.TOKEN), intent.getStringExtra(AuthConstant.ID));
            }
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
        mediator.onDestroy();
    }

    private void bindHeader() {
        final MySimpleImageLoader imageLoader = App.getImageLoader();
        imageLoader.loadImageFromUrl(data.getPhotoUrl(), imageView);
        ((CollapsingToolbarLayout) findViewById(R.id.toolbar_layout)).setTitle(data.getName());
        ((AppBarLayout) findViewById(R.id.app_bar)).setExpanded(true, true);
        descriptionText.setText(data.getDescription());
        final Intent intent = getIntent();
        new LikeButtonManager(
                intent.getStringExtra(AuthConstant.TOKEN),
                intent.getStringExtra(MainActivity.TITLE_KEY),
                findViewById(R.id.like_count),
                data.isLike(),
                Integer.toString(data.getLikeCount()),
                this );
        new FavoritesButtonManager(
                intent.getStringExtra(AuthConstant.TOKEN),
                intent.getStringExtra(MainActivity.TITLE_KEY),
                findViewById(R.id.fav_count),
                data.isRepost(),
                Integer.toString(data.getFavCount()),
                this );
    }

    @Override
    public void returnData(ArticleDO response) {
        data = response;
        if (data != null) {
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

    @Override
    public void refresh() {
        final Intent intent = getIntent();
        mediator.loadData(intent.getStringExtra(MainActivity.TITLE_KEY), intent.getStringExtra(AuthConstant.TOKEN), intent.getStringExtra(AuthConstant.ID));
    }
}