package com.leokom.games.chess.player.legal.brain.normalized;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.leokom.games.chess.engine.Move;
import com.leokom.games.chess.engine.Position;
import com.leokom.games.chess.engine.Side;
import com.leokom.games.chess.player.legal.brain.common.Evaluator;
import com.leokom.games.chess.player.legal.brain.common.SideEvaluator;
import com.leokom.games.chess.player.legal.brain.internal.common.SymmetricEvaluator;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Leonid
 * Date-time: 14.07.14 22:58
 */
public class CastlingSafetyEvaluator implements Evaluator {
	private static final double BEST_MOVE = 1;
	private static final double WORST_MOVE = 0;

	private static final int MAX_PIECES_BETWEEN_KING_AND_ROOKS = 5;
	//maximally 5: 8 in the row - pieces of interest themselves
	private static final Range PIECES_BETWEEN_KING_AND_ROOKS = new Range( 0, MAX_PIECES_BETWEEN_KING_AND_ROOKS);

	//something between the worst and the best
	private static final Range ACCEPTABLE_MOVES_EVALUATION = new Range( 0.25, 0.75 );

	private static final SymmetricalNormalizedRange RANGE = new SymmetricalNormalizedRange( WORST_MOVE, BEST_MOVE );


	/*
	 * If rook is captured - don't think it would be possible
	 * castling with it
	 *
	 * + Moving a piece out between king & rook is good
	 *
	 * + Moving a piece out between king & rook is even better
	 * if it immediately gives space for castling
	 *
	 * Moving 1 rook is not so harmful if second one hasn't yet moved
	 *
	 * @return move estimate [0, 1]
	 */
	@Override
	public double evaluateMove( Position position, Move move ) {
		Position targetPosition = position.move( move );

		return RANGE.normalize( new SymmetricEvaluator( new CastlingSafetySideEvaluator() ).evaluate( targetPosition ));
	}

	private static class CastlingSafetySideEvaluator implements SideEvaluator {
		private static final Set< String > FILES_IN_BETWEEN_QUEEN_SIDE = ImmutableSet.of( "b", "c", "d" );
		private static final Set< String > FILES_IN_BETWEEN_KING_SIDE = ImmutableSet.of( "f", "g" );


		@Override
		public double evaluatePosition(Position position, Side side) {
			//strategy : 'castling addicted player'
			// avoid moving rook and king
			//if it's not castling (I want to see castling)
			//after castling we allow such moves

			//if anywhere in the history the castling was executed, our goal has been reached
			if ( position.hasCastlingExecuted( side ) ) {
				return BEST_MOVE;
			}

			//if king has moved already - all other moves don't help in castling safety
			if ( position.hasKingMoved( side ) ) {
				return WORST_MOVE;
			}

			//any of rooks is moving: reducing chances to castle
			if ( position.hasARookMoved( side ) || position.hasHRookMoved( side ) ) {
				return WORST_MOVE;
			}

			//the less occupied - the better
			//normalizing to range [ 0.25, 0.75 ] of 'acceptable' moves
			return PIECES_BETWEEN_KING_AND_ROOKS.convert( ACCEPTABLE_MOVES_EVALUATION, getFreeInBetween( position, side ) );
		}


		private int getFreeInBetween( Position position, Side side ) {
			return MAX_PIECES_BETWEEN_KING_AND_ROOKS - getOccupiedInBetween( position, side );
		}

		/**
		 * Collecting pieces between king and not-moved rook
		 * @param position position
		 * @param side side (important to inject to check state AFTER move)
		 * @return amount of occupied squares between king & not-moved rook
		 */
		private int getOccupiedInBetween( Position position, Side side ) {
			//collecting squares between king and not-moved rook

			int rank = side == Side.WHITE ? 1 : 8;
			Set< String > squaresInBetween = new HashSet<>();
			if ( !position.hasARookMoved( side ) ) {
				FILES_IN_BETWEEN_QUEEN_SIDE.forEach( file -> squaresInBetween.add( file + rank ) );
			}

			if ( !position.hasHRookMoved( side ) ) {
				FILES_IN_BETWEEN_KING_SIDE.forEach( file -> squaresInBetween.add( file + rank ) );
			}

			final Set<String> occupiedInBetween = Sets.intersection( position.getSquaresOccupied(), squaresInBetween );

			return occupiedInBetween.size();
		}
	}

}
