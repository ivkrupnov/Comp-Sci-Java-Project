
//Importing various libraries
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

//Main class that starts the game
public class Game implements KeyListener {

	// Variables for the player character, AI character, drawing the game, and game
	// window.
	private TronPlayer player;
	private TronAI computer;
	private Graphics graphics;
	private JFrame window;

	// Sets the game resolution, game width and height, size of each title, and
	// difficulty level of AI.
	public static final int width = 40;
	public static final int height = 40;
	public static final int dimension = 20;
	private int levelAI = 2; // default level

	// Main method constuctor that intializes the game.
	public Game() {
		// Creates game window, TronPlayer, TronAI, and Graphics.
		window = new JFrame();
		player = new TronPlayer();
		computer = new TronAI();
		graphics = new Graphics(this);

		window.add(graphics);

		// Sets up the game window,
		window.setTitle("Tron Game");
		window.setSize(width * dimension + 16, height * dimension + dimension + 20);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// Start method that starts the game and updates the game status
	public void start() {
		graphics.state = "RUNNING";
	}

	// Update method that updates the game status
	public void update() {
		if (graphics.state == "RUNNING") {
			computer.move(player.getHeadX(), player.getHeadY(), player.getMove(), player.getBody(), levelAI);

			if (!check_wall_collision() && !check_self_collision() && !check_ai_collision()) {
				player.move();
			} else {
				graphics.state = "END";
			}

		}
	}

	// Method that checks if the player collides with the wall
	private boolean check_wall_collision() {
		if (player.getHeadX() < 0 || player.getHeadX() >= width * dimension
				|| player.getHeadY() < 0 || player.getHeadY() >= height * dimension) {
			return true;
		}
		return false;
	}

	// Method that checks if the player collides with the AI
	private boolean check_ai_collision() {
		for (int i = 0; i < computer.getBody().size(); i++) {
			if (player.getHeadX() == computer.getBody().get(i).x
					&& player.getHeadY() == computer.getBody().get(i).y) {
				return true;
			}
		}
		return false;
	}

	// Method that checks if the player collides with itself
	private boolean check_self_collision() {
		for (int i = 1; i < player.getBody().size(); i++) {
			if (player.getHeadX() == player.getBody().get(i).x &&
					player.getHeadY() == player.getBody().get(i).y) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {

		int keyCode = e.getKeyCode();

		if (graphics.state == "RUNNING") {
			if (keyCode == KeyEvent.VK_W && player.getMove() != "DOWN") {
				player.setMoveUp();
			}

			if (keyCode == KeyEvent.VK_S && player.getMove() != "UP") {
				player.setMoveDown();
			}

			if (keyCode == KeyEvent.VK_A && player.getMove() != "RIGHT") {
				player.setMoveLeft();
			}

			if (keyCode == KeyEvent.VK_D && player.getMove() != "LEFT") {
				player.setMoveRight();
			}

			if (keyCode == KeyEvent.VK_ESCAPE) {
				resetGame();
			}
		} else if (graphics.state.equals("END") && keyCode == KeyEvent.VK_ENTER) {
			resetGame();
		} else if (graphics.state.equals("START") && keyCode == KeyEvent.VK_ENTER) {
			start();
		} else if (graphics.state.equals("START") && keyCode == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

	// Method that resets the game
	public void resetGame() {
		player = new TronPlayer();
		computer = new TronAI();
		graphics.updateGameComponents(player, computer);
		graphics.state = "START";
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	public TronPlayer getPlayer() {
		return player;
	}

	public TronAI getAI() {
		return computer;
	}

	public void setLevelAI(int level) {
		levelAI = level;
	}

	public int getLevelAI() {
		return levelAI;
	}
}
