package Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.*;



public class ClearService {

    private final UserDAO dataAccessUser;
    private final GameDAO dataAccessGame;
    private final AuthDAO dataAccessAuth;

    public ClearService(UserDAO dataAccessUser, GameDAO dataAccessGame, AuthDAO dataAccessAuth){
        this.dataAccessUser = dataAccessUser;
        this.dataAccessGame = dataAccessGame;
        this.dataAccessAuth = dataAccessAuth;
    }


    public void clearAll() throws DataAccessException {
        clearAuth();
        clearGame();
        clearUser();
    }



    public void clearAuth() throws  DataAccessException {
        dataAccessAuth.clearTotal();
    }

    public void clearGame() throws DataAccessException {
        dataAccessGame.clear();
    }

    public void clearUser() throws DataAccessException {
       dataAccessUser.clear();
    }



}
