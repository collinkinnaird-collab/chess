package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;



public class UserServiceTest {


    static UserService service;
    static ClearService clear;
    static AuthDAO daoAuth;
    static UserDAO daoUser;


    @BeforeEach
    void begin() throws DataAccessException {
        daoAuth = new MemoryAuthDAO();
        daoUser = new MemoryUserDAO();
        service = new UserService(daoUser, daoAuth);
        clear = new ClearService(daoUser, new MemoryGameDAO()
                , daoAuth);
        clear.clearAll();

    }

    @Test
    @DisplayName("Create Valid User")
    void registerSuccess() throws DataAccessException, AlreadyTakenException {

        UserData user = new UserData("John", "flip88", "John.Doe@gmail.com");

        AuthData test = service.register(user);
        Assertions.assertEquals(daoAuth.getAuth(test.authToken()), test);


    }
    @Test
    @DisplayName("Create Invalid User")
    void registerFailure() throws DataAccessException, AlreadyTakenException {
        UserData user = new UserData("John", "flip88", "John.Doe@gmail.com");


        service.register(user);
        Assertions.assertThrows(AlreadyTakenException.class, () -> service.register(user));


    }

    @Test
    void loginSuccess() throws DataAccessException, AlreadyTakenException {

        var user = new UserData("John", "Doe240", "John.Doe@gmail.com");
        AuthData test = service.register(user);
        service.logout(test.authToken());
        AuthData other  = service.logIn(user);
        Assertions.assertEquals(test.username(), other.username());

    }

    @Test
    void loginFail() throws DataAccessException{
        UserData user = new UserData("", "flip88", "John.Doe@gmail.com");

        service.logIn(user);
        Assertions.assertThrows(DataAccessException.class, () -> service.register(user));
    }

    @Test
    void logoutSuccess() throws DataAccessException, AlreadyTakenException {

        var user = new UserData("John", "Doe240", "John.Doe@gmail.com");
        AuthData test = service.register(user);
        AuthData other  = service.logIn(user);
        service.logout(other.authToken());
        Assertions.assertFalse(daoAuth.deleteAuth(other));

    }

    @Test
    void logoutfail() throws DataAccessException, AlreadyTakenException {

        var user = new UserData("John", "Doe240", "John.Doe@gmail.com");
        AuthData test = service.register(user);
        AuthData other  = service.logIn(user);
        service.logout(other.authToken());
        service.logout((other.authToken()));

    }


}
