import structure5.*;


class Evolution {


    public static void main(String args[]) {
	for(String s : args) {
	    System.out.println(s);
	}
	System.out.println("done");
    }
    

    public Evolution() {


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