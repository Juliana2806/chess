package com.leokom.chess;

import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import org.apache.logging.log4j.LogManager;

/**
 * Create &amp; Run Game of Chess.
 * Author: Leonid
 * Date-time: 11.02.16 23:00
 */
public final class Game {
	private final Player whitePlayer;
	private final Player blackPlayer;

	/**
	 * Initiate game between two players
	 * @param whitePlayer white player
	 * @param blackPlayer black player
	 */
	public Game( Player whitePlayer, Player blackPlayer ) {
		this.whitePlayer = whitePlayer;
		this.blackPlayer = blackPlayer;
	}

	/**
	 * Run the game.
	 * @return winner among whitePlayer, blackPlayer or null in case of draw
	 */
	public Player run() {
		LogManager.getLogger().info( "Starting game : {} vs {}", whitePlayer.name(), blackPlayer.name() );

		//setting opponents for symmetry. Technically it's possible
		// for one set to make a back reference
		blackPlayer.setOpponent( whitePlayer );
		whitePlayer.setOpponent( blackPlayer );

		//Black is informed first, to passively wait for the White's first move
		blackPlayer.opponentSuggestsMeStartNewGameBlack();

		//inform white that black is ready so you may start
		//white player should start the game e.g. by providing main loop
		whitePlayer.opponentSuggestsMeStartNewGameWhite();

		LogManager.getLogger().info( "Game finished: {} vs {}", whitePlayer.name(), blackPlayer.name() );

		//TODO: asymmetry, need validating that blackPlayer position gives same result
		//maybe it's time to share the Position
		Position position = whitePlayer.getPosition();

		if ( position.isTerminal() ) {
			final Side winningSide = position.getWinningSide();

			if ( winningSide == null ) {
				return null;
			}

			return winningSide == Side.WHITE ? whitePlayer : blackPlayer;
		}
		else {
			LogManager.getLogger().warn( "The game has been finished without reaching a terminal position" );
			return null;
		}
	}
}
