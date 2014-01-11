package com.leokom.chess.engine;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Set;

/**
 * Author: Leonid
 * Date-time: 14.12.13 14:01
 */
public class PositionAllowedMovesTest {
	@Test
	public void simplePositionSingleMove() {
		Position position = new Position( null );
		position.add( Side.WHITE, "a1", PieceType.KING );
		position.add( Side.BLACK, "c1", PieceType.KING );

		Set< String[] > moves = position.getMoves( Side.WHITE );

		assertEquals( 1, moves.size() );
		final String[] move = moves.iterator().next();
		assertArrayEquals( new String[]{ "a1", "a2" }, move );
	}

	@Test //same test as previous but changing colours
	public void simplePositionTriangulateByColor() {
		Position position = new Position( null );
		position.add( Side.BLACK, "a1", PieceType.KING );
		position.add( Side.WHITE, "c1", PieceType.KING );

		Set< String[] > moves = position.getMoves( Side.BLACK );

		assertEquals( 1, moves.size() );
		final String[] move = moves.iterator().next();
		assertArrayEquals( new String[]{ "a1", "a2" }, move );
	}

	@Test
	public void simplePositionNoMoves() {
		Position position = new Position( null );
		position.add( Side.WHITE, "a1", PieceType.KING );

		position.add( Side.BLACK, "b1", PieceType.BISHOP ); //any?
		position.add( Side.BLACK, "a2", PieceType.PAWN ); //any?
		position.add( Side.BLACK, "b2", PieceType.PAWN ); //any?
		position.add( Side.BLACK, "c1", PieceType.KING );

		Set< String[] > moves = position.getMoves( Side.WHITE );

		assertEquals( 0, moves.size() );
	}
}
