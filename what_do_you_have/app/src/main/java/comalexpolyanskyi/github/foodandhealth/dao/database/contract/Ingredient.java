package comalexpolyanskyi.github.foodandhealth.dao.database.contract;

import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.Table;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbString;

@Table(name = "INGREDIENTS")
public class Ingredient implements CachedTable {

    @dbString
    public static final String NAME = "name";

}
