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
 * 
 * This class represents the main application class.
 * It extends the PApplet class, 
 * which provides basic functionality for drawing and 
 * interacting with the user.
 * 
 */
public class App extends PApplet{

    /**
     * size of sprite
     */
    public static final int SPRITESIZE = 480;

    /**
     * cell size, size of each chess square
     */
    public static final int CELLSIZE = 48;

    /**
     * sidebar size, where message and timer is shown
     */
    public static final int SIDEBAR = 120;

    /**
     * board width size 
     */
    public static final int BOARD_WIDTH = 14;

    /**
     * wdith of game screen
     */
    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;

    /**
     * height of game screen
     */
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE;

    /**
     * fps of game
     */
    public static final int FPS = 60;
	
    /**
     * configPath, path to json which will be read for game configuration.
     */
    public String configPath;

    /**
     * Black ArrayList
     */
    protected ArrayList<Pieces> cpuArray = new ArrayList<Pieces>();

    /**
     * White ArrayList
     */
    protected ArrayList<Pieces> playerArray = new ArrayList<Pieces>();

    /**
     * loading app
     */
    public App() {
        this.configPath = "config.json";
    }

    /**
     * for testing different cofig.json files
     * @param configPath path to config.json file
     */
    public App(String configPath) {
        this.configPath = configPath;
    }

    // /**
    //  * Switch player colour to black piece
    //  * @param board char representation of the chess board 
    //  * @return board char representation of the chess board 
    //  */
    // public static char[][] switchToBlack(char[][] board){

    //     int rows = board.length;
    //     int cols = board[0].length;

    //     for (int i = 0; i < rows / 2; i++) {
    //         char[] temp = board[i];
    //         board[i] = board[rows - i - 1];
    //         board[rows - i - 1] = temp;
    //     }
    //     return board;
    // }

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * create an array to represent the board
     */
    public static char[][] board = new char[14][14];

    /**
     * movement speed of pieces, from config file
     */
    static double speed;

    /**
     * maximum time taken for piece animation, from config file
     */
    static double maxTime;

    /*
     * sidebar playerTimer object
     */
    Sidebar playerTimer;

    /**
     * sidebar cpuTimer object
     */
    Sidebar cpuTimer;

    /**
     * sidebar text object
     */
    Sidebar text;

    /**
     * player king is in check 
     */
    boolean playerChecked = false;

    /**
     * check if castling can occur 
     */
    boolean canCastle = true;

    /**
     * castling animation 
     */
    boolean castleAnimation = false;

    /**
     * player Colour
     */
    String playerColour;

    /**
     * cpu first move
     */
    boolean cpuFirstMove = false;


    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */
    public void setup() {
        frameRate(FPS);
        // PApplet Board = new Board();


        // Load images during setup


		// load config
        JSONObject conf = loadJSONObject(new File(this.configPath));

        String layout = conf.getString("layout");

        JSONObject timeControls = conf.getJSONObject("time_controls");
        JSONObject player = timeControls.getJSONObject("player");
        JSONObject cpu = timeControls.getJSONObject("cpu");

        // playerTime 

        int playerTime = player.getInt("seconds");
        int playerIncrement = player.getInt("increment");

        playerTimer = new Sidebar(playerTime, playerIncrement);

        // cpuTime 

        int cpuTime = cpu.getInt("seconds");
        int cpuIncrement = cpu.getInt("increment");

        cpuTimer = new Sidebar(cpuTime, cpuIncrement);



        // speed config
        speed = conf.getDouble("piece_movement_speed");

        // max time config
        maxTime = conf.getDouble("max_movement_time");

        // set speed and max time
        Pieces.setSpeedAndMaxTime(speed, maxTime);

        // reads txt layout file
        try {
            File file = new File(layout);
            Scanner scan = new Scanner(file);
            int j = 0;
            while (scan.hasNextLine()) {
              String line = scan.nextLine();
              char[] charArray = line.toCharArray(); 
              for (int i = 0; i < charArray.length; i++) {
                char temp = charArray[i]; // add each character to the ArrayList
                board[j][i] = temp;
              }
              j++;
            }
            scan.close();
        
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        String playerColour = conf.getString("player_colour");

        // check if selected piece colour is black 
        if (playerColour.equals("black")) {
            // board = switchToBlack(board);
	        cpuFirstMove = true;
        }
        
        
        // load in all piece images
        PImage bPawn = loadImage("src/main/resources/XXLChess/b-pawn.png");
        PImage wPawn = loadImage("src/main/resources/XXLChess/w-pawn.png");

        PImage bRook = loadImage("src/main/resources/XXLChess/b-rook.png");
        PImage wRook = loadImage("src/main/resources/XXLChess/w-rook.png");

        PImage bKnight = loadImage("src/main/resources/XXLChess/b-knight.png");
        PImage wKnight = loadImage("src/main/resources/XXLChess/w-knight.png");

        PImage bBishop = loadImage("src/main/resources/XXLChess/b-bishop.png");
        PImage wBishop = loadImage("src/main/resources/XXLChess/w-bishop.png");

        PImage bArchbishop = loadImage("src/main/resources/XXLChess/b-archbishop.png");
        PImage wArchbishop = loadImage("src/main/resources/XXLChess/w-archbishop.png");

        PImage bCamel = loadImage("src/main/resources/XXLChess/b-camel.png");
        PImage wCamel = loadImage("src/main/resources/XXLChess/w-camel.png");

        PImage bGeneral = loadImage("src/main/resources/XXLChess/b-knight-king.png");
        PImage wGeneral = loadImage("src/main/resources/XXLChess/w-knight-king.png");

        PImage bAmazon = loadImage("src/main/resources/XXLChess/b-amazon.png");
        PImage wAmazon = loadImage("src/main/resources/XXLChess/w-amazon.png");

        PImage bKing = loadImage("src/main/resources/XXLChess/b-king.png");
        PImage wKing = loadImage("src/main/resources/XXLChess/w-king.png");

        PImage bChancellor = loadImage("src/main/resources/XXLChess/b-chancellor.png");
        PImage wChancellor = loadImage("src/main/resources/XXLChess/w-chancellor.png");

        PImage bQueen = loadImage("src/main/resources/XXLChess/b-queen.png");
        PImage wQueen = loadImage("src/main/resources/XXLChess/w-queen.png");

        // turn off rectangle borders
        noStroke();

        // loop through each row and column
        for (int row = 0; row < 14; row++) {
            for (int col = 0; col < 14; col++) {
            // calculate the x and y positions of the square
                int x = col * CELLSIZE;
                int y = row * CELLSIZE;

                // draw the square with the appropriate color
                if ((row + col) % 2 == 0) {
                    fill(237,216,179,255);
                } else {
                    fill(175,135,97,255);
                }

                // fills in corrosponding colour into created rectange 
                rect(x, y, CELLSIZE, CELLSIZE);
                char piece = board[row][col];

                if (board[row][col] != ' ' && playerColour.equals("white")) {

                    // creates loaded piece object for every piece in level.txt file
                    switch(piece) {
                        case 'P':
                            cpuArray.add(new Pawn(x, y, "pawn", 'b', 1.00, bPawn, playerColour));
                            break;

                        case 'p':
                            playerArray.add(new Pawn(x, y, "pawn", 'w', 1.00, wPawn, playerColour));
                            break;

                        case 'R':
                            cpuArray.add(new Rook(x, y, "rook", 'b', 5.25, bRook, playerColour));
                            break;
                            
                        case 'r':
                            playerArray.add(new Rook(x, y, "rook", 'w', 5.25, wRook, playerColour));
                            break;
                        
                        case 'N':
                            cpuArray.add(new Knight(x, y, "knight", 'b', 2, bKnight, playerColour));
                            break;

                        case 'n':
                            playerArray.add(new Knight(x, y, "knight", 'w', 2, wKnight, playerColour));
                            break;
                        
                        case 'B':
                            cpuArray.add(new Bishop(x, y, "bishop", 'b', 3.625, bBishop, playerColour));
                            break;

                        case 'b':
                            playerArray.add(new Bishop(x, y, "bishop", 'w', 3.625, wBishop, playerColour));
                            break;

                        case 'H':
                            cpuArray.add(new Archbishop(x, y, "archbishop", 'b', 7.5, bArchbishop, playerColour));
                            break;

                        case 'h':
                            playerArray.add(new Archbishop(x, y, "archbishop", 'w', 7.5, wArchbishop, playerColour));
                            break;

                        case 'C':
                            cpuArray.add(new Camel(x, y, "camel", 'b', 2, bCamel, playerColour));
                            break;

                        case 'c':
                            playerArray.add(new Camel(x, y, "camel", '2', 2, wCamel, playerColour));
                            break;
                        
                        case 'G':
                            cpuArray.add(new General(x, y, "general", 'b', 5, bGeneral, playerColour));
                            break;

                        case 'g':
                            playerArray.add(new General(x, y, "general", 'w', 5, wGeneral, playerColour));
                            break;

                        case 'A':
                            cpuArray.add(new Amazon(x, y, "amazon", 'b', 12, bAmazon, playerColour));
                            break;

                        case 'a':
                            playerArray.add(new Amazon(x, y, "amazon", 'w', 12, wAmazon, playerColour));
                            break;

                        case 'K':
                            cpuArray.add(new King(x, y, "king", 'b', 100, bKing, playerColour));
                            break;
                        case 'k':
                            playerArray.add(new King(x, y, "king", 'w', 100, wKing, playerColour));
                            break;

                        case 'E':
                            cpuArray.add(new Chancellor(x, y, "chancellor", 'b', 8.5, bChancellor, playerColour));
                            break;

                        case 'e':
                            playerArray.add(new Chancellor(x, y, "chancellor", 'w', 8.5, wChancellor, playerColour));
                            break;
                        case 'Q':
                            cpuArray.add(new Queen(x, y, "queen", 'b', 9.5, bQueen, playerColour));
                            break;
                        
                        case 'q':
                            playerArray.add(new Queen(x, y, "queen", 'w', 9.5, wQueen, playerColour));
                            break;
                    }
                }

                if (board[row][col] != ' ' && playerColour.equals("black")) {

                    // creates loaded piece object for every piece in level.txt file
                    switch(piece) {
                        case 'p':
                            cpuArray.add(new Pawn(x, y, "pawn", 'b', 1.00, bPawn, playerColour));
                            break;

                        case 'P':
                            playerArray.add(new Pawn(x, y, "pawn", 'w', 1.00, wPawn, playerColour));
                            break;

                        case 'r':
                            cpuArray.add(new Rook(x, y, "rook", 'b', 5.25, bRook, playerColour));
                            break;
                            
                        case 'R':
                            playerArray.add(new Rook(x, y, "rook", 'w', 5.25, wRook, playerColour));
                            break;
                        
                        case 'n':
                            cpuArray.add(new Knight(x, y, "knight", 'b', 2, bKnight, playerColour));
                            break;

                        case 'N':
                            playerArray.add(new Knight(x, y, "knight", 'w', 2, wKnight, playerColour));
                            break;
                        
                        case 'b':
                            cpuArray.add(new Bishop(x, y, "bishop", 'b', 3.625, bBishop, playerColour));
                            break;

                        case 'B':
                            playerArray.add(new Bishop(x, y, "bishop", 'w', 3.625, wBishop, playerColour));
                            break;

                        case 'h':
                            cpuArray.add(new Archbishop(x, y, "archbishop", 'b', 7.5, bArchbishop, playerColour));
                            break;

                        case 'H':
                            playerArray.add(new Archbishop(x, y, "archbishop", 'w', 7.5, wArchbishop, playerColour));
                            break;

                        case 'c':
                            cpuArray.add(new Camel(x, y, "camel", 'b', 2, bCamel, playerColour));
                            break;

                        case 'C':
                            playerArray.add(new Camel(x, y, "camel", '2', 2, wCamel, playerColour));
                            break;
                        
                        case 'g':
                            cpuArray.add(new General(x, y, "general", 'b', 5, bGeneral, playerColour));
                            break;

                        case 'G':
                            playerArray.add(new General(x, y, "general", 'w', 5, wGeneral, playerColour));
                            break;

                        case 'a':
                            cpuArray.add(new Amazon(x, y, "amazon", 'b', 12, bAmazon, playerColour));
                            break;

                        case 'A':
                            playerArray.add(new Amazon(x, y, "amazon", 'w', 12, wAmazon, playerColour));
                            break;

                        case 'k':
                            cpuArray.add(new King(x, y, "king", 'b', 100, bKing, playerColour));
                            break;
                        case 'K':
                            playerArray.add(new King(x, y, "king", 'w', 100, wKing, playerColour));
                            break;

                        case 'e':
                            cpuArray.add(new Chancellor(x, y, "chancellor", 'b', 8.5, bChancellor, playerColour));
                            break;

                        case 'E':
                            playerArray.add(new Chancellor(x, y, "chancellor", 'w', 8.5, wChancellor, playerColour));
                            break;
                        case 'q':
                            cpuArray.add(new Queen(x, y, "queen", 'b', 9.5, bQueen, playerColour));
                            break;
                        
                        case 'Q':
                            playerArray.add(new Queen(x, y, "queen", 'w', 9.5, wQueen, playerColour));
                            break;
                    }
                }
            }
        }
         // check if selected piece colour is black 
         if (playerColour.equals("black")) {
            // switch arraylists 
            ArrayList<Pieces> temp = new ArrayList<Pieces>();
            temp = playerArray;
            playerArray = cpuArray;
            cpuArray = temp;
        }

        // create time object 
        text = new Sidebar(0, 0);

        // set cpu formatted string manually
        cpuTimer.setFormattedTimeString();

        // set original king x-axis line for castling
        Pieces.setKingXAxis(playerArray);

    }
       
    /**
     * Receive key pressed signal from the keyboard.
    */
    public void keyPressed(){

        if (key == 'e' && ( !text.getWinner() && (!playerTimer.getWinner() && !cpuTimer.getWinner()))) {
            // Do something if the "e" key was pressed
            text.resign(this);
            // System.out.println("player has resign");
          }

          if (key == 'r' && (text.getWinner() || playerTimer.getWinner() || cpuTimer.getWinner())) {
            // player king is in check 
            playerChecked = false;
            // text.setWinner();
            playerTimer.setWinner();
            cpuTimer.setWinner();
            playerArray.clear();
            cpuArray.clear();
            setup();
        }
        
    }
    
    /**
     * Receive key released signal from the keyboard.
    */
    public void keyReleased(){

    }

    // create highlighted title object 
    private Board highlight = new Board();


    // boolean tracts second mouse click for moving piece
    private boolean movePiece = false;

    // all possible position moves - minus attack moves
    private ArrayList<int[]> possiblePositionsArray = new ArrayList<int[]>();

    // all possible attack positions 
    private ArrayList<int[]> possibleAttackPositionsArray = new ArrayList<int[]>();

    // clicked Piece, piece which is selected
    private Pieces playerPiece = null;

    // boolean for a peice being moved
    private boolean playerMoving = false;

    // cpuMoved
    private boolean cpuMoving = false;
    private Pieces cpuPiece = null;
    private Pieces rook = null;

    // delay boolean for slowing check and highlight for cpu 
    // private boolean delay = false;
    



    /**
     * Recieves mouse press from player
     * @param e MouseEvent from player
     */
    @Override
    public void mousePressed(MouseEvent e) {

        ArrayList<int[]> allBlackAttack = Pieces.getAllAttackPositions(cpuArray, playerArray);
        
        if ((playerPiece == null || playerPiece.getisAnimationFinsihed()) && !cpuMoving &&  ( !text.getWinner() && (!playerTimer.getWinner() && !cpuTimer.getWinner())) && !cpuFirstMove){

            // Mouse click:  x-coordinates, y-coordinates
            int mouseX = e.getX();
            int mouseY = e.getY();

            // Mouse click: x-Array, y-Array
            int mouseXArray = mouseX / CELLSIZE;
            int mouseYArray = mouseY / CELLSIZE;

            // checks if a player piece has been selected
            if (Board.checkClickedForPiece(mouseXArray, mouseYArray, playerArray)) {

                // System.out.println("clicked");
                highlight.resetColour(this); // dont think i need 
                movePiece = true;
                
                //get Pieces object of clicked piece on board 
                playerPiece = Pieces.getPiece(mouseXArray, mouseYArray, playerArray);

                // highlight green selected piece square
                highlight.highlightSelectedPieces(this, playerPiece);

                // get clicked pieces possible moves 
                possiblePositionsArray = playerPiece.getPossibleMoves(playerArray, cpuArray);

                // get clicked pieces possible attack moves
                possibleAttackPositionsArray = playerPiece.getAttackingPositions(playerArray, cpuArray);

                // highlight both attack, a regular moves
                highlight.highlightMoves(this, possiblePositionsArray, playerArray, cpuArray, possibleAttackPositionsArray, playerPiece);

            }

            // check that player piece has been selected and either a empty square is selected or a possible attacked CPU piece
            // check if mouse click is either on empty space OR clicked on black piece after white piece has been pressed
            else if ((!Board.checkClickedForPiece(mouseXArray, mouseYArray, playerArray) || Board.checkAttackingPosition(mouseXArray, mouseYArray, cpuArray)) && movePiece && playerPiece != null && playerPiece.checkValidMove(possiblePositionsArray, playerArray, cpuArray, possibleAttackPositionsArray, mouseXArray, mouseYArray)) {
                      

                
                boolean castle = false;
                // check if king is castling 
                if (playerPiece.getType().equals("king")) {
                    double distance = Math.sqrt(Math.pow(mouseXArray - (playerPiece.getX() / 48), 2) + Math.pow(mouseYArray - playerPiece.getY() / 48, 2));
                    
                    // castling occuring
                    if (distance > 1) {
                        castle = true;
                    }
                }

                if ((castle == true && playerPiece.getCastle()) && (playerPiece.getY() / 48 == mouseYArray)) { 
                    castleAnimation = true;
                    // castle left side of king
                    if (playerPiece.getX() / 48 > mouseXArray) {
                        rook = playerPiece.getLeftCastleRook(playerArray, cpuArray);
                        if (rook != null) { rook.movePiece(this, new int[]{(mouseXArray + 1), mouseYArray});}
                    }

                    // castle right side of king
                    if (playerPiece.getX() / 48 < mouseXArray) {
                        rook = playerPiece.getRightCastleRook(playerArray, cpuArray);
                        if (rook != null) { rook.movePiece(this, new int[]{(mouseXArray - 1), mouseYArray});}
                    }
                    playerPiece.movePiece(this, new int[]{mouseXArray, mouseYArray});
                }
                else { playerPiece.movePiece(this, new int[]{mouseXArray, mouseYArray}); }
                // playerPiece.movePiece(this, new int[]{mouseXArray, mouseYArray});
                playerMoving = true;

                // check if king moved
                if (playerPiece.getType().equals("king")) {playerPiece.setCastle();} 
                // check rook has moved
                if (playerPiece.getType().equals("rook")) {playerPiece.setCastle();}
                
                text.setDefendKing();
            }

            else if ((!Board.checkClickedForPiece(mouseXArray, mouseYArray, playerArray) || Board.checkAttackingPosition(mouseXArray, mouseYArray, cpuArray)) && movePiece && playerPiece != null && !playerPiece.checkValidMove(possiblePositionsArray, playerArray, cpuArray, possibleAttackPositionsArray, mouseXArray, mouseYArray) && Board.checkForKingCheck(allBlackAttack, playerArray, cpuArray)) {
                text.defendKing(this, playerArray, cpuArray);
            }
        }
    }

    

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    /**
     * checks for draws 
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return boolean true or false
     */
    public boolean checkForDraw(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        ArrayList<int[]> pArray = new ArrayList<int[]>();
        ArrayList<int[]> cArray = new ArrayList<int[]>();
        
        // get player king 
        for (Pieces p : playerArray) {     
            pArray.addAll(p.getPossibleMoves(playerArray, cpuArray)); 
        }

        // get cpu king 
        for (Pieces c : cpuArray) {
            cArray.addAll(c.getPossibleMoves(cpuArray, playerArray));
        }

        // check player king possible moves 
        if (pArray.isEmpty()) {
            cpuTimer.displayOther(this, 48);
            // playerTimer.displayOther(this, 624);
            text.drawWinner(this);
            return true;
        }

        // check cpuking possible moves 
        if (cArray.isEmpty()) {
            text.drawWinner(this);
            // cpuTimer.displayOther(this, 48);
            playerTimer.displayOther(this, 624);
            return true;
        }
        return false;
    }
    
    /**
     * Draw all elements in the game each frame, FPS at 60. 
    */
    public void draw() {

        cpuTimer.displayOther(this, 48);

        if ( !text.getWinner() && (!playerTimer.getWinner() && !cpuTimer.getWinner()))  {
            
            // player's turn, Timer is on
            if (!cpuMoving & !cpuFirstMove) {
                playerTimer.playerTimer(this);
                cpuTimer.displayOther(this, 48);
            }

            // cpu's turn, Timer on
            if (!playerMoving && cpuMoving) {
                cpuTimer.cpuTimer(this);
                playerTimer.displayOther(this, 624);
            }

            // highlight checked king
            if (playerChecked && text.getDefendKing()) {
                text.playerKingCheck(this, playerArray, cpuArray, playerPiece);
            }

            // undergoing castle animation
            if (castleAnimation) {
                Board.resetBoardColours(this);
                rook.tick(this);
                rook.drawTick(this);
            }

            // flickering castle animation
            if (!text.getDefendKing()) {
                text.defendKing(this, playerArray, cpuArray);
            }

		// player is black 
		if (cpuFirstMove) {
			// cpu moving
			cpuMoving = true;
			cpuFirstMove = false;
			cpuMovement(playerArray.get(0), playerArray, cpuArray, playerArray.get(0).getAttackingPositions(playerArray, cpuArray));
		}

            if (playerMoving && playerPiece != null) {
                // check that animation isn't 
                if (!playerPiece.getisAnimationFinsihed()) {
                    Board.resetBoardColours(this);
                    playerPiece.tick(this);
                    playerPiece.drawTick(this);
                    
                }
                if (playerPiece.getisAnimationFinsihed() && (rook == null || rook.getisAnimationFinsihed())) {
                    // player checked king has moved
                    castleAnimation = false;
                    playerChecked = false;
                    playerMoving = false;
                    playerTimer.addTimeIncrement();
                    playerTimer.setFormattedTimeString();
                    playerTimer.displayOther(this, 624);

                    Board.resetBoardColours(this);
                    cpuTimer.cpuTimer(this);

                    // check piece for promotion
                    checkPromotion(playerPiece);

                    // check if move was a take
                    // check if a possible attack move is valid and a cpu piece is selected
                    if (Board.checkAttackingPosition((playerPiece.getX() / 48), (playerPiece.getY() / 48), cpuArray)){
                        cpuArray = playerPiece.take(cpuArray);
                    }

                    // cpu moving
                    cpuMoving = true;

                    checkForDraw(playerArray, cpuArray);

                    // check if cpu king is in check
                    // deal with check 
                    if (Board.checkForKingCheck(playerPiece.getAttackingPositions(playerArray, cpuArray), cpuArray, playerArray)) {
                        // System.out.println("CPU KING CHECK");
                        // text.cpuKingCheck(this);
                        Board.checkHighlight(this, cpuArray);
        
                        cpuCheckAlgorithm(playerPiece, playerArray, cpuArray);

                        return;
                    }
                    // CPU - AI 
                    else { cpuMovement(playerPiece, playerArray, cpuArray, playerPiece.getAttackingPositions(playerArray, cpuArray));}
                }
            }
            // cpu draw functions
            if (cpuMoving && cpuPiece != null) {
                // System.out.println("cpu moving and not null");
                if (!cpuPiece.getisAnimationFinsihed()) {

                    // draw the updated positions of a player pieces
                    Board.resetBoardColours(this);
                    cpuPiece.tick(this);
                    cpuPiece.drawTick(this);
                }
                if (cpuPiece.getisAnimationFinsihed()) {
                    cpuTimer.addTimeIncrement();
                    playerTimer.playerTimer(this);
                    Board.resetBoardColours(this);
                    cpuPiece.moveHighlight(this);
                    cpuMoving = false;

                    // check piece for promotion
                    checkPromotion(cpuPiece);

                    // check if move was a take
                    // check if a possible attack move is valid and a cpu piece is selected
                    if (Board.checkAttackingPosition((cpuPiece.getX() / 48), (cpuPiece.getY() / 48), playerArray)){
                        // update if starting line - if rook on startline is taken
                        cpuPiece.updateOriginalKingArray(playerArray);
                        playerArray = cpuPiece.take(playerArray);
                    }

                    // check if white king is in check
                    Check(playerArray, cpuArray); 

                    // reset playerPiece - highlighting Piece in green, regardless of if king in check
                    playerPiece = null;
                }
            }
        }
         // draw the updated positions of a player pieces 
         for (Pieces p : playerArray) {
            p.draw(this);
        }

        // draw the updated positions of a CPU pieces 
        for (Pieces p : cpuArray) {
            p.draw(this);
        }
        
    }
	
	// Add any additional methods or attributes you want. Please put classes in different files.

    /**
     * main method which executes application
     * @param args arguments
     */
    public static void main(String[] args) {
        PApplet.main("XXLChess.App");
    }


    /**
     * calcualtes designs to deal with cpu king check 
     * and makes appropiate moves to deal with such.
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @param playerPiece clicked player piece
     */
    public void cpuCheckAlgorithm(Pieces playerPiece, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        // all of white defended pieces 
        ArrayList<int[]> allWhiteDefended = Pieces.getAllDefendedPositions(cpuArray, playerArray);

        // all possible taking moves - too get out of check
        ArrayList<int[]> taking = new ArrayList<int[]>();

        // check if it can attack, checking piece 
        ArrayList<int[]> allBlackPieceAttack = Pieces.getAllAttackPositions(cpuArray, playerArray);

        int checkX = playerPiece.getX() / 48;
        int checkY = playerPiece.getY() / 48;

        ArrayList<Pieces> blackOutOfCheckAttack = new ArrayList<Pieces>();
        
        // check if piece can be taken 
        for (int[] attack : allBlackPieceAttack) {
            if (Board.checkAttackingPosition(attack[0], attack[1], playerArray)) {
    
                // check black pieces for an attack to get out of check
                for (Pieces b : cpuArray) {
                    taking = b.getAttackingPositions(cpuArray, playerArray);
    
                    for (int[] a : taking) {
    
                        if (a[0] == checkX && a[1] == checkY) {
                            blackOutOfCheckAttack.add(b);
                        }
                    }
                }
            }
        }

        ArrayList<int[]> possibleattacks = new ArrayList<int[]>();

        // loop through all possible out of check attacks
        for (Pieces Piece : blackOutOfCheckAttack) {
            // Pieces possible attacks
            possibleattacks = Piece.getAttackingPositions(cpuArray, playerArray);

            // defended positions are removed from possible black moves, that put king in check
            ArrayList<int[]> attackList = new ArrayList<>();
            attackList.add(new int[] {checkX, checkY});
            ArrayList<int[]> removedAttack = Board.removeCommonPositions(attackList, allWhiteDefended);
            
            if (blackOutOfCheckAttack != null && !removedAttack.isEmpty()) {
                cpuPiece = Piece;
                cpuPiece.movePiece(this, new int[]{(playerPiece.getX() / 48), (playerPiece.getY() / 48)});
                return;
            }
        }
        // block check
        MoveResult moveResult = Board.blockCheck(this, playerArray, cpuArray); 
        if (Board.blockCheck(this, playerArray, cpuArray) == null) {
            
            Board.checkHighlight(this, cpuArray);
            // System.out.println("CHECKMATE!!!");
            // System.out.println("PLAYER WINS");
            text.playerCheckmateWinner(this);
            playerTimer.displayOther(this, 624);
            Board.highlightContributionPieces(this, playerArray, cpuArray);
            return;
        }
        else {
            cpuPiece = moveResult.getBlackPiece();
            int[] target = moveResult.getCaptureCoordinate();
            cpuPiece.movePiece(this, target);
            return;
        }
        
    }


    /**
     * calcualtes piece movement for a king which isn't in check 
     * @param playerPiece clicked player piece
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @param possibleAttackPositionsArray a list storing all possible attack positions 
     */
    public void cpuMovement(Pieces playerPiece, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray, ArrayList<int[]> possibleAttackPositionsArray) {
        // all of white defended pieces 

        ArrayList<int[]> allWhiteDefended = Pieces.getAllDefendedPositions(cpuArray, playerArray);

        boolean blackPieceBeingAttacked = false;
        int[] attackedPiecePosition;
        // check if black pieces is being attacked 
        for (int[] pos : possibleAttackPositionsArray) {

            if (Board.checkBlackPieceBlock(pos[0], pos[1], cpuArray)) {
                blackPieceBeingAttacked = true;
                attackedPiecePosition = pos;
            }
        }

        // if cpu piece can take player piece wich isn't defended 

        // get cpu piece which can take the just moved player piece 
        cpuPiece = Pieces.getPieceWhichCanAttack(playerPiece, cpuArray, playerArray);
        // check if it defended
        if (!Pieces.getIsDefended(playerPiece, allWhiteDefended)) {
            
            if (playerPiece != null && cpuPiece != null) {
                // take player piece that just moved - as it was left undefended
                cpuPiece.movePiece(this, new int[]{(playerPiece.getX() / 48), (playerPiece.getY() / 48)});
                return;
            }

        }

        boolean movementRequired = false;

        // cpu piece is being attack, movement is required
        if (blackPieceBeingAttacked) {

            cpuPiece = Board.checkForHighestValueAttackedPiece(possibleAttackPositionsArray, cpuArray);

            // if value of both the attack and defender is similar don't need to move
            if (Pieces.compareAttackAndDefenderValues(playerPiece, cpuPiece)) {
                movementRequired = true;
            }

            // attacked pieces possible moves
            ArrayList<int[]> attackedPiecePossibleMoves = cpuPiece.getPossibleMoves(playerArray, cpuArray);

            // get all the defend postions for black 
            ArrayList<int[]> allDefendPositions = Pieces.getAllDefendedPositions(playerArray, cpuArray);

            // get posible position out of the all defended position
            ArrayList<int[]> attackedPieceDefendedMoves = Pieces.getDefendedPossibleMoves(allDefendPositions, attackedPiecePossibleMoves); 

            // get the most defended possible position
            int[] mostFrequentAttackPositions = getMoveFrequentPosition(attackedPieceDefendedMoves);

            if (mostFrequentAttackPositions != null && movementRequired) {
                
                cpuPiece.movePiece(this, new int[]{mostFrequentAttackPositions[0], mostFrequentAttackPositions[1]});
                return;
            }

            // move to the most defended square   
        }
        if (!blackPieceBeingAttacked || !movementRequired) {
            
            int x = 0;
            int y = 0;

            // check if attacking piece can be taken - black take white
            ArrayList<int[]> blackAttack = Pieces.getAllAttackPositions(cpuArray, playerArray);

            for (int[] attack : blackAttack) {

                // checks if a black piece can take a white piece
                if (Board.checkAttackingPosition(attack[0], attack[1], playerArray)) {
                    x = attack[0];
                    y = attack[1];
                    break;
                }
            }

            // gets piece that can be taken
            playerPiece = Pieces.getPiece(x, y, playerArray);

            // get all white defended positions
            ArrayList<int[]> allWhiteDefendedPositions = Pieces.getAllDefendedPositions(cpuArray, playerArray);
            if (playerPiece != null) {
                // get black piece that can take white piece
                cpuPiece = Pieces.getPieceWhichCanAttack(playerPiece, cpuArray, playerArray);

                // check if attacking piece is defended
                // and blackPiece is not null - meaning that their is no black piece which can attack white piece.
                if (!Pieces.getIsDefended(playerPiece, allWhiteDefendedPositions) && cpuPiece != null) {
                    
                    // black takes undefended piece
                    cpuPiece.movePiece(this, new int[]{(playerPiece.getX() / 48), (playerPiece.getY() / 48)});
                    return;
                }
            }

            // develop high value pieces which can be developed
            String[] developingPieces = {"knight", "camel", "bishop", "guard", "archbishop", "amazon"};


            for (String type : developingPieces) {

                // returns piece for which needs to be developed
                cpuPiece = Board.checkforDevelopment(type, playerArray, cpuArray, board);
                // System.out.println("developing move ");

                if (cpuPiece != null) {

                    ArrayList<int[]> freePositions = Board.moveToFreePosition(cpuPiece, playerArray, cpuArray);

                    // get all the defend postions for black 
                    ArrayList<int[]> allDefendPositions = Pieces.getAllDefendedPositions(playerArray, cpuArray);

                    // get posible position out of the all defended position
                    ArrayList<int[]> PieceDefendedMoves = Pieces.getDefendedPossibleMoves(allDefendPositions, freePositions); 

                    // get the most defended possible position
                    int[] mostFrequentPositions = getMoveFrequentPosition(PieceDefendedMoves);

                    cpuPiece.movePiece(this, new int[]{mostFrequentPositions[0], mostFrequentPositions[1]});
                    // System.out.println("developing move ");
                    return;
                }
            }

            // choose between random move or attack future move
            Random random = new Random();
            int randomNumber = random.nextInt(2); // 0 or 1
            if (randomNumber == 0) {

                // check if it can attack higher value piece - via a undefended position

                // get unsafe positions 
                ArrayList<int[]> allUnsafePositions = Pieces.getAllAttackPositions(playerArray, cpuArray);

                // get black piece which can attack a undefened white piece via a safe position

                // ArrayList<Pieces, int[]> future = getFutureMove(playerArray, cpuArray, allUnsafePositions);
                MoveResult future = Pieces.getFutureMove(playerArray, cpuArray, allUnsafePositions);

                cpuPiece = future.getBlackPiece();
                int[] futureCoor = future.getCaptureCoordinate();

                if (cpuPiece != null && futureCoor != null) {
                    cpuPiece.movePiece(this, new int[]{futureCoor[0], futureCoor[1]});
                    return;
                }
            }

        }
        
        // Consider draw scenario. it will never break out of while loop

        // no pieces are under attack + all key pieces have been developed
        if (!checkForDraw(playerArray, cpuArray)) {
            Random random = new Random();
            while (true) {
                if (cpuArray.isEmpty()) {
                    continue;
                }
                int maxValue = cpuArray.size() - 1;
                int randomNumber = random.nextInt((maxValue - 0) + 1);

                cpuPiece = cpuArray.get(randomNumber);
                if (!cpuPiece.getPossibleMoves(cpuArray, playerArray).isEmpty()){
                    break;
                }
            }

            // move random piece to random position 
            while (true) {
                List<int[]> possibleMoves = cpuPiece.getPossibleMoves(cpuArray, playerArray);
                if (possibleMoves.isEmpty()) {
                    continue;
                }
                int maxValue = possibleMoves.size() - 1;
                if (maxValue < 0) {
                    continue;
                }

                int randomNumber = random.nextInt(maxValue + 1);
                int[] randomPosition = possibleMoves.get(randomNumber);
                if (!Board.checkPieceBlock(randomPosition[0], randomPosition[1], playerArray, cpuArray)){
                    cpuPiece.movePiece(this, new int[]{randomPosition[0], randomPosition[1]});
                    return;
                }
            }
        }
        else {cpuMoving = false; return;}

    }


    /**
     * Returns most frequent square out of possible positions
     * @param positions all possible piece positions
     * @return int[]
     */
    public int[] getMoveFrequentPosition(ArrayList<int[]> positions) {

        Map<String, Integer> freqMap = new HashMap<String, Integer>();

        // Count the frequency of each element
        for (int[] pos : positions) {
            String key = Arrays.toString(pos);
            freqMap.put(key, freqMap.getOrDefault(key, 0) + 1);
        }

        // Find the most frequent element
        int maxFreq = 0;
        int[] mostFrequent = null;
        for (int[] pos : positions) {
            String key = Arrays.toString(pos);
            int freq = freqMap.get(key);
            if (freq > maxFreq) {
                maxFreq = freq;
                mostFrequent = pos;
            }
        }

        return mostFrequent;
    }


    /**
     * Updates chess piece sprite if pawn promotes to a queen
     * @param piece piece object
     */
    public void checkPromotion(Pieces piece) {

        PImage bQueen = loadImage("src/main/resources/XXLChess/b-queen.png");
        PImage wQueen = loadImage("src/main/resources/XXLChess/w-queen.png");

        // check if pawn can be promoted to a queen 
        if (piece.getType().equals("pawn") && piece.getColour() == 'w' && piece.getY() / 48 == 6) {
            piece.setType("queen");
            piece.setSprite(wQueen);
        }
        else if (piece.getType().equals("pawn") && piece.getColour() == 'b' && piece.getY() / 48 == 7) {
            piece.setType("queen");
            piece.setSprite(bQueen);
        }
    }


    /**
     * Checks if player king is in check - if true then highlight square 
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     */
    public void Check(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        // get all cpu possible attacks 
        ArrayList<int[]> allBlackAttack = Pieces.getAllAttackPositions(cpuArray, playerArray);

        // highlights checked player king
        if(Board.checkForKingCheck(allBlackAttack, playerArray, cpuArray)) {
            Board.checkHighlight(this, playerArray);
            // System.out.println("White King is In Check");
            
            // test for checkmate 
            if (!checkForCheckmate(cpuPiece, playerArray, cpuArray)) {
                playerChecked = true;
                text.playerKingCheck(this, playerArray, cpuArray, playerPiece);
            }
            
        }
        return;
    }

    /**
     * checks if player is in checkmate
     * @param cpuPiece piece object that cpu has selected
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return boolean, true or false
     */
    public boolean checkForCheckmate(Pieces cpuPiece, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        // all of white defended pieces 
        ArrayList<int[]> allWhiteDefended = Pieces.getAllDefendedPositions(playerArray, cpuArray);

        // all possible taking moves - too get out of check
        ArrayList<int[]> taking = new ArrayList<int[]>();

        // check if it can attack, checking piece 
        ArrayList<int[]> allBlackPieceAttack = Pieces.getAllAttackPositions(playerArray, cpuArray);

        int checkX = cpuPiece.getX() / 48;
        int checkY = cpuPiece.getY() / 48;

        ArrayList<Pieces> blackOutOfCheckAttack = new ArrayList<Pieces>();
        
        // check if piece can be taken 
        for (int[] attack : allBlackPieceAttack) {
            if (Board.checkAttackingPosition(attack[0], attack[1], cpuArray)) {
    
                // check black pieces for an attack to get out of check
                for (Pieces b : playerArray) {
                    taking = b.getAttackingPositions(playerArray, cpuArray);
    
                    for (int[] a : taking) {
    
                        if (a[0] == checkX && a[1] == checkY) {
                            blackOutOfCheckAttack.add(b);
                        }
                    }
                }
            }
        }

        ArrayList<int[]> possibleattacks = new ArrayList<int[]>();
        // loop through all possible out of check attacks
        for (Pieces Piece : blackOutOfCheckAttack) {
            // Pieces possible attacks
            possibleattacks = Piece.getAttackingPositions(playerArray, cpuArray);

            // defended positions are removed from possible black moves, that put king in check
            ArrayList<int[]> attackList = new ArrayList<>();
            attackList.add(new int[] {checkX, checkY});
            ArrayList<int[]> removedAttack = Board.removeCommonPositions(attackList, allWhiteDefended);
    
            if (blackOutOfCheckAttack != null && !removedAttack.isEmpty()) {
                return false;
            }
        }
    
        // block check
        if (!Board.whiteBlockCheck(this, cpuArray, playerArray)) {

            Board.checkHighlight(this, playerArray);
            // System.out.println("CHECKMATE!!!");
            // System.out.println("CPU WINS");
            text.cpuCheckmateWinner(this);
            cpuTimer.displayOther(this, 48);
            Board.highlightContributionPieces(this, cpuArray, playerArray);

            return true;
        }
        return false;
    }
}