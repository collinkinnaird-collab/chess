package Service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import dataaccess.DataAccessException;
import model.GameData;

public class GameService {


    private final UserDAO dataAccessUser;
    private final GameDAO dataAccessGame;
    private final AuthDAO dataAccessAuth;

    public GameService (UserDAO dataAccessUser, GameDAO dataAccessGame, AuthDAO dataAccessAuth){
        this.dataAccessUser = dataAccessUser;
        this.dataAccessGame = dataAccessGame;
        this.dataAccessAuth = dataAccessAuth;
    }

    public int createGame(String authorization, GameData myGame) throws DataAccessException{

        AuthData gameCreatorUser = dataAccessAuth.getAuth(authorization);

        dataAccessGame.createGame(myGame);

        return myGame.gameID();

    }

    public void listOfGames(String authToken) throws DataAccessException{
        AuthData gameCreatorUser = dataAccessAuth.getAuth(authToken);

        dataAccessGame.listGames();
    }
}
