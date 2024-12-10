//Various libraries
import java.awt.Rectangle;
import java.util.ArrayList;

//Various variable for the AI to keep track of movement and its best move in the game, as well as the directions
public class TronAI {
    private ArrayList<Rectangle> body;
    private String move;
    private String bestMove;
    private static final String[] DIRECTIONS = { "UP", "DOWN", "LEFT", "RIGHT" };

    //Initializing the TronAI
    public TronAI() {
        body = new ArrayList<>();
        Rectangle initialPosition = new Rectangle(Game.dimension, Game.dimension);
        initialPosition.setLocation(Game.width / 3 * Game.dimension, Game.height / 2 * Game.dimension);
        body.add(initialPosition);
        move = "NOTHING";
        bestMove = "NOTHING";
    }

    //AI checks for the best move possible and moves the block position
    public void move(int playerX, int playerY, String playerMove, ArrayList<Rectangle> playerBody, int depth) {
        if (!playerMove.equals("NOTHING")) {
            Rectangle head = body.get(0);

            bestMove = findBestMove(head, body, playerBody, depth);
            if (!bestMove.equals("NOTHING")) {
                Rectangle newHead = moveInDirection(head, bestMove);
                body.add(0, newHead);
            }
        }
    }

    private String findBestMove(Rectangle head, ArrayList<Rectangle> aiBody, ArrayList<Rectangle> playerBody,
            int depth) {

        String bestMove = "NOTHING";
        int maxSafeMoves = -1;

        for (String direction : DIRECTIONS) {
            Rectangle newHead = moveInDirection(head, direction);
            if (isValidMove(newHead.x, newHead.y, aiBody, playerBody)) {
                ArrayList<Rectangle> newAiBody = new ArrayList<>(aiBody);
                newAiBody.add(0, newHead);

                int safeMoves = countSafeMoves(newHead, newAiBody, playerBody, depth - 1);
                if (safeMoves > maxSafeMoves) {
                    maxSafeMoves = safeMoves;
                    bestMove = direction;
                }
            }
        }

        return bestMove;
    }

    private int countSafeMoves(Rectangle head, ArrayList<Rectangle> aiBody, ArrayList<Rectangle> playerBody,
            int depth) {
        if (depth == 0) {
            return 1;
        }

        int safeMoves = 0;

        for (String direction : DIRECTIONS) {
            Rectangle newHead = moveInDirection(head, direction);
            if (isValidMove(newHead.x, newHead.y, aiBody, playerBody)) {
                ArrayList<Rectangle> newAiBody = new ArrayList<>(aiBody);
                newAiBody.add(0, newHead);

                safeMoves += countSafeMoves(newHead, newAiBody, playerBody, depth - 1);
            }
        }

        return safeMoves;
    }

    private boolean isValidMove(int x, int y, ArrayList<Rectangle> aiBody, ArrayList<Rectangle> playerBody) {
        // Check game boundaries
        if (x < 0 || y < 0 || x >= Game.width * Game.dimension || y >= Game.height * Game.dimension)
            return false;

        // Check collision with AI body
        for (Rectangle part : aiBody) {
            if (part.x == x && part.y == y)
                return false;
        }

        // Check collision with player body
        for (Rectangle part : playerBody) {
            if (part.x == x && part.y == y)
                return false;
        }

        return true;
    }

    private Rectangle moveInDirection(Rectangle pos, String direction) {
        Rectangle newPos = new Rectangle(Game.dimension, Game.dimension);
        switch (direction) {
            case "UP":
                newPos.setLocation(pos.x, pos.y - Game.dimension);
                break;
            case "DOWN":
                newPos.setLocation(pos.x, pos.y + Game.dimension);
                break;
            case "LEFT":
                newPos.setLocation(pos.x - Game.dimension, pos.y);
                break;
            case "RIGHT":
                newPos.setLocation(pos.x + Game.dimension, pos.y);
                break;
        }
        return newPos;
    }

    public ArrayList<Rectangle> getBody() {
        return body;
    }

    public String getMove() {
        return move;
    }
}
