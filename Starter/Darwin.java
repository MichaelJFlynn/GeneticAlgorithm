
import structure5.*;
import java.util.Random;
import java.util.Scanner;
import java.io.*;

/**
 * This class controls the simulation.  The design is entirely up to
 * you.  You should include a main method that takes the array of
 * species file names passed in and populates a world with species of
 * each type.  
 * <p>
 * Be sure to call the WorldMap.pause() method every time
 * through the main simulation loop or else the simulation will be too fast. 
 * For example:
 * <pre>
 *   public void simulate() {
 *       for (int rounds = 0; rounds < numRounds; rounds++) {
 *         giveEachCreatureOneTurn();
 *         pause(100);
 *       }
 *   }
 * </pre>
 */
class Darwin {

    private static int sidelength = 15;
    private Vector<Species> species;
    private World<Creature> world;
    private Vector<Creature> creatures;
    private int numRounds;
    private boolean graphics;

    public Darwin(String[] speciesNames, int numCreatures, int numR, boolean graphs) {
	WorldMap.createWorldMap(sidelength, sidelength, graphs);
	Random random = new Random();
	graphics = graphs;
	world = new World<Creature>(sidelength, sidelength);
	numRounds = numR;
	species = new Vector<Species>();
	creatures = new Vector<Creature>();
	System.out.println("Creating creatures...");
	for(int i=0; i<speciesNames.length; i++) { // for each species, intiailize some creatures randomly
	    Species thisSpecies = new Species(speciesNames[i]);
	    species.add(thisSpecies);
	    for(int j=0; j< numCreatures;) {
		int x = random.nextInt(sidelength);
		int y = random.nextInt(sidelength);
		Position place = new Position(x,y);
		if(world.get(place) == null) {
		    Creature kreature = new Creature(thisSpecies, world, place, random.nextInt(4));
		    creatures.add(kreature);
		    world.set(place, kreature);
		    j++;
		}
	    }
	}
    }

    // I know these are almost redundant, but I don't think it's
    // necessary to shrink them down to one method
    public Darwin(Vector<Species> speciesNames, int numCreatures, int numR, boolean graphs) {
	WorldMap.createWorldMap(sidelength, sidelength, graphs);
	Random random = new Random();
	graphics = graphs;
	world = new World<Creature>(sidelength, sidelength);
	numRounds = numR;
	species = new Vector<Species>();
	creatures = new Vector<Creature>();
	//System.out.println("Creating creatures...");
	for(int i=0; i<speciesNames.size(); i++) { // for each species, intiailize some creatures randomly
	    Species thisSpecies = speciesNames.get(i);
	    species.add(thisSpecies);
	    for(int j=0; j< numCreatures;) {
		int x = random.nextInt(sidelength);
		int y = random.nextInt(sidelength);
		Position place = new Position(x,y);
		if(world.get(place) == null) {
		    Creature kreature = new Creature(thisSpecies, world, place, random.nextInt(4));
		    creatures.add(kreature);
		    world.set(place, kreature);
		    j++;
		}
	    }
	}
    }
    
    public void setGraphics(boolean b) {
	graphics = b;
    }
    /**
     * The array passed into main will include the arguments that
     * appeared on the command line.  For example, running "java
     * Darwin Hop.txt Rover.txt" will call the main method with s
     * being an array of two strings: "Hop.txt" and "Rover.txt".
     */
    public static void main(String s[]) {
	Scanner in = new Scanner(System.in);
	System.out.print("Number of Creatures: ");
	int numCreats = in.nextInt();
	System.out.print("Number of Rounds: ");
	int numRound = in.nextInt();
	boolean graph = false;
	boolean answered = false;
	do{
	    System.out.print("Graphics? (y/n): ");
	    String answer = in.next();
	    if(answer.equals("y")) {
		graph = true;
		answered = true;
	    } else if(answer.equals("n")) {
		graph = false;
		answered = true;
	    } else {
		System.out.println("Please answer \'y\' or \'n\'");
	    }
	} while(!answered);
	in.close();
	Darwin charles = new Darwin(s,numCreats,numRound, graph);
	System.out.println("Simulating...");
	charles.simulate(true);
    }


    public Game simulate(boolean verbose) {
	
	for (int rounds = 0; rounds < numRounds; rounds++) {
	    for(int i=0; i<creatures.size(); i++) {
		//System.out.println("Taking a turn:");
		creatures.get(i).takeOneTurn();
	    }
	    if(graphics){
		WorldMap.pause(100);
	    }
        }
	Vector<Association<Species, Integer>> scores = new Vector<Association<Species, Integer>>();
	// calculate scores and such
	Integer max = 0;
	Species winner = null;
	Vector<Species> included = new Vector<Species>();
	for(int i=0; i<creatures.size(); i++) {
	    Species sp = creatures.get(i).species();
	    if(!included.contains(sp)){
		included.add(sp);
	    }
	    boolean contains = false;
	    for(int j=0; j<scores.size(); j++) {
		if(scores.get(j).getKey().getName().equals(sp.getName())) {
		    contains = true;
		    Integer newValue = scores.get(j).getValue() + 1;
		    scores.get(j).setValue(newValue);
		    if(max < newValue) {
			max = newValue;
			winner = scores.get(j).getKey();
		    } 
		}
	    }
	    if(!contains) {
		scores.add(new Association<Species, Integer>( sp, 1));
	    }
	}
	// account for the Species that scored 0
	for(int i=0; i<species.size(); i++) {
	    Species spec = species.get(i);
	    if(!included.contains(spec)){
		scores.add(new Association<Species, Integer>(spec, 0));
	    }
	}
	Game game = new Game(winner, max, scores);
	if(verbose) {
	    game.printGame();
	}
	
	return game;
    }
}
