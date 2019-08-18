package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.brain.common.Evaluator;

/**
 * Author: Leonid
 * Date-time: 01.03.15 22:32
 *
 * Evaluate final game state.
 * Checkmate is the highest goal of the whole game and will mean win.
 */
public class TerminalEvaluator implements Evaluator {

	private static final double BEST_MOVE = 1;
	private static final double DRAW_EVALUATION = 0.001;
	private static final double WORST_MOVE = 0;

	/**
	 *
	 * {@inheritDoc}
	 * @return 0 or 1
	 */
	@Override
	public double evaluateMove( Position position, Move move ) {
		final Position result = position.move( move );

		if ( !result.isTerminal() ) {
			return WORST_MOVE;
		}

		//assuming it's draw
		if ( result.getWinningSide() == null ) {
			return DRAW_EVALUATION;
		}

		return position.getSide( move.getFrom() ) == result.getWinningSide() ? BEST_MOVE : WORST_MOVE;
	}
}
