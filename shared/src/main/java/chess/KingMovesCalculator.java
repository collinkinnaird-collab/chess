package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition ){
        Collection<ChessMove> kingMoves = new ArrayList<>();

        int[][] possibleMoves = {{1, 1}, {-1, 1}, {1, -1}, {-1, -1} ,{0, 1} ,{0, -1}, {1, 0}, {-1, 0}};

        kingMoves = eightOptions(board, myPosition, possibleMoves);

        return kingMoves;
    }
}
