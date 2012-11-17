package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Communicator;

/**
 * Middle-level command work for Winboard.
 * It must know about commands format.
 * Probably it mustn't know about the commands sequence
 * (but this will be clarified by TDD)
 *
 *
 * When we'll need it for test purposes - we'll extract interface to test more
 * high-level component.
 *
 * Author: Leonid
 * Date-time: 10.11.12 21:22
 */
class WinboardCommanderImpl implements WinboardCommander {
	private Communicator communicator;
	private ProtoverListener protoverListener;
	private QuitListener quitListener;

	/**
	 * Create the commander, with communicator injected
	 *
	 * @param communicator low-level framework to use to send/receive the commands
	 */
	public WinboardCommanderImpl( Communicator communicator ) {
		this.communicator = communicator;
	}

	/**
	 * Switches Winboard engine in 'features set up mode'
	 */
	@Override
	public void startInit() {
		communicator.send( "feature done=0" );
	}

	@Override
	public void enableUserMovePrefixes() {
		communicator.send( "feature usermove=1" );
	}

	@Override
	public void finishInit() {
		communicator.send( "feature done=1" );
	}

	@Override
	public void agreeToDrawOffer() {
		//'draw': The engine's opponent offers the engine a draw.
		// To accept the draw, send "offer draw".

		//TODO: need implement some method ignoreDrawOffer?
		// To decline, ignore the offer (that is, send nothing).
		communicator.send( "offer draw" );
	}

	@Override
	public void setProtoverListener( ProtoverListener protoverListener ) {
		this.protoverListener = protoverListener;
	}

	@Override
	public void setQuitListener( QuitListener listener ) {
		this.quitListener = listener;
	}

	@Override
	public void getInput() {
		String whatToReceive = communicator.receive();
		if ( whatToReceive.startsWith( "protover" ) && protoverListener != null ) {
			protoverListener.execute();
		}
		if ( whatToReceive.equals( "quit" ) && quitListener != null ) {
			quitListener.execute();
		}
	}

	@Override
	public Communicator getCommunicator() {
		return this.communicator;
	}
}
