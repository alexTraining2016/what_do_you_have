package comalexpolyanskyi.github.foodandhealth.presenter;

/**
 * Created by Алексей on 01.11.2016.
 */

public interface ArticlesTypeRequest {
    int ALL_FOOD_RECIPES = 0;
    int FOOD_RECIPES_BY_INGREDIENT = 1;
    int FAVORITES_FOOD_RECIPES = 2;
    int ALL_TRAINING_RECIPES = 3;
    int ALL_DIET_RECIPES = 4;
    int FAVORITES_TRAINING_AND_DIET_RECIPES = 5;
}
