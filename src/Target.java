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
 * circle of a specified color.
 */
public class Target extends GameObj implements Comparable<Target>{
	private int id;
	private int lives = 5;
	public static int SIZE = 50;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;

    private BufferedImage img;

    public Target(int id, int courtWidth, int courtHeight, int posX, int posY, String imgFile) {
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
    
    public Integer getId() {
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
	public int compareTo(Target t) {
		return t.getId() - getId();
	}
}