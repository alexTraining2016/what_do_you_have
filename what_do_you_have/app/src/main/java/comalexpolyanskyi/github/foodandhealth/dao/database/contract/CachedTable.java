package comalexpolyanskyi.github.foodandhealth.dao.database.contract;

import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbInteger;
import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbLong;

public interface CachedTable {

    @dbInteger
    String ID = "_id";

    @dbLong
    String RECORDING_TIME = "recordingTime";

    @dbLong
    String AGING_TIME = "agingTime";
}
