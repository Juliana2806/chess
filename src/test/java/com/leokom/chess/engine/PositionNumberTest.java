package com.leokom.chess.engine;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PositionNumberTest {
    @Test
    public void initialPosition() {
        assertEquals( 1, Position.getInitialPosition().getMoveNumber() );
    }

    @Test
    public void nextIsStillFirst() {
        assertEquals( 1, Position.getInitialPosition().move( new Move( "e2", "e4" ) ).getMoveNumber() );
    }

    @Test
    public void twoActionsMoveToNext() {
        Position position = Position.getInitialPosition()
            .move(new Move("b1", "c3"))
            .move(new Move( "b8", "c7" ));
        assertEquals( 2, position.getMoveNumber() );
    }

    @Test
    public void pawnMovesCorrectlyCounted() {
        Position position = Position.getInitialPosition()
                .move(new Move("e2", "e4"))
                .move(new Move( "e7", "e5" ));
        assertEquals( 2, position.getMoveNumber() );
    }

    @Test
    public void drawOfferNotChangesCounter() {
        Position position = Position.getInitialPosition()
                .move(new Move("e2", "e4"))
                .move(Move.OFFER_DRAW);

        assertEquals( 1, position.getMoveNumber() );
    }

    /*
     * + Single change
     * + pawns to prove wrong base
     * + Special moves to check
     */
}
