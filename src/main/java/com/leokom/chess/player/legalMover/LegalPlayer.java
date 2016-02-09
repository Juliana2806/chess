package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author: Leonid
 * Date-time: 09.12.13 22:02
 */
public class LegalPlayer implements Player {
	private Player opponent;
	private Position position = Position.getInitialPosition();
	private Evaluator brains;
	private boolean recordingMode;

	/**
	 * Create player
	 */
	public LegalPlayer() {
		this( new MasterEvaluator() );
	}

	/**
	 * Create a player with injected brains
	 * @param brains brains to evaluate moves
	 */
	LegalPlayer( Evaluator brains ) {
		this.brains = brains;
	}

	@Override
	public void opponentSuggestsMeStartNewGameWhite() {
		getLogger().info( "Opponent suggested me started a new game whites. Starting it" );
		position = Position.getInitialPosition();
		executeMove();
	}

	@Override
	public void opponentSuggestsMeStartNewGameBlack() {
		getLogger().info( "Opponent suggested me started a new game black. Starting it" );
		position = Position.getInitialPosition();
	}

	@Override
	public void opponentMoved( Move opponentMove ) {
		LogManager.getLogger().info( "Opponent moved : {}", opponentMove );
		//REFACTOR: should be part of man-in-the-middle (judge, board, validator?)
		if ( opponentMove == null ) {
			throw new IllegalArgumentException( "Wrong opponent move null" );
		}

		//updating internal representation of our position according to the opponent's move
		updatePositionByOpponentMove( opponentMove );

		//can be not our move : when opponent offers draw before HIS move
		//so he still has the right to move
		if ( isOurMove( opponentMove ) && !recordingMode ) {
			executeMove();
		}
	}

	//TODO: better need querying the position to check whether it's our side to move!
	//(then need storing 'our side')
	private boolean isOurMove( Move opponentMove ) {
		return opponentMove != Move.OFFER_DRAW;
	}

	private void updatePositionByOpponentMove( Move opponentMove ) {
		position = position.move( opponentMove );
	}

	//exposing package-private for tests
	void executeMove() {
		Set< Move > legalMoves = position.getMoves();

		if ( !legalMoves.isEmpty() ) {
			Move move = findBestMove( legalMoves );

			updateInternalPositionPresentation( move );

			informOpponentAboutTheMove( move );
		}
		else {
			getLogger().info( "We cannot execute any moves." +
					" Final state has been detected." +
					" Winning side : " + position.getWinningSide() );
		}
	}

	private void informOpponentAboutTheMove( Move move ) {
		opponent.opponentMoved( move );
	}

	//updating internal representation of current position according to our move
	private void updateInternalPositionPresentation( Move move ) {
		getLogger().info( this.position.getSideToMove() + " : Moved " + move );
		position = position.move( move );
		getLogger().info( "\nNew position : " + position );
	}

	/**
	 *
	 * @param legalMoves not-empty set of moves
	 * @return best move according to current strategy
	 */
	private Move findBestMove( Set< Move > legalMoves ) {
		Map< Move, Double > moveRatings = new HashMap<>();
		for ( Move move : legalMoves ) {
			moveRatings.put( move, brains.evaluateMove( position, move ) );
		}

		return getMoveWithMaxRating( moveRatings );
	}

	private Move getMoveWithMaxRating( Map< Move, Double > moveValues ) {
		return
			moveValues.entrySet().stream()
			.sorted( Map.Entry.< Move, Double >comparingByValue().reversed() )
			.findFirst().get().getKey();
	}


	private Logger getLogger() {
		return LogManager.getLogger( this.getClass() );
	}

	@Override
	public void setOpponent( Player opponent ) {
		this.opponent = opponent;
	}

	@Override
	public void switchToRecodingMode() {
		getLogger().info( "Switching to recording mode... Moves will be executed by external source" );
		this.recordingMode = true;
	}

	@Override
	public void leaveRecordingMode() {
		getLogger().info( "Leaving recording mode" );
		this.recordingMode = false;
	}

	@Override
	public void joinGameForSideToMove() {
		getLogger().info( "Opponent suggested me to join the game for side: {}. Joining...", position.getSideToMove() );
		leaveRecordingMode();
		executeMove();
	}

	//injecting the position for tests, however maybe in future
	//it's useful for starting game from a non-initial position
	void setPosition( Position position ) {
		this.position = position;
	}

	//TODO: public for Winboard<->Legal integration test
	public Position getPosition() {
		return this.position;
	}

	public boolean isRecordingMode() {
		return recordingMode;
	}
}
