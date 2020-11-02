import static org.junit.Assert.fail;

import javax.swing.JLabel;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/** 
 *  You can use this file (and others) to test your
 *  implementation.
 */

public class GameTest {
	private GameCourt court;
	JLabel status = new JLabel("Game in play!");
	
	@Before
	public void setUp() {
        // We initialize a new GameCourt for each test
        court = new GameCourt(status);
        court.reset();
    }

    @Test
    public void testReset() {
    	court.reset();
        assertEquals(3, court.getBirdIDs().size());
        assertEquals(3, court.getTargetIDs().size());
        assertEquals(0, court.getElimIDs().size());
        assertTrue(court.getPlaying());
        assertFalse(court.peltExists());
        assertFalse(court.birdLaunching());
        assertFalse(court.yelBirdLaunching());
        assertFalse(court.blBirdLaunching());
    }
    
    @Test
    public void testResetAfter() {
    	//take down target 1
    	int temp = court.getTargetLives(1);
    	for (int i = 1; i <= temp; i++) {
    		court.decTargetLives(1);
    	}
    	court.tick();
    	court.reset();
    	
        assertEquals(3, court.getBirdIDs().size());
        assertEquals(3, court.getTargetIDs().size());
        assertEquals(0, court.getElimIDs().size());
        assertTrue(court.getPlaying());
        assertFalse(court.peltExists());
        assertFalse(court.birdLaunching());
        assertFalse(court.yelBirdLaunching());
        assertFalse(court.blBirdLaunching());
    }
    
    @Test
    public void testSaved() {
    	assertFalse(court.saved());
    	court.save();
    	assertTrue(court.saved());
    }
    
    @Test
    public void testElim() {
    	//take down target 1
    	int temp = court.getTargetLives(1);
    	for (int i = 1; i <= temp; i++) {
    		court.decTargetLives(1);
    	}
    	court.tick();
    	assertEquals(0, court.getTargetLives(1));
    	assertEquals(1, court.getElimIDs().size());
    }
    
    @Test
    public void testNewTurn() {
    	int tempBird = court.getBirdLives(1);
    	int tempYelBird = court.getBirdLives(2);
    	int tempBlBird = court.getBirdLives(3);
    	court.newTurnBird();
    	court.newTurnYelBird();
    	court.newTurnBlBird();
    	assertEquals(tempBird - 1, court.getBirdLives(1));
    	assertEquals(tempYelBird - 1, court.getBirdLives(2));
    	assertEquals(tempBlBird - 1, court.getBirdLives(3));
    }
    
    @Test
    public void testDecTargetLivesOnce() {
    	int temp1 = court.getTargetLives(1);
    	int temp2 = court.getTargetLives(2);
    	int temp3 = court.getTargetLives(3);
    	court.decTargetLives(1);
    	court.decTargetLives(2);
    	court.decTargetLives(3);
    	assertEquals(temp1 - 1, court.getTargetLives(1));
    	assertEquals(temp2 - 1, court.getTargetLives(2));
    	assertEquals(temp3 - 1, court.getTargetLives(3));
    }
    
	//dec target lives more times than num lives
    @Test
    public void testDecTargetLivesOver() {
    	int temp = court.getTargetLives(1);
    	for (int i = 0; i <= temp; i++) {
    		court.decTargetLives(1);
    	}
    	assertEquals(0, court.getTargetLives(1));
    }
    
    @Test
    public void testGameEndFalse() {
    	//initially
    	assertEquals(court.checkGameEnd(), 0);
    	
    	//after one collision of each
    	court.newTurnBird();
    	court.newTurnYelBird();
    	court.newTurnBlBird();
    	court.decTargetLives(1);
    	court.decTargetLives(2);
    	court.decTargetLives(3);
    	assertEquals(court.checkGameEnd(), 0);
    }
    
    @Test
    public void testGameEndLose() {
    	//each target is hit once (targets alive)
    	for (int i = 1; i <= 3; i++) {
    		court.decTargetLives(i);
    	}
    	
    	int temp1 = court.getBirdLives(1);
    	int temp2 = court.getBirdLives(2);
    	int temp3 = court.getBirdLives(3);
    	
    	//each bird has used up all lives (no birds alive)
    	for (int i = 0; i < temp1; i++) {
    		court.newTurnBird();
    		System.out.println(court.getBirdLives(1));
    	}
    	for (int i = 0; i < temp2; i++) {
    		court.newTurnYelBird();
    	}
    	for (int i = 0; i < temp3; i++) {
    		court.newTurnBlBird();
    	}
    	
    	assertEquals(court.checkGameEnd(), -1);
    }
    
    @Test
    public void testGameEndWin1() {
    	//each target has used up all lives (no targets alive)
    	for (int t : court.getTargetIDs()) {
    		int temp = court.getTargetLives(t);
    		for (int i = 0; i < temp; i++) {
        		court.decTargetLives(t);
        	}
    	}
    	
    	int temp1 = court.getBirdLives(1);
    	int temp2 = court.getBirdLives(2);
    	int temp3 = court.getBirdLives(3);
    	
    	//each bird has used up all lives (no birds alive)
    	for (int i = 0; i < temp1; i++) {
    		court.newTurnBird();
    	}
    	for (int i = 0; i < temp2; i++) {
    		court.newTurnYelBird();
    	}
    	for (int i = 0; i < temp3; i++) {
    		court.newTurnBlBird();
    	}
    	
    	assertEquals(court.checkGameEnd(), 1);
    }
    
    @Test
    public void testGameEndWin2() {
    	//each target has used up all lives (no targets alive)
    	for (int t : court.getTargetIDs()) {
    		int temp = court.getTargetLives(t);
    		for (int i = 1; i <= temp; i++) {
        		court.decTargetLives(t);
        	}
    	}
    	
    	int temp1 = court.getBirdLives(1);
    	int temp2 = court.getBirdLives(2);
    	
    	//two birds have used up all lives (1 bird alive)
    	for (int i = 0; i < temp1; i++) {
    		court.newTurnBird();
    		System.out.println(court.getBirdLives(1));
    	}
    	for (int i = 0; i < temp2; i++) {
    		court.newTurnYelBird();
    	}
    	    	
    	assertEquals(court.checkGameEnd(), 1);
    }
    

}
