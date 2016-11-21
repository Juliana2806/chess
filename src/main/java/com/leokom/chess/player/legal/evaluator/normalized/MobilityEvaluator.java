package com.leokom.chess.player.legal.evaluator.normalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.evaluator.common.Evaluator;
import com.leokom.chess.player.legal.evaluator.common.EvaluatorType;
import com.leokom.chess.player.legal.evaluator.denormalized.DenormalizedEvaluatorFactory;

/**
 * If inside a position there is a bigger variety of moves
 * we consider this as better
 *
 * Author: Leonid
 * Date-time: 23.07.14 21:46
 */
class MobilityEvaluator implements Evaluator {
	//TODO: calculate theoretical max. possible moves count in a position (9 promoted queens + all others? )
	private static final int MAXIMAL_POSSIBLE_MOVES = 1000;

	@Override
	public double evaluateMove( Position position, Move move ) {
		return new DenormalizedEvaluatorFactory().get( EvaluatorType.MOBILITY )
				.evaluateMove( position, move )
				/ MAXIMAL_POSSIBLE_MOVES;
	}
}
