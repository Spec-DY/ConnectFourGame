package connect;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The GUI view for the Connect Four game, responsible for presenting the game state to the user
 * and providing interactive components to accept user actions.
 */
public class ConnectFourGuiView extends JFrame {
  private final ConnectFourModelImpl model;
  private final JLabel[][] boardCells = new JLabel[6][7]; //  6x7 board
  private JPanel buttonPanel;
  private JLabel statusLabel; // Status label to display messages
  private JButton resetButton; // reset game

  /**
   * Constructs a new game view frame.
   *
   * @param model The game model which contains the state of the Connect Four game.
   */
  public ConnectFourGuiView(ConnectFourModelImpl model) {
    this.model = model;
    initializeUi();
    initializeStatusPanel();
    setResizable(false);
  }

  /**
   * Initializes the user interface, creating the layout, the board grid, and the column buttons.
   */
  private void initializeUi() {
    setTitle("Connect Four");
    setSize(700, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    buttonPanel = new JPanel(new GridLayout(1, 7)); // Initialize the button panel
    for (int i = 0; i < 7; i++) {
      JButton button = new JButton("Column " + (i + 1));
      button.setActionCommand(Integer.toString(i));
      buttonPanel.add(button);
    }
    add(buttonPanel, BorderLayout.NORTH);

    JPanel boardPanel = new JPanel(new GridLayout(6, 7)); // Create a 6x7 board
    for (int row = 0; row < 6; row++) {
      for (int col = 0; col < 7; col++) {
        JLabel cellLabel = new JLabel();
        cellLabel.setHorizontalAlignment(JLabel.CENTER);
        cellLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cellLabel.setOpaque(true);
        cellLabel.setBackground(Color.WHITE);
        boardCells[row][col] = cellLabel;
        boardPanel.add(cellLabel);
      }
    }
    add(boardPanel, BorderLayout.CENTER);
    setVisible(true);
  }

  /**
   * Initializes the status panel which includes the game status label and the reset button.
   */
  private void initializeStatusPanel() {
    JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    statusLabel = new JLabel("Red's Turn");
    statusPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    resetButton = new JButton("Reset Game");
    resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    statusPanel.add(statusLabel);
    statusPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    statusPanel.add(resetButton);

    add(statusPanel, BorderLayout.SOUTH);
  }

  /**
   * Updates the board view to reflect the current state of the game model.
   */
  public void updateBoard() {
    Player[][] boardState = model.getBoardState();
    for (int row = 0; row < 6; row++) {
      for (int col = 0; col < 7; col++) {
        if (boardState[row][col] != null) {
          boardCells[row][col].setBackground(boardState[row][col]
              == Player.RED ? Color.RED : Color.YELLOW);
        } else {
          boardCells[row][col].setBackground(Color.WHITE);
        }
      }
    }
    revalidate();
    repaint();
  }

  /**
   * Registers an ActionListener with the reset button.
   *
   * @param listener The ActionListener to be invoked when the reset button is pressed.
   */
  public void setResetButtonListener(ActionListener listener) {
    resetButton.addActionListener(listener);
  }

  /**
   * Registers a Consumer with each column button that accepts the column index as an input.
   *
   * @param listener The Consumer to be invoked with the
   *                 index of the column when a column button is pressed.
   */
  public void setColumnButtonListener(Consumer<Integer> listener) {
    for (Component comp : buttonPanel.getComponents()) {
      if (comp instanceof JButton) {
        ((JButton) comp).addActionListener(
            e -> listener.accept(Integer.parseInt(e.getActionCommand())));
      }
    }
  }

  /**
   * Updates the status label with the given message.
   *
   * @param message The message to be displayed on the status label.
   */
  public void updateStatus(String message) {
    statusLabel.setText(message);
  }
}
