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
public class Eliminated extends GameObj implements Comparable<Eliminated>{
	public static int SIZE = 70;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;

    private BufferedImage img;

    public Eliminated(int courtWidth, int courtHeight, int posX, int posY, String imgFile) {
    	super(INIT_VEL_X, INIT_VEL_Y, posX, posY, SIZE, SIZE, courtWidth, courtHeight);
        
        try {
            if (img == null) {
                img = ImageIO.read(new File(imgFile));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    @Override
    public void draw(Graphics g) {
    	g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }
    
    @Override
    public int compareTo(Eliminated e) {
    	return 0;
    }
    
}