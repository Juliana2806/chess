package com.leokom.chess.player.simple;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 * Run just 2 moves for white/black (central pawns)
 * Always agree to draw.
 * Resign on the 3'd move.
 * This player guarantees finite game.
 *
 * Author: Leonid
 * Date-time: 15.04.13 22:26
 */
public class SimplePlayer implements Player {
	private Position position = Position.getInitialPosition();
	private int moveNumber;
	private Player opponent;
	private final Logger logger = LogManager.getLogger( this.getClass() );
	private boolean recordingMode = false;

	public SimplePlayer() {
		moveNumber = 0;
	}

	@Override
	public void setOpponent( Player opponent ) {
		this.opponent = opponent;
	}

	@Override
	public void switchToRecodingMode() {
		recordingMode = true;
	}

	@Override
	public void leaveRecordingMode() {
		recordingMode = false;
	}

	@Override
	public void joinGameForSideToMove() {
		recordingMode = false;
		executeMove((Move) null);
	}

	@Override
	public Position getPosition() {
		return position;
	}

	@Override
	public void opponentMoved( Move... opponentMoves ) {
		Arrays.stream( opponentMoves ).forEach( this::updatePosition );
		if ( recordingMode ) {
			return;
		}

		if ( position.isTerminal() ) {
			logger.info( "Game is over." );
			return;
		}

		executeMove( opponentMoves );
	}

	private void updatePosition( Move opponentMove ) {
		position = position.move( opponentMove );
	}

	private void executeMove( Move... opponentMoves ) {
		LogManager.getLogger().info( "We're going to execute a move" );
		int rankFrom = position.getSideToMove() == Side.WHITE ? 2 : 7;
		int rankTo = position.getSideToMove() == Side.WHITE ? 4 : 5;
		moveNumber++;
		logger.info( "Move number = " + moveNumber );

		//Simplest possible strategy - agree to the draw offer
		if ( opponentMoves != null && Arrays.stream( opponentMoves ).anyMatch(Move.OFFER_DRAW::equals) ) {
			moveTo( Move.ACCEPT_DRAW );
			return;
		}

		switch ( moveNumber ) {
			case 1:
				moveTo( new Move( "e" + rankFrom,  "e" + rankTo ) );
				break;
			case 2:
				moveTo( new Move( "d" + rankFrom, "d" + rankTo ) );
				//NOTE: interesting to implement - how much do we need to wait for result?
				//NOTE2: it's not recommended way to offer draw after the move.
				//TODO: technically the Position must prohibit this offer draw
				//now position.move is almost non-validating
				if ( !position.isTerminal() ) {
					offerDraw();
				}
				break;
			default:
				resign();
		}
	}

	private void offerDraw() {
		moveTo( Move.OFFER_DRAW );
	}

	private void resign() {
		moveTo( Move.RESIGN );
	}

	/**
	 * Execute the move
	 * @param move move to do
	 */
	private void moveTo( Move move ) {
		logger.info( "Executing a move: " + move );
		updatePosition(move);
		//hiding complexity of opponent.opponentMoved call
		opponent.opponentMoved( move );
	}

	@Override
	public void opponentSuggestsMeStartNewGameWhite() {
		LogManager.getLogger().info( "Opponent suggests me start new game white" );
		moveNumber = 0;
		position = Position.getInitialPosition();
		executeMove((Move) null);
	}

	@Override
	public void opponentSuggestsMeStartNewGameBlack() {
		LogManager.getLogger().info( "Opponent suggests me start new game black" );
		moveNumber = 0;
		position = Position.getInitialPosition();
	}
}
