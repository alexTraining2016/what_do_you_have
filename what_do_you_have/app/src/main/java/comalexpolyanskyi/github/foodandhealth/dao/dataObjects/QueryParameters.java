package comalexpolyanskyi.github.foodandhealth.dao.dataObjects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class QueryParameters implements Serializable {
    private int viewType;
    private HashMap<String, String> params;

    public QueryParameters(int viewType, HashMap<String, String> params) {
        this.viewType = viewType;
        this.params = params;
    }

    public int getViewType() {
        return viewType;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
