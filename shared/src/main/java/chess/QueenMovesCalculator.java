package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition ){
        Collection<ChessMove> queenMoves = new ArrayList<>();

        int[][] possibleMoves = {{1, 1}, {-1, 1}, {1, -1}, {-1, -1} ,{0, 1} ,{0, -1}, {1, 0}, {-1, 0}};

        queenMoves = loop(board, myPosition, possibleMoves);

        return queenMoves;
    }
}
