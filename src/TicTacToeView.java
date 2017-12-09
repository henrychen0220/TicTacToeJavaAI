
/**
 * 
 * This class will be responsible for printing
 * the game board based on its current state
 */
public class TicTacToeView {
	/**
	 * This will print the gameboard based on the state that
	 * the board is in. For empty positions, it will be ' ' on
	 * the board. For user, it will be 'X' on the board. For AI,
	 * it will be 'O' on the board
	 * 
	 * @param board game board
	 */
	public void printBoard(int [][] board) {
		System.out.println("");
		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 5; ++j) {
				if (j % 2 != 0) {
					System.out.print("|");
				} else if (i % 2 != 0) {
					System.out.print("-");
				} else {
					int player = board[i/2][j/2];
					if (player == 1) {
						System.out.print("X");
					} else if (player == 2) {
						System.out.print("O");
					} else {
						System.out.print(" ");
					}
				}
			}
			System.out.println("");
		}
	}
}
