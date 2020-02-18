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
	
	
	public double FIDERatingChange () {
		/* Rn = Ro + K x (W-We), where:
		Rating - Rating of a player.
			Rc - Opponent rating.
W - Score.
K val - K is the development coefficient.
K is the development coefficient.
K = 40 for a player new to the rating list until he has completed events with at least 30 games
K = 20 as long as a player's rating remains under 2400.
K = 10 once a player's published rating has reached 2400 and remains at that level subsequently, even if the rating drops below 2400.
K = 40 for all players until their 18th birthday, as long as their rating remains under 2300.

K = 20 for RAPID and BLITZ ratings all players. */
	}
	public double FRBEKBSBRatingChange(int nPlayerRating, int nGamePlayedByPlayer, int nOpponentRating) {
		int nD = nPlayerRating - nOpponentRating;
		/* K computation */
		if (nGamePlayedByPlayer>=300) {
			if (nPlayerRating>2200) {
				K=10;
			}
			else if (nPlayerRating>2000) {
				K=12;
			}
			else {
				K=16;
			}
		}
		else {	/* nGamePlayedByPlayer < 300 */
			if (nGamePlayedByPlayer>100) {
				K=24;
			}
			else {
				K=32;
			}
		}
		
		/*
		
    D        = Rtg Dif = la différence entre les .cotes ELO
    We H  = l'espérance de gain We pour le joueur à l'indice le plus élevé.
    We L   = l'espérance de gain We pour le joueur à l'indice le plus bas.
    (A noter qu'en Belgique, lorsque la différence de cote D dépasse 411, We H est plafonné à .92, et We L est donc toujours .08).

Cette formule exprime la méthode continue d'évaluation où les cotes sont ajustées après chaque nouvelle récolte de résultats. Les performances récentes y sont pondérées par les résultats antérieurs.

Le coefficient K matérialise la vitesse variable avec laquelle des changements arrivent dans la force et les résultats d'un joueur. Au début, quand les changements peuvent être rapides, K est élevé, et va diminuer au fur et à mesure de la carrière du joueur. Un K élevé donne plus de poids aux performances récentes, un K plus bas donne plus de poids aux résultats antérieurs.

Les valeurs assignées à K ont été tirées d'une étude mathématique du problème et varient suivant que le classement est annuel ou à intervalles plus rapprochés. Le Prof. Elo, avec qui le Dr Douha a eu un échange de correspondance, a recommandé les valeurs suivantes dans notre cas (2 classements par an) :

    K = 32 de la 21ème à la 100ème partie
    K = 24 de la 101ème à la 300ème partie

Après 300 parties

( L’ex-condition d’application pour K = 10 trouvait sa raison d’être dans le mise en parallèle ELO belge / ELO FIDE qui étaient attribués antérieurement aux joueurs ayant plus de 2200 ELO (explication historique du Dr Douha). L’élargissement de la plage de cotation FIDE la rend obsolète. De plus, par exemple, un joueur avec moins de 100 parties, dont la cote oscille autour de 2200, voit son K passer de 32 à 10 et de 10 à 32, ce qui peut engendrer des instabilités de sa cote.)

    K = 16 si la cote est <= 2000
    K = 12 si la cote est > 2000 & <= 2200
    K = 10 si la cote est > 2200.

Tout joueur qui, après 20 parties, n'obtient pas 1150 pts se voit automatiquement donner une cote de 1150.

Si la différence de cote entre deux joueurs dépasse 350 pts, elle est ramenée à 350.

Si deux joueurs sont à égalité de points, le joueur qui a le plus de parties enregistrées est classé le premier. */
		
		
	}
}
