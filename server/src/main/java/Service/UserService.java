package Service;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import dataaccess.DataAccessException;
import model.*;
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

    public AuthData register(UserData registerRequest) throws DataAccessException{

        String authToken = UUID.randomUUID().toString();
        AuthData userAuth = new AuthData(registerRequest.username(), authToken);

        dataAccessUser.register(registerRequest);

        dataAccessAuth.newAuth(userAuth);

        return userAuth;

    }

    public AuthData logIn(UserData loginRequest) throws DataAccessException{

        String authToken = UUID.randomUUID().toString();
        AuthData loginUserAuth = new AuthData(loginRequest.username(), authToken);

        dataAccessUser.getUser(loginRequest);

        dataAccessAuth.newAuth(loginUserAuth);

        return loginUserAuth;

    }

    public void logout(String logoutRequest) throws DataAccessException{

        AuthData userAuth = dataAccessAuth.getAuth(logoutRequest);

        if(dataAccessAuth.deleteAuth(userAuth));
        {
            System.out.println("200");
        }




    }

}
