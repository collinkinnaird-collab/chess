package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard  {

    ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
        
    }

    public ChessBoard(ChessBoard copy){

        ChessPiece[][] copyBoard = copy.squares;
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                if (squares[i][j].getPieceType() != null){
                    ChessPiece copyPiece = new ChessPiece(squares[i][j].pieceColor, squares[i][j].getPieceType());
                    copyBoard[i][j] = copyPiece;

                }
            }
        }
        // nested for loop,

        this.squares = copyBoard;


    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn()-1] = piece;
    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
//        throw new RuntimeException("Not implemented");
//
        return  squares[position.getRow() - 1][position.getColumn()-1];
    }
    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

    // first row of white
        ChessPiece rook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece knight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece bishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece queen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPiece king = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPiece bishop2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece knight2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece rook2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);

        // second row of white
        ChessPiece pawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece pawn2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece pawn3 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece pawn4 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece pawn5 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece pawn6 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece pawn7 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece pawn8 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);

        // first row of black
        ChessPiece brook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPiece bknight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPiece bbishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPiece bqueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        ChessPiece bking = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        ChessPiece bbishop2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPiece bknight2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPiece brook2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);

        // second row of black
        ChessPiece bpawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece bpawn2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece bpawn3 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece bpawn4 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece bpawn5 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece bpawn6 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece bpawn7 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPiece bpawn8 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);

        addPiece(new ChessPosition(1,1), rook);
        addPiece(new ChessPosition(1,2), knight);
        addPiece(new ChessPosition(1,3), bishop);
        addPiece(new ChessPosition(1,4), queen);
        addPiece(new ChessPosition(1,5), king);
        addPiece(new ChessPosition(1,6), bishop2);
        addPiece(new ChessPosition(1,7), knight2);
        addPiece(new ChessPosition(1,8), rook2);

        addPiece(new ChessPosition(2,1), pawn);
        addPiece(new ChessPosition(2,2), pawn2);
        addPiece(new ChessPosition(2,3), pawn3);
        addPiece(new ChessPosition(2,4), pawn4);
        addPiece(new ChessPosition(2,5), pawn5);
        addPiece(new ChessPosition(2,6), pawn6);
        addPiece(new ChessPosition(2,7), pawn7);
        addPiece(new ChessPosition(2,8), pawn8);

        addPiece(new ChessPosition(8,1), brook);
        addPiece(new ChessPosition(8,2), bknight);
        addPiece(new ChessPosition(8,3), bbishop);
        addPiece(new ChessPosition(8,4), bqueen);
        addPiece(new ChessPosition(8,5), bking);
        addPiece(new ChessPosition(8,6), bbishop2);
        addPiece(new ChessPosition(8,7), bknight2);
        addPiece(new ChessPosition(8,8), brook2);

        addPiece(new ChessPosition(7,1), bpawn);
        addPiece(new ChessPosition(7,2), bpawn2);
        addPiece(new ChessPosition(7,3), bpawn3);
        addPiece(new ChessPosition(7,4), bpawn4);
        addPiece(new ChessPosition(7,5), bpawn5);
        addPiece(new ChessPosition(7,6), bpawn6);
        addPiece(new ChessPosition(7,7), bpawn7);
        addPiece(new ChessPosition(7,8), bpawn8);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }


}
