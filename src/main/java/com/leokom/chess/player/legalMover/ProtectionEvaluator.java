package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;

/**
 * Author: Leonid
 * Date-time: 21.10.14 23:03
 */
class ProtectionEvaluator implements Evaluator {
	private static final double WORST_MOVE = 0.0;

	/**
	 	Protection has 2 aspects:
	 	a) tactical: act when your pieces are under attack
		b) strategical: make the pieces protect each other even against further attacks

		From visual POV it would be more interesting to implement a)
		since it will look like smart behaviour of protection against direct attack.

		Ways to protect a)
		- go away from attack (but be careful not moving to attacked square)
		- capture the attacker (but be careful - it might be protected)
		- protect your piece (so you'll be able to capture the attacker next move
		if the square isn't double-attacked)
		- put another piece in front of yours (if the attacker is not king or knight)
		(probably if it's less valuable piece)

		2 first ways can be described in 'reduction of attacking index of opponent'
		3'd way is more related to strategical protection (but anyway it's a way to act)
		4'th way is a little bit similar to 1-2  (if we take piece value into account)
	 */
	@Override
	public double evaluateMove( Position position, Move move ) {
		if ( move.isSpecial() ) {
			return WORST_MOVE;
		}

		final Side ourSide = position.getSide( move.getFrom() );
		float opponentAttackIndex = AttackIndexCalculator.getAttackIndex(
			position.move( move ), ourSide.opposite() );
		return 1 - opponentAttackIndex / MaterialEvaluator.MAXIMAL_VALUE;
	}
 }