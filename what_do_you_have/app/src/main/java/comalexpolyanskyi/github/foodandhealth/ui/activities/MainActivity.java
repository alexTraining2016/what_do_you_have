package comalexpolyanskyi.github.foodandhealth.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.HashSet;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.ui.fragments.IngredientListFragment;
import comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments.AllRecipesRVFragment;
import comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments.BaseRVFragment;
import comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments.CookbookRVFragment;
import comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments.DietRecipesRVFragment;
import comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments.FitnessFavoritesRVFragment;
import comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments.RecipesByIngredientFragment;
import comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments.TrainingRVFragment;
import comalexpolyanskyi.github.foodandhealth.utils.auth.AuthConstant;
import comalexpolyanskyi.github.foodandhealth.utils.holders.AppStyleHolder;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        BaseRVFragment.OnListFragmentInteractionListener, IngredientListFragment.OnFABClickListener {

    public static final String TITLE_KEY = "title";
    private AppStyleHolder appStyleHolder;

    private Toolbar toolbar;
    private View headerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        appStyleHolder = AppStyleHolder.initialize();
        setSupportActionBar(toolbar);
        final NavigationView navigationView = setupNavigationDrawer(toolbar);

        if (savedInstanceState == null) {
            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_all_recipes));
        } else {
            toolbar.setBackgroundColor(ContextCompat.getColor(this, appStyleHolder.getColor()));
            headerLayout.setBackground(ContextCompat.getDrawable(this, appStyleHolder.getDrawable()));

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(appStyleHolder.getTitle());
            }
        }
    }

    private NavigationView setupNavigationDrawer(Toolbar toolbar) {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerLayout = navigationView.getHeaderView(0);
        ((TextView)headerLayout.findViewById(R.id.user_name)).setText(getIntent().getStringExtra(AuthConstant.NAME));
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
        final Fragment fragment = prepareFragment(id);
        if (fragment != null) {
            completeTransaction(fragment, appStyleHolder.getTitle());
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private Fragment prepareFragment(int id) {
        Fragment fragment = null;
        final Bundle bundle = getIntent().getExtras();
        if (id == R.id.nav_i_have) {
            appStyleHolder.defaultInitialize(getString(R.string.i_have));
            fragment = IngredientListFragment.newInstance(bundle);
        } else if (id == R.id.nav_all_recipes) {
            appStyleHolder.defaultInitialize(getString(R.string.all_recipes));
            fragment = AllRecipesRVFragment.newInstance(bundle);
        } else if (id == R.id.nav_favorites) {
            appStyleHolder.defaultInitialize(getString(R.string.favorites));
            fragment = CookbookRVFragment.newInstance(bundle);
        } else if (id == R.id.nav_diets) {
            appStyleHolder.fitnessInitialize(getString(R.string.diets));
            fragment = DietRecipesRVFragment.newInstance(bundle);
        } else if (id == R.id.nav_training_program) {
            appStyleHolder.fitnessInitialize(getString(R.string.training_program));
            fragment = TrainingRVFragment.newInstance(bundle);
        } else if (id == R.id.nav_favorites_program) {
            appStyleHolder.fitnessInitialize(getString(R.string.favorites_program));
            fragment = FitnessFavoritesRVFragment.newInstance(bundle);
        }
        return fragment;
    }

    private void completeTransaction(Fragment fragment, String title) {
        final FragmentTransaction fragmentTransaction;
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();
        toolbar.setBackgroundColor(ContextCompat.getColor(this, appStyleHolder.getColor()));
        headerLayout.setBackground(ContextCompat.getDrawable(this, appStyleHolder.getDrawable()));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onRecipesFragmentInteraction(View v) {
        final ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this, v.findViewById(R.id.imageView), DescriptionActivity.EXTRA_IMAGE);
        final Intent intent = new Intent(this, DescriptionActivity.class);
        intent.putExtra(AuthConstant.TOKEN, getIntent().getStringExtra(AuthConstant.TOKEN));
        intent.putExtra(TITLE_KEY, v.getTag().toString());
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    @Override
    public void onRecipesByIngredient(HashSet<Integer> ingredientsSet) {
        appStyleHolder.defaultInitialize(getString(R.string.i_have));
        final Bundle args = getIntent().getExtras();
        args.putSerializable(RecipesByIngredientFragment.INGREDIENT_ID_SET, ingredientsSet);
        final Fragment fragment = RecipesByIngredientFragment.newInstance(args);
        completeTransaction(fragment, getString(R.string.can_be));
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}