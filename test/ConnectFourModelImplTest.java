import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import connect.ConnectFourModelImpl;
import connect.Player;
import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for ConnectFourModelImpl class which covers all functional aspects of the
 * Connect Four game logic. This includes verifying the initial state of the game, the
 * enforcement of game rules, and the detection of win conditions for both RED and YELLOW
 * players as well as draw scenarios. Exception handling and board reset functionality are
 * also tested to ensure robustness and correct reinitialization of the game state.
 */
public class ConnectFourModelImplTest {

  private ConnectFourModelImpl model;

  /**
   * Sets up the testing environment before each test.
   * Initializes a new ConnectFourModelImpl instance with a standard 6x7 board.
   */
  @Before
  public void setUp() {
    model = new ConnectFourModelImpl(6, 7);
    model.initializeBoard();
  }

  /**
   * Verifies that the winner is null at the start of the game.
   */
  @Test
  public void testWinnerIsNullAtBeginning() {
    assertNull("Winner should be null at the beginning of the game", model.getWinner());
  }

  /**
   * Verifies that creating a board smaller than the minimum 4x4 size
   * throws an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBoardSmallerThan4x4() {
    new ConnectFourModelImpl(3, 3);
  }

  /**
   * Verifies that creating a board with less than 4 rows throws an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRowSmallerThan4() {
    new ConnectFourModelImpl(2, 6);
  }

  /**
   * Verifies that creating a board with less than 4 columns throws an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testColumnSmallerThan4() {
    new ConnectFourModelImpl(7, 3);
  }

  /**
   * Tests that the correct player makes a move and the board state reflects this.
   * RED is expected to start and then YELLOW follows.
   */
  @Test
  public void testCorrectPlayerMakesMove() {
    model.makeMove(0); // RED makes the first move
    assertEquals("The cell should contain a disc from the RED player", Player.RED,
        model.getBoardState()[5][0]);
    model.makeMove(0); // YELLOW makes the next move
    assertEquals("The cell should contain a disc from the YELLOW player", Player.YELLOW,
        model.getBoardState()[4][0]);
    model.makeMove(0); // RED again
    assertEquals("The cell should contain a disc from the RED player", Player.RED,
        model.getBoardState()[3][0]);
  }

  /**
   * Verifies that making a move in a column that is out of bounds (positive side)
   * throws an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMakeMoveThrowsExceptionWhenColumnOutOfBoundsPositive() {
    model.makeMove(7);  // This board has 7 columns, valid column indexes are 0-6
  }

  /**
   * Verifies that making a move in a column that is out of bounds (negative side)
   * throws an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMakeMoveThrowsExceptionWhenColumnOutOfBoundsNegative() {
    model.makeMove(-1); // Column indexes start from 0
  }

  /**
   * Verifies that an attempt to make a move in a full column throws an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testMakeMoveThrowsExceptionWhenColumnIsFull() {
    for (int i = 0; i < 6; i++) {
      model.makeMove(0); // Fill first column to full
    }
    model.makeMove(0);
  }

  /**
   * Verifies that the current player is returned correctly after each move.
   * Also checks that the current player does not change after a column is filled.
   */
  @Test
  public void testGetTurnReturnsCorrectPlayer() {
    assertEquals("RED should be the first player", Player.RED, model.getTurn());
    model.makeMove(0); // After RED's move
    assertEquals("YELLOW should be the next player", Player.YELLOW, model.getTurn());
    for (int i = 0; i < 6; i++) {
      model.makeMove(1); // Fill second column to full
    }
    assertEquals("YELLOW should be the next player", Player.YELLOW, model.getTurn());
  }

  /**
   * Verifies that getTurn returns null when a player has won and the game is over.
   */
  @Test
  public void testGetTurnReturnsNullWhenPlayerWin() {
    // Red win
    for (int i = 0; i < 3; i++) {
      model.makeMove(1); // Fill second column to full
      model.makeMove(0);
    }
    model.makeMove(1);
    assertNull("getTurn should return null when RED wins", model.getTurn());
    System.out.println(model);
    model.resetBoard();
    // Yellow win
    for (int i = 0; i < 3; i++) {
      model.makeMove(1); // Fill second column to full
      model.makeMove(0);
    }
    model.makeMove(2);
    assertEquals("getTurn should return Yellow for now", Player.YELLOW, model.getTurn());
    model.makeMove(0);
    assertNull("getTurn should return null when Yellow wins", model.getTurn());
    System.out.println(model);
  }

  /**
   * Verifies that getTurn returns null when the game ends in a draw and no more moves can be made.
   */
  @Test
  public void testGetTurnReturnsNullWhenDraw() {
    // To display a draw like this:
    // YRYR
    // YRYR
    // RYRY
    // RYRY

    ConnectFourModelImpl board = new ConnectFourModelImpl(4, 4);
    int[] moves = {
        0, 1, 0, 1,  // Column 0 and 1 for the first row (bottom of the board array)
        2, 3, 2, 3,  // Column 2 and 3 for the second row
        1, 0, 1, 0,  // Column 1 and 0 for the third row
        3, 2, 3, 2,  // Column 3 and 2 for the fourth row (top of the board array)
    };

    for (int move : moves) {
      board.makeMove(move);
    }

    System.out.println(board);
    System.out.println(board.getWinner());   // no winner
    System.out.println(board.isGameOver());  // game is over

    assertNull("getTurn should return null when the game is over", board.getTurn());
  }

  /**
   * Verifies that the game is correctly marked as over after a horizontal win by the RED player.
   */
  @Test
  public void testIsGameOverHorizontalWinRed() {
    ConnectFourModelImpl board = new ConnectFourModelImpl(4, 4);
    board.makeMove(1);
    board.makeMove(1);
    board.makeMove(1);
    board.makeMove(1);
    board.makeMove(0);
    assertFalse("Game is not over", board.isGameOver());
    board.makeMove(3);
    board.makeMove(3);
    board.makeMove(2);
    board.makeMove(3);
    board.makeMove(3);
    board.makeMove(0);
    board.makeMove(2);
    board.makeMove(0);
    assertFalse("Game is not over", board.isGameOver());
    board.makeMove(0);
    assertFalse("Game is not over", board.isGameOver());
    board.makeMove(2);
    System.out.println(board);
    assertTrue("Game is over return true", board.isGameOver());
    assertEquals("Game over Red wins", Player.RED, board.getWinner());
  }

  /**
   * Verifies that the game is correctly marked as over after a horizontal win by the YELLOW player.
   */
  @Test
  public void testIsGameOverHorizontalWinYellow() {
    assertFalse("Game is not over", model.isGameOver());
    model.makeMove(0);
    model.makeMove(3);
    model.makeMove(0);
    model.makeMove(2);
    model.makeMove(1);
    model.makeMove(4);
    assertFalse("Game is not over", model.isGameOver());
    model.makeMove(1);
    assertFalse("Game is not over", model.isGameOver());
    model.makeMove(5);
    System.out.println(model);
    assertTrue("Game is over return true", model.isGameOver());
    assertEquals("Game over Yellow wins", Player.YELLOW, model.getWinner());
  }

  /**
   * Verifies that the game is over and the RED player is the winner after a vertical win.
   */
  @Test
  public void testIsGameOverForVerticalWinRed() {
    model.makeMove(1);
    model.makeMove(2);
    model.makeMove(1);
    model.makeMove(3);
    model.makeMove(1);
    model.makeMove(2);
    assertFalse("Game is not over", model.isGameOver());
    model.makeMove(1);

    System.out.println(model);
    assertTrue("isGameOver should return true for a vertical win for Red", model.isGameOver());
    assertEquals("Winner should be Red for a vertical win", Player.RED, model.getWinner());
  }

  /**
   * Verifies that the game is over and the YELLOW player is the winner after a vertical win.
   */
  @Test
  public void testIsGameOverForVerticalWinYellow() {
    model.makeMove(0);
    model.makeMove(1);
    model.makeMove(2);
    model.makeMove(1);
    assertFalse("Game is not over", model.isGameOver());
    model.makeMove(3);
    model.makeMove(1);
    model.makeMove(4);
    assertFalse("Game is not over", model.isGameOver());
    model.makeMove(1);
    System.out.println(model);
    assertTrue("isGameOver should return true for a vertical win for Yellow", model.isGameOver());
    assertEquals("Winner should be Yellow for a vertical win", Player.YELLOW, model.getWinner());
  }

  /**
   * Verifies that the game is over with a diagonal win
   * from top left to bottom right by the RED player.
   */
  @Test
  public void testIsGameOverDiagonalRedWinTopLeftToBottomRight() {
    // Red win : Top Left To Bottom Right
    model.makeMove(0);
    model.makeMove(1);
    model.makeMove(3);
    System.out.println(model);
    model.makeMove(1);
    model.makeMove(0);
    System.out.println(model);
    model.makeMove(0);
    model.makeMove(0);
    assertFalse("Game is not over", model.isGameOver());
    model.makeMove(2);
    model.makeMove(1);
    model.makeMove(4);
    System.out.println(model);
    model.makeMove(2);
    model.makeMove(4);
    model.makeMove(3);
    System.out.println(model);
    assertTrue("Game should be over with a diagonal line Top Left To Bottom Right",
        model.isGameOver());
    assertEquals("RED should be the winner with a diagonal line Top Left To Bottom Right",
        Player.RED, model.getWinner());
  }

  /**
   * Verifies that the game is over with a diagonal win
   * from bottom right to top left by the RED player.
   */
  @Test
  public void testIsGameOverDiagonalRedWinBottomRightToTopLeft() {
    // Red win : Bottom Right To Top Left
    model.makeMove(6);
    model.makeMove(5);
    model.makeMove(5);
    System.out.println(model);
    model.makeMove(4);
    model.makeMove(3);
    System.out.println(model);
    model.makeMove(4);
    model.makeMove(4);
    model.makeMove(3);
    System.out.println(model);
    model.makeMove(3);
    model.makeMove(0);
    model.makeMove(3);
    System.out.println(model);
    assertTrue("Game should be over with a diagonal line Bottom Right To Top Left",
        model.isGameOver());
    assertEquals("RED should be the winner with a diagonal line Bottom Right To Top Left",
        Player.RED, model.getWinner());
  }

  /**
   * Verifies that the game is over with a diagonal win
   * from top left to bottom right by the YELLOW player.
   */
  @Test
  public void testIsGameOverDiagonalYellowWinTopLeftToBottomRight() {
    // Yellow win : Top Left To Bottom Right
    model.makeMove(1);
    model.makeMove(0);
    model.makeMove(1);
    model.makeMove(3);
    System.out.println(model);
    model.makeMove(0);
    model.makeMove(0);
    System.out.println(model);
    model.makeMove(2);
    assertFalse("Game is not over", model.isGameOver());
    model.makeMove(0);
    model.makeMove(4);
    model.makeMove(1);
    System.out.println(model);
    model.makeMove(4);
    System.out.println(model);
    model.makeMove(2);
    System.out.println(model);
    assertTrue(
        "isGameOver should return true for a diagonal win for Yellow Top Left To Bottom Right",
        model.isGameOver());
    assertEquals("Winner should be Yellow for a diagonal win Top Left To Bottom Right",
        Player.YELLOW, model.getWinner());
  }

  /**
   * Verifies that the game is over with a diagonal win
   * from bottom right to top left by the YELLOW player.
   */
  @Test
  public void testIsGameOverDiagonalYellowWinBottomRightToTopLeft() {
    // Yellow win : Bottom Right To Top Left
    model.makeMove(5);
    model.makeMove(6);
    model.makeMove(4);
    System.out.println(model);
    model.makeMove(5);
    model.makeMove(4);
    System.out.println(model);
    model.makeMove(3);
    model.makeMove(3);
    model.makeMove(4);
    System.out.println(model);
    model.makeMove(0);
    model.makeMove(3);
    model.makeMove(2);
    model.makeMove(3);
    System.out.println(model);

    assertTrue(
        "isGameOver should return true for a diagonal win for Yellow Bottom Right To Top Left",
        model.isGameOver());
    assertEquals("Winner should be Yellow for a diagonal win Bottom Right To Top Left",
        Player.YELLOW, model.getWinner());
  }

  /**
   * Verifies that the game is over with a diagonal win
   * from top right to bottom left by the RED player.
   */
  @Test
  public void testIsGameOverDiagonalRedWinTopRightToBottomLeft() {
    // Red win : Top Right To Bottom Left
    model.makeMove(3);
    model.makeMove(1);
    System.out.println(model);
    model.makeMove(3);
    model.makeMove(2);
    System.out.println(model);
    model.makeMove(2);
    model.makeMove(3);
    System.out.println(model);
    model.makeMove(3);
    model.makeMove(4);
    model.makeMove(2);
    System.out.println(model);
    model.makeMove(4);
    model.makeMove(1);
    System.out.println(model);
    model.makeMove(4);
    model.makeMove(0);
    System.out.println(model);
    assertTrue("Game should be over with a diagonal line Top Right To Bottom Left",
        model.isGameOver());
    assertEquals("RED should be the winner with a diagonal line Top Right To Bottom Left",
        Player.RED, model.getWinner());
  }

  /**
   * Verifies that the game is over with a diagonal win
   * from bottom left to top right by the RED player.
   */
  @Test
  public void testIsGameOverDiagonalRedWinBottomLeftToTopRight() {
    // Red win ： Bottom Left To Top Right
    model.makeMove(1);
    model.makeMove(2);
    model.makeMove(2);
    System.out.println(model);
    model.makeMove(3);
    model.makeMove(3);
    System.out.println(model);
    model.makeMove(4);
    model.makeMove(3);
    model.makeMove(4);
    System.out.println(model);
    model.makeMove(4);
    model.makeMove(6);
    System.out.println(model);
    model.makeMove(4);
    System.out.println(model);

    assertTrue("Game should be over with a diagonal line Bottom Left To Top Right",
        model.isGameOver());
    assertEquals("RED should be the winner with a diagonal line Bottom Left To Top Right",
        Player.RED, model.getWinner());
  }

  /**
   * Verifies that the game is over with a diagonal win
   * from top right to bottom left by the YELLOW player.
   */
  @Test
  public void testIsGameOverDiagonalYellowWinTopRightToBottomLeft() {
    // Yellow win : Top Right To Bottom Left
    model.makeMove(1);
    model.makeMove(3);
    System.out.println(model);
    model.makeMove(2);
    model.makeMove(3);
    System.out.println(model);
    model.makeMove(3);
    model.makeMove(2);
    System.out.println(model);
    model.makeMove(4);
    model.makeMove(3);
    model.makeMove(4);
    System.out.println(model);
    model.makeMove(2);
    model.makeMove(4);
    System.out.println(model);
    model.makeMove(1);
    model.makeMove(6);
    model.makeMove(0);
    System.out.println(model);
    assertTrue(
        "isGameOver should return true for a diagonal win for Yellow Top Right To Bottom Left",
        model.isGameOver());
    assertEquals("Winner should be Yellow for a diagonal win Top Right To Bottom Left",
        Player.YELLOW, model.getWinner());
  }

  /**
   * Verifies that the game is over with a diagonal win
   * from bottom left to top right by the YELLOW player.
   */
  @Test
  public void testIsGameOverDiagonalYellowWinBottomLeftToTopRight() {
    // Yellow win : Bottom Left To Top Right
    model.makeMove(0);
    model.makeMove(1);
    System.out.println(model);
    model.makeMove(2);
    model.makeMove(2);
    System.out.println(model);
    assertFalse("Game is not over", model.isGameOver());
    model.makeMove(3);
    model.makeMove(3);
    System.out.println(model);
    model.makeMove(4);
    model.makeMove(3);
    model.makeMove(4);
    model.makeMove(4);
    model.makeMove(0);
    assertFalse("Game is not over", model.isGameOver());
    model.makeMove(4);
    System.out.println(model);
    assertTrue(
        "isGameOver should return true for a diagonal win for Yellow Bottom Left To Top Right",
        model.isGameOver());
    assertEquals("Winner should be Yellow for a diagonal win Bottom Left To Top Right",
        Player.YELLOW, model.getWinner());
  }

  /**
   * Tests that the game correctly ends when the board is full and there is no winner (draw).
   */
  @Test
  public void testIsGameOverWhenBoardFull() {
    // Still displaying this draw pattern:
    // YRYR
    // YRYR
    // RYRY
    // RYRY

    ConnectFourModelImpl board = new ConnectFourModelImpl(4, 4);
    int[] moves = {
        0, 1, 0, 1,  // Column 0 and 1 for the first row (bottom of the board array)
        2, 3, 2, 3,  // Column 2 and 3 for the second row
        1, 0, 1, 0,  // Column 1 and 0 for the third row
        3, 2, 3, 2,  // Column 3 and 2 for the fourth row (top of the board array)
    };

    for (int move : moves) {
      board.makeMove(move);
    }

    System.out.println(board);
    assertTrue("Game is over ", board.isGameOver());
    assertNull("Draw", board.getWinner());
  }

  /**
   * Tests that no more moves can be made after the game is over without a winner.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNoMoreMovesAfterGameOverAndNoWinner() {
    ConnectFourModelImpl board = new ConnectFourModelImpl(4, 4);
    int[] moves = {
        0, 1, 0, 1,  // Column 0 and 1 for the first row (bottom of the board array)
        2, 3, 2, 3,  // Column 2 and 3 for the second row
        1, 0, 1, 0,  // Column 1 and 0 for the third row
        3, 2, 3, 2,  // Column 3 and 2 for the fourth row (top of the board array)
    };

    for (int move : moves) {
      board.makeMove(move);
    }
    // Any moves will throw error message
    board.makeMove(0);
  }

  /**
   * Tests that the board is cleared of a single disc after the reset.
   */
  @Test
  public void testResetBoardSingleDisc() {
    model.makeMove(0); // Make a move to change the initial state
    model.resetBoard();
    assertNull("After reset, this disc disappear", model.getBoardState()[5][0]);
  }

  /**
   * Tests that the entire board is cleared after the reset.
   */
  @Test
  public void testResetBoardClearsEntireBoard() {
    // Fill up some cells to change the board's initial state
    model.makeMove(0);
    model.makeMove(1);
    model.resetBoard();

    Player[][] boardState = model.getBoardState();
    for (Player[] row : boardState) {
      for (Player cell : row) {
        assertNull("After reset, every cell in the board should be null", cell);
      }
    }
  }

  /**
   * Tests that the current player is reset to RED after the board reset.
   */
  @Test
  public void testResetBoardResetsCurrentPlayerToRed() {
    model.makeMove(0); // Red starts
    model.resetBoard();
    assertEquals("After reset, the current player should be RED", Player.RED, model.getTurn());
    model.makeMove(2);
    model.makeMove(0);
    model.makeMove(2);
    model.resetBoard();
    assertEquals("The current player become RED again", Player.RED, model.getTurn());
  }

  /**
   * Tests that the winner is cleared after the board reset.
   */
  @Test
  public void testResetBoardClearsWinner() {
    // Simulate a winning scenario for RED or YELLOW and then reset
    model.makeMove(0);
    model.makeMove(1);
    model.makeMove(0);
    model.makeMove(1);
    model.makeMove(0);
    model.makeMove(1);
    model.makeMove(0); // RED wins
    assertEquals("For now, RED is the winner", Player.RED, model.getWinner());
    model.resetBoard();
    assertNull("After reset, there is no more winner", model.getWinner());
  }

  /**
   * Tests that the size of the board remains unchanged after the board reset.
   */
  @Test
  public void testResetBoardKeepsBoardSize() {
    model.resetBoard();
    assertEquals("Board should still have 6 rows after reset", 6, model.getBoardState().length);
    assertEquals("Board should still have 7 columns after reset", 7,
        model.getBoardState()[0].length);
  }

  /**
   * Tests the toString method of ConnectFourModelImpl
   * for correct string representation of the board.
   */
  @Test
  public void testToStringMethod() {
    String initialBoardString = model.toString();
    assertTrue("Initial board should be empty",
        initialBoardString.contains("."));  // "." meaning empty
    model.makeMove(0); // RED makes a move
    String boardStringAfterMove = model.toString();
    assertTrue("Board should reflect the RED move",
        boardStringAfterMove.contains("R"));  // "R" meaning RED

    // Output wanted:
    // YRYR
    // YRYR
    // RYRY
    // RYRY
    ConnectFourModelImpl board = new ConnectFourModelImpl(4, 4);
    int[] moves = {
        0, 1, 0, 1,  // Column 0 and 1 for the first row (bottom of the board array)
        2, 3, 2, 3,  // Column 2 and 3 for the second row
        1, 0, 1, 0,  // Column 1 and 0 for the third row
        3, 2, 3, 2,  // Column 3 and 2 for the fourth row (top of the board array)
    };

    for (int move : moves) {
      board.makeMove(move);
    }

    String expectedOutput = "YRYR\n"
        + "YRYR\n"
        + "RYRY\n"
        + "RYRY\n";
    // Invoke toString and compare with the expected output
    assertEquals("Board state should match expected output", expectedOutput, board.toString());
  }
}
