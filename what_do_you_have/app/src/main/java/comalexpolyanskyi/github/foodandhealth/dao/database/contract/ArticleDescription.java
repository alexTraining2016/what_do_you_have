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

    @dbInteger
    public static final String IS_LIKE = "isLike";

    @dbInteger
    public static final String IS_REPOST = "isRepost";

    @dbInteger
    public static final String USER_ID = "userId";

}
