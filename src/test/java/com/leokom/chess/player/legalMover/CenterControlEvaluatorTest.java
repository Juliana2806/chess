package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.PieceType;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import org.junit.Before;
import org.junit.Test;

public class CenterControlEvaluatorTest {
	private CenterControlEvaluator evaluator;
	private EvaluatorAsserts asserts;

	@Before
	public void prepare() {
		evaluator = new CenterControlEvaluator();
		asserts = new EvaluatorAsserts( evaluator );
	}

	@Test
	public void evaluateMove() {
		Position position = new Position( null );
		position.add( Side.WHITE, "c3", PieceType.KING );

		//controls d5, d4
		Move centerControlMove = new Move( "c3", "c4" );

		Move notCenterControlMove = new Move( "c3", "b2" );

		asserts.assertFirstBetter( position, centerControlMove, notCenterControlMove );
	}

	@Test
	public void givenKnightShouldD5ProveImportance() {
		Position position = new Position( null );
		position.add( Side.WHITE, "a6", PieceType.KNIGHT );

		Move toAttackD5 = new Move( "a6", "c7" );
		Move toGoToBorder = new Move( "a6", "b8" );

		asserts.assertFirstBetter( position, toAttackD5, toGoToBorder );
	}

	@Test
	public void blackKingToCenter() {
		Position position = new Position( null );
		position.add( Side.BLACK, "e7", PieceType.KING );

		//controls d5, e5
		Move centerControlMove = new Move( "e7", "e6" );

		Move notCenterControlMove = new Move( "e7", "d7" );

		asserts.assertFirstBetter( position, centerControlMove, notCenterControlMove );
	}

	@Test
	public void kingNearCenterBetterThanToBorder() {
		Position position = new Position( null );
		position.add( Side.BLACK, "b5", PieceType.KING );

		Move toCenter = new Move( "b5", "c5" );
		Move toBorder = new Move( "b5", "a5" );

		asserts.assertFirstBetter( position, toCenter, toBorder );
	}

	@Test
	public void kingIsCoolAtC3() {
		Position position = new Position( null );
		position.add( Side.BLACK, "b3", PieceType.KING );

		//attacks d4
		Move toCenter = new Move( "b3", "c3" );
		Move toBorder = new Move( "b3", "b2" );

		asserts.assertFirstBetter( position, toCenter, toBorder );
	}

	@Test
	public void f6AlsoGivesControl() {
		Position position = new Position( null );
		position.add( Side.WHITE, "f7", PieceType.KING );

		//attacks d4
		Move toCenter = new Move( "f7", "f6" );
		Move toBorder = new Move( "f7", "g8" );

		asserts.assertFirstBetter( position, toCenter, toBorder );
	}

	@Test
	public void rookTriangulate() {
		Position position = new Position( null );
		position.add( Side.WHITE, "f7", PieceType.ROOK );

		Move toEFile = new Move( "f7", "e7" );
		Move toSquareDrawnByKingInTestsAbove = new Move( "f7", "f6" );

		asserts.assertFirstBetter( position, toEFile, toSquareDrawnByKingInTestsAbove );
	}
}