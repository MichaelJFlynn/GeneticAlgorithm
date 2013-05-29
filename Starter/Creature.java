
import structure5.*;
import java.util.*;

/**
 * This class represents one creature on the board.
 * Each creature must remember its species, position, direction,
 * and the world in which it is living.
 * <p>
 * In addition, the Creature must remember the next instruction
 * out of its program to execute.
 * <p>
 * The creature is also repsonsible for making itself appear in
 * the WorldMap.  In fact, you should only update the WorldMap from
 * inside the Creature class.  
 */

public class Creature {

    private Species sp;
    private World<Creature> w;
    private Position p;
    private int direction;
    private int instructionCounter;
    private Random random;
    /**
     * Create a creature of the given species, with the indicated
     * position and direction.  Note that we also pass in the 
     * world-- remember this world, so that you can check what
     * is in front of the creature and to update the board
     * when the creature moves.
     */
    public Creature(Species species,
		    World<Creature> world,
		    Position pos,
		    int dir) {
	random = new Random();
	sp = species;
	w = world;
	p = pos;
	direction = dir;
	instructionCounter = 1;
	WorldMap.displaySquare(p, species.getName().charAt(0), direction, species.getColor());
    }

    /**
     * Return the species of the creature.
     */
    public Species species() {
	return sp;
    }

    /**
     * Return the current direction of the creature.
     */
    public int direction() {
	return direction;
    }

    public void infect(Species spec, int pC) {
	sp = spec;
	instructionCounter = pC;
	WorldMap.displaySquare(p, sp.getName().charAt(0), direction, sp.getColor());
    }

    /**
     * Return the position of the creature.
     */
    public Position position() {
	return p;
    }
    
    /**
     * Execute steps from the Creature's program until 
     * a hop, left, right, or infect instruction is executed.
     * So end on 1, 2, 3, or 4.
     */ 
    public void takeOneTurn() {
	
	while(true) {
	    Instruction inst = sp.programStep(instructionCounter);
	    int opCode = inst.getOpcode();
	    boolean done = false;
	    Position oldp = p;
	    Position newp = p.getAdjacent(direction);
	    switch(opCode) {
	    case 1:  // hop
		if(w.inRange(newp) && w.get(newp) == null) {
		    p = newp;
		    w.set(oldp, null);
		    w.set(p, this);
		}
		done = true;
		break;
	    case 2: // left
		direction = leftFrom(direction); 
		done = true;
		break;
	    case 3: // right
		direction = rightFrom(direction);
		done = true;
		break;
	    case 4: // infect
		if(w.inRange(newp) && w.get(newp) != null && !w.get(newp).species().equals(sp.getName())) {
		    w.get(newp).infect(sp, inst.getAddress());
		}
		done = true;
		break;
	    case 5: //ifempty
		if(w.inRange(newp) && w.get(newp) == null) {
		    instructionCounter = inst.getAddress()-1;
		}
		break;
	    case 6: // ifwall
		if(!w.inRange(newp)){
		    instructionCounter = inst.getAddress()-1;
		}
		break;
	    case 7: // ifsame
		if(w.inRange(newp) && w.get(newp) != null && w.get(newp).species().getName().equals(sp.getName())) {
		    instructionCounter = inst.getAddress()-1;
		}
		break;
	    case 8: //ifenemy
		if(w.inRange(newp) && w.get(newp) != null && !w.get(newp).species().getName().equals(sp.getName())) {
		    instructionCounter = inst.getAddress()-1;
		}
		break;
	    case 9: // IFRANDOM
		if(random.nextBoolean()) {
		    instructionCounter = inst.getAddress()-1;
		}
	    case 10: // go
		instructionCounter = inst.getAddress()-1; // minus 1 because it is about to be incremented (quick fix)
		break;
	    }
	    instructionCounter++;
	    if(done) {
		//System.out.println(oldp + " " + p);
		WorldMap.displaySquare(oldp, ' ', 0, "black");
		WorldMap.displaySquare(p, sp.getName().charAt(0), direction, sp.getColor()); 
		return;
	    }
	}
    }
    
    /**
     * Return the compass direction the is 90 degrees left of
     * the one passed in.
     */
    public static int leftFrom(int direction) {
	Assert.pre(0 <= direction && direction < 4, "Bad direction");
	return (4 + direction - 1) % 4;
    }

    /**
     * Return the compass direction the is 90 degrees right of
     * the one passed in.
     */
    public static int rightFrom(int direction) {
	Assert.pre(0 <= direction && direction < 4, "Bad direction");
	return (direction + 1) % 4;
    }

    /**
     */
    public static void main(String st[]) {

	// test creature code here.
	Creature Kreature = new Creature(new Species("Flytrap.txt"), 
					 new World<Creature>(10, 10), 
					 new Position(1,1), 
					 2);
	
    }


}
