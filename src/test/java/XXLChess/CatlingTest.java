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

public class CatlingTest extends App {
    
    private PImage testImage;
    private Pieces playerPiece;
    private Pieces cpuPiece;
    private String playerColour = "white";
    private ArrayList<Pieces> playerArray = new ArrayList<>();
    private ArrayList<Pieces> cpuArray = new ArrayList<>();
    private String testConfigPath;


    @Test
    // mimic castling 
    public void mousePressedTest() {
        testConfigPath = "src/test/java/XXLChess/configTest/configCastlingTest.json";
        App appCastle = new App(testConfigPath);
        PApplet.runSketch(new String[] {"App"}, appCastle);

        // Right side - first castling
        appCastle.setup();
        appCastle.delay(1000); 

        // Click king title
        MouseEvent event = new MouseEvent(appCastle, 501, 201, 0, 336, 624, 37, 1);
        appCastle.mousePressed(event);
        appCastle.delay(200); 

        // Click title for king to move too - test right side castle
        event = new MouseEvent(appCastle, 501, 201, 0, 432, 624, 37, 1);
        appCastle.mousePressed(event);
        appCastle.delay(1000); 

        // Left side - second castling
        appCastle.setup();
        appCastle.delay(3000); 

        // Click king title
        event = new MouseEvent(appCastle, 501, 201, 0, 336, 624, 37, 1);
        appCastle.mousePressed(event);
        appCastle.delay(200);

        // Click title for king to move too - test left side castle
        event = new MouseEvent(appCastle, 501, 201, 0, 240, 624, 37, 1);
        appCastle.mousePressed(event);
        appCastle.delay(1000);

        appCastle.dispose();
    }

    @Test
    // test that castling has been disabled
    public void disabledCatling() {

        playerPiece = new King(192, 624, "king", 'w', 100, testImage, "white"); 
        playerArray.add(new Knight(0, 624, "knight", 'b', 2, testImage, playerColour));
        playerArray.add(new Rook(0, 624, "rook", 'b', 5.25, testImage, playerColour));
        playerArray.add(new King(192, 624, "king", 'w', 100, testImage, playerColour));
        playerPiece.checkCanCastle(playerArray, cpuArray);
    }

}
