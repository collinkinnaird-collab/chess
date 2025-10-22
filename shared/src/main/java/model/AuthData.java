package model;
import java.util.UUID;

import com.google.gson.Gson;

public record AuthData(String username, String authToken) {


//    public AuthData newAuthToken(UserData user){
//
//
//    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
