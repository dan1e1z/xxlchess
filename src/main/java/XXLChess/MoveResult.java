package XXLChess;


/**
 * This class represents the result of a move in a chess game.
 * It contains information about the piece that was moved,
 *  the square that it was moved to, and whether or not the move was successful.
 */
public class MoveResult{

    /**
     *  parent object of black piece
     */
    private Pieces blackPiece;

    /**
     * coordinates of black piece
     */
    private int[] captureCoordinate;


    /**
     * constructor of moveResult
     * @param blackPiece black piece
     * @param captureCoordinate coordinates of black piece
     */
    public MoveResult(Pieces blackPiece, int[] captureCoordinate) {
        this.blackPiece = blackPiece;
        this.captureCoordinate = captureCoordinate;
    }

    /**
     * getter method for blackPiece
     * @return blackPiece cpu chess piece
     */
    public Pieces getBlackPiece() {
        return blackPiece;
    }

    /**
     * getter method for blackPieces coordinates
     * @return captureCoordinate coordinates of cpu piece
     */
    public int[] getCaptureCoordinate() {
        return captureCoordinate;
    }
}