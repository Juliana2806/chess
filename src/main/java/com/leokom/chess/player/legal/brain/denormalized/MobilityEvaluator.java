package com.leokom.chess.player.legal.brain.denormalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.legal.brain.common.Evaluator;

/**
 * If inside a position there is a bigger variety of moves
 * we consider this as better
 *
 * Author: Leonid
 * Date-time: 23.07.14 21:46
 */
class MobilityEvaluator implements Evaluator {
	private static final double WORST_MOVE = 0.0;

	/**
	 * {@inheritDoc}
	 * @return [ -max amount of legal moves in a position, max amount of legal moves in a position ]
	 */
	@Override
	public double evaluateMove( Position position, Move move ) {
		final Position target = position.move( move );
		//TODO: all evaluators must take into account
		//possibility that the position is terminal
		//most likely MasterEvaluator might take preventive actions.

		//proved need in LegalPlayerTest and in (temporarily ignored) LegalPlayerSelfTest
		if ( target.isTerminal() ) {
			return WORST_MOVE;
		}

		final Side side = position.getSideToMove();
		//sonar suggests this casting
		return (double) getMobilityIndex( target, side ) - getMobilityIndex( target, side.opposite() );
	}

	private int getMobilityIndex( Position target, Side side ) {
		if ( target.getSideToMove() == side ) {
			return target.getMoves().size();
		}
		else {
			return target.toMirror().getMoves().size();
		}
	}
}
