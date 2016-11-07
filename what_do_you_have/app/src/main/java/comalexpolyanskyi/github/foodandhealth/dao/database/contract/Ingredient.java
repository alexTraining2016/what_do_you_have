package comalexpolyanskyi.github.foodandhealth.dao.database.contract;

import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.Table;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbInteger;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbString;

@Table(name = "INGREDIENTS")
public class Ingredient implements CachedTable {

    @dbInteger
    public static final String ID = "_id";

    @dbString
    public static final String NAME = "name";

}
