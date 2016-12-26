package comalexpolyanskyi.github.foodandhealth.dao.database.contract;

import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.Table;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbInteger;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbString;

@Table(name = "ARTICLEDESK")
public class ArticleDescription implements CachedTable {

    @dbString
    public static final String NAME = "name";

    @dbString
    public static final String IMAGE_URI = "imageUrl";

    @dbString
    public static final String DESCRIPTION = "description";

    @dbInteger
    public static final String LIKE_COUNT = "likeCount";

    @dbInteger
    public static final String REPOST_COUNT = "repostCount";

    @dbString
    public static String TYPE_NAME = "typeName";

    @dbString
    public static String DIFFICULTY_LEVEL = "difficultyLevel";

    @dbString
    public static String INGREDIENT_LIST = "ingredientList";

    @dbString
    public static String REQUIRED_TIME = "requiredTime";
}
