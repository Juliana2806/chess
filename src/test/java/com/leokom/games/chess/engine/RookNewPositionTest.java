package com.leokom.games.chess.engine;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Author: Leonid
 * Date-time: 04.07.13 22:34
 */
public class RookNewPositionTest {
	@Test
	public void moveRook() {
		Position position = new Position( Side.BLACK );
		position.add( Side.WHITE, "a1", PieceType.QUEEN );
		position.add( Side.BLACK, "a8", PieceType.ROOK );

		final Position captured = position.move( "a8", "a1" );
		assertTrue( captured.hasPiece( Side.BLACK, "a1", PieceType.ROOK ) );
	}
}
