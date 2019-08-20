package com.leokom.chess.players;

import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.legal.LegalPlayer;
import com.leokom.chess.player.legal.brain.denormalized.DenormalizedBrain;
import com.leokom.chess.player.legal.brain.normalized.MasterEvaluator;
import com.leokom.chess.player.legal.brain.normalized.NormalizedBrain;
import com.leokom.chess.player.legal.brain.simple.SimpleBrain;
import com.leokom.chess.player.winboard.WinboardPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Function;

/**
 * Create players for the chess game based on command-line parameters
 *
 * Author: Leonid
 * Date-time: 06.05.14 22:45
 */
public final class CommandLinePlayers implements Function< Side, Player > {
	private static final Logger logger = LogManager.getLogger( CommandLinePlayers.class );

	private final ChessSystemProperty engineProperty;
	private final ChessSystemProperty depthProperty;

	public CommandLinePlayers() {
		this.engineProperty = new ChessSystemProperty( "engine" );
		this.depthProperty = new ChessSystemProperty( "depth" );
	}

	/**
	 * Chess system properties.
	 * Represent properties in format 'side.property' (like 'white.depth' or 'black.engine')
	 */
	private static class ChessSystemProperty {
		private final String propertyName;

		ChessSystemProperty( String propertyName ) {
			this.propertyName = propertyName;
		}

		Optional<String> getFor( Side side ) {
			return Optional.ofNullable(
				System.getProperty(
					String.format( "%s.%s", side.name().toLowerCase(), propertyName )
				)
			);
		}
	}

	/**
	 * Create player for the side
	 * Basing on defaults or system properties.
	 * Defaults :
	 * WHITE: Winboard
	 * BLACK: brain.normalized
	 *
	 * There are practical important limitations (not yet validated):
	 *
	 * Winboard vs Winboard game has no practical use (both will work with System.out)
	 * Winboard vs any other engine that uses System.out has no practical use (UCI?)
	 *
	 * brain.* vs brain.* is possible but can lead to StackOverflow due to
	 * no limits on move amount and single-threaded model of execution
	 * (although some brains like brain.simple have internal limit on count of moves).
	 *
	 * brain.normalized supports optional depth parameter.
	 *
	 * @param side side to create
	 * @return new instance of a player
	 */
	@Override
	public Player apply( Side side ) {
		String engineName = engineProperty.getFor( side ).orElseGet( () -> {
			logger.info( "Selecting a default engine for Side = " + side );
			return side == Side.WHITE ?	"ui.winboard" : "brain.normalized";
		} );

		return getPlayer( side, engineName );
	}

	private Player getPlayer( Side side, String engineName ) {
		logger.info("Selecting an engine for Side = " + side + " by engine name = " + engineName);
		switch (engineName) {
			case "brain.normalized":
				int depth = depthProperty.getFor(side)
						.map(Integer::valueOf)
						.orElse( 1 ); //this depth has been used for years
				return new LegalPlayer( new NormalizedBrain<>( new MasterEvaluator(), depth ) );
			case "brain.denormalized":
				return new LegalPlayer( new DenormalizedBrain() );
			case "brain.simple":
				return new LegalPlayer( new SimpleBrain() );
			case "ui.winboard":
				return WinboardPlayer.create();
			default:
				throw new IllegalArgumentException( "The engine is not supported: " + engineName);
		}
	}
}
