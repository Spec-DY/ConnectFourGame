package connect;

import java.io.IOException;
import java.util.Scanner;

/**
 * Implements the controller for a Connect Four game, handling user interaction
 * through the console. It mediates between the game model and the view, processing
 * user inputs to play the game and displaying the game state and outcomes.
 */
public class ConnectFourConsoleController implements ConnectFourController {
  private final Scanner scanner;
  private final ConnectFourView view;

  /**
   * Constructs a controller with the given input source and game view.
   *
   * @param in   The source from which to read user input. Must not be null.
   * @param view The game view to display messages and game state. Must not be null.
   * @throws IllegalArgumentException if either argument is null.
   */
  public ConnectFourConsoleController(Readable in, ConnectFourView view) {
    if (in == null || view == null) {
      throw new IllegalArgumentException("Input stream and view cannot be null");
    }
    this.scanner = new Scanner(in);
    this.view = view;
  }

  /**
   * Starts and manages a game session. Controls the game loop, prompting for and processing
   * user moves until the game is over. It handles invalid inputs and allows players to quit
   * or play again.
   *
   * @param m The game model, representing the state and logic of the Connect Four game.
   * @throws IllegalArgumentException if the game model is null.
   */
  @Override
  public void playGame(ConnectFourModel m) throws IllegalArgumentException {
    if (m == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }

    try {
      while (!m.isGameOver()) {
        view.displayGameState(m.toString());
        view.displayPlayerTurn(m.getTurn().getDisplayName());

        String input = scanner.next();

        // q to quit the game
        if ("q".equalsIgnoreCase(input)) {
          view.displayGameQuit(m.toString());
          return;
        }

        try {
          int column = Integer.parseInt(input) - 1; // change to 1 indexed
          m.makeMove(column);
        } catch (NumberFormatException e) {
          view.displayInvalidNumber(input);
        } catch (IllegalArgumentException e) {
          view.displayErrorMessage(e.getMessage());
        }
      }

      // Display final game state and winner
      view.displayGameState(m.toString());
      if (m.getWinner() != null) {
        view.displayGameOver(m.getWinner().getDisplayName());
      } else {
        view.displayGameOver(null);
      }

      // Ask if play again
      view.askPlayAgain();
      if (scanner.next().equalsIgnoreCase("yes")) {
        m.resetBoard();
        playGame(m); // Recursively start a new game
      }
    } catch (IOException e) {
      System.err.println("An error occurred while displaying the game: " + e.getMessage());
    }
  }
}
