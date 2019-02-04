package com.leokom.chess.player.legal;

import com.leokom.chess.player.legal.brain.simple.SimplePlayerSupplier;
import com.leokom.chess.player.legal.brain.denormalized.DenormalizedBrain;
import com.leokom.chess.player.legal.brain.normalized.MasterEvaluator;
import com.leokom.chess.player.legal.brain.normalized.NormalizedBrain;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LegalPlayerNameTest {
    @Test
    public void denormalizedBrain() {
        assertEquals( "LegalPlayer : DenormalizedBrain", new LegalPlayer( new DenormalizedBrain() ).name() );
    }

    @Test
    public void normalizedBrain() {
        assertEquals( "LegalPlayer : NormalizedBrain: 1 depth", new LegalPlayer( new NormalizedBrain<>( new MasterEvaluator()) ).name() );
    }

    @Test
    public void simpleBrain() {
        assertEquals( "LegalPlayer : SimpleBrain", new SimplePlayerSupplier().get().name() );
    }
}
