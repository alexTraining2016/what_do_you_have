package comalexpolyanskyi.github.foodandhealth.models.pojo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Алексей on 22.10.2016.
 */

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
