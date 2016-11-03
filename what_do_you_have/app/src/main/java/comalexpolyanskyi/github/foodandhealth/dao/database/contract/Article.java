package comalexpolyanskyi.github.foodandhealth.dao.database.contract;

import android.provider.BaseColumns;

import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.Table;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbInteger;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbString;

@Table(name = "ARTICLE")
public class Article implements BaseColumns {

    @dbInteger
    public static final String ID = "id";

    @dbString
    public static final String NAME = "name";

    @dbInteger
    public static final String TYPE = "type";

    @dbString
    public static final String DESCRIPTION = "description";

    @dbString
    public static final String IMAGE_URI = "imageUrl";

    @dbInteger
    public static final String LIKE_COUNT = "likeCount";

    @dbInteger
    public static final String REPOST_COUNT = "repostCount";
}
