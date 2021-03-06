package comalexpolyanskyi.github.foodandhealth.dao.dataObject;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticleByIngredientDO extends ArticleListItemDO {

    public ArticleByIngredientDO(Cursor cursor) {
        super(cursor);
    }

    public List<IngredientIdDO> getIngredientsId() {
        return ingredientsId;
    }

    @SerializedName("ingredients_id")
    private List<IngredientIdDO> ingredientsId;
}
