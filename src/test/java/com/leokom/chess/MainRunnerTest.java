package com.leokom.chess;

import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.simple.SimpleEnginePlayer;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainRunnerTest {
	@Test
	public void noSystemPropertiesDefaultPlayer() {
		final Player player = MainRunner.createPlayer( Side.WHITE );
		assertTrue( player instanceof SimpleEnginePlayer );
	}
}