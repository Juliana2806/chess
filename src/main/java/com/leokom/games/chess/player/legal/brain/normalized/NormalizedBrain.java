package com.leokom.games.chess.player.legal.brain.normalized;

import com.leokom.games.commons.engine.GameState;
import com.leokom.games.commons.engine.GameTransition;
import com.leokom.games.chess.engine.Move;
import com.leokom.games.chess.player.legal.brain.common.GenericBrain;
import com.leokom.games.commons.brain.GenericEvaluator;
import org.apache.logging.log4j.LogManager;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * Initial decision maker.
 *
 * Historically it was based on MasterEvaluator.
 * Now it has become generic (actually even not depending on chess-related notions).
 * You can inject any custom evaluator that acts as a normalized one via constructor.
 *
 * We assume that the evaluator always evaluates the move from the side to move the next ply
 * It was a logical assumption when we developed a 1-ply engine.
 * It can still be kept.
 * The alternative could be: stable evaluator that returns positive/negative result depending on color of the side to move
 *
 * @param <S> game state
 * @param <T> transition type
 *
 * Author: Leonid
 * Date-time: 23.08.16 22:54
 */
public class NormalizedBrain < S extends GameState<T, S>, T extends GameTransition> implements GenericBrain<S, T> {
	//this constant will increase with chess evolution
	private static final int MAXIMAL_SUPPORTED_DEPTH = 2;
	//this is an absolute constant
	private static final int MINIMAL_POSSIBLE_DEPTH = 1;
	private final GenericEvaluator<S, T> evaluator;
	private final int pliesDepth;

	/**
	 * Create normalized brain
	 * @param evaluator evaluator with results in [ 0, 1 ] range
	 */
	public NormalizedBrain( GenericEvaluator<S, T> evaluator ) {
		this( evaluator, 1 );
	}

	/**
	 * Create brain with custom plies depth
	 * @param evaluator evaluator with results in [ 0, 1 ] range
	 * @param pliesDepth depth to think (1 or 2 are supported)
	 */
	public NormalizedBrain(GenericEvaluator<S, T> evaluator, int pliesDepth ) {
		if ( pliesDepth < MINIMAL_POSSIBLE_DEPTH) {
			throw new IllegalArgumentException( String.format( "This depth is wrong: %s", pliesDepth ) );
		}

		if ( pliesDepth > MAXIMAL_SUPPORTED_DEPTH) {
			throw new IllegalArgumentException( String.format( "This depth is not supported yet: %s", pliesDepth ) );
		}

		//just 1 or 2 is supported now
		this.evaluator  = pliesDepth == 1 ?
			new ValidatingNormalizedEvaluator<>(
				evaluator
			) :
			new TwoPliesEvaluator<>(
				new ValidatingNormalizedEvaluator<>(
					evaluator
				)
			);
		this.pliesDepth = pliesDepth;
	}

	/**
	 * If there are several 'best' moves, one of them is returned.
	 * (which one of them is chosen is undefined.
	 * This selection is not guaranteed to be the same under same circumstances)

	 * @inheritDoc
	 * @param position
	 * @return the best move
	 */
	/*
	 * Thanks to possibility to have different results for the same possible moves
	 * we have a some kind of randomness without explicitly
	 * creating it.
	 * That means : even in the same position the current algorithm
	 * can select a different move (most likely thanks to unordered
	 * nature of Set)
	 *
	 */
	@Override
	public List<T> findBestMove( S position ) {
		//1. filtering Draw offers till #161 is solved
		//this looks safe since Offer draw cannot be a single legal move in a position.
		//the best place to filter is this decision maker because it's used both by Normalized and Denormalized branches

		//2. in future we may even not materialize the map and continue the Stream API chain to find the best move
		Map<T, Double> moveRatings =
			getMovesWithoutDrawOffer( position ).collect(
				toMap(
					identity(),
					move -> this.evaluator.evaluateMove( position, move )
				)
			);
		List<T> bestMove = getMoveWithMaxRating( moveRatings );
		LogManager.getLogger().info( "Best move(s): {}", bestMove );
		return bestMove;
	}

	@Override
	public String name() {
		return String.format( "NormalizedBrain: %s depth", pliesDepth );
	}

	private Stream<T> getMovesWithoutDrawOffer(S position) {
		return position.getMoves().stream().filter(move -> move != Move.OFFER_DRAW);
	}

	private List<T> getMoveWithMaxRating( Map<T, Double > moveValues ) {
		return moveValues.entrySet().stream()
				.max(Map.Entry.comparingByValue())
				.map(Map.Entry::getKey)
				.map( Collections::singletonList )
				.orElseGet( Collections::emptyList );
	}
}
