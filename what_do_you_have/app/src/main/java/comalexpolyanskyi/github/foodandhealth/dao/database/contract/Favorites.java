package comalexpolyanskyi.github.foodandhealth.dao.database.contract;

import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.Table;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbInteger;

@Table(name = "ARTICLEFAVORITES")
public class Favorites {

    @dbInteger
    public static final String ID = "_id";

    @dbInteger
    public static final String USER_ID = "user_id";

    @dbInteger
    public static final String ART_ID = "art_id";

    @dbInteger
    public static final String ISFAVORITES = "fav";

    @dbInteger
    public static final String ISLIKE = "like";



}
