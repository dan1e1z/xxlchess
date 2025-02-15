package XXLChess;


import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import processing.core.*;


public class SidebarTest extends App {

    private Sidebar text;
    private Sidebar playerTimer;
    private Sidebar cpuTimer;

    private PImage testImage;
    private String playerColour;

    private ArrayList<Pieces> playerArray = new ArrayList<>();
    private ArrayList<Pieces> cpuArray = new ArrayList<>();

    private Pieces playerPiece;
    private String testConfigPath;


    @Test
    // constructing playerTimer, cpuTimer and Sidebar text object
    public void sidebarConstructorTest() {
        text = new Sidebar(0, 0);
        assertNotNull(text);
    }

    @Test 
    // test playerTimer and cpuTimer
    public void timerTest() {
        PApplet app = new PApplet();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);

        text = new Sidebar(1, 1);
        app.delay(200);
        text.cpuTimer(app);
        app.dispose();

    }

    @Test 
    // test draw into sidebar
    public void drawSidebar() {
        PApplet app = new PApplet();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);

        text = new Sidebar(0, 0);
        app.delay(200);
        text.playerTimeWinner(app);
        text.cpuTimeWinner(app);
        text.playerCheckmateWinner(app);
        text.cpuCheckmateWinner(app);
        text.drawWinner(app);
        text.cpuKingCheck(app);
        text.resign(app);
        text.addTimeIncrement();
        text.displayOther(app, 280);

        cpuArray.add(new King(336, 0, "king", 'b', 100, testImage, playerColour));
        playerArray.add(new King(366, 624, "king", 'w', 100, testImage, playerColour));

        text.playerKingCheck(app, playerArray, cpuArray, playerPiece);
        
        for (int i = 0; i < 360; i++) {
            text.defendKing(app, playerArray, cpuArray);
            text.playerCheckmateWinner(app);

        }
        app.dispose();

    }

    @Test 
    // test setters and getters
    public void setandgetTest() {
        
        Sidebar text = new Sidebar(0, 0);

        text.getWinner();
        text.setWinner();
        text.getDefendKing();
        text.setDefendKing();
    }

    @Test
    // test player and cpu timer
    public void TimerTest() {

        Sidebar playerTimer = new Sidebar(0 , 0);
        Sidebar cpuTimer = new Sidebar(0 , 0);

        testConfigPath = "src/test/java/XXLChess/configTest/configRegularTest.json";

        PApplet app = new PApplet();
        PApplet.runSketch(new String[] {"App"}, app);

        app.setup();
        app.delay(1000);

        for (int i = 0; i< 360; i++) {
            playerTimer.playerTimer(app);
            cpuTimer.cpuTimer(app);
            playerTimer.setFormattedTimeString();
            cpuTimer.setFormattedTimeString();
        }
        app.dispose();

    }
}