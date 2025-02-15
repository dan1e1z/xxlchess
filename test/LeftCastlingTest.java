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

public class LeftCastlingTest extends App {
    
    private String testConfigPath;

    @Test
    public void mousePressedTest() {
        // mimic castling 
        testConfigPath = "src/test/java/XXLChess/configTest/configCastlingTest.json";
        App appCastle = new App(testConfigPath);
        PApplet.runSketch(new String[] {"App"}, appCastle);

        // left - side
        appCastle.setup();
        appCastle.delay(1000); // Wait for the setup to complete

        // click king title
        MouseEvent event = new MouseEvent(appCastle, 501, 201, 0, 336, 624, 37, 1);
        appCastle.mousePressed(event);
        appCastle.delay(1000); // Wait for the move animation to complete

        // click title for king to move too - test left side castle
        event = new MouseEvent(appCastle, 501, 201, 0, 240, 624, 37, 1);
        appCastle.mousePressed(event);
        appCastle.delay(1000); // Wait for the move animation to complete
        appCastle.dispose();
    }
}
