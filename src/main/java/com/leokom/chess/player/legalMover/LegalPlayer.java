package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;

import java.util.Set;

/**
 * Author: Leonid
 * Date-time: 09.12.13 22:02
 */
public class LegalPlayer implements Player {
	private Player opponent;
	private Position position = Position.getInitialPosition();

	@Override
	public void run() {

	}

	@Override
	public void opponentOfferedDraw() {

	}

	@Override
	public void opponentAgreedToDrawOffer() {

	}

	@Override
	public void opponentMoved( String opponentMove ) {
		//TODO: null is a hidden ugly way to say 'it's our first move now'
		if ( opponentMove != null ) {
			//TODO: hard dependency on NOT-INTERNAL format (Winboard?)
			//TODO: castling etc will cause crash here?
			String source = opponentMove.substring( 0, 2 );
			String destination = opponentMove.substring( 2, 4 );

			position = position.move( source, destination );
		}

		Set< String[] > moves = position.getMoves( Side.WHITE );

		//TODO: if empty set it means the game has been finished (what's the result?)
		if ( !moves.isEmpty() ) {
			String[] possibleMove = moves.iterator().next();

			final String from = possibleMove[ 0 ];
			final String to = possibleMove[ 1 ];
			opponent.opponentMoved( from + to );

			position = position.move( from, to );
		}
		//TODO: else?

	}

	@Override
	public void opponentResigned() {

	}

	@Override
	public void setOpponent( Player opponent ) {
		this.opponent = opponent;
	}

	//injecting the position for tests, however maybe in future
	//it's useful for starting game from a non-initial position
	void setPosition( Position position ) {
		this.position = position;
	}
}
