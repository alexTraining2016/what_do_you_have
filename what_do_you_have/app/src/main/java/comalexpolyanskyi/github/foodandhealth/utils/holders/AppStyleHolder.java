package comalexpolyanskyi.github.foodandhealth.utils.holders;


import comalexpolyanskyi.github.foodandhealth.R;

public class AppStyleHolder {

    private static AppStyleHolder styleHolder = null;

    private int bgDrawable;
    private int theme = 0;
    private int color = 0;
    private int drawable = R.drawable.food;
    private String title = null;

    public int getTheme() {
        return theme;
    }

    public int getColor() {
        return color;
    }

    public int getDrawable() {
        return drawable;
    }

    public String getTitle() {
        return title;
    }

    private AppStyleHolder(){
        defaultInitialize(ContextHolder.getContext().getString(R.string.all_recipes));
    }

    public static AppStyleHolder initialize(){
        if(styleHolder == null){
            styleHolder = new AppStyleHolder();
        }
        return styleHolder;
    }

    public void defaultInitialize(String title){
        this.title = title;
        theme = R.style.FoodAppTheme;
        color = R.color.colorPrimaryFood;
        drawable = R.drawable.food;
        bgDrawable = R.drawable.food_bg;
    }

    public void fitnessInitialize(String title){
        theme = R.style.TrainingAppTheme;
        this.title = title;
        color = R.color.colorPrimaryTraining;
        drawable = R.drawable.fitness;
        bgDrawable = R.drawable.sport_bg;
    }

    public int getBgDrawable() {
        return bgDrawable;
    }
}
