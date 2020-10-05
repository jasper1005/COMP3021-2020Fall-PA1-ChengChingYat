package castle.comp3021.assignment.player;

import castle.comp3021.assignment.piece.Archer;
import castle.comp3021.assignment.piece.Knight;
import castle.comp3021.assignment.protocol.*;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;

/**
 * The player that makes move according to user input from console.
 */
public class ConsolePlayer extends Player {

    private final static String INCORRECT_FORMAT = "[Invalid Move]: Incorrect format";
    private final static String SAME_PLAYER = "[Invalid Move]: piece cannot be captured by another piece belonging to the same player";
    private final static String VIOLATED_KNIGHT = "[Invalid Move]: knight move rule is violated";
    private final static String VIOLATED_ARCHER = "[Invalid Move]: archer move rule is violated";
    private final static String NOT_ALLOWED_CAPTURE = "[Invalid Move]: Capturing piece in the first 5 moves are not allowed";
    private final static String NO_PIECE = "[Invalid Move]: No piece at s(%d, %d)";
    private final static String OPPONENT_PIECE = "[Invalid Move]: Cannot move a piece not belonging to you";

    public ConsolePlayer(String name, Color color) {
        super(name, color);
    }

    public ConsolePlayer(String name) {
        this(name, Color.GREEN);
    }

    /**
     * Choose a move from available moves.
     * This method will be called by {@link Game} object to get the move that the player wants to make when it is the
     * player's turn.
     * <p>
     * {@link ConsolePlayer} returns a move according to user's input in the console.
     * The console input format should conform the format described in the assignment description.
     * (e.g. {@literal a1->b3} means move the {@link Piece} at {@link Place}(x=0,y=0) to {@link Place}(x=1,y=2))
     * Note that in the {@link Game}.board, the index starts from 0 in both x and y dimension, while in the console
     * display, x dimension index starts from 'a' and y dimension index starts from 1.
     * <p>
     * Hint: be sure to handle invalid input to avoid invalid {@link Move}s.
     * <p>
     * <strong>Attention: Student should make sure the {@link Move} returned is valid.</strong>
     * <p>
     * <strong>Attention: {@link Place} object uses integer as index of x and y-axis, both starting from 0 to
     * facilitate programming.
     * This is VERY different from the coordinate used in console display.</strong>
     *
     * @param game           the current game object
     * @param availableMoves available moves for this player to choose from.
     * @return the chosen move
     */
    @Override
    public @NotNull Move nextMove(Game game, Move[] availableMoves) {
        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.printf("[%s] Make a Move: ", name);
            String input = sc.nextLine();
            var words = input.split("->");
            if(words.length != 2) {
                System.out.println(INCORRECT_FORMAT);
                continue;
            }

            words[0] = words[0].trim();
            words[1] = words[1].trim();
            Place source = toPlace(game, words[0].trim());
            Place dest = toPlace(game, words[1].trim());
            if(source == null || dest == null) {
                System.out.println(INCORRECT_FORMAT);
                continue;
            }

            var move = new Move(source,dest);
            if(containMove(availableMoves, move)) {
                return move;
            }

            var piece = game.getPiece(source.x(),source.y());
            if(piece == null) {
                System.out.printf(NO_PIECE+"\n",source.x(),source.y());
                continue;
            } else if (piece.getPlayer() != this) {
                System.out.println(OPPONENT_PIECE);
                continue;
            }

            piece = game.getPiece(dest.x(),dest.y());
            if(piece != null && piece.getPlayer() == this) {
                System.out.println(SAME_PLAYER);
                continue;
            } else if(piece == null) {
                if(piece instanceof Archer)
                    System.out.println(VIOLATED_ARCHER);
                else if(piece instanceof Knight)
                    System.out.println(VIOLATED_KNIGHT);
                continue;
            }
        }
    }

    private boolean containMove(Move[] availableMoves, Move move) {
        for (var m: availableMoves) {
            if(m.equals(move))
                return true;
        }
        return false;
    }

    private Place toPlace(Game game, String word) {
        if(word == "")
            return null;

        char ch = word.charAt(0);
        if(!(ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z'))
            return null;

        int size = game.getConfiguration().getSize();
        if(ch >= 'A' && ch <= 'Z' && ch - 'A' >= size ||
                ch >= 'a' && ch <= 'z' && ch - 'a' >= size)
            return null;



        int x,y;
        if(ch >= 'A' && ch <= 'Z')
            x = ch - 'A';
        else
            x = ch - 'a';

        try {
            y = Integer.parseInt(word.substring(1));
            --y;
            if(y < 0 || y >= size)
                return null;
        } catch (Exception e) {
            return null;
        }

        return new Place(x,y);
    }
}
