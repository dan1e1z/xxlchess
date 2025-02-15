package XXLChess;

import processing.core.PImage;
import processing.core.PApplet;
import processing.core.*;

// Directly Import Math.min to prevent need of using Math.min()
import static java.lang.Math.min;


import java.util.*;
import java.util.ArrayList;


/**
 * This is an abstract class that represents a chess piece.
 * It provides common functionality for all chess pieces, 
 * such as getting and setting the piece's position, 
 * and checking if a move is valid.
 * Concrete subclasses of this class must implement the move() method, which specifies how the piece moves. 
 */
public abstract class Pieces {
    
    /**
     * The chess piece's x-coordinate.
     */
    protected int x;

    /**
     * The chess piece's x-array coordinate
     */
    protected int arrayX;

    /**
     * The chess piece's double x-coordinate for animation
     */
    protected double posX;

    /**
     * The chess piece's y-coordinate.
     */
    protected int y;

    /**
     * The chess piece's y-array coordinate
     */
    protected int arrayY;

    /**
     * The chess piece's double x-coordinate for animation
     */
    protected double posY;


    /**
     * The chess piece's type.
     */
    protected String type;
    
    /**
     * The chess piece's colour.
     */
    protected char colour;

    /**
     * The chess piece's value.
     */
    protected double value;

    /**
     * The chess piece's sprite.
     */
    private PImage sprite;

    /**
     * Player colour
     */
    private String playerColour;

    /**
     * speed config
     */
    private static double speed;

    /**
     * max time config
     */
    private static double maxTime;

    /**
     * start coordinates
     */
    private int startX, startY;

    /**
     * target coordinates
     */
    private int targetX, targetY;

    /**
     * animation detection
     */
    protected boolean animationFinished = true;

    /**
     * check if castling can occur 
     */
    private boolean canCastle = true;

    /**
     * original king x-axis pieces
     */
    private static ArrayList<Pieces> originalKingLine = new ArrayList<Pieces>();

    /**
     * distance, timeTaken and calculated decrements for animation
     */
    private double distance, timeTaken;
    private double vX, vY;

    /**
     * Creates a new chess piece's object.
     * 
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param type the piece type.
     * @param colour the colour of the piece
     * @param value the value of the piece
     * @param sprite the sprite of the piece
     * @param playerColour the colour of the player
     */
    public Pieces(int x, int y, String type, char colour, double value, PImage sprite, String playerColour) {
        this.x = x;
        this.y = y;
        this.arrayX = this.x / 48;
        this.arrayY = this.y / 48;
        this.type = type;
        this.colour = colour;
        this.value = value;
        this.sprite = sprite;

        // config speed and maxTime from config 
        this.speed = speed;
        this.maxTime = maxTime;

        // start and target coordinates
        this.startX = this.arrayX;
        this.startY = this.arrayY;

        // target array coordinates for animation
        this.targetX = this.startX;
        this.targetY = this.startY;

        // double accurate coordinates for animation
        this.posX = this.x;
        this.posY = this.y;

        // check if animation is done
        this.animationFinished = true;

        // colour of player 
        this.playerColour = playerColour;

        // checks if piece can castle 
        // check if piece is either king or rook
        if (type.equals("king") || type.equals("rook")) {this.canCastle = true;}
        else {this.canCastle = false;}
        

        // original pieces on king x-axis 
        this.originalKingLine = originalKingLine;
    }

    /**
     * uses to pass set speed and maxTime from json config file 
     * @param s speed in which pieces move in pixels/second
     * @param max maximum time it should take for a piece to move
     */
    public static void setSpeedAndMaxTime(double s, double max) {
        speed = s;
        maxTime = max;
    }

    /**
     * Sets startX and startY coordinates for begining og animation
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void setStart(int x, int y) {
        this.startX = x;
        this.startY = y;
    }

    /**
     * Set Array of original pieces on king x-axis
     * @param playerArray contains all piece objects of player
     */
    public static void setKingXAxis(ArrayList<Pieces> playerArray) {

        ArrayList<Pieces> tempArray = new ArrayList<Pieces>();

        // look for king 
        Pieces king = null;

        // looks for king piece
        for (Pieces p : playerArray) {
            if (p.getType().equals("king")) {
                king = p;
            }
        }

        // king coordinates 
        int kingX = king.getX() / 48;
        int kingY = king.getY() / 48;

        // loop through array adding, pieces which satisfy the conditions
        // must be min 3 space differance
        // not out of bounds 

        for (Pieces p : playerArray) {

            if (p.getX() / 48 == kingX && p.getY() / 48 == kingY) {tempArray.add(p);}
            if ((p.getY() / 48) == kingY && ((p.getX() / 48) > (kingX + 3) || (p.getX() / 48) < (kingX - 3)) ) { tempArray.add(p);}
        }
        originalKingLine = tempArray;
    }

    /**
     * update originalKingArray, such that it doesn't include pieces which have been taken (removed)
     * @param playerArray contains all piece objects of player
     */
    public void updateOriginalKingArray(ArrayList<Pieces> playerArray) {

        Pieces piece = null;

        for (Pieces p : playerArray) {

            if ((p.getX() / 48 == this.getX() / 48) && (p.getY() / 48 == this.getY() / 48) ){
                piece = p;
                break;
            }
        }
    
        int count = 0;

        for (Pieces p : this.originalKingLine) {

            if (p == piece){
                this.originalKingLine.remove(count);
                break;
            }
            count++;
        }


    }

    /**
     * set castle to false - piece can not castle
     */
    public void setCastle() {
        this.canCastle = false;
    }

    /**
     * Return this.castle - determines if a piece is legible to castle
     * @return boolean, true or false
     */
    public boolean getCastle() {
        return this.canCastle;
    }

    /**
     * Sets the chess piece's sprite.
     * 
     * @param sprite The new sprite to use.
     */
 
    /**
     * Draws the chess piece's to the screen.
     * 
     * @param app The window to draw onto.
     */
    public void draw(PApplet app) {
        // The image() method is used to draw PImages onto the screen.
        // The first argument is the image, the second and third arguments are coordinates, then height and width.
        app.image(this.sprite, this.x, this.y, 48, 48);
    }


    /**
     * tick draw with double accuracy and precision 
     * @param app draws the piece animation
     */
    public void drawTick(PApplet app) {
        // The image() method is used to draw PImages onto the screen.
        // The first argument is the image, the second and third arguments are coordinates, then height and width.
        app.image(this.sprite, (float) this.posX, (float) this.posY, 48, 48);
    }

    /**
     * gettter method to check if animation has been finished
     * @return boolean, true or false
     */
    public boolean getisAnimationFinsihed() {
        return this.animationFinished;
    }

    /**
     * setter method to convert int x and y coordinates to doubles for movement due to accuracy
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void setposCoordinates(int x, int y) {
        this.posX = (double) x;
        this.posY = (double) y;
    } 

    /**
     * Returns boolean, checking if king can castle
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return true if king can castle, otherwise returns false
     */
    public boolean checkCanCastle(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        // look for king 
        Pieces king = null;

        // looks for king piece
        for (Pieces p : playerArray) {
            if (p.getType().equals("king")) {
                king = p;
            }
        }
        // check if king can castle 
        if (king.canCastle == false) { return false; }

        // check if space 2+ and 2- is free
        boolean check = true;

        int y = king.getY() / 48;

        //right
        int rightX1 = (king.getX() / 48) + 1;
        int rightX2 = (king.getX() / 48) + 2;

        // left
        int leftX1 = (king.getX() / 48) - 1;
        int leftX2 = (king.getX() / 48) - 2;

        // castle left
        boolean canCastleLeft = true;

        // castle right
        boolean canCastleRight = true;

        // check castle options
        if (Board.checkPieceBlock(leftX1, y, playerArray,  cpuArray) || Board.checkPieceBlock(leftX2, y, playerArray,  cpuArray)) {
            canCastleLeft = false;
        }

        if (Board.checkPieceBlock(rightX1, y, playerArray,  cpuArray) || Board.checkPieceBlock(rightX2, y, playerArray,  cpuArray)) {
            canCastleRight = false;
        }

        int count  = 0;

        for (Pieces p : this.originalKingLine) {
            if (p.getType().equals("king")) { break; }
            else {count++;}
        }

        // check if originalKingLine is less than 2 
        if (this.originalKingLine.size() <= 1) { return false; }

        // left and right of king
        ArrayList<Pieces> leftHalf = new ArrayList<Pieces>(this.originalKingLine.subList(0, count));
        ArrayList<Pieces> rightHalf = new ArrayList<Pieces>(this.originalKingLine.subList(count + 1, this.originalKingLine.size() - 1));
        
        boolean left = false;
        boolean right = false;

        // check agianst leftHalf
        for (Pieces p : leftHalf) {
            if (p.canCastle == true) {left = true; break;}
        }

        // check agianst rightHalf
        for (Pieces p : rightHalf) {
            if (p.canCastle == true) {right = true; break;}
        }
        
        if ((canCastleLeft && left) || (canCastleRight && right) ) {
            return true;
        }
        return false;
    }


    /**
     * checks rook on left side of the king
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return p rook object on left side of the king
     */
    public Pieces getLeftCastleRook(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        Pieces king = Pieces.getKing(playerArray, cpuArray);

        int count  = 0;

        for (Pieces p : this.originalKingLine) {
            if (p.getType().equals("king")) { break; }
            else {count++;}
        }

        // left and right of king
        ArrayList<Pieces> leftHalf = new ArrayList<Pieces>(this.originalKingLine.subList(0, count));

        // look for rook that can castle 
        for (Pieces p : leftHalf) {
            if (p.canCastle && p.getType().equals("rook")) {
                return p;
            }
        }
        return null;
    }

    /**
     * checks rook on right side of the king
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return p rook object on right side of the king
     */
    public Pieces getRightCastleRook(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        Pieces king = Pieces.getKing(playerArray, cpuArray);

        int count  = 0;

        for (Pieces p : this.originalKingLine) {
            if (p.getType().equals("king")) { break; }
            else {count++;}
        }

        // left and right of king
        ArrayList<Pieces> rightHalf = new ArrayList<Pieces>(this.originalKingLine.subList(count + 1, this.originalKingLine.size()));

        // look for rook that can castle 
        for (Pieces p : rightHalf) {
            if (p.canCastle && p.getType().equals("rook")) {
                return p;
            }
        }
        return null;
    }



    /**
     * Returns boolean, checking if king can castle on the right side of the king
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return true if king can castle on the right side, otherwise returns false
     */
    public boolean checkRightCanCastle(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        // get king piece 
        Pieces king = Pieces.getKing(playerArray, cpuArray);

        // check if king can castle 
        if (king.canCastle == false) { return false; }

        // check if space 2+ and 2- is free
        boolean check = true;

        int y = king.getY() / 48;

        //right
        int rightX1 = (king.getX() / 48) + 1;
        int rightX2 = (king.getX() / 48) + 2;

        // castle right
        boolean canCastleRight = true;


        if (Board.checkPieceBlock(rightX1, y, playerArray,  cpuArray) || Board.checkPieceBlock(rightX2, y, playerArray,  cpuArray)) {
            canCastleRight = false;
        }

        int count = 0;

        for (Pieces p : this.originalKingLine) {
            if (p.getType().equals("king")) { break; }
            else {count++;}
        }

        // check if originalKingLine is less than 2 
        if (this.originalKingLine.size() <= 1) { return false; }

        // left and right of king
        ArrayList<Pieces> rightHalf = new ArrayList<Pieces>(this.originalKingLine.subList(count + 1, this.originalKingLine.size()));

        boolean right = false;

        // check agianst rightHalf
        for (Pieces p : rightHalf) {
            if (p.canCastle == true) {right = true; break;}
        }
        
        if ( (canCastleRight && right) ) {
            // System.out.println("right can castle on right");
            return true;
        }

        return false;
    }

    /**
     * return king object
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return king returns king Pieces object
     */
    public static Pieces getKing(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        // look for king 
        Pieces king = null;

        // looks for king piece
        for (Pieces p : playerArray) {
            if (p.getType().equals("king")) {
                king = p;
            }
        }
        return king;

    }

    /**
     * Returns boolean, checking if king can castle on the left side of the king
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return true if king can castle on the left side, otherwise returns false
     */
    public boolean checkLeftCanCastle(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        // get king piece 
        Pieces king = Pieces.getKing(playerArray, cpuArray);

        // check if king can castle 
        if (king.canCastle == false) { return false; }

        // check if space 2+ and 2- is free
        boolean check = true;

        int y = king.getY() / 48;

        // left
        int leftX1 = (king.getX() / 48) - 1;
        int leftX2 = (king.getX() / 48) - 2;

        // castle left
        boolean canCastleLeft = true;

        // check castle options
        if (Board.checkPieceBlock(leftX1, y, playerArray,  cpuArray) || Board.checkPieceBlock(leftX2, y, playerArray,  cpuArray)) {
            canCastleLeft = false;
        }

        int count  = 0;

        for (Pieces p : this.originalKingLine) {
            if (p.getType().equals("king")) { break; }
            else {count++;}
        }

        // check if originalKingLine is less than 2 
        if (this.originalKingLine.size() <= 1) { return false; }

        // left and right of king
        ArrayList<Pieces> leftHalf = new ArrayList<Pieces>(this.originalKingLine.subList(0, count));
        
        boolean left = false;

        // check agianst leftHalf
        for (Pieces p : leftHalf) {
            if (p.canCastle == true) {left = true; break;}
        }
        
        if ((canCastleLeft && left) ) {
            return true;
        }
        return false;
    }
    
    /**
     * Moves the piece to the specified position.
     * @param app to draw on instance
     * @param position An array containing the target x and y positions.
     */

     public void movePiece(PApplet app, int[] position) {

        // System.out.println(this.type + " " + this.colour);
        this.targetX = position[0] * 48; // multiply by 48 to convert from cell index to pixel coordinate
        this.targetY = position[1] * 48; // multiply by 48 to convert from cell index to pixel coordinate

        // System.out.println("targetX: " + position[0]);
        // System.out.println("targetY: " + position[1]);

        // set persision x, y coordinates
        setposCoordinates(this.x, this.y);

        // set start x and y for tick 
        setStart(this.x, this.y);

        // calculate distance between current position and target position
        distance = Math.sqrt(Math.pow(this.targetX - this.x, 2) + Math.pow(this.targetY - this.y, 2));

        // check that speed doesn't exceed maxTime
        timeTaken = distance / this.speed;

        if (timeTaken > maxTime) {
            // calculate new speed
            this.speed = distance / maxTime;
        }

        // calculate velocities
        vX = (this.speed / distance) * ((this.targetX - this.x)) / 60;
        vY = (this.speed / distance) * ((this.targetY - this.y)) / 60;
        // System.out.println("x: " + vX + " y: " + vY);

        this.animationFinished = false;

        // temporarily move piece out of sight 
        this.x = -48;
        this.y = -48;

         // clear square selected piece is moving from - player piece
         if (((this.x / 48) + (this.y / 48)) % 2 == 0) {
            app.fill(237,216,179,255);  // white
        } else {
            app.fill(175,135,97,255);  // brown
        }
        // fills in corresponding color into created rectangle 
        app.rect(this.x , this.y , 48, 48);

    }


    /**
     * highlights target and starting postion of piece which is moving in green 
     * @param app to draw on instance
     */
    public void moveHighlight(PApplet app) {

        // start
        // highlights square with light green - indiccating original positions and the target position
        if (((this.startX / 48) + (this.startY / 48)) % 2 == 0) {
            app.fill(207,209,111,255);  // light green - move
        } else {
            app.fill(172,161,61,255);  // dark green - move
        }
        app.rect(this.startX, this.startY, 48, 48);

        //target 
        // highlights square with light green - indiccating original positions and the target position
        if (((this.targetX / 48) + (this.targetY / 48)) % 2 == 0) {
            app.fill(207,209,111,255);  // light green - move
        } else {
            app.fill(172,161,61,255);  // dark green - move
        }
        app.rect(this.targetX, this.targetY, 48, 48);

    }

    // @Override
    /**
     * calculates vX and vY values which allow for animation to run smoothly
     * @param app to draw on instance
     */
    public void tick(PApplet app) {

        this.moveHighlight(app);

        this.posX += vX;
        this.posY += vY;

        if (Math.abs(this.posX - (this.targetX)) < 24 && Math.abs(this.posY - (this.targetY)) < 24) {
            this.x = this.targetX;  // snap to target position
            this.y = this.targetY;
            this.animationFinished = true;
        }
    }

    /**
     * takes piece
     * @param Array either playerArray or cpuArray
     * @return Array containing pieces
     */
    public ArrayList<Pieces> take(ArrayList<Pieces> Array) {

        int count = 0;

        for (Pieces p : Array) {

            if ((p.getX() / 48 == this.getX() / 48) && (p.getY() / 48 == this.getY() / 48) ){
                Array.remove(count);
                break;
            }
            count++;
        }
        return Array;

    }

    /**
     * returns piece which can take white piece 
     * @param attack piece which is getting attacked
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return b Pieces object, piece which can attack

     */
    public static Pieces getPieceWhichCanAttack(Pieces attack, ArrayList<Pieces> cpuArray, ArrayList<Pieces> playerArray) {

        int x = attack.getX() / 48;
        int y = attack.getY() / 48;

        for (Pieces b : cpuArray) {

            // swaps cpuArray and playerArray as we are testing for black piece attacks
            ArrayList<int[]> attacks = b.getAttackingPositions(cpuArray, playerArray);         

            // check if white piece coordinates are inside black pieces attack array
            for (int[] a : attacks) {

                if (a[0] == x && a[1] == y) {
                    return b;
                }
            }
        }
        return null;
    }

    /**
     * compare two pieces and check if attacking piece is of highter value
     * @param movingPiece piece which has moved to attack 
     * @param attackPiece piece which is being attacked
     * @return boolean, true if attacker is of highter value, otherwise false
     */
    public static boolean compareAttackAndDefenderValues(Pieces movingPiece, Pieces attackPiece) {

        if (movingPiece.getValue() <= attackPiece.getValue()) {
            return true;
        }
        return false;
    }

    /**
     * returns true is piece is being defended, otherwise returns false
     * @param attacked attacked piece
     * @param allWhiteDefendedPositions list of all defended positions
     * @return boolean, true if piece is being defended, otherwise false
     */
    public static boolean getIsDefended(Pieces attacked, ArrayList<int[]> allWhiteDefendedPositions) {

        int x = attacked.getX() / 48;
        int y = attacked.getY() / 48;

        for (int[] defended : allWhiteDefendedPositions) {
            if (defended[0] == x && defended[1] == y) {
                return true;
            }
        }

        return false;
    }

    /**
     * Removes all positions which are being attacked - returns a list of all the positions which a piece can move to which are defended
     * @param allDefendPositions list containing all defened positions
     * @param attackedPiecePossibleMoves list containing all possible piece moves
     * @return list of defended possible moves
     */
    public static ArrayList<int[]> getDefendedPossibleMoves(ArrayList<int[]> allDefendPositions, ArrayList<int[]> attackedPiecePossibleMoves) {

        ArrayList<int[]> attackedPieceDefendedMoves = new ArrayList<int[]>();

        for (int[] a : attackedPiecePossibleMoves) {
            
            // compare against all defend positions
            for (int[] d : allDefendPositions) {

                if (a[0] == d[0] && a[1] == d[1]) {
                    
                    attackedPieceDefendedMoves.add(a);
                }
            }
        }
        return attackedPieceDefendedMoves;
    }

    /**
     * Returns the defended positions on the board, black defending
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return all defended positions
     */
     public static ArrayList<int[]> getAllDefendedPositions(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        ArrayList<int[]> allDefendPositions = new ArrayList<int[]>();

        for (Pieces b : cpuArray) {
            allDefendPositions.addAll(b.getAttackingPositions(playerArray, cpuArray));
        } 
        
        return allDefendPositions;
    }
    
    

    /**
     * Gets the x-coordinate.
     * @return The x-coordinate.
     */
    public int getX() {
        return this.x;
    }

    /**
     * sets the x-coordinate
     * @param x The x-coordinate
     */
    public void setX(int x) {
        this.x = x;
        this.arrayX = this.x / 48;
    }

    /**
     * gets the x array coordinate
     * @return x array coordinate
     */
    public int getArrayX() {
        return this.arrayX;
    }

    /**
     * gets the y array coordinate
     * @return y array coordinate
     */
    public int getArrayY() {
        return this.arrayY;
    }

    /**
     * Returns the y-coordinate.
     * @return The y-coordinate.
     */
    public int getY() {
        return this.y;
    }

    /**
     * sets the y-coordinates 
     * @param y- coordinate
     */
    public void setY(int y) {
        this.y = y;
        this.arrayY = this.y / 48;
    }

    /**
     * gets the piece type 
     * @return the piece type
     */
    public String getType() {
        return this.type;
    }

    /**
     * sets the piece type
     * @param type piece type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
    * sets the piece image sprite 
    * @param sprite piece image sprite
    */
    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    /**
     * getter for piece image sprite
     * @return this.sprite image of chess piece 
     */
    public PImage getSprite() {
        return this.sprite;
    }

    /**
     * gets the piece value 
     * @return the piece value
     */
    public double getValue() {
        return this.value;
    }

    /**
     * gets the piece colour
     * @return this.colour char value of the piece colour
     */
    public char getColour() {
        return this.colour;
    }

    /**
     * gets clicked peice
     * @param x x-coordinate
     * @param y y-coordinate
     * @param Array playerArray or cpuArray
     * @return Pieces
     */
    public static Pieces getPiece(int x, int y, ArrayList<Pieces> Array) {
        for ( Pieces p : Array ) {
            if ( (p.getX() / 48) == x && (p.getY() / 48) == y ) {
                return p;
            }
        }
        return null;
    }


    /**
     * returns a list containing all the coordinates of which the piece can move too 
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return list containing all the coordinates of which the piece can move
     */
    public ArrayList<int[]> getPossibleMoves(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        String type = this.type;
        int x = this.x / 48;
        int y = this.y / 48;
        char colour = this.colour;

        switch (type) {
            case "pawn":

                return this.getPossiblePawnMoves(x, y, playerArray, cpuArray, colour);

            case "rook":
                return this.getPossibleRookMoves(x, y, playerArray, cpuArray);

            case "knight":
                return this.getPossibleKnightMoves(x, y, playerArray, cpuArray);

            case "bishop":
                return this.getPossibleBishopMoves(x, y, playerArray, cpuArray);

            case "archbishop":
                return this.getPossibleArchbishopMoves(x, y, playerArray, cpuArray);

            case "camel":
                return this.getPossibleCamelMoves(x, y, playerArray, cpuArray);

            case "general":
                return this.getPossibleGeneralMoves(x, y, playerArray, cpuArray);

            case "amazon":
                return this.getPossibleAmazonMoves(x, y, playerArray, cpuArray);

            case "king":
                return this.getPossibleKingMoves(x, y, playerArray, cpuArray);

            case "chancellor":
                return this.getPossibleChancellorMoves(x, y, playerArray, cpuArray);

            case "queen":
                return this.getPossibleQueenMoves(x, y, playerArray, cpuArray);


            default : 
                return null;
        }

    }

    /**
     * getter for player colour
     * @return String the player colour
     */
    public String getPlayerColour() {
        return this.playerColour;
    }

    
    private boolean foundMatch = false;


    /**
     * get possible pawn moves
     * @param x x-coordinate
     * @param y y-coordinate
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @param colour char colour of piece
     * @return list of pawn possible moves
     */
    public static ArrayList<int[]> getPossiblePawnMoves(int x, int y, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray, char colour) {
        
        int startingLine = 0;
        ArrayList<int[]> positions = new ArrayList<int[]>();
        boolean foundMatch = true;

        String stringColour;
        if (colour == 'b') {
            stringColour = "black";
        }
        else {stringColour = "white";}

        if (stringColour.equals(playerArray.get(0).getPlayerColour())) {
    
            // Pawn is at the starting line can movve either 1 or 2 spaces forawrd
            if (y == 12) {

                // check if piece is blocking path 
                // white pieces 
                if (Board.checkPieceBlock(x, y - 1, playerArray, cpuArray)) {
                    return positions;
                }

                if (Board.checkPieceBlock(x, y - 2, playerArray, cpuArray)) {
                    positions.add(new int[]{x, y - 1});
                    return positions;
                }

                positions.add(new int[]{x, y - 1});
                positions.add(new int[]{x, y - 2});
                return positions;
            }

            // Pawn isn't on starting line, can only move 1 space forward
            else if (y != 12) {

                if (Board.checkPieceBlock(x, y - 1, playerArray, cpuArray)) {
                    return positions;
                }

                positions.add(new int[]{x, y - 1});

                return positions;
            }
        }

        if (!stringColour.equals(playerArray.get(0).getPlayerColour())) {
            // Pawn is at the starting line can movve either 1 or 2 spaces forawrd
            if (y == 1) {

                // check if piece is blocking path 
                // white pieces 
                if (Board.checkPieceBlock(x, y + 1, playerArray, cpuArray)) {
                    return positions;
                }

                if (Board.checkPieceBlock(x, y + 2, playerArray, cpuArray)) {
                    positions.add(new int[]{x, y + 1});
                    return positions;
                }

                positions.add(new int[]{x, y + 1});
                positions.add(new int[]{x, y + 2});
                return positions;
            }

            // Pawn isn't on starting line, can only move 1 space forward
            else if (y != 1) {
                foundMatch = false;

                // Check if black piece is blocking path
                // Check aginst Black pieces
                if (Board.checkPieceBlock(x, y + 1, playerArray, cpuArray)) {
                    return positions;
                }

                positions.add(new int[]{x, y + 1});
                return positions;
            }
        }
        return null;
    }

    /**
     * checks that clicked piece can be moved to clicked position/title
     * @param possiblePositionsArray list of possible positions
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @param possibleAttackPositionsArray list of possible attack positions
     * @param arrayX x array coordinates
     * @param arrayY y array coordinates
     * @return boolean, true or false
     * 
     */
    public boolean checkValidMove(ArrayList<int[]> possiblePositionsArray, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray, ArrayList<int[]> possibleAttackPositionsArray, int arrayX, int arrayY) {

        ArrayList<int[]> attackArray = new ArrayList<int[]>();
        ArrayList<int[]> tempArray = new ArrayList<int[]>();

        int tempX = this.x;
        int tempY = this.y;

        // adds the attacked postions to the new Array - giving the postions which are currently being attacked
        if (possibleAttackPositionsArray != null) {

            for (Pieces b : cpuArray) {
                for (int[] p : possibleAttackPositionsArray) {

                    if (b.getX() / 48 == p[0] && b.getY() / 48 == p[1]) {
                        int[] attack = {b.getX() / 48, b.getY() / 48};
                        attackArray.add(attack);
                    }
                }
            }   
        }

        boolean canAttack = true;
        // check if piece is pinned
        this.x = arrayX * 48;
        this.y = arrayY * 48;


        // check if you can take pinning piece
        Pieces attackingPiece = Pieces.getPieceAttacingKing(cpuArray, playerArray);
        if ( attackingPiece != null && (attackingPiece.getX() == this.x && attackingPiece.getY() == this.y)) {
            this.x = tempX;
            this.y = tempY;
            return true;
        }

        ArrayList<int[]> allCPUAttacks = Pieces.getAllAttackPositions(cpuArray, playerArray);

        if (Board.checkForKingCheck(allCPUAttacks, playerArray, cpuArray)) {
            this.x = tempX;
            this.y = tempY;
            return false;
        }
        this.x = tempX;
        this.y = tempY;

        ArrayList<int[]> totalPositions = new ArrayList<int[]>();
        totalPositions.addAll(attackArray);
        totalPositions.addAll(possiblePositionsArray);

        // get king piece
        Pieces king = Pieces.getKing(playerArray, cpuArray);

        if (this.checkLeftCanCastle(playerArray, cpuArray) && this.type.equals("king")) { totalPositions.add(new int[]{king.getX() / 48 - 2, king.getY() / 48}); }

        if (this.checkRightCanCastle(playerArray, cpuArray) && this.type.equals("king")) { totalPositions.add(new int[]{king.getX() / 48 + 2, king.getY() / 48});}

        for (int[] position : totalPositions) {
            if (position[0] == arrayX && position[1] == arrayY) {
                return true;
            }
        }
        return false;
    }


     /**
     * Returns all possible move positions for the rook piece. (up-y)(down-y)(x-right)(x-left)
     * @param x x-coordinate
     * @param y y-coordinate
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return list of rook possible moves
     */
    public static ArrayList<int[]> getPossibleRookMoves(int x, int y, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        ArrayList<int[]> rookPositions = new ArrayList<int[]>();
        boolean foundMatch = false;

                // Check xLeft 
                foundMatch = false;
                for (int xLeft = x - 1; xLeft >= 0 && !foundMatch; xLeft--) {
                    rookPositions.add(new int[]{xLeft, y});

                    if (Board.checkPieceBlock(xLeft, y, playerArray, cpuArray)) {
                        foundMatch = true;
                        rookPositions.remove(rookPositions.size() - 1);
                        break;
                    }
                }


                // Check xRight
                foundMatch = false;
                for (int xRight = x + 1; xRight < 14 && !foundMatch; xRight++) {
                    rookPositions.add(new int[]{xRight, y});

                    if (Board.checkPieceBlock(xRight, y, playerArray, cpuArray)) {
                        foundMatch = true;
                        rookPositions.remove(rookPositions.size() - 1);
                        break;
                    }
                }

                // Check yUp
                foundMatch = false;
                for (int yUp = y - 1; yUp >= 0 && !foundMatch; yUp--) {
                    rookPositions.add(new int[]{x, yUp});

                    if (Board.checkPieceBlock(x, yUp, playerArray, cpuArray)) {
                        foundMatch = true;
                        rookPositions.remove(rookPositions.size() - 1);
                        break;
                    }
                }

                // Check yDown
                foundMatch = false;
                for (int yDown = y + 1; yDown < 14 && !foundMatch; yDown++) {
                    rookPositions.add(new int[]{x, yDown});

                    if (Board.checkPieceBlock(x, yDown, playerArray, cpuArray)) {
                        foundMatch = true;
                        rookPositions.remove(rookPositions.size() - 1);
                        break;
                    }
                }

        return rookPositions;
    }

    /**
     * Returns all possible move positions for the knight piece.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return list of knight possible moves
     */
    public static ArrayList<int[]> getPossibleKnightMoves(int x, int y, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        int[][] possibleKnightMoves = {
            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };

        ArrayList<int[]> knightPositions = new ArrayList<int[]>();

        for (int[] move : possibleKnightMoves) {
            int newX = x + move[0];
            int newY = y + move[1];

            // Check if the new move is within the board limits
            if (newX >= 0 && newX < 14 && newY >= 0 && newY < 14) {
                if (!Board.checkPieceBlock(newX, newY, playerArray, cpuArray)) {
                    int[] validMove = {newX, newY};
                    knightPositions.add(validMove);
                }
            }
        }

        return knightPositions;
    }


    /**
     * Returns all possible move positions for the bishop piece.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return list of bishop possible moves
     */
    public static ArrayList<int[]> getPossibleBishopMoves(int x, int y, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        ArrayList<int[]> bishopPositions = new ArrayList<int[]>();
        boolean foundMatch = false;

        // Checks bottom right positions
        // Check all diagonal directions for possible moves
        foundMatch = false;
        for (int i = 1; i < 14 && !foundMatch; i++) {
            int newX = x + i;
            int newY = y + i;
            
            // Check if the new move is within the board limits
            if (newX >= 0 && newX < 14 && newY >= 0 && newY < 14) {
                int[] validMove = {newX, newY};
                
                bishopPositions.add(validMove);

                if (Board.checkPieceBlock(newX, newY, playerArray, cpuArray)) {
                    foundMatch = true;
                    bishopPositions.remove(bishopPositions.size() - 1);
                    break;
                }
            }
        }


        // Checks top left positions
        foundMatch = false;
        for (int i = 1; i < 14 && !foundMatch; i++) {
            int newX = x - i;
            int newY = y - i;
            
            // Check if the new move is within the board limits
            if (newX >= 0 && newX < 14 && newY >= 0 && newY < 14) {
                int[] validMove = {newX, newY};
                
                bishopPositions.add(validMove);

                if (Board.checkPieceBlock(newX, newY, playerArray, cpuArray)) {
                    foundMatch = true;
                    bishopPositions.remove(bishopPositions.size() - 1);
                    break;
                }
            }
        }
        
        // Checks top right positions
        foundMatch = false;
        for (int i = 1; i < 14 && !foundMatch; i++) {
            int newX = x + i;
            int newY = y - i;
            
            // Check if the new move is within the board limits
            if (newX >= 0 && newX < 14 && newY >= 0 && newY < 14) {
                int[] validMove = {newX, newY};
                
                bishopPositions.add(validMove);

                if (Board.checkPieceBlock(newX, newY, playerArray, cpuArray)) {
                    foundMatch = true;
                    bishopPositions.remove(bishopPositions.size() - 1);
                    break;
                }
            }
        }

        // Checks bottom left positions
        foundMatch = false;
        for (int i = 1; i < 14 && !foundMatch; i++) {
            int newX = x - i;
            int newY = y + i;
            
            // Check if the new move is within the board limits
            if (newX >= 0 && newX < 14 && newY >= 0 && newY < 14) {
                int[] validMove = {newX, newY};
                
                bishopPositions.add(validMove);

                if (Board.checkPieceBlock(newX, newY, playerArray, cpuArray)) {
                    foundMatch = true;
                    bishopPositions.remove(bishopPositions.size() - 1);
                    break;
                }
            }
        }
        return bishopPositions;
    }

    /**
     * Returns all possible move positions for the camel piece.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return list of camel possible moves
     */
    public static ArrayList<int[]> getPossibleCamelMoves(int x, int y, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        int[][] possibleCamelMoves = {
            {-3, -1}, {-3, 1}, {-1, -3}, {-1, 3},
            {1, -3}, {1, 3}, {3, -1}, {3, 1}
        };

        ArrayList<int[]> camelPositions = new ArrayList<int[]>();
        boolean foundMatch = false;

        for (int[] move : possibleCamelMoves) {
            int newX = x + move[0];
            int newY = y + move[1];

            // Check if the new move is within the board limits
            if (newX >= 0 && newX < 14 && newY >= 0 && newY < 14) {
                int[] validMove = {newX, newY};
                foundMatch = false;

                if (Board.checkPieceBlock(newX, newY, playerArray, cpuArray)) {
                    foundMatch = true;
                }

                if (!foundMatch) {
                    camelPositions.add(validMove);
                    
                }
            }
        }
        return camelPositions;
    }

    /**
     * Returns all possible move positions for the king piece.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return list of king possible moves
     */
    public static ArrayList<int[]> getPossibleKingMoves(int x, int y, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        // get all black attacked positions
        ArrayList<int[]> allBlackAttacks = getAllAttackPositions(cpuArray, playerArray);

        int[][] possibleKingMoves = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
        };

        ArrayList<int[]> kingPositions = new ArrayList<int[]>();
        boolean foundMatch = false;

        for (int[] move : possibleKingMoves) {
            int newX = x + move[0];
            int newY = y + move[1];

            // Check if the new move is within the board limits
            if (newX >= 0 && newX < 14 && newY >= 0 && newY < 14) {
                int[] validMove = {newX, newY};
                foundMatch = false;

                if (Board.checkPieceBlock(newX, newY, playerArray, cpuArray)) {
                    foundMatch = true;
                }
                
                // remove any positions which black can attack 
                for (int[] a : allBlackAttacks) {
                    if (Arrays.equals(validMove, a)) {
                        foundMatch = true;
                    }
                }

                if (!foundMatch) {
                    kingPositions.add(validMove);
                }
            }
        }
        return kingPositions;
    }

    /**
     * Returns all possible move positions for the general piece.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return list of general possible moves
     */
    public static ArrayList<int[]> getPossibleGeneralKingMoves(int x, int y, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        int[][] possibleKingMoves = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
        };

        ArrayList<int[]> generalKingPositions = new ArrayList<int[]>();
        boolean foundMatch = false;

        for (int[] move : possibleKingMoves) {
            int newX = x + move[0];
            int newY = y + move[1];

            // Check if the new move is within the board limits
            if (newX >= 0 && newX < 14 && newY >= 0 && newY < 14) {
                int[] validMove = {newX, newY};
                foundMatch = false;

                if (Board.checkPieceBlock(newX, newY, playerArray, cpuArray)) {
                    foundMatch = true;
                }

                if (!foundMatch) {
                    generalKingPositions.add(validMove);
                }
            }
        }

        return generalKingPositions;
    }

    /**
     * Returns all possible move positions for the archbishop piece.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return list of archbishop possible moves
     */
    public static ArrayList<int[]> getPossibleArchbishopMoves(int x, int y, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {
        ArrayList<int[]> archbishopPositions = new ArrayList<int[]>();
        ArrayList<int[]> archbishopBishopPositions = getPossibleBishopMoves(x, y, playerArray, cpuArray);
        ArrayList<int[]> archbishopKnightPositions = getPossibleKnightMoves(x, y, playerArray, cpuArray);
        archbishopPositions.addAll(archbishopBishopPositions);
        archbishopPositions.addAll(archbishopKnightPositions);

        return archbishopPositions;
    }


    /**
     * Returns all possible move positions for the general piece.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return list of general possible moves
     */
    public static ArrayList<int[]> getPossibleGeneralMoves(int x, int y, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {
        ArrayList<int[]> generalPositions = new ArrayList<int[]>();
        ArrayList<int[]> generalKingPositions = getPossibleGeneralKingMoves(x, y, playerArray, cpuArray);
        ArrayList<int[]> generalKnightPositions = getPossibleKnightMoves(x, y, playerArray, cpuArray);
        generalPositions.addAll(generalKingPositions);
        generalPositions.addAll(generalKnightPositions);

        return generalPositions;
    }

    /**
     * Returns all possible move positions for the amazon piece.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return list of amazon possible moves
     */
    public static ArrayList<int[]> getPossibleAmazonMoves(int x, int y, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {
        ArrayList<int[]> amazonPositions = new ArrayList<int[]>();
        ArrayList<int[]> amazonKnightPositions = getPossibleKnightMoves(x, y, playerArray, cpuArray);
        ArrayList<int[]> amazonBishopPositions = getPossibleBishopMoves(x, y, playerArray, cpuArray);
        ArrayList<int[]> amazonRookPositions = getPossibleRookMoves(x, y, playerArray, cpuArray);
        amazonPositions.addAll(amazonKnightPositions);
        amazonPositions.addAll(amazonBishopPositions);
        amazonPositions.addAll(amazonRookPositions);

        return amazonPositions;
    }

    /**
     * Returns all possible move positions for the chancellor piece.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return list of chancellor possible moves
     */
    public static ArrayList<int[]> getPossibleChancellorMoves(int x, int y, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {
        ArrayList<int[]> chancellorPositions = new ArrayList<int[]>();
        ArrayList<int[]> chancellorKnightPositions = getPossibleKnightMoves(x, y, playerArray, cpuArray);
        ArrayList<int[]> chancellorRookPositions = getPossibleRookMoves(x, y, playerArray, cpuArray);
        chancellorPositions.addAll(chancellorKnightPositions);
        chancellorPositions.addAll(chancellorRookPositions);

        return chancellorPositions;
    }

    /**
     * Returns all possible move positions for the queen piece.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return list of queen possible moves
     */
    public static ArrayList<int[]> getPossibleQueenMoves(int x, int y, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {
        ArrayList<int[]> queenPositions = new ArrayList<int[]>();
        ArrayList<int[]> queenBishopPositions = getPossibleBishopMoves(x, y, playerArray, cpuArray);
        ArrayList<int[]> queenRookPositions = getPossibleRookMoves(x, y, playerArray, cpuArray);
        queenPositions.addAll(queenBishopPositions);
        queenPositions.addAll(queenRookPositions);

        return queenPositions;
    }


    /**
     *  Returns attacking positions for the given object. Returns a list
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return array of attacking positions
     */
    public ArrayList<int[]> getAttackingPositions(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {
        String type = this.type;
        char colour = this.colour;

        switch (type) {
            case "pawn":
                return this.getPossibleAttackPawnMoves(playerArray, cpuArray, colour);

            case "rook":
                return this.getPossibleAttackRookMoves(playerArray, cpuArray);

            case "knight":
                return this.getPossibleAttackKnightMoves(playerArray, cpuArray);

            case "bishop":
                return this.getPossibleAttackBishopMoves(playerArray, cpuArray);

            case "archbishop":
                return this.getPossibleAttackArchbishopMoves(playerArray, cpuArray);

            case "camel":
                return this.getPossibleAttackCamelMoves(playerArray, cpuArray);

            case "general":
                return this.getPossibleAttackGeneralMoves(playerArray, cpuArray);

            case "amazon":
                return this.getPossibleAttackAmazonMoves(playerArray, cpuArray);

            case "king":
                return this.getPossibleAttackKingMoves(playerArray, cpuArray);

            case "chancellor":
                return this.getPossibleAttackChancellorMoves(playerArray, cpuArray);

            case "queen":
                return this.getPossibleAttackQueenMoves(playerArray, cpuArray);

            default : 
                return null;
        }
    }

    /**
     * get piece thats attacking king
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return pieces king object
     */
    public static Pieces getPieceAttacingKing(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        Pieces king = Pieces.getKing(cpuArray, playerArray);

        for (Pieces piece : playerArray) {

            ArrayList<int[]> attackPositions = piece.getAttackingPositions(playerArray, cpuArray);

            for (int[] a : attackPositions) {
                // check against king position
                if (king.getX() / 48 == a[0] && king.getY() / 48 == a[1]) {
                    return piece;
                }
            }
        }
        return null;

    }

    /**
     * Returns attacking positions for the given pawn. Returns a list
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @param colour char piece colour
     * @return possible pawn attack moves
     */
    public ArrayList<int[]> getPossibleAttackPawnMoves( ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray, char colour) {
        
        int x = this.getX() / 48;
        int y = this.getY() / 48;

        String stringColour;
        if (colour == 'b') {
            stringColour = "black";
        }
        else {stringColour = "white";}

        boolean isPlayer = playerArray.contains(this);
        boolean isCPU = cpuArray.contains(this);

        int[][] possiblePawnAttackArray = null;
        if (!stringColour.equals(this.playerColour)) { 
            possiblePawnAttackArray = new int[][]{{-1, 1}, {1, 1}}; // {-1, -1} {1, -1} white {-1, 1} {1, 1} black
        }

        if (stringColour.equals(this.playerColour)) {
            possiblePawnAttackArray = new int[][]{{-1, -1}, {1, -1}}; // {-1, -1} {1, -1} white {-1, 1} {1, 1} black
        }
        
        ArrayList<int[]> attackPositions = new ArrayList<int[]>();

        // Search through black pieces for possible attack 
        for (int[] move : possiblePawnAttackArray) {
            int newX = x + move[0];
            int newY = y + move[1];

            // Check if the new move is within the board limits
            if (newX >= 0 && newX < 14 && newY >= 0 && newY < 14) {
                int[] validMove = {newX, newY};

                attackPositions.add(validMove);
                
                // if (Board.checkWhitePieceBlock(newX, newY, playerArray)) {
                //     attackPositions.remove(attackPositions.size() - 1);
                // }
            }
        }

        return attackPositions;
    }

     /**
     * Returns attacking positions for the given rook piece. Returns a list
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return possible rook attack moves
     */
    public ArrayList<int[]> getPossibleAttackRookMoves(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        int x = this.getX() / 48;
        int y = this.getY() / 48;

        ArrayList<int[]> attackPositions = new ArrayList<int[]>();

        // check for the black piece above - y-axis --
        for (int i = y - 1; i >= 0; i--) {

            int[] attack = {x, i};
            attackPositions.add(attack);

            if (Board.checkBlackPieceBlock(x, i, cpuArray)) {
                break;
            }

            if (Board.checkWhitePieceBlock(x, i, playerArray)) {
                attackPositions.remove(attackPositions.size() - 1);
                break;
            }
        }

        // check for the black piece below - y-axis ++
        for (int i = y + 1; i < 14; i++) {
            int[] attack = {x, i};
            attackPositions.add(attack);

            if (Board.checkBlackPieceBlock(x, i, cpuArray)) {
                break;
            }

            if (Board.checkWhitePieceBlock(x, i, playerArray)) {
                attackPositions.remove(attackPositions.size() - 1);
                break;
            }
        }
        
        // check for the black piece to the right - x-axis ++
        for (int i = x + 1; i < 14; i++) {

            int[] attack = {i, y};
            attackPositions.add(attack);

            if (Board.checkBlackPieceBlock(i, y, cpuArray)) {
                break;

            }

            if (Board.checkWhitePieceBlock(i, y, playerArray)) {
                attackPositions.remove(attackPositions.size() - 1);
                break;
            }
        }

        // check for the black piece to the left - x-axis --
        for (int i = x - 1; i >= 0; i--) {
            int[] attack = {i, y};
            attackPositions.add(attack);

            if (Board.checkBlackPieceBlock(i, y, cpuArray)) {
                break;
            }

            if (Board.checkWhitePieceBlock(i, y, playerArray)) {
                attackPositions.remove(attackPositions.size() - 1);
                break;
            }
        }
        return attackPositions;
    }

     /**
     * Returns the attack positions for the knight piece. Returns a list
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return possible knight attack moves
     */
    public ArrayList<int[]> getPossibleAttackKnightMoves(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        int x = this.x / 48;
        int y = this.y / 48;

        int[][] possibleKnightMoves = {
            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };

        ArrayList<int[]> attackPositions = new ArrayList<int[]>();


        for (int[] move : possibleKnightMoves) {
            int newX = x + move[0];
            int newY = y + move[1];

            // Check if the new move is within the board limits
            if (newX >= 0 && newX < 14 && newY >= 0 && newY < 14) {
                int[] validMove = {newX, newY};

                attackPositions.add(validMove);

                // if (Board.checkWhitePieceBlock(newX, newY, playerArray)) {
                //     attackPositions.remove(attackPositions.size() - 1);
                // }

            }
        }
        return attackPositions;
    }

      /**
     * Returns the attack positions for the bishop piece. Returns a list
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return possible bishop attack moves
     */
    public ArrayList<int[]> getPossibleAttackBishopMoves(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        int x = this.x / 48;
        int y = this.y / 48;

        ArrayList<int[]> attackPositions = new ArrayList<int[]>();

        // Checks bottom right positions
        // Check all diagonal directions for possible moves
        for (int i = 1; i < 14; i++) {
            int newX = x + i;
            int newY = y + i;
            
            // Check if the new move is within the board limits
            if (newX < 14 && newX >= 0 && newY < 14 && newY >= 0) {
                int[] validMove = {newX, newY};

                attackPositions.add(validMove);
                
                if (Board.checkBlackPieceBlock(newX, newY, cpuArray)) {
                    break;
                }
    
                if (Board.checkWhitePieceBlock(newX, newY, playerArray)) {
                    attackPositions.remove(attackPositions.size() - 1);
                    break;
                }
            }
        }

        // checks top right position
        for (int i = 1; i < 14; i++) {
            int newX = x + i;
            int newY = y - i;
            
            // Check if the new move is within the board limits
            if (newX < 14 && newX >= 0 && newY < 14 && newY >= 0) {
                int[] validMove = {newX, newY};

                attackPositions.add(validMove);

                if (Board.checkBlackPieceBlock(newX, newY, cpuArray)) {
                    break;
                }
    
                if (Board.checkWhitePieceBlock(newX, newY, playerArray)) {
                    attackPositions.remove(attackPositions.size() - 1);
                    break;
                }
            }
        }

        // checks bottom left position
        for (int i = 1; i < 14; i++) {
            int newX = x - i;
            int newY = y + i;
            
            // Check if the new move is within the board limits
            if (newX < 14 && newX >= 0 && newY < 14 && newY >= 0) {
                int[] validMove = {newX, newY};

                attackPositions.add(validMove);

                if (Board.checkBlackPieceBlock(newX, newY, cpuArray)) {
                    break;
                }
    
                if (Board.checkWhitePieceBlock(newX, newY, playerArray)) {
                    attackPositions.remove(attackPositions.size() - 1);
                    break;
                }
            }
        }

        // checks bottom left position
        for (int i = 1; i < 14; i++) {
            int newX = x - i;
            int newY = y - i;
            
            // Check if the new move is within the board limits
            if (newX < 14 && newX >= 0 && newY < 14 && newY >= 0) {
                int[] validMove = {newX, newY};

                attackPositions.add(validMove);

                if (Board.checkBlackPieceBlock(newX, newY, cpuArray)) {
                    break;
                }
    
                if (Board.checkWhitePieceBlock(newX, newY, playerArray)) {
                    attackPositions.remove(attackPositions.size() - 1);
                    break;
                }
            }
        }
        return attackPositions;

    }

    /**
     * Returns the attack positions for the archbishop piece. Returns a list
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return possible archbishop attack moves
     */
    public ArrayList<int[]> getPossibleAttackArchbishopMoves(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        ArrayList<int[]> archbishopPositions = new ArrayList<int[]>();
        ArrayList<int[]> archbishopBishopPositions = getPossibleAttackBishopMoves(playerArray, cpuArray);
        ArrayList<int[]> archbishopKnightPositions = getPossibleAttackKnightMoves(playerArray, cpuArray);
        archbishopPositions.addAll(archbishopBishopPositions);
        archbishopPositions.addAll(archbishopKnightPositions);

        return archbishopPositions;

    }

    /**
     * Returns the attack positions for the camel piece. Returns a list
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return possible camel attack moves
     */
    public ArrayList<int[]> getPossibleAttackCamelMoves(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        int x = this.x / 48;
        int y = this.y / 48;

        int[][] possibleKnightMoves = {
            {-3, -1}, {-3, 1}, {-1, -3}, {-1, 3},
            {1, -3}, {1, 3}, {3, -1}, {3, 1}
        };

        ArrayList<int[]> attackPositions = new ArrayList<int[]>();
        
        for (int[] move : possibleKnightMoves) {
            int newX = x + move[0];
            int newY = y + move[1];

            // Check if the new move is within the board limits
            if (newX >= 0 && newX < 14 && newY >= 0 && newY < 14) {
                int[] validMove = {newX, newY};

                attackPositions.add(validMove);
                
                // if (Board.checkWhitePieceBlock(newX, newY, playerArray)) {
                //     attackPositions.remove(attackPositions.size() - 1);
                //     break;
                // }
            }
        }

        return attackPositions;
    }
    
    /**
     * Returns the attack positions for the general piece. Returns a list
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return possible general attack moves
     */
    public ArrayList<int[]> getPossibleAttackGeneralMoves(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        ArrayList<int[]> generalPositions = new ArrayList<int[]>();
        ArrayList<int[]> generalKingPositions = getPossibleAttackKingMoves(playerArray, cpuArray);
        ArrayList<int[]> generalKnightPositions = getPossibleAttackKnightMoves(playerArray, cpuArray);
        generalPositions.addAll(generalKingPositions);
        generalPositions.addAll(generalKnightPositions);

        return generalPositions;
    }

    /**
     * Returns the attack positions for the amazon piece. Returns a list
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return possible amazon attack moves
     */
    public ArrayList<int[]> getPossibleAttackAmazonMoves(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {
        
        ArrayList<int[]> amazonPositions = new ArrayList<int[]>();

        ArrayList<int[]> amazonKnightPositions = getPossibleAttackKnightMoves(playerArray, cpuArray);
        if (amazonKnightPositions != null) {
            amazonPositions.addAll(amazonKnightPositions);
        }

        ArrayList<int[]> amazonBishopPositions = getPossibleAttackBishopMoves(playerArray, cpuArray);
        if (amazonBishopPositions != null) {
            amazonPositions.addAll(amazonBishopPositions);
        }

        ArrayList<int[]> amazonRookPositions = getPossibleAttackRookMoves(playerArray, cpuArray);
        if (amazonRookPositions != null) {
            amazonPositions.addAll(amazonRookPositions);
        }

        return amazonPositions;
    }

    /**
     * Returns the attack positions for the king piece. Returns a list
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return possible king attack moves
     */
    public ArrayList<int[]> getPossibleAttackKingMoves(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        int x = this.x / 48;
        int y = this.y / 48;
        
        int[][] possibleKingMoves = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
        };

        ArrayList<int[]> attackPositions = new ArrayList<int[]>();

        for (int[] move : possibleKingMoves) {
            int newX = x + move[0];
            int newY = y + move[1];

            // Check if the new move is within the board limits
            if (newX >= 0 && newX < 14 && newY >= 0 && newY < 14) {
                int[] validMove = {newX, newY};
                attackPositions.add(validMove);

                // if (Board.checkWhitePieceBlock(newX, newY, playerArray)) {
                //     attackPositions.remove(attackPositions.size() - 1);
                // }
            }
        }
        return attackPositions;

    }

    /**
     * Returns the attack positions for the chancellor piece. Returns a list
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return possible chancellor attack moves
     */
    public ArrayList<int[]> getPossibleAttackChancellorMoves(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {
        ArrayList<int[]> chancellorPositions = new ArrayList<int[]>();
        ArrayList<int[]> chancellorKnightPositions = getPossibleAttackKnightMoves(playerArray, cpuArray);
        ArrayList<int[]> chancellorRookPositions = getPossibleAttackRookMoves(playerArray, cpuArray);

        if (chancellorKnightPositions != null) {
            chancellorPositions.addAll(chancellorKnightPositions);
        }

        if (chancellorRookPositions != null) {
            chancellorPositions.addAll(chancellorRookPositions);
        }

        return chancellorPositions;
    }

    
    /**
     * Returns the attack positions for the queen piece. Returns a list
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return possible queen attack moves
     */
    public ArrayList<int[]> getPossibleAttackQueenMoves(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {
        ArrayList<int[]> queenPositions = new ArrayList<int[]>();
        ArrayList<int[]> queenBishopPositions = getPossibleAttackBishopMoves(playerArray, cpuArray);
        ArrayList<int[]> queenRookPositions = getPossibleAttackRookMoves(playerArray, cpuArray);
        queenPositions.addAll(queenBishopPositions);
        queenPositions.addAll(queenRookPositions);

        return queenPositions;
    }

     /**
     * Returns all attacking postions on the board. white attacking
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return list containing all attack positions
     */
    public static ArrayList<int[]> getAllAttackPositions(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        ArrayList<int[]> allAttackPositions = new ArrayList<int[]>();

        for (Pieces w : playerArray) {
            allAttackPositions.addAll(w.getAttackingPositions(playerArray, cpuArray));
        } 
        
        return allAttackPositions;
    }


    /**
     * Returns all attacking postions from pieces of lower value
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @param Piece gets the attack positions for that piece
     * @return list containing all attack positions 
     * 
     */
    public static ArrayList<int[]> getAllAttackPositions(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray, Pieces Piece) {

        ArrayList<int[]> allAttackPositions = new ArrayList<int[]>();
        for (Pieces w : playerArray) {

            // check if white piece is of lower value than black piece
            if (Piece.getValue() > w.getValue()) {
                allAttackPositions.addAll(w.getAttackingPositions(playerArray, cpuArray));
            }
        } 
        return allAttackPositions;
    }


    /**
     * Returns black Piece and its moved to position, which will threaten a undefened white piece via a safe square
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @param allUnsafePositions list contains all unsafe positions
     * @return MoveResult object for future move 
     */
    public static MoveResult getFutureMove(ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray, ArrayList<int[]> allUnsafePositions) {

        // varibles which will be returned
        Pieces maxPiece = null;
        double max = 0;
        int x = 0;
        int y = 0;

        // temp variables
        int tempX = 0;
        int tempY = 0;

        Pieces attackedPiece = null;

        // look through all black pieces 
        for (Pieces b : cpuArray) {

            // returns all possible moveable positions
            ArrayList<int[]> possiblePositions = b.getPossibleMoves(playerArray, cpuArray);

            // returns all safe possible moveable positions 
            ArrayList<int[]> safePossiblePositions = Board.removeCommonPositions(possiblePositions, allUnsafePositions);
        
            // iterate through the safe moveable positions - go thought attacks
            for (int[] move : safePossiblePositions) {
                
                // temperarly set x and y coordinates to the moveable positions
                int moveX = move[0] * 48;
                int moveY = move[1] * 48;

                tempX = b.getX();
                tempY = b.getY();

                b.setX(moveX);
                b.setY(moveY);

                // all new possible attacks
                ArrayList<int[]> newAttacks = b.getAttackingPositions(cpuArray, playerArray);

                attackedPiece = Board.checkForHighestValueAttackedPiece(newAttacks, playerArray);

                if (attackedPiece != null && attackedPiece.getValue() >= max) {

                    maxPiece = b;
                    max = maxPiece.getValue(); 
                    x = moveX / 48;
                    y = moveY / 48;
                }
                
                // set the coordinates back to the original
                b.setX(tempX);
                b.setY(tempY);
            }
        }


        // Create a new integer array
        int[] coordinate = {x, y};

        MoveResult moveResult = new MoveResult(maxPiece, coordinate); // create a new MoveResult object with the black piece and capture coordinate
        return moveResult;
    }
}