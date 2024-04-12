import connect.ConnectFourGuiController;
import connect.ConnectFourGuiView;
import connect.ConnectFourModelImpl;
import javax.swing.SwingUtilities;

/**
 * Entry point for the Connect Four game application using MVC pattern.
 * Initializes and starts the game.
 * Uses:
 * - {@link ConnectFourModelImpl} for game logic.
 * - {@link ConnectFourGuiView} for GUI display.
 * - {@link ConnectFourGuiController} for handling user interactions.
 */
public class ConnectFourApp {
  /**
   * Launches the Connect Four game application.
   * Creates the game model, view, and controller,
   * and starts the game in the Swing event dispatch
   * thread for UI thread safety.
   *
   * @param args not used
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      ConnectFourModelImpl model = new ConnectFourModelImpl(6, 7);
      ConnectFourGuiView view = new ConnectFourGuiView(model);
      ConnectFourGuiController controller = new ConnectFourGuiController(model, view);
      controller.startGame();
    });
  }
}
