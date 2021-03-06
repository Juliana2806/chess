package com.leokom.games.chess.engine;

import java.util.*;

import com.google.common.collect.ImmutableMap;

/**
 * Knowledge about the initial chess position might be received here
 */
final class InitialPosition {
	private InitialPosition() {}

	private static final int WHITE_PAWN_INITIAL_RANK = 2;
	private static final int BLACK_PAWN_INITIAL_RANK = 7;


	private static final Map< Side, Integer > PAWN_INITIAL_RANKS =
			ImmutableMap.of( Side.WHITE, WHITE_PAWN_INITIAL_RANK, Side.BLACK, BLACK_PAWN_INITIAL_RANK );

	private static final int WHITE_NOT_PAWN_INITIAL_RANK = 1;
	private static final int BLACK_NOT_PAWN_INITIAL_RANK = 8;
	private static final Map< Side, Integer > NOT_PAWN_INITIAL_RANKS = 
			ImmutableMap.of( Side.WHITE, WHITE_NOT_PAWN_INITIAL_RANK, Side.BLACK, BLACK_NOT_PAWN_INITIAL_RANK );

	static int getPawnInitialRank( Side side ) {
		return PAWN_INITIAL_RANKS.get( side );
	}

	//not-pawn has no good name - 'piece' can be used in that meaning but creates ambiguity
	static int getNotPawnInitialRank( Side side ) {
		return NOT_PAWN_INITIAL_RANKS.get( side );
	}

	static Position generate( Rules rules ) {
		final Position result = new Position( Side.WHITE, null );
		result.setRules( rules );

		final Set< String > initialRookFiles = new HashSet<>( Arrays.asList( "a", "h" ) );
		final Set< String > initialKnightFiles = new HashSet<>( Arrays.asList( "b", "g" ) );
		final Set< String > initialBishopFiles = new HashSet<>( Arrays.asList( "c", "f" ) );
		final String initialQueenFile = "d";
		final String initialKingFile = "e";

		for ( Side side: Side.values() ) {
			for (char file = Board.MINIMAL_FILE; file <= Board.MAXIMAL_FILE; file++ ) {
				result.add( side, String.valueOf( file ) + PAWN_INITIAL_RANKS.get( side ), PieceType.PAWN );
			}

			final int rank = NOT_PAWN_INITIAL_RANKS.get( side );

			for ( String rookFile : initialRookFiles ) {
				result.add( side, rookFile + rank, PieceType.ROOK );
			}

			for ( String bishopFile : initialBishopFiles ) {
				result.add( side, bishopFile + rank, PieceType.BISHOP );
			}

			for ( String knightFile : initialKnightFiles ) {
				result.add( side, knightFile + rank, PieceType.KNIGHT );
			}

			result.add( side, initialQueenFile + rank, PieceType.QUEEN );
			result.add( side, initialKingFile + rank, PieceType.KING );
		}

		return result;
	}

	static Position generate() {
		return generate( Rules.DEFAULT );
	}
}