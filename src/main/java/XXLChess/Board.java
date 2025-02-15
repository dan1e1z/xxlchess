package XXLChess;                    

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;
import processing.core.PFont;
import processing.event.MouseEvent;
import processing.core.PGraphics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.awt.Font;
import java.io.*;
import java.util.*;

/**
 * This class represents the chessboard.
 * which provides basic functionality for drawing and interacting with the user.
 */
public class Board {

    /**
     * current square on chessboard which is being highlighted
     */
    protected ArrayList<int[]> currentHighlightedTitles = new ArrayList<int[]>();

    /**
     * array which stores the possible positions of a piece
     */
    protected ArrayList<int[]> possiblePositionsArray = new ArrayList<>();

    /**
     * array which stores the possible attack positions of a piece
     */
    protected ArrayList<int[]> possibleAttackPositionsArray = new ArrayList<>();

    // protected ArrayList<int[]> prevousHighlightedTitles = new ArrayList<int[]>();

    // protected boolean blue, lightRed, green, yellow, darkRed = false;

    /**
     * constructor creates a board object 
     * allows for highllighting of chess board sqaures
     */
    public Board() {

        // current highlighted titles
        this.currentHighlightedTitles = currentHighlightedTitles;

        // previous highlighted titles
        // this.prevousHighlightedTitles = prevousHighlightedTitles;

        // all possible moves
        this.possiblePositionsArray = possiblePositionsArray;

        // all possible attack positions 
        this.possibleAttackPositionsArray = possibleAttackPositionsArray;

        // all difference coloured HighlightedTitles colours
        // this.blue = blue; // light blue (199,225,239,255)  -  dark blue (174,209,224,255)
        // this.lightRed = lightRed;
        // this.green = green; // (109,137,76,255)
        // this.yellow = yellow;
        // this.darkRed = darkRed;
    }

    /**
     * Checks if clicked position, has a chess piece
     * @param x x-coordinate
     * @param y y-coordinate
     * @param playerArray array storing player piece objects
     * @return boolean, true or false
     */
    public static boolean checkClickedForPiece(int x, int y, ArrayList<Pieces> playerArray) {

        for (Pieces p : playerArray) {

            if (x == (p.getX() / 48) && y == (p.getY() / 48)) {
                return true;
            }
        } 
        return false;
    }

    /**
     * Highlights selected chess piece in green
     * @param app reference to the Processing application object
     * @param clickedPiece piece that has been clicked
     */
    public void highlightSelectedPieces(PApplet app, Pieces clickedPiece) {
        app.fill(109,137,76,255); // green

        // fills in corrosponding colour into created rectange 
        app.rect(clickedPiece.x , clickedPiece.y, 48, 48);
        this.currentHighlightedTitles.add(new int[]{clickedPiece.x / 48, clickedPiece.y / 48});
 
    }

    /**
     * draws over previous piece aniamtion to allow for smooth animation 
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void tickClear(int x, int y) {
        this.currentHighlightedTitles.add(new int[]{x / 48, y / 48});
    }

    /**
     * gets highlighted titles
     * @return this.currentHighlightedTitles
     */
    public ArrayList<int[]> gethighlightSelectedPieces() {
        return this.currentHighlightedTitles;
    }

    /**
     * Returns True if king is in check, returns false otherwise
     * @param possibleAttackPositionsArray list storing all possible attack positions 
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return boolean, true or false 
     */
    public static boolean checkForKingCheck(ArrayList<int[]> possibleAttackPositionsArray, ArrayList<Pieces> cpuArray, ArrayList<Pieces> playerArray) {

        Pieces king = null;

        // looks for king piece
        for (Pieces p : cpuArray) {
            if (p.getType().equals("king")) {
                king = p;
            }
        }

        // checks if king is in check 
        for (int[] attack : possibleAttackPositionsArray) {
            if (king.getX() / 48 == attack[0] && king.getY() / 48 == attack[1]) {
                return true;
            }
        }
        return false;
    }

    /**
     * highlights checked king title in red
     * @param app reference to the Processing application object
     * @param cpuArray contains all piece objects of cpu
     */
    public static void checkHighlight(PApplet app, ArrayList<Pieces> cpuArray){

        for (Pieces b : cpuArray) {
            // check for king 
            if (b.getType().equals("king")) {
                app.fill(201,0,0,255);
                // int x = b.getX() / 48;
                // int y = b.getY() / 48;
                app.rect( b.getX(), b.getY(), 48, 48);
                return;
            }
        }
        return;
    }


    /**
     * resets colour of board to default
     * @param app reference to the Processing application object
     */
    public void resetColour(PApplet app) {

        for (int[] r : this.currentHighlightedTitles) {
            if ((r[0] + r[1]) % 2 == 0) {
                app.fill(237,216,179,255);  // white
            } else {
                app.fill(175,135,97,255);  // brown
            }
            // fills in corresponding color into created rectangle 
            app.rect(r[0] * 48 , r[1] * 48 , 48, 48);
        }   
        
    }

    /**
     * brute force tests all possible moves to see if it blocks check
     * @param app reference to the Processing application object
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return MoveResult, object containing the black piece object and its coordinates
     */
    public static MoveResult blockCheck(PApplet app, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {

        ArrayList<int[]> tryMoves = new ArrayList<int[]>();
        // check each move and see it check is still present if not, make that move
        ArrayList<int[]> allAttackedPositions =  new ArrayList<int[]>();
    
        int tempX = 0;
        int tempY = 0;
    
        for (Pieces b : cpuArray) {
            tryMoves = b.getPossibleMoves(cpuArray, playerArray);
    
            boolean check = true;
    
            for (int[] move : tryMoves) {
                tempX = b.getX();
                tempY = b.getY();
    
                b.setX(move[0] * 48);
                b.setY(move[1] * 48);

                allAttackedPositions = Pieces.getAllAttackPositions(playerArray, cpuArray);
                check = checkForKingCheck(allAttackedPositions, cpuArray, playerArray);
                resetBoardColours(app);
    
                if (check) {
                    b.setX(tempX);
                    b.setY(tempY);
                }
    
                if (!check) {
                    b.setX(tempX);
                    b.setY(tempY);

                    // Create a new integer array
                    int[] coordinate = {move[0], move[1]};

                    MoveResult moveResult = new MoveResult(b, coordinate); // create a new MoveResult object with the black piece and capture coordinate
                    return moveResult;
                }
            }
        }
        return null;
    }

    /**
     * brute force tests all possible moves to see if it blocks check
     * @param app reference to the Processing application object
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return boolean, true or false 
     */
    public static boolean whiteBlockCheck(PApplet app, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray) {
        ArrayList<int[]> tryMoves = new ArrayList<int[]>();
        // check each move and see it check is still present if not, make that move
        ArrayList<int[]> allAttackedPositions =  new ArrayList<int[]>();
    
        int tempX = 0;
        int tempY = 0;
    
        for (Pieces b : cpuArray) {
            tryMoves = b.getPossibleMoves(cpuArray, playerArray);
    
            boolean check = true;
    
            for (int[] move : tryMoves) {
                tempX = b.getX();
                tempY = b.getY();
    
                b.setX(move[0] * 48);
                b.setY(move[1] * 48);

                allAttackedPositions = Pieces.getAllAttackPositions(playerArray, cpuArray);

                check = checkForKingCheck(allAttackedPositions, cpuArray, playerArray);
                resetBoardColours(app);
    
                if (check) {
                    b.setX(tempX);
                    b.setY(tempY);
                }
    
                if (!check) {
                    b.setX(tempX);
                    b.setY(tempY);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * resets board colour 
     * @param app reference to the Processing application object
     */
    public static void resetBoardColours(PApplet app) {

        if (app == null) { return; }

        for (int row = 0; row < 14; row++) {
            for (int col = 0; col < 14; col++) {
                int currentColor = app.get(0, 0); // Store the current fill color
                if (currentColor != app.color(237, 216, 179, 255) || currentColor != app.color(175, 135, 97, 255)) {
                    // draw the square with the appropriate color
                    if ((row + col) % 2 == 0) {
                        app.fill(237,216,179,255);  // white
                    } else {
                        app.fill(175,135,97,255);  // brown
                    }
                }
                // fills in corresponding color into created rectangle 
                app.rect(row * 48 , col * 48 , 48, 48);
            }
        }
    }

    /**
     * Returns true if a piece is blocking another piece otherwise, false
     * @param x x-coordinate
     * @param y y-coordinate
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu 
     * @return boolean, true or false
     */
    public static boolean checkPieceBlock(int x, int y, ArrayList<Pieces> playerArray,  ArrayList<Pieces> cpuArray) {
 
        for (Pieces w : playerArray) {
            if ((w.getX() / 48) == x && (w.getY() / 48) == y) {
                return true;
            }
        }

        for (Pieces b : cpuArray) {
            if ((b.getX() / 48) == x && (b.getY() / 48) == y) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns true if a white piece is blocking another piece, otherwise false 
     * @param x x-coordinate
     * @param y y-coordinate
     * @param playerArray contains all piece objects of player
     * @return boolean, true or false
     */
    public static boolean checkWhitePieceBlock(int x, int y, ArrayList<Pieces> playerArray) {

        for (Pieces w : playerArray) {
            if ((w.getX() / 48) == x && (w.getY() / 48) == y) {
                return true;
            }
        }

        return false;
    }


    /**
     * Returns true if a black piece is blocking another piece, otherwise false 
     * @param x x-coordinate
     * @param y y-coordinate
     * @param cpuArray contains all piece objects of cpu 
     * @return boolean, true or false 
     */
    public static boolean checkBlackPieceBlock(int x, int y, ArrayList<Pieces> cpuArray) {

        for (Pieces b : cpuArray) {
            if ((b.getX() / 48) == x && (b.getY() / 48) == y) {
                return true;
            }
        }

        return false;
    }

    /**
     * returns attacked piece which has the highest value
     * @param possibleAttackPositionsArray list containing all the attack positions
     * @param cpuArray contains all piece objects of cpu 
     * @return Pieces, piece object of hightest valued called piece 
     */
    public static Pieces checkForHighestValueAttackedPiece(ArrayList<int[]> possibleAttackPositionsArray, ArrayList<Pieces> cpuArray) {

        Pieces attackPiece = null;
        double value = 0;

        // looks for attacked piece
        for (Pieces p : cpuArray) {
            
            for (int[] attack : possibleAttackPositionsArray) {
                if (p.getX() / 48 == attack[0] && p.getY() / 48 == attack[1]) {
                    
                    // compare to value of other attacked piece
                    if (p.getValue() > value) {
                        value = p.getValue();
                        attackPiece = p;
                    }
                }
            }
        }
        return attackPiece;
    }

    /**
     * Returns true if piece possible moves/attacks can take a cpu piece
     * @param x x-coordinate
     * @param y y-coordinate
     * @param cpuArray contains all piece objects of cpu 
     * @return boolean, true or false 
     */
    public static boolean checkAttackingPosition(int x, int y, ArrayList<Pieces> cpuArray) {

        for (Pieces b : cpuArray) {

            if (b.getX() / 48 == x && b.getY() / 48 == y) {
                return true;
            }
        }
        return false;
    }


    /**
     * Highlights Board with possible Pieces movements - including attack movements (blue)(red)
     * @param app reference to the Processing application object
     * @param possiblePositionsArray list containing all movement positions
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @param possibleAttackPositionsArray list containing all the attack positions
     * @param playerPiece clicked player piece object
     */
    public void highlightMoves(PApplet app, ArrayList<int[]> possiblePositionsArray, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray, ArrayList<int[]> possibleAttackPositionsArray, Pieces playerPiece) {
        
        ArrayList<int[]> attackArray = new ArrayList<int[]>();
        ArrayList<int[]> moveArray = new ArrayList<int[]>();

        ArrayList<int[]> totalCheck = new ArrayList<int[]>();
        totalCheck.addAll(possiblePositionsArray);
        totalCheck.addAll(possibleAttackPositionsArray);
        
        for (int[] m : totalCheck) {

            if (playerPiece.checkValidMove(possiblePositionsArray, playerArray, cpuArray, possibleAttackPositionsArray, m[0], m[1])) {
                moveArray.add(m);
            }
        }

        // get king piece 
        Pieces king = Pieces.getKing(playerArray, cpuArray);

        // check if can castle righcheckRightCanCastlet side 
        if (king.checkRightCanCastle(playerArray, cpuArray) && playerPiece.getType().equals("king")) {
            moveArray.add(new int[]{king.getX() / 48 + 2, king.getY() / 48});
        }

        // check if can castle left side 
        if (king.checkLeftCanCastle(playerArray, cpuArray) && playerPiece.getType().equals("king")) {
            moveArray.add(new int[]{king.getX() / 48 - 2, king.getY() / 48});
        }

        ArrayList<int[]> attackArray1 = new ArrayList<int[]>();


        // // adds the attacked postions to the new Array - giving the postions which are currently being attacked
        if (moveArray != null) {

            for (Pieces b : cpuArray) {
                for (int[] p : moveArray) {

                    if (b.getX() / 48 == p[0] && b.getY() / 48 == p[1]) {
                        int[] attack = {b.getX() / 48, b.getY() / 48};
                        attackArray.add(attack);
                        // this.currentHighlightedTitles.add(attack);
                    }
                }
            }   
        }

        ArrayList<int[]> regularMove = removeCommonPositions(moveArray, attackArray);


        // check 
        for (int[] p : regularMove) {
            // draw the square with the appropriate color
            if ((p[0] + p[1]) % 2 == 0) {
                app.fill(199,225,239,255); // light blue - replace white
            } else {
                app.fill(174,209,224,255); // dark blue - replace brown
            }
            
            app.rect(p[0] * 48, p[1] * 48, 48, 48);
        }
        
        if (attackArray != null) {
            for (int[] attack : attackArray) {
                app.fill(254,161,105,255);
                app.rect(attack[0] * 48, attack[1] * 48, 48, 48);
            }
        }
        int x = playerPiece.getX() / 48;
        int y = playerPiece.getY() / 48;
        moveArray.add(new int[]{x, y});
        this.currentHighlightedTitles.addAll(moveArray);
    }

    /**
     * Checks if piece is in its original position, deafult. returns true if it is, otherwise returns false
     * @param type type of piece
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @param board 2D array of char representation of board
     * @return Pieces, object of developing piece
     */
    public static Pieces checkforDevelopment(String type, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray, char[][] board) {

        HashMap<String, Character> pieceMap = new HashMap<>();

        if (playerArray.get(0).getPlayerColour().equals("white")) {
        //if player is white 
        pieceMap.put("knight", 'N');
        pieceMap.put("camel", 'C');
        pieceMap.put("bishop", 'B');
        pieceMap.put("general", 'G');
        // pieceMap.put("rook", 'R');
        pieceMap.put("archbishop", 'H');
        pieceMap.put("queen", 'Q');
        pieceMap.put("amazon", 'A');
        }

        if (playerArray.get(0).getPlayerColour().equals("black")) {
        // if player is black
        pieceMap.put("knight", 'n');
        pieceMap.put("camel", 'c');
        pieceMap.put("bishop", 'b');
        pieceMap.put("general", 'g');
        // pieceMap.put("rook", 'R');
        pieceMap.put("archbishop", 'h');
        pieceMap.put("queen", 'q');
        pieceMap.put("amazon", 'a');
        }
        int row = 0;
        for (Pieces p : cpuArray) {

            if (p.getType().equals(type)) {

                if (board[ p.getY() / 48][p.getX() / 48] == pieceMap.get(type)){

                    // check if piece can move otherwise skip the development 
                    if  (!Board.moveToFreePosition(p, playerArray, cpuArray ).isEmpty()) {
                        return p;
                    }
                }
            }
            row++;
        }
        return null;

    }

    /**
     * returns unique possible movement positions for a piece
     * @param possibleMoves possible moves of piece
     * @param allAttackedPositions all attacked positions 
     * @return arraylist of unique possible movement positions for a piece
     */
    public static ArrayList<int[]> removeCommonPositions(ArrayList<int[]> possibleMoves, ArrayList<int[]> allAttackedPositions) {

        // avaliable positions for black piece movment
        ArrayList<int[]> result = new ArrayList<>();

        for (int[] move : possibleMoves) {
            boolean isAttacked = false;
            for (int[] attacked : allAttackedPositions) {
                if (Arrays.equals(move, attacked)) {
                    isAttacked = true;
                    break;
                }
            }
            if (!isAttacked) {
                result.add(move);
            }
        }
        return result;
    }

    /**
     * returns arraylist of positions which are possible minus positions that are being attacked by pieces of lower value
     * @param Piece piece object
     * @param playerArray contains all piece objects of player
     * @param cpuArray contains all piece objects of cpu
     * @return arraylist of positions which are possible minus positions that are being attacked
     */
    public static ArrayList<int[]> moveToFreePosition(Pieces Piece, ArrayList<Pieces> playerArray, ArrayList<Pieces> cpuArray ) {

        // All possible positions for black piece
        ArrayList<int[]> possibleMoves = Piece.getPossibleMoves(playerArray, cpuArray);

        // All attacked positions from pieces of lower value
        ArrayList<int[]> allAttackedPositions = Pieces.getAllAttackPositions(playerArray, cpuArray, Piece);

        // remove common elements from PossibleMoves and allAttackedPositions 
        ArrayList<int[]> allPossibleMoves = Board.removeCommonPositions(possibleMoves, allAttackedPositions);
     
        return allPossibleMoves;
    }

    /**
     * highlighting contributing checkmate pieces
     * @param app reference to the Processing application object
     * @param winnerPiecesArray list of winner piece objects
     * @param loserPiecesArray list of loser piece objects
     */
    public static void highlightContributionPieces(PApplet app, ArrayList<Pieces> winnerPiecesArray, ArrayList<Pieces> loserPiecesArray) {
        
        // loser king piece
        Pieces loserKing;

        // king positio
        int x = 0;
        int y = 0; 

        // contributing pieces arraylist
        ArrayList<Pieces> contributingPieces = new ArrayList<Pieces>();

        // get king position from loser
        for (Pieces p : loserPiecesArray) {
            if (p.getType().equals("king")) {
                loserKing = p;
                x = loserKing.getX() / 48;
                y = loserKing.getY() / 48;
            }
        }

        int[][] possibleKingMoves = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
        };

        for (int[] move : possibleKingMoves) {
            int newX = x + move[0];
            int newY = y + move[1];

            // Check if the new move is within the board limits
            if (newX >= 0 && newX < 14 && newY >= 0 && newY < 14) {
                int[] validMove = {newX, newY};

                // check winner pieces positions aginst possible king moves 
                for (Pieces w : winnerPiecesArray) {
                    ArrayList<int[]> attackPositions = w.getAttackingPositions(winnerPiecesArray, loserPiecesArray);
                    
                    // check positions 
                    for (int[] pos : attackPositions) {
                        
                        if (validMove[0] == pos[0] && validMove[1] == pos[1]) {
                            contributingPieces.add(w);
                            break;
                        }
                    }
                }
            }
        }

        for (Pieces con : contributingPieces) {
            app.fill(252,161,101,255);
            app.rect(con.getX(), con.getY(), 48, 48);
            con.draw(app);
        }
    }
}