package com.leokom.chess;

import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.PositionBuilder;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.legalMover.LegalPlayer;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Author: Leonid
 * Date-time: 06.04.16 21:08
 */
public class SimulatorTest {
	private final Player first = mock( Player.class );
	private final Player second = mock( Player.class );

	private Position position = mock( Position.class );

	@Test
	public void runGame() {
		runSimulator();

		verify( second ).opponentSuggestsMeStartNewGameBlack();
		verify( first ).opponentSuggestsMeStartNewGameWhite();
	}

	@Test
	public void afterFinishRunAnotherGameReversed() {
		runSimulator();

		verify( first ).opponentSuggestsMeStartNewGameBlack();
		verify( second ).opponentSuggestsMeStartNewGameWhite();
	}

	@Test
	public void getStatistics() {
		SimulatorStatistics statistics = runSimulator();
		assertNotNull( statistics );
	}

	@Test
	public void statisticsReflectsReality() {
		this.position = new PositionBuilder().winningSide( Side.BLACK ).build();

		SimulatorStatistics statistics = runSimulator();
		assertEquals( 1, statistics.getFirstWins() );
		assertEquals( 1, statistics.getSecondWins() );
	}

	@Test
	public void twoDraws() {
		this.position = new PositionBuilder().draw().build();

		SimulatorStatistics statistics = runSimulator();
		assertEquals( 0, statistics.getFirstWins() );
		assertEquals( 0, statistics.getSecondWins() );
	}

	private SimulatorStatistics runSimulator() {
		//after correct game (or after full refactoring)
		//both players must return the same position : so we just stating this as a fact here
		when( first.getPosition() ).thenReturn( this.position );
		when( second.getPosition() ).thenReturn( this.position );

		return new Simulator( first, second ).run();
	}

	@Ignore( "long, probably need to move to IT" )
	@Test
	public void legalPlayerEqualProbableDraw() {
		new Simulator( new LegalPlayer(), new LegalPlayer() ).run();
	}
}