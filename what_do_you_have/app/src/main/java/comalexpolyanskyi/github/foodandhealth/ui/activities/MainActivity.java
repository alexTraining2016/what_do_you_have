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
import comalexpolyanskyi.github.foodandhealth.models.pojo.QueryParameters;
import comalexpolyanskyi.github.foodandhealth.ui.fragments.RecipesListFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        RecipesListFragment.OnListFragmentInteractionListener {

    public static final int ALL_FOOD_RECIPES = 0;
    public static final int FOOD_RECIPES_BY_INGREDIENT = 1;
    public static final int FAVORITES_FOOD_RECIPES = 2;
    public static final int ALL_TRAINING_RECIPES = 3;
    public static final int ALL_DIET_RECIPES = 4;
    public static final int FAVORITES_TRAINING_AND_DIET_RECIPES = 5;
    public static final String TITLE_KEY = "title";
    public static final String THEME_KEY = "theme";
    public static final String COLOR_KEY = "color";
    public static final String DRAWABLE_KEY = "draw";
    private int theme = 0;
    private int color = 0;
    private int drawable = R.drawable.food;
    private String title = null;
    private Toolbar toolbar;
    private View headerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        setupNavigationDrawer(toolbar);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            theme = savedInstanceState.getInt(THEME_KEY);
            color = savedInstanceState.getInt(COLOR_KEY);
            drawable = savedInstanceState.getInt(DRAWABLE_KEY);
            toolbar.setBackgroundColor(getResources().getColor(color));
            headerLayout.setBackground(getResources().getDrawable(drawable));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupNavigationDrawer(Toolbar toolbar){
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
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_all_recipes));
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(THEME_KEY, theme);
        outState.putInt(COLOR_KEY, color);
        outState.putInt(DRAWABLE_KEY, drawable);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = prepareFragment(id);
        if (fragment != null) {
            completeTransaction(fragment, title);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Fragment prepareFragment(int id){
        Fragment fragment = null;
        if (id == R.id.nav_all_recipes) {
            title = getString(R.string.all_recipes);
            theme = R.style.FoodAppTheme;
            color = R.color.colorPrimaryFood;
            drawable = R.drawable.food;
            fragment = RecipesListFragment.newInstance(new QueryParameters(0, new HashMap<String, String>()));
        }else if(id == R.id.nav_favorites){
            title = getString(R.string.favorites);
            theme = R.style.FoodAppTheme;
            color = R.color.colorPrimaryFood;
            drawable = R.drawable.food;
            fragment = RecipesListFragment.newInstance(new QueryParameters(0, new HashMap<String, String>()));
        }else if(id == R.id.nav_diets){
            title = getString(R.string.diets);
            theme = R.style.TrainingAppTheme;
            color = R.color.colorPrimaryTraining;
            drawable = R.drawable.fitness;
            fragment = RecipesListFragment.newInstance(new QueryParameters(0, new HashMap<String, String>()));
        }else if(id == R.id.nav_training_program){
            title = getString(R.string.training_program);
            theme = R.style.TrainingAppTheme;
            color = R.color.colorPrimaryTraining;
            drawable = R.drawable.fitness;
            fragment = RecipesListFragment.newInstance(new QueryParameters(0, new HashMap<String, String>()));
        }else if(id == R.id.nav_favorites_program){
            theme = R.style.TrainingAppTheme;
            title = getString(R.string.favorites_program);
            color = R.color.colorPrimaryTraining;
            drawable = R.drawable.fitness;
            fragment = RecipesListFragment.newInstance(new QueryParameters(0, new HashMap<String, String>()));
        }
        return fragment;
    }

    private void completeTransaction(Fragment fragment, String title){
        FragmentTransaction fragmentTransaction;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();

        toolbar.setBackgroundColor(getResources().getColor(color));
        headerLayout.setBackground(getResources().getDrawable(drawable));
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
        intent.putExtra(THEME_KEY, theme);
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }
}