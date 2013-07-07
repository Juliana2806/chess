package com.leokom.chess.engine;

import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 07.07.13 21:37
 */
public class KingAllowedMovesTest {
	@Test
	public void a1() {
		Position position = new Position( null );
		position.add( Side.WHITE, "a1", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position,
				"a1",
				"b1", "a2", "b2"
		);
	}
}
