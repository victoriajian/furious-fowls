/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("Furious Fowls");
        frame.setLocation(250, 0);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Game in play!");
        status_panel.add(status);

        // Main playing area
        final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we define it as an
        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("New Game");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        control_panel.add(reset);
        
        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
        
        //File I/O buttons
        final JButton save = new JButton("Save Game");
	    final JButton getSaved = new JButton("Get Last Saved Game");
	    
	    
	    save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (court.getPlaying()) {
            		court.save();
                    status.setText("Last saved game:      " + court.getScores());
            	}
            }
        });
        control_panel.add(save);
        
        getSaved.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (court.saved()) {
            		court.getSaved();
            		
            	} else {
            		status.setText("No saved Game!");
            	}
                
            }
        });
        control_panel.add(getSaved);
        
        //Instructions
        final JFrame inst = new JFrame("Instructions");
         
        JPanel panel = (JPanel)inst.getContentPane();
 
        JLabel label = new JLabel();
        ImageIcon imageIcon = new ImageIcon("files//instructions.png");
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(500, 350,  java.awt.Image.SCALE_SMOOTH);  
        imageIcon = new ImageIcon(newimg);  // transform it back
        
        label.setIcon(imageIcon);
        panel.add(label);
        inst.setPreferredSize(new Dimension(500, 400)); 
        inst.setLocation(500,  200);
        inst.pack();
        inst.setVisible(true);
        
        
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}