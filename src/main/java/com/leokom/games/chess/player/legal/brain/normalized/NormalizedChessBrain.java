package com.leokom.games.chess.player.legal.brain.normalized;

import com.leokom.games.chess.engine.Move;
import com.leokom.games.chess.engine.Position;
import com.leokom.games.chess.player.legal.brain.common.Evaluator;
import com.leokom.games.commons.brain.GenericBrain;
import com.leokom.games.commons.brain.normalized.NormalizedBrain;

import java.util.List;

public class NormalizedChessBrain implements GenericBrain<Position, Move> {
    private final NormalizedBrain<Position, Move> engine;

    public NormalizedChessBrain(Evaluator evaluator) {
        this( evaluator, 1 );
    }

    public NormalizedChessBrain(Evaluator evaluator, int depth) {
        this(
            new NormalizedBrain<>(
                evaluator,
                depth,
                move -> move != Move.OFFER_DRAW //we're not supporting draw offers so far
            )
        );
    }

    private NormalizedChessBrain(NormalizedBrain<Position, Move> engine) {
        this.engine = engine;
    }

    @Override
    public List<Move> findBestMove(Position position) {
        return engine.findBestMove( position );
    }

    @Override
    public Move findBestMoveForOpponent(Position position) {
        return engine.findBestMoveForOpponent( position );
    }

    @Override
    public String name() {
        return engine.name();
    }
}
