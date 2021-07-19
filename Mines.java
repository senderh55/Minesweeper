package mines;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Mines {
	private int height, width;
	private Place[][] board;
	private boolean showAll = false;

	public Mines(int height, int width, int numMines) {
		this.height = height;
		this.width = width;
		createBoard();
		setMines(numMines);
	}

	private void createBoard() { // create board of places
		board = new Place[height][width];
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				board[i][j] = new Place(i, j);
	}

	// inner class describing Place
	private class Place {
		int i, j;
		boolean isMine = false, isOpen = false, isFlag = false;

		public Place(int i, int j) {
			this.i = i;
			this.j = j;
		}

		public int getI() {
			return i;
		}

		public int getJ() {
			return j;
		}

		// create and returns the list of legal neighbors of the location
		public Collection<Place> neighbours() {
			List<Place> neighboursList = new ArrayList<Place>();
			if (i > 0) // up
				neighboursList.add(board[i - 1][j]);
			if (i < height - 1) // down
				neighboursList.add(board[i + 1][j]);
			if (j > 0) // left
				neighboursList.add(board[i][j - 1]);
			if (j < width - 1) // right
				neighboursList.add(board[i][j + 1]);
			if (i > 0 && j > 0) // up and left
				neighboursList.add(board[i - 1][j - 1]);
			if (i < height - 1 && j < width - 1) // down and right
				neighboursList.add(board[i + 1][j + 1]);
			if (i > 0 && j < width - 1) // up and right
				neighboursList.add(board[i - 1][j + 1]);
			if (i < height - 1 && j > 0) // down and left
				neighboursList.add(board[i + 1][j - 1]);
			return neighboursList;
		}

		// print point information according to its status
		public String toString() {
			// checks if the status of the place is closed(and showAll disabled) and the place is flag
			if (!showAll && !isOpen) {
				if (isFlag)
					return "F";
				return ".";
			}
			// if place is mine - open place will return false, so if showAll allowed and isMine return "X"
			if (showAll && isMine)
				return "X";
			// count the number of mines around the place and return relevent string
			int count = NeighborsMineCounter(this);
			if (count == 0)
				return " ";
			return "" + count;

		}
	}

	private void setMines(int numMines) { // set mines in random places according board range
		Random rand = new Random();
		while (numMines > 0) 
			if(addMine(rand.nextInt(height), rand.nextInt(width)))
				numMines--;
	}

	public boolean addMine(int i, int j) { // add mine to the place
		if (board[i][j].isMine) // place already with mine
			return false;
		board[i][j].isMine = true;
		return true;
	}

	/*
	 * indicates that the user is opening this location. returns true if it is not a
	 * mine. in addition, if there are no mines in place neighbors, open the
	 * neighboring locations recursively.
	 */
	public boolean open(int i, int j) {
		if (board[i][j].isMine)// its a mine
			return false;
		if (board[i][j].isOpen) // place already opened
			return true;
		board[i][j].isOpen = true;
		
		int minesCount = NeighborsMineCounter(board[i][j]);
		if (minesCount == 0) // there are no mines in the place neighbors - continue openning them
			for (Place neighbor : board[i][j].neighbours())
				open(neighbor.getI(), neighbor.getJ());
		return true;
	}

	// count the number of mines in place neigbors
	private int NeighborsMineCounter(Place p) {
		int count = 0;
		for (Place neighbor : p.neighbours())
			if (neighbor.isMine)
				count++;
		return count;
	}

	public void toggleFlag(int x, int y) { // put flag or removes it depending on the situation
		board[x][y].isFlag = !(board[x][y].isFlag);
	}

	public boolean isDone() {
		// checks if all places without mines are open
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				if (!(board[i][j].isOpen) && !(board[i][j].isMine))
					return false;
		return true;
	}

	// Returns representation as a string of the place
	public String get(int i, int j) {
		return board[i][j].toString();
	}

	/*
	 * set value in showAll, when it is true get and toString methods will display
	 * the places as open (but without a real opening of the places)
	 */
	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++)
				b.append(get(i, j));
			b.append('\n');
		}
		return b.toString();
	}

}
