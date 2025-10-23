package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GameServiceTests {


    static UserService service;
    static ClearService clear;
    static GameService gameService;
    static AuthDAO DAOauth;
    static UserDAO DAOuser;
    static GameDAO DAOgame;


    @BeforeEach
    void begin() throws DataAccessException {
        DAOauth = new MemoryAuthDAO();
        DAOuser = new MemoryUserDAO();
        DAOgame = new MemoryGameDAO();
        service = new UserService(DAOuser, DAOauth);
        clear = new ClearService(DAOuser, DAOgame
                , DAOauth);
        gameService = new GameService(DAOuser, DAOgame, DAOauth);
        clear.clearAll();

    }

    @Test
    void createGame() throws DataAccessException{



        String authToken = UUID.randomUUID().toString();
        AuthData loginUserAuth = new AuthData("SWAG", authToken);

        int myGame = gameService.createGame(loginUserAuth.authToken(), "EPIC_GAME");
        //Assertions.assertEquals(DAOgame.createGame(myGame), myGame.gameID());


    }



}
