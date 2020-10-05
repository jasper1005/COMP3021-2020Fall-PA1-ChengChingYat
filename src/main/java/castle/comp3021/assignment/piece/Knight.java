package castle.comp3021.assignment.piece;

import castle.comp3021.assignment.protocol.Game;
import castle.comp3021.assignment.protocol.Move;
import castle.comp3021.assignment.protocol.Piece;
import castle.comp3021.assignment.protocol.Place;
import castle.comp3021.assignment.protocol.Player;

import java.util.ArrayList;

/**
 * Knight piece that moves similar to knight in chess.
 * Rules of move of Knight can be found in wikipedia (https://en.wikipedia.org/wiki/Knight_(chess)).
 *
 * @see <a href='https://en.wikipedia.org/wiki/Knight_(chess)'>Wikipedia</a>
 */
public class Knight extends Piece {
    public Knight(Player player) {
        super(player);
    }

    @Override
    public char getLabel() {
        return 'K';
    }

    /**
     * Returns an array of moves that are valid given the current place of the piece.
     * Given the {@link Game} object and the {@link Place} that current knight piece locates, this method should
     * return ALL VALID {@link Move}s according to the current {@link Place} of this knight piece.
     * All the returned {@link Move} should have source equal to the source parameter.
     * <p>
     * Hint: you should consider corner cases when the {@link Move} is not valid on the gameboard.
     * Several tests are provided and your implementation should pass them.
     * <p>
     * <strong>Attention: Student should make sure all {@link Move}s returned are valid.</strong>
     *
     * @param game   the game object
     * @param source the current place of the piece
     * @return an array of available moves
     */
    @Override
    public Move[] getAvailableMoves(Game game, Place source) {
        if(game == null || source == null)
            return null;

        int[] xC = {-1,1};
        int[] yC = {-2,2};

        var ret = new ArrayList<Move>();
        for(int idxX = 0;idxX < xC.length;++idxX) {
            for(int idxY = 0;idxY < yC.length;++idxY) {
                int newX = source.x() + xC[idxX];
                int newY = source.y() + yC[idxY];
                ret = tryToAddMove(game, source, newX, newY, ret);

                newX = source.x() + yC[idxX];
                newY = source.y() + xC[idxY];
                ret = tryToAddMove(game, source, newX, newY, ret);
            }
        }

        return ret.toArray(Move[]::new);
    }

    private ArrayList<Move> tryToAddMove(Game game, Place source, int newX, int newY, ArrayList<Move> ret) {
        int size = game.getConfiguration().getSize();
        boolean canCapture = game.getConfiguration().getNumMovesProtection() <= game.getNumMoves();

        // not in the gameboard
        if(!legalPlace(size,newX,newY))
            return ret;

        // check if there is some piece block it

        int middleX = (source.x() + newX)/2;
        int middleY = (source.y() + newY)/2;
        if(newX -  source.x() == -1)
            ++middleX;
        if(newY - source.y() == -1)
            ++middleY;
        if(game.getPiece(middleX,middleY) != null)
            return ret;

        var destPiece = game.getPiece(newX,newY);
        if(destPiece == null) {
            ret.add(new Move(source,newX,newY));
            return  ret;
        }

        // check if can capture it
        if(destPiece.getPlayer() == this.getPlayer())
            return ret;

        if(canCapture)
            ret.add(new Move(source,newX,newY));

        return ret;
    }

    private boolean legalPlace(int size, int newX, int newY) {
        return newX >= 0 && newX < size && newY >= 0 && newY < size;
    }
}
