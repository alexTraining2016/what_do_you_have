package comalexpolyanskyi.github.foodandhealth.dao.dataObject;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import comalexpolyanskyi.github.foodandhealth.dao.database.contract.Ingredient;

public class IngredientItemDO {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    public IngredientItemDO(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(Ingredient.ID));
        this.name = cursor.getString(cursor.getColumnIndex(Ingredient.NAME));
        this.image = cursor.getString(cursor.getColumnIndex(Ingredient.IMAGE));
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }
}
