package service;

import Service.ClearService;
import Service.GameService;
import Service.UserService;
import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import passoff.chess.game.GameStatusTests;

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


    void assertUserEqual (UserData expected, UserData actual){
        assertEquals(expected.email(), actual.email());
        assertEquals(expected.password(), actual.password());
        assertEquals(expected.username(), actual.username());
    }



}
