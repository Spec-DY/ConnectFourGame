import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import connect.ConnectFourConsoleController;
import connect.ConnectFourModel;
import connect.ConnectFourView;
import connect.MockConnectFourModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;




/**
 * Tests for the ConnectFourConsoleController. These tests check that the
 * ConnectFourConsoleController correctly handles various game scenarios, including game
 * outcomes (win or tie), invalid input handling, and player actions like quitting or
 * choosing to play again.
 */
public class ConnectFourConsoleControllerTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  /**
   * Sets up the test environment before each test. This includes redirecting the
   * System.out to capture console output for verification.
   */
  @Before
  public void setUp() {
    System.setOut(new PrintStream(outContent));
  }

  /**
   * Cleans up after each test. This includes resetting the System.out to its original
   * state and optionally printing the captured output for debugging.
   */
  @After
  public void cleanUpStreams() {
    System.setOut(originalOut);
    System.out.println(outContent.toString());
  }

  /**
   * Tests that the game correctly identifies and communicates a tie scenario.
   */
  @Test
  public void testGameEndsInTie() {
    String input = "q\n";
    ConnectFourModel model =
        new MockConnectFourModel(new boolean[] {false, true}, "Game over! It's a tie!");
    ConnectFourConsoleController controller = new ConnectFourConsoleController(
        new InputStreamReader(new ByteArrayInputStream(input.getBytes())),
        new ConnectFourView(System.out)
    );
    controller.playGame(model);
    assertTrue("Expected tie message not found", outContent.toString().contains("It's a tie!"));
  }

  /**
   * Tests that the game correctly identifies and communicates a winning scenario
   * for the RED player.
   */
  @Test
  public void testGameEndingWithRedWin() {
    String input = "q\n";
    ConnectFourModel model =
        new MockConnectFourModel(new boolean[] {false, true}, "RED is the winner.");
    ConnectFourConsoleController controller = new ConnectFourConsoleController(
        new InputStreamReader(new ByteArrayInputStream(input.getBytes())),
        new ConnectFourView(System.out)
    );
    controller.playGame(model);
    assertEquals("Winner should be RED", "RED", model.getWinner().toString());
    assertTrue("Expected win message not found",
        outContent.toString().contains("RED is the winner."));
  }

  /**
   * Tests that the game correctly handles invalid number input from the user.
   */
  @Test
  public void testInputNotValidNumber() {
    String input = "wdonij\nq\n";  // some random characters
    ConnectFourModel model = new MockConnectFourModel(new boolean[] {false, false}, "");
    ConnectFourConsoleController controller = new ConnectFourConsoleController(
        new InputStreamReader(new ByteArrayInputStream(input.getBytes())),
        new ConnectFourView(System.out)
    );
    controller.playGame(model);
    assertTrue("Expected invalid input message not found",
        outContent.toString().contains("Not a valid number"));
  }

  /**
   * Tests that the game offers and correctly processes the user's choice to play again
   * after a game ends.
   */
  @Test
  public void testPlayAgainFeature() {
    String input = "q\nyes\nq\n";
    ConnectFourModel model = new MockConnectFourModel(new boolean[] {true, true}, "");
    ConnectFourConsoleController controller = new ConnectFourConsoleController(
        new InputStreamReader(new ByteArrayInputStream(input.getBytes())),
        new ConnectFourView(System.out)
    );
    controller.playGame(model);
    assertTrue("Expected play again message not found",
        outContent.toString().contains("Do you want to play again?"));
  }

  /**
   * Tests that the game correctly handles a user's decision to quit the game.
   */
  @Test
  public void testQuitGame() {
    String input = "q\n";
    ConnectFourModel model = new MockConnectFourModel(new boolean[] {false}, "");
    ConnectFourConsoleController controller = new ConnectFourConsoleController(
        new InputStreamReader(new ByteArrayInputStream(input.getBytes())),
        new ConnectFourView(System.out)
    );
    controller.playGame(model);
    assertTrue("Expected quit game message not found",
        outContent.toString().contains("Game quit!"));
  }

  /**
   * Tests that the game correctly identifies and communicates when a move is attempted
   * outside the bounds of the game board.
   */
  @Test
  public void testMoveOutsideBoard() {
    String input = "9\nq\n"; // 7 column board
    ConnectFourModel model = new MockConnectFourModel(new boolean[] {false, false}, "");
    ConnectFourConsoleController controller = new ConnectFourConsoleController(
        new InputStreamReader(new ByteArrayInputStream(input.getBytes())),
        new ConnectFourView(System.out)
    );
    controller.playGame(model);
    assertTrue("Expected out of bounds message not found",
        outContent.toString().contains("The column is out of bounds"));
  }

  /**
   * Tests that the controller throws an IllegalArgumentException
   * when constructed with invalid parameters.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidModelThrowsException() {
    new ConnectFourConsoleController(new InputStreamReader(new ByteArrayInputStream("".getBytes())),
        null);
  }
}
