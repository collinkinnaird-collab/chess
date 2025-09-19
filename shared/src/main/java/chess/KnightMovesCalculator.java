package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition ){
        Collection<ChessMove> knightMoves = new ArrayList<>();

        int[][] possibleMoves = {{2, 1}, {2, -1}, {-2, -1}, {-2, 1} ,{1, 2} ,{1, -2}, {-1, 2}, {-1, -2}};

        knightMoves = eightOptions(board, myPosition, possibleMoves);

        return knightMoves;
    }
}
