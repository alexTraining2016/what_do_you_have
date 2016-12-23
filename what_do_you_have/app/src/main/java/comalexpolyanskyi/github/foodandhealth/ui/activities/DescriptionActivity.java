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
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.mediators.fragmentMediators.DescriptionActivityMediator;
import comalexpolyanskyi.github.foodandhealth.ui.buttonManagers.FavoritesButtonManager;
import comalexpolyanskyi.github.foodandhealth.ui.buttonManagers.LikeButtonManager;
import comalexpolyanskyi.github.foodandhealth.ui.buttonManagers.abstractManagers.AbstractButtonManager;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;
import comalexpolyanskyi.github.foodandhealth.utils.holders.AppStyleHolder;
import comalexpolyanskyi.github.foodandhealth.utils.imageloader.MySimpleImageLoader;


public class DescriptionActivity extends AppCompatActivity implements InteractionContract.RequiredView<ArticleDO>, AbstractButtonManager.DataUpdateCallback, View.OnClickListener {

    public static final String EXTRA_IMAGE = "DescriptionActivity:image";
    public static final String ACTION = "action";
    public static final String DATA_KEY = "data";
    private ImageView imageView;
    private View progressBar;
    private ArticleDO data;
    private TextView descriptionText;
    private InteractionContract.Mediator<String> mediator;
    private AbstractButtonManager likeButtonManager, favButtonManager;
    private boolean isUpdate = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(AppStyleHolder.initialize().getTheme());
        setContentView(R.layout.desctiption_activity_scrolling);
        bindView();
        mediator = new DescriptionActivityMediator(this);
        bindHeaderButton(getIntent());
        loadData(savedInstanceState, getIntent());
    }

    private void loadData(@Nullable final Bundle savedInstanceState, final Intent intent) {
        if (savedInstanceState != null) {
            returnData((ArticleDO) savedInstanceState.getSerializable(DATA_KEY));
        }
        
        if (data == null) {
            mediator.loadData(DescriptionActivityMediator.LOAD, intent.getStringExtra(MainActivity.TITLE_KEY),
                    intent.getStringExtra(AuthConstant.TOKEN), intent.getStringExtra(AuthConstant.ID));
        }
    }

    private void bindView() {
        imageView = (ImageView) findViewById(R.id.imageView);
        final View v = findViewById(R.id.toolbar_layout);
        ViewCompat.setTransitionName(v, EXTRA_IMAGE);
        descriptionText = (TextView) findViewById(R.id.description);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.description_tollbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        progressBar = findViewById(R.id.description_progress);
    }

    private void bindHeaderButton(final Intent intent) {
        likeButtonManager = new LikeButtonManager(findViewById(R.id.like_count), this);
        favButtonManager = new FavoritesButtonManager(findViewById(R.id.fav_count), this);

        final String token = intent.getStringExtra(AuthConstant.TOKEN);
        if (!mediator.accessCheck(token)) {
            likeButtonManager.setOnClickListener(this);
            favButtonManager.setOnClickListener(this);
        }
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
        if (!isUpdate) {
            final MySimpleImageLoader imageLoader = App.getImageLoader();
            imageLoader.loadImageFromUrl(data.getPhotoUrl(), imageView);
            ((CollapsingToolbarLayout) findViewById(R.id.toolbar_layout)).setTitle(data.getName());
            ((AppBarLayout) findViewById(R.id.app_bar)).setExpanded(true, true);
            descriptionText.setText(data.getDescription());
            likeButtonManager.setData(String.valueOf(data.getLikeCount()), data.isLike());
            favButtonManager.setData(String.valueOf(data.getFavCount()), data.isRepost());
        } else {
            isUpdate = false;
            likeButtonManager.updateDrawable(data.isLike());
            likeButtonManager.updateText(Integer.toString(data.getLikeCount()));
            favButtonManager.updateDrawable(data.isRepost());
            favButtonManager.updateText(Integer.toString(data.getFavCount()));
        }
    }

    @Override
    public void returnData(final ArticleDO data) {
        if (data != null) {
            this.data = data;
            bindHeader();
        }
    }

    @Override
    public void returnError(final String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).setAction(ACTION, null).show();

        if (data != null) {
            bindHeader();
        }
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
    public void refresh(final String type) {
        isUpdate = true;
        final Intent intent = getIntent();
        mediator.loadData(DescriptionActivityMediator.UPDATE, intent.getStringExtra(MainActivity.TITLE_KEY),
                intent.getStringExtra(AuthConstant.TOKEN), type);
    }

    @Override
    public void onClick(View v) {
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.access_error), Snackbar.LENGTH_LONG).setAction(ACTION, null).show();
    }
}