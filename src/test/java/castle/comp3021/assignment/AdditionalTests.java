package castle.comp3021.assignment;

import castle.comp3021.assignment.mock.MockPiece;
import castle.comp3021.assignment.mock.MockPlayer;
import castle.comp3021.assignment.piece.Archer;
import castle.comp3021.assignment.piece.Knight;
import castle.comp3021.assignment.player.ConsolePlayer;
import castle.comp3021.assignment.protocol.*;
import castle.comp3021.assignment.util.Compares;
import castle.comp3021.assignment.util.SampleTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Put your additional JUnit5 tests for Bonus Task 2 in this class.
 */
public class AdditionalTests {
    private Configuration config;
    private MockPlayer player1;
    private MockPlayer player2;

    @BeforeEach
    public void setUpGame() {
        this.player1 = new MockPlayer(Color.PURPLE);
        this.player2 = new MockPlayer(Color.YELLOW);
        this.config = new Configuration(5, new Player[]{player1, player2});
    }

    /**
     * Test if the returned value of {@link Knight#getAvailableMoves(Game, Place)} contains and only contains all
     * valid moves.
     */
    @Test
    public void testKnightGetAvailableMovesBlock() {
        var knight1 = new Knight(player1);
        var knight2 = new Knight(player2);
        this.config.addInitialPiece(knight1, 0, 0);
        this.config.addInitialPiece(knight2, 0, 1);
        var game = new JesonMor(this.config);
        var moves = knight1.getAvailableMoves(game, new Place(0, 0));
        var expectedMoves = new Move[]{
                new Move(0, 0, 2, 1),
        };
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));

        moves = knight2.getAvailableMoves(game, new Place(0, 1));
        expectedMoves = new Move[]{
                new Move(0, 1, 2, 0),
                new Move(0, 1, 2, 2),
                new Move(0, 1, 1, 3),
        };
        assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    /**
     * Test if the returned value of {@link Knight#getAvailableMoves(Game, Place)} contains and only contains all
     * valid moves.
     */
    @Test
    public void testKnightGetAvailableMovesCapture() {
        var knight1 = new Knight(player1);
        var knight2 = new Knight(player2);
        this.config.addInitialPiece(knight1, 0, 0);
        this.config.addInitialPiece(knight2, 1, 2);
        var game = new JesonMor(this.config);
        var moves = knight1.getAvailableMoves(game, new Place(0, 0));
        var expectedMoves = new Move[]{
                new Move(0, 0, 1, 2),
                new Move(0, 0, 2, 1),
        };
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));

        var config = new Configuration(5, new Player[]{player1, player2}, 2);
        config.addInitialPiece(knight1, 0, 0);
        config.addInitialPiece(knight2, 1, 2);
        game = new JesonMor(config);
        moves = knight1.getAvailableMoves(game, new Place(0, 0));
        expectedMoves = new Move[]{
                new Move(0, 0, 2, 1),
        };
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    /**
     * Test if the returned value of {@link Knight#getAvailableMoves(Game, Place)} contains and only contains all
     * valid moves.
     */
    @Test
    public void testArcherGetAvailableMovesCapture() {
        var archer1 = new Archer(player1);
        var archer2 = new Archer(player2);

        this.config.addInitialPiece(archer1, 0, 0);
        this.config.addInitialPiece(archer1, 2, 0);
        this.config.addInitialPiece(archer1, 4, 0);

        this.config.addInitialPiece(archer2, 0, 2);
        this.config.addInitialPiece(archer2, 0, 4);

        var game = new JesonMor(this.config);
        var moves = archer1.getAvailableMoves(game, new Place(0, 0));
        var expectedMoves = new Move[]{
                new Move(0, 0, 0, 1),
                new Move(0, 0, 1, 0),
                new Move(0, 0, 0, 4),
        };
        assertTrue(Compares.areContentsEqual(moves, expectedMoves));

        var newConfig = new Configuration(5, new Player[]{player1,player2}, 1);
        newConfig.addInitialPiece(archer1, 0, 0);
        newConfig.addInitialPiece(archer1, 2, 0);
        newConfig.addInitialPiece(archer1, 4, 0);

        newConfig.addInitialPiece(archer2, 0, 2);
        newConfig.addInitialPiece(archer2, 0, 4);

        game = new JesonMor(newConfig);
        moves = archer1.getAvailableMoves(game, new Place(0, 0));
        expectedMoves = new Move[]{
                new Move(0, 0, 0, 1),
                new Move(0, 0, 1, 0),
        };
        assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void testWinByLeaveCentralPlaceInProtectionNumber() {
        var player1 = new MockPlayer();
        var player2 = new MockPlayer();
        var piece1 = new Knight(player1);
        var config = new Configuration(5, new Player[]{player1, player2},2);
        config.addInitialPiece(piece1, 1, 4);
        config.addInitialPiece(new Knight(player2), 3, 3);
        var game = new JesonMor(config);
        var winner = game.getWinner(player1, piece1, new Move(2, 2, 1, 4));
        assertEquals(null, winner);
    }

    @Test
    public void testWinAllOwnPiece() {
        var player1 = new MockPlayer();
        var player2 = new MockPlayer();
        var config = new Configuration(3, new Player[]{player1, player2});
        config.addInitialPiece(new Knight(player1), 0, 0);
        config.addInitialPiece(new Archer(player2), 1, 2);
        var game = new JesonMor(config);
        player1.setNextMoves(new Move[]{
                new Move(0, 0, 1, 2),
        });
        var winner = game.start();
        assertEquals(player1, winner);
        assertEquals(3, player1.getScore());
        assertEquals(0, player2.getScore());
        assertEquals(1, game.getNumMoves());
    }
    @Test
    @SampleTest
    public void testNextMove() {
        String data = "c3->c2->d1\r\nc33->c2\r\nh3->c2\r\nb1->c2\r\na1->a1\r\n11->1a1\r\na1->\r\nA1->B7\r\na1->a1a\r\nc3->c2\r\n";
        InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            var player1 = new MockPlayer(Color.PURPLE);
            var player2 = new ConsolePlayer("RandomPlayer");
            var config = new Configuration(3, new Player[]{player1, player2});
            var piece1 = new MockPiece(player1);
            var piece2 = new MockPiece(player2);
            config.addInitialPiece(piece1, 0, 0);
            config.addInitialPiece(piece2, 2, 2);
            var game = new JesonMor(config);
            var move = player2.nextMove(game, game.getAvailableMoves(player2));
            assertEquals(new Move(2, 2, 2, 1), move);
        } finally {
            System.setIn(stdin);
        }
    }
}
