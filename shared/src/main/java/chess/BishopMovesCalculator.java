package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition myPosition ) {

        Collection<ChessMove> bishopMoves = new ArrayList<>();
        int[][] possibleMoves = {{1, 1}, {-1, 1}, {1, -1}, {-1, -1}};

        bishopMoves = loop(board, myPosition, possibleMoves);

        return bishopMoves;

    }




}
