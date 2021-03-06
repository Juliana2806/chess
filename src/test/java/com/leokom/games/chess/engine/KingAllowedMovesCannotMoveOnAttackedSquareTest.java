package com.leokom.games.chess.engine;

import org.junit.Test;

/**
 * Test for phrase 'not attacked by one or more of the opponent’s pieces'
 * in 3.8 a
 * Author: Leonid
 * Date-time: 09.07.13 22:17
 */
public class KingAllowedMovesCannotMoveOnAttackedSquareTest {
	@Test
	public void withPawnNotPromotion() {
		PositionBuilder position = new PositionBuilder();
		position.addPawn( Side.WHITE, "a2" ); //attacks b3
		position.add( Side.BLACK, "a3", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
			position, "a3",
			"a2", "a4", "b4", "b2" );

	}

	@Test
	public void withPawnPromotion() {
		PositionBuilder position = new PositionBuilder();
		position.addPawn( Side.BLACK, "h2" ); //may be promoted to h1 or capture g1
		position.add( Side.WHITE, "f1", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "f1",
				"e1", "e2", "f2", "g2" ); //but not g1
	}

	@Test
	public void knightAttacked() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.BLACK, "c3", PieceType.KNIGHT ); //controls b1, a2

		position.add( Side.WHITE, "a1", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "a1",
				"b2" );
	}

	@Test
	public void ourSideKnightIsNotAttacker() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "c3", PieceType.KNIGHT ); //controls b1, a2

		position.add( Side.WHITE, "a1", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "a1",
				"b2", "b1", "a2" );
	}

	@Test
	public void knightAndPawnAttack() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.BLACK, "c3", PieceType.KNIGHT ); //controls b1, a2
		position.add( Side.BLACK, "a2", PieceType.PAWN ); //controls b1

		position.add( Side.WHITE, "a1", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "a1"
				, "b2");
	}

	@Test
	public void knightAndPawnAttackNoWay() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.BLACK, "c3", PieceType.KNIGHT ); //controls b1, a2
		position.add( Side.BLACK, "a3", PieceType.PAWN ); //controls b2

		position.add( Side.WHITE, "a1", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "a1" );
	}

	@Test
	public void cannotCaptureIfControlledByPawn() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "g7", PieceType.PAWN );
		position.add( Side.WHITE, "h6", PieceType.PAWN ); //protects the pawn

		position.add( Side.BLACK, "f7", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "f7",
				"g8", "e8", "e7", "e6", "f6", "g6" ); //but not protected g7
	}

	@Test
	public void canCaptureAPawnThatControlsOtherPiece() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "g8", PieceType.KNIGHT );
		position.add( Side.WHITE, "h7", PieceType.PAWN ); //protects the knight

		position.add( Side.BLACK, "h8", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "h8",
				"g7", "h7" ); //cannot capture the protected rook, but can capture the protector: pawn
	}

	@Test
	public void bishopProtected() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "c3", PieceType.BISHOP ); //controls b2, a1
		position.add( Side.BLACK, "a2", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "a2",
				"a3", "b1", "b3" );
	}

	@Test
	public void bishopProtectedCannotCapture() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "c3", PieceType.BISHOP ); //controls b2, a1
		position.add( Side.WHITE, "a1", PieceType.BISHOP ); //controls b2, a1
		position.add( Side.BLACK, "a2", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "a2",
				"a3", "b1", "b3" );
	}

	@Test
	public void rookControl() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "b3", PieceType.ROOK ); //controls file b, rank 3
		position.add( Side.BLACK, "a1", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "a1",
				"a2" ); //b is protected
	}

	@Test
	public void rookProtectsOwnPiece() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "c2", PieceType.ROOK );
		position.add( Side.WHITE, "a2", PieceType.PAWN );

		position.add( Side.BLACK, "a1", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "a1",
				"b1" ); //2 is protected
	}

	//this scenario describes an important case:
	//the king tries to move to a square that WAS NOT initially attacked (because king itself prevented the attack)
	//but now it's attacked. FIDE rules have some ambiguity in 3.8 (solved completely in 3.9)
	@Test
	public void integrationOfRookAndPawn() {  //they greatly reduce king's possibilities
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "g8", PieceType.ROOK );
		position.add( Side.WHITE, "h7", PieceType.PAWN ); //protects the rook

		position.add( Side.BLACK, "f8", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "f8",
				"e7", "f7" );
	}

	@Test
	public void bishopAttackFromCheckToCheck() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "f6", PieceType.BISHOP );

		position.add( Side.BLACK, "h8", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "h8",
				"h7", "g8" ); //g7 is under check
	}

	@Test
	public void bishopAttackFromCheckToCheckCanCapture() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "f6", PieceType.BISHOP );

		position.add( Side.BLACK, "g7", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "g7",
				"f6", "f7", "f8", "g8", "g6", "h6", "h7" ); //h8 is under check
	}

	@Test
	public void queenAttacked() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "f6", PieceType.QUEEN );

		position.add( Side.BLACK, "h8", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "h8",
				"h7", "g8" ); //g7 is under check
	}

	@Test
	public void queenAttackedVertically() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "g5", PieceType.QUEEN );

		position.add( Side.BLACK, "h8", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "h8",
				"h7" ); //g is under queen's control
	}

	@Test
	public void queenProtectsFromCapture() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "g5", PieceType.QUEEN );
		position.add( Side.WHITE, "g8", PieceType.KNIGHT ); //protected

		position.add( Side.BLACK, "h8", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "h8",
				"h7" ); //g is under queen's control
	}

	@Test
	public void queenProtectsFromCaptureNoWayForKing() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "g6", PieceType.QUEEN );
		position.add( Side.WHITE, "g8", PieceType.KNIGHT ); //protected

		position.add( Side.BLACK, "h8", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "h8"
				 ); //g is under queen's control, h7 as well
	}

	@Test
	public void ownQueenIsNotAProblem() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "g8", PieceType.KNIGHT );

		position.add( Side.BLACK, "g6", PieceType.QUEEN ); //own queen
		position.add( Side.BLACK, "h8", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "h8",
				"g8", "g7", "h7"
		);
	}

	@Test
	public void kingCannotMeetAKing() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "a1", PieceType.KING );

		position.add( Side.BLACK, "c1", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "a1",
				"a2" //b1, b2 are controlled by the opponent's king
		);
	}
}
