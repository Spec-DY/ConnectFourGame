package connect;

/**
 * Interface for the Model component in the MVC architecture of a Connect Four game.
 * This interface defines the core functionalities required to manage the game's state,
 * including initializing the game board, handling player moves, and checking for
 * win/draw conditions.
 */
public interface ConnectFourModel {

  /**
   * Initializes the game board with a specified number of rows and columns.
   * Each cell in the board is set to a default state (e.g., empty).
   */
  void initializeBoard();

  /**
   * Attempts to place a disc in the specified column.
   * The disc will occupy the lowest available row within the column.
   * If the column is full, the move will be rejected.
   *
   * @param column the column in which to place the disc
   * @throws IllegalArgumentException if the column is out of bounds
   */
  void makeMove(int column) throws IllegalArgumentException;

  /**
   * Retrieves the player whose turn it is to make a move.
   *
   * @return the player whose turn it is
   */
  Player getTurn();

  /**
   * Checks if the game is over. The game is over when either the board is full, or
   * one player has won.
   *
   * @return true if the game is over, false otherwise
   */
  boolean isGameOver();

  /**
   * Retrieves the winner of the game, or {@code null} if there is no winner. If the game is not
   * over, returns {@code null}.
   *
   * @return the winner, or null if there is no winner
   */
  Player getWinner();

  /**
   * Resets the game board to its initial state, clearing all discs.
   */
  void resetBoard();

  /**
   * Retrieves the current state of the game board. This method is useful
   * for the View component to display the current game status.
   *
   * @return a 2D array representing the current state of the board
   */
  Player[][] getBoardState();
}
