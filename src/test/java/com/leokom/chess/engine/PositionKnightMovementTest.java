package com.leokom.chess.engine;

import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 29.06.13 17:36
 */
public class PositionKnightMovementTest {
	@Test
	public void simpleMove() {
		Position position = new Position( null );
		position.add( Side.WHITE, "b1", PieceType.KNIGHT );

		final Position newPosition = position.move( "b1", "c3" );

		PositionAsserts.assertHasPiece( newPosition, PieceType.KNIGHT, Side.WHITE, "c3" );

	}
}
