package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    protected final ChessGame.TeamColor pieceColor;
    private final PieceType type;


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    public ChessPiece(ChessPiece clone){
        this.pieceColor = clone.pieceColor;
        this.type = clone.type;

    }


    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ChessPiece piece = board.getPiece(myPosition);

        PieceMovesCalculator calculateMove;

        if(piece.getPieceType() == PieceType.BISHOP)
        {
             calculateMove = new BishopMovesCalculator();
        } else if(piece.getPieceType() == PieceType.ROOK) {
            calculateMove = new RookMovesCalculator();
        } else if (piece.getPieceType() == PieceType.KING){
            calculateMove = new KingMovesCalculator();
        } else if (piece.getPieceType() == PieceType.QUEEN){
            calculateMove = new QueenMovesCalculator();
        } else if (piece.getPieceType() == PieceType.PAWN){
            calculateMove = new PawnMovesCalculator();
        }else if (piece.getPieceType() == PieceType.KNIGHT) {
            calculateMove = new KnightMovesCalculator();
        } else {
            return null;
        }

        return calculateMove.pieceMoves(board, myPosition);

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
