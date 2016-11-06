package comalexpolyanskyi.github.foodandhealth.dao.database.contract;

import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.Table;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbInteger;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbLong;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbString;

@Table(name = "ARTICLEDESK")
public class ArticleDescription {

    @dbInteger
    public static final String ID = "id";

    @dbString
    public static final String NAME = "name";

    @dbString
    public static final String DESCRIPTION = "description";

    @dbString
    public static final String IMAGE_URI = "imageUrl";

    @dbInteger
    public static final String LIKE_COUNT = "likeCount";

    @dbInteger
    public static final String REPOST_COUNT = "repostCount";

    @dbLong
    public static final String RECORDING_TIME = "recordingTime";

    @dbLong
    public static final String AGING_TIME = "agingTime";
}
