package org.chess.pgn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.TreeMap;


public class PGNStream {

	// Use preferably readPGN1c().
	
	
	private final String NEW_LINE = System.getProperty("line.separator");

	
//	public void readPGNFile() {
//		
//		String sLine = "[White \"George, Michaï¿½l\"]";
//		
//		char[] acLine = sLine.toCharArray();
//        int nLineLength = acLine.length;
//        int nStartOfTagName;
//        int nEndOfTagName;
//        String sTagName;
//        int nStartOfTagValue;
//        String sTagValue;
//        StringBuilder sbMoveSection = new StringBuilder();
//       
//        // TAG SECTION.
//        int nPos=-1;
//        // Search left square bracket (skipping white space characters if necessary).
//        while (++nPos<nLineLength && acLine[nPos]==' ');
//        while (nPos<nLineLength && acLine[nPos]=='[') {
//                // Search beginning of tag name (skipping white space characters if necessary).
//                while (++nPos<nLineLength && acLine[nPos]==' ');
//                nStartOfTagName=nPos;
//                // Search beginning of the tag value.
//                while (++nPos<nLineLength && acLine[nPos]!='"');
//                if (nPos<nLineLength) {
//                        // Search end of the tag name (there may be 0+ white space characters between the tag name and the tag value.).
//                        nEndOfTagName=nPos;
//                        while (acLine[--nEndOfTagName]==' ');
//                        // Extract tag name.
//                        sTagName = String.copyValueOf(acLine, nStartOfTagName, nEndOfTagName-nStartOfTagName+1);
//                        nStartOfTagValue = nPos + 1;
//                        // Search end of the tag value.
//                        while (++nPos<nLineLength && acLine[nPos]!='"');
//                        if (nPos<nLineLength) {
//                                // Extract tag value.
//                                sTagValue = String.copyValueOf(acLine, nStartOfTagValue, nPos-nStartOfTagValue);
//                                // Search right square bracket (skipping white space characters if necessary).
//                                while (++nPos<nLineLength && acLine[nPos]==' ');
//                                if (nPos!=nLineLength && acLine[nPos] == ']') {
//
//                                        // STORE TAG PAIR.
//
//                                }
//                                while (++nPos<nLineLength && acLine[nPos]==' ');
//                        }
//                }
//        }
//       
//        // MOVE SECTION.
//        if (nPos<nLineLength) { // Empty line?
//                sbMoveSection.append(sLine);
//                sbMoveSection.append(NEW_LINE);
//        }
//	}
	public ArrayList<PGNGame> readPGNFile(String sInputFile) {
//      private ArrayList<PGNGame> readPGNFile(PlayerRecord[] aprPlayerFRBE, String[] asWithdrawn, OLD_FIDERecord[] afrFIDERating, String[] asCity, String[] asEvent, String[] asCountry, String sInputFile, String sOutputFile, StringBuilder sbErrorLog, boolean bAddFRBE, boolean bAddFIDE, boolean bAddTitle, String sUserName) {
		 
//        string sLine = "[Name \"Lucky Luke\"]";
//        sLine = "[\"Lucky Luke\"]";
//        sLine = "[A\"Lucky Luke\"]";
//        sLine = "[Lucky Luke\"]";
//        sLine = "[Lucky Luke]";
        //sLine = "";
        //sLine = " ";
        //sLine = "[]";
        //sLine = "[ ]";
        //sLine = "[\"\"]";
        //sLine = "[Name\"Lucky Luke\"]";
        //sLine = "[Name\"Lucky Luke\"";
//        sLine = "[  Name\"Lucky Luke\"   ]";
        //sLine = "     [  Name\"Lucky Luke\"   ]";
        //sLine = "     [  LastName\"Lucky\"   ][FirstName \"Luke\"]";
        //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName \"Luke\"    ]";
        //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName        \"Luke\"    ]";
        //sLine = "     [  LastName\"Lucky\"   ][    FirstName\"Luke\"    ]";
        //sLine = "     Le chien aboie et la caravanne passe.";
        //sLine = "Name\"Lucky Luke\"";

		ArrayList<PGNGame> lstcgaReturnValue = new ArrayList<PGNGame>();
		
        String sLine;
        int nLineNumber = 0;
		char[] acLine = null;
        int nLineLength;
        int nStartOfTagName;
        int nEndOfTagName;
        int nStartOfTagValue;
        int nEndOfTagValue;
        String sTagName;
        String sTagValue;
        HashSet<String> hseTagName = new HashSet<String>();   // Duplicate values are prohibited.
        StringBuilder sbMoveSection = new StringBuilder();
        int nPos;
//            int nGameNumber=0;
//            PGNGame cgCurrentGame=new PGNGame();
//            ArrayList<PGNGame> arrPGNGame = new ArrayList<PGNGame>();
        PGNGame cgaCurrentGame = new PGNGame();
        char cFirstLetter;
        
        try {
    // OPEN FILE. 
            BufferedReader brInputFile=new BufferedReader(new FileReader(sInputFile));
     
    // READ-WRITE FILE.             
            while ((sLine=brInputFile.readLine()) != null) {
                nLineNumber++;
                acLine = sLine.toCharArray();
                nLineLength = acLine.length;
                
	           // TAG PAIR SECTION.
	           nPos = -1;
	           // Search left bracket.
	           while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
	           while (nPos < nLineLength && acLine[nPos] == '[') {

	        	   
                   // START OF A NEW GAME.
                   if (sbMoveSection.length()>0) { // As order is facultative, "Event" tag is not a valid trigger.
                      // SET MOVE SECTION.
                	   cgaCurrentGame.setMovetext(sbMoveSection.toString());

                	   // CHECK CONSTRAINTS.
                      // APPEND OBJECT TO COLLECTION.
                	   lstcgaReturnValue.add(cgaCurrentGame);
                	   cgaCurrentGame = new PGNGame();
                      // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//        	   		   javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
                      sbMoveSection.setLength(0);
                      hseTagName.clear();
                   }

	        	   
	        	   
	        	   
	        	   // Search beginning of tag name.
	              while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
	              nStartOfTagName = nPos;
	              // Search beginning of the tag value.
	              while (++nPos < nLineLength && acLine[nPos] != '"') ;
	              if (nPos < nLineLength) {
	                 nStartOfTagValue = nPos;
	                 // Search (backward) end of the tag name.
	                 nEndOfTagName = nPos;
	                 while (acLine[--nEndOfTagName] == ' ') ;   // 0+ spaces allowed between tag name and value.
	                 // Search end of the tag value.
	                 while (++nPos < nLineLength && acLine[nPos] != '"') ;
	                 nEndOfTagValue = nPos;
	                 if (nPos < nLineLength) {
	                    // Search right bracket.
	                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
	                    if (nPos != nLineLength && acLine[nPos] == ']') {
							// Extract tag name.
							sTagName = new String(acLine, nStartOfTagName, nEndOfTagName - nStartOfTagName + 1);
							// Extract tag value.
							sTagValue = new String(acLine, nStartOfTagValue + 1, nEndOfTagValue - nStartOfTagValue - 1);
	
							// AVOID DUPLICATES (probing hashset).
							if (hseTagName.add(sTagName)) {
	                       
								// ADD TAG PAIR TO OBJECT CHESS GAME.
								cFirstLetter = sTagName.charAt(0);
								if (cFirstLetter<'Q') {
									if (cFirstLetter<'C') {
										if (sTagName.equals("Black")) {
											cgaCurrentGame.setBlack(sTagValue);
										}
										else if (sTagName.equals("BlackElo")) {
											cgaCurrentGame.setBlackElo(sTagValue);
										}
										else if (sTagName.equals("BlackTitle")) {
											cgaCurrentGame.setBlackTitle(sTagValue);
										}
										else if (sTagName.equals("Annotator")) {
											cgaCurrentGame.setAnnotator(sTagValue);
										}
										else if (sTagName.equals("Board")) {
											cgaCurrentGame.setBoard(sTagValue);
										}
										else if (sTagName.equals("BlackNA")) {
											cgaCurrentGame.setBlackNA(sTagValue);
										}
										else if (sTagName.equals("BlackType")) {
											cgaCurrentGame.setBlackType(sTagValue);
										}
										else {
											// UNKNOWN TAG.
										}
									}
									else {
										if (sTagName.equals("Event")) {
											cgaCurrentGame.setEvent(sTagValue);
										}
										else if (sTagName.equals("Date")) {
											cgaCurrentGame.setDate(sTagValue);
										}
										if (cFirstLetter<'L') {
											if (sTagName.equals("ECO")) {
												cgaCurrentGame.setECO(sTagValue);
											}
											else if (sTagName.equals("EventDate")) {
												cgaCurrentGame.setEventDate(sTagValue);
											}
											else if (sTagName.equals("FEN")) {
												cgaCurrentGame.setFEN(sTagValue);
											}
											else if (sTagName.equals("EventSponsor")) {
												cgaCurrentGame.setEventSponsor(sTagValue);
											}
											else {
												// UNKNOWN TAG.
											}
										}
										else {
											if (sTagName.equals("Opening")) {
												cgaCurrentGame.setOpening(sTagValue);
											}
											else if (sTagName.equals("Mode")) {
												cgaCurrentGame.setMode(sTagValue);
											}
											else if (sTagName.equals("NIC")) {
												cgaCurrentGame.setNIC(sTagValue);
											}
											else if (sTagName.equals("PlyCount")) {
//												cgaCurrentGame.setPlyCount(sTagValue);
											}
											else {
												// UNKNOWN TAG.
											}
										}
									}
								}
								else {
									if (cFirstLetter<'S') {	// >Q <S
										if (sTagName.equals("Round")) {
											cgaCurrentGame.setRound(sTagValue);
										}
										else if (sTagName.equals("Result")) {
											cgaCurrentGame.setResult(sTagValue);
										}
										else {
											// UNKNOWN TAG.
										}
									}
									else {	// >Q >=S
										if (sTagName.equals("Site")) {
											cgaCurrentGame.setSite(sTagValue);
										}
										else if (sTagName.equals("White")) {
											cgaCurrentGame.setWhite(sTagValue);
										}
										if (cFirstLetter<'U') {
											if (sTagName.equals("TimeControl")) {
												cgaCurrentGame.setTimeControl(sTagValue);
											}
											else if (sTagName.equals("SetUp")) {
												// Do nothing: SetUp is set in PGNGame.setFEN().
											}
											else if (sTagName.equals("Time")) {
												cgaCurrentGame.setTime(sTagValue);
											}
											else if (sTagName.equals("Section")) {
												cgaCurrentGame.setSection(sTagValue);
											}
											else if (sTagName.equals("Stage")) {
												cgaCurrentGame.setStage(sTagValue);
											}
											else if (sTagName.equals("Termination")) {
												cgaCurrentGame.setTermination(sTagValue);
											}
											else if (sTagName.equals("SubVariation")) {
												cgaCurrentGame.setSubVariation(sTagValue);
											}
											else {
												// UNKNOWN TAG.
											}
										}
										else {	// >Q >=S >=U
											if (sTagName.equals("WhiteElo")) {
												cgaCurrentGame.setWhiteElo(sTagValue);
											}
											else if (sTagName.equals("WhiteTitle")) {
												cgaCurrentGame.setWhiteTitle(sTagValue);
											}
											else if (sTagName.equals("Variation")) {
												cgaCurrentGame.setVariation(sTagValue);
											}
											else if (sTagName.equals("WhiteNA")) {
												cgaCurrentGame.setWhiteNA(sTagValue);
											}
											else if (sTagName.equals("UTCDate")) {
												cgaCurrentGame.setUTCDate(sTagValue);
											}
											else if (sTagName.equals("UTCTime")) {
												cgaCurrentGame.setUTCTime(sTagValue);
											}
											else if (sTagName.equals("WhiteType")) {
												cgaCurrentGame.setWhiteType(sTagValue);
											}
											else {
												// UNKNOWN TAG.
											}
										}
									}
								}       	                       
	                       
								}
								else {
								// add to error log.
								}
	                    }
	                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
	                 }
	              }
	           }
	
	           // MOVETEXT SECTION.
	           if (nPos < nLineLength) {   // Avoids empty line.
	              sbMoveSection.append(sLine);
	              sbMoveSection.append(NEW_LINE);
	           }
            }
	        // CLOSE FILE.
            // SET MOVE SECTION.
      	   cgaCurrentGame.setMovetext(sbMoveSection.toString());
            // CHECK CONSTRAINTS.
      	   
            // APPEND OBJECT TO COLLECTION.
      	   lstcgaReturnValue.add(cgaCurrentGame);
            // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//	   		javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
            hseTagName.clear();
	        // FREE RESOURCES.
	
          //CLOSE FILES.             
            brInputFile.close();                
            brInputFile = null;
        }
        catch (java.io.FileNotFoundException e) {
        	javax.swing.JOptionPane.showMessageDialog(null, "Input file (\"" + sInputFile + "\") does not exist.");
        }
        catch (java.io.IOException e) {
           e.printStackTrace();
        }
        
        return lstcgaReturnValue;
    }

	public ArrayList<PGNGame> readPGNFile1b(String sInputFile) {
//      private ArrayList<PGNGame> readPGNFile(PlayerRecord[] aprPlayerFRBE, String[] asWithdrawn, OLD_FIDERecord[] afrFIDERating, String[] asCity, String[] asEvent, String[] asCountry, String sInputFile, String sOutputFile, StringBuilder sbErrorLog, boolean bAddFRBE, boolean bAddFIDE, boolean bAddTitle, String sUserName) {
		 
//        string sLine = "[Name \"Lucky Luke\"]";
//        sLine = "[\"Lucky Luke\"]";
//        sLine = "[A\"Lucky Luke\"]";
//        sLine = "[Lucky Luke\"]";
//        sLine = "[Lucky Luke]";
        //sLine = "";
        //sLine = " ";
        //sLine = "[]";
        //sLine = "[ ]";
        //sLine = "[\"\"]";
        //sLine = "[Name\"Lucky Luke\"]";
        //sLine = "[Name\"Lucky Luke\"";
//        sLine = "[  Name\"Lucky Luke\"   ]";
        //sLine = "     [  Name\"Lucky Luke\"   ]";
        //sLine = "     [  LastName\"Lucky\"   ][FirstName \"Luke\"]";
        //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName \"Luke\"    ]";
        //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName        \"Luke\"    ]";
        //sLine = "     [  LastName\"Lucky\"   ][    FirstName\"Luke\"    ]";
        //sLine = "     Le chien aboie et la caravanne passe.";
        //sLine = "Name\"Lucky Luke\"";

		ArrayList<PGNGame> lstcgaReturnValue = new ArrayList<PGNGame>();
		
        String sLine;
        int nLineNumber = 0;
		char[] acLine = null;
        int nLineLength;
        int nStartOfTagName;
        int nEndOfTagName;
        int nStartOfTagValue;
        int nEndOfTagValue;
        String sTagName;
        String sTagValue;
        int nLengthOfTagName;
                HashSet<String> hseTagName = new HashSet<String>();   // Duplicate values are prohibited.
        StringBuilder sbMoveSection = new StringBuilder();
        int nPos;
//            int nGameNumber=0;
//            PGNGame cgCurrentGame=new PGNGame();
//            ArrayList<PGNGame> arrPGNGame = new ArrayList<PGNGame>();
        PGNGame cgaCurrentGame = new PGNGame();
        char cFirstLetter;
        
        try {
    // OPEN FILE. 
            BufferedReader brInputFile=new BufferedReader(new FileReader(sInputFile));
     
    // READ-WRITE FILE.             
            while ((sLine=brInputFile.readLine()) != null) {
                nLineNumber++;
                acLine = sLine.toCharArray();
                nLineLength = acLine.length;
                
	           // TAG PAIR SECTION.
	           nPos = -1;
	           // Search left bracket.
	           while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
	           while (nPos < nLineLength && acLine[nPos] == '[') {

	        	   
                   // START OF A NEW GAME.
                   if (sbMoveSection.length()>0) { // As order is facultative, "Event" tag is not a valid trigger.
                      // SET MOVE SECTION.
                	   cgaCurrentGame.setMovetext(sbMoveSection.toString());

                	   // CHECK CONSTRAINTS.
                      // APPEND OBJECT TO COLLECTION.
                	   lstcgaReturnValue.add(cgaCurrentGame);
                	   cgaCurrentGame = new PGNGame();
                      // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//        	   		   javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
                      sbMoveSection.setLength(0);
                      hseTagName.clear();
                   }

	        	   
	        	   
	        	   
	        	   // Search beginning of tag name.
	              while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
	              nStartOfTagName = nPos;
	              // Search beginning of the tag value.
	              while (++nPos < nLineLength && acLine[nPos] != '"') ;
	              if (nPos < nLineLength) {
	                 nStartOfTagValue = nPos;
	                 // Search (backward) end of the tag name.
	                 nEndOfTagName = nPos;
	                 while (acLine[--nEndOfTagName] == ' ') ;   // 0+ spaces allowed between tag name and value.
	                 // Search end of the tag value.
	                 while (++nPos < nLineLength && acLine[nPos] != '"') ;
	                 nEndOfTagValue = nPos;
	                 if (nPos < nLineLength) {
	                    // Search right bracket.
	                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
	                    if (nPos != nLineLength && acLine[nPos] == ']') {
							// Extract tag name.
							sTagName = new String(acLine, nStartOfTagName, nEndOfTagName - nStartOfTagName + 1);
							// Extract tag value.
							sTagValue = new String(acLine, nStartOfTagValue + 1, nEndOfTagValue - nStartOfTagValue - 1);
	
							// AVOID DUPLICATES (probing hashset).
							if (hseTagName.add(sTagName)) {
	                       
								// ADD TAG PAIR TO OBJECT CHESS GAME.
								cFirstLetter = sTagName.charAt(0);
								nLengthOfTagName=sTagName.length();
								if (cFirstLetter<'Q') {
									if (cFirstLetter<'C') {
										if (nLengthOfTagName==5 && sTagName.equals("Black")) {
											cgaCurrentGame.setBlack(sTagValue);
										}
										else if (nLengthOfTagName==8 && sTagName.equals("BlackElo")) {
											cgaCurrentGame.setBlackElo(sTagValue);
										}
										else if (nLengthOfTagName==10 && sTagName.equals("BlackTitle")) {
												cgaCurrentGame.setBlackTitle(sTagValue);
										}
										else if (nLengthOfTagName==9 && sTagName.equals("Annotator")) {
											cgaCurrentGame.setAnnotator(sTagValue);
										}
										else if (nLengthOfTagName==5 && sTagName.equals("Board")) {
											cgaCurrentGame.setBoard(sTagValue);
										}
										else if (nLengthOfTagName==7 && sTagName.equals("BlackNA")) {
											cgaCurrentGame.setBlackNA(sTagValue);
										}
										else if (nLengthOfTagName==9 && sTagName.equals("BlackType")) {
											cgaCurrentGame.setBlackType(sTagValue);
										}
										else {
											// UNKNOWN TAG.
										}
									}
									else {
										if (nLengthOfTagName==5 && sTagName.equals("Event")) {
											cgaCurrentGame.setEvent(sTagValue);
										}
										else if (nLengthOfTagName==4 && sTagName.equals("Date")) {
											cgaCurrentGame.setDate(sTagValue);
										}
										if (cFirstLetter<'L') {
											if (nLengthOfTagName==3 && sTagName.equals("ECO")) {
												cgaCurrentGame.setECO(sTagValue);
											}
											else if (nLengthOfTagName==9 && sTagName.equals("EventDate")) {
												cgaCurrentGame.setEventDate(sTagValue);
											}
											else if (nLengthOfTagName==3 && sTagName.equals("FEN")) {
												cgaCurrentGame.setFEN(sTagValue);
											}
											else if (nLengthOfTagName==12 && sTagName.equals("EventSponsor")) {
												cgaCurrentGame.setEventSponsor(sTagValue);
											}
											else {
												// UNKNOWN TAG.
											}
										}
										else {
											if (nLengthOfTagName==7 && sTagName.equals("Opening")) {
												cgaCurrentGame.setOpening(sTagValue);
											}
											else if (nLengthOfTagName==4 && sTagName.equals("Mode")) {
												cgaCurrentGame.setMode(sTagValue);
											}
											else if (nLengthOfTagName==3 && sTagName.equals("NIC")) {
												cgaCurrentGame.setNIC(sTagValue);
											}
											else if (nLengthOfTagName==8 && sTagName.equals("PlyCount")) {
//												cgaCurrentGame.setPlyCount(sTagValue);
											}
											else {
												// UNKNOWN TAG.
											}
										}
									}
								}
								else {
									if (cFirstLetter<'S') {	// >Q <S
										if (nLengthOfTagName==5 && sTagName.equals("Round")) {
											cgaCurrentGame.setRound(sTagValue);
										}
										else if (nLengthOfTagName==6 && sTagName.equals("Result")) {
											cgaCurrentGame.setResult(sTagValue);
										}
										else {
											// UNKNOWN TAG.
										}
									}
									else {	// >Q >=S
										if (nLengthOfTagName==4 && sTagName.equals("Site")) {
											cgaCurrentGame.setSite(sTagValue);
										}
										else if (nLengthOfTagName==5 && sTagName.equals("White")) {
											cgaCurrentGame.setWhite(sTagValue);
										}
										if (cFirstLetter<'U') {
											if (nLengthOfTagName==10 && sTagName.equals("TimeControl")) {
												cgaCurrentGame.setTimeControl(sTagValue);
											}
											else if (nLengthOfTagName==5 && sTagName.equals("SetUp")) {
												// Do nothing: SetUp is set in PGNGame.setFEN().
											}
											else if (nLengthOfTagName==4 && sTagName.equals("Time")) {
												cgaCurrentGame.setTime(sTagValue);
											}
											else if (nLengthOfTagName==6 && sTagName.equals("Section")) {
												cgaCurrentGame.setSection(sTagValue);
											}
											else if (nLengthOfTagName==5 && sTagName.equals("Stage")) {
												cgaCurrentGame.setStage(sTagValue);
											}
											else if (nLengthOfTagName==11 && sTagName.equals("Termination")) {
												cgaCurrentGame.setTermination(sTagValue);
											}
											else if (nLengthOfTagName==12 && sTagName.equals("SubVariation")) {
												cgaCurrentGame.setSubVariation(sTagValue);
											}
											else {
												// UNKNOWN TAG.
											}
										}
										else {	// >Q >=S >=U
											if (nLengthOfTagName==8 && sTagName.equals("WhiteElo")) {
												cgaCurrentGame.setWhiteElo(sTagValue);
											}
											else if (nLengthOfTagName==10 && sTagName.equals("WhiteTitle")) {
												cgaCurrentGame.setWhiteTitle(sTagValue);
											}
											else if (nLengthOfTagName==9 && sTagName.equals("Variation")) {
												cgaCurrentGame.setVariation(sTagValue);
											}
											else if (nLengthOfTagName==7 && sTagName.equals("WhiteNA")) {
												cgaCurrentGame.setWhiteNA(sTagValue);
											}
											else if (nLengthOfTagName==7 && sTagName.equals("UTCDate")) {
												cgaCurrentGame.setUTCDate(sTagValue);
											}
											else if (nLengthOfTagName==7 && sTagName.equals("UTCTime")) {
												cgaCurrentGame.setUTCTime(sTagValue);
											}
											else if (nLengthOfTagName==9 && sTagName.equals("WhiteType")) {
												cgaCurrentGame.setWhiteType(sTagValue);
											}
											else {
												// UNKNOWN TAG.
											}
										}
									}
								}       	                       
	                       
								}
								else {
								// add to error log.
								}
	                    }
	                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
	                 }
	              }
	           }
	
	           // MOVETEXT SECTION.
	           if (nPos < nLineLength) {   // Avoids empty line.
	              sbMoveSection.append(sLine);
	              sbMoveSection.append(NEW_LINE);
	           }
            }
	        // CLOSE FILE.
            // SET MOVE SECTION.
      	   cgaCurrentGame.setMovetext(sbMoveSection.toString());
            // CHECK CONSTRAINTS.
      	   
            // APPEND OBJECT TO COLLECTION.
      	   lstcgaReturnValue.add(cgaCurrentGame);
            // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//	   		javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
            hseTagName.clear();
	        // FREE RESOURCES.
	
          //CLOSE FILES.             
            brInputFile.close();                
            brInputFile = null;
        }
        catch (java.io.FileNotFoundException e) {
        	javax.swing.JOptionPane.showMessageDialog(null, "Input file (\"" + sInputFile + "\") does not exist.");
        }
        catch (java.io.IOException e) {
           e.printStackTrace();
        }
        
        return lstcgaReturnValue;
    }
	
	public ArrayList<PGNGame> readPGNFile1c(String sInputFile) {
//      private ArrayList<PGNGame> readPGNFile(PlayerRecord[] aprPlayerFRBE, String[] asWithdrawn, OLD_FIDERecord[] afrFIDERating, String[] asCity, String[] asEvent, String[] asCountry, String sInputFile, String sOutputFile, StringBuilder sbErrorLog, boolean bAddFRBE, boolean bAddFIDE, boolean bAddTitle, String sUserName) {
		 
//        string sLine = "[Name \"Lucky Luke\"]";
//        sLine = "[\"Lucky Luke\"]";
//        sLine = "[A\"Lucky Luke\"]";
//        sLine = "[Lucky Luke\"]";
//        sLine = "[Lucky Luke]";
        //sLine = "";
        //sLine = " ";
        //sLine = "[]";
        //sLine = "[ ]";
        //sLine = "[\"\"]";
        //sLine = "[Name\"Lucky Luke\"]";
        //sLine = "[Name\"Lucky Luke\"";
//        sLine = "[  Name\"Lucky Luke\"   ]";
        //sLine = "     [  Name\"Lucky Luke\"   ]";
        //sLine = "     [  LastName\"Lucky\"   ][FirstName \"Luke\"]";
        //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName \"Luke\"    ]";
        //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName        \"Luke\"    ]";
        //sLine = "     [  LastName\"Lucky\"   ][    FirstName\"Luke\"    ]";
        //sLine = "     Le chien aboie et la caravanne passe.";
        //sLine = "Name\"Lucky Luke\"";

		ArrayList<PGNGame> lstcgaReturnValue = new ArrayList<PGNGame>();
		
        String sLine;
        int nLineNumber = 0;
		char[] acLine = null;
        int nLineLength;
        int nStartOfTagName;
        int nEndOfTagName;
        int nStartOfTagValue;
        int nEndOfTagValue;
        String sTagName;
        String sTagValue;
        int nLengthOfTagName;
                HashSet<String> hseTagName = new HashSet<String>();   // Duplicate values are prohibited.
        StringBuilder sbMoveSection = new StringBuilder();
        int nPos;
//            int nGameNumber=0;
//            PGNGame cgCurrentGame=new PGNGame();
//            ArrayList<PGNGame> arrPGNGame = new ArrayList<PGNGame>();
        PGNGame cgaCurrentGame = new PGNGame();
        char cFirstLetter;
        
        try {
    // OPEN FILE. 
            BufferedReader brInputFile = new BufferedReader(new FileReader(sInputFile));
     
    // READ-WRITE FILE.             
            while ((sLine=brInputFile.readLine()) != null) {
                nLineNumber++;
                acLine = sLine.toCharArray();
                nLineLength = acLine.length;
                
	           // TAG PAIR SECTION.
	           nPos = -1;
	           // Search left bracket.
	           while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
	           while (nPos < nLineLength && acLine[nPos] == '[') {

	        	   
                   // START OF A NEW GAME.
                   if (sbMoveSection.length()>0) { // As order is facultative, "Event" tag is not a valid trigger.
                      // SET MOVE SECTION.
                	   cgaCurrentGame.setMovetext(sbMoveSection.toString());

                	   // CHECK CONSTRAINTS.
                      // APPEND OBJECT TO COLLECTION.
                	   lstcgaReturnValue.add(cgaCurrentGame);
                	   cgaCurrentGame = new PGNGame();
                      // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//        	   		   javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
                      sbMoveSection.setLength(0);
                      hseTagName.clear();
                   }

	        	   
	        	   
	        	   
	        	   // Search beginning of tag name.
	              while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
	              nStartOfTagName = nPos;
	              // Search beginning of the tag value.
	              while (++nPos < nLineLength && acLine[nPos] != '"') ;
	              if (nPos < nLineLength) {
	                 nStartOfTagValue = nPos;
	                 // Search (backward) end of the tag name.
	                 nEndOfTagName = nPos;
	                 while (acLine[--nEndOfTagName] == ' ') ;   // 0+ spaces allowed between tag name and value.
	                 // Search end of the tag value.
	                 while (++nPos < nLineLength && acLine[nPos] != '"') ;
	                 nEndOfTagValue = nPos;
	                 if (nPos < nLineLength) {
	                    // Search right bracket.
	                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
	                    if (nPos != nLineLength && acLine[nPos] == ']') {
							// Extract tag name.
							sTagName = new String(acLine, nStartOfTagName, nEndOfTagName - nStartOfTagName + 1);
							// Extract tag value.
							sTagValue = new String(acLine, nStartOfTagValue + 1, nEndOfTagValue - nStartOfTagValue - 1);
	
							// AVOID DUPLICATES (probing hashset).
							if (hseTagName.add(sTagName)) {
	                       
								// ADD TAG PAIR TO CHESS GAME OBJECT.
								cFirstLetter = sTagName.charAt(0);
								nLengthOfTagName=sTagName.length();
								if (cFirstLetter<'Q') {
									if (cFirstLetter<'C') {
										if (nLengthOfTagName==5) {
											if (sTagName.equals("Black")) {
												cgaCurrentGame.setBlack(sTagValue);
											}
											else if (sTagName.equals("Board")) {
												cgaCurrentGame.setBoard(sTagValue);
											}
											else {
												// Custom tag pair.
												cgaCurrentGame.addCustomTagPair(sTagName, sTagValue);
											}
										}
										else if (nLengthOfTagName==8 && sTagName.equals("BlackElo")) {
											cgaCurrentGame.setBlackElo(sTagValue);
										}
										else if (nLengthOfTagName==10 && sTagName.equals("BlackTitle")) {
												cgaCurrentGame.setBlackTitle(sTagValue);
										}
										else if (nLengthOfTagName==9) {
											if (sTagName.equals("Annotator")) {
												cgaCurrentGame.setAnnotator(sTagValue);
											}
											else if (sTagName.equals("BlackType")) {
												cgaCurrentGame.setBlackType(sTagValue);
											}
											else {
												// Custom tag pair.
												cgaCurrentGame.addCustomTagPair(sTagName, sTagValue);
											}
										}
										else if (nLengthOfTagName==7 && sTagName.equals("BlackNA")) {
											cgaCurrentGame.setBlackNA(sTagValue);
										}
										else {
											// Custom tag pair.
											cgaCurrentGame.addCustomTagPair(sTagName, sTagValue);
										}
									}
									else {
										if (nLengthOfTagName==5 && sTagName.equals("Event")) {
											cgaCurrentGame.setEvent(sTagValue);
										}
										else if (nLengthOfTagName==4 && sTagName.equals("Date")) {
											cgaCurrentGame.setDate(sTagValue);
										}
										else if (cFirstLetter<'L') {
											if (nLengthOfTagName==3) {
												if (sTagName.equals("ECO")) {
													cgaCurrentGame.setECO(sTagValue);
												}
												else if (sTagName.equals("FEN")) {
													cgaCurrentGame.setFEN(sTagValue);
												}
												else {
													// Custom tag pair.
													cgaCurrentGame.addCustomTagPair(sTagName, sTagValue);
												}
											}
											else if (nLengthOfTagName==9 && sTagName.equals("EventDate")) {
												cgaCurrentGame.setEventDate(sTagValue);
											}
											else if (nLengthOfTagName==12 && sTagName.equals("EventSponsor")) {
												cgaCurrentGame.setEventSponsor(sTagValue);
											}
											else {
												// Custom tag pair.
												cgaCurrentGame.addCustomTagPair(sTagName, sTagValue);
											}
										}
										else {
											if (nLengthOfTagName==7 && sTagName.equals("Opening")) {
												cgaCurrentGame.setOpening(sTagValue);
											}
											else if (nLengthOfTagName==4 && sTagName.equals("Mode")) {
												cgaCurrentGame.setMode(sTagValue);
											}
											else if (nLengthOfTagName==3 && sTagName.equals("NIC")) {
												cgaCurrentGame.setNIC(sTagValue);
											}
											else if (nLengthOfTagName==8 && sTagName.equals("PlyCount")) {
//												cgaCurrentGame.setPlyCount(sTagValue);
											}
											else {
												// Custom tag pair.
												cgaCurrentGame.addCustomTagPair(sTagName, sTagValue);
											}
										}
									}
								}
								else {
									if (cFirstLetter<'S') {	// >Q <S
										if (nLengthOfTagName==5 && sTagName.equals("Round")) {
											cgaCurrentGame.setRound(sTagValue);
										}
										else if (nLengthOfTagName==6 && sTagName.equals("Result")) {
											cgaCurrentGame.setResult(sTagValue);
										}
										else {
											// Custom tag pair.
											cgaCurrentGame.addCustomTagPair(sTagName, sTagValue);
										}
									}
									else {	// >Q >=S
										if (nLengthOfTagName==4 && sTagName.equals("Site")) {
											cgaCurrentGame.setSite(sTagValue);
										}
										else if (nLengthOfTagName==5 && sTagName.equals("White")) {
											cgaCurrentGame.setWhite(sTagValue);
										}
										else if (cFirstLetter<'U') {
											if (nLengthOfTagName==11) {
												if (sTagName.equals("TimeControl")) {
													cgaCurrentGame.setTimeControl(sTagValue);
												}
												else if (sTagName.equals("Termination")) {
													cgaCurrentGame.setTermination(sTagValue);
												}
											}
											else if (nLengthOfTagName==5) {
												if (sTagName.equals("SetUp")) {
													// Do nothing: SetUp is set in PGNGame.setFEN().
												}
												else if (sTagName.equals("Stage")) {
													cgaCurrentGame.setStage(sTagValue);
												}
											}
											else if (nLengthOfTagName==4 && sTagName.equals("Time")) {
												cgaCurrentGame.setTime(sTagValue);
											}
											else if (nLengthOfTagName==7 && sTagName.equals("Section")) {
												cgaCurrentGame.setSection(sTagValue);
											}
											else if (nLengthOfTagName==12 && sTagName.equals("SubVariation")) {
												cgaCurrentGame.setSubVariation(sTagValue);
											}
											else {
												// Custom tag pair.
												cgaCurrentGame.addCustomTagPair(sTagName, sTagValue);
											}
										}
										else {	// >Q >=S >=U
											if (nLengthOfTagName==8 && sTagName.equals("WhiteElo")) {
												cgaCurrentGame.setWhiteElo(sTagValue);
											}
											else if (nLengthOfTagName==10 && sTagName.equals("WhiteTitle")) {
												cgaCurrentGame.setWhiteTitle(sTagValue);
											}
											else if (nLengthOfTagName==9) {
												if (sTagName.equals("Variation")) {
													cgaCurrentGame.setVariation(sTagValue);
													
												}
												else if (sTagName.equals("WhiteType")) {
													cgaCurrentGame.setWhiteType(sTagValue);
												}
												else {
													// Custom tag pair.
													cgaCurrentGame.addCustomTagPair(sTagName, sTagValue);
												}
											}
											else if (nLengthOfTagName==7) {
												if (sTagName.equals("WhiteNA")) {
													cgaCurrentGame.setWhiteNA(sTagValue);
												}
												else if (sTagName.equals("UTCDate")) {
													cgaCurrentGame.setUTCDate(sTagValue);
												}
												else if (sTagName.equals("UTCTime")) {
													cgaCurrentGame.setUTCTime(sTagValue);
												}
												else {
													// Custom tag pair.
													cgaCurrentGame.addCustomTagPair(sTagName, sTagValue);
												}
											}
											else {
												// Custom tag pair.
												cgaCurrentGame.addCustomTagPair(sTagName, sTagValue);
											}
										}
									}
								}       	                       
	                       
								}
								else {
								// add to error log.
								}
	                    }
	                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
	                 }
	              }
	           }
	
	           // MOVETEXT SECTION.
	           if (nPos < nLineLength) {   // Avoids empty line.
	              sbMoveSection.append(sLine);
	              sbMoveSection.append(NEW_LINE);
	           }
            }
	        // CLOSE FILE.
            // SET MOVE SECTION.
      	   cgaCurrentGame.setMovetext(sbMoveSection.toString());
            // CHECK CONSTRAINTS.
      	   
            // APPEND OBJECT TO COLLECTION.
      	   lstcgaReturnValue.add(cgaCurrentGame);
            // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//	   		javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
            hseTagName.clear();
	        // FREE RESOURCES.
	
          //CLOSE FILES.             
            brInputFile.close();                
            brInputFile = null;
        }
        catch (java.io.FileNotFoundException e) {
        	javax.swing.JOptionPane.showMessageDialog(null, "Input file (\"" + sInputFile + "\") does not exist.");
        }
        catch (java.io.IOException e) {
           e.printStackTrace();
        }
        
        return lstcgaReturnValue;
    }

	
	
public ArrayList<PGNGame> readPGNFile2(String sInputFile) {
//  private ArrayList<PGNGame> readPGNFile(PlayerRecord[] aprPlayerFRBE, String[] asWithdrawn, OLD_FIDERecord[] afrFIDERating, String[] asCity, String[] asEvent, String[] asCountry, String sInputFile, String sOutputFile, StringBuilder sbErrorLog, boolean bAddFRBE, boolean bAddFIDE, boolean bAddTitle, String sUserName) {
	 
//    string sLine = "[Name \"Lucky Luke\"]";
//    sLine = "[\"Lucky Luke\"]";
//    sLine = "[A\"Lucky Luke\"]";
//    sLine = "[Lucky Luke\"]";
//    sLine = "[Lucky Luke]";
    //sLine = "";
    //sLine = " ";
    //sLine = "[]";
    //sLine = "[ ]";
    //sLine = "[\"\"]";
    //sLine = "[Name\"Lucky Luke\"]";
    //sLine = "[Name\"Lucky Luke\"";
//    sLine = "[  Name\"Lucky Luke\"   ]";
    //sLine = "     [  Name\"Lucky Luke\"   ]";
    //sLine = "     [  LastName\"Lucky\"   ][FirstName \"Luke\"]";
    //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName \"Luke\"    ]";
    //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName        \"Luke\"    ]";
    //sLine = "     [  LastName\"Lucky\"   ][    FirstName\"Luke\"    ]";
    //sLine = "     Le chien aboie et la caravanne passe.";
    //sLine = "Name\"Lucky Luke\"";

	ArrayList<PGNGame> lstcgaReturnValue = new ArrayList<PGNGame>();
	HashSet<String> hseStandardTag = new HashSet<String>();
    hseStandardTag.add("Event");
    hseStandardTag.add("Site");
    hseStandardTag.add("Date");
    hseStandardTag.add("Round");
    hseStandardTag.add("White");
    hseStandardTag.add("Black");
    hseStandardTag.add("Result");
    hseStandardTag.add("Annotator");
    hseStandardTag.add("BlackElo");
    hseStandardTag.add("BlackNA"); 
    hseStandardTag.add("BlackTitle");
    hseStandardTag.add("BlackType"); 
    hseStandardTag.add("Board");
    hseStandardTag.add("ECO");
    hseStandardTag.add("EventDate");; 
    hseStandardTag.add("EventSponsor"); 
    hseStandardTag.add("FEN");
    hseStandardTag.add("Mode"); 
    hseStandardTag.add("MoveSection");
    hseStandardTag.add("NIC");
    hseStandardTag.add("Opening");
    hseStandardTag.add("PlyCount"); 
    hseStandardTag.add("Section"); 
    hseStandardTag.add("SetUp");
    hseStandardTag.add("Stage"); 
    hseStandardTag.add("SubVariation");
    hseStandardTag.add("Termination"); 
    hseStandardTag.add("Time"); 
    hseStandardTag.add("TimeControl");
    hseStandardTag.add("UTCDate"); 
    hseStandardTag.add("UTCTime"); 
    hseStandardTag.add("Variation");
    hseStandardTag.add("WhiteElo");
    hseStandardTag.add("WhiteNA"); 
    hseStandardTag.add("WhiteTitle");
    hseStandardTag.add("WhiteType"); 

	
    String sLine;
    int nLineNumber = 0;
	char[] acLine = null;
    int nLineLength;
    int nStartOfTagName;
    int nEndOfTagName;
    int nStartOfTagValue;
    int nEndOfTagValue;
    String sTagName;
    String sTagValue;
            HashSet<String> hseTagName = new HashSet<String>();   // Duplicate values are prohibited.
    StringBuilder sbMoveSection = new StringBuilder();
    int nPos;
//        int nGameNumber=0;
//        PGNGame cgCurrentGame=new PGNGame();
//        ArrayList<PGNGame> arrPGNGame = new ArrayList<PGNGame>();
    PGNGame cgaCurrentGame = new PGNGame();
    char cFirstLetter;
    Class[] aclsParameterType=new Class[1];
    aclsParameterType[0]=String.class;
    Method metMethod;
    
    try {
// OPEN FILE. 
        BufferedReader brInputFile=new BufferedReader(new FileReader(sInputFile));
 
// READ-WRITE FILE.             
        while ((sLine=brInputFile.readLine()) != null) {
            nLineNumber++;
            acLine = sLine.toCharArray();
            nLineLength = acLine.length;
            
           // TAG PAIR SECTION.
           nPos = -1;
           // Search left bracket.
           while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
           while (nPos < nLineLength && acLine[nPos] == '[') {

        	   
               // START OF A NEW GAME.
               if (sbMoveSection.length()>0) { // As order is facultative, "Event" tag is not a valid trigger.
                  // SET MOVE SECTION.
            	   cgaCurrentGame.setMovetext(sbMoveSection.toString());

            	   // CHECK CONSTRAINTS.
                  // APPEND OBJECT TO COLLECTION.
            	   lstcgaReturnValue.add(cgaCurrentGame);
            	   cgaCurrentGame = new PGNGame();
                  // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//    	   		   javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
                  sbMoveSection.setLength(0);
                  hseTagName.clear();
               }

        	   
        	   
        	   
        	   // Search beginning of tag name.
              while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
              nStartOfTagName = nPos;
              // Search beginning of the tag value.
              while (++nPos < nLineLength && acLine[nPos] != '"') ;
              if (nPos < nLineLength) {
                 nStartOfTagValue = nPos;
                 // Search (backward) end of the tag name.
                 nEndOfTagName = nPos;
                 while (acLine[--nEndOfTagName] == ' ') ;   // 0+ spaces allowed between tag name and value.
                 // Search end of the tag value.
                 while (++nPos < nLineLength && acLine[nPos] != '"') ;
                 nEndOfTagValue = nPos;
                 if (nPos < nLineLength) {
                    // Search right bracket.
                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
                    if (nPos != nLineLength && acLine[nPos] == ']') {
						// Extract tag name.
						sTagName = new String(acLine, nStartOfTagName, nEndOfTagName - nStartOfTagName + 1);
						// Extract tag value.
						sTagValue = new String(acLine, nStartOfTagValue + 1, nEndOfTagValue - nStartOfTagValue - 1);

						// AVOID DUPLICATES (probing hashset).
						if (hseTagName.add(sTagName)) {
                       
							// ADD TAG PAIR TO OBJECT CHESS GAME.
							if (hseStandardTag.contains(sTagName)) {

								// INVOKE METHOD.
								try {
					                metMethod = cgaCurrentGame.getClass().getMethod("set" + sTagName, String.class);
					                metMethod.invoke(cgaCurrentGame, sTagValue);	// boolean bOutput = (boolean)metMethod...
							    } catch (Exception e) {
						              e.printStackTrace();
							    } 								
								
								
							}
						}
						else {
						// add to error log.
						}
                    }
                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
                 }
              }
           }

           // MOVETEXT SECTION.
           if (nPos < nLineLength) {   // Avoids empty line.
              sbMoveSection.append(sLine);
              sbMoveSection.append(NEW_LINE);
           }
        }
        // CLOSE FILE.
        // SET MOVE SECTION.
  	   cgaCurrentGame.setMovetext(sbMoveSection.toString());
        // CHECK CONSTRAINTS.
  	   
        // APPEND OBJECT TO COLLECTION.
  	   lstcgaReturnValue.add(cgaCurrentGame);
        // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//   		javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
        hseTagName.clear();
        // FREE RESOURCES.

      //CLOSE FILES.             
        brInputFile.close();                
        brInputFile = null;
    }
    catch (java.io.FileNotFoundException e) {
    	javax.swing.JOptionPane.showMessageDialog(null, "Input file (\"" + sInputFile + "\") does not exist.");
    }
    catch (java.io.IOException e) {
       e.printStackTrace();
    }
    
    return lstcgaReturnValue;
}

public ArrayList<PGNGame> readPGNFile3(String sInputFile) {
//  private ArrayList<PGNGame> readPGNFile(PlayerRecord[] aprPlayerFRBE, String[] asWithdrawn, OLD_FIDERecord[] afrFIDERating, String[] asCity, String[] asEvent, String[] asCountry, String sInputFile, String sOutputFile, StringBuilder sbErrorLog, boolean bAddFRBE, boolean bAddFIDE, boolean bAddTitle, String sUserName) {
	 
//    string sLine = "[Name \"Lucky Luke\"]";
//    sLine = "[\"Lucky Luke\"]";
//    sLine = "[A\"Lucky Luke\"]";
//    sLine = "[Lucky Luke\"]";
//    sLine = "[Lucky Luke]";
    //sLine = "";
    //sLine = " ";
    //sLine = "[]";
    //sLine = "[ ]";
    //sLine = "[\"\"]";
    //sLine = "[Name\"Lucky Luke\"]";
    //sLine = "[Name\"Lucky Luke\"";
//    sLine = "[  Name\"Lucky Luke\"   ]";
    //sLine = "     [  Name\"Lucky Luke\"   ]";
    //sLine = "     [  LastName\"Lucky\"   ][FirstName \"Luke\"]";
    //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName \"Luke\"    ]";
    //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName        \"Luke\"    ]";
    //sLine = "     [  LastName\"Lucky\"   ][    FirstName\"Luke\"    ]";
    //sLine = "     Le chien aboie et la caravanne passe.";
    //sLine = "Name\"Lucky Luke\"";

    PGNGame cgaCurrentGame = new PGNGame();
	ArrayList<PGNGame> lstcgaReturnValue = new ArrayList<PGNGame>();
	String[] asStandardTag = {"Annotator", "Black", "BlackElo", "BlackNA", "BlackTitle", "BlackType", "Board", "Date", "ECO",
			"Event", "EventDate", "EventSponsor", "FEN", "Mode", "MoveSection", "NIC", "Opening", "PlyCount", "Result", "Round",
			"Section", "SetUp", "Site", "Stage", "SubVariation", "Termination", "Time", "TimeControl", "UTCDate", "UTCTime",
			"Variation", "White", "WhiteElo", "WhiteNA", "WhiteTitle", "WhiteType"};  
	Method[] metPGNProperty = null;
	Class<? extends PGNGame> claCurrentGame = cgaCurrentGame.getClass();
	try {
		metPGNProperty= new Method[] {
	    		claCurrentGame.getMethod("setAnnotator", String.class),
	    		claCurrentGame.getMethod("setBlack", String.class),
	    		claCurrentGame.getMethod("setBlackElo", String.class),
	    		claCurrentGame.getMethod("setBlackNA", String.class),
	    		claCurrentGame.getMethod("setBlackTitle", String.class),
	    		claCurrentGame.getMethod("setBlackType", String.class),
	    		claCurrentGame.getMethod("setBoard", String.class),
	    		claCurrentGame.getMethod("setDate", String.class),
	    		claCurrentGame.getMethod("setECO", String.class),
	    		claCurrentGame.getMethod("setEvent", String.class),
	    		claCurrentGame.getMethod("setEventDate", String.class),
	    		claCurrentGame.getMethod("setEventSponsor", String.class),
	    		claCurrentGame.getMethod("setFEN", String.class),
	    		claCurrentGame.getMethod("setMode", String.class),
	    		claCurrentGame.getMethod("setMoveSection", String.class),
	    		claCurrentGame.getMethod("setNIC", String.class),
	    		claCurrentGame.getMethod("setOpening", String.class),
	    		claCurrentGame.getMethod("setPlyCount", String.class),
	    		claCurrentGame.getMethod("setResult", String.class),
	    		claCurrentGame.getMethod("setRound", String.class),
	    		claCurrentGame.getMethod("setSection", String.class),
	    		claCurrentGame.getMethod("setSetUp", String.class),
	    		claCurrentGame.getMethod("setSite", String.class),
	    		claCurrentGame.getMethod("setStage", String.class),
	    		claCurrentGame.getMethod("setSubVariation", String.class),
	    		claCurrentGame.getMethod("setTermination", String.class),
	    		claCurrentGame.getMethod("setTime", String.class),
	    		claCurrentGame.getMethod("setTimeControl", String.class),
	    		claCurrentGame.getMethod("setUTCDate", String.class),
	    		claCurrentGame.getMethod("setUTCTime", String.class),
	    		claCurrentGame.getMethod("setVariation", String.class),
	    		claCurrentGame.getMethod("setWhite", String.class),
	    		claCurrentGame.getMethod("setWhiteElo", String.class),
	    		claCurrentGame.getMethod("setWhiteNA", String.class),
	    		claCurrentGame.getMethod("setWhiteTitle", String.class),
	    		claCurrentGame.getMethod("setWhiteType", String.class)  
	    };
    } catch (Exception e) {
          e.printStackTrace();
    } 								
	
    String sLine;
    int nLineNumber = 0;
	char[] acLine = null;
    int nLineLength;
    int nStartOfTagName;
    int nEndOfTagName;
    int nStartOfTagValue;
    int nEndOfTagValue;
    String sTagName;
    String sTagValue;
    HashSet<String> hseTagName = new HashSet<String>();   // Duplicate values are prohibited.
    StringBuilder sbMoveSection = new StringBuilder();
    int nPos;
//        int nGameNumber=0;
//        PGNGame cgCurrentGame=new PGNGame();
//        ArrayList<PGNGame> arrPGNGame = new ArrayList<PGNGame>();
//    char cFirstLetter;
//    Class[] aclsParameterType=new Class[1];
//    aclsParameterType[0]=String.class;
    int nIndex;
    
    try {
// OPEN FILE. 
        BufferedReader brInputFile=new BufferedReader(new FileReader(sInputFile));
 
// READ-WRITE FILE.             
        while ((sLine=brInputFile.readLine()) != null) {
            nLineNumber++;
            acLine = sLine.toCharArray();
            nLineLength = acLine.length;
            
           // TAG PAIR SECTION.
           nPos = -1;
           // Search left bracket.
           while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
           while (nPos < nLineLength && acLine[nPos] == '[') {

        	   
               // START OF A NEW GAME.
               if (sbMoveSection.length()>0) { // As order is facultative, "Event" tag is not a valid trigger.
                  // SET MOVE SECTION.
            	   cgaCurrentGame.setMovetext(sbMoveSection.toString());

            	   // CHECK CONSTRAINTS.
                  // APPEND OBJECT TO COLLECTION.
            	   lstcgaReturnValue.add(cgaCurrentGame);
            	   cgaCurrentGame = new PGNGame();
                  // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//    	   		   javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
                  sbMoveSection.setLength(0);
                  hseTagName.clear();
               }

        	   
        	   
        	   // ANALYSE LINE TRYING TO RETRIEVE TAG NAME AND TAG VALUE.
        	   // Search beginning of tag name.
              while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
              nStartOfTagName = nPos;
              // Search beginning of the tag value.
              while (++nPos < nLineLength && acLine[nPos] != '"') ;
              if (nPos < nLineLength) {
                 nStartOfTagValue = nPos;
                 // Search (backward) end of the tag name.
                 nEndOfTagName = nPos;
                 while (acLine[--nEndOfTagName] == ' ') ;   // 0+ spaces allowed between tag name and value.
                 // Search end of the tag value.
                 while (++nPos < nLineLength && acLine[nPos] != '"') ;
                 nEndOfTagValue = nPos;
                 if (nPos < nLineLength) {
                    // Search right bracket.
                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
                    if (nPos != nLineLength && acLine[nPos] == ']') {
						// Extract tag name.
						sTagName = new String(acLine, nStartOfTagName, nEndOfTagName - nStartOfTagName + 1);
						// Extract tag value.
						sTagValue = new String(acLine, nStartOfTagValue + 1, nEndOfTagValue - nStartOfTagValue - 1);

						// AVOID DUPLICATES (probing hashset).
						if (hseTagName.add(sTagName)) {
                       
							// ADD TAG PAIR TO OBJECT CHESS GAME.
							if ((nIndex=Arrays.binarySearch(asStandardTag, sTagName))>-1) {

								// INVOKE METHOD.
								try {
					                metPGNProperty[nIndex].invoke(cgaCurrentGame, sTagValue);	// boolean bOutput = (boolean)metMethod...
							    } catch (Exception e) {
						              e.printStackTrace();
							    } 								
								
								
							}
							else {
//								if (sTagName.compareTo("WhiteFRBEKBSB")!=0 && sTagName.compareTo("BlackFRBEKBSB")!=0&& sTagName.compareTo("WhiteTeam")!=0 && sTagName.compareTo("BlackTeam")!=0) {
//									javax.swing.JOptionPane.showMessageDialog(null, sTagName);
//								}
							}
						}
						else {
							// add to error log.
						}
                    }
                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
                 }
              }
           }

           // MOVETEXT SECTION.
           if (nPos < nLineLength) {   // Avoids empty line.
              sbMoveSection.append(sLine);
              sbMoveSection.append(NEW_LINE);
           }
        }
        // CLOSE FILE.
        // SET MOVE SECTION.
  	   cgaCurrentGame.setMovetext(sbMoveSection.toString());
        // CHECK CONSTRAINTS.
  	   
        // APPEND OBJECT TO COLLECTION.
  	   lstcgaReturnValue.add(cgaCurrentGame);
        // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//   		javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
        hseTagName.clear();
        // FREE RESOURCES.

      //CLOSE FILES.             
        brInputFile.close();                
        brInputFile = null;
    }
    catch (java.io.FileNotFoundException e) {
    	javax.swing.JOptionPane.showMessageDialog(null, "Input file (\"" + sInputFile + "\") does not exist.");
    }
    catch (java.io.IOException e) {
       e.printStackTrace();
    }
    
    return lstcgaReturnValue;
}


public ArrayList<PGNGame> readPGNFile4(String sInputFile) {
//  private ArrayList<PGNGame> readPGNFile(PlayerRecord[] aprPlayerFRBE, String[] asWithdrawn, OLD_FIDERecord[] afrFIDERating, String[] asCity, String[] asEvent, String[] asCountry, String sInputFile, String sOutputFile, StringBuilder sbErrorLog, boolean bAddFRBE, boolean bAddFIDE, boolean bAddTitle, String sUserName) {
	 
//    string sLine = "[Name \"Lucky Luke\"]";
//    sLine = "[\"Lucky Luke\"]";
//    sLine = "[A\"Lucky Luke\"]";
//    sLine = "[Lucky Luke\"]";
//    sLine = "[Lucky Luke]";
    //sLine = "";
    //sLine = " ";
    //sLine = "[]";
    //sLine = "[ ]";
    //sLine = "[\"\"]";
    //sLine = "[Name\"Lucky Luke\"]";
    //sLine = "[Name\"Lucky Luke\"";
//    sLine = "[  Name\"Lucky Luke\"   ]";
    //sLine = "     [  Name\"Lucky Luke\"   ]";
    //sLine = "     [  LastName\"Lucky\"   ][FirstName \"Luke\"]";
    //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName \"Luke\"    ]";
    //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName        \"Luke\"    ]";
    //sLine = "     [  LastName\"Lucky\"   ][    FirstName\"Luke\"    ]";
    //sLine = "     Le chien aboie et la caravanne passe.";
    //sLine = "Name\"Lucky Luke\"";

    PGNGame cgaCurrentGame = new PGNGame();
	ArrayList<PGNGame> lstcgaReturnValue = new ArrayList<PGNGame>();
	Hashtable<String, Integer> htaStandardTag = new Hashtable<String, Integer>();
	htaStandardTag.put("Annotator", new Integer(0));
	htaStandardTag.put("Black", new Integer(1));
	htaStandardTag.put("BlackElo", new Integer(2));
	htaStandardTag.put("BlackNA", new Integer(3));
	htaStandardTag.put("BlackTitle", new Integer(4));
	htaStandardTag.put("BlackType", new Integer(5));
	htaStandardTag.put("Board", new Integer(6));
	htaStandardTag.put("Date", new Integer(7));
	htaStandardTag.put("ECO", new Integer(8));
	htaStandardTag.put("Event", new Integer(9));
	htaStandardTag.put("EventDate", new Integer(10));
	htaStandardTag.put("EventSponsor", new Integer(11));
	htaStandardTag.put("FEN", new Integer(12));
	htaStandardTag.put("Mode", new Integer(13));
	htaStandardTag.put("MoveSection", new Integer(14));
	htaStandardTag.put("NIC", new Integer(15));
	htaStandardTag.put("Opening", new Integer(16));
	htaStandardTag.put("PlyCount", new Integer(17));
	htaStandardTag.put("Result", new Integer(18));
	htaStandardTag.put("Round", new Integer(19));
	htaStandardTag.put("Section", new Integer(20));
	htaStandardTag.put("SetUp", new Integer(21));
	htaStandardTag.put("Site", new Integer(22));
	htaStandardTag.put("Stage", new Integer(23));
	htaStandardTag.put("SubVariation", new Integer(24));
	htaStandardTag.put("Termination", new Integer(25));
	htaStandardTag.put("Time", new Integer(26));
	htaStandardTag.put("TimeControl", new Integer(27));
	htaStandardTag.put("UTCDate", new Integer(28));
	htaStandardTag.put("UTCTime", new Integer(29));
	htaStandardTag.put("Variation", new Integer(30));
	htaStandardTag.put("White", new Integer(31));
	htaStandardTag.put("WhiteElo", new Integer(32));
	htaStandardTag.put("WhiteNA", new Integer(33));
	htaStandardTag.put("WhiteTitle", new Integer(34));
	htaStandardTag.put("WhiteType", new Integer(35));
	
	Method[] metPGNProperty = null;
	try {
			metPGNProperty= new Method[] {
    		cgaCurrentGame.getClass().getMethod("setAnnotator", String.class),
    		cgaCurrentGame.getClass().getMethod("setBlack", String.class),
    		cgaCurrentGame.getClass().getMethod("setBlackElo", String.class),
    		cgaCurrentGame.getClass().getMethod("setBlackNA", String.class),
    		cgaCurrentGame.getClass().getMethod("setBlackTitle", String.class),
    		cgaCurrentGame.getClass().getMethod("setBlackType", String.class),
    		cgaCurrentGame.getClass().getMethod("setBoard", String.class),
    		cgaCurrentGame.getClass().getMethod("setDate", String.class),
    		cgaCurrentGame.getClass().getMethod("setECO", String.class),
    		cgaCurrentGame.getClass().getMethod("setEvent", String.class),
    		cgaCurrentGame.getClass().getMethod("setEventDate", String.class),
    		cgaCurrentGame.getClass().getMethod("setEventSponsor", String.class),
    		cgaCurrentGame.getClass().getMethod("setFEN", String.class),
    		cgaCurrentGame.getClass().getMethod("setMode", String.class),
    		cgaCurrentGame.getClass().getMethod("setMoveSection", String.class),
    		cgaCurrentGame.getClass().getMethod("setNIC", String.class),
    		cgaCurrentGame.getClass().getMethod("setOpening", String.class),
    		cgaCurrentGame.getClass().getMethod("setPlyCount", String.class),
    		cgaCurrentGame.getClass().getMethod("setResult", String.class),
    		cgaCurrentGame.getClass().getMethod("setRound", String.class),
    		cgaCurrentGame.getClass().getMethod("setSection", String.class),
    		cgaCurrentGame.getClass().getMethod("setSetUp", String.class),
    		cgaCurrentGame.getClass().getMethod("setSite", String.class),
    		cgaCurrentGame.getClass().getMethod("setStage", String.class),
    		cgaCurrentGame.getClass().getMethod("setSubVariation", String.class),
    		cgaCurrentGame.getClass().getMethod("setTermination", String.class),
    		cgaCurrentGame.getClass().getMethod("setTime", String.class),
    		cgaCurrentGame.getClass().getMethod("setTimeControl", String.class),
    		cgaCurrentGame.getClass().getMethod("setUTCDate", String.class),
    		cgaCurrentGame.getClass().getMethod("setUTCTime", String.class),
    		cgaCurrentGame.getClass().getMethod("setVariation", String.class),
    		cgaCurrentGame.getClass().getMethod("setWhite", String.class),
    		cgaCurrentGame.getClass().getMethod("setWhiteElo", String.class),
    		cgaCurrentGame.getClass().getMethod("setWhiteNA", String.class),
    		cgaCurrentGame.getClass().getMethod("setWhiteTitle", String.class),
    		cgaCurrentGame.getClass().getMethod("setWhiteType", String.class)  
    };
    } catch (Exception e) {
          e.printStackTrace();
    } 								
	
    String sLine;
    int nLineNumber = 0;
	char[] acLine = null;
    int nLineLength;
    int nStartOfTagName;
    int nEndOfTagName;
    int nStartOfTagValue;
    int nEndOfTagValue;
    String sTagName;
    String sTagValue;
    HashSet<String> hseTagName = new HashSet<String>();   // Duplicate values are prohibited.
    StringBuilder sbMoveSection = new StringBuilder();
    int nPos;
//        int nGameNumber=0;
//        PGNGame cgCurrentGame=new PGNGame();
//        ArrayList<PGNGame> arrPGNGame = new ArrayList<PGNGame>();
//    char cFirstLetter;
//    Class[] aclsParameterType=new Class[1];
//    aclsParameterType[0]=String.class;
    Integer intIndex;
//    int nIndex;
    
    try {
// OPEN FILE. 
        BufferedReader brInputFile=new BufferedReader(new FileReader(sInputFile));
 
// READ-WRITE FILE.             
        while ((sLine=brInputFile.readLine()) != null) {
            nLineNumber++;
            acLine = sLine.toCharArray();
            nLineLength = acLine.length;
            
           // TAG PAIR SECTION.
           nPos = -1;
           // Search left bracket.
           while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
           while (nPos < nLineLength && acLine[nPos] == '[') {

        	   
               // START OF A NEW GAME.
               if (sbMoveSection.length()>0) { // As order is facultative, "Event" tag is not a valid trigger.
                  // SET MOVE SECTION.
            	   cgaCurrentGame.setMovetext(sbMoveSection.toString());

            	   // CHECK CONSTRAINTS.
                  // APPEND OBJECT TO COLLECTION.
            	   lstcgaReturnValue.add(cgaCurrentGame);
            	   cgaCurrentGame = new PGNGame();
                  // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//    	   		   javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
                  sbMoveSection.setLength(0);
                  hseTagName.clear();
               }

        	   
        	   
        	   // ANALYSE LINE TRYING TO RETRIEVE TAG NAME AND TAG VALUE.
        	   // Search beginning of tag name.
              while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
              nStartOfTagName = nPos;
              // Search beginning of the tag value.
              while (++nPos < nLineLength && acLine[nPos] != '"') ;
              if (nPos < nLineLength) {
                 nStartOfTagValue = nPos;
                 // Search (backward) end of the tag name.
                 nEndOfTagName = nPos;
                 while (acLine[--nEndOfTagName] == ' ') ;   // 0+ spaces allowed between tag name and value.
                 // Search end of the tag value.
                 while (++nPos < nLineLength && acLine[nPos] != '"') ;
                 nEndOfTagValue = nPos;
                 if (nPos < nLineLength) {
                    // Search right bracket.
                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
                    if (nPos != nLineLength && acLine[nPos] == ']') {
						// Extract tag name.
						sTagName = new String(acLine, nStartOfTagName, nEndOfTagName - nStartOfTagName + 1);
						// Extract tag value.
						sTagValue = new String(acLine, nStartOfTagValue + 1, nEndOfTagValue - nStartOfTagValue - 1);

						// AVOID DUPLICATES (probing hashset).
						if (hseTagName.add(sTagName)) {
                       
							// ADD TAG PAIR TO OBJECT CHESS GAME.
							if ((intIndex=htaStandardTag.get(sTagName))!=null) {

								// INVOKE METHOD.
								try {
					                metPGNProperty[intIndex].invoke(cgaCurrentGame, sTagValue);	// boolean bOutput = (boolean)metMethod...
							    } catch (Exception e) {
						              e.printStackTrace();
							    } 								
								
								
							}
							else {
//								if (sTagName.compareTo("WhiteFRBEKBSB")!=0 && sTagName.compareTo("BlackFRBEKBSB")!=0&& sTagName.compareTo("WhiteTeam")!=0 && sTagName.compareTo("BlackTeam")!=0) {
//									javax.swing.JOptionPane.showMessageDialog(null, sTagName);
//								}
							}
						}
						else {
							// add to error log.
						}
                    }
                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
                 }
              }
           }

           // MOVETEXT SECTION.
           if (nPos < nLineLength) {   // Avoids empty line.
              sbMoveSection.append(sLine);
              sbMoveSection.append(NEW_LINE);
           }
        }
        // CLOSE FILE.
        // SET MOVE SECTION.
  	   cgaCurrentGame.setMovetext(sbMoveSection.toString());
        // CHECK CONSTRAINTS.
  	   
        // APPEND OBJECT TO COLLECTION.
  	   lstcgaReturnValue.add(cgaCurrentGame);
        // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//   		javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
        hseTagName.clear();
        // FREE RESOURCES.

      //CLOSE FILES.             
        brInputFile.close();                
        brInputFile = null;
    }
    catch (java.io.FileNotFoundException e) {
    	javax.swing.JOptionPane.showMessageDialog(null, "Input file (\"" + sInputFile + "\") does not exist.");
    }
    catch (java.io.IOException e) {
       e.printStackTrace();
    }
    
    return lstcgaReturnValue;
}


public ArrayList<PGNGame> readPGNFile4_2(String sInputFile) {
//  private ArrayList<PGNGame> readPGNFile(PlayerRecord[] aprPlayerFRBE, String[] asWithdrawn, OLD_FIDERecord[] afrFIDERating, String[] asCity, String[] asEvent, String[] asCountry, String sInputFile, String sOutputFile, StringBuilder sbErrorLog, boolean bAddFRBE, boolean bAddFIDE, boolean bAddTitle, String sUserName) {
	 
//    string sLine = "[Name \"Lucky Luke\"]";
//    sLine = "[\"Lucky Luke\"]";
//    sLine = "[A\"Lucky Luke\"]";
//    sLine = "[Lucky Luke\"]";
//    sLine = "[Lucky Luke]";
    //sLine = "";
    //sLine = " ";
    //sLine = "[]";
    //sLine = "[ ]";
    //sLine = "[\"\"]";
    //sLine = "[Name\"Lucky Luke\"]";
    //sLine = "[Name\"Lucky Luke\"";
//    sLine = "[  Name\"Lucky Luke\"   ]";
    //sLine = "     [  Name\"Lucky Luke\"   ]";
    //sLine = "     [  LastName\"Lucky\"   ][FirstName \"Luke\"]";
    //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName \"Luke\"    ]";
    //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName        \"Luke\"    ]";
    //sLine = "     [  LastName\"Lucky\"   ][    FirstName\"Luke\"    ]";
    //sLine = "     Le chien aboie et la caravanne passe.";
    //sLine = "Name\"Lucky Luke\"";

    PGNGame cgaCurrentGame = new PGNGame();
	ArrayList<PGNGame> lstcgaReturnValue = new ArrayList<PGNGame>();
	HashMap<String, Integer> htaStandardTag = new HashMap<String, Integer>();
	htaStandardTag.put("Annotator", new Integer(0));
	htaStandardTag.put("Black", new Integer(1));
	htaStandardTag.put("BlackElo", new Integer(2));
	htaStandardTag.put("BlackNA", new Integer(3));
	htaStandardTag.put("BlackTitle", new Integer(4));
	htaStandardTag.put("BlackType", new Integer(5));
	htaStandardTag.put("Board", new Integer(6));
	htaStandardTag.put("Date", new Integer(7));
	htaStandardTag.put("ECO", new Integer(8));
	htaStandardTag.put("Event", new Integer(9));
	htaStandardTag.put("EventDate", new Integer(10));
	htaStandardTag.put("EventSponsor", new Integer(11));
	htaStandardTag.put("FEN", new Integer(12));
	htaStandardTag.put("Mode", new Integer(13));
	htaStandardTag.put("MoveSection", new Integer(14));
	htaStandardTag.put("NIC", new Integer(15));
	htaStandardTag.put("Opening", new Integer(16));
	htaStandardTag.put("PlyCount", new Integer(17));
	htaStandardTag.put("Result", new Integer(18));
	htaStandardTag.put("Round", new Integer(19));
	htaStandardTag.put("Section", new Integer(20));
	htaStandardTag.put("SetUp", new Integer(21));
	htaStandardTag.put("Site", new Integer(22));
	htaStandardTag.put("Stage", new Integer(23));
	htaStandardTag.put("SubVariation", new Integer(24));
	htaStandardTag.put("Termination", new Integer(25));
	htaStandardTag.put("Time", new Integer(26));
	htaStandardTag.put("TimeControl", new Integer(27));
	htaStandardTag.put("UTCDate", new Integer(28));
	htaStandardTag.put("UTCTime", new Integer(29));
	htaStandardTag.put("Variation", new Integer(30));
	htaStandardTag.put("White", new Integer(31));
	htaStandardTag.put("WhiteElo", new Integer(32));
	htaStandardTag.put("WhiteNA", new Integer(33));
	htaStandardTag.put("WhiteTitle", new Integer(34));
	htaStandardTag.put("WhiteType", new Integer(35));
	
//	Class<? extends PGNGame> claCurrentGame = cgaCurrentGame.getClass();
	Method[] metPGNProperty = null;
	try {
			metPGNProperty= new Method[] {
	    		cgaCurrentGame.getClass().getMethod("setAnnotator", String.class),
	    		cgaCurrentGame.getClass().getMethod("setBlack", String.class),
	    		cgaCurrentGame.getClass().getMethod("setBlackElo", String.class),
	    		cgaCurrentGame.getClass().getMethod("setBlackNA", String.class),
	    		cgaCurrentGame.getClass().getMethod("setBlackTitle", String.class),
	    		cgaCurrentGame.getClass().getMethod("setBlackType", String.class),
	    		cgaCurrentGame.getClass().getMethod("setBoard", String.class),
	    		cgaCurrentGame.getClass().getMethod("setDate", String.class),
	    		cgaCurrentGame.getClass().getMethod("setECO", String.class),
	    		cgaCurrentGame.getClass().getMethod("setEvent", String.class),
	    		cgaCurrentGame.getClass().getMethod("setEventDate", String.class),
	    		cgaCurrentGame.getClass().getMethod("setEventSponsor", String.class),
	    		cgaCurrentGame.getClass().getMethod("setFEN", String.class),
	    		cgaCurrentGame.getClass().getMethod("setMode", String.class),
	    		cgaCurrentGame.getClass().getMethod("setMoveSection", String.class),
	    		cgaCurrentGame.getClass().getMethod("setNIC", String.class),
	    		cgaCurrentGame.getClass().getMethod("setOpening", String.class),
	    		cgaCurrentGame.getClass().getMethod("setPlyCount", String.class),
	    		cgaCurrentGame.getClass().getMethod("setResult", String.class),
	    		cgaCurrentGame.getClass().getMethod("setRound", String.class),
	    		cgaCurrentGame.getClass().getMethod("setSection", String.class),
	    		cgaCurrentGame.getClass().getMethod("setSetUp", String.class),
	    		cgaCurrentGame.getClass().getMethod("setSite", String.class),
	    		cgaCurrentGame.getClass().getMethod("setStage", String.class),
	    		cgaCurrentGame.getClass().getMethod("setSubVariation", String.class),
	    		cgaCurrentGame.getClass().getMethod("setTermination", String.class),
	    		cgaCurrentGame.getClass().getMethod("setTime", String.class),
	    		cgaCurrentGame.getClass().getMethod("setTimeControl", String.class),
	    		cgaCurrentGame.getClass().getMethod("setUTCDate", String.class),
	    		cgaCurrentGame.getClass().getMethod("setUTCTime", String.class),
	    		cgaCurrentGame.getClass().getMethod("setVariation", String.class),
	    		cgaCurrentGame.getClass().getMethod("setWhite", String.class),
	    		cgaCurrentGame.getClass().getMethod("setWhiteElo", String.class),
	    		cgaCurrentGame.getClass().getMethod("setWhiteNA", String.class),
	    		cgaCurrentGame.getClass().getMethod("setWhiteTitle", String.class),
	    		cgaCurrentGame.getClass().getMethod("setWhiteType", String.class)  
			};
			for (int nI=0; nI<35; nI++) {
				metPGNProperty[nI].setAccessible(true);
				
			}
			
    } catch (Exception e) {
          e.printStackTrace();
    } 								
	
    String sLine;
    int nLineNumber = 0;
	char[] acLine = null;
    int nLineLength;
    int nStartOfTagName;
    int nEndOfTagName;
    int nStartOfTagValue;
    int nEndOfTagValue;
    String sTagName;
    String sTagValue;
    HashSet<String> hseTagName = new HashSet<String>();   // Duplicate values are prohibited.
    StringBuilder sbMoveSection = new StringBuilder();
    int nPos;
//        int nGameNumber=0;
//        PGNGame cgCurrentGame=new PGNGame();
//        ArrayList<PGNGame> arrPGNGame = new ArrayList<PGNGame>();
//    char cFirstLetter;
//    Class[] aclsParameterType=new Class[1];
//    aclsParameterType[0]=String.class;
    Integer intIndex;
    int nIndex;
    
    try {
// OPEN FILE. 
        BufferedReader brInputFile=new BufferedReader(new FileReader(sInputFile));
 
// READ-WRITE FILE.             
        while ((sLine=brInputFile.readLine()) != null) {
            nLineNumber++;
            acLine = sLine.toCharArray();
            nLineLength = acLine.length;
            
           // TAG PAIR SECTION.
           nPos = -1;
           // Search left bracket.
           while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
           while (nPos < nLineLength && acLine[nPos] == '[') {

        	   
               // START OF A NEW GAME.
               if (sbMoveSection.length()>0) { // As order is facultative, "Event" tag is not a valid trigger.
                  // SET MOVE SECTION.
            	   cgaCurrentGame.setMovetext(sbMoveSection.toString());

            	   // CHECK CONSTRAINTS.
                  // APPEND OBJECT TO COLLECTION.
            	   lstcgaReturnValue.add(cgaCurrentGame);
            	   cgaCurrentGame = new PGNGame();
                  // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//    	   		   javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
                  sbMoveSection.setLength(0);
                  hseTagName.clear();
               }

        	   
        	   
        	   // ANALYSE LINE TRYING TO RETRIEVE TAG NAME AND TAG VALUE.
        	   // Search beginning of tag name.
              while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
              nStartOfTagName = nPos;
              // Search beginning of the tag value.
              while (++nPos < nLineLength && acLine[nPos] != '"') ;
              if (nPos < nLineLength) {
                 nStartOfTagValue = nPos;
                 // Search (backward) end of the tag name.
                 nEndOfTagName = nPos;
                 while (acLine[--nEndOfTagName] == ' ') ;   // 0+ spaces allowed between tag name and value.
                 // Search end of the tag value.
                 while (++nPos < nLineLength && acLine[nPos] != '"') ;
                 nEndOfTagValue = nPos;
                 if (nPos < nLineLength) {
                    // Search right bracket.
                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
                    if (nPos != nLineLength && acLine[nPos] == ']') {
						// Extract tag name.
						sTagName = new String(acLine, nStartOfTagName, nEndOfTagName - nStartOfTagName + 1);
						// Extract tag value.
//						sTagValue = new String(acLine, nStartOfTagValue + 1, nEndOfTagValue - nStartOfTagValue - 1);

						// AVOID DUPLICATES (probing hashset).
						if (hseTagName.add(sTagName)) {
                       
							// ADD TAG PAIR TO OBJECT CHESS GAME.
//							if (htaStandardTag.containsKey(sTagName)) {
							if ((intIndex=htaStandardTag.get(sTagName))!=null) {

								// INVOKE METHOD.
								try {
					                metPGNProperty[(int)intIndex].invoke(cgaCurrentGame, new String(acLine, nStartOfTagValue + 1, nEndOfTagValue - nStartOfTagValue - 1));	// boolean bOutput = (boolean)metMethod...
//									metPGNProperty[htaStandardTag.get(sTagName)].invoke(cgaCurrentGame, sTagValue);
							    } catch (Exception e) {
						              e.printStackTrace();
							    } 								
								
								
							}
							else {
//								if (sTagName.compareTo("WhiteFRBEKBSB")!=0 && sTagName.compareTo("BlackFRBEKBSB")!=0&& sTagName.compareTo("WhiteTeam")!=0 && sTagName.compareTo("BlackTeam")!=0) {
//									javax.swing.JOptionPane.showMessageDialog(null, sTagName);
//								}
							}
						}
						else {
							// add to error log.
						}
                    }
                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
                 }
              }
           }

           // MOVETEXT SECTION.
           if (nPos < nLineLength) {   // Avoids empty line.
              sbMoveSection.append(sLine);
              sbMoveSection.append(NEW_LINE);
           }
        }
        // CLOSE FILE.
        // SET MOVE SECTION.
  	   cgaCurrentGame.setMovetext(sbMoveSection.toString());
        // CHECK CONSTRAINTS.
  	   
        // APPEND OBJECT TO COLLECTION.
  	   lstcgaReturnValue.add(cgaCurrentGame);
        // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//   		javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
        hseTagName.clear();
        // FREE RESOURCES.

      //CLOSE FILES.             
        brInputFile.close();                
        brInputFile = null;
    }
    catch (java.io.FileNotFoundException e) {
    	javax.swing.JOptionPane.showMessageDialog(null, "Input file (\"" + sInputFile + "\") does not exist.");
    }
    catch (java.io.IOException e) {
       e.printStackTrace();
    }
    
    return lstcgaReturnValue;
}



public ArrayList<PGNGame> readPGNFile5(String sInputFile) {
//  private ArrayList<PGNGame> readPGNFile(PlayerRecord[] aprPlayerFRBE, String[] asWithdrawn, OLD_FIDERecord[] afrFIDERating, String[] asCity, String[] asEvent, String[] asCountry, String sInputFile, String sOutputFile, StringBuilder sbErrorLog, boolean bAddFRBE, boolean bAddFIDE, boolean bAddTitle, String sUserName) {
	 
//    string sLine = "[Name \"Lucky Luke\"]";
//    sLine = "[\"Lucky Luke\"]";
//    sLine = "[A\"Lucky Luke\"]";
//    sLine = "[Lucky Luke\"]";
//    sLine = "[Lucky Luke]";
    //sLine = "";
    //sLine = " ";
    //sLine = "[]";
    //sLine = "[ ]";
    //sLine = "[\"\"]";
    //sLine = "[Name\"Lucky Luke\"]";
    //sLine = "[Name\"Lucky Luke\"";
//    sLine = "[  Name\"Lucky Luke\"   ]";
    //sLine = "     [  Name\"Lucky Luke\"   ]";
    //sLine = "     [  LastName\"Lucky\"   ][FirstName \"Luke\"]";
    //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName \"Luke\"    ]";
    //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName        \"Luke\"    ]";
    //sLine = "     [  LastName\"Lucky\"   ][    FirstName\"Luke\"    ]";
    //sLine = "     Le chien aboie et la caravanne passe.";
    //sLine = "Name\"Lucky Luke\"";

    PGNGame cgaCurrentGame = new PGNGame();
	ArrayList<PGNGame> lstcgaReturnValue = new ArrayList<PGNGame>();
	Class<? extends PGNGame> claCurrentGame = cgaCurrentGame.getClass();
	Hashtable<String, Method> htaStandardTag = new Hashtable<String, Method>();
	try {

	
		htaStandardTag.put("Annotator", claCurrentGame.getMethod("setAnnotator", String.class));
		htaStandardTag.put("Black", claCurrentGame.getMethod("setBlack", String.class));
		htaStandardTag.put("BlackElo", claCurrentGame.getMethod("setBlackElo", String.class));
		htaStandardTag.put("BlackNA", claCurrentGame.getMethod("setBlackNA", String.class));
		htaStandardTag.put("BlackTitle", claCurrentGame.getMethod("setBlackTitle", String.class));
		htaStandardTag.put("BlackType", claCurrentGame.getMethod("setBlackType", String.class));
		htaStandardTag.put("Board", claCurrentGame.getMethod("setBoard", String.class));
		htaStandardTag.put("Date", claCurrentGame.getMethod("setDate", String.class));
		htaStandardTag.put("ECO", claCurrentGame.getMethod("setECO", String.class));
		htaStandardTag.put("Event", claCurrentGame.getMethod("setEvent", String.class));
		htaStandardTag.put("EventDate", claCurrentGame.getMethod("setEventDate", String.class));
		htaStandardTag.put("EventSponsor", claCurrentGame.getMethod("setEventSponsor", String.class));
		htaStandardTag.put("FEN", claCurrentGame.getMethod("setFEN", String.class));
		htaStandardTag.put("Mode", claCurrentGame.getMethod("setMode", String.class));
		htaStandardTag.put("MoveSection", claCurrentGame.getMethod("setMoveSection", String.class));
		htaStandardTag.put("NIC", claCurrentGame.getMethod("setNIC", String.class));
		htaStandardTag.put("Opening", claCurrentGame.getMethod("setOpening", String.class));
		htaStandardTag.put("PlyCount", claCurrentGame.getMethod("setPlyCount", String.class));
		htaStandardTag.put("Result", claCurrentGame.getMethod("setResult", String.class));
		htaStandardTag.put("Round", claCurrentGame.getMethod("setRound", String.class));
		htaStandardTag.put("Section", claCurrentGame.getMethod("setSection", String.class));
		htaStandardTag.put("SetUp", claCurrentGame.getMethod("setSetUp", String.class));
		htaStandardTag.put("Site", claCurrentGame.getMethod("setSite", String.class));
		htaStandardTag.put("Stage", claCurrentGame.getMethod("setStage", String.class));
		htaStandardTag.put("SubVariation", claCurrentGame.getMethod("setSubVariation", String.class));
		htaStandardTag.put("Termination", claCurrentGame.getMethod("setTermination", String.class));
		htaStandardTag.put("Time", claCurrentGame.getMethod("setTime", String.class));
		htaStandardTag.put("TimeControl", claCurrentGame.getMethod("setTimeControl", String.class));
		htaStandardTag.put("UTCDate", claCurrentGame.getMethod("setUTCDate", String.class));
		htaStandardTag.put("UTCTime", claCurrentGame.getMethod("setUTCTime", String.class));
		htaStandardTag.put("Variation", claCurrentGame.getMethod("setVariation", String.class));
		htaStandardTag.put("White", claCurrentGame.getMethod("setWhite", String.class));
		htaStandardTag.put("WhiteElo", claCurrentGame.getMethod("setWhiteElo", String.class));
		htaStandardTag.put("WhiteNA", claCurrentGame.getMethod("setWhiteNA", String.class));
		htaStandardTag.put("WhiteTitle", claCurrentGame.getMethod("setWhiteTitle", String.class));
		htaStandardTag.put("WhiteType", claCurrentGame.getMethod("setWhiteType", String.class));
	
    } catch (Exception e) {
          e.printStackTrace();
    } 								
	
    String sLine;
    int nLineNumber = 0;
	char[] acLine = null;
    int nLineLength;
    int nStartOfTagName;
    int nEndOfTagName;
    int nStartOfTagValue;
    int nEndOfTagValue;
    String sTagName;
    String sTagValue;
    HashSet<String> hseTagName = new HashSet<String>();   // Duplicate values are prohibited.
    StringBuilder sbMoveSection = new StringBuilder();
    int nPos;
//        int nGameNumber=0;
//        PGNGame cgCurrentGame=new PGNGame();
//        ArrayList<PGNGame> arrPGNGame = new ArrayList<PGNGame>();
//    char cFirstLetter;
//    Class[] aclsParameterType=new Class[1];
//    aclsParameterType[0]=String.class;
    Method metIndex =null;
    
    try {
// OPEN FILE. 
        BufferedReader brInputFile=new BufferedReader(new FileReader(sInputFile));
 
// READ-WRITE FILE.             
        while ((sLine=brInputFile.readLine()) != null) {
            nLineNumber++;
            acLine = sLine.toCharArray();
            nLineLength = acLine.length;
            
           // TAG PAIR SECTION.
           nPos = -1;
           // Search left bracket.
           while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
           while (nPos < nLineLength && acLine[nPos] == '[') {

        	   
               // START OF A NEW GAME.
               if (sbMoveSection.length()>0) { // As order is facultative, "Event" tag is not a valid trigger.
                  // SET MOVE SECTION.
            	   cgaCurrentGame.setMovetext(sbMoveSection.toString());

            	   // CHECK CONSTRAINTS.
                  // APPEND OBJECT TO COLLECTION.
            	   lstcgaReturnValue.add(cgaCurrentGame);
            	   cgaCurrentGame = new PGNGame();
                  // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//    	   		   javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
                  sbMoveSection.setLength(0);
                  hseTagName.clear();
               }

        	   
        	   
        	   // ANALYSE LINE TRYING TO RETRIEVE TAG NAME AND TAG VALUE.
        	   // Search beginning of tag name.
              while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
              nStartOfTagName = nPos;
              // Search beginning of the tag value.
              while (++nPos < nLineLength && acLine[nPos] != '"') ;
              if (nPos < nLineLength) {
                 nStartOfTagValue = nPos;
                 // Search (backward) end of the tag name.
                 nEndOfTagName = nPos;
                 while (acLine[--nEndOfTagName] == ' ') ;   // 0+ spaces allowed between tag name and value.
                 // Search end of the tag value.
                 while (++nPos < nLineLength && acLine[nPos] != '"') ;
                 nEndOfTagValue = nPos;
                 if (nPos < nLineLength) {
                    // Search right bracket.
                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
                    if (nPos != nLineLength && acLine[nPos] == ']') {
						// Extract tag name.
						sTagName = new String(acLine, nStartOfTagName, nEndOfTagName - nStartOfTagName + 1);
						// Extract tag value.
						sTagValue = new String(acLine, nStartOfTagValue + 1, nEndOfTagValue - nStartOfTagValue - 1);

						// AVOID DUPLICATES (probing hashset).
						if (hseTagName.add(sTagName)) {
                       
							// ADD TAG PAIR TO OBJECT CHESS GAME.
							if (htaStandardTag.containsKey(sTagName)) {
//							if ((metIndex=)!=null) {

								// INVOKE METHOD.
								try {
									htaStandardTag.get(sTagName).invoke(cgaCurrentGame, sTagValue);	// boolean bOutput = (boolean)metMethod...
							    }
//								catch (Exception e) {
//						              e.printStackTrace();
//							    } 								
								catch (Exception e) {
							    } 								
								
								
							}
							else {
//								if (sTagName.compareTo("WhiteFRBEKBSB")!=0 && sTagName.compareTo("BlackFRBEKBSB")!=0&& sTagName.compareTo("WhiteTeam")!=0 && sTagName.compareTo("BlackTeam")!=0) {
//									javax.swing.JOptionPane.showMessageDialog(null, sTagName);
//								}
							}
						}
						else {
							// add to error log.
						}
                    }
                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
                 }
              }
           }

           // MOVETEXT SECTION.
           if (nPos < nLineLength) {   // Avoids empty line.
              sbMoveSection.append(sLine);
              sbMoveSection.append(NEW_LINE);
           }
        }
        // CLOSE FILE.
        // SET MOVE SECTION.
  	   cgaCurrentGame.setMovetext(sbMoveSection.toString());
        // CHECK CONSTRAINTS.
  	   
        // APPEND OBJECT TO COLLECTION.
  	   lstcgaReturnValue.add(cgaCurrentGame);
        // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//   		javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
        hseTagName.clear();
        // FREE RESOURCES.

      //CLOSE FILES.             
        brInputFile.close();                
        brInputFile = null;
    }
    catch (java.io.FileNotFoundException e) {
    	javax.swing.JOptionPane.showMessageDialog(null, "Input file (\"" + sInputFile + "\") does not exist.");
    }
    catch (java.io.IOException e) {
       e.printStackTrace();
    }
    
    return lstcgaReturnValue;
}



public ArrayList<PGNGame> readPGNFile6(String sInputFile) {
//  private ArrayList<PGNGame> readPGNFile(PlayerRecord[] aprPlayerFRBE, String[] asWithdrawn, OLD_FIDERecord[] afrFIDERating, String[] asCity, String[] asEvent, String[] asCountry, String sInputFile, String sOutputFile, StringBuilder sbErrorLog, boolean bAddFRBE, boolean bAddFIDE, boolean bAddTitle, String sUserName) {
	 
//    string sLine = "[Name \"Lucky Luke\"]";
//    sLine = "[\"Lucky Luke\"]";
//    sLine = "[A\"Lucky Luke\"]";
//    sLine = "[Lucky Luke\"]";
//    sLine = "[Lucky Luke]";
    //sLine = "";
    //sLine = " ";
    //sLine = "[]";
    //sLine = "[ ]";
    //sLine = "[\"\"]";
    //sLine = "[Name\"Lucky Luke\"]";
    //sLine = "[Name\"Lucky Luke\"";
//    sLine = "[  Name\"Lucky Luke\"   ]";
    //sLine = "     [  Name\"Lucky Luke\"   ]";
    //sLine = "     [  LastName\"Lucky\"   ][FirstName \"Luke\"]";
    //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName \"Luke\"    ]";
    //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName        \"Luke\"    ]";
    //sLine = "     [  LastName\"Lucky\"   ][    FirstName\"Luke\"    ]";
    //sLine = "     Le chien aboie et la caravanne passe.";
    //sLine = "Name\"Lucky Luke\"";

    PGNGame cgaCurrentGame = new PGNGame();
	ArrayList<PGNGame> lstcgaReturnValue = new ArrayList<PGNGame>();
	Class<? extends PGNGame> claCurrentGame = cgaCurrentGame.getClass();
	HashMap<String, Method> htaStandardTag = new HashMap<String, Method>(37, 1);
	try {

	
		htaStandardTag.put("Annotator", claCurrentGame.getMethod("setAnnotator", String.class));
		htaStandardTag.put("Black", claCurrentGame.getMethod("setBlack", String.class));
		htaStandardTag.put("BlackElo", claCurrentGame.getMethod("setBlackElo", String.class));
		htaStandardTag.put("BlackNA", claCurrentGame.getMethod("setBlackNA", String.class));
		htaStandardTag.put("BlackTitle", claCurrentGame.getMethod("setBlackTitle", String.class));
		htaStandardTag.put("BlackType", claCurrentGame.getMethod("setBlackType", String.class));
		htaStandardTag.put("Board", claCurrentGame.getMethod("setBoard", String.class));
		htaStandardTag.put("Date", claCurrentGame.getMethod("setDate", String.class));
		htaStandardTag.put("ECO", claCurrentGame.getMethod("setECO", String.class));
		htaStandardTag.put("Event", claCurrentGame.getMethod("setEvent", String.class));
		htaStandardTag.put("EventDate", claCurrentGame.getMethod("setEventDate", String.class));
		htaStandardTag.put("EventSponsor", claCurrentGame.getMethod("setEventSponsor", String.class));
		htaStandardTag.put("FEN", claCurrentGame.getMethod("setFEN", String.class));
		htaStandardTag.put("Mode", claCurrentGame.getMethod("setMode", String.class));
		htaStandardTag.put("MoveSection", claCurrentGame.getMethod("setMoveSection", String.class));
		htaStandardTag.put("NIC", claCurrentGame.getMethod("setNIC", String.class));
		htaStandardTag.put("Opening", claCurrentGame.getMethod("setOpening", String.class));
		htaStandardTag.put("PlyCount", claCurrentGame.getMethod("setPlyCount", String.class));
		htaStandardTag.put("Result", claCurrentGame.getMethod("setResult", String.class));
		htaStandardTag.put("Round", claCurrentGame.getMethod("setRound", String.class));
		htaStandardTag.put("Section", claCurrentGame.getMethod("setSection", String.class));
		htaStandardTag.put("SetUp", claCurrentGame.getMethod("setSetUp", String.class));
		htaStandardTag.put("Site", claCurrentGame.getMethod("setSite", String.class));
		htaStandardTag.put("Stage", claCurrentGame.getMethod("setStage", String.class));
		htaStandardTag.put("SubVariation", claCurrentGame.getMethod("setSubVariation", String.class));
		htaStandardTag.put("Termination", claCurrentGame.getMethod("setTermination", String.class));
		htaStandardTag.put("Time", claCurrentGame.getMethod("setTime", String.class));
		htaStandardTag.put("TimeControl", claCurrentGame.getMethod("setTimeControl", String.class));
		htaStandardTag.put("UTCDate", claCurrentGame.getMethod("setUTCDate", String.class));
		htaStandardTag.put("UTCTime", claCurrentGame.getMethod("setUTCTime", String.class));
		htaStandardTag.put("Variation", claCurrentGame.getMethod("setVariation", String.class));
		htaStandardTag.put("White", claCurrentGame.getMethod("setWhite", String.class));
		htaStandardTag.put("WhiteElo", claCurrentGame.getMethod("setWhiteElo", String.class));
		htaStandardTag.put("WhiteNA", claCurrentGame.getMethod("setWhiteNA", String.class));
		htaStandardTag.put("WhiteTitle", claCurrentGame.getMethod("setWhiteTitle", String.class));
		htaStandardTag.put("WhiteType", claCurrentGame.getMethod("setWhiteType", String.class));
	
		htaStandardTag.get("Annotator").setAccessible(true);
		htaStandardTag.get("Black").setAccessible(true);
		htaStandardTag.get("BlackElo").setAccessible(true);
		htaStandardTag.get("BlackNA").setAccessible(true);
		htaStandardTag.get("BlackTitle").setAccessible(true);
		htaStandardTag.get("BlackType").setAccessible(true);
		htaStandardTag.get("Board").setAccessible(true);
		htaStandardTag.get("Date").setAccessible(true);
		htaStandardTag.get("ECO").setAccessible(true);
		htaStandardTag.get("Event").setAccessible(true);
		htaStandardTag.get("EventDate").setAccessible(true);
		htaStandardTag.get("EventSponsor").setAccessible(true);
		htaStandardTag.get("FEN").setAccessible(true);
		htaStandardTag.get("Mode").setAccessible(true);
		htaStandardTag.get("MoveSection").setAccessible(true);
		htaStandardTag.get("NIC").setAccessible(true);
		htaStandardTag.get("Opening").setAccessible(true);
		htaStandardTag.get("PlyCount").setAccessible(true);
		htaStandardTag.get("Result").setAccessible(true);
		htaStandardTag.get("Round").setAccessible(true);
		htaStandardTag.get("Section").setAccessible(true);
		htaStandardTag.get("SetUp").setAccessible(true);
		htaStandardTag.get("Site").setAccessible(true);
		htaStandardTag.get("Stage").setAccessible(true);
		htaStandardTag.get("SubVariation").setAccessible(true);
		htaStandardTag.get("Termination").setAccessible(true);
		htaStandardTag.get("Time").setAccessible(true);
		htaStandardTag.get("TimeControl").setAccessible(true);
		htaStandardTag.get("UTCDate").setAccessible(true);
		htaStandardTag.get("UTCTime").setAccessible(true);
		htaStandardTag.get("Variation").setAccessible(true);
		htaStandardTag.get("White").setAccessible(true);
		htaStandardTag.get("WhiteElo").setAccessible(true);
		htaStandardTag.get("WhiteNA").setAccessible(true);
		htaStandardTag.get("WhiteTitle").setAccessible(true);
		htaStandardTag.get("WhiteType").setAccessible(true);
			
		
    } catch (Exception e) {
          e.printStackTrace();
    } 								
	
	
    String sLine;
    int nLineNumber = 0;
	char[] acLine = null;
    int nLineLength;
    int nStartOfTagName;
    int nEndOfTagName;
    int nStartOfTagValue;
    int nEndOfTagValue;
    String sTagName;
    String sTagValue;
    HashSet<String> hseTagName = new HashSet<String>();   // Duplicate values are prohibited.
    StringBuilder sbMoveSection = new StringBuilder();
    int nPos;
//        int nGameNumber=0;
//        PGNGame cgCurrentGame=new PGNGame();
//        ArrayList<PGNGame> arrPGNGame = new ArrayList<PGNGame>();
//    char cFirstLetter;
//    Class[] aclsParameterType=new Class[1];
//    aclsParameterType[0]=String.class;
    Method metIndex =null;
    
    try {
// OPEN FILE. 
        BufferedReader brInputFile=new BufferedReader(new FileReader(sInputFile));
 
// READ-WRITE FILE.             
        while ((sLine=brInputFile.readLine()) != null) {
            nLineNumber++;
            acLine = sLine.toCharArray();
            nLineLength = acLine.length;
            
           // TAG PAIR SECTION.
           nPos = -1;
           // Search left bracket.
           while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
           while (nPos < nLineLength && acLine[nPos] == '[') {

        	   
               // START OF A NEW GAME.
               if (sbMoveSection.length()>0) { // As order is facultative, "Event" tag is not a valid trigger.
                  // SET MOVE SECTION.
            	   cgaCurrentGame.setMovetext(sbMoveSection.toString());

            	   // CHECK CONSTRAINTS.
                  // APPEND OBJECT TO COLLECTION.
            	   lstcgaReturnValue.add(cgaCurrentGame);
            	   cgaCurrentGame = new PGNGame();
                  // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//    	   		   javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
                  sbMoveSection.setLength(0);
                  hseTagName.clear();
               }

        	   
        	   
        	   // ANALYSE LINE TRYING TO RETRIEVE TAG NAME AND TAG VALUE.
        	   // Search beginning of tag name.
              while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
              nStartOfTagName = nPos;
              // Search beginning of the tag value.
              while (++nPos < nLineLength && acLine[nPos] != '"') ;
              if (nPos < nLineLength) {
                 nStartOfTagValue = nPos;
                 // Search (backward) end of the tag name.
                 nEndOfTagName = nPos;
                 while (acLine[--nEndOfTagName] == ' ') ;   // 0+ spaces allowed between tag name and value.
                 // Search end of the tag value.
                 while (++nPos < nLineLength && acLine[nPos] != '"') ;
                 nEndOfTagValue = nPos;
                 if (nPos < nLineLength) {
                    // Search right bracket.
                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
                    if (nPos != nLineLength && acLine[nPos] == ']') {
						// Extract tag name.
						sTagName = new String(acLine, nStartOfTagName, nEndOfTagName - nStartOfTagName + 1);
						// Extract tag value.
						sTagValue = new String(acLine, nStartOfTagValue + 1, nEndOfTagValue - nStartOfTagValue - 1);

						// AVOID DUPLICATES (probing hashset).
						if (hseTagName.add(sTagName)) {
                       
							// ADD TAG PAIR TO OBJECT CHESS GAME.
//							if (htaStandardTag.containsKey(sTagName)) {
							if ((metIndex=htaStandardTag.get(sTagName))!=null) {

								// INVOKE METHOD.
								try {
//									htaStandardTag.get(sTagName).invoke(cgaCurrentGame, sTagValue);	// boolean bOutput = (boolean)metMethod...
									metIndex.invoke(cgaCurrentGame, sTagValue);	// boolean bOutput = (boolean)metMethod...
							    }
//								catch (Exception e) {
//						              e.printStackTrace();
//							    } 								
								catch (Exception e) {
							    } 								
								
								
							}
							else {
//								if (sTagName.compareTo("WhiteFRBEKBSB")!=0 && sTagName.compareTo("BlackFRBEKBSB")!=0&& sTagName.compareTo("WhiteTeam")!=0 && sTagName.compareTo("BlackTeam")!=0) {
//									javax.swing.JOptionPane.showMessageDialog(null, sTagName);
//								}
							}
						}
						else {
							// add to error log.
						}
                    }
                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
                 }
              }
           }

           // MOVETEXT SECTION.
           if (nPos < nLineLength) {   // Avoids empty line.
              sbMoveSection.append(sLine);
              sbMoveSection.append(NEW_LINE);
           }
        }
        // CLOSE FILE.
        // SET MOVE SECTION.
  	   cgaCurrentGame.setMovetext(sbMoveSection.toString());
        // CHECK CONSTRAINTS.
  	   
        // APPEND OBJECT TO COLLECTION.
  	   lstcgaReturnValue.add(cgaCurrentGame);
        // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//   		javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
        hseTagName.clear();
        // FREE RESOURCES.

      //CLOSE FILES.             
        brInputFile.close();                
        brInputFile = null;
    }
    catch (java.io.FileNotFoundException e) {
    	javax.swing.JOptionPane.showMessageDialog(null, "Input file (\"" + sInputFile + "\") does not exist.");
    }
    catch (java.io.IOException e) {
       e.printStackTrace();
    }
    
    return lstcgaReturnValue;
}


public ArrayList<PGNGame> readPGNFile7(String sInputFile) {
//  private ArrayList<PGNGame> readPGNFile(PlayerRecord[] aprPlayerFRBE, String[] asWithdrawn, OLD_FIDERecord[] afrFIDERating, String[] asCity, String[] asEvent, String[] asCountry, String sInputFile, String sOutputFile, StringBuilder sbErrorLog, boolean bAddFRBE, boolean bAddFIDE, boolean bAddTitle, String sUserName) {
	 
//    string sLine = "[Name \"Lucky Luke\"]";
//    sLine = "[\"Lucky Luke\"]";
//    sLine = "[A\"Lucky Luke\"]";
//    sLine = "[Lucky Luke\"]";
//    sLine = "[Lucky Luke]";
    //sLine = "";
    //sLine = " ";
    //sLine = "[]";
    //sLine = "[ ]";
    //sLine = "[\"\"]";
    //sLine = "[Name\"Lucky Luke\"]";
    //sLine = "[Name\"Lucky Luke\"";
//    sLine = "[  Name\"Lucky Luke\"   ]";
    //sLine = "     [  Name\"Lucky Luke\"   ]";
    //sLine = "     [  LastName\"Lucky\"   ][FirstName \"Luke\"]";
    //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName \"Luke\"    ]";
    //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName        \"Luke\"    ]";
    //sLine = "     [  LastName\"Lucky\"   ][    FirstName\"Luke\"    ]";
    //sLine = "     Le chien aboie et la caravanne passe.";
    //sLine = "Name\"Lucky Luke\"";

    PGNGame cgaCurrentGame = new PGNGame();
	ArrayList<PGNGame> lstcgaReturnValue = new ArrayList<PGNGame>();
	HashMap<String, Integer> htaStandardTag = new HashMap<String, Integer>();
	htaStandardTag.put("Annotator", new Integer(0));
	htaStandardTag.put("Black", new Integer(1));
	htaStandardTag.put("BlackElo", new Integer(2));
	htaStandardTag.put("BlackNA", new Integer(3));
	htaStandardTag.put("BlackTitle", new Integer(4));
	htaStandardTag.put("BlackType", new Integer(5));
	htaStandardTag.put("Board", new Integer(6));
	htaStandardTag.put("Date", new Integer(7));
	htaStandardTag.put("ECO", new Integer(8));
	htaStandardTag.put("Event", new Integer(9));
	htaStandardTag.put("EventDate", new Integer(10));
	htaStandardTag.put("EventSponsor", new Integer(11));
	htaStandardTag.put("FEN", new Integer(12));
	htaStandardTag.put("Mode", new Integer(13));
	htaStandardTag.put("MoveSection", new Integer(14));
	htaStandardTag.put("NIC", new Integer(15));
	htaStandardTag.put("Opening", new Integer(16));
	htaStandardTag.put("PlyCount", new Integer(17));
	htaStandardTag.put("Result", new Integer(18));
	htaStandardTag.put("Round", new Integer(19));
	htaStandardTag.put("Section", new Integer(20));
	htaStandardTag.put("SetUp", new Integer(21));
	htaStandardTag.put("Site", new Integer(22));
	htaStandardTag.put("Stage", new Integer(23));
	htaStandardTag.put("SubVariation", new Integer(24));
	htaStandardTag.put("Termination", new Integer(25));
	htaStandardTag.put("Time", new Integer(26));
	htaStandardTag.put("TimeControl", new Integer(27));
	htaStandardTag.put("UTCDate", new Integer(28));
	htaStandardTag.put("UTCTime", new Integer(29));
	htaStandardTag.put("Variation", new Integer(30));
	htaStandardTag.put("White", new Integer(31));
	htaStandardTag.put("WhiteElo", new Integer(32));
	htaStandardTag.put("WhiteNA", new Integer(33));
	htaStandardTag.put("WhiteTitle", new Integer(34));
	htaStandardTag.put("WhiteType", new Integer(35));
	
	Method[] metPGNProperty = null;
	Class<? extends PGNGame> claCurrentGame = cgaCurrentGame.getClass();

	try {
			metPGNProperty= new Method[] {
    		claCurrentGame.getMethod("setAnnotator", String.class),
    		claCurrentGame.getMethod("setBlack", String.class),
    		claCurrentGame.getMethod("setBlackElo", String.class),
    		claCurrentGame.getMethod("setBlackNA", String.class),
    		claCurrentGame.getMethod("setBlackTitle", String.class),
    		claCurrentGame.getMethod("setBlackType", String.class),
    		claCurrentGame.getMethod("setBoard", String.class),
    		claCurrentGame.getMethod("setDate", String.class),
    		claCurrentGame.getMethod("setECO", String.class),
    		claCurrentGame.getMethod("setEvent", String.class),
    		claCurrentGame.getMethod("setEventDate", String.class),
    		claCurrentGame.getMethod("setEventSponsor", String.class),
    		claCurrentGame.getMethod("setFEN", String.class),
    		claCurrentGame.getMethod("setMode", String.class),
    		claCurrentGame.getMethod("setMoveSection", String.class),
    		claCurrentGame.getMethod("setNIC", String.class),
    		claCurrentGame.getMethod("setOpening", String.class),
    		claCurrentGame.getMethod("setPlyCount", String.class),
    		claCurrentGame.getMethod("setResult", String.class),
    		claCurrentGame.getMethod("setRound", String.class),
    		claCurrentGame.getMethod("setSection", String.class),
    		claCurrentGame.getMethod("setSetUp", String.class),
    		claCurrentGame.getMethod("setSite", String.class),
    		claCurrentGame.getMethod("setStage", String.class),
    		claCurrentGame.getMethod("setSubVariation", String.class),
    		claCurrentGame.getMethod("setTermination", String.class),
    		claCurrentGame.getMethod("setTime", String.class),
    		claCurrentGame.getMethod("setTimeControl", String.class),
    		claCurrentGame.getMethod("setUTCDate", String.class),
    		claCurrentGame.getMethod("setUTCTime", String.class),
    		claCurrentGame.getMethod("setVariation", String.class),
    		claCurrentGame.getMethod("setWhite", String.class),
    		claCurrentGame.getMethod("setWhiteElo", String.class),
    		claCurrentGame.getMethod("setWhiteNA", String.class),
    		claCurrentGame.getMethod("setWhiteTitle", String.class),
    		claCurrentGame.getMethod("setWhiteType", String.class)  
    };
    } catch (Exception e) {
          e.printStackTrace();
    } 								
	
    String sLine;
    int nLineNumber = 0;
	char[] acLine = null;
    int nLineLength;
    int nStartOfTagName;
    int nEndOfTagName;
    int nStartOfTagValue;
    int nEndOfTagValue;
    String sTagName;
    String sTagValue;
    HashSet<String> hseTagName = new HashSet<String>();   // Duplicate values are prohibited.
    StringBuilder sbMoveSection = new StringBuilder();
    int nPos;
//        int nGameNumber=0;
//        PGNGame cgCurrentGame=new PGNGame();
//        ArrayList<PGNGame> arrPGNGame = new ArrayList<PGNGame>();
//    char cFirstLetter;
//    Class[] aclsParameterType=new Class[1];
//    aclsParameterType[0]=String.class;
    Integer intIndex;
    int nIndex;
    
    try {
// OPEN FILE. 
        BufferedReader brInputFile=new BufferedReader(new FileReader(sInputFile));
 
// READ-WRITE FILE.             
        while ((sLine=brInputFile.readLine()) != null) {
            nLineNumber++;
            acLine = sLine.toCharArray();
            nLineLength = acLine.length;
            
           // TAG PAIR SECTION.
           nPos = -1;
           // Search left bracket.
           while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
           while (nPos < nLineLength && acLine[nPos] == '[') {

        	   
               // START OF A NEW GAME.
               if (sbMoveSection.length()>0) { // As order is facultative, "Event" tag is not a valid trigger.
                  // SET MOVE SECTION.
            	   cgaCurrentGame.setMovetext(sbMoveSection.toString());

            	   // CHECK CONSTRAINTS.
                  // APPEND OBJECT TO COLLECTION.
            	   lstcgaReturnValue.add(cgaCurrentGame);
            	   cgaCurrentGame = new PGNGame();
                  // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//    	   		   javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
                  sbMoveSection.setLength(0);
                  hseTagName.clear();
               }

        	   
        	   
        	   // ANALYSE LINE TRYING TO RETRIEVE TAG NAME AND TAG VALUE.
        	   // Search beginning of tag name.
              while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
              nStartOfTagName = nPos;
              // Search beginning of the tag value.
              while (++nPos < nLineLength && acLine[nPos] != '"') ;
              if (nPos < nLineLength) {
                 nStartOfTagValue = nPos;
                 // Search (backward) end of the tag name.
                 nEndOfTagName = nPos;
                 while (acLine[--nEndOfTagName] == ' ') ;   // 0+ spaces allowed between tag name and value.
                 // Search end of the tag value.
                 while (++nPos < nLineLength && acLine[nPos] != '"') ;
                 nEndOfTagValue = nPos;
                 if (nPos < nLineLength) {
                    // Search right bracket.
                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
                    if (nPos != nLineLength && acLine[nPos] == ']') {
						// Extract tag name.
						sTagName = new String(acLine, nStartOfTagName, nEndOfTagName - nStartOfTagName + 1);
						// Extract tag value.
						sTagValue = new String(acLine, nStartOfTagValue + 1, nEndOfTagValue - nStartOfTagValue - 1);

						// AVOID DUPLICATES (probing hashset).
						if (hseTagName.add(sTagName)) {
                       
							// ADD TAG PAIR TO OBJECT CHESS GAME.
//							if (htaStandardTag.containsKey(sTagName)) {
							if ((intIndex=htaStandardTag.get(sTagName))!=null) {

								// INVOKE METHOD.
								try {
					                metPGNProperty[(int)intIndex].invoke(cgaCurrentGame, sTagValue);	// boolean bOutput = (boolean)metMethod...
//									metPGNProperty[htaStandardTag.get(sTagName)].invoke(cgaCurrentGame, sTagValue);
							    } catch (Exception e) {
						              e.printStackTrace();
							    } 								
								
								
							}
							else {
//								if (sTagName.compareTo("WhiteFRBEKBSB")!=0 && sTagName.compareTo("BlackFRBEKBSB")!=0&& sTagName.compareTo("WhiteTeam")!=0 && sTagName.compareTo("BlackTeam")!=0) {
//									javax.swing.JOptionPane.showMessageDialog(null, sTagName);
//								}
							}
						}
						else {
							// add to error log.
						}
                    }
                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
                 }
              }
           }

           // MOVETEXT SECTION.
           if (nPos < nLineLength) {   // Avoids empty line.
              sbMoveSection.append(sLine);
              sbMoveSection.append(NEW_LINE);
           }
        }
        // CLOSE FILE.
        // SET MOVE SECTION.
  	   cgaCurrentGame.setMovetext(sbMoveSection.toString());
        // CHECK CONSTRAINTS.
  	   
        // APPEND OBJECT TO COLLECTION.
  	   lstcgaReturnValue.add(cgaCurrentGame);
        // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//   		javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
        hseTagName.clear();
        // FREE RESOURCES.

      //CLOSE FILES.             
        brInputFile.close();                
        brInputFile = null;
    }
    catch (java.io.FileNotFoundException e) {
    	javax.swing.JOptionPane.showMessageDialog(null, "Input file (\"" + sInputFile + "\") does not exist.");
    }
    catch (java.io.IOException e) {
       e.printStackTrace();
    }
    
    return lstcgaReturnValue;
}


public ArrayList<PGNGame> readPGNFile8(String sInputFile) {
//  private ArrayList<PGNGame> readPGNFile(PlayerRecord[] aprPlayerFRBE, String[] asWithdrawn, OLD_FIDERecord[] afrFIDERating, String[] asCity, String[] asEvent, String[] asCountry, String sInputFile, String sOutputFile, StringBuilder sbErrorLog, boolean bAddFRBE, boolean bAddFIDE, boolean bAddTitle, String sUserName) {
	 
//    string sLine = "[Name \"Lucky Luke\"]";
//    sLine = "[\"Lucky Luke\"]";
//    sLine = "[A\"Lucky Luke\"]";
//    sLine = "[Lucky Luke\"]";
//    sLine = "[Lucky Luke]";
    //sLine = "";
    //sLine = " ";
    //sLine = "[]";
    //sLine = "[ ]";
    //sLine = "[\"\"]";
    //sLine = "[Name\"Lucky Luke\"]";
    //sLine = "[Name\"Lucky Luke\"";
//    sLine = "[  Name\"Lucky Luke\"   ]";
    //sLine = "     [  Name\"Lucky Luke\"   ]";
    //sLine = "     [  LastName\"Lucky\"   ][FirstName \"Luke\"]";
    //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName \"Luke\"    ]";
    //sLine = "     [  LastName\"Lucky\"   ]      [    FirstName        \"Luke\"    ]";
    //sLine = "     [  LastName\"Lucky\"   ][    FirstName\"Luke\"    ]";
    //sLine = "     Le chien aboie et la caravanne passe.";
    //sLine = "Name\"Lucky Luke\"";

	int nPos;
    int nOffSet;
    final int STANDARD_TAGS_COUNT = 35;
    int nFound;
	int nLengthOfSearched;
	char cFirstSearchedChar;
    
	char[] acStandardTags = {
            'A', 'n', 'n', 'o', 't', 'a', 't', 'o', 'r', '\0', '\0', '\0',
            'B', 'l', 'a', 'c', 'k', '\0', '\0', '\0', '\0', '\0', '\0', '\0',
            'B', 'l', 'a', 'c', 'k', 'E', 'l', 'o', '\0', '\0', '\0', '\0',
            'B', 'l', 'a', 'c', 'k', 'N', 'A', '\0', '\0', '\0', '\0', '\0',
            'B', 'l', 'a', 'c', 'k', 'T', 'i', 't', 'l', 'e', '\0', '\0',
            'B', 'l', 'a', 'c', 'k', 'T', 'y', 'p', 'e', '\0', '\0', '\0',
            'B', 'o', 'a', 'r', 'd', '\0', '\0', '\0', '\0', '\0', '\0', '\0',
            'D', 'a', 't', 'e', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0',
            'E', 'C', 'O', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0',
            'E', 'v', 'e', 'n', 't', '\0', '\0', '\0', '\0', '\0', '\0', '\0',
            'E', 'v', 'e', 'n', 't', 'D', 'a', 't', 'e', '\0', '\0', '\0',
            'E', 'v', 'e', 'n', 't', 'S', 'p', 'o', 'n', 's', 'o', 'r',
            'F', 'E', 'N', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0',
            'M', 'o', 'd', 'e', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0',
            'N', 'I', 'C', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0',
            'O', 'p', 'e', 'n', 'i', 'n', 'g', '\0', '\0', '\0', '\0', '\0',
            'P', 'l', 'y', 'C', 'o', 'u', 'n', 't', '\0', '\0', '\0', '\0',
            'R', 'e', 's', 'u', 'l', 't', '\0', '\0', '\0', '\0', '\0', '\0',
            'R', 'o', 'u', 'n', 'd', '\0', '\0', '\0', '\0', '\0', '\0', '\0',
            'S', 'e', 'c', 't', 'i', 'o', 'n', '\0', '\0', '\0', '\0', '\0',
            'S', 'e', 't', 'U', 'p', '\0', '\0', '\0', '\0', '\0', '\0', '\0',
            'S', 'i', 't', 'e', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0',
            'S', 't', 'a', 'g', 'e', '\0', '\0', '\0', '\0', '\0', '\0', '\0',
            'S', 'u', 'b', 'V', 'a', 'r', 'i', 'a', 't', 'i', 'o', 'n',
            'T', 'e', 'r', 'm', 'i', 'n', 'a', 't', 'i', 'o', 'n', '\0',
            'T', 'i', 'm', 'e', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0',
            'T', 'i', 'm', 'e', 'C', 'o', 'n', 't', 'r', 'o', 'l', '\0',
            'U', 'T', 'C', 'D', 'a', 't', 'e', '\0', '\0', '\0', '\0', '\0',
            'U', 'T', 'C', 'T', 'i', 'm', 'e', '\0', '\0', '\0', '\0', '\0',
            'V', 'a', 'r', 'i', 'a', 't', 'i', 'o', 'n', '\0', '\0', '\0',
            'W', 'h', 'i', 't', 'e', '\0', '\0', '\0', '\0', '\0', '\0', '\0',
            'W', 'h', 'i', 't', 'e', 'E', 'l', 'o', '\0', '\0', '\0', '\0',
            'W', 'h', 'i', 't', 'e', 'N', 'A', '\0', '\0', '\0', '\0', '\0',
            'W', 'h', 'i', 't', 'e', 'T', 'i', 't', 'l', 'e', '\0', '\0',
            'W', 'h', 'i', 't', 'e', 'T', 'y', 'p', 'e', '\0', '\0', '\0'
         };
         int[] anLengthOfStandardTags = { 9, 5, 8, 7, 10, 9, 5, 4, 3, 5, 9, 12, 3, 4, 3, 7, 8, 6, 5, 7, 5,
            4, 5, 12, 11, 4, 11, 7, 7, 9, 5, 8, 7, 10, 9};
         char[] acFirstLetterOfStandardTags = {'A', 'B', 'B', 'B', 'B', 'B', 'B', 'D', 'E', 'E', 'E', 'E',
            'F', 'M', 'N', 'O', 'P', 'R', 'R', 'S', 'S', 'S', 'S', 'S', 'T', 'T', 'T', 'U', 'U', 'V', 'W',
            'W', 'W', 'W', 'W'};
         int[] anBinarySearch = {0, 1, -1, 7, 8, 12, -1, -1, -1, -1, -1, -1, 13, 14, 15, 16, -1, 17, 19, 24,
            27, 29, 30, -1, -1, -1};   // Position of the first tag beginning with a given letter (e.g.: alphabetically the first to begin with a 'D' is the 7th, 'C' has none).	
	
	
    PGNGame cgaCurrentGame = new PGNGame();
	ArrayList<PGNGame> lstcgaReturnValue = new ArrayList<PGNGame>();
	
	
	Method[] metPGNProperty = null;
//	Class<? extends PGNGame> claCurrentGame = cgaCurrentGame.getClass();

	try {
			metPGNProperty= new Method[] {
    		cgaCurrentGame.getClass().getMethod("setAnnotator", String.class),
    		cgaCurrentGame.getClass().getMethod("setBlack", String.class),
    		cgaCurrentGame.getClass().getMethod("setBlackElo", String.class),
    		cgaCurrentGame.getClass().getMethod("setBlackNA", String.class),
    		cgaCurrentGame.getClass().getMethod("setBlackTitle", String.class),
    		cgaCurrentGame.getClass().getMethod("setBlackType", String.class),
    		cgaCurrentGame.getClass().getMethod("setBoard", String.class),
    		cgaCurrentGame.getClass().getMethod("setDate", String.class),
    		cgaCurrentGame.getClass().getMethod("setECO", String.class),
    		cgaCurrentGame.getClass().getMethod("setEvent", String.class),
    		cgaCurrentGame.getClass().getMethod("setEventDate", String.class),
    		cgaCurrentGame.getClass().getMethod("setEventSponsor", String.class),
    		cgaCurrentGame.getClass().getMethod("setFEN", String.class),
    		cgaCurrentGame.getClass().getMethod("setMode", String.class),
//    		cgaCurrentGame.getClass().getMethod("setMoveSection", String.class),
    		cgaCurrentGame.getClass().getMethod("setNIC", String.class),
    		cgaCurrentGame.getClass().getMethod("setOpening", String.class),
    		cgaCurrentGame.getClass().getMethod("setPlyCount", String.class),
    		cgaCurrentGame.getClass().getMethod("setResult", String.class),
    		cgaCurrentGame.getClass().getMethod("setRound", String.class),
    		cgaCurrentGame.getClass().getMethod("setSection", String.class),
    		cgaCurrentGame.getClass().getMethod("setSetUp", String.class),
    		cgaCurrentGame.getClass().getMethod("setSite", String.class),
    		cgaCurrentGame.getClass().getMethod("setStage", String.class),
    		cgaCurrentGame.getClass().getMethod("setSubVariation", String.class),
    		cgaCurrentGame.getClass().getMethod("setTermination", String.class),
    		cgaCurrentGame.getClass().getMethod("setTime", String.class),
    		cgaCurrentGame.getClass().getMethod("setTimeControl", String.class),
    		cgaCurrentGame.getClass().getMethod("setUTCDate", String.class),
    		cgaCurrentGame.getClass().getMethod("setUTCTime", String.class),
    		cgaCurrentGame.getClass().getMethod("setVariation", String.class),
    		cgaCurrentGame.getClass().getMethod("setWhite", String.class),
    		cgaCurrentGame.getClass().getMethod("setWhiteElo", String.class),
    		cgaCurrentGame.getClass().getMethod("setWhiteNA", String.class),
    		cgaCurrentGame.getClass().getMethod("setWhiteTitle", String.class),
    		cgaCurrentGame.getClass().getMethod("setWhiteType", String.class)  
    };
    } catch (Exception e) {
          e.printStackTrace();
    } 								
	
    String sLine;
    int nLineNumber = 0;
	char[] acLine = null;
    int nLineLength;
    int nStartOfTagName;
    int nEndOfTagName;
    int nStartOfTagValue;
    int nEndOfTagValue;
    String sTagName;
    String sTagValue;
    HashSet<String> hseTagName = new HashSet<String>();   // Duplicate values are prohibited.
    StringBuilder sbMoveSection = new StringBuilder();
    int nPos2;
//        int nGameNumber=0;
//        PGNGame cgCurrentGame=new PGNGame();
//        ArrayList<PGNGame> arrPGNGame = new ArrayList<PGNGame>();
//    char cFirstLetter;
//    Class[] aclsParameterType=new Class[1];
//    aclsParameterType[0]=String.class;
    Integer intIndex;
    int nIndex;
    
    try {
// OPEN FILE. 
        BufferedReader brInputFile=new BufferedReader(new FileReader(sInputFile));
 
// READ-WRITE FILE.             
        while ((sLine=brInputFile.readLine()) != null) {
            nLineNumber++;
            acLine = sLine.toCharArray();
            nLineLength = acLine.length;
            
           // TAG PAIR SECTION.
           nPos = -1;
           // Search left bracket.
           while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
           while (nPos < nLineLength && acLine[nPos] == '[') {

        	   
               // START OF A NEW GAME.
               if (sbMoveSection.length()>0) { // As order is facultative, "Event" tag is not a valid trigger.
                  // SET MOVE SECTION.
            	   cgaCurrentGame.setMovetext(sbMoveSection.toString());

            	   // CHECK CONSTRAINTS.
                  // APPEND OBJECT TO COLLECTION.
            	   lstcgaReturnValue.add(cgaCurrentGame);
            	   cgaCurrentGame = new PGNGame();
                  // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//    	   		   javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
                  sbMoveSection.setLength(0);
                  hseTagName.clear();
               }

        	   
        	   
        	   // ANALYSE LINE TRYING TO RETRIEVE TAG NAME AND TAG VALUE.
        	   // Search beginning of tag name.
              while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
              nStartOfTagName = nPos;
              // Search beginning of the tag value.
              while (++nPos < nLineLength && acLine[nPos] != '"') ;
              if (nPos < nLineLength) {
                 nStartOfTagValue = nPos;
                 // Search (backward) end of the tag name.
                 nEndOfTagName = nPos;
                 while (acLine[--nEndOfTagName] == ' ') ;   // 0+ spaces allowed between tag name and value.
                 // Search end of the tag value.
                 while (++nPos < nLineLength && acLine[nPos] != '"') ;
                 nEndOfTagValue = nPos;
                 if (nPos < nLineLength) {
                    // Search right bracket.
                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
                    if (nPos != nLineLength && acLine[nPos] == ']') {
//						sTagName = new String(acLine, nStartOfTagName, nEndOfTagName - nStartOfTagName + 1);
//						sTagValue = new String(acLine, nStartOfTagValue + 1, nEndOfTagValue - nStartOfTagValue - 1);

						// AVOID DUPLICATES (probing hashset).
						if (hseTagName.add(new String(acLine, nStartOfTagName, nEndOfTagName - nStartOfTagName + 1))) {
                       
							// ADD TAG PAIR TO CURRENT CHESS GAME.
							nLengthOfSearched = nEndOfTagName - nStartOfTagName + 1;
							if (nLengthOfSearched >2 && nLengthOfSearched < 13) {	// DISCARD: shortest standard tag name are ECO, FEN, NIC ; longest are EventSponsor and SubVariation. 
								cFirstSearchedChar = acLine[nStartOfTagName];
								if (cFirstSearchedChar >= 'A' && cFirstSearchedChar <= 'Z') {	// DISCARD: every tag name starts with a non-accented uppercase.
									// SEARCH TAG NAME FOUND IN THE PGN FILE IN THE STANDARD TAG NAMES. 
							         nFound=anBinarySearch[cFirstSearchedChar-'A'];	// Fast search on the first letter.
							         if (nFound!=-1) {
							        	 // Browse names of the standard tags list while their first letter matches our.
							            do {
							               if (anLengthOfStandardTags[nFound] == nLengthOfSearched) {	// Compare file tag name to standard tag name list based on length of the names.
							                  // Compare the two tags letter by letter.
							                  nPos2=0;
							                  nOffSet = nFound * 12;           // 12 = "row" length of a acStandardTag[] <=> length of the longest standard tag <=>
							                  while (++nPos2<nLengthOfSearched && acStandardTags[nOffSet+nPos2]==acLine[nStartOfTagName + nPos2]);
							                  if (nPos2 == nLengthOfSearched) { // Found.
							                     cFirstSearchedChar = 'Z';     // Trick to exit from the do...while loop (no tag starts with a 'Z').
							                  }
							               }
							            } while (++nFound<STANDARD_TAGS_COUNT && acFirstLetterOfStandardTags[nFound]==cFirstSearchedChar);
							            if (cFirstSearchedChar=='Z') {
											try {
								                metPGNProperty[nFound-1].invoke(cgaCurrentGame, new String(acLine, nStartOfTagValue + 1, nEndOfTagValue - nStartOfTagValue - 1));	// boolean bOutput = (boolean)metMethod...
										    } catch (Exception e) {
									              e.printStackTrace();
										    } 								
							            }
							         }
								}
							}
						}
							
							
							
							
							
							
							
							
							
							
							
							
							
							
//							if ((intIndex=htaStandardTag.get(sTagName))!=null) {

								// INVOKE METHOD.
//								try {
//					                metPGNProperty[(int)intIndex].invoke(cgaCurrentGame, sTagValue);	// boolean bOutput = (boolean)metMethod...
////									metPGNProperty[htaStandardTag.get(sTagName)].invoke(cgaCurrentGame, sTagValue);
//							    } catch (Exception e) {
//						              e.printStackTrace();
//							    } 								
								
								
//							}
//							else {
//								if (sTagName.compareTo("WhiteFRBEKBSB")!=0 && sTagName.compareTo("BlackFRBEKBSB")!=0&& sTagName.compareTo("WhiteTeam")!=0 && sTagName.compareTo("BlackTeam")!=0) {
//									javax.swing.JOptionPane.showMessageDialog(null, sTagName);
//								}
//							}
//						}
//						else {
//							// add to error log.
//						}
                    }
                    while (++nPos < nLineLength && acLine[nPos] == ' ') ;  // Skip spaces when necessary.
                 }
              }
           }

           // MOVETEXT SECTION.
           if (nPos < nLineLength) {   // Avoids empty line.
              sbMoveSection.append(sLine);
              sbMoveSection.append(NEW_LINE);
           }
        }
        // CLOSE FILE.
        // SET MOVE SECTION.
  	   cgaCurrentGame.setMovetext(sbMoveSection.toString());
        // CHECK CONSTRAINTS.
  	   
        // APPEND OBJECT TO COLLECTION.
  	   lstcgaReturnValue.add(cgaCurrentGame);
        // CLEAR TAG AND MOVE SECTION AND DOUBLON SET.
//   		javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
        hseTagName.clear();
        // FREE RESOURCES.

      //CLOSE FILES.             
        brInputFile.close();                
        brInputFile = null;
    }
    catch (java.io.FileNotFoundException e) {
    	javax.swing.JOptionPane.showMessageDialog(null, "Input file (\"" + sInputFile + "\") does not exist.");
    }
    catch (java.io.IOException e) {
       e.printStackTrace();
    }
    
    return lstcgaReturnValue;
}

	public void writePGN(ArrayList<PGNGame> apgnGameToOutput) {
    	final String EOL = System.getProperty("line.separator");
    	final String END_OF_TAG =  "\"]" + EOL;
		
		// TODO: to generalize this method, this must not be specific to my own use.
		// TODO: to see if this method must be partially placed in the PGNGame.toString().
		
		try {
			int nGameCount = apgnGameToOutput.size();

			// DISCARD.
			if (nGameCount==0) {
				return;
			}
			 
			PGNGame pgnCurrentGame = apgnGameToOutput.get(0);
			BufferedWriter bwPGNFile = new BufferedWriter(new FileWriter("newgame.out"));

			
			
	        StringBuilder sbTextToExport = new StringBuilder();


			
			
			
			for (int nI=0; nI<nGameCount; nI++) {
				pgnCurrentGame = apgnGameToOutput.get(nI);
				// Seven Tag Roster.
		        sbTextToExport.append("[Event \"");
		        sbTextToExport.append(pgnCurrentGame.getEvent());
		        sbTextToExport.append(END_OF_TAG);
		        sbTextToExport.append("[Site \"");
		        sbTextToExport.append(pgnCurrentGame.getSite());
		        sbTextToExport.append(END_OF_TAG);
		        sbTextToExport.append("[Date \"");
		        sbTextToExport.append(pgnCurrentGame.getDate());
		        sbTextToExport.append(END_OF_TAG);
		        sbTextToExport.append("[Round \"");
		        sbTextToExport.append(pgnCurrentGame.getRound());
		        sbTextToExport.append(END_OF_TAG);
		        sbTextToExport.append("[White \"");
		        sbTextToExport.append(pgnCurrentGame.getWhite());
		        sbTextToExport.append(END_OF_TAG);
		        sbTextToExport.append("[Black \"");
		        sbTextToExport.append(pgnCurrentGame.getBlack());
		        sbTextToExport.append(END_OF_TAG);
		        sbTextToExport.append("[Result \"");
		        sbTextToExport.append(pgnCurrentGame.getResult());
		        sbTextToExport.append(END_OF_TAG);
				// Annotator.
				if (!pgnCurrentGame.getAnnotator().equals("?")) {
			        sbTextToExport.append("[Annotator \"");
			        sbTextToExport.append(pgnCurrentGame.getAnnotator());
			        sbTextToExport.append(END_OF_TAG);
				}
				// BlackElo
		        sbTextToExport.append("[BlackElo \"");
		        sbTextToExport.append(pgnCurrentGame.getBlackElo());
		        sbTextToExport.append(END_OF_TAG);
				// BlackNA
				if ((!pgnCurrentGame.getWhiteNA().equals("?")) || (!pgnCurrentGame.getBlackNA().equals("?"))) {
			        sbTextToExport.append("[BlackNA \"");
			        sbTextToExport.append(pgnCurrentGame.getBlackNA());
			        sbTextToExport.append(END_OF_TAG);
				}

				
				// TEST: RETRIEVING ALL CUSTOMS TAGS.
				String sTagName;
				pgnCurrentGame.addCustomTagPair("cerise", "ananas");
				TreeMap<String, String> mapCustomTagPair = pgnCurrentGame.getAllCustomTagPairs();
				java.util.Iterator<String> itCustomTagPair = mapCustomTagPair.keySet().iterator();
			    while (itCustomTagPair.hasNext()) {
			    	sTagName = itCustomTagPair.next();
			    	javax.swing.JOptionPane.showMessageDialog(null, "(" + sTagName + ", " + mapCustomTagPair.get(sTagName) + ")");
			    }
			    itCustomTagPair = null;
			    mapCustomTagPair.clear();
			    mapCustomTagPair = null;
			    sTagName = null;

			    
				// BlackTitle
		        sbTextToExport.append("[BlackTitle \"");
		        sbTextToExport.append(pgnCurrentGame.getBlackTitle());
		        sbTextToExport.append(END_OF_TAG);
				// BlackType
		        if (!pgnCurrentGame.getWhiteType().equals("human") || !pgnCurrentGame.getBlackType().equals("human")) {
			        sbTextToExport.append("[BlackType \"");
			        sbTextToExport.append(pgnCurrentGame.getBlackType());
			        sbTextToExport.append(END_OF_TAG);
				}
				// Board
		        sbTextToExport.append("[Board \"");
		        sbTextToExport.append(pgnCurrentGame.getBoard());
		        sbTextToExport.append(END_OF_TAG);
				// ECO
		        sbTextToExport.append("[ECO \"");
		        sbTextToExport.append(pgnCurrentGame.getECO());
		        sbTextToExport.append(END_OF_TAG);
				// EventDate
		        sbTextToExport.append("[EventDate \"");
		        sbTextToExport.append(pgnCurrentGame.getEventDate());
		        sbTextToExport.append(END_OF_TAG);
				// EventSponsor
		        if (!(pgnCurrentGame.getEventSponsor().equals("?") || pgnCurrentGame.getEventSponsor().equals("-"))) {
			        sbTextToExport.append("[EventSponsor \"");
			        sbTextToExport.append(pgnCurrentGame.getEventSponsor());
			        sbTextToExport.append(END_OF_TAG);
		        }
				// FEN
				if (!pgnCurrentGame.getSetUp().equals("0")) {
			        sbTextToExport.append("[FEN \"");
			        sbTextToExport.append(pgnCurrentGame.getFEN());
			        sbTextToExport.append(END_OF_TAG);
				}
				// Mode
		        if (!pgnCurrentGame.getMode().equals("OTB")) {
			        sbTextToExport.append("[Mode \"");
			        sbTextToExport.append(pgnCurrentGame.getMode());
			        sbTextToExport.append(END_OF_TAG);
		        }
				// NIC
		        sbTextToExport.append("[NIC \"");
		        sbTextToExport.append(pgnCurrentGame.getNIC());
		        sbTextToExport.append(END_OF_TAG);
				// Opening
		        sbTextToExport.append("[Opening \"");
		        sbTextToExport.append(pgnCurrentGame.getOpening());
		        sbTextToExport.append(END_OF_TAG);
				// PlyCount
		        sbTextToExport.append("[PlyCount \"");
		        sbTextToExport.append(pgnCurrentGame.getPlyCount());
		        sbTextToExport.append(END_OF_TAG);
				// Section
		        sbTextToExport.append("[Section \"");
		        sbTextToExport.append(pgnCurrentGame.getSection());
		        sbTextToExport.append(END_OF_TAG);
				// SetUp
				if (!pgnCurrentGame.getSetUp().equals("0")) {
			        sbTextToExport.append("[SetUp \"");
			        sbTextToExport.append(pgnCurrentGame.getSetUp());
			        sbTextToExport.append(END_OF_TAG);
				}
				// Stage
//				if (!(pgnCurrentGame.getStage().equals("?") || pgnCurrentGame.getStage().equals("-"))) {
			        sbTextToExport.append("[Stage \"");
			        sbTextToExport.append(pgnCurrentGame.getStage());
			        sbTextToExport.append(END_OF_TAG);
//				}
				// SubVariation
				if (!pgnCurrentGame.getSubVariation().equals("?")) {
			        sbTextToExport.append("[SubVariation \"");
			        sbTextToExport.append(pgnCurrentGame.getSubVariation());
			        sbTextToExport.append(END_OF_TAG);
				}
				// Termination
//				if (!pgnCurrentGame.getTermination().equals("normal")) {
			        sbTextToExport.append("[Termination \"");
			        sbTextToExport.append(pgnCurrentGame.getTermination());
			        sbTextToExport.append(END_OF_TAG);
//				}
				// Time
		        sbTextToExport.append("[Time \"");
		        sbTextToExport.append(pgnCurrentGame.getTime());
		        sbTextToExport.append(END_OF_TAG);
				// TimeControl
		        sbTextToExport.append("[TimeControl \"");
		        sbTextToExport.append(pgnCurrentGame.getTimeControl());
		        sbTextToExport.append(END_OF_TAG);
				// UTCDate
//				if (!pgnCurrentGame.getUTCDate().equals("????.??.??")) {
			        sbTextToExport.append("[UTCDate \"");
			        sbTextToExport.append(pgnCurrentGame.getUTCDate());
			        sbTextToExport.append(END_OF_TAG);
//				}
				// UTCTime
//				if (!pgnCurrentGame.getUTCTime().equals("??:??:??")) {
			        sbTextToExport.append("[UTCTime \"");
			        sbTextToExport.append(pgnCurrentGame.getUTCTime());
			        sbTextToExport.append(END_OF_TAG);
//				}
				// Variation
		        sbTextToExport.append("[Variation \"");
		        sbTextToExport.append(pgnCurrentGame.getVariation());
		        sbTextToExport.append(END_OF_TAG);
				// WhiteElo
		        sbTextToExport.append("[WhiteElo \"");
		        sbTextToExport.append(pgnCurrentGame.getWhiteElo());
		        sbTextToExport.append(END_OF_TAG);
				// WhiteNA
				if ((!pgnCurrentGame.getWhiteNA().equals("?")) || (!pgnCurrentGame.getBlackNA().equals("?"))) {
			        sbTextToExport.append("[WhiteNA \"");
			        sbTextToExport.append(pgnCurrentGame.getWhiteNA());
			        sbTextToExport.append(END_OF_TAG);
				}
				// WhiteTitle
		        sbTextToExport.append("[WhiteTitle \"");
		        sbTextToExport.append(pgnCurrentGame.getWhiteTitle());
		        sbTextToExport.append(END_OF_TAG);
				// WhiteType
		        if (!pgnCurrentGame.getWhiteType().equals("human") || !pgnCurrentGame.getBlackType().equals("human")) {
			        sbTextToExport.append("[WhiteType \"");
			        sbTextToExport.append(pgnCurrentGame.getWhiteType());
			        sbTextToExport.append(END_OF_TAG);
				}
		        sbTextToExport.append(EOL);
		        
		        // TODO: to avoid hardcoding the Custom Tag Pairs.
		        PlayerDBF temp =new PlayerDBF();
		        sbTextToExport.append("[BlackFRBEKBSB \"");
		        sbTextToExport.append(String.valueOf(temp.getFRBE(pgnCurrentGame.getBlack())));
		        sbTextToExport.append(END_OF_TAG);
		        sbTextToExport.append("[BlackTeam \"?");
		        sbTextToExport.append(END_OF_TAG);
		        sbTextToExport.append("[WhiteFRBEKBSB \"");
		        sbTextToExport.append(String.valueOf(temp.getFRBE(pgnCurrentGame.getWhite())));
		        sbTextToExport.append(END_OF_TAG);
		        sbTextToExport.append("[WhiteTeam \"?");
		        sbTextToExport.append(END_OF_TAG);
		        sbTextToExport.append(EOL);
		        temp=null;

				// MOVE SECTION.
		        sbTextToExport.append(pgnCurrentGame.getMovetext());
		        sbTextToExport.append(EOL);	// Insert blank line.

				// WRITE TEXT TO FILE.
				bwPGNFile.write(sbTextToExport.toString());
				bwPGNFile.newLine();

			}			 
			bwPGNFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}     		
	}

	public void Puzzle2LaTeX(ArrayList<PGNGame> cgOutput) {
		try {
			int nGameCount = cgOutput.size();

			// DISCARD.
			if (nGameCount==0) {
				return;
			}
			 
			
			
			BufferedWriter bwPGNFile = new BufferedWriter(new FileWriter("newgame.out.pgn"));
			
			
			
			bwPGNFile.write("\\documentclass[10pt]{article}");
			bwPGNFile.newLine();

			bwPGNFile.write("\\usepackage[ps]{skak}");
			bwPGNFile.write("\\usepackage{times}");
			bwPGNFile.write("\\usepackage{a4wide}");
			bwPGNFile.newLine();
			bwPGNFile.write("\\begin{document}");
			bwPGNFile.newLine();
			bwPGNFile.write("\noindent");
			bwPGNFile.write("\\setlength{\\parindent}{0pt}");
			bwPGNFile.write("\\smallboard");
			bwPGNFile.write("\\notationoff");
			bwPGNFile.newLine();
			bwPGNFile.write("\\section{Preface}");
			bwPGNFile.write("The 3500 exercises of this document are provided \"as it\" without any guaranty of any kind. They come from the site of Claude Kaber. PGN files can be retrieved at http://webplaza.pt.lu/ckaber/chess.htm\\#chess\\%20training. As far as I know, they are free of use.");
			bwPGNFile.newLine();
			bwPGNFile.write("Puzzle difficulty range from \"*\" to \"*****\".");
			bwPGNFile.write("If you have any question or suggestion of any kind, let me know. My e-mail is:");
			bwPGNFile.write("tactics.to.stronghold@spamgourmet.com");

			PGNGame pgnCurrentGame = cgOutput.get(0);
			bwPGNFile.write("[Event \"" + pgnCurrentGame.getEvent() + "\"]");
			bwPGNFile.newLine();
			bwPGNFile.write("[Site \"" + pgnCurrentGame.getSite() + "\"]");
			bwPGNFile.newLine();
			bwPGNFile.write("[Date \"" + pgnCurrentGame.getDate() + "\"]");
			bwPGNFile.newLine();
			bwPGNFile.write("[Round \"" + pgnCurrentGame.getRound() + "\"]");
			bwPGNFile.newLine();
			bwPGNFile.write("[White \"" + pgnCurrentGame.getWhite() + "\"]");
			bwPGNFile.newLine();
			bwPGNFile.write("[Black \"" + pgnCurrentGame.getBlack() + "\"]");
			bwPGNFile.newLine();
			bwPGNFile.write("[Result \"" + pgnCurrentGame.getResult() + "\"]");
			bwPGNFile.newLine();
			if (pgnCurrentGame.getSetUp().equals("1")) {
				bwPGNFile.write("[SetUp \"" + pgnCurrentGame.getSetUp() + "\"]");
				bwPGNFile.newLine();
				bwPGNFile.write("[FEN \"" + pgnCurrentGame.getFEN() + "\"]");
				bwPGNFile.newLine();
			}
			if (!pgnCurrentGame.getEventDate().equals("????.??.??")) {
				bwPGNFile.write("[EventDate \"" + pgnCurrentGame.getEventDate() + "\"]");
				bwPGNFile.newLine();
			}
			if (!pgnCurrentGame.getAnnotator().equals("?")) {
				bwPGNFile.write("[Annotator \"" + pgnCurrentGame.getAnnotator() + "\"]");
				bwPGNFile.newLine();
			}
			bwPGNFile.newLine();
			bwPGNFile.write(pgnCurrentGame.getMovetext());
			bwPGNFile.newLine();
			
			for (int nI=1; nI<nGameCount; nI++) {
				bwPGNFile.newLine();
				bwPGNFile.write("[Event \"" + cgOutput.get(nI).getEvent() + "\"]");
				bwPGNFile.newLine();
				bwPGNFile.write("[Site \"" + cgOutput.get(nI).getSite() + "\"]");
				bwPGNFile.newLine();
				bwPGNFile.write("[Date \"" + cgOutput.get(nI).getDate() + "\"]");
				bwPGNFile.newLine();
				bwPGNFile.write("[Round \"" + cgOutput.get(nI).getRound() + "\"]");
				bwPGNFile.newLine();
				bwPGNFile.write("[White \"" + cgOutput.get(nI).getWhite() + "\"]");
				bwPGNFile.newLine();
				bwPGNFile.write("[Black \"" + cgOutput.get(nI).getBlack() + "\"]");
				bwPGNFile.newLine();
				bwPGNFile.write("[Result \"" + cgOutput.get(nI).getResult() + "\"]");
				bwPGNFile.newLine();
				if (cgOutput.get(nI).getSetUp().equals("1")) {
					bwPGNFile.write("[SetUp \"" + cgOutput.get(nI).getSetUp() + "\"]");
					bwPGNFile.newLine();
					bwPGNFile.write("[FEN \"" + cgOutput.get(nI).getFEN() + "\"]");
					bwPGNFile.newLine();
				}
				if (!cgOutput.get(nI).getEventDate().equals("????.??.??")) {
					bwPGNFile.write("[EventDate \"" + cgOutput.get(nI).getEventDate() + "\"]");
					bwPGNFile.newLine();
				}
				if (!cgOutput.get(nI).getAnnotator().equals("?")) {
					bwPGNFile.write("[Annotator \"" + cgOutput.get(nI).getAnnotator() + "\"]");
					bwPGNFile.newLine();
				}
				bwPGNFile.write("[PlyCount \"" + cgOutput.get(nI).getPlyCount() + "\"]");
				bwPGNFile.newLine();
				// Move section.
				bwPGNFile.newLine();
				bwPGNFile.write(cgOutput.get(nI).getMovetext());
				bwPGNFile.newLine();
			}			 
			bwPGNFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}     		
		
	}
	
}

