package comalexpolyanskyi.github.foodandhealth.dao.dataObject;

import android.content.ContentValues;

public class ParametersInformationRequest {

    private String url;
    private String selectParameters;
    private ContentValues updateParameters;

    public ParametersInformationRequest(String url, String selectParameters, ContentValues updateParameters) {
        this.url = url;
        this.selectParameters = selectParameters;
        this.updateParameters = updateParameters;
    }

    public String getUrl() {
        return url;
    }

    public String getSelectParameters() {
        return selectParameters;
    }

    public ContentValues getUpdateParameters() {
        return updateParameters;
    }

}
