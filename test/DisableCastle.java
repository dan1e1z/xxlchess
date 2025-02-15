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


public class DisableCastle extends App {
    
    private String testConfigPath;

    @Test
    // move king or rook, disable castling
    public void disableCastlingTest() {
        testConfigPath = "src/test/java/XXLChess/configTest/configCastlingTest.json";
        App appNotCastle = new App(testConfigPath);
        PApplet.runSketch(new String[] {"App"}, appNotCastle);

        // move king - not castle
        appNotCastle.setup();
        appNotCastle.delay(1000); // Wait for the setup to complete

        MouseEvent event = new MouseEvent(appNotCastle, 501, 201, 0, 336, 624, 37, 1);
        appNotCastle.mousePressed(event);
        appNotCastle.delay(200); // Wait for the move animation to complete

        event = new MouseEvent(appNotCastle, 501, 201, 0, 288, 624, 37, 1);
        appNotCastle.mousePressed(event);
        appNotCastle.delay(2000); // Wait for the move animation to complete

        // move rook - not castle
        appNotCastle.setup();
        appNotCastle.delay(200); // Wait for the setup to complete

        event = new MouseEvent(appNotCastle, 501, 201, 0, 0, 624, 37, 1);
        appNotCastle.mousePressed(event);
        appNotCastle.delay(200); // Wait for the move animation to complete

        event = new MouseEvent(appNotCastle, 501, 201, 0, 48, 624, 37, 1);
        appNotCastle.mousePressed(event);
        appNotCastle.delay(2000); // Wait for the move animation to complete
    }
    
}
