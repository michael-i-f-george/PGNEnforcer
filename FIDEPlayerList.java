package org.chess.pgn;

import java.io.File;
import java.util.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.helpers.DefaultHandler;


/**
 * Retrieve players from the FIDE XML file.
 * @author michael
 *
 */
public class FIDEPlayerList {
	List<FIDEPlayer> mlstFIDEPlayer = null;

    public FIDEPlayerList(String sFileFullPath) {
		SAXParserFactory saxFIDERatingsFactory = SAXParserFactory.newInstance();
		try {
			SAXParser saxFIDERatingsParser = saxFIDERatingsFactory.newSAXParser();
			DefaultHandler saxFIDERatingsHandler = new XMLFIDERatingsHandler();
			saxFIDERatingsParser.parse(new File(sFileFullPath), saxFIDERatingsHandler);
			mlstFIDEPlayer = ((XMLFIDERatingsHandler) saxFIDERatingsHandler).getFIDERatings();
		} catch (Exception e) {
			javax.swing.JOptionPane.showMessageDialog(null, "Constructor of FIDEPlayerList: " + e.toString());
		}
    }
    /**
     * Used only for HSQLDB tests.
     * @return
     */
    public List<FIDEPlayer> getList() {
    	return mlstFIDEPlayer;
    }
    
    private String removeFrenchAccents(String sSentence) {
    	// The two following arrays are ordered.
        final char[] acAccentedLetter = {'�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�','�'};  // Ordered. 
        final char[] acNotAccentedLetter = {'A','A','A','A','A','A','C','E','E','E','E','I','I','I','I','N','O','O','O','O','O','U','U','U','U','Y','a','a','a','a','a','c','e','e','e','e','i','i','i','i','n','o','o','o','o','o','u','u','u','u','y','y'};   // Ordered.
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
            	// If binary search doesn't work anymore : order the accented characters by use (i.e. 'é', 'è' at the beginning; 'ÿ' at the end).
                if ((nSearchResult=Arrays.binarySearch(acAccentedLetter,cCurrentChar))>-1) {
                    acReturnValue[nJ++]=acNotAccentedLetter[nSearchResult];
                }
                else {
                	// Ligatures (aesc and ethel: Æ æ Œ œ).
                    if (cCurrentChar=='�') {
                    	acReturnValue[nJ++]='o';
                    	acReturnValue[nJ++]='e';
                    } else if (cCurrentChar=='�') {
                    	acReturnValue[nJ++]='O';
                    	acReturnValue[nJ++]='E';
                    } else if (cCurrentChar=='�') {
                    	acReturnValue[nJ++]='a';
                    	acReturnValue[nJ++]='e';
                    } else if (cCurrentChar=='�') {
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
    private int performBinarySearch(String sSearchedFullName) {
		int nLowerBound = 0;
		int nMiddle;
		int nUpperBound = mlstFIDEPlayer.size() - 1;

		sSearchedFullName = this.removeFrenchAccents(sSearchedFullName).toUpperCase();
		while (nLowerBound < nUpperBound) {
			nMiddle = (nLowerBound + nUpperBound) / 2;
			   
//			assert(nMiddle < nUpperBound);
			
//			if (mlstFIDEPlayer.get(nMiddle).getName().compareToIgnoreCase(sSearchedFullName)<0) {
			if (mlstFIDEPlayer.get(nMiddle).getName().compareTo(sSearchedFullName)<0) {
				nLowerBound = nMiddle + 1;
			}
			else {
				nUpperBound = nMiddle;
			}
		}
		   
		if ((nUpperBound == nLowerBound) && (mlstFIDEPlayer.get(nLowerBound).getName().equals(sSearchedFullName))) {
			return nLowerBound;
		}
		else {
			return -1;
		}
	}
    public FIDEPlayer getFIDEPlayer(String sPlayerFullName) {
    	FIDEPlayer frReturnValue = null;

    	int nFound = performBinarySearch(sPlayerFullName);
    	if (nFound>-1) {
    		frReturnValue = mlstFIDEPlayer.get(nFound);
    	}
    	
    	return frReturnValue;
    }
    public int getSize() {
    	return mlstFIDEPlayer.size();
    }
    
}
