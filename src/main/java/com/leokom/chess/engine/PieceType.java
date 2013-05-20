package com.leokom.chess.engine;

/**
 * Define all possible piece types for chess
 * Author: Leonid
 * Date-time: 05.09.12 21:36
 */
public enum PieceType {
	/**
	 * The piece that initially looks weak but
	 * in team with other pawns and pieces could be very powerful
	 * It also could be promoted to other pieces except King
	 */
	PAWN( "" ),

	/**
	 * 3.6. The knight may move to one of the squares nearest to that on which it stands but not on the same rank, file or diagonal.
	 */
	KNIGHT ( "N" ),

	/**
	 * 3.2. The bishop may move to any square along a diagonal on which it stands.
	 */
	BISHOP( "B" ),

	/**
	 * 3.3 The rook may move to any square along the file or the rank on which it stands
	 * See also king description (when available)
	 * for castling
	 */
	ROOK( "R" ),

	/**
	 * The piece that is considered to be the strongest in most cases.
	 * However the game may continue without it (in contrary to game without king)
	 */
	QUEEN ( "Q" );

	private final String notation;

	/**
	 * Create piece type with internal standardized string notation
	 * @param notation string notation for the piece
	 */
	PieceType( String notation ) {
		this.notation = notation;
	}

	static PieceType byNotation( String notation ) {
		for ( PieceType pieceType : values() ) {
			if ( pieceType.notation.equals( notation ) ) {
				return pieceType;
			}
		}

		throw new IllegalArgumentException( "No piece type is known for notation: " + notation );
	}
}
