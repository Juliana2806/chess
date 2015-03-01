package com.leokom.chess.player.winboard;

import com.leokom.chess.player.Player;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.stubbing.Stubber;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


/**
 * Author: Leonid
 * Date-time: 19.08.12 18:16
 */
public class WinBoardPlayerTest {
	private static final int PROTOCOL_VERSION = 2; //any??
	private static final int WAIT_TILL_QUIT = 5000;

	@Test
	public void offerDrawTransmittedToTheOpponent() {
		WinboardCommander commander = mock( WinboardCommander.class );
		final WinboardPlayer player = new WinboardPlayer( commander );
		final Player opponent = mock( Player.class );
		player.setOpponent( opponent );

		executeOfferDrawListener( commander ).when( commander ).processInputFromServer();

		commander.processInputFromServer();

		verify( opponent ).opponentOfferedDraw();
	}

	//this test should emulate WinBoard behaviour and analyze our reaction on it.
	//in theory in future we could extract some Winboard emulator

	@Test
	public void creationSwitchesToInitMode() {
		WinboardCommander commander = mock( WinboardCommander.class );

		//implicit call of startInit
		new WinboardPlayer( commander );

		//it really checks only 1 method call
		verify( commander ).startInit();
	}

	//ensure need of refactoring into commander instead of communicator
	@Test( timeout = WAIT_TILL_QUIT )
	public void useCommanderForQuitCommand() {
		final Communicator quitCommunicator = mock( Communicator.class );
		stub( quitCommunicator.receive() ).toReturn( "quit" );

		WinboardCommander commander = new WinboardCommanderImpl( quitCommunicator );

		WinboardPlayer player = new WinboardPlayer(	commander );

		player.opponentSuggestsMeStartNewGameWhite();
	}

	@Test
	public void quitCommandSwitchesShutdownNeed() {
		WinboardCommander commander = mock( WinboardCommander.class );

		WinboardPlayer player = new WinboardPlayer(	commander );
		ArgumentCaptor<QuitListener> quitListener = ArgumentCaptor.forClass( QuitListener.class );
		verify( commander ).onQuit( quitListener.capture() );

		assertFalse( player.needShuttingDown() );

		//correct quit listener must enable need of shutting down
		quitListener.getValue().execute();

		assertTrue( player.needShuttingDown() );
	}

	/**
	 * Correct quit listener must set up inner flag to quit
	 */
	@Test
	public void quitListenerActsCorrectly() {
		//dummy implementation - each time anybody sets a protover listener -
		//we quit IMMEDIATELY
		final WinboardCommander commander = mock( WinboardCommander.class );

		final ArgumentCaptor<QuitListener> quitListener = ArgumentCaptor.forClass( QuitListener.class );
		final WinboardPlayer winboardPlayer = new WinboardPlayer( commander );
		verify( commander ).onQuit( quitListener.capture() );

		quitListener.getValue().execute();

		assertTrue( winboardPlayer.needShuttingDown() );
	}

	//ensure need of refactoring into commander instead of communicator
	//this is an integration test to ensure the loop won't be infinite
	//after receiving quit command
	@Test( timeout = WAIT_TILL_QUIT )
	public void useCommanderForQuitCommandRealTest() {
		//dummy implementation - each time anybody sets a protover listener -
		//we quit IMMEDIATELY
		final WinboardCommander commander = mock( WinboardCommander.class );

		final Player winboardPlayer = new WinboardPlayer( commander );

		executeQuitListener( commander ).when( commander ).processInputFromServer();

		winboardPlayer.opponentSuggestsMeStartNewGameWhite();
	}

	//TODO: very similar to executeQuitListener...
	private static Stubber executeOfferDrawListener( WinboardCommander commander ) {
		final ArgumentCaptor< OfferDrawListener > offerDrawListener = ArgumentCaptor.forClass( OfferDrawListener.class );
		verify( commander ).onOfferDraw( offerDrawListener.capture() );

		return doAnswer( invocationOnMock -> {
			offerDrawListener.getValue().execute();
			return null; //just for compiler... due to generic Answer interface
		} );
	}

	private static Stubber executeQuitListener( WinboardCommander commander ) {
		final ArgumentCaptor<QuitListener> quitListener = ArgumentCaptor.forClass( QuitListener.class );

		verify( commander ).onQuit( quitListener.capture() );

		return doAnswer( invocationOnMock -> {
			quitListener.getValue().execute();
			return null;  //just for compiler... due to generic Answer interface
		} );
	}

	/**
	 * validate that our WinboardPlayer
	 * in its constructor
	 * initializes the commander's protover listener
	 * in which, sets up our desired winboard properties
	 * and finishes the initialization
	 */
	@Test
	public void correctProtoverReaction() {
		//dummy implementation - each time anybody sets a protover listener -
		//we call it IMMEDIATELY
		WinboardCommander commander = mock( WinboardCommander.class );
		ArgumentCaptor< ProtoverListener > listenerCaptor = ArgumentCaptor.forClass( ProtoverListener.class );
		new WinboardPlayer(	commander );

		//the player must have set its listener in constructor...
		verify( commander ).onProtover( listenerCaptor.capture() );

		//calling the protover listener - it must have implications described below.
		listenerCaptor.getValue().execute( PROTOCOL_VERSION );

		InOrder orderedCalls = inOrder( commander );

		orderedCalls.verify( commander ).enableUserMovePrefixes();
		orderedCalls.verify( commander ).finishInit();
	}
}
