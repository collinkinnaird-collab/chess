package model;

import com.google.gson.*;

public record UserData(String username, String password, String email) {

    public UserData registerNewUser(String newUsername, String newPassword, String newEmail) {
        return new UserData (newUsername, newPassword, newEmail);
    }

    public String toString() {
        return new Gson().toJson(this);
    }

}