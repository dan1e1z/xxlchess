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


public class CpuDrawTest{
    
    private PImage testImage;
    private Pieces playerPiece;
    private Pieces cpuPiece;
    private String playerColour = "white";
    private ArrayList<Pieces> playerArray = new ArrayList<>();
    private ArrayList<Pieces> cpuArray = new ArrayList<>();
    private String testConfigPath;

    @Test 
    // special movement case
    public void speicalAlgorithmTest() {

        // testConfigPath = "src/test/java/XXLChess/configTest/configRegularTest.json";
        testConfigPath = "src/test/java/XXLChess/configTest/configRegularTest.json";

        App app = new App(testConfigPath);
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        playerPiece = new Pawn(48, 144, "pawn", 'w', 1, app.loadImage("src/main/resources/XXLChess/b-pawn.png"), playerColour);

        cpuArray.add(new Knight(0, 96, "knight", 'b', 2, app.loadImage("src/main/resources/XXLChess/b-knight.png"), playerColour));
        cpuArray.add(new Rook(0, 0, "rook", 'b', 5.25, app.loadImage("src/main/resources/XXLChess/b-rook.png"), playerColour));
        ArrayList<int[]> possibleAttack = new ArrayList<int[]>();
        possibleAttack.add(new int[]{0,2});
        possibleAttack.add(new int[]{0,4});

        app.cpuMovement(playerPiece, playerArray, cpuArray, possibleAttack);
        // app.delay(2000);
    }
}
