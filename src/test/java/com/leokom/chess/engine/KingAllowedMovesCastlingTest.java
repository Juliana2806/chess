package com.leokom.chess.engine;

import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 16.02.14 22:02
 */
public class KingAllowedMovesCastlingTest {
	@Test
	public void castlingIsAllowed() {
		Position position = new Position( null );
		position.add( Side.WHITE, "e1", PieceType.KING );

		position.add( Side.WHITE, "h1", PieceType.ROOK );

		PositionAsserts.assertAllowedMovesInclude(
				position, "e1", "g1" );
	}

	@Test
	public void castlingIsAllowedBlack() {
		Position position = new Position( null );
		position.add( Side.BLACK, "e8", PieceType.KING );

		position.add( Side.BLACK, "h8", PieceType.ROOK );

		PositionAsserts.assertAllowedMovesInclude(
				position, "e8", "g8" );
	}

	@Test
	public void castlingQueenSide() {
		Position position = new Position( null );
		position.add( Side.BLACK, "e8", PieceType.KING );

		position.add( Side.BLACK, "a8", PieceType.ROOK );

		PositionAsserts.assertAllowedMovesInclude(
				position, "e8", "c8" );
	}

	@Test
	public void rightToCastlingIsLostIfKingMoved() {
		Position position = new Position( null );
		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.WHITE, "h1", PieceType.ROOK );

		position.add( Side.BLACK, "h8", PieceType.KING );

		position.move( "e1", "e2" );
		position.move( "h8", "h7" ); //any valid black move

		position.move( "e2", "e1" );
		position.move( "h7", "h8" ); //any valid black move

		PositionAsserts.assertAllowedMovesOmit(
				position, "e1", "g1" );
	}

	//TODO: extra test: PERMANENTLY lost right to castle (check 1 more move)
	//TODO: extra test: no castling possible after castling

}
