package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GameServiceTests {


    static UserService service;
    static ClearService clear;
    static GameService gameService;
    static AuthDAO daoAuth;
    static UserDAO daoUser;
    static GameDAO daoGame;


    @BeforeEach
    void begin() throws DataAccessException {
        daoAuth = new MemoryAuthDAO();
        daoUser = new MemoryUserDAO();
        daoGame = new MemoryGameDAO();
        service = new UserService(daoUser, daoAuth);
        clear = new ClearService(daoUser, daoGame
                , daoAuth);
        gameService = new GameService(daoUser, daoGame, daoAuth);
        clear.clearAll();

    }

    @Test
    void createGame() throws DataAccessException{



        String authToken = UUID.randomUUID().toString();
        AuthData loginUserAuth = new AuthData("SWAG", authToken);

        int myGame = gameService.createGame(loginUserAuth.authToken(), "EPIC_GAME");
        Assertions.assertEquals("EPIC_GAME", daoGame.createGame("EPIC_GAME"));


    }



}
