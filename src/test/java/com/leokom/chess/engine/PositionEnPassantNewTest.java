package com.leokom.chess.engine;

import org.junit.Test;
import static com.leokom.chess.engine.PositionAsserts.*;
import static com.leokom.chess.engine.Side.*;

/**
 * Author: Leonid
 * Date-time: 18.09.12 21:50
 */
public class PositionEnPassantNewTest {
	@Test
	public void enPassantLeft() {
		Position position = new Position( "d" );
		position.addPawn( BLACK, "d5" );

		position.addPawn( WHITE, "e5" );

		Position newPosition = position.move( "e5", "d6" );

		assertEmptySquare( newPosition, "e5" );
		assertHasPawn( newPosition, "d6", WHITE );
		assertEmptySquare( newPosition, "d5" );
	}

	@Test
	public void enPassantLeftTriangle() {
		Position position = new Position( "e" );
		position.addPawn( BLACK, "e5" );

		position.addPawn( WHITE, "f5" );

		Position newPosition = position.move( "f5", "e6" );

		assertEmptySquare( newPosition, "f5" );
		assertHasPawn( newPosition, "e6", WHITE );
		assertEmptySquare( newPosition, "e5" );
	}

	@Test
	public void right() {
		Position position = new Position( "g" );
		position.addPawn( BLACK, "g5" );

		position.addPawn( WHITE, "f5" );

		Position newPosition = position.move( "f5", "g6" );

		assertEmptySquare( newPosition, "f5" );
		assertEmptySquare( newPosition, "g5" );
		assertHasPawn( newPosition, "g6", WHITE );
	}
}