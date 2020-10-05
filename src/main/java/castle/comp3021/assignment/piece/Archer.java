package castle.comp3021.assignment.piece;

import castle.comp3021.assignment.protocol.Game;
import castle.comp3021.assignment.protocol.Move;
import castle.comp3021.assignment.protocol.Piece;
import castle.comp3021.assignment.protocol.Place;
import castle.comp3021.assignment.protocol.Player;

import java.util.ArrayList;

/**
 * Archer piece that moves similar to cannon in chinese chess.
 * Rules of move of Archer can be found in wikipedia (https://en.wikipedia.org/wiki/Xiangqi#Cannon).
 * <p>
 * <strong>Attention: If you want to implement Archer as the bonus task, you should remove "{@code throw new
 * UnsupportedOperationException();}" in the constructor of this class.</strong>
 *
 * @see <a href='https://en.wikipedia.org/wiki/Xiangqi#Cannon'>Wikipedia</a>
 */
public class Archer extends Piece {
    public Archer(Player player) {
        super(player);
    }

    @Override
    public char getLabel() {
        return 'A';
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
        var size = game.getConfiguration().getSize();
        var canCapture  = game.getConfiguration().getNumMovesProtection() <= game.getNumMoves();
        var ret = new ArrayList<Move>();
        int newX,newY;
        boolean canLeft, canRight, canUp, canDown;

        canLeft = true;
        canRight = true;
        canUp = true;
        canDown = true;
        for(int i = 1;i < size;++i) {
            if(canRight) {
                newX = source.x() + i;
                newY = source.y();
                if (legalPlace(size, newX, newY) && game.getPiece(newX, newY) == null)
                    ret.add(new Move(source, newX, newY));
                else {
                    canRight = false;
                    if(canCapture) {
                        ++newX;
                        while(legalPlace(size, newX, newY) && game.getPiece(newX,newY) == null) {
                            ++newX;
                        }
                        if(legalPlace(size,newX,newY) && game.getPiece(newX,newY).getPlayer() != this.getPlayer()) {
                            ret.add(new Move(source,newX,newY));
                        }
                    }
                }
            }

            if(canUp) {
                newX = source.x();
                newY = source.y() + i;
                if (legalPlace(size, newX, newY) && game.getPiece(newX, newY) == null)
                    ret.add(new Move(source, newX, newY));
                else {
                    canUp = false;
                    if(canCapture) {
                        ++newY;
                        while(legalPlace(size, newX, newY) && game.getPiece(newX,newY) == null) {
                            ++newY;
                        }
                        if(legalPlace(size,newX,newY) && game.getPiece(newX,newY).getPlayer() != this.getPlayer()) {
                            ret.add(new Move(source,newX,newY));
                        }
                    }
                }
            }

            if(canLeft) {
                newX = source.x() - i;
                newY = source.y();
                if (legalPlace(size, newX, newY) && game.getPiece(newX, newY) == null)
                    ret.add(new Move(source, newX, newY));
                else {
                    canLeft = false;
                    if(canCapture) {
                        --newX;
                        while (legalPlace(size, newX, newY) && game.getPiece(newX, newY) == null) {
                            --newX;
                        }
                        if (legalPlace(size, newX, newY) && game.getPiece(newX, newY).getPlayer() != this.getPlayer()) {
                            ret.add(new Move(source, newX, newY));
                        }
                    }
                }
            }

            if(canDown) {
                newX = source.x();
                newY = source.y() - i;
                if (legalPlace(size, newX, newY) && game.getPiece(newX, newY) == null)
                    ret.add(new Move(source, newX, newY));
                else {
                    canDown = false;
                    if(canCapture) {
                        --newY;
                        while (legalPlace(size, newX, newY) && game.getPiece(newX, newY) == null) {
                            --newY;
                        }
                        if (legalPlace(size, newX, newY) && game.getPiece(newX, newY).getPlayer() != this.getPlayer()) {
                            ret.add(new Move(source, newX, newY));
                        }
                    }
                }
            }
        }
        return ret.toArray(Move[]::new);
    }

    private boolean legalPlace(int size, int newX, int newY) {
        return newX >= 0 && newX < size && newY >= 0 && newY < size;
    }
}
