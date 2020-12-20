package com.leokom.games.chess.player;

import com.leokom.games.chess.engine.Move;
import com.leokom.games.chess.engine.Position;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 *
 * Created by Leonid on 07.02.17.
 */
//create players for test purposes
//particularly similar to WinboardTestGameBuilder
public class PlayerBuilder {
    private final Player opponent;
    private final Player player;

    private final List< List< Move > > movesToExecute = new ArrayList<>();
    private Position position;

    public PlayerBuilder( Player opponent ) {
        this.player = Mockito.mock( Player.class );
        this.opponent = opponent;
        this.position = Position.getInitialPosition();

        updatePositionByOpponentMove();

        // the delayed calculation allows overcome immutability of the position 
        //it will return the actual field value at the time of the call
        when( player.getPosition() ).then( (invocationOnMock) -> position );
    }

    private void updatePositionByOpponentMove() {
        ArgumentCaptor< Move > opponentMoveCaptor = ArgumentCaptor.forClass( Move.class );

        doAnswer( invocationOnMock -> {
            // we cannot use the captor's getAllValues() because they increase with every mock invocation
            // so we crack just current mock invocation arguments here with this casting
            List< Move > moves = Arrays.stream( invocationOnMock.getArguments()).map( Move.class::cast ).collect( Collectors.toList() );
            moves.forEach(this::updatePosition);
            doMove();
            return null;
        } ).when( player ).opponentMoved( opponentMoveCaptor.capture() );
    }

    private void updatePosition( Move move ) {
        position = position.move(move);
    }

    public PlayerBuilder move( Move move ) {
        this.movesToExecute.add( singletonList( move ) );
        return this;
    }

    public PlayerBuilder moveMulti( Move ... moves ) {
        this.movesToExecute.add( asList( moves ) );
        return this;
    }

    public PlayerBuilder move( String from, String to ) {
        return this.move( new Move( from, to ) );
    }

    public Player build() {
        doAnswer(invocationOnMock -> {
            doMove();
            return null;
        }).when( player ).opponentSuggestsMeStartNewGameWhite();

        return player;
    }

    private void doMove() {
        if ( movesToExecute.isEmpty()  ) {
            return;
        }

        if ( position.isTerminal() ) {
            throw new IllegalStateException( "A move cannot executed from a terminal position. Moves to execute: " + movesToExecute );
        }

        //next time we don't want the mock invoked again
        List< Move > toBeDone = movesToExecute.remove(0);
        toBeDone.forEach(this::updatePosition);
        opponent.opponentMoved( toBeDone.toArray(new Move[]{}) );
    }
}
