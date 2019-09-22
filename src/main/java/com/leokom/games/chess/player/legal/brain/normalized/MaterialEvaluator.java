package com.leokom.games.chess.player.legal.brain.normalized;

import com.leokom.games.chess.engine.Move;
import com.leokom.games.chess.engine.PieceType;
import com.leokom.games.chess.engine.Position;
import com.leokom.games.chess.player.legal.brain.common.Evaluator;
import com.leokom.games.chess.player.legal.brain.common.EvaluatorType;
import com.leokom.games.chess.player.legal.brain.denormalized.DenormalizedEvaluatorFactory;
import com.leokom.games.commons.brain.normalized.range.SymmetricalNormalizedRange;

import static com.leokom.games.chess.player.legal.brain.internal.common.MaterialValues.VALUES;

/**
 * Evaluate material domination
 *
 * This is the first brain
 * for which I think symmetric counter-part
 * has no sense (since it's a difference between us & opponent)
 *
 */
class MaterialEvaluator implements Evaluator {
	@Override
	public double evaluateMove( Position position, Move move ) {
		double materialAdvantage = new DenormalizedEvaluatorFactory().get( EvaluatorType.MATERIAL ).evaluateMove( position, move );
		return RANGE.normalize( materialAdvantage );
	}

	//highly depends on actual values
	//and on fact we evaluate queen higher than other pieces except king
	//and on assumption about 8 max promoted queens

	//no pawns included here since they are 'promoted'
	static final int MAXIMAL_VALUE =
					VALUES.get( PieceType.KING ) +
					9 * VALUES.get( PieceType.QUEEN ) +
					2 * VALUES.get( PieceType.ROOK ) +
					2 * VALUES.get( PieceType.BISHOP ) +
					2 * VALUES.get( PieceType.KNIGHT );

	//technically it should be VALUE(KING)
	//but to support 'invalid' positions we keep the lowest possible value
	private static final int MINIMAL_VALUE = 0;

	private static final SymmetricalNormalizedRange RANGE = new SymmetricalNormalizedRange( MINIMAL_VALUE, MAXIMAL_VALUE );
}
