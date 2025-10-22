package dataaccess;

import dataaccess.DataAccessException;
import model.UserData;
import model.AuthData;
import org.eclipse.jetty.server.Authentication;

import java.util.*;

public class MemoryUserDAO implements UserDAO {

    private final Collection<UserData> verifiedUser = new ArrayList<>();

    @Override
    public UserData register(UserData newUser) throws DataAccessException, AlreadyTakenException {

        try {
            getUser(newUser);

        } catch (DataAccessException e) {
            verifiedUser.add(newUser);
            return newUser;
        }

        throw new AlreadyTakenException("user Already exists");
    }

    @Override
    public UserData getUser(UserData existingUser) throws DataAccessException{

        boolean doesExist = false;
        for (UserData userName : verifiedUser) {

            if (existingUser.username().equals(userName.username())) {

                doesExist = true;
                break;
            }

        }
        if(doesExist) {
            return existingUser;
        }
        else{
            throw new DataAccessException("user does not exist");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        verifiedUser.clear();
    }

}
