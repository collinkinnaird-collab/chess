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
    private Object login(Context context) throws DataAccessException{


        UserData loginRequest = new Gson().fromJson(context.body(), UserData.class);

        if(loginRequest.username() == null || loginRequest.password() == null)
        {
            context.status(400);
            return exceptionHandler(new BadRequestException("bad request"), context);
        }
        try {
            AuthData newAuth = userService.logIn(loginRequest);
            context.status(200);
            return context.json(new Gson().toJson(newAuth));
        } catch (DataAccessException e) {
            context.status(401);
            return exceptionHandler(new DataAccessException("unauthorized"), context);
        }
    }

    private void logout(Context context) throws DataAccessException{
       String authHeader = context.header("authorization");

       userService.logout(authHeader);

    }

    private void createGame(Context context) throws DataAccessException{
        String authHeader = context.header("authorization");
        GameData makeGame = new Gson().fromJson(context.body(), GameData.class);
        int gameID = gameService.createGame(authHeader, makeGame.gameName());
        context.json(new Gson().toJson(makeGame));

    }
    private void listGames(Context context) throws DataAccessException{
        String authHeader = context.header("authorization");

        gameService.listOfGames(authHeader);

    }

    private void joinGame(Context context) throws DataAccessException{
        String authHeader = context.header("authorization");
        GameData joinGame = new Gson().fromJson(context.body(), GameData.class);

        if(joinGame.blackUsername() != null) {
            gameService.joinAGame(authHeader, joinGame.gameID(), joinGame.blackUsername());
        }
        else{
            gameService.joinAGame(authHeader, joinGame.gameID(), joinGame.whiteUsername());
        }



    }

    private void clear(Context context) throws DataAccessException{
        clearService.clearAll();

    }

    public void stop() {
        javalin.stop();
    }
}
