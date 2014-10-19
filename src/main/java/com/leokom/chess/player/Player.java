package com.leokom.chess.player;

import com.leokom.chess.engine.Move;

/**
 * Engine-agnostic player.
 * This interface is suggested to be common chess player abstraction
 * (independent of the fact the player is represented by a chess engine,
 * a human or a winboard-protocol )
 */
public interface Player {
	void opponentOfferedDraw();
	void opponentAgreedToDrawOffer();

	/**
	 *
	 * The method should be starting point to move for whites
	 *
	 * Black player should understand that the opponent suggests
	 * him start new game, and play white in this game.
	 *
	 */
	void opponentSuggestsMeStartNewGameWhite();

	/**
	 * React on another player's move
	 *
	 * @param opponentMove move received from the opponent
	 *
	 * Move must be specified via notation (case-sensitive!) specified by examples:
	 * Normal move: e2e4
	 * Pawn promotion: e7e8Q
	 * Castling: e1g1
	 *
	 * @deprecated Use #opponentMoved(Move) instead
	 */
	@Deprecated
	void opponentMoved( String opponentMove );

	/**
	 * Trying Java8 feature of default implementation
	 * to make refactoring of opponentMoved towards Move class
	 * less painful
	 * @param opponentMove opponent move
	 */
	default void opponentMoved( Move opponentMove ) {
		opponentMoved( opponentMove.toOldStringPresentation() );
	}

	void opponentResigned();

	//TODO: this method is extracted because we need
	//to set up bidirectional connection
	//think about better solution
	//(this is not good because player without opponent is in
	//half-constructed state)
	void setOpponent( Player opponent );

}
