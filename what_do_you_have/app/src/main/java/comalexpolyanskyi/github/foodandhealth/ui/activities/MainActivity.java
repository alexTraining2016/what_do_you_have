package comalexpolyanskyi.github.foodandhealth.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.HashMap;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.dao.dataObjects.QueryParameters;
import comalexpolyanskyi.github.foodandhealth.presenter.ArticlesTypeRequest;
import comalexpolyanskyi.github.foodandhealth.ui.fragments.IngredientListFragment;
import comalexpolyanskyi.github.foodandhealth.ui.fragments.ArticleListFragment;
import comalexpolyanskyi.github.foodandhealth.utils.holders.AppStyleHolder;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ArticleListFragment.OnListFragmentInteractionListener {

    public static final String TITLE_KEY = "title";
    private AppStyleHolder appStyleHolder;

    private Toolbar toolbar;
    private View headerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        appStyleHolder = AppStyleHolder.initialize();
        setSupportActionBar(toolbar);
        NavigationView navigationView = setupNavigationDrawer(toolbar);
        if(savedInstanceState == null){
            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_all_recipes));
        }else{
            toolbar.setBackgroundColor(getResources().getColor(appStyleHolder.getColor()));
            headerLayout.setBackground(getResources().getDrawable(appStyleHolder.getDrawable()));
            getSupportActionBar().setTitle(appStyleHolder.getTitle());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        return super.onCreateOptionsMenu(menu);
    }

    private NavigationView setupNavigationDrawer(Toolbar toolbar){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerLayout = navigationView.getHeaderView(0);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        return navigationView;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = prepareFragment(id);
        if (fragment != null) {
            completeTransaction(fragment, appStyleHolder.getTitle());
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Fragment prepareFragment(int id){
        Fragment fragment = null;
        if(id == R.id.nav_i_have){
            appStyleHolder.defaultInitialize(getString(R.string.i_have));
            fragment = new IngredientListFragment();
        }else if (id == R.id.nav_all_recipes) {
            appStyleHolder.defaultInitialize(getString(R.string.all_recipes));
            fragment = ArticleListFragment.newInstance(new QueryParameters(ArticlesTypeRequest.ALL_FOOD_RECIPES, new HashMap<String, String>()));
        }else if(id == R.id.nav_favorites){
            appStyleHolder.defaultInitialize(getString(R.string.favorites));
            fragment = ArticleListFragment.newInstance(new QueryParameters(ArticlesTypeRequest.FAVORITES_FOOD_RECIPES, new HashMap<String, String>()));
        }else if(id == R.id.nav_diets){
            appStyleHolder.fitnessInitialize(getString(R.string.diets));
            fragment = ArticleListFragment.newInstance(new QueryParameters(ArticlesTypeRequest.ALL_DIET_RECIPES, new HashMap<String, String>()));
        }else if(id == R.id.nav_training_program){
            appStyleHolder.fitnessInitialize(getString(R.string.training_program));
            fragment = ArticleListFragment.newInstance(new QueryParameters(ArticlesTypeRequest.ALL_TRAINING_RECIPES, new HashMap<String, String>()));
        }else if(id == R.id.nav_favorites_program){
            appStyleHolder.fitnessInitialize(getString(R.string.favorites_program));
            fragment = ArticleListFragment.newInstance(new QueryParameters(ArticlesTypeRequest.FAVORITES_TRAINING_AND_DIET_RECIPES, new HashMap<String, String>()));
        }
        return fragment;
    }

    private void completeTransaction(Fragment fragment, String title){
        FragmentTransaction fragmentTransaction;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();

        toolbar.setBackgroundColor(getResources().getColor(appStyleHolder.getColor()));
        headerLayout.setBackground(getResources().getDrawable(appStyleHolder.getDrawable()));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onRecipesFragmentInteraction(View v) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this, v.findViewById(R.id.imageView), DescriptionActivity.EXTRA_IMAGE);
        Intent intent = new Intent(this, DescriptionActivity.class);
        intent.putExtra(TITLE_KEY, v.getTag().toString());
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }
}