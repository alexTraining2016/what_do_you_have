package comalexpolyanskyi.github.foodandhealth.utils.auth;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AuthData implements Serializable {

    @SerializedName("token")
    private String token;

    @SerializedName("name")
    private String name;

    @SerializedName("uid")
    private String id;

    public AuthData(String token, String name, String id) {

        this.token = token;
        this.name = name;
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public boolean isEmpty() {
        return token.equals(AuthConstant.EMPTY) || name.equals(AuthConstant.EMPTY) || id.equals(AuthConstant.EMPTY);
    }
}
