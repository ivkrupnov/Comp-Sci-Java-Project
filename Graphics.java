// import java.awt.Color;
// import java.awt.Font;
// import java.awt.Graphics2D;
// import java.awt.Rectangle;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.awt.event.MouseAdapter;
// import java.awt.event.MouseEvent;
// import javax.swing.JPanel;
// import javax.swing.Timer;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Graphics extends JPanel implements ActionListener{
	private Timer t = new Timer(100, this);
	public String state;
	
	private TronPlayer p;
	private TronAI c;
	private Game game;
	private Color pColor = Color.BLUE;
	private Color cColor = Color.YELLOW;
	private Rectangle difficultyBounds;
	private Rectangle easyBounds, mediumBounds,hardBounds, backBounds;
	
	public Graphics(Game g) {
		t.start();
		state = "START";
		
		game = g;
		p = g.getPlayer();
		c = g.getAI();
		
		//add a keyListner 
		this.addKeyListener(g);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (difficultyBounds != null && difficultyBounds.contains(e.getPoint())) {
					state = "DIFFICULTY";
					difficultyBounds = null;
					repaint();
				}
				if (easyBounds != null && easyBounds.contains(e.getPoint())) {
					game.setLevelAI(2);
					easyBounds = null;
					mediumBounds = null;
					hardBounds = null;
					backBounds = null;
					state = "START";
					repaint();
				}
				if (mediumBounds != null && mediumBounds.contains(e.getPoint())) {
					game.setLevelAI(6);
					easyBounds = null;
					mediumBounds = null;
					hardBounds = null;
					backBounds = null;
					state = "START";
					repaint();
				}
				if (hardBounds != null && hardBounds.contains(e.getPoint())) {
					game.setLevelAI(12);
					easyBounds = null;
					mediumBounds = null;
					hardBounds = null;
					backBounds = null;
					state = "START";
					repaint();
				}
				if (backBounds != null && backBounds.contains(e.getPoint())) {
					easyBounds = null;
					mediumBounds = null;
					hardBounds = null;
					backBounds = null;
					state = "START";
					repaint();
				}
			}
		});
		
	}
	
	public void paintComponent(java.awt.Graphics g) {

		super.paintComponent(g);
		// Creating a 2D graphics object
		Graphics2D g2d = (Graphics2D) g;

		//Setting the background color of the game
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, Game.width * Game.dimension + 5, Game.height * Game.dimension + 5);
		
		//Creating various Fonts for the text to be displayed in the game
		Font TitleFont = new Font("Arial", Font.BOLD, 35);
		Font buttonFont = new Font("Arial", Font.PLAIN, 20);
		Font buttonBoldFont = new Font("Arial", Font.BOLD, 22);
		Font scoreTitleFont = new Font("Arial", Font.BOLD, 24);
		Font scoreFont = new Font("Arial", Font.PLAIN, 18);
		
		//In the START status, we draw our home screen with text and position it on screen.
		if(state == "START") {
			// Draw the title
			drawCenteredText(g2d, "Tron Game", TitleFont, -90);

			String difficulty = "Difficulty:" + Game.levelAI;
			// Check if the mouse is hovering over the difficulty button and change the font accordingly
			if (difficultyBounds != null && this.getMousePosition() != null && difficultyBounds.contains(this.getMousePosition())) {
				g2d.setFont(buttonBoldFont);
			} else {
				g2d.setFont(buttonFont);
			}
			g2d.drawString(difficulty, (Game.width * Game.dimension - g2d.getFontMetrics().stringWidth(difficulty)) / 2, Game.height / 2 * Game.dimension - 20);
			difficultyBounds = new Rectangle((Game.width * Game.dimension - g2d.getFontMetrics().stringWidth(difficulty)) / 2, Game.height / 2 * Game.dimension - 20 - g2d.getFontMetrics().getAscent(), g2d.getFontMetrics().stringWidth(difficulty), g2d.getFontMetrics().getHeight());

			// Draw the start message
			drawCenteredText(g2d, "Press Enter to start", buttonFont, 40);
		}
		//In status RUNNING, it draws the grid, and starts the AI and the player race
		else if(state == "RUNNING") {
			// Draw the grid
			drawGrid(g2d);

			// Draws the TronPlayer body and colors it
			drawBody(g2d, p.getBody(), pColor);
				
			// Draws the TronAI body and colors it
			drawBody(g2d, c.getBody(), cColor);

			// Draw the score during the gameplay and updates it as the player and AI move in grid.
			drawScore(g2d, scoreFont);
		}

		//In DIFFICULTY status, it loads the difficulty screen with various text options such as Easy Medium and Hard to choose from, as well as a back option.
		else if (state.equals("DIFFICULTY")) {
			g2d.setColor(Color.white);
			g2d.setFont(buttonFont);

			// Menu items configuration
			String[] menuItems = {"Easy", "Medium", "Hard", "Back"};
			int baseY = Game.height / 2 * Game.dimension - 20;
			int ySpacing = 40;

			// Helper method to check mouse hover
			Point mousePos = this.getMousePosition();

			// Draw each menu item
			for (int i = 0; i < menuItems.length; i++) {
				String item = menuItems[i];
				int y = baseY + (i * ySpacing);
				
				// Adjust y position for "Back" button
				if (i == 3) y += 30;

				// Calculate x position
				int x = (Game.width * Game.dimension - g2d.getFontMetrics().stringWidth(item)) / 2;
				
				// Create bounds rectangle
				Rectangle bounds = new Rectangle(x, y - g2d.getFontMetrics().getAscent(), g2d.getFontMetrics().stringWidth(item), g2d.getFontMetrics().getHeight());

				// Set font based on hover state
				if (bounds != null && mousePos != null && bounds.contains(mousePos)) {
					g2d.setFont(buttonBoldFont);
				} else {
					g2d.setFont(buttonFont);
				}

				// Calculate nex x position
				x = (Game.width * Game.dimension - g2d.getFontMetrics().stringWidth(item)) / 2;
				
				// Draw the text
				g2d.drawString(item, x, y);
				
				// Store bounds in appropriate variable
				switch(i) {
					case 0: easyBounds = bounds; break;
					case 1: mediumBounds = bounds; break;
					case 2: hardBounds = bounds; break;
					case 3: backBounds = bounds; break;
				}
			}
		}
		//In END status, it displays the end screen with the winner and the score of the player and AI.
		else {
			String winner = "You Won!";
			if(c.getBody().size() > p.getBody().size()) {
				winner = "AI Won!";
			}
			drawCenteredText(g2d, winner, scoreTitleFont, -60);
			drawCenteredText(g2d, "AI Score: " + (c.getBody().size()), scoreFont, -20);
			drawCenteredText(g2d, "Your Score: " + (p.getBody().size()), scoreFont, 20);
			drawCenteredText(g2d, "Press Enter to Continue", buttonFont, 80);
		}
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
		for(Rectangle bodyPart : body) {
			if(bodyPart == bodyHead) {
				// Darken the head
				g2d.setColor(bodyColor.darker().darker());
			} 
			g2d.fill(bodyPart);
			g2d.setColor(Color.BLACK);
			g2d.draw(bodyPart);
			g2d.setColor(bodyColor);
		}
    }

	// Helper method to draw centered text
	private void drawCenteredText(Graphics2D g2d, String text, Font font, int yOffest) {
        g2d.setFont(font);
        g2d.setColor(Color.white);
        int x = (Game.width * Game.dimension - g2d.getFontMetrics().stringWidth(text)) / 2;
		int yBase = Game.height / 2 * Game.dimension;
        g2d.drawString(text, x, yBase + yOffest);
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
