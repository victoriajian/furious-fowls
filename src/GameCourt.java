/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;


/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {
	
	// TreeMaps that store information about targets and birds
	private Map<Integer, Target> targets = new TreeMap<Integer, Target>();
	private Map<Integer, Bird> birds = new TreeMap<Integer, Bird>();
	private Map<Integer, Eliminated> elim = new TreeMap<Integer, Eliminated>();

    // game objects
    private Bird bird; // the bird, controlled by the mouse
    private YellowBird yelBird;
    private BlackBird blBird;
    private Pelt pelt;
    private Target target1; // the targets the birds are trying to hit
    private Target target2;
    private Target target3;
    private Eliminated t1Elim; 
    private Eliminated t2Elim; 
    private Eliminated t3Elim; 

    // states of game objects
    public boolean playing = false; // whether the game is running 
    private boolean birdLaunching = false; // whether the bird is moving
    private boolean yelBirdLaunching = false; // whether the yellow bird is moving
    private boolean blBirdLaunching = false; // whether the black bird is moving

    private JLabel status; // Current status text, i.e. "Running..."

    // Game constants
    public static final int COURT_WIDTH = 1000;
    public static final int COURT_HEIGHT = 600;
    public static final int SQUARE_VELOCITY = 4;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;
    
    //String that displays scores (game state)
    private String scores;
    private String gameEnd;
    
    // indicates whether there has been a saved game
    private boolean saved;
    
    BufferedImage img = null;

    public GameCourt(JLabel status) {

        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	tick();
                
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!
        

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);
        
        addMouseListener(new MouseAdapter() { 
        	Point mousePressedLoc = new Point(0,0);
        	boolean birdPressed = false;
        	boolean yelBirdPressed = false;
        	boolean blBirdPressed = false;
        	int newVx = 0;
        	int newVy = 0;
            public void mousePressed(MouseEvent me) { 
            	mousePressedLoc = me.getPoint();
            	//check if the mouse is pressing on the bird
            	if ((mousePressedLoc.x >= 50 && mousePressedLoc.x <= 110)
            			&& (mousePressedLoc.y >= 500 && mousePressedLoc.y <= 560)) {
            		birdPressed = true;
            		System.out.println("mouse pressed on bird");
            	} else if ((mousePressedLoc.x >= 130 && mousePressedLoc.x <= 190)
            			&& (mousePressedLoc.y >= 500 && mousePressedLoc.y <= 560)) {
            		yelBirdPressed = true;
            		System.out.println("mouse pressed on yellow bird");
            	} else if ((mousePressedLoc.x >= 210 && mousePressedLoc.x <= 270)
            			&& (mousePressedLoc.y >= 500 && mousePressedLoc.y <= 560)) {
            		blBirdPressed = true;
            		System.out.println("mouse pressed on black bird");
            	}
            	System.out.println("mouse pressed at " + me.getPoint()); 
            	System.out.println("bird at " + bird.getPx() + ", " + bird.getPy()); 
            }
            public void mouseReleased(MouseEvent me) {
            	newVx = (int) (0.3 * (mousePressedLoc.x - me.getX()));
            	newVy = (int) (0.3 * (mousePressedLoc.y - me.getY()));
            	if (birdPressed) {
            		bird.setVx(newVx);
                	bird.setVy(newVy);
                	birdPressed = false;
            	} else if (yelBirdPressed) {
            		yelBird.setVx(newVx);
            		yelBird.setVy(newVy);
            		yelBirdPressed = false;
            	} else if (blBirdPressed) {
            		blBird.setVx(newVx);
            		blBird.setVy(newVy);
            		blBirdPressed = false;
            	}
            	
            	if (bird.getVx() != 0 || bird.getVy() != 0) {
            		birdLaunching = true;
            	}
            	if (yelBird.getVx() != 0 || yelBird.getVy() != 0) {
            		yelBirdLaunching = true;
            	}
            	if (blBird.getVx() != 0 || blBird.getVy() != 0) {
            		blBirdLaunching = true;
            	}
            	
                System.out.println("mouse released at " + me.getPoint()); 
            }
            
          });
        
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
            	if (e.getKeyCode() == KeyEvent.VK_D) {
            		if (yelBirdLaunching) {
	                	System.out.println("should drop pelt");
	            		pelt = yelBird.dropPelt(COURT_WIDTH, COURT_HEIGHT);
            		} else if (blBirdLaunching) {
	                	System.out.println("black bird go down");
	            		blBird.dropDown();
            		}
                }
            }
        });
        

        this.status = status;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        try {
            if (img == null) {
                img = ImageIO.read(new File("files//background.png"));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    	
    	t1Elim = null;
    	t2Elim = null;
    	t3Elim = null;
    	pelt = null;
    	bird = new Bird(1, COURT_WIDTH, COURT_HEIGHT, 50, 500, "files/grey-bird.png");
    	yelBird = new YellowBird(2, COURT_WIDTH, COURT_HEIGHT, 130, 500, "files/yellow-bird.png");
    	blBird = new BlackBird(3, COURT_WIDTH, COURT_HEIGHT, 210, 500, "files/black-bird.png");
        target1 = new Target(1, COURT_WIDTH, COURT_HEIGHT, 600, 450, "files/weary-face.png");
        target2 = new Target(2, COURT_WIDTH, COURT_HEIGHT, 700, 400, "files/hot-face.png");
        target3 = new Target(3, COURT_WIDTH, COURT_HEIGHT, 800, 500, "files/flushed-face.png");
        
        birds.put(bird.getId(), bird);
        birds.put(yelBird.getId(), yelBird);
        birds.put(blBird.getId(), blBird);
        targets.put(target1.getId(), target1);
        targets.put(target2.getId(), target2);
        targets.put(target3.getId(), target3);
        
        elim.clear();
        
        for(Bird b : birds.values()) {
        	b.setVx(0);
        	b.setVy(0);
        }

        playing = true;
        
        scores = "Gray Bird: " + bird.getLives()
	    	+ "     Yellow Bird: " + yelBird.getLives()
			+ "     Black Bird: " + blBird.getLives()
			+ "     Target 1: " + target1.getLives()
			+ "     Target 2: " + target2.getLives()
			+ "     Target 3: " + target3.getLives();
        gameEnd = null;

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }
    
    

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
    	if (playing) {
    		// advance the square and snitch in their current direction.
        	
        	// as long as the bird is not on the ground, increment its vy every frame
        	decBirdVel();
        	
        	moveBirds();

            // check for bird and target collision
        	checkCollisions();
        	        
            // check that the turn is over (bird lands on ground)
            checkTurnOver();
            
            // check if targets have 0 hits left
            checkTargetEnd();
            
            // check if the user has won or lost
            if (checkGameEnd() == 1) {
            	status.setText("You Win!");
            	gameEnd = ("You Win!");
            	playing = false;
            } else if (checkGameEnd() == -1) {
            	status.setText("You Lose!");
            	gameEnd = ("You Lose!");
            	playing = false;
            }
            
            scores = "Gray Bird: " + bird.getLives()
        	    	+ "     Yellow Bird: " + yelBird.getLives()
        			+ "     Black Bird: " + blBird.getLives()
        			+ "     Target 1: " + target1.getLives()
        			+ "     Target 2: " + target2.getLives()
        			+ "     Target 3: " + target3.getLives();

            // update the display
            repaint();
    	}
    
    }
    
    /** 
     *  TICK HELPER METHODS
     */
    
    public void newTurnBird() {
    	// sets bird to its launching position
    	bird.setPx(50);
    	bird.setPy(500);
    	bird.setVx(0);
    	bird.setVy(0);
    	if (bird.getLives() > 0) {
    		bird.decLives(); // decreases bird lives by 1
    	}
    	System.out.println("bird lives left: " + bird.getLives());
    }
    
    public void newTurnYelBird() {
    	// sets bird to its launching position
    	yelBird.setPx(130);
    	yelBird.setPy(500);
    	yelBird.setVx(0);
    	yelBird.setVy(0);
    	if (yelBird.getLives() > 0) {
    		yelBird.decLives();
    	}
    	System.out.println("yellow bird lives left: " + yelBird.getLives());
    }
    
    public void newTurnBlBird() {
    	// sets bird to its launching position
    	blBird.setPx(210);
    	blBird.setPy(500);
    	blBird.setVx(0);
    	blBird.setVy(0);
    	if (blBird.getLives() > 0) {
    		blBird.decLives();
    	}
    	System.out.println("black bird lives left: " + blBird.getLives());
    }

	private void checkTargetEnd() {
		if (target1.getLives() == 0) {
        	t1Elim = new Eliminated (COURT_WIDTH, COURT_HEIGHT, target1.getPx(), target1.getPy(), 
        			"files/collision.png");
        	elim.put(1, t1Elim);
        }
        if (target2.getLives() == 0) {
        	t2Elim = new Eliminated (COURT_WIDTH, COURT_HEIGHT, 700, 400, 
        			"files/collision.png");
        	elim.put(2, t2Elim);
        }
        if (target3.getLives() == 0) {
        	t3Elim = new Eliminated (COURT_WIDTH, COURT_HEIGHT, target3.getPx(), target3.getPy(), 
        			"files/collision.png");
        	elim.put(3, t3Elim);
        }
	}

	// a turn is over if any of the birds have hit the ground. returns true if this has happened
	private boolean checkTurnOver() {
		if (bird.getPy() == 540) {
        	try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
			}
        	birdLaunching = false;
        	newTurnBird();
        	return true;
        } else if (yelBird.getPy() == 540) {
        	try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
			}
        	yelBirdLaunching = false;
        	newTurnYelBird();
        	return true;
        } else if (blBird.getPy() == 540) {
        	try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
			}
        	blBirdLaunching = false;
        	newTurnBlBird();
        	return true;
        }
		return false;
	}

	//check if either birds or pelt has hit a target
	private void checkCollisions() {
		for (int t : targets.keySet()) {
			for (int b : birds.keySet()) {
				if (targets.get(t).intersects(birds.get(b))
						&& targets.get(t).getLives() > 0) {
					decTargetLives(t);
				}
			}
		}
    	
    	if (pelt != null) {
    		for (int t : targets.keySet()) {
    			if (targets.get(t).intersects(pelt) && targets.get(t).getLives() > 0) {
    				decTargetLives(t);
    			}
    		}
    	}
	}
	
	public void decTargetLives(int targetID) {
		if (targets.get(targetID).getLives() > 0) {
			targets.get(targetID).decLives();
		}
	}

	private void moveBirds() {
		for (Bird b : birds.values()) {
			if (b.getLives() > 0) {
				b.move();
			}
		}
    	
    	if (pelt != null) {
    		pelt.move();
    	}
	}

	private void decBirdVel() {
		if (birdLaunching) {
    		bird.setVy((int) (bird.getVy() + 0.1 * INTERVAL));
    	} else if (yelBirdLaunching) {
    		yelBird.setVy((int) (yelBird.getVy() + 0.1 * INTERVAL));
    	} else if (blBirdLaunching) {
    		blBird.setVy((int) (blBird.getVy() + 0.1 * INTERVAL));
    	}
	}
	
    public int checkGameEnd() {
    	boolean aliveTarget = !(target1.getLives() == 0 && target2.getLives() == 0
    			&& target3.getLives() == 0);
    	boolean aliveBird = !(bird.getLives() == 0 && yelBird.getLives() == 0
    			&& blBird.getLives() == 0);
    	
    	if (!aliveBird && aliveTarget) { 
    		playing = false;
    		return -1;
    	} else if ((aliveBird && !aliveTarget) || (!aliveBird && !aliveTarget)) {
    		playing = false;
    		return 1;
    	} else {
    		return 0;
    	}
    }
    
    /** 
     *  FILE I/O
     */
    
    public void save() {
    	String output = "";
    	for (Bird b : birds.values()) {
    		output += b.getLives() + "\n";
    	}
    	for (Target t : targets.values()) {
    		output += t.getLives() + "\n";
    	}
    	if (pelt != null) {
    		output += pelt.getPx() + "\n";
    		output += pelt.getPy();
    	}
    	try {
			BufferedWriter bw = 
				new BufferedWriter(
						new FileWriter("saved.txt"));
			bw.write(output);
			bw.close();
		}catch(IOException e) {
			System.out.println("Can't Save");
		}
    	System.out.println(output);
    	saved = true;
    }
    
    public void getSaved() {
    	String filePath = "saved.txt";
		FileLineIterator it = new FileLineIterator(filePath);
		List<String> lines = new LinkedList<String>();
		while(it.hasNext()) {
			lines.add(it.next());
		}
		for (int i = 0; i < 3; i++) {
			birds.get(i+1).setLives(Integer.parseInt(lines.get(i)));
			System.out.print(lines.get(i) + " ");
			
		}
		for (int i = 3; i < 6; i++) {
			targets.get(i-2).setLives(Integer.parseInt(lines.get(i)));
			if (targets.get(i-2).getLives() > 0) {
				elim.replace(i-2, null);
			}
			System.out.print(lines.get(i) + " ");
		}
		if (lines.size() > 6) {
			pelt.setPx(Integer.parseInt(lines.get(6)));
			pelt.setPy(Integer.parseInt(lines.get(7)));
		} else {
			pelt = null;
		}
    }
    
    public boolean saved() {
    	return saved;
    }
    
    /** 
     *  GETTER METHODS FOR JUNIT TESTING
     */
    
    public String getScores() {
    	return scores;
    }
    
    //gets collection of bird IDs
    public Collection<Integer> getBirdIDs() {
    	List<Integer> ids = new LinkedList<Integer>();
    	ids.addAll(birds.keySet());
    	return ids;
    }
    
    //gets collection of target IDs
    public Collection<Integer> getTargetIDs() {
    	List<Integer> ids = new LinkedList<Integer>();
    	ids.addAll(targets.keySet());
    	return ids;
    }
    
    //gets collection of target elims
    public Collection<Integer> getElimIDs() {
    	List<Integer> ids = new LinkedList<Integer>();
    	ids.addAll(elim.keySet());
    	return ids;
    }
    
    public int getBirdLives(int birdID) {
    	return birds.get(birdID).getLives();
    }
    
    public int getTargetLives(int targetID) {
    	return targets.get(targetID).getLives();
    }
    
    public boolean getPlaying() {
    	return playing;
    }
    
    public boolean birdLaunching() {
    	return birdLaunching;
    }
    
    public boolean yelBirdLaunching() {
    	return yelBirdLaunching;
    }
    
    public boolean blBirdLaunching() {
    	return blBirdLaunching;
    }
    
    public boolean peltExists() {
    	return (pelt != null);
    }
    
    /** 
     *  PAINT/DRAW
     */
    
    public void drawText(Graphics g, String s, int x, int y, Color c, Font f) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(c);
        g2.setFont(f);
        g2.drawString(s, x, y);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); 
        
        g.drawImage(img, 0, 0, null);
        
        //display game state
        Font f1 = new Font("Helvetica", Font.PLAIN, 20);
        drawText(g, scores, 70, 30, Color.BLACK, f1);
        
        if (gameEnd != null) {
        	Font f2 = new Font("Helvetica", Font.BOLD, 30);
        	drawText(g, gameEnd, 400, 350, Color.WHITE, f2);
        }
        
        bird.draw(g);
        yelBird.draw(g);
        blBird.draw(g);
        target1.draw(g);
        target2.draw(g);
        target3.draw(g);
        
        if (pelt != null) {
        	pelt.draw(g);
        }
        
        for (int i : elim.keySet()) {
        	if (elim.get(i) != null) {
        		elim.get(i).draw(g);
        	}
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}