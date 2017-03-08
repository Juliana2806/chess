package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.brain.common.Evaluator;
import com.leokom.chess.player.legal.brain.common.EvaluatorType;
import com.leokom.chess.player.legal.brain.denormalized.DenormalizedEvaluatorFactory;

/**
 * Author: Leonid
 * Date-time: 25.08.16 20:54
 */
class AttackEvaluator implements Evaluator {
	@Override
	public double evaluateMove( Position position, Move move ) {
		return
			new DenormalizedEvaluatorFactory().get( EvaluatorType.ATTACK )
			.evaluateMove( position, move ) / MaterialEvaluator.MAXIMAL_VALUE;
	}
}
