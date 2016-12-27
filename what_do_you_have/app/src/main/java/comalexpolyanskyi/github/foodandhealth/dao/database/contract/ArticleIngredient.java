package comalexpolyanskyi.github.foodandhealth.dao.database.contract;

import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.Table;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbInteger;

@Table(name = "ARTICLEINGREDIENT")
public class ArticleIngredient {

    @dbInteger
    public static final String ID = "_id";

    @dbInteger
    public static final String ARTICLE_ID = "article_id";

    @dbInteger
    public static final String INGREDIENT_ID = "ingredient_Id";
}
