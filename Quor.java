package aircondition;

import java.util.Iterator;
import java.util.LinkedList;

public class Quor {
	int x, y;
	LinkedList<Quor> adj;

	Quor(int x, int y) {
		this.x = x;
		this.y = y;
		adj = new LinkedList<Quor>();
	}

	void add(Quor q) {
		adj.add(q);
		q.add(this);
	}

	public boolean equals(int x, int y) {
		return this.x == x && this.y == y;
		// uses x and y so it's simple to see if a spot is in this quor's adjacents
	}

	public void remove(int x, int y) {
		Iterator<Quor> it = adj.iterator();
		while (it.hasNext()) {
			Quor temp = it.next();
			if (temp.equals(x, y)) {
				adj.remove(temp);
				temp.remove(this.x, this.y);
				break;
			}
		}
		// simple because you can search for a node by the spot to find if it needs to
		// be removed
	}

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

}
