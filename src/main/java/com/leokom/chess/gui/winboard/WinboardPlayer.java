package com.leokom.chess.gui.winboard;

import com.leokom.chess.player.DrawOfferedListener;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.NeedToGoListener;
import org.apache.log4j.Logger;

/**
 * Central entry point to Winboard processing.
 * Singleton to prohibit irregularities
 * Author: Leonid
 * Date-time: 20.08.12 19:28
 */
public class WinboardPlayer implements Player {
	private NeedToGoListener needToGoListener;
	private Logger logger = Logger.getLogger( this.getClass() );
	private final WinboardCommander commander;
	private boolean needQuit = false;

	//TODO: THINK about consequences of:
	//creating several instances of the controller (must be singleton)
	//calling run several times (from different threads)

	/**
	 * Create instance on Winboard controller.
	 * @param winboardCommander instance of mid-level winboard-commander.
	 */
	WinboardPlayer( WinboardCommander winboardCommander ) {
		this.commander = winboardCommander;

		commander.onXBoard( new XBoardListener() {
			@Override
			public void execute() {
				logger.info( "Ready to work" );
			}
		} );

		commander.onQuit( new QuitListener() {
			@Override
			public void execute() {
				needQuit = true;
			}
		} );

		commander.onUserMove( new UserMoveListener() {
			@Override
			public void execute( String move ) {
				//TODO: prove this parameter passing
				needToGoListener.opponentMoved( move );
			}
		} );

		commander.onGo( new GoListener() {
			@Override
			public void execute() {
				needToGoListener.opponentMoved( null );
			}
		} );

		commander.onProtover( new ProtoverListener() {
			@Override
			public void execute( int protocolVersion ) {
				commander.enableUserMovePrefixes();
				commander.finishInit();

				logger.info( "Protocol version detected = " + protocolVersion );
			}
		} );

		//critically important to send this sequence at the start
		//to ensure the Winboard won't ignore our 'setfeature' commands
		//set feature commands must be sent in response to protover
		commander.startInit();
	}

	//may create attach - now it's over-projecting - 1 is OK
	@Override
	public void onOpponentMoved( NeedToGoListener needToGoListenerToSet ) {
		this.needToGoListener = needToGoListenerToSet;
	}

	/**
	 * Run main loop that works till winboard sends us termination signal
	 */
	@Override
	public void run() {
		while( true ) {
			commander.processInputFromServer();
			if ( needQuit ) {
				break;
			}
		}
	}

	@Override
	public void onOpponentOfferedDraw( final DrawOfferedListener listener ) {
		//low-level interface adapting to the high-level
		commander.onOfferDraw( new OfferDrawListener() {
			@Override
			public void execute() {
				listener.opponentOfferedDraw();
			}
		} );
	}

	//listener to another player's move
	@Override
	public void opponentMoved( String opponentMove ) {
		commander.opponentMoved( opponentMove );
	}

	@Override
	public void opponentOfferedDraw() {
		commander.offerDraw();
	}

	@Override
	public void opponentResigned() {
		commander.resign();
	}

	//TODO: validate legality of this method call!
	@Override
	public void opponentAgreedToDrawOffer() {
		commander.agreeToDrawOffer();
	}

	/**
	 * Check if we need break main loop
	 * @return true if main loop must be stopped
	 */
	boolean needShuttingDown() {
		return needQuit;
	}
}
