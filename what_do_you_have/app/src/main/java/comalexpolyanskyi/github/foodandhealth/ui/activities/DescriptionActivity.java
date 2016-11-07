package comalexpolyanskyi.github.foodandhealth.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import comalexpolyanskyi.github.foodandhealth.App;
import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.ArticleDO;
import comalexpolyanskyi.github.foodandhealth.presenter.DescriptionActivityPresenter;
import comalexpolyanskyi.github.foodandhealth.presenter.MVPContract;
import comalexpolyanskyi.github.foodandhealth.ui.views.VectorImageTextView;
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
    private DescriptionActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(AppStyleHolder.initialize().getTheme());
        setContentView(R.layout.desctiption_activity_scrolling);
        bindView();
        bindPresenter(savedInstanceState);
    }

    private void bindPresenter(Bundle savedInstanceState){
        if(savedInstanceState == null){
            Intent intent = getIntent();
            this.presenter = new DescriptionActivityPresenter(this);
            presenter.loadData(intent.getExtras().getString(MainActivity.TITLE_KEY));
        }else{
            returnData((ArticleDO) savedInstanceState.getSerializable(DATA_KEY));
            this.presenter = new DescriptionActivityPresenter(this);
        }
    }

    private void bindView(){
        imageView = (ImageView) findViewById(R.id.imageView);
        ViewCompat.setTransitionName(imageView, EXTRA_IMAGE);
        descriptionText = (TextView) findViewById(R.id.description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.description_tollbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.description_progress);
        likeButton = (VectorImageTextView) findViewById(R.id.like_count);
        favButton = (VectorImageTextView) findViewById(R.id.fav_count);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Добавлено в избранное", Snackbar.LENGTH_LONG)
                        .setAction(ACTION, null).show();
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

    @Override
    public void returnData(ArticleDO response) {
        data = response;
        MySimpleImageLoader imageLoader = App.getImageLoader();
        imageLoader.loadImageFromUrl(response.getPhotoUrl(), imageView);
        ((CollapsingToolbarLayout)findViewById(R.id.toolbar_layout)).setTitle(response.getName());
        ((AppBarLayout)findViewById(R.id.app_bar)).setExpanded(true, true);
        descriptionText.setText(response.getDescription());
        likeButton.setText(response.getLikeCount()+"");
        favButton.setText(response.getFavCount()+"");
    }

    @Override
    public void returnError(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).setAction(ACTION, null).show();
    }

    @Override
    public void showProgress(boolean isInProgress) {
        if(isInProgress){
            progressBar.setVisibility(View.VISIBLE);
            findViewById(R.id.app_bar).setVisibility(View.GONE);
            descriptionText.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            findViewById(R.id.app_bar).setVisibility(View.VISIBLE);
            descriptionText.setVisibility(View.VISIBLE);
        }
    }
}
