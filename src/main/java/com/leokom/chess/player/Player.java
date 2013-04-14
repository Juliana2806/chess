package com.leokom.chess.player;

/**
 * Engine-agnostic player.
 * This interface is suggested to be common chess player abstraction
 * (independent of the fact the player is represented by a chess engine,
 * a human or a winboard-protocol )
 */
public interface Player extends NeedToGoListener, DrawOfferedListener, ResignListener {
	//may create attach - now it's over-projecting - 1 is OK

	/* The 'on' listeners
	 * represent the part of player that may be taught */

	void onOpponentOfferedDraw( DrawOfferedListener listener );

	//TODO: think if it's player's property
	void run();

	void opponentAgreedToDrawOffer();

	//TODO: this method is extracted because we need
	//to set up bidirectional connection
	//think about better solution
	//(this is not good because player without opponent is in
	//half-constructed state)
	void setOpponent( Player opponent );
}
