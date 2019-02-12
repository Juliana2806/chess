package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.*;
import com.leokom.chess.player.legal.brain.common.Evaluator;
import com.leokom.chess.player.legal.brain.common.EvaluatorAsserts;
import com.leokom.chess.player.legal.brain.common.EvaluatorFactory;
import com.leokom.chess.player.legal.brain.common.EvaluatorType;
import com.leokom.chess.player.legal.brain.internal.common.EvaluatorWeights;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MasterEvaluatorTest {
	private Evaluator evaluator;

	@Before
	public void prepare(){
		evaluator = new MasterEvaluator();
	}

	/**
	 * Avoid really 'stupid' moves
	 */
	@Test
	public void beSmartALittle() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "g6", PieceType.QUEEN );
		position.add( Side.BLACK, "g7", PieceType.PAWN );
		//protects the pawn
		position.add( Side.BLACK, "g8", PieceType.ROOK );

		Move captureWithRiskToLoseQueen = new Move( "g6", "g7" );
		Move simpleMove = new Move( "g6", "g5" );

		new EvaluatorAsserts( evaluator )
				.assertFirstBetter( position, simpleMove, captureWithRiskToLoseQueen );
	}

	@Test
	public void resignIsWeak() {
		Position position = Position.getInitialPosition();

		new EvaluatorAsserts( evaluator )
				.assertFirstBetter( position, new Move( "e2", "e4" ), Move.RESIGN );
	}

	//enforce rules that are valid for the whole normalized package, to MasterEvaluator itself
	@Test
	public void allMovesMustBeEvaluatedFrom0To1() {
		Position position = Position.getInitialPosition();

		assertAllMovesEvaluatedIn0To1Range(position, evaluator);
	}

	private void assertAllMovesEvaluatedIn0To1Range(Position position, Evaluator evaluatorToValidate) {
		position.getMoves().forEach( move -> {
			double result = evaluatorToValidate.evaluateMove(position, move);
			assertTrue(
					String.format( "The move %s must be evaluated in range [0,1], actually: %s", move, result )
					,result >= 0.0 && result <= 1.0 );
		} );
	}

	@Test
	public void allMovesMustBeEvaluatedFrom0To1EvenWithCustomWeights() {
		Map<EvaluatorType, Double> weights = new HashMap<>();
		Arrays.stream( EvaluatorType.values() ).forEach( type -> weights.put( type, 1.0 ) );

		MasterEvaluator masterEvaluatorWithCustomWeights = new MasterEvaluator(weights);
		assertAllMovesEvaluatedIn0To1Range( Position.getInitialPosition(), masterEvaluatorWithCustomWeights );
	}

	@Test
	public void singleNonTerminalEvaluatorWeightMaximalResultIs1() {
		Map<EvaluatorType, Double> weights = new HashMap<>();
		weights.put( EvaluatorType.TERMINAL, 1.0 );
		weights.put( EvaluatorType.CASTLING_SAFETY, 1.0 ); //max weight

		EvaluatorFactory factory = Mockito.mock(EvaluatorFactory.class);
		Evaluator castlingEvaluatorMock = Mockito.mock(Evaluator.class);
		//max estimate
		Mockito.when( castlingEvaluatorMock.evaluateMove( Mockito.any(), Mockito.any() ) ).thenReturn( 1.0 );
		Mockito.when( factory.get( EvaluatorType.CASTLING_SAFETY )) .thenReturn( castlingEvaluatorMock );

		MasterEvaluator masterEvaluator = new MasterEvaluator( new EvaluatorWeights( weights ), factory );
		double evaluation = masterEvaluator.evaluateMove(Position.getInitialPosition(), new Move("e2", "e4"));
		assertEquals( 1.0, evaluation, 0 );
	}

	//losing should get the minimal possible value
	@Test
	public void losingIsEvaluatedTo0() {
		Position position = Position.getInitialPosition();
		assertEquals( 0.0, evaluator.evaluateMove(position, Move.RESIGN), 0 );
	}

	@Test
	public void winningIsEvaluatedTo1() {
		Position position = Position.getInitialPosition()
				.move( "f2", "f3" )
				.move( "e7", "e5" )
				.move( "g2", "g4" );
		//fools checkmate is prepared


		Move checkmateMove = new Move( "d8", "h4" );
		assertEquals( 1, evaluator.evaluateMove( position, checkmateMove ), 0 );
	}
}