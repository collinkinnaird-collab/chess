package server;

import Service.ClearService;
import Service.GameService;
import Service.UserService;
import dataaccess.DataAccessException;
import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import model.*;
import java.util.UUID;


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
                .delete("/db", this::clear);



        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);

        return javalin.port();
    }

    public int port() {
        return javalin.port();
    }

    private void registerUser(Context context) throws DataAccessException{
        UserData registerRequest = new Gson().fromJson(context.body(), UserData.class);
        AuthData newAuth = userService.register(registerRequest);
        context.json(new Gson().toJson(newAuth));
    }

    private void login(Context context) throws DataAccessException{
        UserData loginRequest = new Gson().fromJson(context.body(), UserData.class);
        AuthData newAuth = userService.logIn(loginRequest);
        context.json(new Gson().toJson(newAuth));
    }

    private void logout(Context context) throws DataAccessException{
       String authHeader = context.header("authorization");

       userService.logout(authHeader);

    }

    private void createGame(Context context) throws DataAccessException{
        String authHeader = context.header("authorization");
        GameData makeGame = new Gson().fromJson(context.body(), GameData.class);
        int gameID = gameService.createGame(authHeader, makeGame);
        context.json(new Gson().toJson(makeGame));

    }
    private void listGames(Context context) throws DataAccessException{
        String authHeader = context.header("authorization");

        gameService.listOfGames(authHeader);

    }

    private void clear(Context context) throws DataAccessException{
        clearService.clearAll();

    }









    //    private void createHandlers(Javalin javalin){
//        javalin.post("register", new ChessServerHandler());
//    }

    public void stop() {
        javalin.stop();
    }
}
