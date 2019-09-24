package com.leokom.games.chess.engine;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Represent chess-board (files/ranks/squares) but not position
 * Author: Leonid
 * Date-time: 15.09.12 18:48
 */
final class Board {

	static final char MINIMAL_FILE = 'a';
	static final char MAXIMAL_FILE = 'h';

	//so far it's not intended to create the boards,
	//however need to rethink this approach...
	private Board() {}

	/**
	 * Minimal rank from FIDE rules
	 */
	static final int MINIMAL_RANK = 1;
	/**
	 * Maximal rank from FIDE rules
	 */
	static final int MAXIMAL_RANK = 8;

	/**
	 * Get file where square is situated
	 * @param square square in format 'e2'
	 * @return file of square
	 */
	static char fileOfSquare( String square ) {
		return square.charAt( 0 );
	}

	static int rankOfSquare( String square ) {
		return Character.getNumericValue( square.charAt( 1 ) );
	}

	private static char fileTo( char file, HorizontalDirection direction, int shift ) {
		switch ( direction ) {
			case LEFT:
				return (char) ( file - shift );
			case RIGHT:
				return (char) ( file + shift );
			default:
				throw new IllegalArgumentException( "Direction is not supported: " + direction );
		}
	}

	//the cache has been introduced according to profiler's result:
	//string concatenation was rather slow
	private static final Table< Character, Integer, String > SQUARES;
	static {
		final ImmutableTable.Builder<Character, Integer, String> tableBuilder = new ImmutableTable.Builder<>();

		for ( char file = MINIMAL_FILE; file <= MAXIMAL_FILE; file++ ) {
			for ( int rank = MINIMAL_RANK; rank <= MAXIMAL_RANK; rank++ ) {
				tableBuilder.put( file, rank, String.valueOf( file ) + rank  );
			}
		}

		SQUARES = tableBuilder.build();
	}

	static Optional<String> squareTo( String square, HorizontalDirection horizontalDirection, int horizontalShift, VerticalDirection verticalDirection, int verticalShift ) {
		char file = fileOfSquare( square );
		int rank = rankOfSquare( square );

		char destinationFile = fileTo( file, horizontalDirection, horizontalShift );
		int destinationRank = rankTo( rank, verticalDirection, verticalShift );

		return Optional.ofNullable( square( destinationFile, destinationRank ) );
	}

	/**
	 * Get square for given file and rank
	 * @param file file
	 * @param rank rank
	 * @return square (null if a file or rank are invalid)
	 */
	static String square( char file, int rank ) {
		//optimized version of 'a' + 1 ==> "a1"
		//that is slow according to profiler
		//this also reduces pressure on GC
		return SQUARES.get( file, rank );
	}

	private static Optional<String> squareTo( String square, HorizontalDirection horizontalDirection ) {
		return squareTo( square, horizontalDirection, 1 );
	}

	private static Optional<String> squareTo( String square, HorizontalDirection horizontalDirection, int horizontalShift ) {
		return squareTo( square, horizontalDirection, horizontalShift, VerticalDirection.UP, 0 );
	}

	private static Optional<String> squareTo( String square, VerticalDirection verticalDirection ) {
		//the intermediate 2 params are unimportant. Need to improve
		return squareTo( square, HorizontalDirection.LEFT, 0, verticalDirection, 1 );
	}

	static Optional<String> squareTo( String square, Direction direction ) {
		switch ( direction ) {
			case UP: return squareTo( square, VerticalDirection.UP );
			case DOWN: return squareTo( square, VerticalDirection.DOWN );
			case LEFT: return squareTo( square, HorizontalDirection.LEFT );
			case RIGHT: return squareTo( square, HorizontalDirection.RIGHT );
			default: throw new IllegalArgumentException( "Unsupported direction: " + direction );
		}
	}

	private static int rankTo( int rank, VerticalDirection verticalDirection, int verticalShift ) {
		return
			verticalDirection == VerticalDirection.UP ?
			rank + verticalShift :
			rank - verticalShift;
	}

	static int rankTo( int rank, VerticalDirection verticalDirection ) {
		return rankTo( rank, verticalDirection, 1 );
	}

	/**
	 *
	 * @param firstSquare first square
	 * @param secondSquare second square
	 * @return true if squares are on the same file
	 */
	static boolean sameFile( String firstSquare, String secondSquare ) {
		return fileOfSquare( firstSquare ) == fileOfSquare( secondSquare );
	}

	private static Optional<String> squareDiagonally( String square, HorizontalDirection horizontalDirection, VerticalDirection verticalDirection, int squaresDiagonally ) {
		return squareTo(
				square, horizontalDirection, squaresDiagonally, verticalDirection, squaresDiagonally );
	}

	static Optional<String> squareDiagonally( String square, HorizontalDirection horizontalDirection, VerticalDirection verticalDirection ) {
		return squareDiagonally( square,horizontalDirection, verticalDirection, 1 );
	}

	static Stream<String> getSquaresBetween( char leftFile, char rightFile, int rank ) {
		//start is inclusive, excluding it explicitly by +1
		return IntStream.range( leftFile + 1, rightFile )
			.mapToObj( file -> square( ( char ) file, rank ) );
	}
}
