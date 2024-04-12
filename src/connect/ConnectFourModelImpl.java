package connect;

/**
 * Implements the Connect Four game logic for a variable-sized board with a minimum size of 4x4.
 * Manages game state, player turns, and
 * win conditions for the classic two-player disc-dropping game.
 * The game follows the standard Connect Four rules where two players drop colored discs into
 * a vertically suspended grid, taking turns. The objective is to be the first to form a
 * horizontal, vertical, or diagonal line of four of one's own discs.
 */
public class ConnectFourModelImpl implements ConnectFourModel {
  private Player[][] board;
  private final int rows;
  private final int columns;
  private Player currentPlayer;
  private Player winner;

  /**
   * Constructs a Connect Four game model with the specified number of rows and columns.
   *
   * @param rows    the number of rows for the game board
   * @param columns the number of columns for the game board
   * @throws IllegalArgumentException if rows or columns are less than 4
   */
  public ConnectFourModelImpl(int rows, int columns) {
    if (rows < 4 || columns < 4) {
      throw new IllegalArgumentException("Board must be at least 4x4");
    }
    this.rows = rows;
    this.columns = columns;
    this.board = new Player[rows][columns];
    this.currentPlayer = Player.RED; // RED starts first
    this.winner = null;
    initializeBoard();
  }

  /**
   * Initializes the game board to the starting state with all cells set to null.
   */
  @Override
  public void initializeBoard() {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        board[i][j] = null;
      }
    }
    currentPlayer = Player.RED;
    winner = null;
  }

  /**
   * Places a disc in the specified column for the current player if possible.
   *
   * @param column the column in which to place the disc
   * @throws IllegalArgumentException if the column is out of bounds or full
   */
  @Override
  public void makeMove(int column) throws IllegalArgumentException {

    if (column < 0 || column >= columns) {
      throw new IllegalArgumentException("Column out of bounds");
    }

    for (int row = rows - 1; row >= 0; row--) {
      if (board[row][column] == null) {
        board[row][column] = currentPlayer;
        checkForWin(row, column);
        togglePlayer();
        return;
      }
    }

    throw new IllegalArgumentException("Column is full");
  }

  /**
   * Checks if there is a win condition at the specified row and column.
   *
   * @param row    the row to check from
   * @param column the column to check from
   */
  private void checkForWin(int row, int column) {
    if (hasConnectedFour(row, column)) {
      winner = currentPlayer;
    }
  }

  /**
   * Determines if there are four connected discs from the specified starting cell.
   *
   * @param row    the starting row
   * @param column the starting column
   * @return true if there are four connected discs, false otherwise
   */
  private boolean hasConnectedFour(int row, int column) {
    // Check horizontal, vertical, diagonal (both directions)
    return checkDirection(row, column, 1, 0) // horizontal
        || checkDirection(row, column, 0, 1) // vertical
        || checkDirection(row, column, 1, 1) // diagonal up-right
        || checkDirection(row, column, 1, -1);  // diagonal down-right
  }

  /**
   * Checks if there are four connected discs in a specific direction.
   *
   * @param row      the starting row
   * @param column   the starting column
   * @param deltaRow the row direction to check
   * @param deltaCol the column direction to check
   * @return true if there are four connected discs in the direction, false otherwise
   */
  private boolean checkDirection(int row, int column, int deltaRow, int deltaCol) {
    int count = 1;
    count += countDirection(row, column, deltaRow, deltaCol);
    count += countDirection(row, column, -deltaRow, -deltaCol);
    return count >= 4;
  }

  /**
   * Counts the number of consecutive discs in a specified direction.
   *
   * @param row      the starting row
   * @param column   the starting column
   * @param deltaRow the row direction to count
   * @param deltaCol the column direction to count
   * @return the number of consecutive discs
   */
  private int countDirection(int row, int column, int deltaRow, int deltaCol) {
    // dRow +1 for downwards
    // dCol +1 for rightwards
    int r = row + deltaRow;
    int c = column + deltaCol;
    int count = 0;
    // while r and c within board and position r,c is occupied by current player
    while (r >= 0 && r < rows && c >= 0 && c < columns && board[r][c] == currentPlayer) {
      count++;
      // move toward that direction
      r += deltaRow;
      c += deltaCol;
    }
    // continuous disc count
    return count;
  }

  /**
   * Toggles the current player.
   */
  private void togglePlayer() {
    currentPlayer = (currentPlayer == Player.RED) ? Player.YELLOW : Player.RED;
  }

  /**
   * Gets the current player's turn.
   *
   * @return the current player, or null if the game is over
   */
  @Override
  public Player getTurn() {
    return isGameOver() ? null : currentPlayer;
  }

  /**
   * Determines if the game is over.
   *
   * @return true if the game is over, false otherwise
   */
  @Override
  public boolean isGameOver() {
    // if there is winner
    if (winner != null) {
      return true;
    }
    // if no winner and still have space
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        if (board[i][j] == null) {
          return false;
        }
      }
    }
    // if no winner and board is full: draw
    return true;
  }

  @Override
  public Player getWinner() {
    return winner;
  }

  @Override
  public void resetBoard() {
    initializeBoard();
  }

  @Override
  public Player[][] getBoardState() {
    Player[][] copy = new Player[rows][columns];
    for (int i = 0; i < rows; i++) {
      System.arraycopy(board[i], 0, copy[i], 0, columns);
    }
    return copy;
  }

  /**
   * Returns a string representation of the game board.
   *
   * @return the game board as a string
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Player[] row : board) {
      for (Player cell : row) {
        sb.append(cell == null ? "." : cell.getDisplayName());
      }
      sb.append("\n");
    }
    return sb.toString();
  }
}
