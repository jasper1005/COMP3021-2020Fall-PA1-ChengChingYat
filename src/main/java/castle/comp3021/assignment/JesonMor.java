package castle.comp3021.assignment;

import castle.comp3021.assignment.piece.Archer;
import castle.comp3021.assignment.protocol.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * This class extends {@link Game}, implementing the game logic of JesonMor game.
 * Student needs to implement methods in this class to make the game work.
 * Hint: make good use of methods predefined in {@link Game} to get various information to facilitate your work.
 * <p>
 * Several sample tests are provided to test your implementation of each method in the test directory.
 * Please make make sure all tests pass before submitting the assignment.
 */
public class JesonMor extends Game {
    public JesonMor(Configuration configuration) {
        super(configuration);
    }

    /**
     * Start the game
     * Players will take turns according to the order in {@link Configuration#getPlayers()} to make a move until
     * a player wins.
     * <p>
     * In the implementation, student should implement the loop letting two players take turns to move pieces.
     * The order of the players should be consistent to the order in {@link Configuration#getPlayers()}.
     * {@link Player#nextMove(Game, Move[])} should be used to retrieve the player's choice of his next move.
     * After each move, {@link Game#refreshOutput()} should be called to refresh the gameboard printed in the console.
     * <p>
     * When a winner appears, set the local variable {@code winner} so that this method can return the winner.
     *
     * @return the winner
     */
    @Override
    public Player start() {
        // reset all things
        Player winner = null;
        this.numMoves = 0;
        this.board = configuration.getInitialBoard();
        this.currentPlayer = null;
        this.refreshOutput();
        while (true) {
            currentPlayer = getNextPlayer();

            var moves = getAvailableMoves(currentPlayer);
            if(moves.length == 0) {
                var opponent = getNextPlayer();
                if(opponent.getScore() < currentPlayer.getScore())
                    return opponent;
                return currentPlayer;
            }

            var move = currentPlayer.nextMove(this,moves);
            movePiece(move);
            ++numMoves;
            var lastPiece = board[move.getDestination().x()][move.getDestination().y()];
            updateScore(currentPlayer,lastPiece, move);
            winner = getWinner(currentPlayer, lastPiece, move);

            if(winner == null && getPieceCount(getNextPlayer()) == 0)
                winner = currentPlayer;

            refreshOutput();

            // student implementation ends here
            if (winner != null) {
                System.out.println();
                System.out.println("Congratulations! ");
                System.out.printf("Winner: %s%s%s\n", winner.getColor(), winner.getName(), Color.DEFAULT);
                return winner;
            }
        }
    }

    private int getPieceCount(Player player) {
        int count = 0;
        for(int i = 0;i<board.length;++i) {
            for(int j = 0;j<board[i].length;++j) {
                if(board[i][j] != null && board[i][j].getPlayer() == player) {
                    ++count;
                }
            }
        }
        return count;
    }

    private Player getNextPlayer() {
        if(currentPlayer == null)
            return getCurrentPlayer();
        if(currentPlayer == getPlayers()[0])
            return getPlayers()[1];
        return getPlayers()[0];
    }

    /**
     * Get the winner of the game. If there is no winner yet, return null;
     * This method will be called every time after a player makes a move and after
     * {@link JesonMor#updateScore(Player, Piece, Move)} is called, in order to
     * check whether any {@link Player} wins.
     * If this method returns a player (the winner), then the game will exit with the winner.
     * If this method returns null, next player will be asked to make a move.
     *
     * @param lastPlayer the last player who makes a move
     * @param lastMove   the last move made by lastPlayer
     * @param lastPiece  the last piece that is moved by the player
     * @return the winner if it exists, otherwise return null
     */
    @Override
    public Player getWinner(Player lastPlayer, Piece lastPiece, Move lastMove) {
        if(lastPiece instanceof Archer)
            return null;
        if(this.getNumMoves() < this.getConfiguration().getNumMovesProtection())
            return null;
        if(!lastMove.getSource().equals(this.getCentralPlace()))
            return null;
        return lastPlayer;
    }

    /**
     * Update the score of a player according to the {@link Piece} and corresponding move made by him just now.
     * This method will be called every time after a player makes a move, in order to update the corresponding score
     * of this player.
     * <p>
     * The score of a player is the cumulative score of each move he makes.
     * The score of each move is calculated with the Manhattan distance between the source and destination {@link Place}.
     * <p>
     * Student can use {@link Player#getScore()} to get the current score of a player before updating.
     * {@link Player#setScore(int)} can be used to update the score of a player.
     * <p>
     * <strong>Attention: do not need to validate move in this method.</strong>
     *
     * @param player the player who just makes a move
     * @param piece  the piece that is just moved
     * @param move   the move that is just made
     */
    public void updateScore(Player player, Piece piece, Move move) {
        var score = destiny(move.getSource(),move.getDestination());
        player.setScore(player.getScore()+score);
    }

    private int destiny(Place source, Place destination) {
        return Math.abs(source.x() - destination.x()) + Math.abs(source.y()-destination.y());
    }


    /**
     * Make a move.
     * This method performs moving a {@link Piece} from source to destination {@link Place} according {@link Move} object.
     * Note that after the move, there will be no {@link Piece} in source {@link Place}.
     * <p>
     * Positions of all {@link Piece}s on the gameboard are stored in {@link JesonMor#board} field as a 2-dimension array of
     * {@link Piece} objects.
     * The x and y coordinate of a {@link Place} on the gameboard are used as index in {@link JesonMor#board}.
     * E.g. {@code board[place.x()][place.y()]}.
     * If one {@link Place} does not have a piece on it, it will be null in {@code board[place.x()][place.y()]}.
     * Student may modify elements in {@link JesonMor#board} to implement moving a {@link Piece}.
     * The {@link Move} object can be considered valid on present gameboard.
     *
     * @param move the move to make
     */
    public void movePiece(@NotNull Move move) {
        var source = move.getSource();
        var dest = move.getDestination();
        board[dest.x()][dest.y()] = board[source.x()][source.y()];
        board[source.x()][source.y()] = null;
    }

    /**
     * Get all available moves of one player.
     * This method is called when it is the {@link Player}'s turn to make a move.
     * It will iterate all {@link Piece}s belonging to the {@link Player} on board and obtain available moves of
     * each of the {@link Piece}s through method {@link Piece#getAvailableMoves(Game, Place)} of each {@link Piece}.
     * <p>
     * <strong>Attention: Student should make sure all {@link Move}s returned are valid.</strong>
     *
     * @param player the player whose available moves to get
     * @return an array of available moves
     */
    public @NotNull Move[] getAvailableMoves(Player player) {
        var ret = new ArrayList<Move>();
        for(int i = 0;i<board.length;++i) {
            for(int j = 0;j<board[i].length;++j) {
                if(board[i][j] != null && board[i][j].getPlayer() == player) {
                    var moves = board[i][j].getAvailableMoves(this,new Place(i,j));
                    for(int k = 0;k<moves.length;++k)
                        ret.add(moves[k]);
                }
            }
        }
        return ret.toArray(Move[]::new);
    }
}
