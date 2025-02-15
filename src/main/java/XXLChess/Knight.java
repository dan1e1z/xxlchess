package XXLChess;

import processing.core.PImage;

import java.util.*;


/**
 * This class represents an knight piece in a chess game.
 * 
 * knight can move, 2 squares vertical, 1 horizontal, or 
* vice versa
 */
public class Knight extends Pieces {

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
     *  constructor of knight object - calls Pieces (parent class)
     * @param x piece x-coordinate
     * @param y piece y-coordinate
     * @param type piece type
     * @param colour piece colour
     * @param value piece value
     * @param sprite piece sprite
     * @param playerColour player colour
     */
    public Knight(int x, int y, String type, char colour, double value, PImage sprite, String playerColour) {
        super(x, y, "knight", colour, 2, sprite, playerColour);
    } 
}