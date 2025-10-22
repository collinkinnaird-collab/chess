package service;

import Service.UserService;
import Service.ClearService;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;



public class UserServiceTest {


    static UserService service;
    static ClearService clear;
    static AuthDAO DAOauth;
    static UserDAO DAOuser;


    @BeforeEach
    void begin() throws DataAccessException {
        DAOauth = new MemoryAuthDAO();
        DAOuser = new MemoryUserDAO();
        service = new UserService(DAOuser, DAOauth);
        clear = new ClearService(DAOuser, new MemoryGameDAO()
                , DAOauth);
        clear.clearAll();

    }

    @Test
    void registerSuccess() throws DataAccessException{

        UserData user = new UserData("John", "flip88", "John.Doe@gmail.com");

        AuthData test = service.register(user);
        Assertions.assertEquals(DAOauth.getAuth(test.username()), test);


    }

    @Test
    void loginSuccess() throws  DataAccessException{

        var user = new UserData("John", "Doe240", "John.Doe@gmail.com");
        AuthData test = service.register(user);
        AuthData other  = service.logIn(user);
        Assertions.assertEquals(test.username(), other.username());

    }

    @Test
    void logoutSuccess() throws  DataAccessException{

        var user = new UserData("John", "Doe240", "John.Doe@gmail.com");
        AuthData test = service.register(user);
        AuthData other  = service.logIn(user);
        service.logout(other.username());
        Assertions.assertTrue(DAOauth.deleteAuth(other));

    }

    void assertUserEqual (UserData expected, UserData actual){
        assertEquals(expected.email(), actual.email());
        assertEquals(expected.password(), actual.password());
        assertEquals(expected.username(), actual.username());
    }



}
