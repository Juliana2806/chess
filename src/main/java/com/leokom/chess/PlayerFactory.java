package com.leokom.chess;

import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.legalMover.LegalPlayer;
import com.leokom.chess.player.simple.SimpleEnginePlayer;
import com.leokom.chess.player.winboard.WinboardPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Create players for the chess game
 *
 * Author: Leonid
 * Date-time: 06.05.14 22:45
 */
final class PlayerFactory {
	private PlayerFactory() {}

	private static Logger logger = LogManager.getLogger( PlayerFactory.class );

	//side -> name of system property that specifies player for the side
	private static final Map< Side, String > SYSTEM_PROPERTIES =
		new HashMap< Side, String >() {{
			put( Side.WHITE, "white" );
			put( Side.BLACK, "black" );
		}};

	/**
	 * Create player for the side
	 * Basing on defaults or system properties.
	 * Defaults :
	 * WHITE: Winboard
	 * BLACK: Simple
	 *
	 * There are practical important limitations (not yet validated):
	 *
	 * Winboard vs Winboard game has no practical use (both will work with System.out)
	 * Winboard vs any other engine that uses System.out has no practical use (UCI?)
	 *
	 * LegalPlayer vs LegalPlayer is possible but can lead to StackOverflow due to
	 * no limits on move amount and single-threaded model of execution
	 *
	 * @param side side to create
	 * @return new instance of a player
	 */
	static Player createPlayer( Side side ) {
		final String engineName = System.getProperty( SYSTEM_PROPERTIES.get( side ) );

		logger.info( "Engine from system properties: " + engineName + ". Side = " + side );

		if ( engineName == null ) {
			logger.info( "No selection done. Selecting default player" );
			return getDefaultPlayer( side );
		}

		switch ( engineName ) {
			case "Legal":
				return new LegalPlayer();
			case "Simple":
				return new SimpleEnginePlayer();
			case "Winboard":
				return WinboardPlayer.create();
			default:
				logger.warn( "Unsupported option specified. Selecting default player" );
				return getDefaultPlayer( side );
		}
	}

	private static Player getDefaultPlayer( Side side ) {
		logger.info( "Selecting default engine for Side = " + side );
		return
			side == Side.WHITE ?
				WinboardPlayer.create() :
				new SimpleEnginePlayer();
	}
}
