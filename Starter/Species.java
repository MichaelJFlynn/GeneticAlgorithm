
import structure5.*;
import java.io.*;
import java.util.Scanner;

/**
 * The individual creatures in the world are all representatives of
 * some species class and share certain common characteristics, such
 * as the species name and the program they execute.  Rather than copy
 * this information into each creature, this data can be recorded once
 * as part of the description for a species and then each creature can
 * simply include the appropriate species pointer as part of its
 * internal data structure.
 * <p>
 * 
 * To encapsulate all of the operations operating on a species within
 * this abstraction, this provides a constructor that will read a file
 * containing the name of the creature and its program, as described
 * in the earlier part of this assignment.  To make the folder
 * structure more manageable, the species files for each creature are
 * stored in a subfolder named Creatures.  This, creating the Species
 * for the file Hop.txt will causes the program to read in
 * "Creatures/Hop.txt".
 * 
 * <p>
 *
 * Note: The instruction addresses start at one, not zero.
 */
public class Species {

    private String name;
    private String color;
    private int size;
    private Vector<Instruction> insts;
    /**
     * Create a species for the given file.
     * @pre fileName exists in the Creature subdirectory.
     */

    // need this constructor to speed up "Evolution.java" because I
    // assume have to read and write files will take an unnecessary
    // amount of time
    public Species(String n, String c, Vector<Instruction> instructions) {
	name = n;
	color = c;
	insts = instructions;
    }

    public Species(String fileName)  {
	Scanner input = 
	    new Scanner(new FileStream("Creatures" + 
				       java.io.File.separator + 
				       fileName));
	name = input.nextLine();
	color = input.nextLine();
	insts = new Vector<Instruction>();
	String currentLine = input.nextLine();
	while(!currentLine.equals(".")) {
	    Scanner splitter = new Scanner(currentLine);
	    Vector<String> tokens = new Vector<String>();
	    while(splitter.hasNext()) {
		tokens.add(splitter.next());
	    }
	    Assert.pre( tokens.size() > 0 && tokens.size() < 3, "Invalid syntax for instruction: " + currentLine);
	    String opcodeString = tokens.get(0);
	    int address = 1;
	    if(tokens.size() == 2) {
		address = Integer.valueOf(tokens.get(1));
	    } else {
		address = 1;
	    }
	    int opcode = -1;
	    if(opcodeString.equals("hop")) opcode = 1;
	    else if(opcodeString.equals("left")) opcode = 2;
	    else if(opcodeString.equals("right")) opcode = 3;
	    else if(opcodeString.equals("infect")) opcode = 4;
	    else if(opcodeString.equals("ifempty")) opcode = 5;
	    else if(opcodeString.equals("ifwall")) opcode = 6;
	    else if(opcodeString.equals("ifsame")) opcode = 7;
	    else if(opcodeString.equals("ifenemy")) opcode = 8;
	    else if(opcodeString.equals("ifrandom")) opcode = 9;
	    else if(opcodeString.equals("go")) opcode = 10;
	    else {
		Assert.fail("Using invalid instruction: " + opcodeString);
	    }
	    Instruction nextInst = new Instruction(opcode, address);
	    insts.add(nextInst);
	    //System.out.println("added instruction: " + currentLine);
	    currentLine = input.nextLine();
	}
    }


    /**
     * Return the name of the species.
     */
    public String getName() {
	return name;
    }

    /**
     * Return the color of the species.
     */
    public String getColor() {
	return color;
    }

    /**
     * Return the number of instructions in the program.
     */
    public int programSize() {
       	return insts.size();
    }

    /**
     * Return an instruction from the program.
     * @pre  1 <= i <= programSize().
     * @post returns instruction i of the program.
     */
    public Instruction programStep(int i) {
	Assert.pre( 1 <= i && i <= programSize(), "Invalid Program Counter");
       	return insts.get(i-1);
    }

    /**
     * Return a String representation of the program.
     */
    public String programToString() {
	String s = "";
	s = s + name + "\n" + color + "\n";
	for (int i = 1; i <= programSize(); i++) {
	    s = s + programStep(i) + "\n";
	}
	s = s + ".";
	return s;
    }

    
    public static void main(String s[]) {
	Species sp = new Species("Food.txt");
	//System.out.println(sp.getName());
	//System.out.println("first step should be hop: " + sp.programStep(1)); 

	/* test all other operations here */
	System.out.print("Name: ");
	System.out.println(sp.getName());
	System.out.print("Color: ");
	System.out.println(sp.getColor());
	System.out.println("Instructions: \n");
	System.out.println(sp.programToString());
    }

}
   
