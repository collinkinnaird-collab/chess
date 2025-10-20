package service;

import Service.UserService;
import dataaccess.DataAccessException;
import chess.ChessGame;
import dataaccess.MemoryDataAccess;
import model.UserData;
import org.junit.jupiter.api.*;
import passoff.model.*;
import passoff.server.TestServerFacade;
import server.Server;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;


public class UserServiceTest {

    static final UserService service = new UserService(new MemoryDataAccess());

    @Test
    void register() throws DataAccessException{

        var user = new UserData("John", "flip88", "John.Doe@gmail.com");
        user = service.register(user);



    }

    void login() throws  DataAccessException{

        var user = new UserData("John", "Doe240", "John.Doe@gmail.com");
        //user = service.login();



    }




}
