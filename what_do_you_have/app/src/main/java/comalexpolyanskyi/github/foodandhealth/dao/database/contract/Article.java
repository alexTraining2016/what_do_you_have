package comalexpolyanskyi.github.foodandhealth.dao.database.contract;

import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.Table;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbInteger;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbString;

@Table(name = "ARTICLE")
public class Article implements CachedTable {

    @dbString
    public static final String NAME = "name";

    @dbString
    public static final String SEARCH_NAME = "sname";

    @dbString
    public static final String IMAGE_URI = "imageUrl";

    @dbInteger
    public static final String TYPE = "type";

}
