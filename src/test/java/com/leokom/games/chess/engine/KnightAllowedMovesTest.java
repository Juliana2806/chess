package com.leokom.games.chess.engine;

import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 21.05.13 22:07
 */
public class KnightAllowedMovesTest {
	@Test
	public void knightMovesSquare() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "a1", PieceType.KNIGHT );

		PositionAsserts.assertAllowedMoves(
			position, "a1", "b3", "c2" );
	}

	@Test
	public void cannotMoveIfOccupiedByMyColour() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "a1", PieceType.KNIGHT );

		position.add( Side.WHITE, "b3", PieceType.PAWN ); //any!

		PositionAsserts.assertAllowedMoves(
				position, "a1", "c2" );
	}

	@Test
	public void cannotMoveIfOccupiedByMyColourTwo() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.BLACK, "a1", PieceType.KNIGHT );

		position.add( Side.BLACK, "b3", PieceType.PAWN ); //any!
		position.add( Side.BLACK, "c2", PieceType.KNIGHT ); //any!

		PositionAsserts.assertAllowedMoves( position, "a1" );
	}

	@Test
	public void canCapture() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.BLACK, "a1", PieceType.KNIGHT );

		position.add( Side.WHITE, "b3", PieceType.PAWN ); //any except king


		PositionAsserts.assertAllowedMoves( position, "a1", "b3", "c2" );
	}

	@Test
	public void knightMovesAnotherSquare() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.BLACK, "h1", PieceType.KNIGHT );

		PositionAsserts.assertAllowedMoves(
				position, "h1", "g3", "f2" );
	}

	@Test
	public void anotherRank() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "a8", PieceType.KNIGHT );

		PositionAsserts.assertAllowedMoves(
				position, "a8", "b6", "c7" );
	}

	@Test
	public void threeDestinations() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.BLACK, "b1", PieceType.KNIGHT );

		PositionAsserts.assertAllowedMoves(
				position, "b1", "a3", "c3", "d2" );
	}

	@Test
	public void eightDestinations() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "f5", PieceType.KNIGHT );

		PositionAsserts.assertAllowedMoves(
				position, "f5",
				"h6", "h4", "g3", "e3", "d4", "d6", "e7", "g7" );
	}
}
