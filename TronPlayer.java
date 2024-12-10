//Various libraries imported
import java.awt.Rectangle;
import java.util.ArrayList;

public class TronPlayer {
	private ArrayList<Rectangle> body;
	
	private String move; //NOTHING, UP, DOWN, LEFT, RIGHT
	
	//Displays the user block on screen, sets the coordinates of the block
	public TronPlayer() {
		body = new ArrayList<>();
		
		Rectangle temp = new Rectangle(Game.dimension, Game.dimension);
		temp.setLocation(Game.width / 2 * Game.dimension, Game.height / 2 * Game.dimension); // set center location
		body.add(temp);
		
		move = "NOTHING";
	}
	
	//Key movememnt based on the key pressed, changes the posiiton of the block
	public void move() {
		if(move != "NOTHING") {
			Rectangle first = body.get(0);
			
			Rectangle temp = new Rectangle(Game.dimension, Game.dimension);
			
			if(move == "UP") {
				temp.setLocation(first.x, first.y - Game.dimension);
			}
			else if(move == "DOWN") {
				temp.setLocation(first.x, first.y + Game.dimension);
			}
			else if(move == "LEFT") {
				temp.setLocation(first.x - Game.dimension, first.y);
			}
			else{
				temp.setLocation(first.x + Game.dimension, first.y);
			}
			
			body.add(0, temp);
		}
	}
	
	public ArrayList<Rectangle> getBody() {
		return body;
	}
	
	public void setBody(ArrayList<Rectangle> body) {
		this.body = body;
	}
	
	public int getX() {
		return body.get(0).x;
	}
	
	public int getY() {
		return body.get(0).y ;
	}
	
	public String getMove() {
		return move;
	}
	
	public void up() {
		move = "UP";
	}
	public void down() {
		move = "DOWN";
	}
	public void left() {
		move = "LEFT";
	}
	public void right() {
		move = "RIGHT";
	}
}
