package connect;

/**
 * Enumerated type representing the possible states of a cell in the game board.
 * Each cell can be occupied by a disc belonging to either player, or be empty.
 */
public enum Player {
  RED("Red"),
  YELLOW("Yellow");

  private final String displayName;

  /**
   * Constructor for the Player enum.
   *
   * @param displayName the string representation of the player
   */
  Player(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Retrieves the string representation of the player.
   *
   * @return the string representation of the player
   */
  public String getDisplayName() {
    return displayName;
  }
}