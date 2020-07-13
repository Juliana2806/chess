package com.leokom.games.chess.pgn;

import com.leokom.games.chess.Game;
import com.leokom.games.chess.engine.Side;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PGNGame {
    private final Event event;
    private final Game game;

    public PGNGame(Event event, Game game) {
        this.event = event;
        this.game = game;
    }

    public String run() {
        PGNTag eventTag = new PGNTag( "Event", (event.getName() != null ? event.getName() : "?") );
        PGNTag locationTag = new PGNTag( "Site", (event.getLocation() != null ? event.getLocation() : "?") );
        PGNTag dateTag = new PGNTag( "Date", ( event.getDate() != null ? DateTimeFormatter.ofPattern("yyyy-MM-dd").format( event.getDate() ) : "????-??-??" ) );
        PGNTag roundTag = new PGNTag( "Round", "-" );

        PGNTag whitePlayerTag = new PGNTag( "White", playerName(Side.WHITE));
        PGNTag blackPlayerTag = new PGNTag( "Black", playerName(Side.BLACK));

        return
            Stream.of( eventTag, locationTag, dateTag, roundTag, whitePlayerTag, blackPlayerTag )
            .map( PGNTag::toString )
            .collect(Collectors.joining( "\n" ) );

    }

    private String playerName(Side side) {
        return game.player(side).name() != null ? game.player(side).name() : "?";
    }
}
