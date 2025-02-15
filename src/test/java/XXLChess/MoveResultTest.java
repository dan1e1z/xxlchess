package XXLChess;


import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MoveResultTest extends App {

    private Pieces blackPiece;
    private int[] captureCoordinate;
    private MoveResult move;

    @Test
    // test constructor of MoveResult
    public void moveResultConstructorTest() {
        move = new MoveResult(blackPiece, captureCoordinate);
        // move.assertNotNull(move);
        move.getBlackPiece();
        move.getCaptureCoordinate();
    }
}