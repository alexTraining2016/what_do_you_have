package comalexpolyanskyi.github.foodandhealth.dao.dataObjects;

import com.google.gson.annotations.SerializedName;

public class IngredientItemDO {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    private boolean isSelected = false;

    public IngredientItemDO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
