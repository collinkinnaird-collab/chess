package server;

import Service.UserService;
import dataaccess.DataAccessException;
import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import model.*;

public class Server {

    private final Javalin javalin;
    private final UserService userService;

    public Server(UserService userService) {

        this.userService = userService;

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
            .post("/register", this::registerUser);



        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);



        return javalin.port();
    }

    private void registerUser(Context context) throws DataAccessException{
        UserData registerRequest = new Gson().fromJson(context.body(), UserData.class);
        registerRequest = userService.register(registerRequest);
        context.json(new Gson().toJson(registerRequest));
    }









    //    private void createHandlers(Javalin javalin){
//        javalin.post("register", new ChessServerHandler());
//    }

    public void stop() {
        javalin.stop();
    }
}
