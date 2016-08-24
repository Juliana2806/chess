package com.leokom.chess.engine;

import org.junit.Test;

import static com.leokom.chess.engine.PositionAsserts.assertEmptySquare;
import static com.leokom.chess.engine.PositionAsserts.assertHasPawn;
import static com.leokom.chess.engine.Side.BLACK;
import static com.leokom.chess.engine.Side.WHITE;
import static org.junit.Assert.assertNull;

/**
 * Author: Leonid
 * Date-time: 18.09.12 21:50
 */
public class PawnEnPassantNewPositionTest {
	@Test
	public void enPassantLeft() {
		Position position = new Position( Side.WHITE );
		position.setEnPassantFile( 'd' );
		position.addPawn( BLACK, "d5" );

		position.addPawn( WHITE, "e5" );

		Position newPosition = position.move( "e5", "d6" );

		//en passant right now is absent
		assertNull( newPosition.getPossibleEnPassantFile() );
		assertEmptySquare( newPosition, "e5" );
		assertHasPawn( newPosition, "d6", WHITE );
		assertEmptySquare( newPosition, "d5" );
	}

	@Test
	public void enPassantLeftTriangle() {
		Position position = new Position( Side.WHITE );
		position.setEnPassantFile( 'e' );

		position.addPawn( BLACK, "e5" );

		position.addPawn( WHITE, "f5" );

		Position newPosition = position.move( "f5", "e6" );

		//en passant right now is absent
		assertNull( newPosition.getPossibleEnPassantFile() );

		assertEmptySquare( newPosition, "f5" );
		assertHasPawn( newPosition, "e6", WHITE );
		assertEmptySquare( newPosition, "e5" );
	}

	@Test
	public void right() {
		Position position = new Position( Side.WHITE );
		position.setEnPassantFile( 'g' );
		position.addPawn( BLACK, "g5" );

		position.addPawn( WHITE, "f5" );

		Position newPosition = position.move( "f5", "g6" );

		//en passant right now is absent
		assertNull( newPosition.getPossibleEnPassantFile() );

		assertEmptySquare( newPosition, "f5" );
		assertEmptySquare( newPosition, "g5" );
		assertHasPawn( newPosition, "g6", WHITE );
	}

	@Test
	public void enPassantBlackLeft() {
		Position position = new Position( Side.BLACK );
		position.setEnPassantFile( 'a' );
		position.addPawn( WHITE, "a4" );

		position.addPawn( BLACK, "b4" );
		Position newPosition = position.move( "b4", "a3" );

		assertEmptySquare( newPosition, "b4" );
		assertHasPawn( newPosition, "a3", BLACK );
		assertEmptySquare( newPosition, "a4" );
	}

	@Test
	public void enPassantBlackTriangulate() {
		Position position = new Position( Side.BLACK );
		position.setEnPassantFile( 'g' );
		position.addPawn( WHITE, "g4" );

		position.addPawn( BLACK, "h4" );
		Position newPosition = position.move( "h4", "g3" );

		assertEmptySquare( newPosition, "h4" );
		assertHasPawn( newPosition, "g3", BLACK );
		assertEmptySquare( newPosition, "g4" );
	}

	@Test
	public void enPassantRightBlack() {
		Position position = new Position( Side.BLACK );
		position.setEnPassantFile( 'c' );
		position.addPawn( WHITE, "c4" );

		position.addPawn( BLACK, "b4" );
		Position newPosition = position.move( "b4", "c3" );

		//en passant right now is absent
		assertNull( newPosition.getPossibleEnPassantFile() );

		assertEmptySquare( newPosition, "b4" );
		assertHasPawn( newPosition, "c3", BLACK );
		assertEmptySquare( newPosition, "c4" );
	}
}