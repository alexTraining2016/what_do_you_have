package comalexpolyanskyi.github.foodandhealth.ui.fragments.descriptionFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import comalexpolyanskyi.github.foodandhealth.R;
import comalexpolyanskyi.github.foodandhealth.dao.dataObject.ArticleDO;

public class PropertiesFragment extends Fragment implements PagesCommunicator<ArticleDO> {

    private int color = 0;
    private ArticleDO data;
    private ImageView imageTime, imageDifficultyLevel, imageType;
    private TextView textTime, textDifficultyLevel, textType, textIngredient;

    public PropertiesFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        final View view = inflater.inflate(R.layout.fragment_properties_desctiption, container, false);
        imageTime = (ImageView) view.findViewById(R.id.image_time);
        imageDifficultyLevel = (ImageView) view.findViewById(R.id.image_difficulty_level);
        imageType = (ImageView) view.findViewById(R.id.image_type);
        textTime = (TextView) view.findViewById(R.id.text_time);
        textDifficultyLevel = (TextView) view.findViewById(R.id.text_difficulty_level) ;
        textType = (TextView) view.findViewById(R.id.text_type);
        textIngredient = (TextView) view.findViewById(R.id.ingredient_list);
        refreshColor();
        refreshData();

        return view;
    }

    @Override
    public void updateData(ArticleDO data) {
        this.data = data;
        refreshData();
    }

    @Override
    public void setupColor(int color) {
        this.color = color;
        refreshColor();
    }

    private void refreshData() {
        if (data != null) {
            textIngredient.setText(data.getIngredientList());
            textTime.setText(data.getRequiredTime());
            textDifficultyLevel.setText(data.getDifficultyLevel());
            textType.setText(data.getTypeName());
        }
    }

    private void refreshColor() {
        if (color != 0) {
            imageTime.setColorFilter(color);
            imageDifficultyLevel.setColorFilter(color);
            imageType.setColorFilter(color);
        }
    }
}
