package com.leokom.games.chess;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class PGNGameTest {
    @Test
    public void eventTag() {
        String pgn = new PGNGame(new Event(null, null )).run();
        assertThat( pgn,
            CoreMatchers.startsWith( "[Event \"?\"]" )
        );
    }

    @Test
    public void eventNameKnown() {
        String pgn = new PGNGame( new Event( "Good event", null ) ).run();
        assertEquals( "[Event \"Good event\"]", pgn.split( "\n" )[ 0 ] );
    }

    @Test
    public void lineSeparators() {
        String pgn = new PGNGame(new Event("Good event", null)).run();
        assertThat( pgn, CoreMatchers.containsString( "\n" ));
    }

    @Test
    public void locationUnknown() {
        String pgn = new PGNGame(new Event(null, null)).run();
        assertEquals( "[Site \"?\"]", pgn.split( "\n" )[ 1 ] );
    }

    @Test
    public void locationKnown() {
        String pgn = new PGNGame(new Event(null, "New York City, NY USA")).run();
        assertEquals( "[Site \"New York City, NY USA\"]", pgn.split( "\n" )[ 1 ] );
    }
}
