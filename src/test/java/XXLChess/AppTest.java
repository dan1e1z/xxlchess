package XXLChess;


import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import processing.data.JSONObject;
import java.io.File;
import java.util.*;
import processing.core.*;
import processing.event.KeyEvent;
import processing.event.MouseEvent;


public class AppTest extends App{

    private PImage testImage;
    private Pieces playerPiece;
    private Pieces cpuPiece;
    private String playerColour = "white";
    private ArrayList<Pieces> playerArray = new ArrayList<>();
    private ArrayList<Pieces> cpuArray = new ArrayList<>();
    private String testConfigPath;



    @Test
    // test cpu alogirthm - dealing with check
    public void cpuCheckAlgorithmTest() {
        testConfigPath = "src/test/java/XXLChess/configTest/configRegularTest.json";
        App app = new App(testConfigPath);
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);

        cpuArray.add(new King(0, 624, "king", 'b', 100, app.loadImage("src/main/resources/XXLChess/b-king.png"), playerColour));
        cpuArray.add(new Queen(0, 528, "queen", 'b', 9.5, app.loadImage("src/main/resources/XXLChess/b-queen.png"), playerColour));


        playerPiece = new Queen(96, 624, "queen", 'w', 9.5, app.loadImage("src/main/resources/XXLChess/w-queen.png"), playerColour);
        playerArray.add(new Queen(96, 624, "queen", 'w', 9.5, app.loadImage("src/main/resources/XXLChess/w-queen.png"), playerColour));
 
        // take checking piece
        app.cpuCheckAlgorithm(playerPiece, playerArray, cpuArray);


        playerArray.add(new Queen(192, 576, "queen", 'w', 9.5, app.loadImage("src/main/resources/XXLChess/w-queen.png"), playerColour));
        playerArray.add(new Queen(624, 624, "queen", 'w', 9.5, app.loadImage("src/main/resources/XXLChess/w-queen.png"), playerColour));
        
        cpuArray.clear();
        cpuArray.add(new King(0, 624, "king", 'b', 100, app.loadImage("src/main/resources/XXLChess/b-king.png"), playerColour));

        // move king away from check
        app.cpuCheckAlgorithm(playerPiece, playerArray, cpuArray);

        //block from check 
        playerArray.clear();
        cpuArray.clear();
        app.checkForDraw(playerArray, cpuArray);

        
        cpuArray.add(new King(0, 624, "king", 'b', 100, app.loadImage("src/main/resources/XXLChess/b-king.png"), playerColour));
        cpuArray.add(new Queen(0, 528, "queen", 'b', 9.5, app.loadImage("src/main/resources/XXLChess/b-queen.png"), playerColour));
        
        playerArray.add(new Queen(192, 576, "queen", 'w', 9.5, app.loadImage("src/main/resources/XXLChess/w-queen.png"), playerColour));
        playerArray.add(new Queen(624, 624, "queen", 'w', 9.5, app.loadImage("src/main/resources/XXLChess/w-queen.png"), playerColour));

        app.cpuCheckAlgorithm(playerPiece, playerArray, cpuArray);

        playerArray.clear();
        cpuArray.clear();
        
        playerArray.add(new Queen(192, 576, "queen", 'w', 9.5, app.loadImage("src/main/resources/XXLChess/w-queen.png"), playerColour));
        playerArray.add(new Queen(624, 624, "queen", 'w', 9.5, app.loadImage("src/main/resources/XXLChess/w-queen.png"), playerColour));
        
        cpuArray.add(new King(0, 624, "king", 'b', 100, app.loadImage("src/main/resources/XXLChess/b-king.png"), playerColour));

        app.cpuCheckAlgorithm(playerPiece, playerArray, cpuArray);
    }

    @Test 
    // cpu movement 
    public void cpuMovementTest() {

        testConfigPath = "src/test/java/XXLChess/configTest/configRegularTest.json";
    
        App app = new App(testConfigPath);
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        // take checking piece

        for (int i = 0; i < 6; i++) {
        // check if black pieces is being attacked - take if value is larger/same
        playerArray.add(new Queen(192, 624, "queen", 'w', 9.5, testImage, playerColour));
        cpuArray.add(new Queen(0, 624, "queen", 'b', 9.5, testImage, playerColour));
        playerPiece = new Queen(192, 624, "queen", 'w', 9.5, testImage, playerColour); 

        ArrayList<int[]> possibleAttack = new ArrayList<int[]>();

        possibleAttack.add(new int[]{0, 624});
        app.cpuMovement(playerPiece, playerArray, cpuArray, possibleAttack);
        app.delay(1000);

        // check if piece is required to move - playerPiece is lower value
        playerArray.clear();
        cpuArray.clear();

        playerArray.add(new Pawn(96, 624, "pawn", 'w', 1, testImage, playerColour));
        cpuArray.add(new Queen(48, 576, "queen", 'b', 9.5, testImage, playerColour));
        possibleAttack.clear();
        possibleAttack.add(new int[]{48, 576});

        app.cpuMovement(playerPiece, playerArray, cpuArray, possibleAttack);
        app.delay(1000);

        // check random move 

        playerArray.clear();
        cpuArray.clear();

        playerArray.add(new Pawn(96, 624, "pawn", 'w', 1, testImage, "white"));
        cpuArray.add(new Queen(48, 576, "queen", 'b', 9.5, testImage, "white"));
        possibleAttack.clear();
        possibleAttack.add(new int[]{48, 576});

        app.cpuMovement(playerPiece, playerArray, cpuArray, possibleAttack);
        app.delay(1000);


        playerArray.clear();
        cpuArray.clear();

        playerArray.add(new Pawn(0, 624, "pawn", 'w', 1, testImage, "white"));
        cpuArray.add(new Pawn(0, 0, "pawn", 'b', 1, testImage, "white"));
        possibleAttack.clear();
        possibleAttack.add(new int[]{48, 576});

        app.cpuMovement(playerPiece, playerArray, cpuArray, possibleAttack);
        app.delay(1000);
        }

    }

    @Test 
    // test key pressed
    public void testKeyPressed() {

        testConfigPath = "src/test/java/XXLChess/configTest/configRegularTest.json";

        App app = new App(testConfigPath);
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        playerPiece = new Pawn(48, 144, "pawn", 'w', 1, testImage, playerColour);

        cpuArray.add(new Knight(0, 96, "knight", 'b', 2, testImage, playerColour));
        cpuArray.add(new Rook(0, 0, "rook", 'b', 5.25, testImage, playerColour));
        playerArray.add(new King(0, 624, "king", 'w', 100, testImage, playerColour));
        ArrayList<int[]> possibleAttack = new ArrayList<int[]>();
        possibleAttack.add(new int[]{0,2});
        possibleAttack.add(new int[]{0,4});

        app.cpuMovement(playerPiece, playerArray, cpuArray, possibleAttack);
        app.key = 'e';
        app.keyPressed();
        app.key = 'r';
        app.keyPressed();
    }
}
