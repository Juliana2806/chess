package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Controller;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Check Winboard controller behaviour in combination with real
 * commander
 *
 * The only thing we allow to mock here is low-level communicator
 *
 * Author: Leonid
 * Date-time: 10.11.12 22:03
 */
public class WinBoardControllerIntegrationTest {
	@Test
	public void switchesWinboardToSetUpMode() {
		//The commander mock is actually EMULATOR OF Winboard behaviour!
		//TODO: think about Mockito usage?
		MockCommunicator communicator = new MockCommunicator();

		final Controller controller = new WinboardController( communicator, new WinboardCommanderImpl( communicator ) );

		assertEquals( 1, communicator.getSentCommands().size() );
		final String initializationString = "feature done=0";
		assertEquals( initializationString, communicator.getSentCommands().get( 0 ) );
	}
}
