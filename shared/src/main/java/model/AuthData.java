package model;
import java.util.UUID;

import com.google.gson.Gson;

public record AuthData(String authToken, String username) {


//    public AuthData newAuthToken(UserData user){
//
//
//    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
