/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A basic game object starting in the upper left corner of the game court. It is displayed as a
 * square of a specified color.
 */
public class Bird extends GameObj implements Comparable<Bird>{
	private int id;
	private int lives = 5;
    public static final int SIZE = 60;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;

    private BufferedImage img;

    /**
    * Note that, because we don't need to do anything special when constructing a Square, we simply
    * use the superclass constructor called with the correct parameters.
    */
    public Bird(int id, int courtWidth, int courtHeight, int posX, int posY, String imgFile) {
        super(INIT_VEL_X, INIT_VEL_Y, posX, posY, SIZE, SIZE, courtWidth, courtHeight);
        
        this.id = id;

        try {
            if (img == null) {
                img = ImageIO.read(new File(imgFile));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
    
    public int getId() {
    	return id;
    }
    
    public void setLives(int l) {
    	lives = l;
    }
    
    public int getLives() {
    	return lives;
    }
    
    public void decLives() {
    	lives--;
    }

    @Override
    public void draw(Graphics g) {
    	g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }
    
    @Override
	public int compareTo(Bird b) {
		return 0;
	}
}