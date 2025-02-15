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

public class MoveCheck extends App{
    
    private String testConfigPath;

    @Test
    // move into check
    public void moveToCheckTest() {

        // test making a invalid move while in check 
        testConfigPath = "src/test/java/XXLChess/configTest/configCheckTest.json";
        App appCheck = new App(testConfigPath);
        PApplet.runSketch(new String[] {"App"}, appCheck);

        appCheck.setup();
        appCheck.delay(1000);

        // click king title 
        MouseEvent event = new MouseEvent(appCheck, 501, 201, 0, 0, 624, 37, 1);
        appCheck.mousePressed(event);
        appCheck.delay(500);


        // click title for king to move too - toward queen
        event = new MouseEvent(appCheck, 501, 201, 0, 48, 624, 37, 1);
        appCheck.mousePressed(event);
        appCheck.delay(2000);
        appCheck.dispose();

    }
}
