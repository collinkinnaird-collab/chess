package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
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

            if( piece.getPieceType() == PieceType.BISHOP) {


                List<ChessMove> bishopMoves = new ArrayList<>();

                int[][] possibleMoves = {{1, 1}, {-1, 1}, {1, -1}, {-1, -1}};

                int set = 0;

                for (int i = 0; i < possibleMoves.length; i++) {
                    int[] directions = possibleMoves[i];

                    int updateXaxis = myPosition.getRow() + directions[0];
                    int updateYaxis = myPosition.getColumn() + directions[1];

                    while (updateYaxis <= 8 && updateYaxis >= 1 && updateXaxis <= 8 && updateXaxis >= 1) {
                        ChessPiece otherPiece = board.getPiece(new ChessPosition(updateXaxis, updateYaxis));
                        if(otherPiece != null) {
                            if (otherPiece.pieceColor == piece.pieceColor) {
                                break;
                            }
                            else {
                                bishopMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(updateXaxis, updateYaxis), null));
                                break;
                            }
                        }
                        else {
                            bishopMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), new ChessPosition(updateXaxis, updateYaxis), null));

                            set++;
                            updateXaxis = updateXaxis + directions[0];
                            updateYaxis = updateYaxis + directions[1];
                        }
                    }
                }

                return bishopMoves;
            }

            return List.of();    // return List.of(new ChessMove(new ChessPosition(5,4), null, null));
    }
}
