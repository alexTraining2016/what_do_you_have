package comalexpolyanskyi.github.foodandhealth.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.presenter.ImageLoader;
import comalexpolyanskyi.github.foodandhealth.ui.views.VectorImageTextView;


public class DescriptionActivity extends AppCompatActivity {
    private ImageLoader imageLoader;
    public static final String EXTRA_IMAGE = "DescriptionActivity:image";
    public static final String TITLE = "title";
    public static final String ACTION = "Action";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        ViewCompat.setTransitionName(imageView, EXTRA_IMAGE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String title = intent.getExtras().getString(TITLE);
        setTitle(title);
        imageLoader = new ImageLoader(ContextCompat.getDrawable(this, R.mipmap.images));
        imageLoader.loadImageFromUrl("http://www.planwallpaper.com/static/images/6768666-1080p-wallpapers.jpg", imageView);
        VectorImageTextView favBtn = (VectorImageTextView) findViewById(R.id.fav);
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Добавлено в избранное", Snackbar.LENGTH_LONG)
                        .setAction(ACTION, null).show();
            }
        });
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
        imageLoader.onDestroy();
    }
}
