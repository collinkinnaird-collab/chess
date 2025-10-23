package model;
import java.util.UUID;

import com.google.gson.Gson;

public record AuthData(String username, String authToken) {


    public String toString() {
        return new Gson().toJson(this);
    }
}
