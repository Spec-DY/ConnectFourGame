package connect;

import java.io.IOException;
import java.util.Objects;

/**
 * View component in the MVC architecture of a Connect Four game. This class defines the core
 * functionalities required to display the game state and messages to the user.
 */
public class ConnectFourView {

  private final Appendable out;

  /**
   * Constructor for the ConnectFourView class.
   *
   * @param out the output destination for game state and messages
   */
  public ConnectFourView(Appendable out) {
    this.out = Objects.requireNonNull(out, "Appendable can't be null");
  }

  /**
   * Displays the current state of the game board.
   *
   * @param gameState the current state of the game board
   * @throws IOException if an I/O error occurs
   */
  public void displayGameState(String gameState) throws IOException {
    out.append(gameState).append("\n");
  }

  /**
   * Displays the player whose turn it is to make a move.
   *
   * @param player the player whose turn it is
   * @throws IOException if an I/O error occurs
   */
  public void displayPlayerTurn(String player) throws IOException {
    out.append("Player ").append(player).append(", make your move: ").append("\n");
  }

  /**
   * Displays an error message for an invalid move.
   *
   * @param invalidInput the invalid input that caused the error
   * @throws IOException if an I/O error occurs
   */
  public void displayInvalidNumber(String invalidInput) throws IOException {
    out.append("Not a valid number: ").append(invalidInput).append("\n");
  }

  /**
   * Displays an error message when there is an illegal argument.
   *
   * @param message the error message
   * @throws IOException if an I/O error occurs
   */
  public void displayErrorMessage(String message) throws IOException {
    out.append(message).append("\n");
  }

  /**
   * Displays the game state when the player quits.
   *
   * @param gameState the game state when the player quits
   * @throws IOException if an I/O error occurs
   */
  public void displayGameQuit(String gameState) throws IOException {
    out.append("Game quit! Ending game state:\n").append(gameState).append("\n");
  }

  /**
   * Displays the game over message, including the winner (if there is one).
   *
   * @param winner the winner of the game, or {@code null} if there is no winner
   * @throws IOException if an I/O error occurs
   */
  public void displayGameOver(String winner) throws IOException {
    if (winner == null) {
      out.append("Game over! It's a tie!\n");
    } else {
      out.append("Game over! ").append(winner).append(" is the winner!\n");
    }
  }

  /**
   * Asks the player if they want to play again.
   *
   * @throws IOException if an I/O error occurs
   */
  public void askPlayAgain() throws IOException {
    out.append("Do you want to play again? (yes/no)\n");
  }
}