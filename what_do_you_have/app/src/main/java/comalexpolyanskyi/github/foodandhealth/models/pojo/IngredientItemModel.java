package comalexpolyanskyi.github.foodandhealth.models.pojo;

/**
 * Created by Алексей on 29.10.2016.
 */

public class IngredientItemModel {
    private int id;
    private String name;
    private boolean isSelected = false;

    public IngredientItemModel(int id, String name) {
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
