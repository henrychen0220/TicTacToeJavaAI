import java.util.Comparator;
import java.util.PriorityQueue;


/**
 * 
 * this class will implement all the AI's logic
 * for TicTacToe game, including determining best move,
 * place that must be defended, and heuristics for selecting
 * position.
 *
 */
public class TicTacToeController {

	
	/**
	 * In this getAIPosition() method, the current best move will be determined.
	 * The highest priority is to pick the position where win is immediately
	 * possible. If not, it will pick the position that AI must defend now to
	 * prevent a loss. If note, the board will pick based on the defend/attack
	 * heuristic.
	 * 
	 * @param board game board
	 * @return the position that AI will move
	 */
	public int[] getAIPosition(int[][] board) {
		int [] win_move = this.existWinMove(board);
		if (win_move[0] != -1 && win_move[1] != -1) {
			return win_move;
		}
		int [] defend_move = this.existMustDefend(board, 2);
		if (defend_move[0] != -1 && defend_move[1] != -1) {
			return defend_move;
		}
		int [] selected = this.selectBasedOnHeuristics(board);
		return selected;
	}
	
	/**
	 * 
	 * this is a private class that I use as model. It is in the
	 * same file because this require some function calls from 
	 * TicTacToeController class. This class will be used
	 * when we are determining our heuristic
	 *
	 */
	private class gameModel {
		private int risk;
		private int attackHeu;
		private TicTacToeController controller;
		private int[] position = new int[2];
		gameModel(int row, int col, int[][] board){
			this.position[0] = row;
			this.position[1] = col;
			this.controller = new TicTacToeController();
			this.risk = this.controller.userWillDefend(board);
			this.attackHeu = this.controller.calculateAttackHeuristic(row, col, board);
		}
	}
	
	/**
	 * 
	 * This is the comparator that will be later used for a priority queue
	 * for determining the best moves based on heuristics. It will pick the position
	 * with lowest risk, if 2 positions have the same risk, it will use their 
	 * offensive heuristics to break the tie.
	 *
	 */
	private class gameModelComparator implements Comparator<gameModel> {
		@Override
		public int compare(gameModel o1, gameModel o2) {
			// TODO Auto-generated method stub
			if (o1.risk == o2.risk) {
				return o2.attackHeu - o1.attackHeu;
			}
			return o1.risk - o2.risk;
		}
	}
	
	/**
	 * This checkHorizontal() method will check if the horizontal
	 * row has a win condition where all positions in the same
	 * row have the same value.
	 * 
	 * @param row row position
	 * @param column column position
	 * @param board game board
	 * @return a boolean value determining whether there's a valid win condition on this row
	 */
	public boolean checkHorizontal(int row, int column, int [][] board) {
		int player = board[row][column];
		if (player == 0) {
			return false;
		}
		for (int i = 0; i < 3; ++i) {
			if (board[row][i] != player) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * This checkVertical() method will check if the vertical
	 * column has a win condition where all positions in the same
	 * column have the same value.
	 * 
	 * @param row row position
	 * @param column column position
	 * @param board game board
	 * @return a boolean value determining whether there's a valid win condition on this column
	 */
	public boolean checkVertical(int row, int column, int[][] board) {
		int player = board[row][column];
		if (player == 0) {
			return false;
		}
		for (int i = 0; i < 3; ++i) {
			if (board[i][column] != player) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * This checkDiagonal() method will check whether the input position
	 * will make the diagonal a valid win condition. If the input position
	 * is not in the diagonal position, return false immediately since
	 * it will not affect the diagonal. Then it will check whether
	 * the top-left, center, bottom-right positions have the same
	 * values. Then top-right, center, bottom-left positions. If any of those
	 * are true, it will return true. Else, it indicates there is no
	 * Diagonals that satisfy the win condition
	 * 
	 * @param row row position
	 * @param column column position
	 * @param board game board
	 * @return a boolean value whether the input position can create a win condition in the diagonals
	 */
	public boolean checkDiagonal(int row, int column, int[][] board) {
		if (!this.isDiagonal(row, column)) {
			return false;
		}
		int center = board[1][1];
//		System.out.println("In checkDiagonal: center = " + center);
		if (center == 0) {
			return false;
		}
		// left diagonal
		if (board[0][0] == center && board[2][2] == center) {
			return true;
		}
		// right diagonal
		if (board[0][2] == center && board[2][0] == center) {
			return true;
		}
		return false;
	}
	
	/**
	 * This will check if the input positions is part of the diagonal
	 * @param row row position
	 * @param col column position
	 * @return a boolean value that shows whether the input position is part of the diagonal positions
	 */
	public boolean isDiagonal(int row, int col) {
		return (row == 0 && col == 0) || (row == 0 && col == 2) || (row == 1 && col == 1) || (row == 2 && col == 0) || (row == 2 && col == 2);
	}
	
	/**
	 * This checkStateAfterMove will be called every time when a user or AI moves to check
	 * whether either of those has win the game
	 * 
	 * @param row input row
	 * @param col input column
	 * @param board game board for checking these booleans
	 * @return true if someone won, else, return false
	 */
	public boolean checkStateAfterMove(int row, int col, int[][] board) {
		return this.checkHorizontal(row, col, board) || this.checkVertical(row, col, board) || this.checkDiagonal(row,col,board);
	}
	
	/**
	 * This existWinMove() checks for AI whether there is a move
	 * that it can make to guarantee the victory in 1 step. This method will
	 * check all the empty positions and check its state if the position
	 * is filled, if the state shows victory, the position will be returned.
	 * 
	 * @param board game board
	 * @return a position where a direct victory is possible, if not, it will return {-1,-1} indicating
	 * that there is no 1 step victory position
	 */
	public int[] existWinMove(int[][] board) {
		int [] winPosition = new int[] {-1,-1};
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				if (board[i][j] == 0) {
					board[i][j] = 2;
					if (this.checkStateAfterMove(i, j, board)) {
						// return position
						board[i][j] = 0;
						winPosition[0] = i;
						winPosition[1] = j;
						return winPosition;
					}
					board[i][j] = 0;
				}
			}
		}
		return winPosition;
	}
	
	/**
	 * this existMustDefend() method will generate a position that
	 * an input player must defend to prevent a loss at that move.
	 * 
	 * @param board game board
	 * @param player whose step is it now?
	 * @return a position where the player must defend now, if there's none, return {-1,-1} indicating
	 * that there is no one step loss.
	 */
	public int[] existMustDefend(int [][] board, int player) {
		int [] defendPosition = new int[] {-1,-1};
		int defend = (player == 1 ? 2 : 1);
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				if (board[i][j] == 0) {
					board[i][j] = defend;
					if (this.checkStateAfterMove(i, j, board)) {
						board[i][j] = 0;
						defendPosition[0] = i;
						defendPosition[1] = j;
						return defendPosition;
					}
					board[i][j] = 0;
				}
			}
		}
		return defendPosition;
	}
	
	/**
	 * This will calculate the offensive heuristic for a row by
	 * calculating whether the AI can possibly fill up this row
	 * to win. If there is already an opponent's piece on this
	 * row, return 0 immediately. 
	 * 
	 * @param row input row that we want to check for our heuristic
	 * @param board game board
	 * @return the integer score of heuristic of row
	 */
	public int horizontalHeuristic(int row, int [][] board) {
		for (int i = 0; i < 3; ++i) {
			if (board[row][i] == 1) {
				return 0;
			}
		}
		return 1;
	}
	
	/**
	 * This will calculate the offensive heuristic for a column by
	 * calculating whether the AI can possibly fill up this entire
	 * column to achieve a victory. If there already existed an opponent's piece
	 * on this column, the method will return 0 immediately.
	 * 
	 * @param col input column that we want to check for our heuristic
	 * @param board game board
	 * @return the integer score of heuristic of column 
	 */
	public int verticalHeuristic(int col, int [][] board) {
		for (int i = 0; i < 3; ++i) {
			if (board[i][col] == 1) {
				return 0;
			}
		}
		return 1;
	}
	
	/**
	 * This diagonalHeuristic() method will calculate the heuristic value
	 * for the diagonals of the input position. If the position doesn't belong 
	 * to the diagonals, it will return 0 immediately. Then, it will check for the
	 * left diagonals and the right diagonals. Based on the input position, either
	 * left, right, or their sum will be returned.
	 * 
	 * @param row row position
	 * @param col column position
	 * @param board game board
	 * @return a heuristic value for diagonals at the input position
	 */
	public int diagonalHeuristic(int row, int col, int [][] board) {
		if (!this.isDiagonal(row, col)) {
			return 0;
		}
		
		int center = board[1][1];
		if (center == 1) {
			return 0;
		}
		int left_dia = (board[0][0] != 1 && board[2][2] != 1) ? 1 : 0; 
		int right_dia = (board[0][2] != 1 && board[2][0] != 1) ? 1 : 0; 
		if ((row == 0 && col == 0) || (row == 2 && col == 2)) {
			return left_dia;
		} else if ((row == 0 && col == 2) || (row == 2 && col == 0)) {
			return right_dia; 
		}
		// center
		return left_dia + right_dia;
	}

	/**
	 * This method() will calculate the number of win move
	 * that a player will have at that moment
	 * 
	 * @param board game board
	 * @param player player making the move at that moment
	 * @return the number of possible win moves at that moment
	 */
	public int countWinMove(int[][] board, int player) {
		int possibleWinMove = 0;
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				if (board[i][j] == 0) {
					board[i][j] = player;
					if (this.checkStateAfterMove(i, j, board)) {
						board[i][j] = 0;
						++possibleWinMove;
					}
					board[i][j] = 0;
				}
			}
		}
		return possibleWinMove;
	}
	
	
	/**
	 * This userWillDefend() method will calculate the risk of every
	 * position an AI enters. It will check for which position that
	 * the user must defend, if there's none, return a high value (10). If there
	 * is, calculate the number of possible winMoves that user can create
	 * if he moves in that position. By doing this, we can prevent any edge
	 * case that will generate more than 1 possible win for the user to prevent
	 * any potential losses.
	 * 
	 * @param board game board
	 * @return the risk that AI will be facing. The lower the better
	 */
	public int userWillDefend(int [][] board) {
		int [] willDefPos = this.existMustDefend(board, 1);
		if (willDefPos[0] == -1 || willDefPos[1] == -1) {
			// higher risk
			return 10;
		}
		board[willDefPos[0]][willDefPos[1]] = 1;
		int possibleWin = this.countWinMove(board, 1);
		board[willDefPos[0]][willDefPos[1]] = 0;
		if (possibleWin > 1) {
			return possibleWin;
		}
		return 0;
	}
	
	/**
	 * This calculateAttackHeuristic() will sum up the offensive
	 * heuristic value for horizontal, vertical, and diagonal for
	 * the input position
	 * 
	 * @param row input row
	 * @param col input column
	 * @param board game board
	 * @return the sum of heuristics value generate for a position
	 */
	public int calculateAttackHeuristic(int row, int col, int[][] board) {
		return this.horizontalHeuristic(row, board) + this.verticalHeuristic(col, board) 
		+ this.diagonalHeuristic(row, col, board);
	}
	
	/**
	 * This selectBasedOnHeuristics method will generate a move that has the
	 * lowest risk based on a priority queue that prioritize low risk value. If
	 * 2 have the same risk, it will search for the position that can provide
	 * higher offensive heuristic. 
	 * 
	 * @param board game board
	 * @return a position that the AI will move based on the heuristics
	 */
	public int[] selectBasedOnHeuristics(int [][] board) {
		int minRisk = Integer.MAX_VALUE;
		int [] selectedPosition = new int[2];
		
		gameModelComparator model_compare = new gameModelComparator();
		PriorityQueue<gameModel> pq = new PriorityQueue<gameModel>(model_compare);
		
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				if (board[i][j] == 0) {
					board[i][j] = 2;
					gameModel pos_model = new gameModel(i, j , board);
					pq.add(pos_model);
					board[i][j] = 0;

				}
			}
		}
		gameModel selectedModel = pq.poll();
		return selectedModel.position;
	}

	
	
}
