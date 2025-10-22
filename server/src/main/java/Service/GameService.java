package Service;

import dataaccess.*;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public class GameService {


    private final UserDAO dataAccessUser;
    private final GameDAO dataAccessGame;
    private final AuthDAO dataAccessAuth;

    public GameService (UserDAO dataAccessUser, GameDAO dataAccessGame, AuthDAO dataAccessAuth){
        this.dataAccessUser = dataAccessUser;
        this.dataAccessGame = dataAccessGame;
        this.dataAccessAuth = dataAccessAuth;
    }

    public int createGame(String authorization, String gameName) throws DataAccessException{

        AuthData gameCreatorUser = dataAccessAuth.getName(authorization);

        int gameID = dataAccessGame.createGame(gameName);

        return gameID;

    }

    public Collection<GameData> listOfGames(String authToken) throws DataAccessException{
        AuthData gameCreatorUser = dataAccessAuth.getName(authToken);

        return dataAccessGame.listGames();
    }

    public void joinAGame(String auth, int ID, String color) throws DataAccessException, BadRequestException
                                                                  , AlreadyTakenException {

        if(color == null){
            throw new BadRequestException("no");
        }


        AuthData confirm = dataAccessAuth.getName(auth);

        GameData foundGame = dataAccessGame.getGame(ID);
        String white = foundGame.whiteUsername();
        String black = foundGame.blackUsername();
        if(color.equals("WHITE"))
        {
            if(white != null)
            {
                throw new AlreadyTakenException("already taken");
            }
            white = confirm.username();
        } else if (color.equals("BLACK")){
            if(black != null)
            {
                throw new AlreadyTakenException("already taken");
            }

            black = confirm.username();

        }

        GameData updatedGame = new GameData(ID, white, black, foundGame.gameName(), foundGame.game());

        dataAccessGame.updateGame(updatedGame);



    }
}
