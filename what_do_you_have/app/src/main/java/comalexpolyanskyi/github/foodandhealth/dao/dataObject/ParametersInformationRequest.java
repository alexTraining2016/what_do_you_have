package comalexpolyanskyi.github.foodandhealth.dao.dataObject;


import android.content.ContentValues;

public class ParametersInformationRequest {

    private String url;
    private String selectParameters;
    private String updateParameters;
    private ContentValues updateContent;

    public ContentValues getUpdateContent() {
        return updateContent;
    }

    public String getUpdateParameters() {
        return updateParameters;
    }

    public ParametersInformationRequest(String url, String selectParameters, String updateParameters, ContentValues updateContent) {
        this.url = url;
        this.selectParameters = selectParameters;
        this.updateParameters = updateParameters;
        this.updateContent = updateContent;
    }

    public ParametersInformationRequest(String url, String selectParameters) {
        this.url = url;
        this.selectParameters = selectParameters;
    }

    public String getUrl() {
        return url;
    }

    public String getSelectParameters() {
        return selectParameters;
    }

}
