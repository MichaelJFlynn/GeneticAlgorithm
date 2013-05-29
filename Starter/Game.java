import structure5.*;

public class Game {
    
    private Species winner;
    private Integer max;
    private Vector<Association<Species,Integer>> scores;

    public Game(Species w, Integer m, Vector<Association<Species, Integer>> s) {
	winner = w;
	scores = s;
	max = m;
    }

    public Species getWinner() {
	return winner;
    }

    public Vector<Association<Species, Integer>> getScores() { 
	return scores;
    }

    public Integer getScore(Species s) {
	for(int i=0; i<scores.size(); i++) {
	    Association<Species, Integer> assoc = scores.get(i);
	    if(assoc.getKey().getName().equals(s.getName())) {
		return assoc.getValue();
	    }
	}
	Assert.fail("There somehow was a missing score");
	return null;
    }

    public void printGame() {
	System.out.println("The winner is: " + winner.getName() + ", with " + max +" creatures.");
	System.out.println("Scores:");
	for(int i=0; i< scores.size(); i++) {
	    Species spec = scores.get(i).getKey();
	    Integer score = scores.get(i).getValue();
	    System.out.println("\t" + spec.getName() + ": " + score);
	}
    }
}