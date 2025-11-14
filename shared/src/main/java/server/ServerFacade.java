package server;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.ListOfGames;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.Collection;

public class ServerFacade {
    private final HttpClient client= HttpClient.newHttpClient();
    private final String serverURL;

    public ServerFacade (String url) {
        serverURL = url;
    }

    public AuthData register(UserData user) throws Exception {
        var path = "/user";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public AuthData login(UserData user) throws Exception {
        var path = "/session";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public void logout(AuthData userAuth) throws Exception{
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, userAuth);
    }

    public void createGame(String game, AuthData userAuth) throws Exception {
        var path = "/game";
        this.makeRequest("POST", path, new GameData(0, null, null, game, null), GameData.class, userAuth);
    }

    public ListOfGames listGames(AuthData userAuth) throws Exception {
        var path = "/game";
        return this.makeRequest("GET", path, null, ListOfGames.class, userAuth);
    }

    public void playGame(Object game, AuthData userAuth) throws Exception {
        var path = "/game";
        this.makeRequest("PUT", path, game, GameData.class, userAuth);
    }

    public void observeGame(AuthData userAuth, int id) throws Exception {
        var path = "/game";
        this.makeRequest("GET", path, new GameData(id, null, null, null, null), GameData.class, userAuth);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, AuthData userAuth) throws Exception {
        try {
            URL url = (new URI( serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);


            writeBody(request, http, userAuth);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);

        } catch (Exception e){
            throw new Exception("DO LATER");
        }
    }

    private static void writeBody(Object request, HttpURLConnection http, AuthData userAuth) throws IOException {
        if (request != null){
            if(userAuth != null){
                http.addRequestProperty("authorization", userAuth.authToken());
            }
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()){
                reqBody.write(reqData.getBytes());
            }
            return;
        } else if (userAuth != null){
            http.addRequestProperty("authorization", userAuth.authToken());
        }

    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws Exception {
        var status = http.getResponseCode();
        if(!isSuccessful(status)) {
            throw new Exception("failure");
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException{
        T response = null;
        if (http.getContentLength() > 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if(responseClass != null){
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }






}
