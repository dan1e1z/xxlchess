package XXLChess;

import java.util.*;
import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import processing.core.*;

public class BoardTest extends App{

    private PImage testImage;
    private String playerColour = "white";
    private ArrayList<int[]> currentHighlightedTitles = new ArrayList<int[]>();
    private Board highlight;
    private ArrayList<Pieces> playerArray = new ArrayList<Pieces>();
    private ArrayList<Pieces> cpuArray = new ArrayList<Pieces>();
    private Pieces playerPiece;
    private String testConfigPath;


    
    @Test 
    // constructing highlight object 
    public void highlightConstructorTest() {

        highlight = new Board();
        assertNotNull(highlight);
    }

    @Test
    // test highlighting title on board 
    public void highlightingBoardTest() {

        testConfigPath = "src/test/java/XXLChess/configTest/configRegularTest.json";
        
        App app = new App(testConfigPath);
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);

        Board board = new Board();

        Pieces piece = new Pawn(0, 48, "pawn", 'b', 1.00, testImage, playerColour);
        playerPiece = new Rook(0, 624, "rook", 'w', 5.25, testImage, playerColour);
        cpuArray.add(new King(96, 528, "king", 'b', 9.5, testImage, playerColour));
        playerArray.add(new King(366, 624, "king", 'w', 100, testImage, playerColour));
        playerArray.add(new Rook(0, 624, "rook", 'w', 5.25, testImage, playerColour));
        playerArray.add(new Rook(624, 624, "rook", 'w', 5.25, testImage, playerColour));

        ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
        possibleMoves.add(new int[]{1,2});
        app.delay(2000);
        board.highlightMoves(app, possibleMoves, playerArray, cpuArray, possibleMoves, playerPiece);

        board.highlightSelectedPieces(app, piece);
        board.tickClear(0, 0);
        board.gethighlightSelectedPieces();
        board.checkHighlight(app, cpuArray);
        board.resetColour(app);
        board.highlightContributionPieces(app, playerArray, cpuArray);
        board.highlightContributionPieces(app, cpuArray, playerArray);

        // no king 
        cpuArray.clear();
        board.checkHighlight(app, cpuArray);
        app.dispose();
    }

    
    @Test 
    // testing all method which return a boolean
    public void booleanReturnTest() {

        Board board = new Board();
        Pieces piece = new Queen(0, 576, "queen", 'w', 9.5, testImage, playerColour);

        playerArray.add(new Queen(0, 576, "queen", 'w', 9.5, testImage, playerColour));
        playerArray.add(new King(0, 624, "king", 'w', 100, testImage, playerColour));
        cpuArray.add(new King(0, 0, "king", 'b', 1.00, testImage, playerColour));
        cpuArray.add(new Queen(96, 528, "queen", 'b', 9.5, testImage, playerColour));

        // check correct boolean for clicked board - if mouse clicked a valid piece
        assertTrue(board.checkClickedForPiece(0, 13, playerArray)); 
        assertFalse(board.checkClickedForPiece(0, 0, playerArray));

        // CheckForKingCheck Method
        ArrayList<int[]> attackingArray = new ArrayList<int[]>();
        assertFalse(board.checkForKingCheck(attackingArray, cpuArray, playerArray));

        attackingArray.add(new int[]{0,0});
        assertTrue(board.checkForKingCheck(attackingArray, cpuArray, playerArray));
        board.checkForHighestValueAttackedPiece(attackingArray, cpuArray);

        // checkAttackingPosition
        assertTrue(board.checkAttackingPosition(2,11, cpuArray));
        assertFalse(board.checkAttackingPosition(0, 12, cpuArray));

        assertNotNull(board.moveToFreePosition(piece, playerArray, cpuArray));

    }

    @Test
    // tests Development of piece on board, including detecting if piece on board is blocking a possible moving piece
    public void MoveResultTest() {

        playerArray.add(new Queen(0, 576, "queen", 'w', 9.5, testImage, playerColour));
        playerArray.add(new King(0, 624, "king", 'w', 100, testImage, playerColour));
        cpuArray.add(new King(0, 0, "king", 'b', 1.00, testImage, "black"));
        cpuArray.add(new Queen(96, 528, "queen", 'b', 9.5, testImage, playerColour));

        char[][] titleChars = {
            {'R', 'N', 'B', 'H', 'C', 'G', 'A', 'K', 'G', 'C', 'E', 'B', 'N', 'R'},
            {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
            {'r', 'n', 'b', 'h', 'c', 'g', 'a', 'k', 'g', 'c', 'e', 'b', 'n', 'r'}
        };

        testConfigPath = "src/test/java/XXLChess/configTest/configRegularTest.json";


        App app = new App(testConfigPath);
        PApplet.runSketch(new String[] {"App"}, app);
        Board board = new Board();

        app.setup();
        app.delay(5000);
        board.blockCheck(app, playerArray, cpuArray);
        board.whiteBlockCheck(app, playerArray, cpuArray);
        board.checkforDevelopment("knight", playerArray, cpuArray, titleChars);
        board.checkforDevelopment("knight", cpuArray, playerArray, titleChars);

        // no pieces in arraylists
        playerArray.clear();
        cpuArray.clear();
        board.blockCheck(app, playerArray, cpuArray);
        app.dispose();
    }
}
