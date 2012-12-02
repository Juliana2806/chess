package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Communicator;
import org.junit.Test;

import static com.leokom.chess.gui.winboard.MockCommunicatorReceiveCreator.getReceiveCommunicator;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Author: Leonid
 * Date-time: 13.11.12 21:33
 */
public class WinBoardCommanderReceiveTest {
	@Test
	public void xboard() {
		Communicator communicator = getReceiveCommunicator( "xboard" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final XBoardListener listener = mock( XBoardListener.class );
		commander.setXboardListener( listener );

		commander.processInputFromServer();

		verify( listener ).execute();
	}

	@Test
	public void opponentOffersDraw() {
		Communicator communicator = getReceiveCommunicator( "draw" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final OfferDrawListener listener = mock( OfferDrawListener.class );
		commander.setOfferDrawListener( listener );

		commander.processInputFromServer();

		verify( listener ).execute();
	}

	//TODO: I implement only simple test for usermove for 2 reasons:
	//1. I want to check if pitest finds it
	//2. I need to check deeper what's the correct format
	@Test
	public void userMove() {
		Communicator communicator = getReceiveCommunicator( "usermove e2e4" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final UserMoveListener listener = mock( UserMoveListener.class );
		commander.setUserMoveListener( listener );

		commander.processInputFromServer();

		verify( listener ).execute();
	}

	@Test
	public void nonGoReceived() {
		Communicator communicator = getReceiveCommunicator( "non-go line" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final GoListener listener = mock( GoListener.class );
		commander.setGoListener( listener );

		commander.processInputFromServer();

		verify( listener, never() ).execute();
	}

	@Test
	public void goReceived() {
		Communicator communicator = getReceiveCommunicator( "go" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final GoListener listener = mock( GoListener.class );
		commander.setGoListener( listener );

		commander.processInputFromServer();

		verify( listener ).execute();
	}

	@Test
	public void goReceivedNoListenerConnection() {
		Communicator communicator = getReceiveCommunicator( "go" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final GoListener listener = mock( GoListener.class );

		commander.processInputFromServer();

		verify( listener, never() ).execute();
	}

	@Test
	public void noProtoverLineSent() {
		Communicator communicator = getReceiveCommunicator( "Any line not starting by 'protover'" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final ProtoverListener listener = mock( ProtoverListener.class );
		commander.setProtoverListener( listener );

		commander.processInputFromServer();

		verify( listener, never() ).execute( anyInt() );
	}

	@Test
	public void protoverLineSent() {
		Communicator communicator = getReceiveCommunicator( "protover 2" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final ProtoverListener listener = mock( ProtoverListener.class );
		commander.setProtoverListener( listener );

		commander.processInputFromServer();

		//NOTE: nice, richer checking than before (argument as well)!
		verify( listener ).execute( 2 );
	}

	@Test
	public void protoverLineWithVersionSent() {
		Communicator communicator = getReceiveCommunicator( "protover 2" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final ProtoverListener listener = mock( ProtoverListener.class );
		commander.setProtoverListener( listener );

		commander.processInputFromServer();

		//NOTE: nice, richer checking than before (argument as well)!
		verify( listener ).execute( 2 );
	}

	@Test
	public void listenerNotSetNoCalls() {
		Communicator communicator = getReceiveCommunicator( "protover" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final ProtoverListener listener = mock( ProtoverListener.class );
		commander.processInputFromServer();

		verify( listener, never() ).execute( anyInt() );
	}

	@Test
	public void protoverLineSentNoInputNoCalls() {
		Communicator communicator = getReceiveCommunicator( "protover" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final ProtoverListener listener = mock( ProtoverListener.class );
		commander.setProtoverListener( listener );

		verify( listener, never() ).execute( anyInt() );
	}

	@Test
	public void nonQuitLine() {
		Communicator communicator = getReceiveCommunicator( "anyNonQuitString" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final QuitListener listener = mock( QuitListener.class );
		commander.setQuitListener( listener );

		commander.processInputFromServer();

		verify( listener, never() ).execute();
	}

	@Test
	public void quitLine() {
		Communicator communicator = getReceiveCommunicator( "quit" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final QuitListener listener = mock( QuitListener.class );
		commander.setQuitListener( listener );

		commander.processInputFromServer();

		verify( listener ).execute();
	}

	@Test
	public void quitWithoutListenerSet() {
		Communicator communicator = getReceiveCommunicator( "quit" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		//creating but not setting to commander
		final QuitListener listener = mock( QuitListener.class );

		commander.processInputFromServer();

		verify( listener, never() ).execute();
	}
}