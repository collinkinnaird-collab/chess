package server;


import dataaccess.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import model.*;

import java.util.Collection;
import java.util.Map;


public class Server {

    private final Javalin javalin;
    UserDAO dataAccessUser;

    {
        try {
            dataAccessUser = new MySqlUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    AuthDAO dataAccessAuth;

    {
        try {
            dataAccessAuth = new MySqlAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    GameDAO dataAccessGame;

    {
        try {
            dataAccessGame = new MySqlGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    UserService userService = new UserService(dataAccessUser, dataAccessAuth);
     ClearService clearService = new ClearService(dataAccessUser, dataAccessGame, dataAccessAuth);
     GameService gameService = new GameService(dataAccessUser, dataAccessGame, dataAccessAuth);

    public Server() {




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

    private void registerUser(Context context) throws DataAccessException {

        UserData registerRequest = new Gson().fromJson(context.body(), UserData.class);

        if(registerRequest.username() == null || registerRequest.password() == null)
        {
            context.status(400);
            exceptionHandler(new BadRequestException("bad request"), context);
            return;
        }
        try{
            AuthData newAuth = userService.register(registerRequest);
            context.status(200);
            context.json(new Gson().toJson(newAuth));
            return;
        } catch (AlreadyTakenException e) {
            context.status(403);
            exceptionHandler(new AlreadyTakenException("already taken"), context);
            return;
        } catch (Exception f){
            context.status(500);
            exceptionHandler(f, context);
            return;
        }

    }
    private void login(Context context) {


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
        } catch (Exception f){
            context.status(500);
            exceptionHandler(f, context);
        }
    }

    private void logout(Context context) throws DataAccessException{
       String authHeader = context.header("authorization");

       try {
           userService.logout(authHeader);
       } catch (DataAccessException e){
           context.status(401);
           exceptionHandler(new DataAccessException("unauthorized"), context);
       } catch (Exception f){
           context.status(500);
           exceptionHandler(f, context);
       }
    }

    private void createGame(Context context) throws DataAccessException{
        String authHeader = context.header("authorization");

        try {
            GameData makeGame = new Gson().fromJson(context.body(), GameData.class);
            if(makeGame.gameName() == null){
                context.status(400);
                exceptionHandler(new BadRequestException("bad request"), context);
                return;
            }
            int gameID = gameService.createGame(authHeader, makeGame.gameName());
            context.status(200);
            var body = new Gson().toJson(Map.of("gameID", gameID));
            context.json(body);
            return;
        } catch (DataAccessException e) {
            context.status(401);
            exceptionHandler(new DataAccessException("unauthorized"), context);
            return;
        } catch (Exception f){

            context.status(500);
            exceptionHandler(f, context);
            return;
        }

    }
    private void listGames(Context context) throws DataAccessException{
        String authHeader = context.header("authorization");

        try {
            Collection<GameData> newList = gameService.listOfGames(authHeader);
            context.status(200);
            var body = new Gson().toJson(Map.of("games", newList));
            context.json(body);
            return;
        } catch (DataAccessException e) {
            context.status(401);
            exceptionHandler(new DataAccessException("unauthorized"), context);
            return;

        } catch (Exception f){
            context.status(500);
            exceptionHandler(f, context);
            return;
        }



    }

    private Object joinGame(Context context) {
        String authHeader = context.header("authorization");
        record UpdateGameData(String playerColor, Integer gameID) {}

        UpdateGameData updatedGame = new Gson().fromJson(context.body(), UpdateGameData.class);

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

        } catch (Exception f){
            context.status(500);
            return exceptionHandler(f, context);

        }

    }

    private void clear(Context context) {
        try {
            clearService.clearAll();
        } catch (DataAccessException e){
            context.status(500);
            exceptionHandler(e, context);
        }
    }

    public void stop() {
        javalin.stop();
    }
}
