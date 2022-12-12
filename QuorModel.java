package aircondition;

import java.util.LinkedList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * @author Darby Oleary
 * @date 12-13-2022
 * 
 *       The game logic for a quoridor game, contains a board of quors and their
 *       links between each other represent barriers existing there or not
 */

public class QuorModel {

	private int size;
	private Quor[][] board;
	private int[] p1 = new int[2];
	private int[] p2 = new int[2];
	protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private char lastDir;
	private boolean player = true;
	// player 1/red/true's goal is the bottom layer, player 2/blue/false's goal is
	// the top

	/**
	 * Constructor method
	 * 
	 * @param n the size of the playing spaces on the board, the board is a square
	 *          so it rows and columns are the same
	 */
	public QuorModel(int n) {
		size = n;
		p1[0] = 0;
		p1[1] = size / 2;
		p2[0] = size - 1;
		p2[1] = size / 2;
		// sets player 1 and 2's positions to the correct spots in the middle of the top
		// and bottom rows
		board = new Quor[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				board[i][j] = new Quor(i, j);
			}
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (j < n - 1) {
					board[i][j].add(board[i][j + 1]);

				}
				if (i < n - 1) {
					board[i][j].add(board[i + 1][j]);

				}
			}
		}
		// creates connections between all adjacent quors
	}

	/**
	 * 
	 * Helper method for the createBarrier function
	 * 
	 * @param p the player you want to check a path for
	 * @return boolean if a path to victory for that player exists
	 */
	private boolean checkPath(boolean p) {
		int x, y;
		if (p) {
			x = p1[0];
			y = p1[1];
		} else {
			x = p2[0];
			y = p2[1];
		}
		LinkedList<Quor> quors = new LinkedList<Quor>();
		boolean visit[][] = new boolean[size][size];
		// array of visited quors to prevent loops from going to the same spots over and
		// over
		quors.add(board[x][y]);
		while (quors.size() > 0) {
			Quor temp = quors.poll();
			if (visit[temp.x][temp.y]) {
				continue;
				// skips already checked quor
			} else {
				visit[temp.x][temp.y] = true;
			}
			// p is the player who is being checked for having a path to victory
			if (temp.x == 0 && !p) {
				return true;
			} else if (temp.x == size - 1 && p) {
				return true;
			}
			// if it finds a node in the winning zone for the player it's checking, that
			// means a path to victory exists, so the function can terminate successfully
			for (Quor q : temp.adj) {
				if (q != null) {
					quors.add(q);
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * This function allows the players to create barriers, and uses the earlier
	 * checkPath method to make sure the barrier doesn't break the rules and create
	 * an unwinnable came on one or both sides, barriers are "owned" by the node
	 * either directly above or to the left of it
	 * 
	 * @param x the x value of the quor who "own's" a barrier you want to create
	 * @param y the x value of the quor who "own's" a barrier you want to create
	 * @param d the direction of the barrier relative to the "owner" node, 'd' for
	 *          down and 'r' for right
	 */
	public void createBarrier(int x, int y, char d) {
		// if somehow it is managed to click on a barrier in the view that doesn't
		// really exist and disconnect a node and a non-existent node the remove
		// function of the quor should simply return false, or it should be caught by
		// the bounds checker, but really the view should have done a better job and
		// given a real game board

		// removing two nodes' connections in their lists is the same as placing a
		// barrier in the viewable game

		// 'd' for down and 'r' for right, every barrier is "owned" by the node either
		// above it or to the left of it, so inputting the barrier's location and the
		// direction tells you where it is
		lastDir = d;
		// saves the direction so the view can access it to adjust the barrier color
		Quor temp = board[x][y];
		Quor temp2;
		// creates a variable to store the node being disconnected from the given node
		// to reset the board if the function doesn't allow a barrier
		int[] spot;
		if (x >= size || y >= size) {
			this.pcs.firePropertyChange("fail", null, "Out of bounds barrier!");
		}
		// Just in case, but is technically impossible if the view is correctly made

		if (d == 'd') {
			if (!temp.contains(x + 1, y)) {
				this.pcs.firePropertyChange("fail", null, "You can not place a barrier on a barrier!");
			}

			spot = new int[] { x, y };
			temp2 = board[x + 1][y];
			board[x][y].remove(temp.x + 1, temp.y);

		} else {
			if (!temp.contains(x, y + 1)) {
				this.pcs.firePropertyChange("fail", null, "You can not place a barrier on a barrier!");
			}

			spot = new int[] { x, y };
			temp2 = board[x][y + 1];
			board[x][y].remove(temp.x, temp.y + 1);

		}
		// it removes the nodes from each other on the main board and test a path,
		// because it is more efficient to do that and reset the connection if there's
		// no path than it is to spend the time and memory creating an entirely new
		// board that is exactly the same except for 2 nodes being unconnected

		if (checkPath(!player) && checkPath(player)) {
			player = !player;

			this.pcs.firePropertyChange("barrier", player, spot);
			// sends who is playing and the spot the barrier should be to the node in one
			// property change to minimize calls back and forth
		} else {

			temp.add(temp2);
			// adds the given node and the one it got removed from back together to reset
			// the board to what it was before

			this.pcs.firePropertyChange("fail", null, "You can not block a player from winning!!");

		}

	}

	/**
	 * @return boolean the current player, player 1/red/true or player 2/blue/false
	 */
	public boolean player() {
		return player;
	}

	/**
	 * @return char the last direction a barrier was placed in, this method solely
	 *         exists to help the view place a barrier on screen by allowing it to
	 *         ask the model what direction it goes in
	 */
	public char lastDir() {
		return lastDir;
	}

	/**
	 * 
	 * Helper method for the main move method, checks for barriers and winners
	 * 
	 * @param x    the x the player wants to move to
	 * @param y    the y the player wants to move to
	 * @param px   the x coordinate of the player being checked
	 * @param py   the y coordinate of the player being checked
	 * @param spot an int array holding the original coordinates of the player to
	 *             pass to the view for it's use, it exists because in the case of a
	 *             jump the original coordinates are different than px and py
	 * @return boolean if the move is valid or not
	 */
	private boolean moveCheck(int x, int y, int px, int py, int[] spot) {
		// this function is based on the global player value due to the move logic
		// explained in the main move function
		if (x >= size || y >= size) {
			this.pcs.firePropertyChange("fail", null, "Out of bounds barrier!");
		}
		// Just in case, but is technically impossible if the view is correctly made

		if (!board[x][y].contains(px, py)) {
			this.pcs.firePropertyChange("fail", null, "You can not move through a barrier!");
			return false;
		}
		spot[2] = x;
		spot[3] = y;
		// first two spots are set in main move method, these two are the new spot the
		// piece wants to move to
		if (x == 0 && !player) {
			this.pcs.firePropertyChange("win", player, spot);
			return true;
		} else if (x == board.length - 1 && player) {
			this.pcs.firePropertyChange("win", player, spot);
			return true;
		}
		// checks if the players moved to a winning spot

		if (player) {
			p1[0] = x;
			p1[1] = y;

		} else {
			p2[0] = x;
			p2[1] = y;

		}
		// moves the player
		player = !player;
		this.pcs.firePropertyChange("move", player, spot);
		return true;
	}

	/**
	 * 
	 * Method that moves a player and checks if the move is a valid distance from
	 * the player
	 * 
	 * @param x the x the player wants to move to
	 * @param y the y the player wants to move to
	 */
	public void move(int x, int y) {
		if ((x == p1[0] && y == p1[1]) || (x == p2[0] && y == p2[1])) {
			this.pcs.firePropertyChange("fail", null, "You can not move to an occupied spot!");
			return;
		}

		int px, py, p2x, p2y;
		int[] spot = new int[4];
		// creates an array of the original spot and the spot it wants to move to, so on
		// a successful move it can fire this to the view and the view and repaint the
		// two spots to the correct colors
		if (player) {
			px = p1[0];
			py = p1[1];
			p2x = p2[0];
			p2y = p2[1];
		} else {
			px = p2[0];
			py = p2[1];
			p2x = p1[0];
			p2y = p1[1];
		}
		spot[0] = px;
		spot[1] = py;
		// code for jumping the other player, checks if players are next to each other
		// and then calls moveCheck if a valid spot is clicked
		if ((Math.abs(px - p2x) == 1 && py == p2y) || (Math.abs(py - p2y) == 1 && px == p2x)) {

			if ((Math.abs(p2x - x) == 1 && p2y == y) || (Math.abs(p2y - y) == 1 && p2x == x)) {
				moveCheck(x, y, p2x, p2y, spot);
				// when the two players are next to each other non-diagonally, a "jump" move is
				// the same as checking if the move the player is making is a valid move for the
				// opponent, so you can simply run moveCheck on the opponent's coordinates and
				// determine who is moving based on the global variable tracking who's turn it
				// is
				return;

			}

			// code for a regular non jump move
		}
		if ((Math.abs(px - x) == 1 && py == y) || (Math.abs(py - y) == 1 && px == x)) {
			moveCheck(x, y, px, py, spot);
			return;
			// returns prevent the default fail propertyChange from firing

		}
		// to simplify methods and if statements needed, the way valid move ranges are
		// checked is by if the absolute value of the difference between the x's or y's
		// is 1, meaning they are next to each other, and then if the one not checked (x
		// if the y difference is 1 and vice versa) are the same, because you can either
		// move vertically in the same column or horizontally in the same row, but not
		// diagonally
		this.pcs.firePropertyChange("fail", null, "You can not move out of range!");

	}

	/**
	 * @param listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	/**
	 * @param listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}

	/**
	 * @return int
	 */
	public int getp1X() {
		return p1[0];
	}

	/**
	 * @return int
	 */
	public int getp1Y() {
		return p1[1];
	}

	/**
	 * @return int
	 */
	public int getp2X() {
		return p2[0];
	}

	/**
	 * @return int
	 */
	public int getp2Y() {
		return p2[1];
	}
}
