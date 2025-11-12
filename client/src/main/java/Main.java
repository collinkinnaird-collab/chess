import chess.*;
import ui.IntroREPL;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        String serverUrl = "http://localhost:8080";

        try{
            new IntroREPL(serverUrl).run();
        } catch (Exception e){
            throw new RuntimeException("no");
        }

    }
}