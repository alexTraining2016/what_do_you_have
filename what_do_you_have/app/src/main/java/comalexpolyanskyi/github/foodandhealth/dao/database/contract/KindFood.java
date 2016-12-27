package comalexpolyanskyi.github.foodandhealth.dao.database.contract;

import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.Table;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbString;

@Table(name = "KINDFOOD")
public class KindFood implements CachedTable {

    @dbString
    public static final String NAME = "type_name";

}
