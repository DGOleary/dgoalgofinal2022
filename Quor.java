package aircondition;

import java.util.Iterator;
import java.util.HashSet;

/**
 * @author Darby Oleary
 * @date 12-13-2022
 * 
 *       A node class for the spots on the board for a quoridor game, that allows you to connect quors, and remove them which simulates placing barriers, along with other methods to help a game class determine valid moves
 */

public class Quor {
	int x, y;
	HashSet<Quor> adj;

	// uses a hashset because the order of the nodes doesn't matter and if a node is
	// somehow accidentally added twice to the adjacency list it wont affect anything
	
	/** 
	 * Constructor method
	 * 
	 * @param x x coordinate of the quor
	 * @param y y coordinate of the qour
	 */
	public Quor(int x, int y) {
		this.x = x;
		this.y = y;
		adj = new HashSet<Quor>();
	}

	
	/** 
	 * 
	 * Doubly adds quors because when you are traversing through the board there could be potential issues if quor's are added in only one direction, especially if the directions don't have a pattern
	 * 
	 * @param q the quor being added
	 */
	void add(Quor q) {
		adj.add(q);
		q.adj.add(this);
	}

	
	/** 
	 * 
	 * For the way this game is made, two quors being in the same spot means they are the same, and it's simple to then just input the coordinates to check
	 * 
	 * @param x the x coordinate of the spot being checked
	 * @param y the x coordinate of the spot being checked
	 * @return boolean if the spot is or isn't in the same spot as this quor
	 */
	public boolean equals(int x, int y) {
		return this.x == x && this.y == y;
		// uses x and y so it's simple to see if a spot is in this quor's adjacents
	}

	
	/** 
	 * 
	 * Removes the quors from the each others adjanency lists to simulate a barrier being placed, they now no longer know the other one exists unless they are added back together, if the spot is invalid it will simply do nothing
	 * 
	 * @param x the x coordinate of the spot being checked
	 * @param y the x coordinate of the spot being checked
	 */
	public void remove(int x, int y) {
		Iterator<Quor> it = adj.iterator();
		while (it.hasNext()) {
			Quor temp = it.next();
			if (temp.equals(x, y)) {
				adj.remove(temp);
				temp.remove(this.x, this.y);
				break;
				// if you found the node to remove going through more nodes is a waste of time
			}
		}
		// simple because you can search for a node by the spot to find if it needs to
		// be removed
	}

	
	/** 
	 * @param x the x coordinate of the spot being checked
	 * @param y the x coordinate of the spot being checked
	 * @return boolean if the quor at the given x and y is in this quor's adjanency list, meaning they are neighbors
	 */
	public boolean contains(int x, int y) {
		Iterator<Quor> it = adj.iterator();
		while (it.hasNext()) {
			Quor temp = it.next();
			if (temp.equals(x, y)) {
				return true;
			}
		}
		return false;
	}
	// uses points instead of a node because it's more simple to use the points and
	// every node has distinct points so that is all that needs to be checked

}
