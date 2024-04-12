package connect;

import javax.swing.JOptionPane;

/**
 * The controller part of the MVC pattern that manages user interactions
 * for the Connect Four game.
 */
public class ConnectFourGuiController {
  private final ConnectFourModelImpl model;
  private final ConnectFourGuiView view;

  /**
   * Constructs a new game controller.
   *
   * @param model The game model which contains the game logic.
   * @param view  The game view which displays the UI.
   */
  public ConnectFourGuiController(ConnectFourModelImpl model, ConnectFourGuiView view) {
    this.model = model;
    this.view = view;
    this.view.setColumnButtonListener(this::handleColumnButtonClick);
    this.view.setResetButtonListener(e -> resetGame());

    updateStatus(); // Set initial status
  }

  /**
   * Handles a column button click, attempts to make a move in the model,
   * and updates the board. Shows error messages for invalid moves and
   * handles game over conditions.
   *
   * @param column The column number where the move is attempted.
   */
  private void handleColumnButtonClick(int column) {
    try {
      model.makeMove(column);
      view.updateBoard();
      if (model.isGameOver()) {
        handleGameOver();
      } else {
        updateStatus(); // Update status after each move
      }
    } catch (IllegalArgumentException e) {
      JOptionPane.showMessageDialog(view, e.getMessage(),
          "Invalid Move", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Handles the end of a game, showing a dialog that announces the winner
   * or a tie, and asks the player whether they want to play again.
   */
  private void handleGameOver() {
    String winnerMessage = model.getWinner() == null ? "It's a tie."
        : model.getWinner().getDisplayName() + " wins!";
    JOptionPane.showMessageDialog(view, "Game over! " + winnerMessage);
    if (askPlayAgain()) {
      model.resetBoard();
      view.updateBoard();
      updateStatus(); // Reset the status message for a new game
    } else {
      System.exit(0);
    }
  }

  /**
   * Prompts the user with a dialog to ask if they want to play again.
   *
   * @return true if the user chooses to play again, false otherwise.
   */
  private boolean askPlayAgain() {
    int result = JOptionPane.showConfirmDialog(view,
        "Play again?", "Game Over", JOptionPane.YES_NO_OPTION);
    return result == JOptionPane.YES_OPTION;
  }

  /**
   * Updates the status label on the view with the current turn or game over message.
   */
  private void updateStatus() {
    String statusMessage = model.isGameOver() ? "Game over."
        : "Current turn: " + model.getTurn().getDisplayName();
    view.updateStatus(statusMessage);
  }

  /**
   * Starts a new game by resetting the model and updating the view.
   */
  public void startGame() {
    model.resetBoard();
    view.updateBoard();
    updateStatus(); // Update status at the start of the game
  }

  /**
   * Resets the game to its initial state.
   */
  private void resetGame() {
    model.resetBoard();
    view.updateBoard();
    updateStatus(); // Reset the status message for a new game
  }
}
