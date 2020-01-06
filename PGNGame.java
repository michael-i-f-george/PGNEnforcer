package org.chess.pgn;

import java.util.TreeMap;

//import org.chess.pgn.FIDERecord.Activity;
//import org.chess.pgn.FIDERecord.Sex;

public class PGNGame {
	
	// Last change: October 16 2012. 

	// CONSTANTS.
	private final String FEN_OF_STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
//    private final String END_OF_LINE = "\r\n";  // TODO: to get EOL value from environment.
	private final String END_OF_LINE = System.getProperty("line.separator");

    // MEMBER VARIABLES.
    private String msEvent="?";
    private String msSite="?";
    private String msDate="????.??.??";
    private String msRound="?";
    private String msWhite="?";
    private String msBlack="?";
    private String msResult="*";
    private String msAnnotator="?";
    private String msBlackElo="?";
    private String msBlackNA="?"; 
    private String msBlackTitle="?";
    private String msBlackType="?"; 
    private String msBoard="?";
    private String msECO="?";
    private String msEventDate="????.??.??"; 
    private String msEventSponsor="?"; 
    private String msFEN=FEN_OF_STARTING_POSITION;
    private String msMode="?"; 
    private String msNIC="?";
    private String msOpening="?";
	private String msPlyCount = "0";	// setPlyCount is not implemented (it's a computation on sMoveSection).
    private String msSection="?"; 
    private String msSetUp="0";	// setSetUp is not implemented (it's just of flag returning msFEN==FEN_OF_STARTING_POSITION).
    private String msStage="?"; 
    private String msSubVariation="?";
    private String msTermination="normal"; 
    private String msTime="??:??:??"; 
    private String msTimeControl="?";
    private String msUTCDate="????.??.??"; 
    private String msUTCTime="??:??:??"; 
    private String msVariation="?";
    private String msWhiteElo="?";
    private String msWhiteNA="?"; 
    private String msWhiteTitle="?";
    private String msWhiteType="?";

	private String msMovetext = "*";
    
	private TreeMap<String, String> mmapCustomTagPair = new TreeMap<String, String>();

    // Custom tags.
    // TODO: remove custom tags from code.
//    private String msWhiteTeam="";
//    private String msBlackTeam="";
//    private String msWhiteFRBEKBSB="";
//    private String msBlackFRBEKBSB="";

    
    
    
    public PGNGame() {
//    	msEvent="?";
//    	msSite="?";
//    	msDate="????.??.??";
//    	msRound="?";
//    	msWhite="?";
//    	msBlack="?";
//    	msResult="*";
    	reset();
    }
    public void setEvent(String sEvent) {
        msEvent=sEvent;
    }
    public String getEvent() {
        return msEvent;
    }
    public void setSite(String sSite) {
        msSite=sSite;
    }
    public String getSite() {
        return msSite;
    }
    public boolean setDate(String sDate) {
        boolean bReturnValue=false;

//        if (sDate.matches("(\\?{4}|\\d{4})\\.(\\?\\?|0[1-9]|1[0-2])\\.(\\?\\?|0[1-9]|[12]\\d|3(0|1))")) {	// Allows thinks like "2012.04.31" or "2011.02.29" but goes faster.
//            msDate=sDate;
//            bReturnValue=true;
//        }
        if (sDate.matches("(\\d{4}|\\?{4})\\.(\\d{2}|\\?{2})\\.(\\d{2}|\\?{2})")) {	// Allows thinks like "2012.04.31" or "2011.02.29" but goes faster.
            msDate=sDate;
            bReturnValue=true;
        }
        
//        /* Year. */
//        if (sDate.matches("\\d\\d\\d\\d.*") || sDate.startsWith("????")) {
//            /* Month and dots. */
//            int nMonth=0;
//            if ((sDate.matches("....\\.\\d\\d\\...")&&(nMonth=Integer.parseInt(sDate.substring(5,7))-1)<12&&nMonth>-1) || sDate.matches("....\\.\\?\\?\\...")) {
//                /* Day. */
//                int[] anDaysInMonth={31,28,31,30,31,30,31,31,30,31,30,31};
//                if ((sDate.matches(".*\\d\\d")&&Integer.parseInt(sDate.substring(8,10))<=anDaysInMonth[nMonth]) || sDate.endsWith("??")) {
//                    msDate=sDate;
//                    bReturnValue=true;
//                }
//            }
//        }
        return bReturnValue;
    }
    public String getDate() {
        return msDate;
    }
    public void setRound(String sRound) {
        msRound=sRound;
    }
    public String getRound() {
        return msRound;
    }
    public void setWhite(String sName) {
        msWhite=sName;
    }
    /**
     * 
     * @return name of the player or players of the white pieces
     */
    public String getWhite() {
        return msWhite;
    }
    public void setBlack(String sName) {
        msBlack=sName;
    }
    
    /**
     * 
     * @return name of the player or players of the black pieces
     */
    public String getBlack() {
        return msBlack;
    }
/*    public boolean setResult(String sResult) {
        boolean bReturnValue=false;
        
        if (sResult.equals("0-1") || sResult.equals("1/2-1/2") || sResult.equals("1-0") || sResult.equals("*")) {
        	if (!msResult.equals(sResult)) {
        		if (msResult.equals("*")) {
            		msResult = sResult; 
            		setMovetextResult(sResult);
            		bReturnValue = true;
        		}
        		else {
            		msResult = "*"; 
            		setMovetextResult("*");
        		}
    		}
        }
        else {
    		msResult="*";
    		setMovetextResult("*");
    		bReturnValue = false;
        }
        
        return bReturnValue;
    }
*/    
    public void setResult(String sResult) {
       
        if (sResult.equals("0-1") || sResult.equals("1/2-1/2") || sResult.equals("1-0")) {
    		msResult = sResult;
    		this.setMovetextResult(sResult);
        }
        else {
        	msResult = "*";
    		this.setMovetextResult("*");
        }
    }
    public String getResult() {
        return msResult;
    }
    public boolean setWhiteTitle(String sTitle) {
        boolean bReturnValue=false;
        
//        if (sTitle.matches("(^\\-|W?[CFIG]M|\\?)(:\\-|W?[CFIG]M|\\?)*$")) {
//      if (sTitle.matches("(\\-|W?[CFIG]M|\\?)(:\\-|W?[CFIG]M|\\?)*")) {
    	  if (sTitle.equals("-") || sTitle.equals("?") || sTitle.matches("(\\-|W?[CFIG]M|\\?)(:\\-|W?[CFIG]M|\\?)*")) {
//       if (sTitle.equals("-") || sTitle.equals("?") || sTitle.equals("FM") || sTitle.equals("IM") || sTitle.equals("GM") || sTitle.equals("WFM") || sTitle.equals("WIM") || sTitle.equals("WGM")) {
            msWhiteTitle=sTitle;
            bReturnValue=true;
        } 
        return bReturnValue;
    }
    public String getWhiteTitle() {
        return msWhiteTitle;
    }
    public boolean setBlackTitle(String sTitle) {
        boolean bReturnValue=false;
        
//        if (sTitle.matches("^(\\-|W?(C|F|I|G)M|\\?)(:\\-|W?(C|F|I|G)M|\\?)*$")) {
//      if (sTitle.matches("(^\\-|W?[CFIG]M|\\?)(:\\-|W?[CFIG]M|\\?)*$")) {
//      if (sTitle.matches("(\\-|W?[CFIG]M|\\?)(:\\-|W?[CFIG]M|\\?)*")) {
    	  if (sTitle.equals("-") || sTitle.equals("?") || sTitle.matches("(\\-|W?[CFIG]M|\\?)(:\\-|W?[CFIG]M|\\?)*")) {
//          if (sTitle.equals("-") || sTitle.equals("?") || sTitle.equals("FM") || sTitle.equals("IM") || sTitle.equals("GM") || sTitle.equals("WFM") || sTitle.equals("WIM") || sTitle.equals("WGM")) {
            msBlackTitle=sTitle;
            bReturnValue=true;
        } 
        return bReturnValue;
    }
    public String getBlackTitle() {
        return msBlackTitle;
    }
    public void setWhiteElo(String sElo) {
        msWhiteElo=sElo;
    }
    public String getWhiteElo() {
        return msWhiteElo;
    }
    public void setBlackElo(String sElo) {
        msBlackElo=sElo;
    }
    public String getBlackElo() {
        return msBlackElo;
    }
//    public void setWhiteFRBEKBSB(String sRating) {
//        msWhiteFRBEKBSB=sRating;
//    }
//    public String getWhiteFRBEKBSB() {
//        return msWhiteFRBEKBSB;
//    }
//    public void setBlackFRBEKBSB(String sRating) {
//        msBlackFRBEKBSB=sRating;
//    }
//    public String getBlackFRBEKBSB() {
//        return msBlackFRBEKBSB;
//    }
//    public void setWhiteTeam(String sTeam) {
//        msWhiteTeam=sTeam;
//    }
//    public String getWhiteTeam() {
//        return msWhiteTeam;
//    }
//    public void setBlackTeam(String sTeam) {
//        msBlackTeam=sTeam;
//    }
//    public String getBlackTeam() {
//        return msBlackTeam;
//    }
//    public boolean setSetUp(String sSetUp) {
//        boolean bReturnValue=false;
//        
//        if (sSetUp.equals("0") || sSetUp.equals("1")) {
//            msSetUp=sSetUp;
//            bReturnValue=true;
//        }
//        return bReturnValue;
//    }
    public String getSetUp() {
        return msSetUp;
    }
    public void setFEN(String sFEN) {
        msFEN=sFEN;

        if (sFEN.equals(FEN_OF_STARTING_POSITION)) {
        	msSetUp="0";
        }
        else {
        	msSetUp="1";
        }
    }
    public String getFEN() {
        return msFEN;
    }
    public boolean setTimeControl(String sTimeControl) {
    	boolean bReturnValue=false;
        
//    	if (sTimeControl.matches("\\?|-|\\*?[1-9][0-9]*|[1-9][0-9]*\\+[1-9][0-9]*|([1-9][0-9]*/[1-9][0-9]*)(:[1-9][0-9]*/[1-9][0-9]*)*(:(\\*?[1-9][0-9]*)|([1-9][0-9]*\\+[1-9][0-9]*))?")) {
    	if (sTimeControl.matches("(-|\\d+/\\d+:)*(\\*?\\d+|(\\d+(/|\\+)\\d+))|\\?")) {
        	msTimeControl=sTimeControl;
        	bReturnValue=true;
    	}
    	return bReturnValue;
    }
    public String getTimeControl() {
        return msTimeControl;
    }
    private String convertS2DHMinS(int nTime) {
        String sReturnValue="";

        int nDays = nTime / 86400;
        int nHours = (nTime % 86400) / 3600;
        int nMinutes = (nTime % 3600) / 60;
        int nSeconds = (nTime % 60);

        if (nDays > 0) {
           sReturnValue += " " + String.valueOf(nDays) + " j";
        }
        if (nHours > 0) {
           sReturnValue += " " + String.valueOf(nHours) + " h";
        }
        if (nMinutes > 0) {
           if (nDays > 0 || nHours == 0 || nSeconds > 0) {
              sReturnValue += " " + String.valueOf(nMinutes) + " min";
           }
           else {
              sReturnValue += " " + String.valueOf(nMinutes);
           }
        }
        if (nSeconds > 0) {
           sReturnValue += " " + String.valueOf(nSeconds) + " s";
        }

        return sReturnValue;
     }
     public String getTimeControlInFrench() {

        // ?
        // -          Pas de contrï¿½le temps = Aucun
        // 300        $300/60$ min
        // *180       $180/60$ min / coup
        // 5400+30    $5400/3600$ h $(5400 mod 3600)/60$ min + $30$ s / coup
        // 40/7200    40 coups / $7200/3600$ h

        // 40/7200:3600'  --> 40 coups / 2 h, 1 h KO

        // REM: < 15 min --> blitz
        // REM: <= 60 --> partie rapide


        // DISCARD (time control unknown or no time control)
        if (msTimeControl=="?" || msTimeControl=="-") { 
           return msTimeControl;
        }


        String sReturnValue = "";
        String sMoves;
        String sTime;
        int nMainTime;
        int nIncrementTime;
        int nMoves;
        int nTime;
        int nTotalMoves = 0;
        int nTotalTime = 0;
        String sCurrentControl;
        String[] asTimeControl = msTimeControl.split(":");
        for (int nI = 0; nI < asTimeControl.length; nI++) {
           if (nI > 0) {
              sReturnValue += ",";
           }
           sCurrentControl = asTimeControl[nI];
           // N/N (n moves in n seconds)
           if (sCurrentControl.indexOf('/')!=-1) {
              sMoves=sCurrentControl.substring(0,sCurrentControl.indexOf('/'));
              sReturnValue+=sMoves;
              nMoves = Integer.parseInt(sMoves);
              if (nMoves>1) {
                 sReturnValue+= " coups /";
              }
              else {
                 sReturnValue+= " coup /";
              }
              sTime = sCurrentControl.substring(sCurrentControl.indexOf('/') + 1, sCurrentControl.length());
              nTime=Integer.parseInt(sTime);
              sReturnValue += convertS2DHMinS(nTime);
              nTotalMoves += nMoves;
              nTotalTime += nMoves * nTime;
           }
           else {
           // N+N (incremental)
              if (sCurrentControl.indexOf('+') != -1) {
                 nMainTime = Integer.parseInt(sCurrentControl.substring(0, sCurrentControl.indexOf('+')));
                 sReturnValue += convertS2DHMinS(nMainTime);
                 nIncrementTime = Integer.parseInt(sCurrentControl.substring(sCurrentControl.indexOf('+') + 1, sCurrentControl.length()));
                 sReturnValue += " + " + convertS2DHMinS(nIncrementTime) + " / coup";
                 nTotalTime += nMainTime + 60 * nIncrementTime;
              }
              else {
           // *N (hourglass)
                 if (sCurrentControl.startsWith("*")) {
                    nTime = Integer.parseInt(sCurrentControl.substring(1, sCurrentControl.length()));
                    sReturnValue += convertS2DHMinS(nTime) + " / coup";
                    nTotalTime+=(60-nTotalMoves)*nTime;
                 }
           // N (sudden death)
                 else {
                    nTime = Integer.parseInt(sCurrentControl);
                    sReturnValue += convertS2DHMinS(nTime);
                    if (nI>0 && nI == asTimeControl.length - 1) {   // Last control.
                       sReturnValue+= " KO";
                    }
                    nTotalTime += nTime;
                 }
              }
           }
        }


        // ADD "(blitz)" OR "(partie rapide)" COMMENT. 
        if (nTotalTime < 900) {   // nTotalTime < 15 minutes => blitz
           sReturnValue += " (blitz)";
        }
        else {
           if (nTotalTime < 3601) { // 15 min <= nTotalTime <= 1 hour
              sReturnValue += " (partie rapide)";
           }
        }

        return sReturnValue;
     }
    public boolean setECO(String sECO) {
        boolean bReturnValue=false;

        if (sECO.matches("[A-E]\\d\\d*")) {
            msECO=sECO;
            bReturnValue=true;
        }
        return bReturnValue;
    }
    public String getECO() {
        return msECO;
    }
    public boolean setNIC(String sNIC) {
        boolean bReturnValue=false;
        if ((sNIC.matches("[A-Z][A-Z] \\d\\d?") || sNIC.matches("[A-Z][A-Z] \\d[0-9]?\\.\\d\\d?") || sNIC.matches("[A-Z][A-Z] \\d\\d?\\.\\d\\d?\\.\\d\\d?"))) {
            msNIC=sNIC;
            bReturnValue=true;
        }
        return bReturnValue;
    }
    public String getNIC() {
        return msNIC;
    }
    public void setOpening(String sOpening) {
        msOpening=sOpening;
    }
    public String getOpening() {
        return msOpening;
    }
    public void setVariation(String sVariation) {
        msVariation=sVariation;
    }
    public String getVariation() {
        return msVariation;
    }
    public void setSubVariation(String sSubVariation) {
    	msSubVariation = sSubVariation;
    }
    public String getSubVariation() {
    	return msSubVariation;
    }
    public String getAnnotator() {
    	return msAnnotator;
    }
    public void setAnnotator(String sAnnotator) {
    	msAnnotator = sAnnotator;
    }
    public String getBoard() {
    	return msBoard;
    }
    public void setBoard(String sBoard) {
    	msBoard = sBoard;
    }
    public String getTime() {
    	return msTime;
    }
    public void setTime(String sTime) {
//    	if (sTime.matches("(\\?\\?|[01][0-9]|2[0-3]):(\\?\\?|[0-5][0-9]):(\\?\\?|[0-5][0-9])")) {
        	msTime = sTime;
//        	bReturnValue=true;
//    	}
    }
    public String getSection() {
    	return msSection;
    }
    public void setSection(String sSection) {
    	msSection = sSection;
    }
    public String getTermination() {
    	return msTermination;
    }
    public void setTermination(String sTermination) {
    	if (sTermination.matches("abandoned|adjudication|death|emergency|normal|rules infraction|time forfeit|unterminated")) {
        	msTermination = sTermination;
    	}
    	else {
    		msTermination="normal";
    	}
    }
    public String getWhiteNA() {
    	return msWhiteNA;
    }
    public void setWhiteNA(String sWhiteNA) {
    	msWhiteNA = sWhiteNA;
    }
    public String getWhiteType() {
    	return msWhiteType;
    }
    public void setWhiteType(String sWhiteType) {
    	msWhiteType = sWhiteType;
    }
    public String getBlackNA() {
    	return msBlackNA;
    }
    public void setBlackNA(String sBlackNA) {
    	msBlackNA = sBlackNA;
    }
    public String getBlackType() {
    	return msBlackType;
    }
    public void setBlackType(String sBlackType) {
    	msBlackType = sBlackType;
    }
	public String getPlyCount() {
		return msPlyCount;	// Avoid recomputing it on each query.
	}
    public String getMode() {
    	return msMode;
    }
    public void setMode(String sMode) {
    	msMode = sMode;
    }
    public String getEventDate() {
    	return msEventDate;
    }
    public void setEventDate(String sEventDate) {
    	msEventDate = sEventDate;
    }
    public String getEventSponsor() {
    	return msEventSponsor;
    }
    public void setEventSponsor(String sEventSponsor) {
    	msEventSponsor = sEventSponsor;
    }
    public String getStage() {
    	return msStage;
    }
    public void setStage(String sStage) {
    	msStage = sStage;
    }
    public String getUTCDate() {
    	return msUTCDate;
    }
    public void setUTCDate(String sUTCDate) {
    	msUTCDate = sUTCDate;
    }
    public String getUTCTime() {
    	return msUTCTime;
    }
    public void setUTCTime(String sUTCTime) {
    	msUTCTime = sUTCTime;
    }
    private int computePlyCount_OLD() {
        int nPlyCount = 0;
        int nMovetextLength = msMovetext.length();
        boolean bCurlyBracketOpen = false;
        boolean bRoundBracketOpen = false;
        boolean bIsLastCharASpace = true;
        boolean bNewLine = true;
        boolean bEscapeRestOfLine = false;
        char cCurrentChar;

        int nPos = -1;
        while (++nPos < nMovetextLength) {
           cCurrentChar = msMovetext.charAt(nPos);

           // IDENTIFY ESCAPE LINE.
           if (cCurrentChar == '%' && bNewLine) {
              bEscapeRestOfLine = true;
              bNewLine = false;
           }

           // END OF LINE?
           if (cCurrentChar == ' ' || cCurrentChar == '\n' || cCurrentChar == '\r') {
              bIsLastCharASpace = true;
              if (cCurrentChar == '\n' || cCurrentChar == '\r') {
                 bEscapeRestOfLine = false;
                 bNewLine = true;
              }
              else {
                 bNewLine = false;
              }
           }
           else {
              if (bIsLastCharASpace) {
                 if (cCurrentChar > '9' || cCurrentChar < '0') {
                    if (cCurrentChar == '(') {
                       if (!bEscapeRestOfLine && !bCurlyBracketOpen) {
                          bRoundBracketOpen = true;
                       }
                    }
                    else if (cCurrentChar == '{') {
                       if (!bEscapeRestOfLine) {
                          bCurlyBracketOpen = true;
                       }
                    }
                    else if (cCurrentChar == ';') {
                       if (!bCurlyBracketOpen) {
                          bEscapeRestOfLine = true;
                       }
                    }
                    else if (!bRoundBracketOpen && !bCurlyBracketOpen && cCurrentChar != '$') {
                       if (!bEscapeRestOfLine) {
                          nPlyCount++;
                       }
                    }
                 }
                 bIsLastCharASpace = false;
              }
              if (cCurrentChar == ')') {
                 if (!bCurlyBracketOpen && !bEscapeRestOfLine) {
                    bRoundBracketOpen = false;
                 }
              }
              else if (cCurrentChar == '}') {
                 if (!bEscapeRestOfLine) {
                    bCurlyBracketOpen=false;
                 }
              }
           }
        }


        // RETURN VALUE.
        if (bRoundBracketOpen || bCurlyBracketOpen) {
           nPlyCount = 0;
        }
        return nPlyCount;

     }
    
    private int computePlyCount_OLD2() {
        int nWordCount = 0;
        int nTextLength = msMovetext.length();
        char cCurrentChar;
        int nRoundBracketOpen = 0;
        int nPos=-1;


        // DISCARD FIRST LINE IF BEGINNING WITH '%'.
        if (nTextLength > 0 && msMovetext.charAt(0) == '%') {
           // Jump to the end of line.
           nPos = 0;     // Char analyzed => get next one.
           while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) != '\r' && cCurrentChar != '\n') ;
        }
        // COUNTABLE WORD?
        if (nPos+1 < nTextLength) {
           if ((cCurrentChar = msMovetext.charAt(nPos+1)) >= 'a' && cCurrentChar < 'i') {
              nWordCount++;
              nPos++;     // Char analyzed => get next one.
           }
           else if (cCurrentChar == 'N' || cCurrentChar == 'B' || cCurrentChar == 'R' || cCurrentChar == 'Q' || cCurrentChar == 'K' || cCurrentChar == 'O') {
              nWordCount++;
              nPos++;     // Char analyzed => get next one.
           }
        }


        // BROWSE TEXT.
        while (++nPos < nTextLength) {

           // LOAD NEXT CHAR.
           cCurrentChar = msMovetext.charAt(nPos);

           // EXAMINE CHAR.
           if (cCurrentChar == ' ') {
              // Countable word follows?
              if (nPos+1 < nTextLength) {
                 if ((cCurrentChar = msMovetext.charAt(nPos + 1)) >= 'a' && cCurrentChar < 'i') {
                    nWordCount++;
                    nPos++;  // Char analyzed => get to next one.
                 }
                 else if (cCurrentChar == 'N' || cCurrentChar == 'B' || cCurrentChar == 'R' || cCurrentChar == 'Q' || cCurrentChar == 'K' || cCurrentChar == 'O') {
                    nWordCount++;
                    nPos++;  // Char analyzed => get to next one.
                 }
              }
           }
           else if (cCurrentChar == '.') {
              if (nPos + 1 < nTextLength) {
                 if ((cCurrentChar = msMovetext.charAt(nPos + 1)) >= 'a' && cCurrentChar < 'i') {
                    nWordCount++;
                    nPos++;  // Char analyzed => get next one.
                 }
                 else if (cCurrentChar == 'N' || cCurrentChar == 'B' || cCurrentChar == 'R' || cCurrentChar == 'Q' || cCurrentChar == 'K' || cCurrentChar == 'O') {
                    nWordCount++;
                    nPos++;  // Char analyzed => get next one.
                 }
              }
           }
           else if (cCurrentChar == '\n' || cCurrentChar == '\r') {
              if (nPos + 1 < nTextLength && msMovetext.charAt(nPos + 1) == '%') {
                 // Jump to the end of line.
                 nPos++;
                 while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) != '\r' && cCurrentChar != '\n') ;
              }
              if (nPos + 1 < nTextLength) {
                 if ((cCurrentChar = msMovetext.charAt(nPos + 1)) >= 'a' && cCurrentChar < 'i') {
                    nWordCount++;
                 }
                 else if (cCurrentChar == 'N' || cCurrentChar == 'B' || cCurrentChar == 'R' || cCurrentChar == 'Q' || cCurrentChar == 'K' || cCurrentChar == 'O') {
                    nWordCount++;
                 }
              }
           }
           else if (cCurrentChar == '(') {
              nRoundBracketOpen++;
              while (++nPos < nTextLength && nRoundBracketOpen != 0) {
                 if ((cCurrentChar = msMovetext.charAt(nPos)) == ')') {
                    nRoundBracketOpen--;
                 }
                 else if (cCurrentChar == '(') {
                    nRoundBracketOpen++;
                 }
                 else if (cCurrentChar == '{') {
                    // Jump to end of comment.
                    while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) != '}') {
                       // Escape lines beginning with '%'.
                       if (cCurrentChar == '%' && (nPos == 0 || msMovetext.charAt(nPos - 1) == '\r' || msMovetext.charAt(nPos - 1) == '\n')) {
                          while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) != '\r' && cCurrentChar != '\n') ;
                       }
                    }
                 }
                 else if (cCurrentChar == ';') {
                    // Jump to the end of line.
                    while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) != '\r' && cCurrentChar != '\n') ;
                 }
              }
              if (nPos + 1 < nTextLength) {
                 if ((cCurrentChar = msMovetext.charAt(nPos + 1)) >= 'a' && cCurrentChar < 'i') {
                    nWordCount++;
                    nPos++;     // Char analyzed => get next one.
                 }
                 else if (cCurrentChar == 'N' || cCurrentChar == 'B' || cCurrentChar == 'R' || cCurrentChar == 'Q' || cCurrentChar == 'K' || cCurrentChar == 'O') {
                    nWordCount++;
                    nPos++;     // Char analyzed => get next one.
                 }
              }
           }
           else if (cCurrentChar == '{') {
              // Jump to end of comment.
              while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) != '}') {
                 // Escape lines beginning with '%'.
                 if (cCurrentChar == '%' && (nPos == 0 || msMovetext.charAt(nPos - 1) == '\r' || msMovetext.charAt(nPos - 1) == '\n')) {
                    while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) != '\r' && cCurrentChar != '\n') ;
                 }
              }
              if (nPos + 1 < nTextLength) {
                 if ((cCurrentChar = msMovetext.charAt(nPos + 1)) >= 'a' && cCurrentChar < 'i') {
                    nWordCount++;
                    nPos++;     // Char analyzed => get next one.
                 }
                 else if (cCurrentChar == 'N' || cCurrentChar == 'B' || cCurrentChar == 'R' || cCurrentChar == 'Q' || cCurrentChar == 'K' || cCurrentChar == 'O') {
                    nWordCount++;
                    nPos++;     // Char analyzed => get next one.
                 }
              }
           }
           else if (cCurrentChar == ';') {
              // Jump to the end of line.
              while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) != '\r' && cCurrentChar != '\n') ;
              if (nPos + 1 < nTextLength) {
                 if ((cCurrentChar = msMovetext.charAt(nPos + 1)) >= 'a' && cCurrentChar < 'i') {
                    nWordCount++;
                    nPos++;     // Char analyzed => get next one.
                 }
                 else if (cCurrentChar == 'N' || cCurrentChar == 'B' || cCurrentChar == 'R' || cCurrentChar == 'Q' || cCurrentChar == 'K' || cCurrentChar == 'O') {
                    nWordCount++;
                    nPos++;     // Char analyzed => get next one.
                 }
              }
           }
        }


        // RETURN VALUE.
        if (nRoundBracketOpen != 0) {
           nWordCount = -1;
        }
        return nWordCount;
     }
    
    
    
    
    private int computePlyCount() {
        int nPlyCount = 0;
        int nTextLength = msMovetext.length();
        char cCurrentChar;
        int nRoundBracketOpen = 0;
        int nPos = -1;


        // DISCARD FIRST LINE IF BEGINNING WITH '%'.
        if (nTextLength > 0 && msMovetext.charAt(0) == '%') {
           // Jump to the end of line.
           nPos = 0;     // Char analyzed => get next one.
           while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) != '\r' && cCurrentChar != '\n') ;
        }
        // COUNTABLE WORD?
        if (nPos + 1 < nTextLength) {
           if ((cCurrentChar = msMovetext.charAt(nPos + 1)) < 'S' && cCurrentChar > 'A') {
              nPlyCount++;
              // As 'O' is an exception, it is not dealed with.
              if (++nPos + 1 < nTextLength && msMovetext.charAt(nPos + 1) == 'x') {
                 nPos++;
              }
              if (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) < 'i' && cCurrentChar >= 'a') {
                 if (++nPos == nTextLength || (cCurrentChar = msMovetext.charAt(nPos)) > '8' || cCurrentChar < '1') {
                    nPos--;
                 }
              }
              else {
                 nPos--;
              }
           }
           else if (cCurrentChar < 'i' && cCurrentChar >= 'a') {
              nPlyCount++;
              if (++nPos + 1 < nTextLength && (cCurrentChar = msMovetext.charAt(nPos + 1)) > '0' && cCurrentChar < '9') {
                 nPos++;        // Char analyzed => get to next one.
              }
           }
        }


        // BROWSE TEXT.
        while (++nPos < nTextLength) {

           // LOAD NEXT CHAR.
           cCurrentChar = msMovetext.charAt(nPos);

           // EXAMINE CHAR.
           if (cCurrentChar == ' ') {
              // Does a countable word follow this ' '?
              if (nPos + 1 < nTextLength) {
                 if ((cCurrentChar = msMovetext.charAt(nPos + 1)) < 'S' && cCurrentChar > 'A') {  // As 'O' is an exception, it is not dealed with.
                    nPlyCount++;
                    // Try to reach word's end.
                    if (++nPos + 1 < nTextLength && msMovetext.charAt(nPos + 1) == 'x') {
                       nPos++;
                    }
                    if (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) < 'i' && cCurrentChar >= 'a') {
                       if (++nPos == nTextLength || (cCurrentChar = msMovetext.charAt(nPos)) > '8' || cCurrentChar < '1') {
                          nPos--;
                       }
                    }
                    else {
                       nPos--;
                    }
                 }
                 else if (cCurrentChar < 'i' && cCurrentChar >= 'a') {
                    nPlyCount++;
                    // Try to reach word's end.
                    if (++nPos + 1 < nTextLength && (cCurrentChar = msMovetext.charAt(nPos + 1)) > '0' && cCurrentChar < '9') {
                       nPos++;        // Char analyzed => get to next one.
                    }
                 }
              }
           }
           else if (cCurrentChar == '.') {  // No special meaning but handled here because it occurs often.
              if (nPos + 1 < nTextLength && (cCurrentChar = msMovetext.charAt(nPos + 1)) != ' ') {
                 if (cCurrentChar < 'S' && cCurrentChar > 'A') {
                    nPlyCount++;
                    if (++nPos + 1 < nTextLength && msMovetext.charAt(nPos + 1) == 'x') {
                       nPos++;
                    }
                    if (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) < 'i' && cCurrentChar >= 'a') {
                       if (++nPos == nTextLength || (cCurrentChar = msMovetext.charAt(nPos)) > '8' || cCurrentChar < '1') {
                          nPos--;
                       }
                    }
                    else {
                       nPos--;
                    }
                 }
                 else if (cCurrentChar < 'i' && cCurrentChar >= 'a') {
                    nPlyCount++;
                    if (++nPos + 1 < nTextLength && (cCurrentChar = msMovetext.charAt(nPos + 1)) > '0' && cCurrentChar < '9') {
                       nPos++;        // Char analyzed => get to next one.
                    }
                 }
              }
           }
           else if (cCurrentChar == '\n' || cCurrentChar == '\r') { // End of line.
              // Get rid of whole the line comment.
              if (nPos + 1 < nTextLength && msMovetext.charAt(nPos + 1) == '%') {
                 nPos++;
                 while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) != '\r' && cCurrentChar != '\n') ;
              }
              // Countable word?
              if (nPos + 1 < nTextLength) {
                 if ((cCurrentChar = msMovetext.charAt(nPos + 1)) < 'S' && cCurrentChar > 'A') {
                    nPlyCount++;
                    if (++nPos + 1 < nTextLength && msMovetext.charAt(nPos + 1) == 'x') {
                       nPos++;
                    }
                    if (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) < 'i' && cCurrentChar >= 'a') {
                       if (++nPos == nTextLength || (cCurrentChar = msMovetext.charAt(nPos)) > '8' || cCurrentChar < '1') {
                          nPos--;
                       }
                    }
                    else {
                       nPos--;
                    }
                 }
                 else if (cCurrentChar < 'i' && cCurrentChar >= 'a') {
                    nPlyCount++;
                    if (++nPos + 1 < nTextLength && (cCurrentChar = msMovetext.charAt(nPos + 1)) > '0' && cCurrentChar < '9') {
                       nPos++;        // Char analyzed => get to next one.
                    }
                 }
              }
           }
           else if (cCurrentChar == '(') {  //
              nRoundBracketOpen++;
              // Jump to last ')'.
              while (++nPos < nTextLength && nRoundBracketOpen != 0) {
                 if ((cCurrentChar = msMovetext.charAt(nPos)) == ')') {
                    nRoundBracketOpen--;
                 }
                 else if (cCurrentChar == '(') {
                    nRoundBracketOpen++;
                 }
                 // Get rid of multiline comment.
                 else if (cCurrentChar == '{') {
                    // Jump to end of comment.
                    while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) != '}') {
                       // Escape lines beginning with '%'.
                       if (cCurrentChar == '%' && (nPos == 0 || msMovetext.charAt(nPos - 1) == '\r' || msMovetext.charAt(nPos - 1) == '\n')) {
                          while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) != '\r' && cCurrentChar != '\n') ;
                       }
                    }
                 }
                 // Get rid of end of line comment.
                 else if (cCurrentChar == ';') {
                    while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) != '\r' && cCurrentChar != '\n') ;
                 }
              }
              // Last ')' reached: countable word?
              if (nPos + 1 < nTextLength && (cCurrentChar = msMovetext.charAt(nPos + 1)) != ' ') {
                 if (cCurrentChar < 'S' && cCurrentChar > 'A') {
                    nPlyCount++;
                    if (++nPos + 1 < nTextLength && msMovetext.charAt(nPos + 1) == 'x') {
                       nPos++;
                    }
                    if (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) < 'i' && cCurrentChar >= 'a') {
                       if (++nPos == nTextLength || (cCurrentChar = msMovetext.charAt(nPos)) > '8' || cCurrentChar < '1') {
                          nPos--;
                       }
                    }
                    else {
                       nPos--;
                    }
                 }
                 else if (cCurrentChar < 'i' && cCurrentChar >= 'a') {
                    nPlyCount++;
                    if (++nPos + 1 < nTextLength && (cCurrentChar = msMovetext.charAt(nPos + 1)) > '0' && cCurrentChar < '9') {
                       nPos++;        // Char analyzed => get to next one.
                    }
                 }
              }
           }
           else if (cCurrentChar == '{') {  // Multiline comment.
              // Jump to end of comment.
              while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) != '}') {
                 // Escape lines beginning with '%'.
                 if (cCurrentChar == '%' && (nPos == 0 || msMovetext.charAt(nPos - 1) == '\r' || msMovetext.charAt(nPos - 1) == '\n')) {
                    while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) != '\r' && cCurrentChar != '\n') ;
                 }
              }
              // End of comment reached: countable word?
              if (nPos + 1 < nTextLength && (cCurrentChar = msMovetext.charAt(nPos + 1)) != ' ') {
                 if (cCurrentChar < 'S' && cCurrentChar > 'A') {
                    nPlyCount++;
                    if (++nPos + 1 < nTextLength && msMovetext.charAt(nPos + 1) == 'x') {
                       nPos++;
                    }
                    if (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) < 'i' && cCurrentChar >= 'a') {
                       if (++nPos == nTextLength || (cCurrentChar = msMovetext.charAt(nPos)) > '8' || cCurrentChar < '1') {
                          nPos--;
                       }
                    }
                    else {
                       nPos--;
                    }
                 }
                 else if (cCurrentChar < 'i' && cCurrentChar >= 'a') {
                    nPlyCount++;
                    if (++nPos + 1 < nTextLength && (cCurrentChar = msMovetext.charAt(nPos + 1)) > '0' && cCurrentChar < '9') {
                       nPos++;        // Char analyzed => get to next one.
                    }
                 }
              }
           }
           else if (cCurrentChar == ';') {  // Rest of line comment.
              // Jump to the end of line.
              while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) != '\r' && cCurrentChar != '\n') ;
              // End of line reached: countable word?
              if (nPos + 1 < nTextLength) {
                 if ((cCurrentChar = msMovetext.charAt(nPos + 1)) < 'S' && cCurrentChar > 'A') {
                    nPlyCount++;
                    if (++nPos + 1 < nTextLength && msMovetext.charAt(nPos + 1) == 'x') {
                       nPos++;
                    }
                    if (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) < 'i' && cCurrentChar >= 'a') {
                       if (++nPos == nTextLength || (cCurrentChar = msMovetext.charAt(nPos)) > '8' || cCurrentChar < '1') {
                          nPos--;
                       }
                    }
                    else {
                       nPos--;
                    }
                 }
                 else if (cCurrentChar < 'i' && cCurrentChar >= 'a') {
                    nPlyCount++;
                    if (++nPos + 1 < nTextLength && (cCurrentChar = msMovetext.charAt(nPos + 1)) > '0' && cCurrentChar < '9') {
                       nPos++;        // Char analyzed => get to next one.
                    }
                 }
              }
           }
        }


        // RETURN VALUE.
        if (nRoundBracketOpen != 0) {
           nPlyCount = -1;
        }
        return nPlyCount;
     }
    
    
    
    private String exportToLaTeX() {
        int nTextLength = msMovetext.length();
        int nRoundBracketOpen = 0;          // Round brackets can be nested.
        int nNAGNumber;
        char cCurrentChar;
        String[] asNAGText = {"", "!", "?", "!!", "??", "!?", "?!",
							   "forced move",
							   "singular move",
							   "worst move",
							   "drawish position",
							   "equal chances, quiet position",
							   "equal chances, active position",
							   "unclear position",
							   "White has a slight advantage",
							   "Black has a slight advantage",
							   "White has a moderate advantage",
							   "Black has a moderate advantage",
							   "White has a decisive advantage",
							   "Black has a decisive advantage",
							   "White has a crushing advantage",
							   "Black has a crushing advantage",
							   "White is in zugzwang",
							   "Black is in zugzwang",
							   "White has a slight space advantage",
							   "Black has a slight space advantage",
							   "White has a moderate space advantage",
							   "Black has a moderate space advantage",
							   "White has a decisive space advantage",
							   "Black has a decisive space advantage",
							   "White has a slight time (development) advantage",
							   "Black has a slight time (development) advantage",
							   "White has a moderate time (development) advantage",
							   "Black has a moderate time (development) advantage",
							   "White has a decisive time (development) advantage",
							   "Black has a decisive time (development) advantage",
							   "White has the initiative",
							   "Black has the initiative",
							   "White has a lasting initiative",
							   "Black has a lasting initiative",
							   "White has the attack",
							   "Black has the attack",
							   "White has insufficient compensation for material deficit",
							   "Black has insufficient compensation for material deficit",
							   "White has sufficient compensation for material deficit",
							   "Black has sufficient compensation for material deficit",
							   "White has more than adequate compensation for material deficit",
							   "Black has more than adequate compensation for material deficit",
							   "White has a slight center control advantage",
							   "Black has a slight center control advantage",
							   "White has a moderate center control advantage",
							   "Black has a moderate center control advantage",
							   "White has a decisive center control advantage",
							   "Black has a decisive center control advantage",
							   "White has a slight kingside control advantage",
							   "Black has a slight kingside control advantage",
							   "White has a moderate kingside control advantage",
							   "Black has a moderate kingside control advantage",
							   "White has a decisive kingside control advantage",
							   "Black has a decisive kingside control advantage",
							   "White has a slight queenside control advantage",
							   "Black has a slight queenside control advantage",
							   "White has a moderate queenside control advantage",
							   "Black has a moderate queenside control advantage",
							   "White has a decisive queenside control advantage",
							   "Black has a decisive queenside control advantage",
							   "White has a vulnerable first rank",
							   "Black has a vulnerable first rank",
							   "White has a well protected first rank",
							   "Black has a well protected first rank",
							   "White has a poorly protected king",
							   "Black has a poorly protected king",
							   "White has a well protected king",
							   "Black has a well protected king",
							   "White has a poorly placed king",
							   "Black has a poorly placed king",
							   "White has a well placed king",
							   "Black has a well placed king",
							   "White has a very weak pawn structure",
							   "Black has a very weak pawn structure",
							   "White has a moderately weak pawn structure",
							   "Black has a moderately weak pawn structure",
							   "White has a moderately strong pawn structure",
							   "Black has a moderately strong pawn structure",
							   "White has a very strong pawn structure",
							   "Black has a very strong pawn structure",
							   "White has poor knight placement",
							   "Black has poor knight placement",
							   "White has good knight placement",
							   "Black has good knight placement",
							   "White has poor bishop placement",
							   "Black has poor bishop placement",
							   "White has good bishop placement",
							   "Black has good bishop placement",
							   "White has poor rook placement",
							   "Black has poor rook placement",
							   "White has good rook placement",
							   "Black has good rook placement",
							   "White has poor queen placement",
							   "Black has poor queen placement",
							   "White has good queen placement",
							   "Black has good queen placement",
							   "White has poor piece coordination",
							   "Black has poor piece coordination",
							   "White has good piece coordination",
							   "Black has good piece coordination",
							   "White has played the opening very poorly",
							   "Black has played the opening very poorly",
							   "White has played the opening poorly",
							   "Black has played the opening poorly",
							   "White has played the opening well",
							   "Black has played the opening well",
							   "White has played the opening very well",
							   "Black has played the opening very well",
							   "White has played the middlegame very poorly",
							   "Black has played the middlegame very poorly",
							   "White has played the middlegame poorly",
							   "Black has played the middlegame poorly",
							   "White has played the middlegame well",
							   "Black has played the middlegame well",
							   "White has played the middlegame very well",
							   "Black has played the middlegame very well",
							   "White has played the ending very poorly",
							   "Black has played the ending very poorly",
							   "White has played the ending poorly",
							   "Black has played the ending poorly",
							   "White has played the ending well",
							   "Black has played the ending well",
							   "White has played the ending very well",
							   "Black has played the ending very well",
							   "White has slight counterplay",
							   "Black has slight counterplay",
							   "White has moderate counterplay",
							   "Black has moderate counterplay",
							   "White has decisive counterplay",
							   "Black has decisive counterplay",
							   "White has moderate time control pressure",
							   "Black has moderate time control pressure",
							   "White has severe time control pressure",
							   "Black has severe time control pressure"							   
		};
        // TODO: to improve NAG implementation.
        int nConversionLength = asNAGText.length;

        
        StringBuilder sbReturnValue = new StringBuilder();
        sbReturnValue.append("\\documentclass[10pt,twocolumn]{article}");
        sbReturnValue.append(END_OF_LINE);
        sbReturnValue.append("\\usepackage[ps]{skak}");
        sbReturnValue.append(END_OF_LINE);
        sbReturnValue.append("\\usepackage{latexsym}");
        sbReturnValue.append(END_OF_LINE);
        sbReturnValue.append("\\usepackage{times}");
        sbReturnValue.append(END_OF_LINE);
        sbReturnValue.append("\\usepackage{a4wide}");
        sbReturnValue.append(END_OF_LINE);
        sbReturnValue.append("\\usepackage[T1]{fontenc}");
        sbReturnValue.append(END_OF_LINE);
        sbReturnValue.append("\\usepackage{amssymb}");
        sbReturnValue.append(END_OF_LINE);
        sbReturnValue.append("\\renewcommand{\\rmdefault}{phv}");
        sbReturnValue.append(END_OF_LINE);
        sbReturnValue.append("\\renewcommand{\\sfdefault}{phv}");
        sbReturnValue.append(END_OF_LINE);
        sbReturnValue.append("\\usepackage[nice]{nicefrac}");
        sbReturnValue.append(END_OF_LINE);
        sbReturnValue.append("\\setlength{\\columnsep}{7mm}");
        sbReturnValue.append(END_OF_LINE);
        sbReturnValue.append("\\setlength{\\parindent}{0pt}");
        sbReturnValue.append(END_OF_LINE);
        sbReturnValue.append("\\setlength{\\columnseprule}{.2pt}");
        sbReturnValue.append(END_OF_LINE);
        sbReturnValue.append("\\begin{document}");
        sbReturnValue.append(END_OF_LINE);
        sbReturnValue.append("\\notationOff");
        sbReturnValue.append(END_OF_LINE);
        sbReturnValue.append("\\textbf{%");
        sbReturnValue.append(END_OF_LINE);
		sbReturnValue.append("$\\square$ ");
		sbReturnValue.append(this.getWhite());
		sbReturnValue.append(" (");
		sbReturnValue.append(this.getWhiteElo());
		sbReturnValue.append(")\\\\");
        sbReturnValue.append(END_OF_LINE);
		sbReturnValue.append("$\\blacksquare$ ");
		sbReturnValue.append(this.getBlack());
		sbReturnValue.append(" (");
		sbReturnValue.append(this.getBlackElo());
		sbReturnValue.append(")\\\\");
        sbReturnValue.append(END_OF_LINE);
		sbReturnValue.append(this.getEvent());
		sbReturnValue.append(' ');
		sbReturnValue.append(this.getDate().substring(0,4));
		sbReturnValue.append(" (");
		sbReturnValue.append(this.getRound());
		sbReturnValue.append(") \\hfill ");
		sbReturnValue.append(this.getECO());
		sbReturnValue.append("\\\\");
        sbReturnValue.append(END_OF_LINE);
        sbReturnValue.append('}');
        sbReturnValue.append(END_OF_LINE);
        sbReturnValue.append(END_OF_LINE);
        sbReturnValue.append("\newgame");
        sbReturnValue.append(END_OF_LINE);
        sbReturnValue.append("\\mainline");

        int nPos = -1;
        while (++nPos < nTextLength) {

           // ESCAPE LINES BEGINNING WITH '%'.
           if ((cCurrentChar = msMovetext.charAt(nPos)) == '%' && (nPos == 0 || msMovetext.charAt(nPos - 1) == '\r' || msMovetext.charAt(nPos - 1) == '\n')) {
              while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) != '\r' && cCurrentChar != '\n') ;
           }

           // CONVERT.
           if (cCurrentChar == '(') {
              if (++nRoundBracketOpen == 1) {
                 sbReturnValue.append("}\\movecomment{");
              }
              else {
                 sbReturnValue.append('(');
              }
           }
           else if (cCurrentChar == ')') {
              if (--nRoundBracketOpen == 0) {
                 sbReturnValue.append("\\mainline{");
              }
              else {
                 sbReturnValue.append(')');
              }
           }
           else if (cCurrentChar == '{') {
              sbReturnValue.append("}");
              // Jump to end of comment.
              while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) != '}') {
                 // Escape lines beginning with '%'.
                 if (cCurrentChar == '%' && (nPos == 0 || msMovetext.charAt(nPos - 1) == '\r' || msMovetext.charAt(nPos - 1) == '\n')) {
                    while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) != '\r' && cCurrentChar != '\n');
                 }
              }
              // Restore previous mode.
              if (nRoundBracketOpen == 0) {
                 sbReturnValue.append("\\mainline{");
              }
              else {
                 sbReturnValue.append("\\movecomment{");
              }
           }
           else if (cCurrentChar == '$') {
              // Capture value.
              nNAGNumber = 0;
              while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) >= '0' && cCurrentChar <= '9') {
                 nNAGNumber *= 10;
                 nNAGNumber += cCurrentChar - '0';
              }
              if (nNAGNumber < nConversionLength) {
                 sbReturnValue.append(asNAGText[nNAGNumber]);
              }
              else {
                 sbReturnValue.append('$');
                 sbReturnValue.append(String.valueOf(nNAGNumber));
              }
           }
           else if (cCurrentChar == ';') {
              // Jump to the end of line.
              while (++nPos < nTextLength && (cCurrentChar = msMovetext.charAt(nPos)) != '\r' && cCurrentChar != '\n') ;
           }
           else {
              sbReturnValue.append(cCurrentChar);
           }
        }


        // RETURN VALUE.
        return sbReturnValue.toString();
     }
    
    /**
     * preconditions: input string is not null,
     *                input string does not end with a blank space,
     *                input string does not end with an end of line marker.
     */
    private String getMovetextResult()  {
    	String sReturnValue = "*"; 
    			
		//  TRIM TRAILING WORD BREAKS (i. e.: EOL and blank spaces chars).
		char cCurrentChar;
		int nPos = msMovetext.length();
		while (--nPos > -1 && (cCurrentChar=msMovetext.charAt(nPos))=='\n' && cCurrentChar=='\r' && cCurrentChar==' ');
		nPos++;
		
		// SEARCH FOR THE LAST WORD BREAK.
		while (--nPos > -1 && (cCurrentChar = msMovetext.charAt(nPos))!=' ' && cCurrentChar!='\n' && cCurrentChar!='\r');
		
		// GET END OF LINE FROM LAST WORD BREAK.
		if (nPos > -1) {
			sReturnValue = msMovetext.substring(nPos + 1); // nPos+1 exists since string cannot end with a word the word break (hence the initial trimming).
		}
		else {
			sReturnValue = msMovetext;
		}
		
		return sReturnValue;
	}
    
    /**
     * Movetext can be split into several lines. End of line (EOL) chars
     * delimits them.
     * When appending the result, we must examine if there is enough room in
     * the previous line, when this one exist.
     **/
     private void setMovetextResult(String sResult) {

         final int BLANK_SPACE_LENGTH = 1;
         final int PGN_STANDARD_LINE_WIDTH_PLUS_ONE = 76;
         
         int nTextLength = msMovetext.length();

            
         assert nTextLength!=0 : "setMovetextResult(): msMovetext can never be empty.";
         assert msMovetext.charAt(nTextLength-1)!='\n' && msMovetext.charAt(nTextLength)!='\r' : "setMovetextResult(): msMovetext cannot end with an EOL character.";
         assert msMovetext.charAt(nTextLength-1)!=' ' : "setMovetextResult(): msMovetext cannot end with a blank space character.";
         assert sResult.charAt(0)!=' ' : "setMovetextResult(): sResult cannot begin with a blank space character.";
         assert sResult.charAt(sResult.length()-1)!=' ' : "setMovetextResult(): sResult cannot end with a blank space character.";

         
         // TRIMMING TRAILING EOL CHARS.
         char cLastChar;
         if (nTextLength > 0) {
	         while ((cLastChar=msMovetext.charAt(nTextLength-1))=='\n' || cLastChar=='\r' || cLastChar==' ') {
	        	 nTextLength--;
	         }
         }
         
	     // FIND THE LAST END OF LINE.
	     char cCurrentChar;
	     int nBeginOfLastEOL = -1;  // In most cases, "end of line" is made of two characters: "CR" + "LF".
	     int nEndOfLastEOL = -1;
	     int nPos = nTextLength;
	     while (nEndOfLastEOL < --nPos) {
	        if ((cCurrentChar = msMovetext.charAt(nPos)) == '\n' || cCurrentChar == '\r') {
	           nEndOfLastEOL = nPos;
	           if (nPos > 0 && ((cCurrentChar = msMovetext.charAt(--nPos)) == '\r' || cCurrentChar == '\n')) {
	              nBeginOfLastEOL = nPos;
	           }
	           else {
	              nBeginOfLastEOL = nEndOfLastEOL;
	           }
	        }
	     }

            
	     // FIND THE PREVIOUS END OF LINE.
	     int nEndOfPreviousEOL = -1;
	     if (nBeginOfLastEOL != -1) {
	        while (nEndOfPreviousEOL < --nPos) {
	           if ((cCurrentChar = msMovetext.charAt(nPos)) == '\n' || cCurrentChar == '\r') {
	              nEndOfPreviousEOL = nPos;
	           }
	        }
	     }

            
	     // FIND THE LAST BLANK SPACE OF LAST LINE.
	     int nLastBlankSpace = nEndOfLastEOL;    // If there is no last EOL, nEndOfLastEOL == -1.
	     nPos = nTextLength;
	     while (nLastBlankSpace < --nPos) {
	        if (msMovetext.charAt(nPos) == ' ') {
	           nLastBlankSpace = nPos;
	        }
	     }       // If no blank space is found, before last EOL, nLastBlankSpace == nEndOfLastEOL.

            
		 // REPLACE THE LAST WORD.
		if (nEndOfLastEOL==-1) {        // Movetext contains only one line.
			if (nLastBlankSpace == nEndOfLastEOL) { // Movetext consists in only one word (no blank space found).
				msMovetext = sResult;
			}
			else {                                                                       // Movetext contains several words.
				if (nLastBlankSpace + BLANK_SPACE_LENGTH + sResult.length() < PGN_STANDARD_LINE_WIDTH_PLUS_ONE) {       // Enough room to place result?
					msMovetext = msMovetext.substring(0, nLastBlankSpace) + " " + sResult;
				}
				else {
					msMovetext = msMovetext.substring(0, nLastBlankSpace) + END_OF_LINE + sResult;
				}
			}
		}
		else {                                          // Move text contains several lines.
			if (nLastBlankSpace == nEndOfLastEOL) { // Last line contains only one word (no blank space found).
				if (nBeginOfLastEOL - nEndOfPreviousEOL + BLANK_SPACE_LENGTH + sResult.length() < PGN_STANDARD_LINE_WIDTH_PLUS_ONE) {    // Enough room at the last line but one?
					msMovetext = msMovetext.substring(0, nBeginOfLastEOL) + " " + sResult;
				}
				else {
					msMovetext = msMovetext.substring(0, nBeginOfLastEOL) + END_OF_LINE + sResult;
				}
			}
			else {                                                                  // Last line containts several words.
				if (nLastBlankSpace - nEndOfLastEOL + BLANK_SPACE_LENGTH + sResult.length() < PGN_STANDARD_LINE_WIDTH_PLUS_ONE) {               // Enough room at the last line?
					msMovetext = msMovetext.substring(0, nLastBlankSpace) + " " + sResult;
				}
				else {
					msMovetext = msMovetext.substring(0, nLastBlankSpace) + END_OF_LINE + sResult;
				}
			}
		}
	}
     
    /**
     * This method is paired with setResult().
     * @param sMovetextSection
     */
	public boolean setMovetext(String sMovetextSection) {
		boolean bValidValue = true;
		
		msMovetext = sMovetextSection;
		
		String sMovetextResult = this.getMovetextResult();
//		String sMovetextResult = this.getResult();
	        
//        assert msMovetext.charAt(nTextLength-1)!='\n' && msMovetext.charAt(nTextLength)!='\r' : "setMovetextResult(): msMovetext cannot end with an EOL character.";
		// assert: Result of 7TR is already set.
		
		if (!sMovetextResult.equals(this.getResult())) {
//        if (sMovetextResult.equals("1-0") || sMovetextResult.equals("1/2-1/2") || sMovetextResult.equals("0-1") || sMovetextResult.equals("*")) {
//        	if (!sMovetextResult.equals(msResult)) {
//        		if (msResult.equals("*")) {
//                    msResult=sMovetextResult;
//            		setMovetextResult(sMovetextResult);
//        		}
//        		else {
//	        		msResult="*";
//	        		setMovetextResult("*");
//        		}
//        	}
			
			bValidValue=false;
        }
//        else {
//    		msResult="*";
//    		setMovetextResult("*");
//        }
		
		// TODO: reinforce PlyCount computation (NAGs, comments, ...).
//		msPlyCount=String.valueOf((msMovetext.split("[ \n]").length-1)*2/3);	// Avoid recomputing it on each query.
		msPlyCount=String.valueOf(computePlyCount());	// Avoid recomputing it on each query.
		
		return bValidValue;
	}

	public String getMovetext() {
		// TODO: to return concatenation of msMovetext and msPlyCount.
    	return msMovetext;
	}
    public void reset() {
//        msEvent="";
//        msSite="";
//        msDate="";
//        msRound="";
//        msWhite="";
//        msBlack="";
//        msResult="";
//        msAnnotator="";
//        msBlackNA=""; 
//        msBlackElo="";
//        msBlackTitle="";
//        msBlackType=""; 
//        msBoard="";
//        msECO="";
//        msEventDate=""; 
//        msEventSponsor=""; 
//        msFEN="";
//        msMode=""; 
//        msNIC="";
//        msOpening="";
//        msPlyCount="0"; 
//        msSection=""; 
//        msSetUp="";
//        msStage=""; 
//        msSubVariation="";
//        msTermination=""; 
//        msTime=""; 
//        msTimeControl="";
//        msUTCDate=""; 
//        msUTCTime=""; 
//        msVariation="";
//        msWhiteElo="";
//        msWhiteNA=""; 
//        msWhiteTitle="";
//        msWhiteType=""; 
//        msMoveSection="";

        msEvent="?";
        msSite="?";
        msDate="????.??.??";
        msRound="?";
        msWhite="?";
        msBlack="?";
        msResult="*";
        msAnnotator="?";
        msBlackElo="?";
        msBlackNA="?"; 
        msBlackTitle="?";
        msBlackType="?"; 
        msBoard="?";
        msECO="?";
        msEventDate="????.??.??"; 
        msEventSponsor="?"; 
        msFEN="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        msMode="?"; 
//        msMoveSection="*";
        msNIC="?";
        msOpening="?";
//        msPlyCount="0"; 
        msSection="?"; 
        msSetUp="0";
        msStage="?"; 
        msSubVariation="?";
        msTermination="normal"; 
        msTime="??:??:??"; 
        msTimeControl="?";
        msUTCDate="????.??.??"; 
        msUTCTime="??:??:??"; 
        msVariation="?";
        msWhiteElo="?";
        msWhiteNA="?"; 
        msWhiteTitle="?";
        msWhiteType="?";
}
    // TODO: to think to a toStringToExport, to select specific tags to pass to PGNStream.writePGNFile().
    @Override public String toString() {
    	final String EOL = System.getProperty("line.separator");
    	final String END_OF_TAG =  "\"]" + EOL;
    	
        StringBuilder sbResult = new StringBuilder();

        sbResult.append(this.getClass().getName() + " Object {" + EOL);
        sbResult.append("[Event \"");
        sbResult.append(this.getEvent());
        sbResult.append(END_OF_TAG);
        sbResult.append("[Site \"");
        sbResult.append(this.getSite());
        sbResult.append(END_OF_TAG);
        sbResult.append("[Date \"");
        sbResult.append(this.getDate());
        sbResult.append(END_OF_TAG);
        sbResult.append("[Round \"");
        sbResult.append(this.getRound());
        sbResult.append(END_OF_TAG);
        sbResult.append("[White \"");
        sbResult.append(this.getWhite());
        sbResult.append(END_OF_TAG);
        sbResult.append("[Black \"");
        sbResult.append(this.getBlack());
        sbResult.append(END_OF_TAG);
        sbResult.append("[Result \"");
        sbResult.append(this.getResult());
        sbResult.append(END_OF_TAG);
        sbResult.append("[Annotator \"");
        sbResult.append(this.getAnnotator());
        sbResult.append(END_OF_TAG);
        sbResult.append("[BlackElo \"");
        sbResult.append(this.getBlackElo());
        sbResult.append(END_OF_TAG);
        sbResult.append("[BlackNA \"");
        sbResult.append(this.getBlackNA());
        sbResult.append(END_OF_TAG);
        sbResult.append("[BlackTitle \"");
        sbResult.append(this.getBlackTitle());
        sbResult.append(END_OF_TAG);
        sbResult.append("[BlackType \"");
        sbResult.append(this.getBlackType());
        sbResult.append(END_OF_TAG);
        sbResult.append("[Board \"");
        sbResult.append(this.getBoard());
        sbResult.append(END_OF_TAG);
        sbResult.append("[ECO \"");
        sbResult.append(this.getECO());
        sbResult.append(END_OF_TAG);
        sbResult.append("[EventDate \"");
        sbResult.append(this.getEventDate());
        sbResult.append(END_OF_TAG);
        sbResult.append("[EventSponsor \"");
        sbResult.append(this.getEventSponsor());
        sbResult.append(END_OF_TAG);
        sbResult.append("[FEN \"");
        sbResult.append(this.getFEN());
        sbResult.append(END_OF_TAG);
        sbResult.append("[Mode \"");
        sbResult.append(this.getMode());
        sbResult.append(END_OF_TAG);
        sbResult.append("[NIC \"");
        sbResult.append(this.getNIC());
        sbResult.append(END_OF_TAG);
        sbResult.append("[Opening \"");
        sbResult.append(this.getOpening());
        sbResult.append(END_OF_TAG);
        sbResult.append("[PlyCount \"");
        sbResult.append(this.getPlyCount());
        sbResult.append(END_OF_TAG);
        sbResult.append("[Section \"");
        sbResult.append(this.getSection());
        sbResult.append(END_OF_TAG);
        sbResult.append("[SetUp \"");
        sbResult.append(this.getSetUp());
        sbResult.append(END_OF_TAG);
        sbResult.append("[Stage \"");
        sbResult.append(this.getStage());
        sbResult.append(END_OF_TAG);
        sbResult.append("[SubVariation \"");
        sbResult.append(this.getSubVariation());
        sbResult.append(END_OF_TAG);
        sbResult.append("[Termination \"");
        sbResult.append(this.getTermination());
        sbResult.append(END_OF_TAG);
        sbResult.append("[Time \"");
        sbResult.append(this.getTime());
        sbResult.append(END_OF_TAG);
        sbResult.append("[TimeControl \"");
        sbResult.append(this.getTimeControl());
        sbResult.append(END_OF_TAG);
        sbResult.append("[UTCDate \"");
        sbResult.append(this.getUTCDate());
        sbResult.append(END_OF_TAG);
        sbResult.append("[UTCTime \"");
        sbResult.append(this.getUTCTime());
        sbResult.append(END_OF_TAG);
        sbResult.append("[Variation \"");
        sbResult.append(this.getVariation());
        sbResult.append(END_OF_TAG);
        sbResult.append("[WhiteElo \"");
        sbResult.append(this.getWhiteElo());
        sbResult.append(END_OF_TAG);
        sbResult.append("[WhiteNA \"");
        sbResult.append(this.getWhiteNA());
        sbResult.append(END_OF_TAG);
        sbResult.append("[WhiteTitle \"");
        sbResult.append(this.getWhiteTitle());
        sbResult.append(END_OF_TAG);
        sbResult.append("[WhiteType \"");
        sbResult.append(this.getWhiteType());
        sbResult.append(END_OF_TAG);
        sbResult.append(EOL);
        sbResult.append(this.getMovetext());
        sbResult.append(EOL + "}");

        return sbResult.toString();
      }
    public boolean addCustomTagPair(String sName, String sValue) {
    	return mmapCustomTagPair.put(sName, sValue)==null;
    } 
    public boolean modifyCustomTagPair(String sName, String sValue) {
    	return mmapCustomTagPair.put(sName, sValue)!=null;
    } 
    public TreeMap<String, String> getAllCustomTagPairs() {
    	return mmapCustomTagPair;
    }
    public String getCustomTagPair(String sTagName){
    	return mmapCustomTagPair.get(sTagName);
    }
    public void finalize() {
    	mmapCustomTagPair.clear();
    	mmapCustomTagPair = null;
    }
}
