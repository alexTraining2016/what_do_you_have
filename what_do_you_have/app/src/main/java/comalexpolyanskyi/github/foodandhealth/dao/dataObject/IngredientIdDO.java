package comalexpolyanskyi.github.foodandhealth.dao.dataObject;

import com.google.gson.annotations.SerializedName;

public class IngredientIdDO {

    public int getId() {
        return id;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    @SerializedName("id")
    private int id;

    @SerializedName("ingredient_id")
    private int ingredientId;
}
