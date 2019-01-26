package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.GameStateImpl;
import com.leokom.chess.engine.GameTransitionImpl;
import com.leokom.chess.player.legal.brain.common.GenericEvaluator;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NormalizedBrainTest {
    @Test
    public void noMovesNoBestMove() {
        GameStateImpl gameState = new GameStateImpl();

        List<GameTransitionImpl> result = new NormalizedBrain< GameStateImpl, GameTransitionImpl >((state, transition) -> 0).findBestMove(gameState);
        assertTrue( result.isEmpty() );
    }

    @Test
    public void singlePossibleMoveReturned() {
        int moveId = 12345;
        GameStateImpl gameState = new GameStateImpl( new GameTransitionImpl(moveId), new GameStateImpl() );

        List<GameTransitionImpl> result = new NormalizedBrain<GameStateImpl, GameTransitionImpl>((state, transition) -> 0).findBestMove(gameState);
        assertEquals( 1, result.size() );
        assertEquals( moveId, result.get(0).getId() );
    }

    @Test
    public void betterMoveFound() {
        GameStateImpl gameState = new GameStateImpl( new GameTransitionImpl(12), new GameStateImpl(),
                new GameTransitionImpl( 20 ), new GameStateImpl() );

        List<GameTransitionImpl> result = new NormalizedBrain<>(getSimpleIdEvaluator()).findBestMove(gameState);
        assertEquals( 1, result.size() );
        assertEquals( 20, result.get(0).getId() );
    }

    //we must not look to the 2'nd ply if we are limited by the 1'st one
    @Test
    public void singlePlyThinkingIsLimited() {
        GameStateImpl gameState = new GameStateImpl(
                new GameTransitionImpl(12), new GameStateImpl( new GameTransitionImpl( 0 ), new GameStateImpl() ),
                new GameTransitionImpl( 20 ), new GameStateImpl( new GameTransitionImpl( 100 ), new GameStateImpl() ) ); // bigger means better for the opponent

        List<GameTransitionImpl> result = new NormalizedBrain<>( getSimpleIdEvaluator()).findBestMove(gameState);
        assertEquals( 1, result.size() );
        assertEquals( 20, result.get(0).getId() );
    }

    //we must look to the 2'nd ply and detect a really better move
    @Test
    public void secondPlyThinkingMustSuggestBetterMove() {
        GameStateImpl gameState = new GameStateImpl(
                new GameTransitionImpl(12), new GameStateImpl( new GameTransitionImpl( 0 ), new GameStateImpl() ),
                new GameTransitionImpl( 20 ), new GameStateImpl( new GameTransitionImpl( 100 ), new GameStateImpl() ) );

        List<GameTransitionImpl> result = new NormalizedBrain<>( getSimpleIdEvaluator(),2).findBestMove(gameState);
        assertEquals( 1, result.size() );
        assertEquals( 12, result.get(0).getId() );
    }

    // just a simple evaluation - let's say bigger id is better
    private GenericEvaluator<GameStateImpl, GameTransitionImpl> getSimpleIdEvaluator() {
        return (state, transition) -> transition.getId() / 100.0;
    }

    @Test
    public void ifOpponentCanSelectCoolMoveDetectThat() {
        GameStateImpl gameState = new GameStateImpl(
                //here the opponent can execute an average move
                new GameTransitionImpl(12), new GameStateImpl( new GameTransitionImpl( 50 ), new GameStateImpl() ),
                //here he can execute a cool and a bad move. Thinking about him in positive way - that he'll select the best one
                new GameTransitionImpl( 20 ), new GameStateImpl( new GameTransitionImpl( 0 ), new GameStateImpl(), new GameTransitionImpl( 100 ), new GameStateImpl() ) );

        List<GameTransitionImpl> result = new NormalizedBrain<>( getSimpleIdEvaluator(),2 ).findBestMove(gameState);
        assertEquals( 1, result.size() );
        assertEquals( 12, result.get(0).getId() );
    }

    @Test
    public void secondPlyThinkingNoCrashOnTerminalPosition() {
        GameStateImpl gameState = new GameStateImpl( new GameTransitionImpl(25 ), new GameStateImpl()  //terminal
        );

        new NormalizedBrain< GameStateImpl, GameTransitionImpl >(
                (state, transition) -> transition.getId(),
                2
        ).findBestMove(gameState);
    }

    @Test
    public void singleMoveMustBeSelectableWhenNextIsTerminal() {
        GameStateImpl gameState = new GameStateImpl( new GameTransitionImpl(0 ), new GameStateImpl() );

        List<GameTransitionImpl> bestMove = new NormalizedBrain<GameStateImpl, GameTransitionImpl>(
                (state, transition) -> transition.getId(),
                2
        ).findBestMove(gameState);

        assertEquals( 1, bestMove.size() );
        assertEquals( 0, bestMove.get(0).getId() );
    }

    @Test
    public void movesLeadingToTerminalBetterToSelect() {
        GameStateImpl gameState = new GameStateImpl(
            new GameTransitionImpl(100 ), new GameStateImpl(),
            new GameTransitionImpl(0 ), new GameStateImpl(),
            new GameTransitionImpl(50 ), new GameStateImpl()
        );

        List<GameTransitionImpl> bestMove = new NormalizedBrain<GameStateImpl, GameTransitionImpl>(
                (state, transition) -> transition.getId(),
                2
        ).findBestMove(gameState);

        assertEquals( 1, bestMove.size() );
        assertEquals( 100, bestMove.get(0).getId() );
    }

    @Test( expected = IllegalArgumentException.class)
    public void depthMore2NotSupported() {
        new NormalizedBrain<GameStateImpl, GameTransitionImpl>( (state, transition) -> transition.getId(), 3 );
    }

    @Test( expected = IllegalArgumentException.class)
    public void depthLess1NotSupported() {
        new NormalizedBrain<GameStateImpl, GameTransitionImpl>( (state, transition) -> transition.getId(), 0 );
    }

    //if evaluator is not providing correct range for the move, we should throw an exception
    //TODO: it's semantically questionable, the evaluator was passed in constructor and we'll throw this from the method
    @Test( expected = IllegalArgumentException.class)
    public void evaluatorWithWrongResultMustBeDetected() {
        new NormalizedBrain<GameStateImpl, GameTransitionImpl>( ( state, transition ) -> 1.1 )
                .findBestMove( new GameStateImpl( new GameTransitionImpl(1), new GameStateImpl() ) );
    }
}