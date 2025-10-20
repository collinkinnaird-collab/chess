package server;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class ChessServerHandler implements Handler {
    public void handle(Context context ){
        context.result("yaya");
    }
}
