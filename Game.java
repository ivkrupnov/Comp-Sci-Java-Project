//Importing various libraries
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

//Main class that starts the game
public class Game implements KeyListener {

	// Variables for the player character, AI character, drawing the game, and game window.
	private TronPlayer player;

	private TronAI computer;

	private Graphics graphics;

	private JFrame window;

	//Sets the game resolution, game width and height, size of each title, and difficulty level of AI.
	public static final int width = 40;
	public static final int height = 40;
	public static final int dimension = 20;
	public static final int difficulty = 10;

	//Main method constuctor that intializes the game.
	public Game() {
		//Creates game window, TronPlayer, TronAI, and Graphics.
		window = new JFrame();

		player = new TronPlayer();

		computer = new TronAI();

		graphics = new Graphics(this);

		window.add(graphics);

		//Sets up the game window, 
		window.setTitle("Tron Game");
		window.setSize(width * dimension + 16, height * dimension + dimension + 20);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	//Start method that starts the game and updates the game status
	public void start() {
		graphics.state = "RUNNING";
	}

	//
	public void update() {
		if (graphics.state == "RUNNING") {
			computer.move(player.getX(), player.getY(), player.getMove(), player.getBody(), difficulty);

			if (!check_wall_collision() && !check_self_collision() && !check_ai_collision()) {
				player.move();
			} else {
				graphics.state = "END";
			}
			
		}
	}

	private boolean check_wall_collision() {
		if (player.getX() < 0 || player.getX() >= width * dimension
				|| player.getY() < 0 || player.getY() >= height * dimension) {
			return true;
		}
		return false;
	}

	private boolean check_ai_collision() {
		for (int i = 0; i < computer.getBody().size(); i++) {
			if (player.getX() == computer.getBody().get(i).x
					&& player.getY() == computer.getBody().get(i).y) {
				return true;
			}
		}
		return false;
	}

	private boolean check_self_collision() {
		for (int i = 1; i < player.getBody().size(); i++) {
			if (player.getX() == player.getBody().get(i).x &&
					player.getY() == player.getBody().get(i).y) {
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
				player.up();
			}

			if (keyCode == KeyEvent.VK_S && player.getMove() != "UP") {
				player.down();
			}

			if (keyCode == KeyEvent.VK_A && player.getMove() != "RIGHT") {
				player.left();
			}

			if (keyCode == KeyEvent.VK_D && player.getMove() != "LEFT") {
				player.right();
			}
		} else if (graphics.state.equals("END") && keyCode == KeyEvent.VK_ENTER) {
			resetGame();
		} else if (graphics.state.equals("START")) {
			this.start();
		}
	}

	public void resetGame(){
		player = new TronPlayer();
		computer = new TronAI();
		graphics.updateGameComponenets(player, computer);
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

	public void setplayer(TronPlayer player) {
		this.player = player;
	}

	public JFrame getWindow() {
		return window;
	}

	public void setWindow(JFrame window) {
		this.window = window;
	}

}
