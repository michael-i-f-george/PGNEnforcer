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

    private final String NEW_LINE = System.getProperty("line.separator");

    public ArrayList<PGNGame> readPGNFile(String sInputFile) {
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
//                          javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
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
//                                                cgaCurrentGame.setPlyCount(sTagValue);
                                            }
                                            else {
                                                // Custom tag pair.
                                                cgaCurrentGame.addCustomTagPair(sTagName, sTagValue);
                                            }
                                        }
                                    }
                                }
                                else {
                                    if (cFirstLetter<'S') {    // >Q <S
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
                                    else {    // >Q >=S
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
                                        else {    // >Q >=S >=U
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
//               javax.swing.JOptionPane.showMessageDialog(null, cgaCurrentGame.getMoveSection());
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
//                if (!(pgnCurrentGame.getStage().equals("?") || pgnCurrentGame.getStage().equals("-"))) {
                    sbTextToExport.append("[Stage \"");
                    sbTextToExport.append(pgnCurrentGame.getStage());
                    sbTextToExport.append(END_OF_TAG);
//                }
                // SubVariation
                if (!pgnCurrentGame.getSubVariation().equals("?")) {
                    sbTextToExport.append("[SubVariation \"");
                    sbTextToExport.append(pgnCurrentGame.getSubVariation());
                    sbTextToExport.append(END_OF_TAG);
                }
                // Termination
//                if (!pgnCurrentGame.getTermination().equals("normal")) {
                    sbTextToExport.append("[Termination \"");
                    sbTextToExport.append(pgnCurrentGame.getTermination());
                    sbTextToExport.append(END_OF_TAG);
//                }
                // Time
                sbTextToExport.append("[Time \"");
                sbTextToExport.append(pgnCurrentGame.getTime());
                sbTextToExport.append(END_OF_TAG);
                // TimeControl
                sbTextToExport.append("[TimeControl \"");
                sbTextToExport.append(pgnCurrentGame.getTimeControl());
                sbTextToExport.append(END_OF_TAG);
                // UTCDate
//                if (!pgnCurrentGame.getUTCDate().equals("????.??.??")) {
                    sbTextToExport.append("[UTCDate \"");
                    sbTextToExport.append(pgnCurrentGame.getUTCDate());
                    sbTextToExport.append(END_OF_TAG);
//                }
                // UTCTime
//                if (!pgnCurrentGame.getUTCTime().equals("??:??:??")) {
                    sbTextToExport.append("[UTCTime \"");
                    sbTextToExport.append(pgnCurrentGame.getUTCTime());
                    sbTextToExport.append(END_OF_TAG);
//                }
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
                sbTextToExport.append(EOL);    // Insert blank line.

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

