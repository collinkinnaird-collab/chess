package dataaccess;

import dataaccess.DataAccessException;
import model.*;

public interface UserDAO {

    UserData register(UserData newUser) throws DataAccessException;

    UserData getUser(UserData existingUser) throws  DataAccessException;

}
