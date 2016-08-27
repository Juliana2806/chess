package com.leokom.chess.player.legal.evaluator.common;

import com.leokom.chess.player.legal.evaluator.denormalized.DenormalizedEvaluatorFactory;
import com.leokom.chess.player.legal.evaluator.normalized.NormalizedEvaluatorFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

/**
 * Author: Leonid
 * Date-time: 27.08.16 15:23
 */
@RunWith( Parameterized.class )
public abstract class EvaluatorTestCase {
	protected EvaluatorAsserts asserts;

	abstract EvaluatorType getEvaluatorType();

	@Parameterized.Parameters
	public static Iterable< EvaluatorFactory > parameters() {
		return Arrays.asList( new NormalizedEvaluatorFactory(),
				new DenormalizedEvaluatorFactory() );
	}

	@Parameterized.Parameter
	public /*for field injection to work*/ EvaluatorFactory evaluatorFactory;

	@Before
	public final void prepareMe() {
		asserts = new EvaluatorAsserts( evaluatorFactory.get( getEvaluatorType() ) );
	}
}
