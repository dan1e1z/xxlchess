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

public class CheckmateTest extends App {
    
    private PImage testImage;
    private Pieces playerPiece;
    private Pieces cpuPiece;
    private String playerColour = "white";
    private ArrayList<Pieces> playerArray = new ArrayList<>();
    private ArrayList<Pieces> cpuArray = new ArrayList<>();
    private String testConfigPath;


    @Test
    // test for checkmate
    public void checkmateTest() {

        // test making a invalid move while in check 
        testConfigPath = "src/test/java/XXLChess/configTest/configCheckmateTest.json";
        App appCheckmate = new App(testConfigPath);
        PApplet.runSketch(new String[] {"App"}, appCheckmate);

        appCheckmate.setup();
        appCheckmate.delay(1000);


        playerArray.add(new King(0, 624, "king", 'w', 100, testImage, playerColour));
        cpuArray.add(new Queen(624, 576, "queen", 'b', 9.5, testImage, playerColour));
        cpuArray.add(new Queen(576, 624, "queen", 'b', 9.5, testImage, playerColour));
        playerArray.add(new Rook(624, 624, "rook", 'b', 5.25, testImage, playerColour));
        Pieces cpuPiece = new Queen(624, 576, "queen", 'b', 9.5, testImage, playerColour);
        
        checkForCheckmate(cpuPiece, playerArray, cpuArray);

        // check that cpu king isn not in checkmate 
        cpuArray.clear();
        cpuArray.add(new Queen(624, 576, "queen", 'b', 9.5, testImage, playerColour));
        checkForCheckmate(cpuPiece, playerArray, cpuArray);

        // test check 
        playerArray.clear();
        cpuArray.clear();
        playerArray.add(new King(0, 624, "king", 'w', 100, testImage, playerColour));
        cpuArray.add(new Queen(624, 624, "queen", 'b', 9.5, testImage, playerColour));
        Check(playerArray, cpuArray);
        appCheckmate.dispose();
    }
}
