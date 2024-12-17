import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Graphics extends JPanel implements ActionListener {
	// Colors for the player and AI
	private static final Color PLAYER_COLOR = Color.BLUE;
	private static final Color AI_COLOR = Color.YELLOW;

	// Timer for the game
	private Timer t = new Timer(100, this);

	// Game components
	public String state;
	private TronPlayer p;
	private TronAI c;
	private Game game;

	// Menu bounding rectangles
	private Rectangle difficultyBounds;
	private Rectangle controlsBounds;
	private Rectangle easyBounds;
	private Rectangle mediumBounds;
	private Rectangle hardBounds;
	private Rectangle backBounds;

	// Creating various Fonts for the text to be displayed in the game
	private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 35);
	private static final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 20);
	private static final Font BUTTON_BOLD_FONT = new Font("Arial", Font.BOLD, 22);
	private static final Font SCORE_TITLE_FONT = new Font("Arial", Font.BOLD, 24);
	private static final Font SCORE_FONT = new Font("Arial", Font.PLAIN, 18);

	// Constants for game states
	private static final String STATE_START = "START";
	private static final String STATE_RUNNING = "RUNNING";
	private static final String STATE_DIFFICULTY = "DIFFICULTY";
	private static final String STATE_CONTROLS = "CONTROLS";
	private static final String STATE_END = "END";

	// Constants for the difficulty levels
	private static final int EASY_LEVEL = 2;
	private static final int MEDIUM_LEVEL = 6;
	private static final int HARD_LEVEL = 10;

	public Graphics(Game g) {
		t.start();
		state = "START";

		game = g;
		p = g.getPlayer();
		c = g.getAI();

		// add a keyListner
		this.addKeyListener(g);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Check if the mouse is hovering over the difficulty button and change the font
				if (difficultyBounds != null && difficultyBounds.contains(e.getPoint())) {
					state = "DIFFICULTY";
					difficultyBounds = null;
					controlsBounds = null;
					repaint();
				}

				// Check if the mouse is hovering over the difficulty menu buttons and change
				// the font
				if (easyBounds != null && easyBounds.contains(e.getPoint())) {
					game.setLevelAI(EASY_LEVEL);
					returnToStartMenu();
				}
				if (mediumBounds != null && mediumBounds.contains(e.getPoint())) {
					game.setLevelAI(MEDIUM_LEVEL);
					returnToStartMenu();
				}
				if (hardBounds != null && hardBounds.contains(e.getPoint())) {
					game.setLevelAI(HARD_LEVEL);
					returnToStartMenu();
				}
				if (backBounds != null && backBounds.contains(e.getPoint())) {
					returnToStartMenu();
				}

				if (controlsBounds != null && controlsBounds.contains(e.getPoint())) {
					state = "CONTROLS";
					difficultyBounds = null;
					controlsBounds = null;
					repaint();
				}
			}
		});

	}

	@Override
	public void paintComponent(java.awt.Graphics g) {

		super.paintComponent(g);
		// Creating a 2D graphics object
		Graphics2D g2d = (Graphics2D) g;

		
		drawBackground(g2d);

		// In the START status, we draw our home screen with text and position it on
		// screen.
		switch (state) {
			case STATE_START:
				// Draw the title
				drawCenteredText(g2d, "Tron Game", TITLE_FONT, -90);

				// Draw the difficulty option
				difficultyBounds = drawCenteredText(g2d, "Difficulty: " + getDifficultyString(game.getLevelAI()), getFontBasedOnHover(difficultyBounds, BUTTON_FONT, BUTTON_BOLD_FONT), -20);

				// Draw the controls option
				controlsBounds = drawCenteredText(g2d, "Controls", getFontBasedOnHover(controlsBounds, BUTTON_FONT, BUTTON_BOLD_FONT), 20);

				// Draw the start message
				drawCenteredText(g2d, "Press Enter to start", BUTTON_FONT, 80);
				break;

			// In status RUNNING, it draws the grid, and starts the AI and the player race
			case STATE_RUNNING:
				// Draw the grid
				drawGrid(g2d);

				// Draws the TronPlayer body and colors it
				drawBody(g2d, p.getBody(), PLAYER_COLOR);

				// Draws the TronAI body and colors it
				drawBody(g2d, c.getBody(), AI_COLOR);

				// Draw the score during the gameplay and updates it as the player and AI move
				// in grid.
				drawScore(g2d, SCORE_FONT);
				break;

			// In DIFFICULTY status, it loads the difficulty screen with various text
			// options such as Easy Medium and Hard to choose from, as well as a back
			// option.
			case STATE_DIFFICULTY:
				// Draw the difficulty menu screen
				drawDifficultyMenuScreen(g2d);
				break;

			// In CONTROLS status, it displays the controls screen with the controls of the game.
			case STATE_CONTROLS:
				// Draw the controls screen
				drawCenteredText(g2d, "Controls", TITLE_FONT, -90);
				drawCenteredText(g2d, "Use the W key to up", BUTTON_FONT, -20);
				drawCenteredText(g2d, "Use the S key to down", BUTTON_FONT, 0);
				drawCenteredText(g2d, "Use the A key to left", BUTTON_FONT, 20);
				drawCenteredText(g2d, "Use the D key to right", BUTTON_FONT, 40);
				drawCenteredText(g2d, "Use the Escape key to exit", BUTTON_FONT, 60);

				// Draw the back button
				backBounds = drawCenteredText(g2d, "Back", getFontBasedOnHover(backBounds, BUTTON_FONT, BUTTON_BOLD_FONT), 100);
				break;
			
			// In END status, it displays the end screen with the winner and the score of
			// the player and AI.
			case STATE_END:
				// Draw the end screen
				drawEndScreen(g2d);

				break;
		}
	}

	// Helper method to draw the difficulty screen
	private void drawDifficultyMenuScreen(Graphics2D g2d) {
		g2d.setColor(Color.WHITE);

		String[] menuItems = { "Easy", "Medium", "Hard", "Back" };
		Rectangle[] bounds = { easyBounds, mediumBounds, hardBounds, backBounds };
		int baseY = -20; // position these relative to the center of the screen
		int ySpacing = 40;

		for (int i = 0; i < menuItems.length; i++) {
			int yOffset = baseY + (i * ySpacing);
			// Extra space before "Back"
			if (i == 3) {
				yOffset += 30;
			}

			Rectangle bound = drawCenteredText(g2d, menuItems[i], getFontBasedOnHover(bounds[i], BUTTON_FONT, BUTTON_BOLD_FONT), yOffset);

			switch (i) {
				case 0:	
					easyBounds = bound;
					break;
				case 1:
					mediumBounds = bound;
					break;
				case 2:
					hardBounds = bound;
					break;
				case 3:
					backBounds = bound;
					break;
			}
		}
	}

	// Helper method to draw the end screen
	private void drawEndScreen(Graphics2D g2d) {
		String winner = "You Won!";
		if (c.getBody().size() > p.getBody().size()) {
			winner = "AI Won!";
		}
		drawCenteredText(g2d, winner, SCORE_TITLE_FONT, -60);
		drawCenteredText(g2d, "AI Score: " + c.getBody().size(), SCORE_FONT, -20);
		drawCenteredText(g2d, "Your Score: " + p.getBody().size(), SCORE_FONT, 20);
		drawCenteredText(g2d, "Press Enter to Continue", BUTTON_FONT, 80);
	}

	// Helper method to draw the background
	private void drawBackground(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, Game.width * Game.dimension + 5, Game.height * Game.dimension + 5);
	}

	// Helper method to draw the score
	private void drawScore(Graphics2D g2d, Font font) {
		g2d.setColor(Color.white);
		g2d.setFont(font);
		g2d.drawString("Player Score: " + p.getBody().size(), 10, 20);
		g2d.drawString("AI Score: " + c.getBody().size(), 10, 40);
	}

	// Helper method to draw the grid
	private void drawGrid(Graphics2D g2d) {
		g2d.setColor(Color.gray.darker().darker());
		for (int i = 0; i < Game.width; i++) {
			g2d.drawLine(i * Game.dimension, 0, i * Game.dimension, Game.height * Game.dimension);
		}
		for (int i = 0; i < Game.height; i++) {
			g2d.drawLine(0, i * Game.dimension, Game.width * Game.dimension, i * Game.dimension);
		}
	}

	// Helper method to draw the body of the player and AI
	private void drawBody(Graphics2D g2d, ArrayList<Rectangle> body, Color bodyColor) {
		Rectangle bodyHead = body.get(0);
		g2d.setColor(bodyColor);
		for (Rectangle bodyPart : body) {
			if (bodyPart == bodyHead) {
				// Darken the head
				g2d.setColor(bodyColor.darker().darker());
			}
			g2d.fill(bodyPart);
			g2d.setColor(Color.BLACK);
			g2d.draw(bodyPart);
			g2d.setColor(bodyColor);
		}
	}

	// Helper method to draw centered text and return the bounds
	private Rectangle drawCenteredText(Graphics2D g2d, String text, Font font, int yOffest) {
		g2d.setFont(font);
		g2d.setColor(Color.white);
		int x = (Game.width * Game.dimension - g2d.getFontMetrics().stringWidth(text)) / 2;
		int yBase = Game.height / 2 * Game.dimension;
		g2d.drawString(text, x, yBase + yOffest);
		return new Rectangle(x, yBase + yOffest - g2d.getFontMetrics().getAscent(),
				g2d.getFontMetrics().stringWidth(text), g2d.getFontMetrics().getHeight());
	}

	// Helper method to set the font based on hover
	private Font getFontBasedOnHover(Rectangle bounds, Font normalFont, Font hoverFont) {
		Point mousePos = this.getMousePosition();
		if (mousePos != null && bounds != null && bounds.contains(mousePos)) {
			return hoverFont;
		} else {
			return normalFont;
		}
	}

	// Helper method to reset the difficulty bounds
	private void returnToStartMenu() {
		resetDifficultyMenuBounds();
		state = STATE_START;
		repaint();
	}

	// Helper method to reset the difficulty bounds
	private void resetDifficultyMenuBounds() {
		easyBounds = null;
		mediumBounds = null;
		hardBounds = null;
		backBounds = null;
	}

	private String getDifficultyString(int levelAI) {
		switch (levelAI) {
			case EASY_LEVEL:
				return "Easy";
			case MEDIUM_LEVEL:
				return "Medium";
			case HARD_LEVEL:
				return "Hard";
			default:
				return "Unknown";
		}
	}

	// Method to update the game components
	public void updateGameComponents(TronPlayer newPlayer, TronAI newComputer) {
		this.p = newPlayer;
		this.c = newComputer;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
		game.update();
	}

}
