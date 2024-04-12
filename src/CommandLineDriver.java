import connect.ConnectFourConsoleController;
import connect.ConnectFourModelImpl;
import connect.ConnectFourView;
import java.io.InputStreamReader;

/**
 * Run a Connect Four game interactively on the console.
 */
public class CommandLineDriver {
  /**
   * Run a Connect Four game interactively on the console.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    Readable input = new InputStreamReader(System.in);
    Appendable output = System.out;
    ConnectFourView view = new ConnectFourView(output);
    new ConnectFourConsoleController(input, view).playGame(new ConnectFourModelImpl(6, 7));
  }
}
