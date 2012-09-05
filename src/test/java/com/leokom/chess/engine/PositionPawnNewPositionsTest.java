package com.leokom.chess.engine;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static com.leokom.chess.engine.PositionUtils.addAny;

/**
 * Generate positions by legal pawn moves using the initial position
 * Author: Leonid
 * Date-time: 31.08.12 22:05
 */
public class PositionPawnNewPositionsTest {
	private Position position;

	@Before
	public void prepare() {
		position = new Position( null );
	}

	@Test
	public void basicContractRequirements() {
		final String anyInitialSquare = "g3";
		final String anyValidSquareToMove = "g4";
		position.addPawn( Side.WHITE, anyInitialSquare );

		Position newPosition = position.move( anyInitialSquare, anyValidSquareToMove );
		assertNotNull( "New position must be not null", newPosition );
		assertNotSame( newPosition, position );
	}

	@Test
	public void singleMove() {
		final String anySquare = "c3";
		final Side side = Side.WHITE;

		final String squareToMove = "c4";

		assertPawnMovement( side, anySquare, squareToMove );
	}

	@Test
	public void singleMoveFromInitialPosition() {
		final String initialSquare = "e2";
		final Side side = Side.WHITE;
		final String squareToMove = "e3";

		assertPawnMovement( side, initialSquare, squareToMove );
	}

	@Test
	public void singleBlackMove() {
		final String initialSquare = "f4";
		final Side side = Side.BLACK;
		final String squareToMove = "f3";

		assertPawnMovement( side, initialSquare, squareToMove );
	}

	@Test
	public void doubleMove() {
		final String initialSquare = "c2";
		final Side side = Side.WHITE;
		final String squareToMove = "c4";

		assertPawnMovement( side, initialSquare, squareToMove );
	}

	@Test
	public void doubleBlackMove() {
		assertPawnMovement( Side.BLACK, "g7", "g5" );
	}

	@Test
	public void preserveOtherWhitePieces() {
		final Side notMovedPieceSide = Side.WHITE;
		final String notMovedPieceSquare = "g4";
		PieceType notMovedPieceType = addAny( position, notMovedPieceSide, notMovedPieceSquare );

		//side effect of moving
		Position newPosition = assertPawnMovement( Side.BLACK, "c6", "c5" );

		assertHasPiece( newPosition, notMovedPieceType, notMovedPieceSide, notMovedPieceSquare );
	}

	private static void assertHasPiece( Position position, PieceType pieceType, Side notMovedPieceSide, String notMovedPieceSquare ) {
		if ( pieceType != PieceType.PAWN ) {
			throw new IllegalArgumentException( "Piece type is not supported yet: " + pieceType );
		}

		assertHasPawn( position, notMovedPieceSquare, notMovedPieceSide );
	}

	/**
	 * Assert that:
	 * if we add a pawn to the #position
	 * and call Position#move method,
	 * the result will have empty initialSquare and pawn on squareToMove
	 * @param side
	 * @param initialSquare
	 * @param squareToMove
	 * @return newPosition for further asserts
	 */
	private Position assertPawnMovement( Side side, String initialSquare, String squareToMove ) {
		position.addPawn( side, initialSquare );
		Position newPosition = position.move( initialSquare, squareToMove );

		assertHasPawn( newPosition, squareToMove, side );
		assertEmptySquare( newPosition, initialSquare );
		return newPosition;
	}

	private static void assertEmptySquare( Position position, String square ) {
		assertTrue( "The square must be empty: " + square, position.isEmptySquare( square ) );
	}

	/**
	 * Assert that position has a pawn on the square
	 * @param position
	 * @param square
	 * @param side
	 */
	private static void assertHasPawn( Position position, String square, Side side ) {
		assertTrue( "Pawn of " + side + " is expected to be on square: " + square,
				position.hasPawn( square, side ) );
	}
}
