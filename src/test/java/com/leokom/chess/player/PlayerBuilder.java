package com.leokom.chess.player;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

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
    private Player player;

    private List< Move > movesToExecute = new ArrayList<>();
    private Position position;

    public PlayerBuilder( Player opponent ) {
        this.player = Mockito.mock( Player.class );
        this.opponent = opponent;
        this.position = Position.getInitialPosition();

        updatePositionByOpponentMove();
        //TODO: it will return the original position due to immutability
        when( player.getPosition() ).thenReturn( position );
    }

    private void updatePositionByOpponentMove() {
        ArgumentCaptor< Move > opponentMoveCaptor = ArgumentCaptor.forClass( Move.class );

        doAnswer( invocationOnMock -> {
            position = position.move( opponentMoveCaptor.getValue() );

            doMove();
            return null;
        } ).when( player ).opponentMoved( opponentMoveCaptor.capture() );
    }

    public PlayerBuilder move( Move move ) {
        this.movesToExecute.add( move );
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

        //next time we don't want the mock invoked again
        Move toBeDone = movesToExecute.remove(0);
        position = position.move( toBeDone );
        opponent.opponentMoved( toBeDone );
    }
}
