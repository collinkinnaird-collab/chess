package server;

import Service.ClearService;
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

    public Server(UserService userService, ClearService clearService) {

        this.userService = userService;
        this.clearService = clearService;

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
            .post("/register", this::registerUser)
            .get("/login", this::login)
                .delete("/clear", this::clear);



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
        loginRequest = userService.logIn(loginRequest);
        context.json(new Gson().toJson(loginRequest));
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
