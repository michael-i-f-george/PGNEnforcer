package org.chess.pgn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import org.chess.pgn.FIDEPlayer.Activity;
import org.chess.pgn.FIDEPlayer.Sex;
import org.chess.pgn.FIDEPlayer.Title;

//
/**
 * Create a list of string, each string being a line of a flat file from the FIDE with ratings.
 * @author michael
 *
 */
public class OLD_FIDERatingList {
	List<String> mlstLine = new ArrayList<String>();

    public OLD_FIDERatingList(String sFileFullPath) {
    	retrieveFIDERatingList(sFileFullPath);
    }
    private void retrieveFIDERatingList(String sFIDEFile) {
    	String sLine;
    	BufferedReader brFIDE;
        try {
            brFIDE = new BufferedReader(new FileReader(sFIDEFile));
            while ((sLine=brFIDE.readLine())!=null) {
            	mlstLine.add(sLine);
            }
            brFIDE.close();                
            brFIDE = null;

        } catch (java.io.FileNotFoundException e) {
           System.out.println("FIDE file (\"" + sFIDEFile + "\") does not exist.");        
        } catch (java.io.IOException e) {
           e.printStackTrace();
        }         
    }
    private String removeFrenchAccents(String sSentence) {
    	// The two following arrays are ordered.
        final char[] acAccentedLetter = {'Ã','Á','À','Ä','Ã','Å','Ç','É','È','Ê','Ë','Î','Ì','Í','Ï','Ñ','Õ','Ö','Ò','Ô','Ó','Û','Ü','Û','Ù','Ú','Ý','â','ä','à','á','ã','ç','é','ê','è','ë','î','ï','ì','í','ñ','ô','ó','ò','õ','ö','ü','û','ù','ú','ý','ÿ'};  // Ordered. 
        final char[] acNotAccentedLetter = {'A','A','A','A','A','A','C','E','E','E','E','I','I','I','I','N','O','O','O','O','O','U','U','U','U','U','Y','a','a','a','a','a','c','e','e','e','e','i','i','i','i','n','o','o','o','o','o','u','u','u','u','y','y'};   // Ordered.
        char cCurrentChar;
        int nSearchResult;
        char[] acReturnValue = new char[sSentence.length()*2];	// Length doubled for ligatures (see below).
        
        int nJ=0;	// Cursor of the output string (acReturnValue[]).
        final int nSentenceLength=sSentence.length();
        for (int nI=-1; ++nI<nSentenceLength;) {
        	// Letter without accent.
            if (((cCurrentChar=sSentence.charAt(nI))>='a' && cCurrentChar<='z') || (cCurrentChar>='A' && cCurrentChar<='Z')) {
            	acReturnValue[nJ++]=cCurrentChar;
            }
            else {
            	// Accented letters.
            	// If binary search doesn't work anymore : order the accented characters by use (i.e. 'Ã©', 'Ã¨' at the beginning; 'Ã¿' at the end).
                if ((nSearchResult=Arrays.binarySearch(acAccentedLetter,cCurrentChar))>-1) {
                    acReturnValue[nJ++]=acNotAccentedLetter[nSearchResult];
                }
                else {
                	// Ligatures (aesc and ethel: Ã† Ã¦ Å’ Å“).
                    if (cCurrentChar=='œ') {
                    	acReturnValue[nJ++]='o';
                    	acReturnValue[nJ++]='e';
                    } else if (cCurrentChar=='Œ') {
                    	acReturnValue[nJ++]='O';
                    	acReturnValue[nJ++]='E';
                    } else if (cCurrentChar=='æ') {
                    	acReturnValue[nJ++]='a';
                    	acReturnValue[nJ++]='e';
                    } else if (cCurrentChar=='Æ') {
                    	acReturnValue[nJ++]='A';
                    	acReturnValue[nJ++]='E';
                    } else {
                    	// Other character ('.', ',', ...).
                    	acReturnValue[nJ++]=cCurrentChar;
                    }
                }
            }
        } 
        
        return new String(acReturnValue,0, nJ);
    }
    private int binarySearch(List<String> lstSearchedThrough, String sSearchedValue, int nStartOfSubstring, int nEndOfSubstring) {
		int nLowerBound = 0;
		int nMiddle;
		int nUpperBound = lstSearchedThrough.size() - 1;
		   
		while (nLowerBound < nUpperBound) {
			nMiddle = (nLowerBound + nUpperBound) / 2;
			   
//			assert(nMiddle < nUpperBound);
			
			if (lstSearchedThrough.get(nMiddle).substring(nStartOfSubstring, nEndOfSubstring).compareToIgnoreCase(sSearchedValue)<0) {
				nLowerBound = nMiddle + 1;
			}
			else {
				nUpperBound = nMiddle;
			}
		}
		   
		if ((nUpperBound == nLowerBound) && (lstSearchedThrough.get(nLowerBound).substring(nStartOfSubstring, nEndOfSubstring).equals(sSearchedValue))) {
			return nLowerBound;
		}
		else {
			return -1;
		}
	}
    public FIDEPlayer getFIDEPlayer(String sPlayerFullName) {
    	// REM: returning a String rather than an object is not faster. 
    	FIDEPlayer frReturnValue = null;
    	
    	
    	//
    	//
    	// TODO: TO AVOID ABSOLUTE VALUES FOR DEFINING COLUMS LIMITS (=> analysis of the first lines).
    	//
    	//
    	
    	
//    	// Fill the rest of player's name with blanks.
//    	sPlayerFullName = String.format("%-" + String.valueOf(43-10) + "s", sPlayerFullName);

   	
//    	int nFound = binarySearch(mlstLine, sPlayerFullName, 10, 43);
    	sPlayerFullName = this.removeFrenchAccents(sPlayerFullName);
    	int nFound = binarySearch(mlstLine, sPlayerFullName, 10, Math.min(43, sPlayerFullName.length()+10));
    	if (nFound>-1) {
    		frReturnValue = new FIDEPlayer();
//    		frReturnValue.setID(Integer.parseInt(mlstLine.get(nFound).substring(0, 8).trim()));
    		frReturnValue.setID(Integer.parseInt(mlstLine.get(nFound).substring(0, 8).trim()));
    		frReturnValue.setName(mlstLine.get(nFound).substring(10, 43));
    		String sTitle = mlstLine.get(nFound).substring(44, 46);
    		if (sTitle.equals("  ")) {			// Java bans switching on a String.
        		frReturnValue.setTitle(Title.NONE);
    		} else if (sTitle.equals("c ")) {
        		frReturnValue.setTitle(Title.CANDIDATE_MASTER);
    		} else if (sTitle.equals("f ")) {
        		frReturnValue.setTitle(Title.FIDE_MASTER);
    		} else if (sTitle.equals("m ")) {
        		frReturnValue.setTitle(Title.INTERNATIONAL_MASTER);    			
    		} else if (sTitle.equals("g ")) {
        		frReturnValue.setTitle(Title.GRANDMASTER);    			
    		} else if (sTitle.equals("wc")) {
        		frReturnValue.setTitle(Title.WOMAN_CANDIDATE_MASTER);    			
    		} else if (sTitle.equals("wf")) {
        		frReturnValue.setTitle(Title.WOMAN_FIDE_MASTER);    			
    		} else if (sTitle.equals("wm")) {
        		frReturnValue.setTitle(Title.WOMAN_INTERNATIONAL_MASTER);    			
    		} else if (sTitle.equals("wg")) {
        		frReturnValue.setTitle(Title.WOMAN_GRANDMASTER);    			
    		}
    		frReturnValue.setCountryCode(mlstLine.get(nFound).substring(48,51));
    		frReturnValue.setElo(Integer.parseInt(mlstLine.get(nFound).substring(53, 57)));
    		frReturnValue.setGamesPlayed(Integer.parseInt(mlstLine.get(nFound).substring(58,62).trim()));
    		frReturnValue.setBirthYear(Integer.parseInt(mlstLine.get(nFound).substring(64, 68)));
    		if (mlstLine.get(nFound).charAt(70)=='w') {
    			frReturnValue.setSex(Sex.FEMALE);
        		if (mlstLine.get(nFound).charAt(71)=='i') {
        			frReturnValue.setActivity(Activity.INACTIVE);
        		}
        		else {
        			frReturnValue.setActivity(Activity.ACTIVE);
        		}
    		}
    		else {
    			frReturnValue.setSex(Sex.MALE);
        		if (mlstLine.get(nFound).charAt(70)=='i') {
        			frReturnValue.setActivity(Activity.INACTIVE);
        		}
        		else {
        			frReturnValue.setActivity(Activity.ACTIVE);
        		}
   			
    		}
    		frReturnValue.setK(Integer.parseInt(mlstLine.get(nFound).substring(75, 77)));

    		// TODO: to fill the rest of the object (i.e. games played and flags).
    	}
    	
    	return frReturnValue;
    }
    
}
