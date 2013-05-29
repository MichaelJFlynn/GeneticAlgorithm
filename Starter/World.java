
import structure5.*;

/**
 * This module includes the functions necessary to keep track of the
 * creatures in a two-dimensional world. In order for the design to be
 * general, the interface adopts the following design: <p>
 * 1. The contents have generic type E.  <p>
 * 2. The dimensions of the world array are specified by the client. <p>
 * There are many ways to implement this structure.  HINT: 
 * look at the structure.Matrix class.  You should need to add no more than
 * about ten lines of code to this file.
 */

public class World<E> { 

    private int width;
    private int height;
    private Matrix<E> mat;

    /**
     * This function creates a new world consisting of width columns 
     * and height rows, each of which is numbered beginning at 0. 
     * A newly created world contains no objects. 
     */
    public World(int w, int h) {
        width = w;
        height = h;
	mat = new Matrix<E>(h, w);
    }

    /**
     * Returns the height of the world.
     */
    public int height() {
        return height;
    }

    /**
     * Returns the width of the world.
     */
    public int width() {
        return width;
    }

    /**
     * Returns whether pos is in the world or not.
     * @pre  pos is a non-null position.
     * @post returns true if pos is an (x,y) location in 
     *       the bounds of the board.
     */
    boolean inRange(Position pos) {
        int x = pos.getX();
	int y = pos.getY();
	boolean xin = (x>=0) && (x<width);
	boolean yin = (y>=0) && (y<height);
        return xin && yin;
    }

    /**
     * Set a position on the board to contain c.
     * @pre  pos is a non-null position on the board.
     */
    public void set(Position pos, E c) {
	int x = pos.getX();
	int y = pos.getY();
        Assert.pre(inRange(pos), "position is not in range: (" + x + ","+y +")");
	mat.set(x,y,c);
    }

    /**
     * Return the contents of a position on the board.
     * @pre  pos is a non-null position on the board.
     */
    public E get(Position pos) {
	int x = pos.getX();
	int y = pos.getY();
        Assert.pre(inRange(pos), "position is not in range: (" + x+ ","+y +")");
	return mat.get(x,y);
    }


    public static void main(String s[]) {
	
	World<String> tests = new World<String>(10, 10);
	//tests.set(new Position(11, 11), "hello");
	tests.set(new Position(1,1), "hello");
	tests.set(new Position(1,2), "hi");
	tests.set(new Position(1,3), "I am the World");
	System.out.println(tests.get(new Position(1,1)));
	System.out.println(tests.get(new Position(1,2)));
	System.out.println(tests.get(new Position(1,3)));
	System.out.println(tests.get(new Position(1,4)));
    }
	
}
