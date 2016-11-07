package comalexpolyanskyi.github.foodandhealth.dao.database.contract;

import comalexpolyanskyi.github.foodandhealth.dao.database.annotations.dbLong;

public interface CachedTable {

    @dbLong
    String RECORDING_TIME = "recordingTime";

    @dbLong
    String AGING_TIME = "agingTime";
}
