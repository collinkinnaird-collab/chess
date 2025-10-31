package service;
import dataaccess.*;
import model.*;

import java.util.Objects;
import java.util.UUID;

//import dataaccess.DataAccess;
//import exception.ResponseException;


public class UserService {

    private final UserDAO dataAccessUser;
    private final AuthDAO dataAccessAuth;

    public UserService(UserDAO dataAccessUser, AuthDAO dataAccessAuth) {
        this.dataAccessUser = dataAccessUser;
        this.dataAccessAuth = dataAccessAuth;
    }

    public AuthData register(UserData registerRequest) throws DataAccessException, AlreadyTakenException {

        String authToken = UUID.randomUUID().toString();
        AuthData userAuth = new AuthData(registerRequest.username(), authToken);

        dataAccessUser.register(registerRequest);

        dataAccessAuth.newAuth(userAuth);

        return userAuth;

    }

    public AuthData logIn(UserData loginRequest) throws Exception {


            String authToken = UUID.randomUUID().toString();
            AuthData loginUserAuth = new AuthData(loginRequest.username(), authToken);

            UserData test = dataAccessUser.getUser(loginRequest);

            if(test.username().equals("PPPPPPPPPPPPPPP"))
            {
                throw new DataAccessException("bad Password");
            }


            dataAccessAuth.newAuth(loginUserAuth);

            return loginUserAuth;

    }

    public void logout(String logoutRequest) throws Exception {

        Boolean success = false;
        AuthData userAuth = dataAccessAuth.getName(logoutRequest);

        success = dataAccessAuth.deleteAuth(userAuth);

    }

}
