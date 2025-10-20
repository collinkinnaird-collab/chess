package dataaccess;

import model.UserData;

public class MemoryDataAccess implements AuthDAO, GameDAO, UserDAO{


    @Override
    public UserData register(UserData newUser) {
        return null;
    }

    @Override
    public UserData getUser(UserData existingUser) {
        return null;
    }
}
