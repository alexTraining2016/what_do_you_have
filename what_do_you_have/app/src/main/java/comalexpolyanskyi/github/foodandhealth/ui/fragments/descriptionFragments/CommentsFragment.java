package comalexpolyanskyi.github.foodandhealth.ui.fragments.descriptionFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class CommentsFragment extends Fragment {

    public CommentsFragment() {
        super();
    }

    public static PropertiesFragment newInstance() {
        PropertiesFragment fragment = new PropertiesFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
}
