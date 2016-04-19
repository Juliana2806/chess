package com.leokom.chess.player.legalMover;

import java.util.Map;

/**
 * Create MasterEvaluator instances
 * with custom weights
 *
 * Author: Leonid
 * Date-time: 19.04.16 23:03
 */
public class MasterEvaluatorBuilder {
	private Map<EvaluatorType, Double > weights = MasterEvaluator.getStandardWeights();

	public MasterEvaluatorBuilder weight( EvaluatorType evaluatorType, double weight ) {
		weights.put( evaluatorType, weight );
		return this;
	}

	public MasterEvaluator build() {
		return new MasterEvaluator( weights );
	}
}