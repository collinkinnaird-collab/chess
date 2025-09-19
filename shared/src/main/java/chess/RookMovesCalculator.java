package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



public class RookMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition ){
        Collection<ChessMove> rookMoves = new ArrayList<>();

        int[][] possibleMoves = {{0, 1} ,{0, -1}, {1, 0}, {-1, 0}};

        rookMoves = loop(board, myPosition, possibleMoves);

        return rookMoves;
    }
}
