package org.chess.pgn;

//import org.chess.pgn.ChessGame;

public class RatedPGNGame extends PGNGame {
    private final int UNKNOWN=Integer.MAX_VALUE;
    private final int UNRATED=Integer.MAX_VALUE;


//    public String getEvent() {
//        return super.getEvent();
//    }
  	
//    public String getDate() {
//    	return super.getDate();
//    }
	
//    public String getRound() {
//    	return super.getRound();
//    }
    
    public String getOpponent(String sUser) {
    	String sReturnValue="UNKNOWN";
    	
    	if (super.getWhite()==sUser) {
    		sReturnValue=super.getBlack();
    	}
    	else {
    		if (super.getBlack()==sUser) {
    			sReturnValue=super.getWhite();
    		}
    	}
    	
    	return sReturnValue;
    }

    /**
     * Compute the development coefficient in Rn = Ro + K x (W - We).
     * @param nGameNumber Games already played by the player + 1.
     * @param fRi Intermediary rating (old rating + sum of the rating changes). The first Ri = Ro, the last Ri=Rn.
     */
    public int getK(int nGameNumber, double dbRi) {
        int nK=32;
        
        // During computation K may vary on nGameNumber and on Ri.
        
        if (dbRi>2200) {
        	nK=10;
        }
        else {
	        if (nGameNumber>300) {
	        	if (dbRi>2000) {
	        		nK=12;
	        	}
	        	else {
	        		nK=16;
	        	}
	        }
	        else {
	            if (nGameNumber>100) {
	                nK=24;
	            }
	            else {
	            	if (nGameNumber>19) {
	            		nK=32;
	            	}
	            	else {
	            		nK=UNKNOWN;	// Highest int Java can represent.
	            	}
	            }
	        }
        }
        
        return nK;
    }    
    
    
    public double getW(String sUser) {
    	double dbReturnValue=0;

    	if (super.getResult()=="1/2-1/2") {
    		dbReturnValue=0.5;
    	}
    	else {
	    	if (super.getResult()=="1-0" && super.getWhite()==sUser) {
	    			dbReturnValue=1;
	    	}
	    	else {
		    	if (super.getResult()=="0-1" && super.getBlack()==sUser) {
	    			dbReturnValue=1;
		    	}
	    	}
    	}
    	
    	return dbReturnValue;
    }

    public float getWe(String sUser) {
    	 
        // DETERMINE PLAYER COLOR AND DISCARD IF NEEDED.
        int nUserRating;
        int nOpponentRating;
        if (super.getWhite()==sUser) {
            nUserRating=Integer.parseInt(super.getCustomTagPair("WhiteFRBEKBSB"));			// nUserRating=Integer.parseInt(super.getWhiteFRBEKBSB());
            nOpponentRating=Integer.parseInt(super.getCustomTagPair("BlackFRBEKBSB"));		// nOpponentRating=Integer.parseInt(super.getBlackFRBEKBSB());
        }
        else {
           if (super.getBlack()==sUser) {
              nUserRating=Integer.parseInt(super.getCustomTagPair("BlackFRBEKBSB"));		// nUserRating=Integer.parseInt(super.getBlackFRBEKBSB());
              nOpponentRating=Integer.parseInt(super.getCustomTagPair("WhiteFRBEKBSB"));	// nOpponentRating=Integer.parseInt(super.getWhiteFRBEKBSB());

           }
           else {
              return 0;
           }
        }


        // DISCARD IF A RATING IS MISSING.
        if (nUserRating==UNRATED || nUserRating==UNKNOWN || nOpponentRating==UNRATED || nOpponentRating==UNKNOWN) {
           return 0;
        } 

    	int nRatingDifference=Math.abs(nUserRating - nOpponentRating);

//      private static int Pd(int nDifference) {
        int[] anDifference={ 4, 11, 18, 26, 33, 40, 47, 54, 62, 69, 77, 84, 92, 99,
        		107, 114, 122, 130, 138, 146, 154, 163, 171, 180, 189, 198, 207, 216,
        		226, 236, 246, 257, 268, 279, 291, 303, 316, 329, 345};
//            int[] anDifference={ 4, 11, 18, 26, 33, 40, 47, 54, 62, 69, 77, 84, 92, 99,
//            		107, 114, 122, 130, 138, 146, 154, 163, 171, 180, 189, 198, 207, 216,
//            		226, 236, 246, 257, 268, 278, 291, 303, 318, 329, 345};
//            
        int nI=0;
        for (; nRatingDifference>anDifference[nI]; nI++);
        
        float fWe=(float)(50+nI)/100; 
        if (nUserRating<nOpponentRating) {
           fWe=1-fWe;
        }
        
        return fWe;
	}


	public double computeRatingChange(String sUser, int nGameNumber, double dbRi) {
		return getK(nGameNumber, dbRi) * (getW(sUser) - getWe(sUser));
	}
     
	public int computePerformance(int OpponentRating, float fW) {
		
//	    private static int Dp(int kfkdfk) {
//	    	int[] anFractionalScoreToRatingDifference = { 0, 7, 14, 21, 29, 36, 43, 50,
//	    			57, 65, 72, 80, 87, 95, 102, 110, 117, 125, 133, 	141, 149, 158,
//	    			166, 175, 184, 193, 202, 211, 220, 230, 240, 251, 262, 273, 284,
//	    			296, 309, 322, 336, 351, 366, 383, 401, 422, 444, 470, 501, 538,
//	    			589, 677, 800};

		return 0;
		
	}
	
    
//    private int Pd(int nDifference) {
//        int[] anDifference={ 4, 11, 18, 26, 33, 40, 47, 54, 62, 69, 77, 84, 92, 99,
//        		107, 114, 122, 130, 138, 146, 154, 163, 171, 180, 189, 198, 207, 216,
//        		226, 236, 246, 257, 268, 279, 291, 303, 316, 329, 345};
//        }
//        
//        for (int nI=0; nDifference>anDifference(nI); nI++);
//        return nI;
//    }

    
    
    
    
    
    
}
