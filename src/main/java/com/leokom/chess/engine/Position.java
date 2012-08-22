package com.leokom.chess.engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Current position on-board (probably with some historical data...)
 * Author: Leonid
 * Date-time: 21.08.12 15:55
 */
public class Position {
	/**
	 * square -> side
	 */
	private Map< String, Side > sidesOccupied = new HashMap<String, Side>();

	/**
	 * Add a pawn to the position
	 * @param side
	 * @param square
	 */
	public void addPawn( Side side, String square ) {
		//TODO: what if the square is already occupied?
		sidesOccupied.put( square, side );
	}

	/**
	 * Get moves that are available from the square provided
	 * @param square square currently in format like 'e2'
	 * @return not-null set of available moves from square (could be empty for sure)
	 * TODO: what if square doesn't contain any pieces?
	 */
	public Set<String> getMovesFrom( String square ) {
		final HashSet<String> result = new HashSet<String>();
		char file = square.charAt( 0 ); //depends on format e2

		//TODO: this internal conversion is needed because char itself has its
		//numeric value
		final int row = Integer.valueOf( String.valueOf(square.charAt( 1 ) ));

		//NOTE: the possible NULL corresponds to to-do in javadoc
		switch ( sidesOccupied.get( square ) ) {
			case WHITE:

				if ( row == 2 ) {
					result.add( file + "3" );
					result.add( file + "4" );
				}
				else {
					int rowForPawn = row + 1;
					result.add( String.valueOf( file ) + rowForPawn );
				}
				break;
			case BLACK:
				result.add( file + "5" );
				result.add( file + "6" );
				break;
		}

		return result;

	}
}
