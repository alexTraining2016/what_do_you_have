package comalexpolyanskyi.github.foodandhealth.dao.database.contract;

import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.Table;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbInteger;

@Table(name = "ARTICLEFAVORITES")
public class Favorites {

    @dbInteger
    String ID = "_id";

    @dbInteger
    String USET_ID = "user_id";

    @dbInteger
    String TYPE = "type";

}
