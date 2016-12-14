package comalexpolyanskyi.github.foodandhealth.dao.dataObject;

import com.google.gson.annotations.SerializedName;

public class IngredientItemDO {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    public IngredientItemDO(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
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
