package comalexpolyanskyi.github.foodandhealth.dao.dataObject;


public class ParametersInformationRequest {

    private String url;
    private String selectParameters;

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