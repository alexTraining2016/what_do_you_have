package comalexpolyanskyi.github.foodandhealth.ui.fragments.descriptionFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ArticleDO;
import comalexpolyanskyi.github.foodandhealth.dao.database.contract.ArticleDescription;

public class ArticleFragment extends Fragment implements PagesCommunicator<ArticleDO> {

    private TextView description;
    private ArticleDO data;

    public ArticleFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);
        final View view = inflater.inflate(R.layout.fragment_article_description, container, false);
        description = (TextView) view.findViewById(R.id.description);
        refreshView();
        return view;
    }

    @Override
    public void updateData(ArticleDO data) {
        this.data = data;
        refreshView();
    }

    @Override
    public void setupColor(int color) {
    }

    private void refreshView() {
        if (data != null && description != null) {
            description.setText(data.getDescription());
        }
    }

}
