package connect;

/**
 * Represents a Controller for Connect Four: handle user moves by executing them
 * using the model; convey move outcomes to the user in some form.
 */
public interface ConnectFourController {

  /**
   * Execute a single game of Connect Four given a Connect Four Model. When the game
   * is over, the playGame method ends.ww
   *
   * @param m a non-null Connect Four Model
   * @throws IllegalArgumentException if the model is null
   */
  void playGame(ConnectFourModel m) throws IllegalArgumentException;
}
