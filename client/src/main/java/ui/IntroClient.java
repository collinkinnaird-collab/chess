package ui;

import model.AuthData;
import model.UserData;
import server.ServerFacade;

import java.util.Arrays;

public class IntroClient {


    private final ServerFacade server;
    private AuthData userAuth;


    public IntroClient(String servrURL){
        server = new ServerFacade(servrURL);
    }


    public String eval(String line) throws Exception {
        try {
            String[] tokens = line.toLowerCase().split(" ");
            String command = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (command){
                case "1" -> register(params);
                case "2" -> login(params);
                case "3" -> "quit";
                case "4" -> help();
                default -> help();
            };
        } catch (Exception e){
            throw new Exception("incorrect parameters");
        }
    }

    public String register(String... params) throws Exception {

        if(params.length == 3){
            String userName = params[0];
            String password = params[1];
            String email = params[2];

            UserData newUser = new UserData(userName, password, email);

            AuthData newAuth = server.register(newUser);
            userAuth = newAuth;

            return String.format("you are register as %s.", userName);

        }
        throw new Exception("incorrect parameters");

    }

    public String login(String... params) throws Exception {
        if(params.length == 2){
            String userName = params[0];
            String password = params[1];

            UserData newUser = new UserData(userName, password, null);

            userAuth = server.login(newUser);

            return String.format("you are logged in as %s", userName);
        }

        throw new Exception("bad input");
    }

    public AuthData getAuthData(){
        return userAuth;
    }

    public String help(){
        return """
                1. Register (username, password, email)
                2. Login (username, password)
                3. Quit
                4. help
                """;
    }




}
