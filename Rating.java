package org.chess.pgn;

import java.util.*;

public class Rating {
	
	public int Performance (int nAverageRating, float fFractionalScore) {
		
		// if fractional score < 0 or > 1: throw exception
		
		// Fractional score must be rounded to two digits.
		
		// if averagerating <0 or > 100,000 : throw exception
		
		int[] anFractionalScoreToRatingDifference = { 0, 7, 14, 21, 29, 36, 43,
				50, 57, 65, 72, 80, 87, 95, 102, 110, 117, 125, 133, 141, 149,
				158, 166, 175, 184, 193, 202, 211, 220, 230, 240, 251, 262, 273,
				284, 296, 309, 322, 336, 351, 366, 383, 401, 422, 444, 470, 501,
				538, 589, 677, 800};
		
		int nFractionalScore = (int) (100 * fFractionalScore);
		
		if (nFractionalScore > 50) {
			return nAverageRating + anFractionalScoreToRatingDifference[nFractionalScore-50];
		}
		else {
			return nAverageRating - anFractionalScoreToRatingDifference[50-nFractionalScore];
		}

	}
	
	public int Performance (Stack<GameResult> staGameResult) {
//		int nN=0;
//		int nRatingSum=0;
//		int fScoreSum=0;
//		int nCurrentRating;
//		
//		while (!staGameResult.empty()) {
//			nCurrentRating=staGameResult.peek.pop().nOpponentRating;
//		}
//		
		return 0;
	}

}
