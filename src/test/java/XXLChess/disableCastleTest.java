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

public class disableCastleTest extends App {
    
    private PImage testImage;
    private Pieces playerPiece;
    private Pieces cpuPiece;
    private String playerColour = "white";
    private ArrayList<Pieces> playerArray = new ArrayList<>();
    private ArrayList<Pieces> cpuArray = new ArrayList<>();
    private String testConfigPath;

    @Test
    // test disable castling by moving king or rook
    public void mousePressedTest() {
        testConfigPath = "src/test/java/XXLChess/configTest/configCastlingTest.json";
        App appCastle = new App(testConfigPath);
        PApplet.runSketch(new String[] {"App"}, appCastle);

        // move king left
        appCastle.setup();
        appCastle.delay(1000); 

        // click king title
        MouseEvent event = new MouseEvent(appCastle, 501, 201, 0, 336, 624, 37, 1);
        appCastle.mousePressed(event);
        appCastle.delay(500); // Wait for the move animation to complete

        // click title for king to move 
        event = new MouseEvent(appCastle, 501, 201, 0, 288, 624, 37, 1);
        appCastle.mousePressed(event);
        appCastle.delay(2000); 
    }
}
