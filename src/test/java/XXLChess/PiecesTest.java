package XXLChess;


import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import processing.core.*;

public class PiecesTest extends App {
    
    private PImage testImage;
    private Pieces playerPiece;
    private Pieces cpuPiece;
    private String playerColour = "white";
    private ArrayList<Pieces> playerArray = new ArrayList<>();
    private ArrayList<Pieces> cpuArray = new ArrayList<>();
    private String testConfigPath;



    @Test
    // constructing chess piece object
    public void pieceConstructorTest() {

        Pieces piece = new Amazon(288, 624, "amazon", 'w', 12, testImage, playerColour);
        assertNotNull(piece);
    }

    @Test
    // test getters and setters
    public void getsetterTest() {
    
        Pieces piece = new Amazon(288, 624, "amazon", 'w', 12, testImage, playerColour);
        playerArray.add(piece);
        piece.setStart(0, 0);
        piece.setCastle();
        piece.getCastle();
        piece.getisAnimationFinsihed();
        piece.setposCoordinates(0, 0);
        piece.setX(0);
        piece.getArrayX();
        piece.getArrayY();
        piece.setY(0);
        piece.setType("pawn");
        piece.setSprite(testImage);
        piece.getValue();
        piece.getColour();
        piece.getPlayerColour();
        piece.getPiece(6, 13, playerArray);
    }

    @Test 
    // testing castling
    public void castlingTest() {

        testConfigPath = "src/test/java/XXLChess/configTest/configRegularTest.json";

        PApplet app = new PApplet();
        PApplet.runSketch(new String[] {"App"}, app);

        playerArray.add(new King(366, 624, "king", 'w', 100, testImage, playerColour));
        playerArray.add(new Rook(0, 624, "rook", 'w', 5.25, testImage, playerColour));
        playerArray.add(new Rook(624, 624, "rook", 'w', 5.25, testImage, playerColour));


        Pieces piece = new King(366, 624, "king", 'w', 100, testImage, playerColour);

        piece.updateOriginalKingArray(playerArray);
        piece.checkCanCastle(playerArray, cpuArray);
        piece.getLeftCastleRook(playerArray, cpuArray);
        piece.getRightCastleRook(playerArray, cpuArray);
        piece.checkRightCanCastle(playerArray, cpuArray);
        piece.checkLeftCanCastle(playerArray, cpuArray);
    }

    @Test 
    // test draw functionality 
    public void drawTest() {

        testConfigPath = "src/test/java/XXLChess/configTest/configRegularTest.json";
        
        PApplet app = new PApplet();
        PApplet.runSketch(new String[] {"App"}, app);

        Pieces piece = new King(366, 624, "king", 'w', 100, testImage, playerColour);


        int[] position = new int[]{1,2};
        app.delay(1000);
        piece.movePiece(app, position);
    }

    @Test 
    // test draw onto board 
    public void boardDrawTest() {

        PApplet app = new PApplet();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        Pieces piece = new King(366, 624, "king", 'w', 100, testImage, playerColour);
        piece.moveHighlight(app);
        piece.tick(app);
    }

    @Test
    // test piece take/movement 
    public void movementTest() {

        Pieces piece = new Queen(366, 624, "queen", 'w', 100, testImage, playerColour);
        delay(200);
        cpuArray.add(new General(366, 624, "general", 'b', 5, testImage, playerColour));
        piece.take(cpuArray);

    }

    @Test
    // test information gathering method - i.e. looking for pieces that fit criteria
    public void infoGatheringTest() {

        ArrayList<int[]> list = new ArrayList<int[]>();
        list.add(new int[]{1, 2});
        Pieces attack = new Queen(366, 624, "king", 'w', 100, testImage, playerColour);
        Pieces piece = new King(366, 624, "king", 'w', 100, testImage, playerColour);
        Pieces piece2 = new King(366, 624, "pawn", 'w', 1.00, testImage, playerColour);


        playerArray.add(new King(366, 624, "king", 'w', 100, testImage, playerColour));
        cpuArray.add(new General(366, 624, "general", 'b', 5, testImage, playerColour));



        delay(200);
        piece.getPieceWhichCanAttack(attack, cpuArray, playerArray);
        assertTrue(piece.compareAttackAndDefenderValues(attack, piece));
        assertFalse(piece.compareAttackAndDefenderValues(piece2, attack));
        assertFalse(piece.getIsDefended(piece, list));
        piece.getDefendedPossibleMoves(list, list);
        piece.getAllDefendedPositions(playerArray, cpuArray);
        delay(200);
        piece.getAllAttackPositions(playerArray, cpuArray, piece);
    }

   // change king x-axis position to 336 NOT 366

    @Test
    // test valid piece move 
    public void validMoveTest() {

        playerPiece = new King(336, 624, "king", 'w', 100, testImage, playerColour);

        playerArray.add(new King(336, 624, "king", 'w', 100, testImage, playerColour));
        cpuArray.add(new General(0, 0, "general", 'b', 5, testImage, playerColour));
        
        ArrayList<int[]> possibleMove = new ArrayList<int[]>();
        ArrayList<int[]> possibleAttack = new ArrayList<int[]>();
        possibleMove.add(new int[]{8, 13});
        possibleAttack.add(new int[]{8, 13});

        assertTrue(playerPiece.checkValidMove(possibleMove, playerArray, cpuArray, possibleAttack, 8, 13));
        assertFalse(playerPiece.checkValidMove(possibleMove, playerArray, cpuArray, possibleAttack, 13, 13));

        playerPiece.getFutureMove(playerArray, cpuArray, possibleAttack);
    }

    @Test 
    // test possible moves 
    public void possibleMovesTest() {

        for (int i = 0; i < 1; i++) {
        playerArray.add(new Pawn(0, 48, "pawn", 'b', 1.00, testImage, playerColour));
        playerArray.add(new Rook(0, 0, "rook", 'b', 5.25, testImage, playerColour));
        playerArray.add(new Knight(48, 624, "knight", 'w', 2, testImage, playerColour));
        playerArray.add(new Bishop(96, 624, "bishop", 'w', 3.625, testImage, playerColour));
        playerArray.add(new Archbishop(144, 624, "archbishop", 'w', 7.5, testImage, playerColour));
        playerArray.add(new Camel(192, 624, "camel", '2', 2, testImage, playerColour));
        playerArray.add(new General(240, 624, "general", 'w', 5, testImage, playerColour));
        playerArray.add(new Amazon(288, 624, "amazon", 'w', 12, testImage, playerColour));
        playerArray.add(new King(336, 624, "king", 'w', 100, testImage, playerColour));
        playerArray.add(new Chancellor(480, 624, "chancellor", 'w', 8.5, testImage, playerColour));
        playerArray.add(new Queen(96, 144, "queen", 'w', 9.5, testImage, playerColour));

        cpuArray.add(new Pawn(0, 576, "pawn", 'w', 1.00, testImage, playerColour));

        cpuArray.add(new Rook(0, 624, "rook", 'w', 5.25, testImage, playerColour));
        cpuArray.add(new Knight(48, 0, "knight", 'b', 2, testImage, playerColour));
        cpuArray.add(new Bishop(96, 0, "bishop", 'b', 3.625, testImage, playerColour));
        cpuArray.add(new Archbishop(144, 0, "archbishop", 'b', 7.5, testImage, playerColour));
        cpuArray.add (new Camel(192, 0, "camel", 'b', 2, testImage, playerColour));
        cpuArray.add(new General(240, 0, "general", 'b', 5, testImage, playerColour));
        cpuArray.add(new Amazon(288, 0, "amazon", 'b', 12, testImage, playerColour));
        cpuArray.add(new King(336, 0, "king", 'b', 100, testImage, playerColour));
        cpuArray.add(new Chancellor(480, 0, "chancellor", 'b', 8.5, testImage, playerColour));
        cpuArray.add(new Queen(96, 96, "queen", 'b', 9.5, testImage, playerColour));
        
        Pieces piece = new Pawn(0, 576, "pawn", 'b', 1.00, testImage, playerColour);

        for (Pieces p : playerArray) {
            p.getPossibleMoves(playerArray, cpuArray);
        }

        piece.getPossiblePawnMoves(0, 12, playerArray, cpuArray, 'w');
        piece.getPossiblePawnMoves(0, 1, playerArray, cpuArray, 'b');

        piece.getPossiblePawnMoves(0, -1, cpuArray,  playerArray,'w');
        piece.getPossiblePawnMoves(0, -1,cpuArray,  playerArray, 'b');

        piece.getPossiblePawnMoves(0, -1, cpuArray,  playerArray, 'w');
        piece.getPossiblePawnMoves(0, -1, cpuArray, playerArray, 'b');

        piece.getPossiblePawnMoves(0, -2, cpuArray,  playerArray, 'w');
        piece.getPossiblePawnMoves(0, -2, cpuArray, playerArray, 'b');

        playerColour = "black";

        }
    }
 }
