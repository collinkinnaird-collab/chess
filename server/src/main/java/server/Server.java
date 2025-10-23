package server;

import Service.ClearService;
import Service.GameService;
import Service.UserService;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import model.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class Server {

    private final Javalin javalin;
    private final UserService userService;
    private final ClearService clearService;
    private final GameService gameService;

    public Server(UserService userService, ClearService clearService, GameService gameService) {

        this.userService = userService;
        this.clearService = clearService;
        this.gameService = gameService;

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
            .post("/user", this::registerUser)
            .post("/session", this::login)
                .delete("/session", this::logout)
                .post("/game", this::createGame)
                .get("/game", this::listGames)
                .put("/game", this::joinGame)
                .delete("/db", this::clear)
                .exception(Exception.class, this::exceptionHandler);




        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);

        return javalin.port();
    }

    public int port() {
        return javalin.port();
    }

    private Object exceptionHandler(Exception e, Context context){
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        return context.json(body);
    }

    private Object registerUser(Context context) throws DataAccessException, BadRequestException, AlreadyTakenException {

        UserData registerRequest = new Gson().fromJson(context.body(), UserData.class);

        if(registerRequest.username() == null || registerRequest.password() == null)
        {
            context.status(400);
            return exceptionHandler(new BadRequestException("bad request"), context);
        }
        try{
            AuthData newAuth = userService.register(registerRequest);
            context.status(200);
            return context.json(new Gson().toJson(newAuth));
        } catch (AlreadyTakenException e) {
            context.status(403);
            return exceptionHandler(new AlreadyTakenException("already taken"), context);
        }
    }
    private void login(Context context) throws DataAccessException{


        UserData loginRequest = new Gson().fromJson(context.body(), UserData.class);

        if(loginRequest.username() == null || loginRequest.password() == null)
        {
            context.status(400);
            exceptionHandler(new BadRequestException("bad request"), context);
            return;
        }
        try {
            AuthData newAuth = userService.logIn(loginRequest);
            context.status(200);
            context.json(new Gson().toJson(newAuth));
            return;
        } catch (DataAccessException e) {
            context.status(401);
            exceptionHandler(new DataAccessException("unauthorized"), context);
            return;
        }
    }

    private void logout(Context context) throws DataAccessException{
       String authHeader = context.header("authorization");

       try {
           userService.logout(authHeader);
       } catch (DataAccessException e){
           context.status(401);
           exceptionHandler(new DataAccessException("unauthorized"), context);
        }
    }

    private Object createGame(Context context) throws DataAccessException{
        String authHeader = context.header("authorization");

        try {
            GameData makeGame = new Gson().fromJson(context.body(), GameData.class);
            if(makeGame.gameName() == null){
                context.status(400);
                return exceptionHandler(new BadRequestException("bad request"), context);
            }
            int gameID = gameService.createGame(authHeader, makeGame.gameName());
            context.status(200);
            var body = new Gson().toJson(Map.of("gameID", gameID));
            return context.json(body);
        } catch (DataAccessException e) {
            context.status(401);
            return exceptionHandler(new DataAccessException("unauthorized"), context);
        }

    }
    private Object listGames(Context context) throws DataAccessException{
        String authHeader = context.header("authorization");

        try {
            Collection<GameData> newList = gameService.listOfGames(authHeader);
            context.status(200);
            var body = new Gson().toJson(Map.of("games", newList));
            return context.json(body);
        } catch (DataAccessException e) {
            context.status(401);
            return exceptionHandler(new DataAccessException("unauthorized"), context);

        }



    }

    private Object joinGame(Context context) throws DataAccessException{
        String authHeader = context.header("authorization");
        record updateGameData(String playerColor, Integer gameID) {}

        updateGameData updatedGame = new Gson().fromJson(context.body(), updateGameData.class);

        try{
            gameService.joinAGame(authHeader, updatedGame.gameID(), updatedGame.playerColor());
            context.status(200);

            return("{}");
//            var body = new Gson().toJson("{}");
//            return context.json(body);
        } catch (DataAccessException e) {
            context.status(401);
            return exceptionHandler(new DataAccessException("unauthorized"), context);
        } catch (BadRequestException b) {
            context.status(400);
            return exceptionHandler(new BadRequestException("bad request"), context);
        } catch (AlreadyTakenException c){
            context.status(403);
            return exceptionHandler(new AlreadyTakenException("already taken"), context);

        }

    }

    private void clear(Context context) throws DataAccessException{
        clearService.clearAll();

    }

    public void stop() {
        javalin.stop();
    }
}
