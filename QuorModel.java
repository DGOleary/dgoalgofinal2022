package aircondition;

import java.util.LinkedList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class QuorModel {

	private int size;
	private Quor[][] board;
	private int[] p1= new int[2];
	private int[] p2= new int[2];
	protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	private boolean player; //player 1/true's goal is the top layer, player 2/false's goal is the bottom
	
	public QuorModel(int n) {
		size=n;
		p1[0]=0;
		p1[1]=size/2;
		p1[0]=size-1;
		p1[1]=size/2;
		board = new Quor[n][n];
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				board[i][j]=new Quor(i,j);
			}
			}
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				if(j<n-1) {
				board[i][j].add(board[i][j+1]);
				//System.out.println(i+" "+j+" , "+i+" "+(j+1));
				}
				if(i<n-1) {
					board[i][j].add(board[i+1][j]);
					//System.out.println(i+" "+j+" , "+(i+1)+" "+j);
				}
			}
		}
		//creates connections between all adjacent quors
	}
	
	boolean checkPath(boolean p) {
		int x,y;
		if(p){
			x=p1[0];
			y=p1[1];
		}else {
			x=p2[0];
			y=p2[1];
		}
		LinkedList<Quor> quors= new LinkedList<Quor>();
		boolean visit[][]=new boolean[size][size];
		quors.add(board[x][y]);
		while(quors.size()>0) {
				Quor temp=quors.poll();
				if(visit[temp.x][temp.y]) {
					continue;
				}else {
					visit[temp.x][temp.y]=true;
				}
				//p is the player who is being checked for having a path to victory
				if(temp.x==0 && p) {
					return true;
				}else if(temp.x==size-1 && !p) {
					return true;
				}
				for(Quor q: temp.adj) {
					if(q!=null) {
					quors.add(q);
					}
				}
		}
		return false;
	}

	public boolean createBarrier(int x, int y, char d) {
		Quor temp=board[x][y];
		Quor temp2;
		if(d=='d') {
			if(!temp.contains(x+1, y)){
				return false;
		}
		temp2=board[x+1][y];
			board[x][y].remove(temp.x+1, temp.y);

		}else {
			if(!temp.contains(x, y+1)){
				return false;
		}
			temp2=board[x][y+1];
			board[x][y].remove(temp.x, temp.y+1);

		}
		
		if(checkPath(!player)){
			player=!player;
			//this.pcs.firePropertyChange("barrier", null, true);
			return true;
			}else{
			board[x][y]=temp;
			if(d=='d'){
			board[x+1][y]=temp2;
			} else{
				board[x][y+1]=temp2;
			}
			return false;

			}

	}
	
	public boolean player() {
		return player;
	}
	
	public boolean moveCheck(int x, int y, int px, int py) {
	if((Math.abs(px-x)==1 && py==y)|| (Math.abs(py-y)==1 && px==x)){
	if(!board[x][y].contains(px, py)){
			return false;
	}	
	player=!player;
	if(x==0 && player){
		return true;
	//this.pcs.firePropertyChange("p1 win", null, true);
	}else if(x==board.length-1 && !player){
	return true;
	//this.pcs.firePropertyChange("p2 win", null, true);
	}
	//this.pcs.firePropertyChange("move", null, true);
	if(player){
			p1[0]=px;
			p1[1]=py;
	}else
			p2[0]=px;
			p2[1]=py;
	}
	return true;
	}
	
	public boolean move(int x, int y) {
		int px, py, p2x, p2y;
		if(player){
			px=p1[0];
			py=p1[1];
			p2x=p2[0];
			p2y=p2[1];
		}else {
			px=p2[0];
			py=p2[1];
			p2x=p1[0];
			p2y=p1[1];
		}
	if((Math.abs(px-p2x)==1 && py==p2y) || (Math.abs(py-p2y)==1 && px==p2x)){
		px=p2x;
			py=p2y;
		moveCheck(x, y, px, py);
		}
	if((Math.abs(px-x)==1 && py==y)|| (Math.abs(py-y)==1 && px==x)){
		moveCheck(x, y, px, py);
	}
	return false;
	}

	  public void addPropertyChangeListener(PropertyChangeListener listener) {
	        this.pcs.addPropertyChangeListener(listener);
	    }
	  
	  public void removePropertyChangeListener(PropertyChangeListener listener) {
	        this.pcs.removePropertyChangeListener(listener);
	    }
	  
	public static void main(String[] args) {
		QuorModel q = new QuorModel(4);
		int t=0;
		
		q.board[2][0].remove(1,0);
		q.board[2][1].remove(1,1);
		q.board[2][2].remove(1,2);
		//q.board[2][3].remove(1,3);
		System.out.println(q.board[2][0].contains(1, 0));
		System.out.println(q.board[1][0].contains(2, 0));
		
		//for(Quor r: q.board[2]) {
		//	r.remove(t, 1);
		//	t++;
		//}
		q.p1[0]=3;
		q.p2[0]=0;
		System.out.println(q.createBarrier(1,2,'r'));
		//q.board[1][0].remove(0,0);
		//q.board[1][1].remove(0,1);
		//System.out.println(q.board[1][0].contains(0, 0));
		//System.out.println(q.board[1][1].contains(0, 1));
		//System.out.println(q.checkPath(true));
		//System.out.println(q.checkPath(false));
	}
}
