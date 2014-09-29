package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.PieceType;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import org.junit.Before;
import org.junit.Test;


public class CastlingSafetyEvaluatorTest {
	private EvaluatorAsserts asserts;

	@Before
	public void prepare() {
		CastlingSafetyEvaluator evaluator = new CastlingSafetyEvaluator();
		asserts = new EvaluatorAsserts( evaluator );
	}

	@Test
	public void shouldKingMovementNotGoodInitially() {
		Position position = new Position();
		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.WHITE, "h1", PieceType.ROOK );
		position.add( Side.WHITE, "c1", PieceType.BISHOP );

		Move kingMove = new Move( "e1", "f1" );

		Move notKingMove = new Move( "c1", "b2" );

		asserts.assertFirstBetter( position, notKingMove, kingMove );
	}


}