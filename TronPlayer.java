//Various libraries imported
import java.awt.Rectangle;
import java.util.ArrayList;

public class TronPlayer {
	private ArrayList<Rectangle> body;
	
	private String move; //NOTHING, UP, DOWN, LEFT, RIGHT
	
	//Displays the user block on screen, sets the coordinates of the block
	public TronPlayer() {
		// create a new body
		body = new ArrayList<>();

		// set initial position
		Rectangle initialPosition = new Rectangle(Game.dimension, Game.dimension);
		// set the location of the block in the center of the screen
		initialPosition.setLocation(Game.width / 2 * Game.dimension, Game.height / 2 * Game.dimension);
		// add the block to the body
		body.add(initialPosition);
		
		move = "NOTHING";
	}
	
	//Key movememnt based on the key pressed, changes the posiiton of the block
	public void move() {
		if(move != "NOTHING") {

			// get the first block of the body
			Rectangle headPosition = body.get(0);
			
			// create a new position
			Rectangle newPosition = new Rectangle(Game.dimension, Game.dimension);
			
			// set the new position based on the move
			if(move == "UP") {
				newPosition.setLocation(headPosition.x, headPosition.y - Game.dimension);
			}
			else if(move == "DOWN") {
				newPosition.setLocation(headPosition.x, headPosition.y + Game.dimension);
			}
			else if(move == "LEFT") {
				newPosition.setLocation(headPosition.x - Game.dimension, headPosition.y);
			}
			else{
				newPosition.setLocation(headPosition.x + Game.dimension, headPosition.y);
			}
			
			// add the new position to the body
			body.add(0, newPosition);
		}
	}
	
	// Returns the body of the block
	public ArrayList<Rectangle> getBody() {
		return body;
	}
	
	// Sets the body of the block
	public void setBody(ArrayList<Rectangle> body) {
		this.body = body;
	}
	
	// Returns the x coordinate of the head block
	public int getHeadX() {
		return body.get(0).x;
	}
	
	// Returns the y coordinate of the head block
	public int getHeadY() {
		return body.get(0).y ;
	}

	// Returns the move direction of the block
		public String getMove() {
		return move;
	}
	
	// Sets UP as the move direction
	public void setMoveUp() {
		move = "UP";
	}

	// Sets DOWN as the move direction
	public void setMoveDown() {
		move = "DOWN";
	}

	// Sets LEFT as the move direction
	public void setMoveLeft() {
		move = "LEFT";
	}

	// Sets RIGHT as the move direction
	public void setMoveRight() {
		move = "RIGHT";
	}
}
