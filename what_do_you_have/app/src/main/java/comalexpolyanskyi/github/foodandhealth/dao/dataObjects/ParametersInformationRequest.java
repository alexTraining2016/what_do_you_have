package comalexpolyanskyi.github.foodandhealth.dao.dataObjects;

import android.content.ContentValues;

import comalexpolyanskyi.github.foodandhealth.dao.database.contract.CachedTable;

public class ParametersInformationRequest<T extends CachedTable> {

    private String url;
    private String[]  selectParameters;
    private ContentValues updateParameters;

    public ParametersInformationRequest(String url, String[] selectParameters, ContentValues updateParameters) {
        this.url = url;
        this.selectParameters = selectParameters;
        this.updateParameters = updateParameters;
    }

    public String getUrl() {
        return url;
    }

    public String[] getSelectParameters() {
        return selectParameters;
    }

    public ContentValues getUpdateParameters() {
        return updateParameters;
    }

}
