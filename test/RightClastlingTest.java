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

public class RightClastlingTest extends App {
    
    private String testConfigPath;

    @Test
    public void mousePressedTest() {
        // mimic castling 
        testConfigPath = "src/test/java/XXLChess/configTest/configCastlingTest.json";
        App appCastleRight = new App(testConfigPath);
        PApplet.runSketch(new String[] {"App"}, appCastleRight);

        // right - side
        appCastleRight.setup();
        appCastleRight.delay(1000); // Wait for the setup to complete

        // click king title
        MouseEvent event = new MouseEvent(appCastleRight, 501, 201, 0, 336, 624, 37, 1);
        appCastleRight.mousePressed(event);
        appCastleRight.delay(200); // Wait for the move animation to complete

        // click title for king to move too - test right side castle
        event = new MouseEvent(appCastleRight, 501, 201, 0, 432, 624, 37, 1);
        appCastleRight.mousePressed(event);
        appCastleRight.delay(1000); // Wait for the move animation to complete
        appCastleRight.dispose();
    }

}
