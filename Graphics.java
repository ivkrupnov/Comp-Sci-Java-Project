import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
//import java.awt.GraphicsEnvironment;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Graphics extends JPanel implements ActionListener{
	private Timer t = new Timer(10, this);
	public String state;
	
	private TronPlayer p;
	private TronAI c;
	private Game game;
	
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
	}
	
	public void paintComponent(java.awt.Graphics g) {

		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, Game.width * Game.dimension + 5, Game.height * Game.dimension + 5);
		g2d.setColor(Color.gray.darker().darker().darker().darker());
		// make grid lines
		for(int i = 0; i < Game.width; i++) {
			g2d.drawLine(i * Game.dimension, 0, i * Game.dimension, Game.height * Game.dimension);
		}
		for(int i = 0; i < Game.height; i++) {
			g2d.drawLine(0, i * Game.dimension, Game.width * Game.dimension, i * Game.dimension);
		}
		
		Font snakeTitleFont = new Font("Arial", Font.BOLD, 35
		);
		Font buttonFont = new Font("Arial", Font.CENTER_BASELINE, 20);
		
		if(state == "START") {
			g2d.setColor(Color.white);
			g2d.drawString("Snake Game", Game.width/2 * Game.dimension - 10, Game.height/2 * Game.dimension -90);
			g2d.setFont(snakeTitleFont);
			
			g2d.drawString("Press Any Key to start", Game.width/2 * Game.dimension - 40, Game.height / 2 * Game.dimension - 20);
			g2d.setFont(buttonFont);

			g2d.drawString("Difficulty", Game.width/2 * Game.dimension, Game.height / 2 * Game.dimension + 20);
			g2d.setFont(buttonFont);
		}
		else if(state == "RUNNING") {
			Rectangle pHead = p.getBody().get(0);
			g2d.setColor(Color.GREEN);
			for(Rectangle r : p.getBody()) {
				if(r == pHead) {
					g2d.setColor(Color.GREEN.darker().darker());
				}
				g2d.fill(r);
				g2d.setColor(Color.black);
				g2d.draw(r);
				g2d.setColor(Color.GREEN);
			}

				

			Rectangle cHead = c.getBody().get(0);
			g2d.setColor(Color.YELLOW);
			for(Rectangle r : c.getBody()) {
				if(r == cHead) {
					g2d.setColor(Color.YELLOW.darker().darker());
				} 
				g2d.fill(r);
				g2d.setColor(Color.black);
				g2d.draw(r);
				g2d.setColor(Color.YELLOW);
				
			}
		}
		else {
			g2d.setColor(Color.white);
			// check who wins
			if(c.getBody().size() - 3 > p.getBody().size() - 3) {
				g2d.drawString("AI Wins", Game.width/2 * Game.dimension - 40, Game.height / 2 * Game.dimension - 60);
			}
			else {
				g2d.drawString("You Win", Game.width/2 * Game.dimension - 40, Game.height / 2 * Game.dimension - 60);
			}
			g2d.drawString("AI Score: " + (c.getBody().size() - 3), Game.width/2 * Game.dimension - 40, Game.height / 2 * Game.dimension - 40);
			g2d.drawString("Your Score: " + (p.getBody().size() - 3), Game.width/2 * Game.dimension - 40, Game.height / 2 * Game.dimension - 20);
		}
	}

	public void updateGameComponenets(TronPlayer newPlayer, TronAI newComputer) {
		this.p = newPlayer;
		this.c = newComputer;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
		game.update();
	}
	
}
