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

    UserDAO mydao;

    static final UserService service = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
    static final ClearService clear = new ClearService(new MemoryUserDAO(), new MemoryGameDAO()
                                                     , new MemoryAuthDAO());

//     static private Server chessServer;

//    @BeforeAll
//    static void startServer() {
//        var service = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
//        chessServer = new Server(service);
//        chessServer.run(8080);
//        var url = "http://localhost:" + chessServer.port();
//
//    }

    @BeforeEach
    void clear() throws DataAccessException{
        clear.clearAll();
    }

    @Test
    void register() throws DataAccessException{

        var user = new UserData("John", "flip88", "John.Doe@gmail.com");

        UserData test = mydao.register(user);

        assertUserEqual(user, test);




    }

    void login() throws  DataAccessException{

        var user = new UserData("John", "Doe240", "John.Doe@gmail.com");
        //user = service.login();



    }

    void assertUserEqual (UserData expected, UserData actual){
        assertEquals(expected.email(), actual.email());
        assertEquals(expected.password(), actual.password());
        assertEquals(expected.username(), actual.username());
    }



}
