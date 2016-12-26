package comalexpolyanskyi.github.foodandhealth.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import comalexpolyanskyi.github.foodandhealth.App;
import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ArticleDO;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.mediators.fragmentMediators.DescriptionActivityMediator;
import comalexpolyanskyi.github.foodandhealth.ui.buttonManagers.FavoritesButtonManager;
import comalexpolyanskyi.github.foodandhealth.ui.buttonManagers.LikeButtonManager;
import comalexpolyanskyi.github.foodandhealth.ui.buttonManagers.abstractManagers.AbstractButtonManager;
import comalexpolyanskyi.github.foodandhealth.ui.fragments.descriptionFragments.ArticleFragment;
import comalexpolyanskyi.github.foodandhealth.ui.fragments.descriptionFragments.CommentsFragment;
import comalexpolyanskyi.github.foodandhealth.ui.fragments.descriptionFragments.PagesCommunicator;
import comalexpolyanskyi.github.foodandhealth.ui.fragments.descriptionFragments.PropertiesFragment;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.ViewPagerAdapter;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;
import comalexpolyanskyi.github.foodandhealth.utils.holders.AppStyleHolder;
import comalexpolyanskyi.github.foodandhealth.utils.imageloader.MySimpleImageLoader;

public class DescriptionActivity extends AppCompatActivity implements InteractionContract.RequiredView<ArticleDO>,
        AbstractButtonManager.DataUpdateCallback, View.OnClickListener, Palette.PaletteAsyncListener {

    public static final String EXTRA_IMAGE = "DescriptionActivity:image";
    public static final String ACTION = "action";
    public static final String DATA_KEY = "data";
    private ImageView imageView;
    private View progressBar;
    private ArticleDO data;
    private InteractionContract.Mediator<String> mediator;
    private AbstractButtonManager likeButtonManager, favButtonManager;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private PagesCommunicator<ArticleDO> articleCommunicator, propertiesCommunicator;
    private boolean isUpdate = false;
    private boolean generationComplete = false;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onGenerated(Palette palette) {
        final Palette.Swatch swatch = palette.getDarkVibrantSwatch();
        if (swatch != null) {
            int mainColor = swatch.getRgb();
            tabLayout.setBackgroundColor(mainColor);
            collapsingToolbarLayout.setStatusBarScrimColor(mainColor);
            collapsingToolbarLayout.setContentScrimColor(mainColor);
            propertiesCommunicator.setupColor(mainColor);
        }
        generationComplete = true;
        showProgress(false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AppStyleHolder appStyleHolder = AppStyleHolder.initialize();
        setTheme(appStyleHolder.getTheme());
        setContentView(R.layout.desctiption_activity_scrolling);
        bindView();
        mediator = new DescriptionActivityMediator(this);
        bindHeaderButton(getIntent());
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
        setPictureFromTab();
        loadData(savedInstanceState, getIntent());
    }

    private void setPictureFromTab() {
        int[] pictureArr = {
                R.drawable.ic_equalizer_black_24dp,
                R.drawable.ic_receipt_black_24dp,
                R.drawable.ic_rate_review_black_24dp
        };

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            final TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setIcon(pictureArr[i]);
            }
        }
    }

    private void setupViewPager() {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        articleCommunicator = new ArticleFragment();
        propertiesCommunicator = new PropertiesFragment();
        adapter.addFragment((Fragment) propertiesCommunicator);
        adapter.addFragment((Fragment) articleCommunicator);
        adapter.addFragment(new CommentsFragment());
        viewPager.setAdapter(adapter);
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
        final Toolbar toolbar = (Toolbar) findViewById(R.id.description_tollbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout = ((CollapsingToolbarLayout) findViewById(R.id.toolbar_layout));

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        progressBar = findViewById(R.id.progress);
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

    private void bindData(boolean showMessage) {
        if (!isUpdate) {
            final MySimpleImageLoader imageLoader = App.getImageLoader();
            imageLoader.loadImageFromUrl(data.getPhotoUrl(), imageView, this);
            collapsingToolbarLayout.setTitle(data.getName());
            ((AppBarLayout) findViewById(R.id.app_bar)).setExpanded(true, true);
            likeButtonManager.setData(String.valueOf(data.getLikeCount()), data.isLike());
            favButtonManager.setData(String.valueOf(data.getFavCount()), data.isRepost());
            articleCommunicator.updateData(data);
            propertiesCommunicator.updateData(data);
        } else {
            isUpdate = false;
            likeButtonManager.resetDrawable(data.isLike(), showMessage);
            likeButtonManager.resetText(Integer.toString(data.getLikeCount()));
            favButtonManager.resetDrawable(data.isRepost(), showMessage);
            favButtonManager.resetText(Integer.toString(data.getFavCount()));
        }
    }

    @Override
    public void returnData(final ArticleDO data) {
        if (data != null) {
            this.data = data;
            bindData(true);
        }
    }

    @Override
    public void returnError(final String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).setAction(ACTION, null).show();
        if (data != null) {
            bindData(false);
        }
    }

    @Override
    public void showProgress(boolean isInProgress) {
        if (isInProgress) {
            progressBar.setVisibility(View.VISIBLE);
            findViewById(R.id.app_bar).setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
        } else if (generationComplete) {
            progressBar.setVisibility(View.GONE);
            findViewById(R.id.app_bar).setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
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