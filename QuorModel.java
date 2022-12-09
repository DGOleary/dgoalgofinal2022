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
	
	private boolean player=true; //player 1/red/true's goal is the bottom layer, player 2/blue/false's goal is the top
	
	public QuorModel(int n) {
		size=n;
		p1[0]=0;
		p1[1]=size/2;
		p2[0]=size-1;
		p2[1]=size/2;
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
	
	private boolean checkPath(boolean p) {
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
				if(temp.x==0 && !p) {
					return true;
				}else if(temp.x==size-1 && p) {
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
		//System.out.println("barrier func "+x+" "+y);
		Quor temp=board[x][y];
		Quor temp2;
		if(d=='d') {
			if(!temp.contains(x+1, y)){
				return false;
		}
			//System.out.println("barrier func2 "+(x+1)+" "+y);
		temp2=board[x+1][y];
			board[x][y].remove(temp.x+1, temp.y);

		}else {
			if(!temp.contains(x, y+1)){
				return false;
		}
			//System.out.println("barrier func2 "+x+" "+(y+1));
			temp2=board[x][y+1];
			board[x][y].remove(temp.x, temp.y+1);

		}
		
		if(checkPath(!player)){
			player=!player;
			this.pcs.firePropertyChange("move", null, player);
			//System.out.println("cut ");
			return true;
			}else{
			//board[x][y]=temp;
			temp.add(temp2);
			//if(d=='d'){
			//board[x+1][y]=temp2;
			//} else{
			//	board[x][y+1]=temp2;
			//}
			return false;

			}

	}
	
	public boolean player() {
		return player;
	}
	
	
	private boolean moveCheck(int x, int y, int px, int py) {
	
	if(!board[x][y].contains(px, py)){
			return false;
	}	
	
	if(x==0 && !player){
		this.pcs.firePropertyChange("win", null, player);
		return true;
	}else if(x==board.length-1 && player){
		this.pcs.firePropertyChange("win", null, player);
	return true;
	}
	//checks if the players moved to a winning spot 
	
	if(player){
			p1[0]=x;
			p1[1]=y;
			//System.out.println("p1 "+x+" "+y);
	}else {
			p2[0]=x;
			p2[1]=y;
			//System.out.println("p2 "+x+" "+y);
	}
	//moves the player
			player=!player;
		this.pcs.firePropertyChange("move", null, player);
	return true;
	}
	
	public boolean move(int x, int y) {
		if((x==p1[0]&&y==p1[1])||(x==p2[0]&&y==p2[1])) {
			return false;
		}
		//System.out.println("player "+player);
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
		//code for jumping the other player, checks if players are next to each other and then calls moveCheck if a valid spot is clicked
	if((Math.abs(px-p2x)==1 && py==p2y) || (Math.abs(py-p2y)==1 && px==p2x)){
		
			if((Math.abs(p2x-x)==1 && p2y==y)|| (Math.abs(p2y-y)==1 && p2x==x)){
		return moveCheck(x, y, p2x, p2y);
		}
	}
	//code for a regular non jump move
	//System.out.println(px);
	//System.out.println(x);
	if((Math.abs(px-x)==1 && py==y) || (Math.abs(py-y)==1 && px==x)){
		return moveCheck(x, y, px, py);
	}
	return false;
	}

	  public void addPropertyChangeListener(PropertyChangeListener listener) {
	        this.pcs.addPropertyChangeListener(listener);
	    }
	  
	  public void removePropertyChangeListener(PropertyChangeListener listener) {
	        this.pcs.removePropertyChangeListener(listener);
	    }
	  
	  public int getp1X() {
		  return p1[0];
	  }
	  
	  public int getp1Y() {
		  return p1[1];
	  }
	  
	  public int getp2X() {
		  return p2[0];
	  }
	  
	  public int getp2Y() {
		  return p2[1];
	  }
}
