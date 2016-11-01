package comalexpolyanskyi.github.foodandhealth.models.dataObjects;

public class IngredientItemDO {
    private int id;
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
