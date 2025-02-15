package XXLChess;                    

//import org.reflections.Reflections;
//import org.reflections.scanners.Scanners;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;
import processing.core.PFont;
import processing.event.MouseEvent;
import processing.core.PGraphics;
import processing.core.PConstants;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.util.*;


/**
 * This class represents a sidebar in a chess game.
 * It is responsible for displaying information about the game,
 *  such as the current player, the time remaining. 
 * This includes the text for messenges.
 */
public class Sidebar {
    
    /**
     * total time, which has been defined in cconfig.json
     */
    private int totalTime;

    /**
     * number of frames which has occured during a players turn
     */
    private int frameCount = 0;

    /**
     * time increment after move ends, defined in config.json
     */
    private int timeIncrement;

    /**
     * formted string version of time for player
     */
    private String formattedTimeString = "";


    /**
     * boolean flag, wheather or not a player has won
     */
    private boolean winner = false;

    /**
     * boolean flag, wheather or not a players king is defended
    */
    private boolean defendKing = true;


    /**
     * constructor for sidebar, gray rectangle in GUI
     * @param totalTime total time on clock
     * @param timeIncrement time increment
     */
    public Sidebar(int totalTime, int timeIncrement) {
        
        // player and cpu time
        this.totalTime = totalTime;

        // player and cpu time increment
        this.timeIncrement = timeIncrement;

        // count number of total frames
        this.frameCount = frameCount;

        // formated string for sidebar text display
        this.formattedTimeString = formattedTimeString;

        // winner 
        this.winner = winner;

        // defend King
        this.defendKing = defendKing;
    }

    /**
     * get Winner - if game has ended
     * @return boolean, true or false 
     */
    public boolean getWinner() {
        return this.winner;
    }

    /**
     * set winner to false, if game has reset
     */
    public void setWinner() {
        this.winner = false;
    }

    /**
     * get DenfedKing - if trying to make an illegal move
     * @return boolean, true of false
     */
    public boolean getDefendKing() {
        return this.defendKing;
    }

    /**
     * set DefendKing to true
     */
    public void setDefendKing() {
        this.defendKing = true;
    }

    /**
     * player Timer - counts down and adds to sidebar
     * @param app reference to the Processing application object
     */
    public void playerTimer(PApplet app) {

        // add one to countdownCount - updated each frame 
        this.frameCount++;

        // frameCount - timer time
        int totalSeconds = this.totalTime - (this.frameCount / 60);

        // check if player has run out of time
        if (totalSeconds < 0) {
            cpuTimeWinner(app);
            return;
        }

        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        String formattedTime = String.format("%d:%02d", minutes, seconds);
    
        this.formattedTimeString = formattedTime;

        app.fill(204,204,204,255);
        app.rect(672, 0, 120, 672);
        app.fill(0);
        app.textSize(18);

        app.text(this.formattedTimeString, 680 , 624);

    }

    /**
     * cpu timer - counts down and adds to sidebar
     * @param app reference to the Processing application object
     */
    public void cpuTimer(PApplet app) {

        // add one to countdownCount - updated each frame 
        this.frameCount++;

        // frameCount - timer time
        int totalSeconds = this.totalTime - (this.frameCount / 60);

        // check if player has run out of time
        if (totalSeconds < 0) {
            playerTimeWinner(app);
            this.winner = true;
            return;
        }

        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        String formattedTime = String.format("%d:%02d", minutes, seconds);
        this.formattedTimeString = formattedTime;

        app.fill(204,204,204,255);
        app.rect(672, 0, 120, 672);
        app.fill(0);
        app.textSize(18);

        app.text(this.formattedTimeString, 680 , 48);
    }

    /**
     * displays waiting players timer
     * @param app reference to the Processing application object
     * @param y which end of the sibar time is being diplayed
     */
    public void displayOther(PApplet app, int y) {

        app.fill(0);
        app.textSize(18);
        app.text(this.formattedTimeString, 680 , y);
    }

    /**
     * Display player won by time
     * @param app reference to the Processing application object
     */
    public void  playerTimeWinner(PApplet app) {
        this.winner = true;
        app.fill(0);
        app.textSize(18);
        app.text("You won on \ntime", 680 , 280);
        app.text("Press 'r' to \nrestart the \ngame", 680 , 350);
    }

    /**
     * Displaycpu won by time
     * @param app reference to the Processing application object
     */
    public void cpuTimeWinner(PApplet app) {
        this.winner = true;
        app.fill(0);
        app.textSize(18);
        app.text("You lost on \ntime", 680 , 280);
        app.text("Press 'r' to \nrestart the \ngame", 680 , 350);
    }

    /**
     * Display player won by checkmate
     * @param app reference to the Processing application object
     */
    public void  playerCheckmateWinner(PApplet app) {
        this.winner = true;
        app.fill(204,204,204,255);
        app.rect(672, 0, 120, 672);
        app.fill(0);
        app.textSize(18);
        app.text("You won by \ncheckmate", 680 , 280);
        app.text("Press 'r' to \nrestart the \ngame", 680 , 350);
    }

    /**
     * Display player won by checkmate
     * @param app reference to the Processing application object
     */
    public void cpuCheckmateWinner(PApplet app) {
        this.winner = true;
        app.fill(0);
        app.textSize(18);
        app.text("You lost by \ncheckmate", 680 , 280);
        app.text("Press 'r' to \nrestart the \ngame", 680 , 350);
    }


    /**
     * Display draw
     * @param app reference to the Processing application object
     */
    public void drawWinner(PApplet app) {
        this.winner = true;
        app.fill(0);
        app.textSize(18);
        app.text("Stalemate – \ndraw", 680 , 280);
        app.text("Press 'r' to \nrestart the \ngame", 680 , 350);

    }

    /**
     * Display cpu king is in check
     * @param app reference to the Processing application object
     */
    public void cpuKingCheck(PApplet app) {
        app.fill(0);
        app.textSize(18);
        app.text("CHECK!", 680 , 336);
    }

    /**
     * Display cpu king is in check
     * @param app reference to the Processing application object
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @param playerPiece piece object of player
     */
    public void playerKingCheck(PApplet app, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray, Pieces playerPiece) {
        app.fill(0);
        app.textSize(18);
        app.text("CHECK!", 680 , 336);
        Pieces king = Pieces.getKing(playerArray, cpuArray);
        
        if (playerPiece != king) {
            app.fill(201,0,0,255);
        }
        else {app.fill(109,137,76,255);} // green
        app.rect(king.getX(), king.getY(), 48, 48);    
    }


    /**
     * player has resigned 
     * @param app reference to the Processing application object
     */
    public void resign(PApplet app) {
        this.winner = true;
        app.fill(0);
        app.textSize(18);
        app.text("You \nresigned", 680, 280);
        app.text("Press 'r' to \nrestart the \ngame", 680 , 350);
    }

    /**
     * add time increment 
     */
    public void addTimeIncrement() {
        this.totalTime += this.timeIncrement;
    }

    /**
     * Set formated string for cpu
     */
    public void setFormattedTimeString() {
        int totalSeconds = this.totalTime - (this.frameCount / 60);

        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        String formattedTime = String.format("%d:%02d", minutes, seconds);
        this.formattedTimeString = formattedTime;
    }

    /**
     * displays : “You must defend your king!” on sidebar, and flashes king
     * @param app reference to the Processing application object
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     */
    public void defendKing(PApplet app, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {
        
        Pieces king = Pieces.getKing(playerArray, cpuArray); 

        this.frameCount++;
        int flashIndex = (int)(this.frameCount / 30.0 / 0.5);
        if (flashIndex % 2 == 0 ) {
            app.fill(201,0,0,255);
            app.rect(king.getX(), king.getY(), 48, 48);
        } else {
            if ((king.getX() / 48 + king.getY() / 48) % 2 == 0) {
                app.fill(237,216,179,255);  // white
            } else {
                app.fill(175,135,97,255);  // brown
            }
            // fills in corresponding color into created rectangle 
            app.rect(king.getX(), king.getY(), 48, 48);  
        }

        this.defendKing = false;

        app.fill(0);
        app.textSize(18);
        app.text("You must \ndefend \nyour king!", 680, 280);

        if (flashIndex >= 6) {
            this.defendKing = true;
            this.frameCount = 0;

            if ((king.getX() / 48 + king.getY() / 48) % 2 == 0) {
                app.fill(237,216,179,255);  // white
            } else {
                app.fill(175,135,97,255);  // brown
            }
            app.rect(king.getX(), king.getY(), 48, 48);  
        }
    }
}