package chess;

import java.util.Collection;
import java.util.List;

public interface PieceMovesCalculator {


    public default Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){

        ChessPiece piece = board.getPiece(myPosition);

        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
            BishopMovesCalculator [] totalMoves = new BishopMovesCalculator[20];
        }

        return List.of();

    }




}
