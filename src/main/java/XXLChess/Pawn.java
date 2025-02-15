package XXLChess;

import processing.core.PImage;

import java.util.*;


/**
 * This class represents an pawn piece in a chess game.
 * 
 * Pawn can move, One space forward. Captures 
 * diagonally only. If blocked, cannot 
 * move.
 */
public class Pawn extends Pieces {

   /**
     * x coordinate in pixels
     */
    protected int x;

    /**
     * y coordinate in pixels
     */
    protected int y;

    /**
     * colour of chess piece
     */
    protected char colour;

    /**
     * image of chess piece
     */
    protected PImage sprite;

    /**
     * player colour which has been set in config.json
     */
    protected String playerColour;

    /**
     *  constructor of pawn object - calls Pieces (parent class)
     * @param x piece x-coordinate
     * @param y piece y-coordinate
     * @param type piece type
     * @param colour piece colour
     * @param value piece value
     * @param sprite piece sprite
     * @param playerColour player colour
     */
    public Pawn(int x, int y, String type, char colour, double value, PImage sprite, String playerColour) {
        super(x, y, "pawn", colour, 1.00, sprite, playerColour);
    }
}
