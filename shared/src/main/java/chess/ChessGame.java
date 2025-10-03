package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame implements CheckMateCalculator{

   // ChessGame myGame = new ChessGame();
    ChessBoard gameBoard;
    TeamColor teamTurn;
    boolean stalemate = false;

    public ChessGame() {

            gameBoard = new ChessBoard();
            gameBoard.resetBoard();
            teamTurn = TeamColor.WHITE;

    }

    public ChessGame(ChessGame copy){

        ChessBoard copyBoard = new ChessBoard(copy.gameBoard);


        this.teamTurn = copy.teamTurn;
        this.gameBoard = copyBoard;



    }




    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {


        Collection<ChessMove> allMoves = new ArrayList<>();
        Collection<ChessMove> goodMoves = new ArrayList<>();
        ChessPiece currentPiece = gameBoard.getPiece(startPosition);
        if (currentPiece.getPieceType() == null)
        {
            return allMoves;
        }

        allMoves = currentPiece.pieceMoves(gameBoard, startPosition);

        for(ChessMove nextMove: allMoves) {

            ChessPiece temporaryBadPiece = gameBoard.getPiece(nextMove.getEndPosition());
            gameBoard.addPiece(startPosition, null);
            gameBoard.addPiece(nextMove.getEndPosition(), currentPiece);
            if (!isInCheck(currentPiece.pieceColor)) {
                goodMoves.add(nextMove);
            }
            gameBoard.addPiece(nextMove.getEndPosition(), temporaryBadPiece);
            gameBoard.addPiece(startPosition, currentPiece);
        }
        return goodMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        gameBoard = getBoard();
        boolean isLegit = false;

          Collection<ChessMove> totalMoves = new ArrayList<>();


        // ChessGame copy = new ChessGame(this);
        if (gameBoard.getPiece(move.getStartPosition()) == null || gameBoard.getPiece(move.getStartPosition()).pieceColor != teamTurn){
            throw new InvalidMoveException();
        }
            ChessPiece trialPiece = gameBoard.getPiece(move.getStartPosition());
            totalMoves = trialPiece.pieceMoves(gameBoard, move.getStartPosition());
            Iterator<ChessMove> iterator = totalMoves.iterator();

            while(iterator.hasNext()){
                if(iterator.next().getEndPosition().equals(move.getEndPosition()))
                {
                    isLegit = true;
                    break;
                }
            }

                if (isInCheck(trialPiece.pieceColor)) {
                    throw new InvalidMoveException();
                }
                if (isInCheckmate(trialPiece.pieceColor)) {

                    getBoard();
                }
                if (isInStalemate(trialPiece.pieceColor))
                {
                    if(teamTurn == TeamColor.WHITE) {
                        teamTurn = TeamColor.BLACK;
                    } else {
                        teamTurn = TeamColor.WHITE;
                    }
                    throw new InvalidMoveException();
                } else if (isLegit) {
                    gameBoard.addPiece(move.getEndPosition(), trialPiece);
                    if(trialPiece.getPieceType() == ChessPiece.PieceType.PAWN && (move.getEndPosition().getRow() == 1 ||move.getEndPosition().getRow() == 8))
                    {
                        gameBoard.addPiece(move.getEndPosition(), new ChessPiece(teamTurn, move.getPromotionPiece()));
                    }
                    else{
                        gameBoard.addPiece(move.getEndPosition(), trialPiece);
                    }
                    gameBoard.addPiece(move.getStartPosition(), null);
                    setBoard(gameBoard);
                    if(teamTurn == TeamColor.WHITE) {
                        teamTurn = TeamColor.BLACK;
                    } else {
                        teamTurn = TeamColor.WHITE;
                    }
                }
                else{
                    throw new InvalidMoveException();
                }


    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        //find king
        ChessPiece kingPiece;
        ChessPosition enemyPosition;
        ChessPosition kingPosition = new ChessPosition(0,0);
        Collection<ChessMove> otherMoves = new ArrayList<>();

        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                if(gameBoard.getPiece(new ChessPosition(i,j))!= null) {
                    kingPiece = gameBoard.getPiece(new ChessPosition(i, j));
                    if (kingPiece.getPieceType() == ChessPiece.PieceType.KING && kingPiece.pieceColor == teamColor) {
                        kingPosition = new ChessPosition(i, j);
                        break;
                    }

                }
            }
        }
        for(int k = 1; k <= 8; k++){
            for(int l = 1; l <= 8; l++){
                if (gameBoard.getPiece(new ChessPosition(k,l)) != null)
                {
                    ChessPiece testPiece = gameBoard.getPiece(new ChessPosition(k,l));
                    if(testPiece.pieceColor != teamColor){
                        otherMoves = testPiece.pieceMoves(gameBoard, new ChessPosition(k,l));
                        Iterator<ChessMove> iterator = otherMoves.iterator();
                        while(iterator.hasNext())
                        {
                            enemyPosition = iterator.next().getEndPosition();
                            if(enemyPosition.equals(kingPosition)){
                                return true;
                            }
                        }
                    }
                }

            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        boolean forSure = true;

        if(isInCheck(teamColor) && (isInStalemate(teamColor) || stalemate))
        {
            for(int k = 1; k <= 8; k++){
                for(int l = 1; l <= 8; l++){
                    if (gameBoard.getPiece(new ChessPosition(k,l)) != null)
                    {
                        ChessPiece testPiece = gameBoard.getPiece(new ChessPosition(k,l));
                        ChessPosition myPosition = new ChessPosition(k,l);
                        if(testPiece.pieceColor == teamColor){
                           if (!validMoves(myPosition).isEmpty()){
                               forSure = false;
                           }

                        }
                    }

                }
            }

            if (!forSure){
                return false;
            } else {
                return true;
            }
        } else {

            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        //find king
        ChessPiece kingPiece = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        ChessPosition kingPosition = new ChessPosition(0,0);

            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    if (gameBoard.getPiece(new ChessPosition(i, j)) != null) {
                        kingPiece = gameBoard.getPiece(new ChessPosition(i, j));
                        if (kingPiece.getPieceType() == ChessPiece.PieceType.KING && kingPiece.pieceColor == teamColor) {
                            kingPosition = new ChessPosition(i, j);
                        }


                    }
                }
            }
        if(validMoves(kingPosition).isEmpty() && !kingPiece.pieceMoves(gameBoard, kingPosition).isEmpty() && !isInCheck(teamColor))
        {
            return true;
        }
        else if (validMoves(kingPosition).isEmpty() && !kingPiece.pieceMoves(gameBoard, kingPosition).isEmpty()){
            stalemate = true;
            return false;
        }
        else {
            return false;
        }
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        if(gameBoard == null)
        {
            gameBoard.resetBoard();
        }
        return gameBoard;
    }

    @Override
    public String toString() {
        return String.format("   %s   ", gameBoard);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(gameBoard, chessGame.gameBoard) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameBoard, teamTurn);
    }


}


//