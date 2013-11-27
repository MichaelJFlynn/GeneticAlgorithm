import structure5.*;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class Evolution {

    private int matchesPerIndividual = 50;
    private int generations = 100;
    private double mutationRate = .01;
    private Random randy;
    private Tree[] population;
    private String[] letters = {"h", "l", "r"};
    private String name = "Genie";
    private String color = "blue";
    private Vector<Species> enemies;

    public static void main(String args[]) {
	Vector<Species> enemies = new Vector<Species>();
	for(String s : args) {
	    enemies.add(new Species(s));
	}
	Evolution evo = new Evolution(enemies);
	evo.run();
    }
    

    public Evolution(Vector<Species> enemies) {
	population = new Tree[100];
	randy = new Random();
	this.enemies = enemies;
    }

    public void run() {
	// intialize population
	System.out.println("Initializing population...");
	for(int i=0; i<100; i++) {
	    population[i] = randomTree(4);
	}
	
	Tree bestTree = new Tree("l");
	double best_ever = 0;
	System.out.println("Running algorithm...");
	int generation = 1;
	while(best_ever < matchesPerIndividual) {
	    // calculate fitness for each
	    double[] fitnesses = new double[100];
	    for(int i=0; i<100; i++) {
		System.out.print(i);
		fitnesses[i] = calculateFitness(population[i]);
		if(i <= 99) {
		    if(i <= 9) {
			System.out.print("\b");
		    } else {
			System.out.print("\b\b");
		    }
		} else {
		    System.out.print("\b\b\b");
		}
	    }

	    // average the fitnesses and print them out for debugging
	    double sum = 0;
	    double best = 0;
	    for(int k=0; k<100; k++) {
		sum += fitnesses[k];
		if( fitnesses[k] > best_ever ) {
		    best_ever = fitnesses[k];
		    bestTree = population[k];
		}
		if (fitnesses[k] > best) best = fitnesses[k];
	    
	    }
	    sum = sum/100;
	    System.out.println("[GENERATION " + generation + "] Average fitness: " + sum + ", Best Fitness: " + best);
	    generation++;
	
	    // save the top 50 for the next generation
	    // lazy implementation...
	    Tree[] nextGen = new Tree[100];
	    int index = 0;
	    int rank;
	    for(int i=0; i<100; i++) {
		rank = 0;
		// for each member of the population
		for(int j=0; j<100; j++) {
		    // count the number of members that member i is greater than
		    if(fitnesses[i] >= fitnesses[j]) {
			rank++;
		    }
		}
		if(rank > 50) {
		    nextGen[index] = population[i];
		    index++;
		}
		if(index >= 50) break;
	    }
	    
	    // mutate for the 50 "offspring" and run the algorithm again
	    for(int i=50; i < 100; i++) {
		Tree childTree = nextGen[i-50].copy();
		mutate(childTree);
		nextGen[i] = childTree;
	    }
	population = nextGen;
	}
	try {
	    Species bestSpec = new Species(name, color, decodeTree(bestTree));
	    String machineCode = bestSpec.programToString();
	    String filename = ".\\Creatures\\" + name + generation + ".txt";
	    File file = new File(filename);
	    FileWriter fw = new FileWriter(file.getAbsoluteFile());
	    BufferedWriter bw = new BufferedWriter(fw);
	    bw.write(machineCode);
	    bw.close();
	    System.out.println("Wrote to " +".\\Creatures\\" + name + generation + ".txt");
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
	
    public void mutate(Tree t) {
	if(randy.nextDouble() < mutationRate) {
	    t.setValue(letters[randy.nextInt(3)]);
	}
	for(Tree child : t.getChildren()) {
	    mutate(child);
	}
    }



    public int calculateFitness(Tree tree) {
	int ret = 0;
	Species spec = new Species(name, color, decodeTree(tree));
	Vector<Species> battlers = new Vector<Species>();
	for(Species s: enemies) battlers.add(s);
	battlers.add(spec);
	for(int i = 0; i < matchesPerIndividual; i++) {
	    Darwin charles = new Darwin(battlers, 10, 200, false);
	    Game game = charles.simulate(false);
       	    if(game.getWinner().equals(spec)) {
		ret += 1;
	    }
	}
	return ret;
    }
    
    
    
    // post: returns a random tree with <depth> layers
    public Tree randomTree(int depth) {
	Random rand = new Random();
	Tree randTree = new Tree(letters[rand.nextInt(3)]);
	if(depth > 0) {
	    for(int i=0; i<5; i++) {
		randTree.set(i, randomTree(depth-1));
	    }
	}
	return randTree;
    }

    // pre: 's' is one of: 'h', 'l' or 'r'
    // post: returns Instruction corresponding to String
    public Instruction decodeString(String s) {
	Assert.pre(s.equals("h") || s.equals("l") || s.equals("r"), "Invalid String to decode");
	if(s.equals("h")) {
	    return new Instruction(1, 0);
	}
	if(s.equals("l")) {
	    return new Instruction(2, 0);
	}
	if(s.equals("r")) {
	    return new Instruction(3,0);
	}
	// we shouldn't get here
	Assert.fail("Somehow got to the end of \'decodeString\'");
	return null;
    }
    
    
    // pre: takes a decision tree of our format 
    // post: returns the decoded Darwin code instructions in a vector,
    // which is readable by the Species constructor
    public Vector<Instruction> decodeTree(Tree gene) {
	// initialize what we will be returning:
	Vector<Instruction> ret = new Vector<Instruction>();
	// collect together the 2 layers, loop because we will do this
	// all the way down
	Vector<Tree> currentLayer = new Vector<Tree>();
	currentLayer.add(gene); // start at the root
	int instructionPointer = 1;
	while(currentLayer.size() > 0) {
	    Vector<Tree> nextLayer = new Vector<Tree>();
	    for(int i=0; i<currentLayer.size(); i++) {
		for(Tree t : currentLayer.get(i).getChildren()) {
		    nextLayer.add(t);
		}
	    }

	    // rest of code here
	    int nodesExplored = 0; // how many nextLayer nodes have
	                           // been accounted for
	    for(int i=0; i<currentLayer.size(); i++) {
		Tree node = currentLayer.get(i);
		
		// add instruction
		ret.add(decodeString(node.getValue()));
		instructionPointer++;
		
		// calculate the amount of nodes in the current layer
		// not reached yet
		int nodesToGo = currentLayer.size() - (i + 1);
		
		// ifwall
		if(node.get(0) != null) { // make sure there is a node
		    int pointer = instructionPointer + 6 +
			7*( nodesToGo + nodesExplored);
		    ret.add(new Instruction(6, pointer));
		    nodesExplored++;
		} else {
		    ret.add(new Instruction(6, 1));
		}
		instructionPointer++;

		// ifsame
		if(node.get(1) != null) {
		    int pointer = instructionPointer + 5 + 
			7*( nodesToGo + nodesExplored);
		    ret.add(new Instruction(7, pointer));
		    nodesExplored++;
		} else {
		    ret.add(new Instruction(7,1));
		}
		instructionPointer++;
		
		// ifenemy, we want to skip the next instruction,
		// infect, and then jump to our post-enemy
		// instructions
		ret.add(new Instruction(8, instructionPointer + 2));
		instructionPointer++;
		
		// default
		if(node.get(2) != null) {
		    int pointer = instructionPointer + 3 +
			7*( nodesToGo + nodesExplored);
		    ret.add(new Instruction(10, pointer));
		    nodesExplored++;
		} else {
		    ret.add(new Instruction(10, 1));
		}
		instructionPointer++;
		
		// infect
		if(node.get(3) != null) {
		    int pointer = instructionPointer + 2 + 
			7*(nodesToGo + nodesExplored);
		    ret.add(new Instruction(4, pointer));
		    nodesExplored++;
		} else {
		    ret.add(new Instruction(4, 1));
		}
		instructionPointer++;
		
		// ifenemy (this is really a go, but only reachable if
		// an enemy was seen)
		if(node.get(4) != null) {
		    int pointer = instructionPointer + 1 + 
			7*(nodesToGo + nodesExplored);
		    ret.add(new Instruction(10, pointer));
		    nodesExplored++;
		} else {
		    ret.add(new Instruction(10, 1));
		}
		instructionPointer++;
	    }
	    
	    currentLayer = nextLayer;
	}
	return ret;
    }
    
}