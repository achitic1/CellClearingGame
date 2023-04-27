/* Group Members
 * Anthony Chitic - achitic
 * Yonghoon Kim - ykim1212
 * Vasudev Joshi - vjoshi
 * Derik Luc - dluc
 */

package model;

import java.util.Random;

/**
 * This class extends GameModel and implements the logic of strategy #1.
 * A description of this strategy can be found in the javadoc for the
 * processCell method.
 * 
 * We define an empty cell as BoardCell.EMPTY. An empty row is defined 
 * as one where every cell corresponds to BoardCell.EMPTY.
 * 
 * @author Department of Computer Science, UMCP
 */

public class ProcessCellGame extends Game {
	Random rand;
	int strategy;
	int score;
	/**
	 * Defines a board with empty cells. It relies on the super class
	 * constructor to define the board.  The random parameter is used
	 * for the generation of random cells.  The strategy parameter
	 * defines which processing cell strategy to use (for this project
	 * the default will be 1).
	 * 
	 * @param maxRows
	 * @param maxCols
	 * @param random
	 * @param strategy
	 */
	public ProcessCellGame(int maxRows, int maxCols, Random random, int strategy) {
		super(maxRows, maxCols);
		this.strategy = strategy;
		rand = random;
	}

	/**
	 * The game is over when the last board row (row with index board.length -1)
	 * is different from empty row.
	 */
	public boolean isGameOver() {
		return !isRowEmpty(board[board.length - 1]);
	}

	public int getScore() {
		return score;
	}

	/**
	 * This method will attempt to insert a row of random BoardCell objects if
	 * the last board row (row with index board.length -1) corresponds to the
	 * empty row; otherwise no operation will take place.
	 */
	public void nextAnimationStep() {
		if(!isGameOver()) {
			BoardCell[] randRow = new BoardCell[this.getMaxCols()];
			for(int col = 0; col < this.getMaxCols(); col++) {
				randRow[col] = BoardCell.getNonEmptyRandomBoardCell(rand);
			}

			shiftRowsDownOne(randRow);
		}
	}

	/**
	 * The default processing associated with this method is that for
	 * strategy #1. If you add a new strategy, make sure you add
	 * a conditional so the processing described below is associated with
	 * strategy #1.
	 * <br><br>
	 * Strategy #1 Description.<br><br>
	 * This method will turn to BoardCell.EMPTY the cell selected and any
	 * adjacent surrounding cells in the vertical, horizontal, and diagonal
	 * directions that have the same color. The clearing of adjacent cells
	 * will continue as long as cells have a color that corresponds to the 
	 * selected cell. Notice that the clearing process does not clear every 
	 * single cell that surrounds a cell selected (only those found in the 
	 * vertical, horizontal or diagonal directions).
	 * <br>
	 * IMPORTANT: Clearing a cell adds one point to the game's score.<br>
	 * <br>
	 * 
	 * If after processing cells, any rows in the board are empty,those
	 * rows will collapse, moving non-empty rows upward. For example, if 
	 * we have the following board (an * represents an empty cell):<br />
	 * <br />
	 * RRR<br />
	 * GGG<br />
	 * YYY<br />
	 * * * *<br/>
	 * <br />
	 * then processing each cell of the second row will generate the following
	 * board<br />
	 * <br />
	 * RRR<br />
	 * YYY<br />
	 * * * *<br/>
	 * * * *<br/>
	 * <br />
	 * IMPORTANT: If the game has ended no action will take place.
	 * 
	 * 
	 */
	public void processCell(int rowIndex, int colIndex) {
		if(isValid(rowIndex, colIndex) && !isGameOver()) {
			BoardCell targetColor;
			targetColor = board[rowIndex][colIndex];

			if(strategy == 1 && targetColor != BoardCell.EMPTY) {
				
				//Recursively clears all horizontal cells
				clearCellHorizontal(rowIndex, colIndex, targetColor);
				board[rowIndex][colIndex] = targetColor;
				
				//Recursively clears all diagonal cells in the direction of /
				clearCellDiagonalOne(rowIndex, colIndex, targetColor);
				board[rowIndex][colIndex] = targetColor;
				
				//Recursively clears all diagonal cells in the direction of \
				clearCellDiagonalTwo(rowIndex, colIndex, targetColor);
				board[rowIndex][colIndex] = targetColor;
				
				//Clears the cells veritcally
				clearCellVertical(rowIndex, colIndex, targetColor);

				//Initial score is counted 3 times due to the need to reset the initial cell
				score = score - 3;

				//Collapses all empty rows
				int numEmptyRows = getNumEmptyRows();
				for(int i = 0; i < numEmptyRows; i++) {
					collapseRow();
				}
				
			} else if(strategy == 2 && targetColor != BoardCell.EMPTY) {
				for(int row = 0; row < board.length; row++) {
					for(int col = 0; col < board[row].length; col++) {
						if(targetColor == board[row][col]) {
							board[row][col] = BoardCell.EMPTY;
							score++;
						}
					}
				}
				
				//Collapses the empty rows
				int numEmptyRows = getNumEmptyRows();
				for(int i = 0; i < numEmptyRows; i++) {
					collapseRow();
				}
			}
		}
	}


	//PRIVATE METHODS

	//Recursive Methods for ProcessCells

	private void clearCellHorizontal(int rowIndex, int colIndex, BoardCell target) {
		if(!isValid(rowIndex, colIndex) || board[rowIndex][colIndex] != target) {
			return;
		} else if(board[rowIndex][colIndex] == target){
			board[rowIndex][colIndex] = BoardCell.EMPTY;
			score++;
			clearCellHorizontal(rowIndex, colIndex + 1, target);
			clearCellHorizontal(rowIndex, colIndex - 1, target);
		}
	}

	private void clearCellDiagonalOne(int rowIndex, int colIndex, BoardCell target) {
		if(!isValid(rowIndex, colIndex) || board[rowIndex][colIndex] != target) {
			return;
		} else if(board[rowIndex][colIndex] == target){
			board[rowIndex][colIndex] = BoardCell.EMPTY;
			score++;
			clearCellDiagonalOne(rowIndex - 1, colIndex + 1, target);
			clearCellDiagonalOne(rowIndex + 1, colIndex - 1, target);
		}
	}

	private void clearCellDiagonalTwo(int rowIndex, int colIndex, BoardCell target) {
		if(!isValid(rowIndex, colIndex) || board[rowIndex][colIndex] != target) {
			return;
		} else if(board[rowIndex][colIndex] == target){
			board[rowIndex][colIndex] = BoardCell.EMPTY;
			score++;
			clearCellDiagonalTwo(rowIndex + 1, colIndex + 1, target);
			clearCellDiagonalTwo(rowIndex - 1, colIndex - 1, target);
		}
	}

	private void clearCellVertical(int rowIndex, int colIndex, BoardCell target) {
		if(!isValid(rowIndex, colIndex) || board[rowIndex][colIndex] != target) {
			return;
		} else if(board[rowIndex][colIndex] == target){
			board[rowIndex][colIndex] = BoardCell.EMPTY;
			score++;
			clearCellVertical(rowIndex + 1, colIndex, target);
			clearCellVertical(rowIndex - 1, colIndex, target);
		}
	}


	//METHODS FOR MANIPULATING ROWS

	private void collapseRow() {
		int lastRowIndex = getIndexOfLastNonEmptyRow();

		for(int row = lastRowIndex; row >= 0; row--) {
			if(this.isRowEmpty(board[row]) ) {
				shiftRowsUpOne(row, lastRowIndex + 1);
			}
		}
	}

	private void shiftRowsUpOne(int firstRow, int lastRow) {
		for(int row = firstRow; row < lastRow; row++) {
			board[row] = board[row + 1];
		}
	}

	private void shiftRowsDownOne(BoardCell[] randRow) {
		int maxRows = this.getMaxRows();
		BoardCell[] tempRow = randRow;
		BoardCell[] currRow;

		for(int row = 0; row < maxRows; row++) {
			if(row == maxRows - 1) {
				board[row] = tempRow;
			} else {
				currRow = board[row];
				board[row] = tempRow;
				tempRow = currRow;
			}
		}
	}


	//METHODS TO CHECK TABLE
	private boolean isRowEmpty(BoardCell[] array) {
		for(BoardCell b : array) {
			if(b != BoardCell.EMPTY) {
				return false;
			}
		}

		return true;
	}

	private boolean isValid(int rowIndex, int colIndex) {
		return (rowIndex < getMaxRows() && rowIndex >= 0 && colIndex < getMaxCols() && colIndex >= 0);
	}
	
	private int getNumEmptyRows() {
		int numEmptyRows = 0;
		int indexLastNonEmpty = getIndexOfLastNonEmptyRow();
		
		for(int row = 0; row < indexLastNonEmpty; row++) {
			if(isRowEmpty(board[row])) {
				numEmptyRows++;
			}
		}
		
		return numEmptyRows;
	}
	
	private int getIndexOfLastNonEmptyRow() {
		for(int row = getMaxRows() - 1; row >= 0; row--) {
			if(!isRowEmpty(board[row])) {
				return row;
			}
		}
		
		return 0;
	}
}