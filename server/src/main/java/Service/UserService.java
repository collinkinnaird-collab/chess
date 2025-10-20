package Service;
import dataaccess.UserDAO;
import dataaccess.DataAccessException;
import model.*;

//import dataaccess.DataAccess;
//import exception.ResponseException;


public class UserService {

    private final UserDAO dataAccessAuth;

    public UserService(UserDAO dataAccessAuth) {
        this.dataAccessAuth = dataAccessAuth;
    }

    public UserData register(UserData registerRequest) throws DataAccessException{

        if(dataAccessAuth.getUser(registerRequest).username() != null)
        {
            throw new DataAccessException("userName already exists");
        }

        return dataAccessAuth.register(registerRequest);

    }

}
