package XXLChess;

import processing.core.PImage;

import java.util.*;

/**
 * This class represents an Amazon piece in a chess game.
 * 
 * Amazons can move like a Knight + Bishop + Rook
 */
public class Amazon extends Pieces {

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
     *  constructor of amazon object - calls Pieces (parent class)
     * @param x piece x-coordinate
     * @param y piece y-coordinate
     * @param type piece type
     * @param colour piece colour
     * @param value piece value
     * @param sprite piece sprite
     * @param playerColour player colour
     */
    public Amazon(int x, int y, String type, char colour, double value, PImage sprite, String playerColour) {
        super(x, y, "amazon", colour, 12, sprite, playerColour);
    }    
}