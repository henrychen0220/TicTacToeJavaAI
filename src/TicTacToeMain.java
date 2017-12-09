import java.util.Scanner;

/**
 * 
 * This class implement basic logic for the TicTacToe game
 * and store basic data structure like gameboard. It will
 * inherit some functions created in TicTacToeController
 *
 */
public class TicTacToeMain extends TicTacToeController {
	
	private int [][] gameBoard = new int [3][3];
	private TicTacToeView gameView = new TicTacToeView();
	private boolean gameOver = false;
	
	// 0 = nothing placed
	// 1 = x placed = user
	// 2 = o placed = computer
	
	/**
	 * this startGame() method will determine who first move by asking
	 * the user. After confirming the first move, it will then begin
	 * the game
	 */
	public void startGame() {
		System.out.println("Start Game");
		int firstMove = this.determineFirstMove();
		this.gameSteps(firstMove);
	}
	
	/**
	 * This is the main method for playing the game, where
	 * it will print the board first to show an empty table. If it's
	 * user's term, it will prompt for input and checked whether the
	 * input is valid. If it's AI's term, it will call functions in
	 * the TicTacToeController to handle AI's logic. The while loop
	 * will end until either of user or AI wins or the game is a tie.
	 * 
	 * @param firstMove who will make the first move as user has specified
	 */
	private void gameSteps(int firstMove) {
		int turn = firstMove;
		int row_moved;
		int col_moved;
		System.out.println("\nInitial Board");
		gameView.printBoard(this.getGameBoard());		
		while (!this.checkTie()) {
			if (turn == 1) {
				System.out.println("\nUser's turn");
				int[] userMoved = this.usersTerm();
				row_moved = userMoved[0];
				col_moved = userMoved[1];
				this.gameBoard[row_moved][col_moved] = 1;
				if (this.checkStateAfterMove(row_moved, col_moved, this.gameBoard)) {
					gameView.printBoard(this.getGameBoard());		
					System.out.println("User has won!");
					return;
				}
				turn = 2;
			} else {
				System.out.println("\nAI's turn");
				int [] AI_move = this.getAIPosition(this.gameBoard);
				row_moved = AI_move[0];
				col_moved = AI_move[1];
				this.gameBoard[row_moved][col_moved] = 2;
				if (this.checkStateAfterMove(row_moved, col_moved, this.gameBoard)) {
					gameView.printBoard(this.getGameBoard());		
					System.out.println("AI has won!");
					return;
				}
				turn = 1;
			}
			gameView.printBoard(this.getGameBoard());		
		}
		System.out.println("\nTie!");
	}
	
	/**
	 * This returns the game board data currrently. 
	 * @return current game board data
	 */
	private int[][] getGameBoard(){
		return this.gameBoard;
	}
	
	/**
	 * this determineFirstMove() asks for user's input and pick
	 * whether user moves first or AI moves first. The input has
	 * to be either 'u' or 'p', else it will show an error and continue
	 * to ask for a new input
	 * 
	 * @return the player that has the first move
	 */
	private int determineFirstMove() {
		Scanner input = new Scanner(System.in);
		System.out.print("Who moves first? (u : user, p : program): ");
		String x = input.next().toLowerCase();
		while (!x.equals("u") && !x.equals("p")) {
			System.out.println("Error: Invalid Input (enter 'u' or 'p') ");
			System.out.print("Who moves first? (u : user, p : program): ");
			x = input.next().toLowerCase();
		}
		if (x.equals("u")) {
			return 1;
		} else {
			return 2;
		}
	}
	
	/**
	 * This usersTerm() method will get the integer input result
	 * from 2 other input methods and determine if they represent
	 * a valid position. It will first check if it is in the range
	 * 0 - 2, then it will check if the specified position is 
	 * occupied or not. If any error has occured, it will log in the
	 * console and ask the user again for input.
	 * 
	 * @return the position where the user specified to move
	 */
	private int[] usersTerm() {
		int [] positionMoved = new int [2];
		int rowMove = this.promptUserRow();
		int colMove = this.promptUserColumn();
		while (true) {
			if (rowMove < 0 || rowMove > 2 || colMove < 0 || colMove > 2) {
				System.out.println("Error: Row and Column specified should be in range 0 to 2");
			} else if (this.gameBoard[rowMove][colMove] != 0) {
				System.out.println("Error: Position specified is already occupied");
			} else {
				break;
			}
			rowMove = this.promptUserRow();
			colMove = this.promptUserColumn();
		}
		positionMoved[0] = rowMove;
		positionMoved[1] = colMove;
		return positionMoved;
	}
	
	/**
	 * this promptUserRow() method asks for user's input to the row
	 * where they want to place their moves. It will check whether
	 * the input is an integer.
	 * 
	 * @return input row number
	 */
	private int promptUserRow() {
		System.out.print("Enter the row to move: ");
		Scanner inputRow = new Scanner(System.in);
		while (!inputRow.hasNextInt()) {
			System.out.println("Error!: Input row must be an integer");
			System.out.print("Enter the row to move: ");
			inputRow.nextLine();
		}
		return inputRow.nextInt();
	}
	
	/**
	 * this promptUserColumn() method asks for user's input to the column
	 * where they want to place their moves. It will check whether
	 * the input is an integer.
	 * 
	 * @return input column number
	 */
	private int promptUserColumn() {
		System.out.print("Enter the column to move: ");
		Scanner inputColumn = new Scanner(System.in);
		while(!inputColumn.hasNextInt()) {
			System.out.println("Error!: Input column must be an integer");
			System.out.print("Enter the column to move: ");
			inputColumn.nextLine();
		}
		return inputColumn.nextInt();
	}
	
	/**
	 * this checkTie() method will check if the current game state
	 * is a tie. A game is a tie if every position is filled while
	 * there is no winner
	 * 
	 * @return a boolean statement whether the game state now is a tie or not
	 */
	private boolean checkTie() {
		// if all positions have been placed (position != 0)
		// if no one won, return true, return false otherwise
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				if (this.gameBoard[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}
	
}
