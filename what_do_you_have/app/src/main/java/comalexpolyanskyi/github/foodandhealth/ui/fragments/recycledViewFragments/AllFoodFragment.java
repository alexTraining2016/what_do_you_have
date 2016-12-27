package comalexpolyanskyi.github.foodandhealth.ui.fragments.recycledViewFragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.KindFood;
import comalexpolyanskyi.github.foodandhealth.mediators.InteractionContract;
import comalexpolyanskyi.github.foodandhealth.mediators.fragmentMediators.AllFoodFragmentMediator;
import comalexpolyanskyi.github.foodandhealth.utils.adapters.ViewPagerAdapter;

public class AllFoodFragment extends Fragment implements InteractionContract.RequiredView<Cursor> {

    private InteractionContract.Mediator<String> mediator;
    private Cursor cursor;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public AllFoodFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void bindPresenter() {
        this.mediator = new AllFoodFragmentMediator(this);
        if (cursor == null) {
            mediator.loadData();
        } else {
            bindTab();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mediator.onDestroy();
        mediator = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_all_food, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.food_kind_viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.food_kind_tabs);
        bindPresenter();
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void bindTab() {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        cursor.moveToFirst();
        do {
            int id = cursor.getInt(cursor.getColumnIndex(KindFood.ID));
            final RecipesByKindRVFragment recipesByKindRVFragment = RecipesByKindRVFragment.newInstance(id);
            final String tabName = cursor.getString(cursor.getColumnIndex(KindFood.NAME));
            adapter.addFragment(recipesByKindRVFragment, tabName);
        } while (cursor.moveToNext());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void returnData(Cursor cursor) {
        this.cursor = cursor;
        bindTab();
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void returnError(String message) {

    }

    @Override
    public void showProgress(boolean isInProgress) {

    }
}
