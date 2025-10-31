package dataaccess;

import model.*;

public interface UserDAO {

    UserData register(UserData newUser) throws DataAccessException, AlreadyTakenException;

    UserData getUser(UserData existingUser) throws Exception;

    void clear() throws DataAccessException;

}
