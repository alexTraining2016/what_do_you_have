package comalexpolyanskyi.github.foodandhealth.dao.dataObjects;

import android.content.ContentValues;

import java.util.HashMap;

/**
 * Created by Алексей on 03.11.2016.
 */

public class ParametersInformationRequest {

    private String url;
    private ContentValues insertParameters, updateParameters;
    private HashMap<String, String> deleteParameters, selectParameters;

    public ParametersInformationRequest(String url, HashMap<String, String> selectParameters, ContentValues insertParameters, ContentValues updateParameters, HashMap<String, String> deleteParameters) {
        this.url = url;
        this.selectParameters = selectParameters;
        this.insertParameters = insertParameters;
        this.updateParameters = updateParameters;
        this.deleteParameters = deleteParameters;
    }

    public String getUrl() {
        return url;
    }

    public ContentValues getInsertParameters() {
        return insertParameters;
    }

    public HashMap<String, String> getDeleteParameters() {
        return deleteParameters;
    }

    public HashMap<String, String> getSelectParameters() {
        return selectParameters;
    }

    public ContentValues getUpdateParameters() {
        return updateParameters;
    }
}
