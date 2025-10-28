package dataaccess;

import model.UserData;

public class MySqlUserDAO implements UserDAO{
    @Override
    public UserData register(UserData newUser) throws DataAccessException, AlreadyTakenException {
        return null;
    }

    @Override
    public UserData getUser(UserData existingUser) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }
}
