package com.leokom.games.chess.engine;

/**
 * Pawn test utilities
 * Author: Leonid
 * Date-time: 25.08.12 18:25
 */
final class PawnUtils {
	private PawnUtils() {}

	/**
	 * Add a pawn to the position,
	 * check if allowed moves are equal to allMoves
	 * @param position
	 * @param pawnPosition
	 * @param side
	 * @param allMoves
	 */
	static void testPawn( Position position, String pawnPosition, Side side, String... allMoves ) {
		position.add( side, pawnPosition, PieceType.PAWN );
		PositionAsserts.assertAllowedMoves( position, pawnPosition, allMoves );
	}

	static void testPawn( PositionBuilder positionBuilder, String pawnPosition, Side side, String... allMoves ) {
		Position position = positionBuilder.addPawn( side, pawnPosition ).setSideOf( pawnPosition ).build();
		PositionAsserts.assertAllowedMoves( position, pawnPosition, allMoves );
	}
}
