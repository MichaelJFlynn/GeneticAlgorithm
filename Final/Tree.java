import structure5.*;
/*
Structure of Tree:
Root value - String: "h", "l" or "r"
Children (at index):
0 ifwall
1 ifsame
2 default
3 infect (me)
4 infect (them)
 */
public class Tree {

    private String value;
    private Tree[] children;

    // default class for Darwin genetic algorithms
    public Tree(String init) {
	value = init;
	children = new Tree[5];
    }

    public Tree(String init, int branches) {
	value = init;
	children = new Tree[branches];
    }

    // post: returns a copy of the Tree
    public Tree copy() {
	Tree t = new Tree(value);
	for(int i=0; i<5; i++) {
	    if( this.get(i) != null ) {
		t.set(i, this.get(i).copy());
	    }
	}
	return t;
    }

    // post: returns a vector of the non-null children
    public Vector<Tree> getChildren() {
	Vector<Tree> ret = new Vector<Tree>(5);
	for(int i=0; i < 5; i++) {
	    if(children[i] != null) {
		ret.add(children[i]);
	    }
	}
	return ret;
    }

    // pre: valid index and Tree
    // post: replaces whatever was at index with sub, returning old
    // Tree
    public Tree set(int index, Tree sub) {
	Assert.pre(0 <= index && index < 5, "Bad index");
	Tree ret = children[index];
	children[index] = sub;
	return ret;
    }

    // pre: valid index
    // post: returns Tree at index
    public Tree get(int index) {
	Assert.pre(0 <= index && index < 5, "Bad index");
	return children[index];
    }

    // post: returns value
    public String getValue() {
	return value;
    }

    // post: replaces value with s
    public void setValue(String s) {
	value = s;
    }

    // post: returns string corresponding to values listed in some order
    public String code() {
	Vector<Tree> vec = getChildren();
	String s = value;
	for(int i=0; i<vec.size(); i++) {
	    s = s + vec.get(i).code();
	}
	return s;
    }

    /*
    // post: returns a following
    public String toString() {
	String s = prefix + (isTail ? "L-- " : "|-- ") + value + "\n";
	for(int i=0; i<5; i++) {
	    if(children[i] != null) {
		s = s + children[i].toString(prefix + (isTail ? "    " : "|   "), i ==4);
	    } else {
		s = s + prefix + (isTail ? "    " : "|   ") + (i == 4 ? "L-- " : "|-- ") + "null" + "\n";
	    }
	}
	return s;
	} */

    public static void main(String[] args) {
	// test code can go here
	Tree tree = new Tree("h");
	tree.set(0, new Tree("h"));
	tree.set(4, new Tree("l"));
	System.out.print(tree);
    }
}