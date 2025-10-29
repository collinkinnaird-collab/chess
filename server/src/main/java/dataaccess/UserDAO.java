package dataaccess;

import model.*;

public interface UserDAO {

    UserData register(UserData newUser) throws DataAccessException, AlreadyTakenException;

    UserData getUser(UserData existingUser) throws DataAccessException;

    void clear() throws DataAccessException;

}
