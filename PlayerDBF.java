package org.chess.pgn;

import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;
//import org.chess.pgn.ChessGame;

/*  TO DO :
    - to create array items by group (instead of one by one),       <-- !!!
    - to check "Interclubs francophones" the same way as "Interclubs nationaux",
    - to correctly use "", "?" , "-" and "...",                     <-- !!!,
    - when read and computed values do not match (e.g. WhiteFRBEKBSB), to keep only the read value, <-- !!!
    - to valid February 29 of leap years,
    - to optimize removeISOLatin1() function,
    - to replace sLineBeginning with several String.beginsWith(),
    - to make file paths system independent,
    - to rename playerRecord.java to FRBERecord.java,
    - to improve date checking,
    - to see if chessGame member string should be initialized to null or to "",
    - to examine putting constants into the function main(),
    - to add a number to the "UNKNOWN" column name,
    - to see if "Math.max()" could be simplified in "max()",
    - to improve XML reading (i.e. city file).
*/

// TO DO : to improve length() method in FIDEColumn class. 





public class PlayerDBF {
    private final char NULL_CHAR='\u0000';
    private final String END_OF_LINE="\r\n";
    private final String CLASS_FORNAME =  "com.sqlmagic.tinysql.dbfFileDriver";
    private final String FIDE_RATING_LIST = "may12frl.txt";
    
    private OLD_FIDERecord[] mafrFIDERecord = null;
    
    private String removeISOLatin1(String sSentence) {
    	
    	final char[] acUnicode={'À','Á','Â','Ã','Ä','Å','Æ','Ç','È','É','Ê','Ë','Ì','Í','Î','Ï','Ð','Ñ','Ò','Ó','Ô','Õ','Ö','Ø','Ù','Ú','Û','Ü','Ý','à','á','â','ã','ä','å','æ','ç','è','é','ê','ë','ì','í','î','ï','ð','ñ','ò','ó','ô','õ','ö','ø','ù','ú','û','ü','ý','ÿ'};
    	final int[] anISOCode= {192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,216,217,218,219,220,221,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,248,249,250,251,252,253,255};
   	
        char cCurrentChar;
        int nSearchResult;
        final int nSentenceLength=sSentence.length();
        StringBuffer sbReturnValue=new StringBuffer(nSentenceLength);

        for (int nI=-1; ++nI<nSentenceLength;) {
            if ((cCurrentChar=sSentence.charAt(nI))=='&' && sSentence.length()>nI+4 && sSentence.substring(nI,nI+6).matches("&#\\d\\d\\d;") && (nSearchResult=Arrays.binarySearch(anISOCode,Integer.parseInt(sSentence.substring(nI+2,nI+5))))>-1) {
                    sbReturnValue.append(acUnicode[nSearchResult]);
                    nI+=5;
            }
            else {
                sbReturnValue.append(cCurrentChar);
            }
        } 
        return sbReturnValue.toString();
    }

    private String[] retrieveCities(String sZipCodeFile) {
        String sLine = null;
        String[] asZipCode=null;
       
        try {
            BufferedReader brZipCode=new BufferedReader(new FileReader(sZipCodeFile));
            while ((sLine=brZipCode.readLine()) != null) {
                sLine=sLine.trim();
                if (sLine.startsWith("<Name>") && sLine.endsWith("</Name>"))
                {
                    sLine=sLine.substring(6,sLine.length()-7); // Remove trimming tags (ie. <Name> and </Name>). 
                       if (asZipCode !=null) {
                           String[] temp=new String[asZipCode.length+1];
                           System.arraycopy(asZipCode,0,temp,0,asZipCode.length);
                           asZipCode=temp;
                       }
                       else {
                           asZipCode=new String[1];
                       }
                       asZipCode[asZipCode.length-1]=removeISOLatin1(sLine);
                }
            }
            brZipCode.close();                
            brZipCode = null;
        } catch (java.io.FileNotFoundException e) {
           System.out.println("City file (\"" + sZipCodeFile + "\") does not exist.");
           asZipCode=null;
        } catch (java.io.IOException e) {
           e.printStackTrace();
           asZipCode=null;
        }         

        return asZipCode;
    }    

    private String[] retrieveEvents(String sEventFile) {
        String sLine = null;
        String[] asEvent=null;
       
        try {
            BufferedReader brEvent = new BufferedReader(new FileReader(sEventFile));
            while ((sLine = brEvent.readLine()) != null) {
                sLine=sLine.trim();
                if (asEvent !=null) {
                    String[] asTemp=new String[asEvent.length+1];
                    System.arraycopy(asEvent,0,asTemp,0,asEvent.length);
                    asEvent=asTemp;
                }
                else {
                    asEvent=new String[1];
                }
                asEvent[asEvent.length-1]=sLine;
            }
            brEvent.close();                
            brEvent = null;
        } catch (java.io.FileNotFoundException e) {
           System.out.println("Event file (\"" + sEventFile + "\") does not exist.");        
        } catch (java.io.IOException e) {
           e.printStackTrace();
        }         
        return asEvent;
    }

    private String[] retrieveCountries(String sCountryFile) {
        String sLine=null;
        String[] asCountry=null;
       
        try {
            BufferedReader brCountry=new BufferedReader(new FileReader(sCountryFile));
            while ((sLine=brCountry.readLine()) != null) {
                if (asCountry!=null) {
                    String[] asTemp=new String[asCountry.length+1];
                    System.arraycopy(asCountry,0,asTemp,0,asCountry.length);
                    asCountry=asTemp;
                }
                else {
                    asCountry=new String[1];
                }
                asCountry[asCountry.length-1]=sLine.trim().substring(0,3);
            }
            brCountry.close();                
            brCountry = null;
        } catch (java.io.FileNotFoundException e) {
        	asCountry=null;
        	System.out.println("Country file (\"" + sCountryFile + "\") does not exist.");        
        } catch (java.io.IOException e) {
        	asCountry=null;
        	e.printStackTrace();
        }
        
        return asCountry;
    }

    
    /**
     * Returns a PlayerRecord array containing the rating of each player. 
     * The url argument must specify an absolute {@link URL}. The name
     * argument is a specifier that is relative to the url argument. 
     * <p>
     * This method always returns immediately, whether or not the 
     * image exists. When this applet attempts to draw the image on
     * the screen, the data will be loaded. The graphics primitives 
     * that draw the image will incrementally paint on the screen. 
     *
     * @param  		Player's full name in uppercase and without coma.
     * @return      Belgian rating or "-1" if not found
     * @see         retrieveFRBERatings()
     */
    public int getFRBE(String sPlayerFullName) {
    	int nReturnValue=-1;
    	
    	
    	// REMOVE ACCENTS AND CONVERT TO UPPERCASE.
    	sPlayerFullName = Normalizer.normalize(sPlayerFullName, Normalizer.Form.NFD);
    	sPlayerFullName = sPlayerFullName.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");    	
//    	sPlayerFullName = sPlayerFullName.toUpperCase();
    	
    	
        try {
        	
// OPEN DBF DATABASE with tinySQL. 
            Class.forName(CLASS_FORNAME).newInstance();		// Load the driver. 
            Connection con = DriverManager.getConnection("jdbc:dbfFile:.", "", "");	// Get the connection. 
            Statement stmt = con.createStatement();					// Create a statement and execute a query. 
            
// QUERY DATABASE.            
            ResultSet rsFRBERatings = stmt.executeQuery("SELECT ELO_CALCUL " +
            											"  FROM Player " +
        												" WHERE NOM_PRENOM = '" + sPlayerFullName + "'");
        	int nRowCount=0;
            while (rsFRBERatings.next()) {
                if (rsFRBERatings.getString(1)!=null) {
                	nReturnValue = rsFRBERatings.getInt(1);
                	nRowCount++;
                }
            }
            if (nRowCount!=1) {
            	nReturnValue=-1;
            }

            stmt.close();
            con.close();
        }
        catch( Exception e ) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        finally {
            // CLOSE CONNECTION TO DATABASE. 
        }
        
     // RETURN VALUE.           
        return nReturnValue;
    	
    }
    
    private PlayerRecord[] retrieveFRBERatings() {
        PlayerRecord[] aprPlayerRecord=null;
        try {
        	
// RETRIEVE RESULSET. 
            Class.forName(CLASS_FORNAME).newInstance();					// Load the driver. 
            String url = "jdbc:dbfFile:.";								// URL to the tinySQL data source. 
            Connection con = DriverManager.getConnection(url, "", "");	// Get the connection. 
            Statement stmt = con.createStatement();						// Create a statement and execute a query. 
            
            /* SELECT NOM_PRENOM, ELO_CALCUL, PART_CALCU
               FROM Player
               WHERE ELO_CALCUL IS NOT NULL
               ORDER BY NOM_PRENOM */ 
            // Unfortunately "IS NOT NULL" and "ORDER BY" are not supported by tinySQL API.
            ResultSet rsFRBERatings = stmt.executeQuery("SELECT NOM_PRENOM, ELO_CALCUL, PART_CALCU, FEDERATION" +
            											"  FROM Player");
    
// COPY RESULTSET TO AN ARRAY. 
            if (rsFRBERatings != null) {
                // Determine ordinal number of "ELO_CALCUL" column. 
                ResultSetMetaData mdFRBERatings = rsFRBERatings.getMetaData();
                int nColumnCount = mdFRBERatings.getColumnCount();
                int nEloCalcul=0;
                for (int nI=0;nI<nColumnCount; nI++) {
                    if (mdFRBERatings.getColumnName(nI+1).equals("ELO_CALCUL")) {
                        nEloCalcul=nI;
                    }
                }
                // Browse ResultSet. 
                int nLastItem=-1;
                aprPlayerRecord=new PlayerRecord[1];
                while (rsFRBERatings.next()) {
                    if (rsFRBERatings.getString(nEloCalcul)!=null) {
                        // Extend array if necessary. 
                        if (aprPlayerRecord.length==++nLastItem) {
                            PlayerRecord[] temp=new PlayerRecord[aprPlayerRecord.length+100];
                            System.arraycopy(aprPlayerRecord,0,temp,0,aprPlayerRecord.length);
                            aprPlayerRecord=temp;
                        }
                        // Store ResulSet values. 
                        aprPlayerRecord[nLastItem]=new PlayerRecord();
                        if (rsFRBERatings.getString(1)!=null) {
                            aprPlayerRecord[nLastItem].setNom_Prenom(rsFRBERatings.getString(1));
                        }
                        if (rsFRBERatings.getString(2)!=null) { // Normally Elo_Calcul is not null here. 
                            aprPlayerRecord[nLastItem].setElo_Calcul(rsFRBERatings.getString(2));
                        }
                        if (rsFRBERatings.getString(3)!=null) {
                            aprPlayerRecord[nLastItem].setPart_Calc(rsFRBERatings.getString(3));
                        }
                        if (rsFRBERatings.getString(4)!=null) {
                            aprPlayerRecord[nLastItem].setFederation(rsFRBERatings.getString(4));
                        }
                    }
                }
                // Downsize the array. 
                if (aprPlayerRecord.length!=nLastItem+1) {
                    PlayerRecord[] temp=new PlayerRecord[nLastItem+1];
                    System.arraycopy(aprPlayerRecord,0,temp,0,nLastItem+1);
                    aprPlayerRecord=temp;
                }
            }
          
// CLOSE CONNECTION TO DATABASE. 
            stmt.close();
            con.close();

// RETURN VALUE.           
        } catch( Exception e ) {
        	aprPlayerRecord=null;
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
        return aprPlayerRecord;
    }

    private String[] retrieveWithdrawnPlayers() {
        String[] asNOM_PRENOM=null;
        try {
// RETRIEVE RESULSET. 
            Class.forName(CLASS_FORNAME).newInstance(); // Load driver. 
            String url = "jdbc:dbfFile:.";                                  // URL to the tinySQL data source. 
            Connection con = DriverManager.getConnection(url, "", "");      // Get the connection. 
            Statement stmt = con.createStatement();                         // Create a statement and execute a query. 
            ResultSet rsWithdrawnPlayer = stmt.executeQuery("SELECT NOM_PRENOM FROM ExPlayer");
    
// COPY RESULTSET TO AN ARRAY. 
                // Browse ResultSet. 
                while (rsWithdrawnPlayer.next()) {
                    if (rsWithdrawnPlayer.getString(1)!=null) {
                        // Add an item to array. 
                        if (asNOM_PRENOM!=null) {
                            String[] asTemp=new String[asNOM_PRENOM.length+1];
                            System.arraycopy(asNOM_PRENOM,0,asTemp,0,asNOM_PRENOM.length);
                            asNOM_PRENOM=asTemp;
                        }
                        else {
                            asNOM_PRENOM=new String[1];
                        }
                        // Store ResulSet values. 
                        int nLastItem=asNOM_PRENOM.length-1;
                        asNOM_PRENOM[nLastItem]=new String();
                        asNOM_PRENOM[nLastItem]=rsWithdrawnPlayer.getString(1);
                    }
                }

          
// CLOSE CONNECTION TO DATABASE. 
        stmt.close();
        con.close();

// RETURN VALUE.           
        } catch( Exception e ) {
        	asNOM_PRENOM=null;
        	System.out.println(e.getMessage());
			  e.printStackTrace();
        }
        return asNOM_PRENOM;
    }

    public OLD_FIDERecord getFIDE(String sFullPlayerName) {
    	OLD_FIDERecord frReturnValue = null;

    	// LOAD FIDE RATING LIST (if necessary).
    	if (mafrFIDERecord == null) {
    		mafrFIDERecord = retrieveFIDERatings(FIDE_RATING_LIST);
    	}
    	
        // SEARCH THROUGH FIDE RATING LIST. 
        int nFound=Arrays.binarySearch(mafrFIDERecord, new FIDEPlayer(sFullPlayerName));
        if (nFound>-1) { 	// Player found.
        	frReturnValue = mafrFIDERecord[nFound];
        }
    	// TODO: to search through FRBE rating list, to see if we have a non rated FIDE.

    	return frReturnValue;
    }

//    public void testLectureSimple() {
//    	import java.io.*;
//
//    	public class TestBufferedReader {
//    	  protected String source;
//
//    	  public TestBufferedReader(String source) {
//    	     this.source = source;
//    	     lecture();
//    	  }
//
//    	  public static void main(String args[]) {
//    	     new TestBufferedReader("source.txt");
//    	  }
//
//    	  private void lecture() {
//    	     try {
//    	        String ligne ;
//    	        BufferedReader fichier = new BufferedReader(new FileReader(source));
//
//    	        while ((ligne = fichier.readLine()) != null) {
//    	           System.out.println(ligne);
//    	        }
//
//    	        fichier.close();
//    	     }
//    	     catch (Exception e) {
//    	        e.printStackTrace();
//    	     }
//    	  }
//    	}
//'"'
//    }
    
    private OLD_FIDERecord[] retrieveFIDERatings(String sFIDEFile) {
    	
    	// DISCARD (if job is already done).
    	if (mafrFIDERecord!=null) {
    		return mafrFIDERecord;
    	}
    	
        try {
            char[] acLineTemplate = null;
            BufferedReader brFIDE = new BufferedReader(new FileReader(sFIDEFile));

// ANALYZE LINES STRUCTURE. 
            String sLine = brFIDE.readLine();  // Skip the first line (could be headers). 
            int nLineCount=0;
            while ((sLine = brFIDE.readLine())!=null && nLineCount++<1000) {
                char[] acLine=new char[sLine.length()];
                sLine.getChars(0,sLine.length(),acLine,0);
                // Insure line template is long enough. 
                if (acLineTemplate==null) {
                    acLineTemplate=new char[acLine.length];
                }
                else {
                    if (acLine.length>acLineTemplate.length) {
                        char[] acTemp=new char[acLine.length];
                        System.arraycopy(acLineTemplate,0,acTemp,0,acLineTemplate.length);
                        acLineTemplate=acTemp;
                    }
                }
                // Parse line. 
                for (int nI=0; nI<acLine.length; nI++) {
                    if (acLineTemplate[nI]!='X') {
                        char cLineItem=acLine[nI];
                        if (cLineItem!=' ') {
                            char cLineTemplateItem='X';
                            cLineTemplateItem='X';
                            if (cLineItem>='0' && cLineItem<='9') {
                                cLineTemplateItem='9';
                            }
                            if (cLineItem=='.') {
                                cLineTemplateItem='.';
                            }
                            // Title and various flag. 
                            if (cLineItem>='c' && cLineItem<='w') {
                                switch(cLineItem) {
                                    case 'w':
                                        cLineTemplateItem=NULL_CHAR;    // Either flag or title. 
                                        break;
                                    case 'i':
                                        cLineTemplateItem='f'; // Flag. 
                                        break;
                                    case 'f':
                                        cLineTemplateItem='t'; // Title. 
                                        break;
                                    case 'm':
                                        cLineTemplateItem='t'; // Title. 
                                        break;
                                    case 'g':
                                        cLineTemplateItem='t'; // Title. 
                                        break;
                                    case 'h':
                                        cLineTemplateItem='t'; // Title. 
                                        break;
                                    case 'c':
                                        cLineTemplateItem='t'; // Title. 
                                        break;
                                }
                            }
                            if (cLineTemplateItem!=NULL_CHAR) {
                                acLineTemplate[nI]=cLineTemplateItem;
                            }
                        }
                    }
                }
            }
            brFIDE.close();                
            brFIDE = null;
            
// IDENTIFY COLUMNS (based on line template). 
            FIDEColumn[] fcColumn=null;
            int nColumnID=-1;
            boolean bPrecedingBlank=true;
            for (int nI=0; nI<acLineTemplate.length; nI++) {
                if (acLineTemplate[nI]==NULL_CHAR) {
                    if (!bPrecedingBlank) {
                        fcColumn[nColumnID].setEnd(nI-1);
                        fcColumn[nColumnID].setName(identifyColumn(String.copyValueOf(acLineTemplate,fcColumn[nColumnID].getStart(),fcColumn[nColumnID].getEnd()-fcColumn[nColumnID].getStart()+1).trim()));
                        bPrecedingBlank=true;
                    }
                }
                else {
                    if (bPrecedingBlank) {
                        // Expand column vector. 
                        if (fcColumn!=null) {
                            FIDEColumn[] fcTemp=new FIDEColumn[++nColumnID+1];
                            System.arraycopy(fcColumn,0,fcTemp,0,fcColumn.length);
                            fcColumn=fcTemp;
                            fcColumn[nColumnID]=new FIDEColumn();
                        }
                        else {
                            fcColumn=new FIDEColumn[1];
                            fcColumn[++nColumnID]=new FIDEColumn();
                        }
                        fcColumn[nColumnID].setStart(nI);
                        bPrecedingBlank=false;
                    }
                }
            }

            
// RETRIEVE COLUMNS CONTENT. 
            int nNAMEStart=0;
            int nNAMEEnd=0;
            int nTITLEStart=0;
            int nTITLEEnd=0;
            int nCOUNTRYStart=0;
            int nCOUNTRYEnd=0;
            int nELOStart=0;
            int nELOEnd=0;
            for (int nI=0; nI<fcColumn.length; nI++) {
                String sColumnName=fcColumn[nI].getName();
                if (sColumnName.equals("NAME")) {
                    nNAMEStart=fcColumn[nI].getStart();
                    nNAMEEnd=fcColumn[nI].getEnd();
                }
                else if (sColumnName.equals("TITLE")) {
                    nTITLEStart=fcColumn[nI].getStart();
                    nTITLEEnd=fcColumn[nI].getEnd();
                }
                else if (sColumnName.equals("COUNTRY")) {
                    nCOUNTRYStart=fcColumn[nI].getStart();
                    nCOUNTRYEnd=fcColumn[nI].getEnd();
                }
                else if (sColumnName.equals("ELO")) {
                    nELOStart=fcColumn[nI].getStart();
                    nELOEnd=fcColumn[nI].getEnd();
                }
            }
            int nMinimalLength=Math.max(Math.max(nNAMEEnd,nTITLEEnd),Math.max(nCOUNTRYEnd,nELOEnd));
            brFIDE = new BufferedReader(new FileReader(sFIDEFile));
            while ((sLine=brFIDE.readLine())!=null) {	// while ((sLine=brFIDE.readLine())!=null && sLine.length()>=nMinimalLength-1) {

                if (sLine.length()>=nMinimalLength) {
                // if (sLine.length()>=nMinimalLength && sLine.substring(nCOUNTRYStart,nCOUNTRYEnd+1).trim().equals("BEL")) {
                    // Add an item to FIDE ratings array. 
                    if (mafrFIDERecord!=null) {
                        OLD_FIDERecord[] temp=new OLD_FIDERecord[mafrFIDERecord.length+1];
                        System.arraycopy(mafrFIDERecord,0,temp,0,mafrFIDERecord.length);
                        mafrFIDERecord=temp;
                    }
                    else {
                    	mafrFIDERecord=new OLD_FIDERecord[1];
                    }
                    mafrFIDERecord[mafrFIDERecord.length-1]=new OLD_FIDERecord();
                   // Fill array item. 
                    mafrFIDERecord[mafrFIDERecord.length-1].setNAME(removeAccents(sLine.substring(nNAMEStart,nNAMEEnd+1)).toUpperCase());
                    mafrFIDERecord[mafrFIDERecord.length-1].setTITLE(sLine.substring(nTITLEStart,nTITLEEnd+1));
                    mafrFIDERecord[mafrFIDERecord.length-1].setCOUNTRY(sLine.substring(nCOUNTRYStart,nCOUNTRYEnd+1));
                    mafrFIDERecord[mafrFIDERecord.length-1].setELO(sLine.substring(nELOStart,nELOEnd+1));
                }
            }
            brFIDE.close();                
            brFIDE = null;

        } catch (java.io.FileNotFoundException e) {
        	mafrFIDERecord=null;
           System.out.println("FIDE file (\"" + sFIDEFile + "\") does not exist.");        
        } catch (java.io.IOException e) {
        	mafrFIDERecord=null;
           e.printStackTrace();
        }         
        return mafrFIDERecord;
    }
    
    private String identifyColumn(String sTemplateWord) {
        String sColumnName=null;
        
        if (sTemplateWord!=null && sTemplateWord.length()>0) {
            if (sTemplateWord.matches("\\d*") && sTemplateWord.length()<4) { // More than four digits in a row. 
                 sColumnName="GAMES";
            }
            if (sTemplateWord.matches("\\d*") && sTemplateWord.length()>4) { // Less than four digits in a row. 
                sColumnName="ID_NUMBER";
            }
            if (sTemplateWord.equals("9999")) {  // Exactly four digits in a row. 
                sColumnName="ELO";
            }
            if (sTemplateWord.equals("99.99.99")) {
                sColumnName="DATE";
            }
            if (sTemplateWord.equals("XXX")) {
                sColumnName="COUNTRY";
            }
            if (sTemplateWord.equals("tt") || sTemplateWord.equals("t")) {
                sColumnName="TITLE";
            }
            if (sTemplateWord.equals("ff") || sTemplateWord.equals("f")) {
                sColumnName="FLAG";
            }
            if (!sTemplateWord.matches("\\d*") && sTemplateWord.length()>10) {   // No digit and longer than ten characters. 
                sColumnName="NAME";
            }
        }
        if (sColumnName==null) {
            sColumnName="UNKNOWN";
        }
        return sColumnName;
    }
    private String removeAccents(String sSentence) {
    	// TODO: to take into account ligatures.

    	final char[] acAccentedLetter=     {'À','Á','Â','Ã','Ä','Å','Ç','È','É','Ê','Ë','Ì','Í','Î','Ï','Ñ','Ò','Ó','Ô','Õ','Ö','Ø','Ù','Ú','Û','Ü','Ý','à','á','â','ã','ä','å','ç','è','é','ê','ë','ì','í','î','ï','ð','ñ','ò','ó','ô','õ','ö','ø','ù','ú','û','ü','ý','ÿ'};
        final char[] acNotAccentedLetter = {'A','A','A','A','A','A','C','E','E','E','E','I','I','I','I','N','O','O','O','O','O','O','U','U','U','U','Y','a','a','a','a','a','a','c','e','e','e','e','i','i','i','i','o','n','o','o','o','o','o','o','u','u','u','u','y','y'};   // Ordered. 
        char cCurrentChar;
        int nSearchResult;
        final int nSentenceLength=sSentence.length();
        StringBuffer sbReturnValue=new StringBuffer(nSentenceLength);

        for (int nI=-1; ++nI<nSentenceLength;) {
            if ((cCurrentChar=sSentence.charAt(nI))!=',' && cCurrentChar!='.') {
                if ((nSearchResult=Arrays.binarySearch(acAccentedLetter,cCurrentChar))>-1) {
                    sbReturnValue.append(acNotAccentedLetter[nSearchResult]);
                }
                else {
                    sbReturnValue.append(cCurrentChar);
                }
            }
        } 
        return sbReturnValue.toString();
    }

    private void confrontFIDEWithFRBE(OLD_FIDERecord[] afrFIDERating, PlayerRecord[] aprPlayerFRBE, PrintWriter pwLogFile, boolean bErrorLog) {
        // FIDE and Belgian ratings are normally sorted. 
        String sFIDEName="";
        int nJ=-1;
        for (int nI=0; nI<afrFIDERating.length; nI++) {
            if (afrFIDERating[nI].getCOUNTRY().equals("BEL")) {                        
                sFIDEName=afrFIDERating[nI].getNAME();
                while (nJ<aprPlayerFRBE.length && sFIDEName.compareTo(aprPlayerFRBE[++nJ].getNom_Prenom())>0);
                if (nJ>=aprPlayerFRBE.length || sFIDEName.compareTo(aprPlayerFRBE[nJ].getNom_Prenom())<0) {
                    pwLogFile.println("FIDE player not found in Belgian rating list: \"" + sFIDEName + "\".");
                    bErrorLog=true;
                }
            }
        }
    }

    private void confrontFIDEWithFRBE(OLD_FIDERecord[] afrFIDERating, PlayerRecord[] aprPlayerFRBE, StringBuilder sbErrorLog) {
        // FIDE and Belgian ratings are normally sorted. 
        String sFIDEName="";
        int nJ=-1;
        for (int nI=0; nI<afrFIDERating.length; nI++) {
            if (afrFIDERating[nI].getCOUNTRY().equals("BEL")) {                        
                sFIDEName=afrFIDERating[nI].getNAME();
                while (nJ<aprPlayerFRBE.length && sFIDEName.compareTo(aprPlayerFRBE[++nJ].getNom_Prenom())>0);
                if (nJ>=aprPlayerFRBE.length || sFIDEName.compareTo(aprPlayerFRBE[nJ].getNom_Prenom())<0) {
                    sbErrorLog.append("FIDE player not found in Belgian rating list: \"" + sFIDEName + "\"." + END_OF_LINE);
                }
            }
        }
    }
    
    private boolean checkEndOfGameConstraints(PGNGame pgnCurrentGame, int nGameNumber, int nLineNumber, PrintWriter pwLogFile, boolean bErrorLog) {
        nGameNumber--;
        nLineNumber-=2;        
// RESULT. 
        if (pgnCurrentGame.getResult().length()==0) {
            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : result tag is missing.");
            bErrorLog=true;
        }
// SETUP AND FEN. 
        if (pgnCurrentGame.getSetUp().equals("1")) {
            if (pgnCurrentGame.getFEN().length()==0) {
                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : SetUp tag set to \"1\" and no FEN string found.");
                bErrorLog=true;
            }
        }
        else {
            if (pgnCurrentGame.getFEN().length()!=0) {
                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : SetUp tag set to \"0\" and FEN string found.");
                bErrorLog=true;
            }
        }
        return bErrorLog;
    }

    private void checkEndOfGameConstraints(PGNGame pgnCurrentGame, int nGameNumber, int nLineNumber, StringBuilder sbErrorLog) {
        nGameNumber--;
        nLineNumber-=2;
        
// RESULT. 
        if (pgnCurrentGame.getResult().length()==0) {
        	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : result tag is missing." + END_OF_LINE);
        }
        
// SETUP AND FEN. 
        if (pgnCurrentGame.getSetUp().equals("1")) {
            if (pgnCurrentGame.getFEN().length()==0) {
                sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : SetUp tag set to \"1\" and no FEN string found." + END_OF_LINE);
            }
        }
        else {
            if (pgnCurrentGame.getFEN().length()!=0) {
            	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : SetUp tag set to \"0\" and FEN string found." + END_OF_LINE);
            }
        }
    }
    
    
    private boolean checkUserConstraints(PGNGame pgnCurrentGame, String[] asEvent, PrintWriter pwLogFile, String sUserName, int nGameNumber, int nLineNumber, boolean bErrorLog) {
        nGameNumber--;
        nLineNumber-=2;
        Calendar calDate=convertPGNDateToCalendar(pgnCurrentGame.getDate());
        
// CHECK EVENT NAME. 
        String sEventName=pgnCurrentGame.getEvent();
        // Search current event in events array. 
        int nI=-1;
        while (++nI<asEvent.length && !sEventName.equals(asEvent[nI]));
        if (nI==asEvent.length) {    // Not found. 
            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : event unknown (\"" + pgnCurrentGame.getEvent() + "\").");
            bErrorLog=true;
        }
        
// NATIONAL TEAM CHAMPIONSHIP. 
        if (pgnCurrentGame.getEvent().equals("Interclubs nationaux")) {
           // National Team Championship happens only the Sunday. 
           if (calDate!=null && calDate.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY) {
                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : \"Interclubs nationaux\" normally takes place the Sunday.");
                bErrorLog = true;
           }
           // Check team name when user plays White. 
           String sWhiteTeam=pgnCurrentGame.getCustomTagPair("WhiteTeam");	// String sWhiteTeam=pgnCurrentGame.getWhiteTeam();
           if (pgnCurrentGame.getWhite().equals(sUserName) && !(sWhiteTeam.startsWith("CREC ") || sWhiteTeam.startsWith("Charleroi ") || sWhiteTeam.startsWith("Fontaine ") || sWhiteTeam.startsWith("Montignies "))) {
                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : user never played for this team (\"" + sWhiteTeam + "\").");
                bErrorLog = true;
           } 
           // Check team name when user plays Black. 
           String sBlackTeam=pgnCurrentGame.getCustomTagPair("BlackTeam");	// String sBlackTeam=pgnCurrentGame.getBlackTeam();
           if (pgnCurrentGame.getBlack().equals(sUserName) && !(sBlackTeam.startsWith("CREC ") || sBlackTeam.startsWith("Charleroi ") || sBlackTeam.startsWith("Fontaine ") || sBlackTeam.startsWith("Montignies "))) {
                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : user never played for this team (\"" + sBlackTeam + "\").");
                bErrorLog = true;
           } 
        }

// FRENCH SPEAKING TEAM CHAMPIONSHIP. 
        if (pgnCurrentGame.getEvent().equals("Interclubs francophones")) {
           // French-speaking Team Championship happens only the Sunday. 
           if (calDate!=null && calDate.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY) {
                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : \"Interclubs nationaux\" normally takes place the Sunday.");
                bErrorLog = true;
           }
           // Check team name when user plays White. 
           String sWhiteTeam=pgnCurrentGame.getCustomTagPair("WhiteTeam");	// String sWhiteTeam=pgnCurrentGame.getWhiteTeam();
           if (pgnCurrentGame.getWhite().equals(sUserName) && !(sWhiteTeam.startsWith("CREC ") || sWhiteTeam.startsWith("Charleroi ") || sWhiteTeam.startsWith("Fontaine ") || sWhiteTeam.startsWith("Montignies "))) {
                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : user never played for this team (\"" + sWhiteTeam + "\").");
                bErrorLog = true;
           } 
           // Check team name when user plays Black. 
           String sBlackTeam=pgnCurrentGame.getCustomTagPair("BlackTeam");	// String sBlackTeam=pgnCurrentGame.getBlackTeam();
           if (pgnCurrentGame.getBlack().equals(sUserName) && !(sBlackTeam.startsWith("CREC ") || sBlackTeam.startsWith("Charleroi ") || sBlackTeam.startsWith("Fontaine ") || sBlackTeam.startsWith("Montignies "))) {
                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : user never played for this team (\"" + sBlackTeam + "\").");
                bErrorLog = true;
           } 
        }
        
// EVENT DAYS. 
        // Jacques Jakobczyk's tournament happens only the Saturday. 
        if (pgnCurrentGame.getEvent().equals("Tournoi Jacques Jakobczyk")) {
            if (calDate!=null && calDate.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY) {
                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : \"Tournoi Jacques Jakobczyk\" normally takes place the Saturday.");
                bErrorLog=true;
            }
        }
        // Francis Gilson's cup happens only the Saturday. 
        if (pgnCurrentGame.getEvent().equals("Coupe Francis Gilson")) {
            if (calDate!=null && calDate.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY) {
                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : \"Coupe Francis Gilson\" normally takes place the Saturday.");
                bErrorLog=true;
            }
        }
       
// TIME CONTROL. 
        if (pgnCurrentGame.getTimeControl().length()!=0) {
            if (!(pgnCurrentGame.getTimeControl().equals("40/7200:3600") || pgnCurrentGame.getTimeControl().equals("-"))) {
                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : time control (\"" + pgnCurrentGame.getTimeControl() + "\") is unusual.");
                bErrorLog=true;
            }
        }
        else {
            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : time control is missing.");
            bErrorLog=true;
        }

// OPENINGS. 
        if (pgnCurrentGame.getNIC().length()==0) {
            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : NIC code missing.");
            bErrorLog=true;
        }
        if (pgnCurrentGame.getECO().length()==0) {
            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : ECO code missing.");
            bErrorLog=true;
        }
        if (pgnCurrentGame.getOpening().length()==0) {
            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : opening name is missing.");
            bErrorLog=true;
        }
  
// RETURN VALUE.         
        return bErrorLog;
    }

    private void checkUserConstraints(PGNGame pgnCurrentGame, String[] asEvent, StringBuilder sbErrorLog,String sUserName, int nGameNumber, int nLineNumber) {
        nGameNumber--;
        nLineNumber-=2;
        Calendar calDate=convertPGNDateToCalendar(pgnCurrentGame.getDate());
        
// CHECK EVENT NAME. 
        String sEventName=pgnCurrentGame.getEvent();
        // Search current event in events array. 
        int nI=-1;
        while (++nI<asEvent.length && !sEventName.equals(asEvent[nI]));
        if (nI==asEvent.length) {    // Not found. 
            sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : event unknown (\"" + pgnCurrentGame.getEvent() + "\")." + END_OF_LINE);
        }
        
// NATIONAL TEAM CHAMPIONSHIP. 
        if (pgnCurrentGame.getEvent().equals("Interclubs nationaux")) {
           // National Team Championship happens only the Sunday. 
           if (calDate!=null && calDate.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY) {
        	   sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : \"Interclubs nationaux\" normally takes place the Sunday." + END_OF_LINE);
           }
           // Check team name when user plays White. 
           String sWhiteTeam=pgnCurrentGame.getCustomTagPair("WhiteTeam");	// String sWhiteTeam=pgnCurrentGame.getWhiteTeam();
           if (pgnCurrentGame.getWhite().equals(sUserName) && !(sWhiteTeam.startsWith("CREC ") || sWhiteTeam.startsWith("Charleroi ") || sWhiteTeam.startsWith("Fontaine ") || sWhiteTeam.startsWith("Montignies "))) {
        	   sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : user never played for this team (\"" + sWhiteTeam + "\")." + END_OF_LINE);
           } 
           // Check team name when user plays Black. 
           String sBlackTeam=pgnCurrentGame.getCustomTagPair("BlackTeam");	// String sBlackTeam=pgnCurrentGame.getBlackTeam();
           if (pgnCurrentGame.getBlack().equals(sUserName) && !(sBlackTeam.startsWith("CREC ") || sBlackTeam.startsWith("Charleroi ") || sBlackTeam.startsWith("Fontaine ") || sBlackTeam.startsWith("Montignies "))) {
        	   sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : user never played for this team (\"" + sBlackTeam + "\")." + END_OF_LINE);
           } 
        }

// FRENCH SPEAKING TEAM CHAMPIONSHIP. 
        if (pgnCurrentGame.getEvent().equals("Interclubs francophones")) {
           // French-speaking Team Championship happens only the Sunday. 
           if (calDate!=null && calDate.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY) {
        	   sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : \"Interclubs nationaux\" normally takes place the Sunday." + END_OF_LINE);
           }
           // Check team name when user plays White. 
           String sWhiteTeam=pgnCurrentGame.getCustomTagPair("WhiteTeam");	// String sWhiteTeam=pgnCurrentGame.getWhiteTeam();
           if (pgnCurrentGame.getWhite().equals(sUserName) && !(sWhiteTeam.startsWith("CREC ") || sWhiteTeam.startsWith("Charleroi ") || sWhiteTeam.startsWith("Fontaine ") || sWhiteTeam.startsWith("Montignies "))) {
        	   sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : user never played for this team (\"" + sWhiteTeam + "\")." + END_OF_LINE);
           } 
           // Check team name when user plays Black. 
           String sBlackTeam=pgnCurrentGame.getCustomTagPair("BlackTeam");	// String sBlackTeam=pgnCurrentGame.getBlackTeam();
           if (pgnCurrentGame.getBlack().equals(sUserName) && !(sBlackTeam.startsWith("CREC ") || sBlackTeam.startsWith("Charleroi ") || sBlackTeam.startsWith("Fontaine ") || sBlackTeam.startsWith("Montignies "))) {
        	   sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : user never played for this team (\"" + sBlackTeam + "\")." + END_OF_LINE);
           } 
        }
        
// EVENT DAYS. 
        // Jacques Jakobczyk's tournament happens only the Saturday. 
        if (pgnCurrentGame.getEvent().equals("Tournoi Jacques Jakobczyk")) {
            if (calDate!=null && calDate.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY) {
            	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : \"Tournoi Jacques Jakobczyk\" normally takes place the Saturday." + END_OF_LINE);
            }
        }
        // Francis Gilson's cup happens only the Saturday. 
        if (pgnCurrentGame.getEvent().equals("Coupe Francis Gilson")) {
            if (calDate!=null && calDate.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY) {
            	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : \"Coupe Francis Gilson\" normally takes place the Saturday." + END_OF_LINE);
            }
        }
       
// TIME CONTROL. 
        if (pgnCurrentGame.getTimeControl().length()!=0) {
            if (!(pgnCurrentGame.getTimeControl().equals("40/7200:3600") || pgnCurrentGame.getTimeControl().equals("-"))) {
            	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : time control (\"" + pgnCurrentGame.getTimeControl() + "\") is unusual." + END_OF_LINE);
            }
        }
        else {
        	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : time control is missing." + END_OF_LINE);
        }

// OPENINGS. 
        if (pgnCurrentGame.getNIC().length()==0) {
        	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : NIC code missing." + END_OF_LINE);
        }
        if (pgnCurrentGame.getECO().length()==0) {
        	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : ECO code missing." + END_OF_LINE);
        }
        if (pgnCurrentGame.getOpening().length()==0) {
        	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : opening name is missing." + END_OF_LINE);
        }
    }
    
    
    private Calendar convertPGNDateToCalendar(String sPGNDate) {
        try {
            DateFormat dfPGNDate=new SimpleDateFormat("yyyy.MM.dd");
            Calendar calPGNDate=dfPGNDate.getCalendar();
            calPGNDate.setLenient(false);   // Disable self-correcting. 
            calPGNDate.setTime(dfPGNDate.parse(sPGNDate));
            return calPGNDate;
        }
        catch (ParseException e) {
            return null;
        }
    }
    
    
    
    
//    /**
//     * @deprecated
//     * Use {@link #readPGNFile(PlayerRecord[], String[], FIDERecord[], String[], String[], String[], String, String, StringBuilder, boolean, boolean, boolean, String)} instead
//     */
//    @Deprecated private boolean readPGNFile(PlayerRecord[] aprPlayerFRBE, String[] asWithdrawn, FIDERecord[] afrFIDERating, String[] asCity, String[] asEvent, String[] asCountry, String sInputFile, String sOutputFile, PrintWriter pwLogFile, boolean bAddFRBE, boolean bAddFIDE, boolean bAddTitle, String sUserName, boolean bErrorLog) {
//        String sLine;
//        int nLineNumber=0;
//        int nGameNumber=0;
//        int nFirstDoubleQuote;
//        int nSecondDoubleQuote;
//        String sLineBeginning="";
//        boolean bMoveSectionFound=false;
//        String sLastCorrectValue="";
//        ChessGame pgnCurrentGame=new ChessGame();
//        String sPreviousDate="0000.00.00";
//        
//    
//        try {
//// OPEN FILES. 
//            BufferedReader brInputFile=new BufferedReader(new FileReader(sInputFile));
//            PrintWriter pwOutputFile=new PrintWriter(new BufferedWriter(new FileWriter(sOutputFile, false)));
// 
//// READ-WRITE FILES.             
//            while ((sLine=brInputFile.readLine()) != null) {
//                nLineNumber++;
//                if ((nFirstDoubleQuote=sLine.indexOf('\"'))>-1) {
//                    sLineBeginning=sLine.substring(0,nFirstDoubleQuote+1);
//            // Event. 
//                    if (sLineBeginning.equals("[Event \"")) {
//                        if (nGameNumber++>0 && !bMoveSectionFound) {
//                            bErrorLog=checkEndOfGameConstraints(pgnCurrentGame, nGameNumber, nLineNumber, pwLogFile, bErrorLog);
//                            bErrorLog=checkUserConstraints(pgnCurrentGame, asEvent, pwLogFile, sUserName, nGameNumber, nLineNumber, bErrorLog);
//                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : move section not found.");
//                            bErrorLog=true;
//                        }
//                        pgnCurrentGame.reset();
//                        bMoveSectionFound=false;
//                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {
//                            pgnCurrentGame.setEvent(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));
//                        }
//                        else {
//                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
//                            bErrorLog=true;
//                        }
//                        pwOutputFile.println(sLine);
//            // Site. 
//                    } else if (sLineBeginning.equals("[Site \"")) {
//                        String sTagValue;
//                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {
//                            sTagValue=sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote);
//                        }
//                        else {
//                            sTagValue="";
//                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
//                            bErrorLog=true;
//                        }
//                        if (!sTagValue.equals(sLastCorrectValue) && !sTagValue.equals("?")) {
//                            // Country code. 
//                            if (sTagValue.matches(".* [A-Z][A-Z][A-Z]")) {
//                                if (sTagValue.endsWith(" BEL")) {    // Belgium. 
//                                    // Check city. 
//                                    if (Arrays.binarySearch(asCity,sTagValue.substring(0,sTagValue.length()-4))>-1) {
//                                        pgnCurrentGame.setSite(sTagValue);
//                                        sLastCorrectValue=sTagValue;
//                                    }
//                                    else {
//                                        pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : city unknown (\"" + sTagValue + "\").");
//                                        bErrorLog=true;
//                                    }
//                                }
//                                else {                              // Foreign country. 
//                                        // Check country acronym. 
//                                        if (Arrays.binarySearch(asCountry,sTagValue.substring(sTagValue.length()-3))>-1) {
//                                            pgnCurrentGame.setSite(sTagValue);
//                                            sLastCorrectValue=sTagValue;
//                                        }
//                                        else {
//                                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : country code incorrect (\"" + sTagValue + "\").");
//                                            bErrorLog=true;
//                                        }
//                                }
//                            }
//                            else {
//                                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : country code missing or incorrect (\"" + sTagValue + "\").");
//                                bErrorLog=true;
//                            }
//                        }
//                        else {
//                            sLastCorrectValue="?";
//                        }
//                        pwOutputFile.println(sLine);
//            // Date. 
//                    } else if (sLineBeginning.equals("[Date \"")) {
//                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {
//                            if (!pgnCurrentGame.setDate(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote))) {
//                                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : invalid date (\"" + sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote) + "\").");
//                                bErrorLog=true;
//                            }
//                            else {
//                                String sDate;
//                                if (sPreviousDate.replace('?','0').compareTo((sDate=pgnCurrentGame.getDate()).replace('?','0'))>0) {
//                                   pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : date of current game (\"" + sDate + "\") is earlier to date of previous game (\"" + sPreviousDate + "\").");
//                                   bErrorLog=true;
//                                }
//                                sPreviousDate=sDate;
//                            }
//                        }
//                        else {
//                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
//                            bErrorLog=true;
//                        }
//                        pwOutputFile.println(sLine);
//            // White. 
//                    } else if (sLineBeginning.equals("[White \"")) {
//                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) { // PGN tag well formed. 
//                            pgnCurrentGame.setWhite(removeAccents(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote)).toUpperCase());
//                            // Add Belgian rating.
//                            int nFRBERowID=-1;
//                            if (bAddFRBE && aprPlayerFRBE.length!=0) {
//                                nFRBERowID=Arrays.binarySearch(aprPlayerFRBE,new PlayerRecord(pgnCurrentGame.getWhite()));
//                                if (nFRBERowID>-1) {     // Player found in Belgian list. 
//                                    pgnCurrentGame.setWhiteFRBEKBSB(aprPlayerFRBE[nFRBERowID].getElo_Calcul());
//                                }
//                                else {              // Player not found in Belgian list. 
//                                    pgnCurrentGame.setWhiteFRBEKBSB("?");
//                                    // Withdrawn player ? 
//                                    if (Arrays.binarySearch(asWithdrawn,pgnCurrentGame.getWhite())>-1) {
//                                        pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : player withdrawn (\"" + pgnCurrentGame.getWhite() + "\").");
//                                        bErrorLog=true;
//                                    }
//                                    else {
//                                        pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : player not in Belgian rating list (\"" + pgnCurrentGame.getWhite() + "\").");
//                                        bErrorLog=true;
//                                    }
//                                }
//                            }
//                            // Add FIDE rating and title. 
//                            if (bAddFIDE && afrFIDERating.length!=0) {
//                                int nTemp=Arrays.binarySearch(afrFIDERating,new FIDERecord(pgnCurrentGame.getWhite()));
//                                if (nTemp>-1) { // Player found in FIDE list.
//                                    pgnCurrentGame.setWhiteElo(afrFIDERating[nTemp].getElo());
//                                    if (afrFIDERating[nTemp].getTitle().length()!=0) {
//                                        pgnCurrentGame.setWhiteTitle(afrFIDERating[nTemp].getTitle());
//                                    }
//                                    else {
//                                        pgnCurrentGame.setWhiteTitle("-");
//                                    }
//                                }
//                                else {          // Player not found in FIDE list.
//                                    if (nFRBERowID>-1 && !aprPlayerFRBE[nFRBERowID].getFederation().endsWith("*")) {    // Player member of FRBE.
//                                        pgnCurrentGame.setWhiteElo("-");
//                                        pgnCurrentGame.setWhiteTitle("-");
//                                    }
//                                    else {  // Player not member of FRBE.
//                                        pgnCurrentGame.setWhiteElo("?");
//                                        pgnCurrentGame.setWhiteTitle("?");
//                                        pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : FIDE rating not found for  \"" + pgnCurrentGame.getWhite() + "\".");
//                                        bErrorLog=true;
//                                    }
//                                }
//                            }
//                        }
//                        else {  // PGN tag wrongly formed. 
//                            pgnCurrentGame.setWhite("?");
//                            if (bAddFRBE && aprPlayerFRBE.length!=0) {
//                                pgnCurrentGame.setWhiteFRBEKBSB("?");
//                            }
//                            if (bAddFIDE && afrFIDERating.length!=0) {
//                                pgnCurrentGame.setWhiteElo("?");
//                            }
//                            if (bAddTitle && afrFIDERating.length!=0) {
//                                pgnCurrentGame.setWhiteTitle("?");
//                            }
//                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
//                            bErrorLog=true;
//                        }
//                        pwOutputFile.println(sLine);
//            // Black. 
//                    } else if (sLineBeginning.equals("[Black \"")) {
//                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) { // PGN tag well formed. 
//                            pgnCurrentGame.setBlack(removeAccents(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote)).toUpperCase());
//                            // Add Belgian rating.
//                            int nFRBERowID=-1;
//                            if (bAddFRBE && aprPlayerFRBE.length!=0) {
//                                nFRBERowID=Arrays.binarySearch(aprPlayerFRBE,new PlayerRecord(pgnCurrentGame.getBlack()));
//                                if (nFRBERowID>-1) {     // Player found in Belgian list. 
//                                    pgnCurrentGame.setBlackFRBEKBSB(aprPlayerFRBE[nFRBERowID].getElo_Calcul());
//                                }
//                                else {              // Player not found in Belgian list. 
//                                    pgnCurrentGame.setBlackFRBEKBSB("?");
//                                    // Withdrawn player ? 
//                                    if (Arrays.binarySearch(asWithdrawn,pgnCurrentGame.getBlack())>-1) {
//                                        pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : player withdrawn (\"" + pgnCurrentGame.getBlack() + "\").");
//                                        bErrorLog=true;
//                                    }
//                                    else {
//                                        pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : player not in Belgian rating list (\"" + pgnCurrentGame.getBlack() + "\").");
//                                        bErrorLog=true;
//                                    }
//                                }
//                            }
//                            // Add FIDE rating and title. 
//                            if (bAddFIDE && afrFIDERating.length!=0) {
//                                int nTemp=Arrays.binarySearch(afrFIDERating,new FIDERecord(pgnCurrentGame.getBlack()));
//                                if (nTemp>-1) { // Player found in FIDE list.
//                                    pgnCurrentGame.setBlackElo(afrFIDERating[nTemp].getELO());
//                                    if (afrFIDERating[nTemp].getTitle().length()!=0) {
//                                        pgnCurrentGame.setBlackTitle(afrFIDERating[nTemp].getTitle());
//                                    }
//                                    else {
//                                        pgnCurrentGame.setBlackTitle("-");
//                                    }
//                                }
//                                else {          // Player not found in FIDE list.
//                                    if (nFRBERowID>-1 && !aprPlayerFRBE[nFRBERowID].getFederation().endsWith("*")) {    // Player member of FRBE.
//                                        pgnCurrentGame.setBlackElo("-");
//                                        pgnCurrentGame.setBlackTitle("-");
//                                    }
//                                    else {  // Player not member of FRBE.
//                                        pgnCurrentGame.setBlackElo("?");
//                                        pgnCurrentGame.setBlackTitle("?");
//                                        pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : FIDE rating not found for  \"" + pgnCurrentGame.getBlack() + "\".");
//                                        bErrorLog=true;
//                                    }
//                                }
//                            }
//                        }
//                        else {  // PGN tag wrongly formed. 
//                            pgnCurrentGame.setBlack("?");
//                            if (bAddFRBE && aprPlayerFRBE.length!=0) {
//                                pgnCurrentGame.setBlackFRBEKBSB("?");
//                            }
//                            if (bAddFIDE && afrFIDERating.length!=0) {
//                                pgnCurrentGame.setBlackElo("?");
//                            }
//                            if (bAddTitle && afrFIDERating.length!=0) {
//                                pgnCurrentGame.setBlackTitle("?");
//                            }
//                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
//                            bErrorLog=true;
//                        }
//                        pwOutputFile.println(sLine);
//            // Result. 
//                    } else if (sLineBeginning.equals("[Result \"")) {
//                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // PGN tag well formed. 
//                            if (!pgnCurrentGame.setResult(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote))) {
//                                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : result (\"" + sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote) + "\") is incorrect.");
//                                bErrorLog=true;
//                            }
//                        }
//                        else {                          // PGN tag wrongly formed. 
//                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
//                            bErrorLog=true;
//                        }
//                        pwOutputFile.println(sLine);
//                        if (bAddTitle) {
//                            pwOutputFile.println("[WhiteTitle \"" + pgnCurrentGame.getWhiteTitle() + "\"]");
//                            pwOutputFile.println("[BlackTitle \"" + pgnCurrentGame.getBlackTitle() + "\"]");
//                        }
//                        if (bAddFIDE) {
//                            pwOutputFile.println("[WhiteElo \"" + pgnCurrentGame.getWhiteElo() + "\"]");
//                            pwOutputFile.println("[BlackElo \"" + pgnCurrentGame.getBlackElo() + "\"]");
//                        }
//                        if (bAddFRBE) {
//                            pwOutputFile.println("[WhiteFRBEKBSB \"" + pgnCurrentGame.getWhiteFRBEKBSB() + "\"]");
//                            pwOutputFile.println("[BlackFRBEKBSB \"" + pgnCurrentGame.getBlackFRBEKBSB() + "\"]");
//                        }
//            // White's FRBE. 
//                    } else if (sLineBeginning.equals("[WhiteFRBEKBSB \"")) {
//                        if (!sLine.equals("[WhiteFRBEKBSB \"" + pgnCurrentGame.getWhiteFRBEKBSB() + "\"]")) {
//                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : National rating differs from database (\"" + pgnCurrentGame.getWhite() + "\", " + pgnCurrentGame.getWhiteFRBEKBSB() + ").");
//                            bErrorLog=true;
//                            pwOutputFile.println(sLine);
//                        }
//            // Black's FRBE. 
//                    } else if (sLineBeginning.equals("[BlackFRBEKBSB \"")) {
//                        if (!sLine.equals("[BlackFRBEKBSB \"" + pgnCurrentGame.getBlackFRBEKBSB() + "\"]")) {
//                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : National rating differs from database (\"" + pgnCurrentGame.getBlack() + "\", " + pgnCurrentGame.getBlackFRBEKBSB() + ").");
//                            bErrorLog=true;
//                            pwOutputFile.println(sLine);
//                        }
//            // White's Elo. 
//                    } else if (sLineBeginning.equals("[WhiteElo \"")) {
//                        if (!sLine.equals("[WhiteElo \"" + pgnCurrentGame.getWhiteElo() + "\"]")) {
//                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : International rating differs from database (\"" + pgnCurrentGame.getWhite() + "\", " + pgnCurrentGame.getWhiteElo() + ").");
//                            bErrorLog=true;
//                            pwOutputFile.println(sLine);
//                        }
//            // Black's Elo. 
//                    } else if (sLineBeginning.equals("[BlackElo \"")) {
//                        if (!sLine.equals("[BlackElo \"" + pgnCurrentGame.getBlackElo() + "\"]")) {
//                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : International rating differs from database (\"" + pgnCurrentGame.getBlack() + "\", " + pgnCurrentGame.getBlackElo() + ").");
//                            bErrorLog=true;
//                            pwOutputFile.println(sLine);
//                        }
//            // White's team. 
//                    } else if (sLineBeginning.equals("[WhiteTeam \"")) {
//                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
//                            pgnCurrentGame.setWhiteTeam(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));
//                        }
//                        else {                          // Tag wrongly formed. 
//                            pgnCurrentGame.setWhiteTeam("");
//                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
//                            bErrorLog=true;
//                        }
//                        pwOutputFile.println(sLine);
//            // Black's team. 
//                    } else if (sLineBeginning.equals("[BlackTeam \"")) {
//                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // PGN tag well formed. 
//                            pgnCurrentGame.setBlackTeam(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));
//                        }
//                        else {                          // PGN tag wrongly formed. 
//                            pgnCurrentGame.setBlackTeam("");
//                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
//                            bErrorLog=true;
//                        }
//                        pwOutputFile.println(sLine);
//            // Setup. 
//                    } else if (sLineBeginning.equals("[SetUp \"")) {
//                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
//                            String sTemp=sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote);
//                            if (!pgnCurrentGame.setSetUp(sTemp)) {
//                                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : wrong value for SetUp tag  (\"" + sTemp + "\").");
//                                bErrorLog=true;
//                            }
//                        }
//                        else {                          // Tag wrongly formed. 
//                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
//                            bErrorLog=true;
//                        }
//                        pwOutputFile.println(sLine);
//            // FEN. 
//                    } else if (sLineBeginning.equals("[FEN \"")) {
//                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
//                            pgnCurrentGame.setFEN(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));
//                        }
//                        else {                          // Tag wrongly formed. 
//                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
//                            bErrorLog=true;
//                            pgnCurrentGame.setFEN("");
//                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
//                            bErrorLog=true;
//                        }
//                        pwOutputFile.println(sLine);
//            // Time control. 
//                    } else if (sLineBeginning.equals("[TimeControl \"")) {
//                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
//                            pgnCurrentGame.setTimeControl(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));
//                        }
//                        else {                          // Tag wrongly formed. 
//                            pgnCurrentGame.setTimeControl("");
//                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
//                            bErrorLog=true;
//                        }
//                        pwOutputFile.println(sLine);
//            // ECO code. 
//                    } else if (sLineBeginning.equals("[ECO \"")) {
//                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
//                            if (!pgnCurrentGame.setECO(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote))) {
//                                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : ECO code (\"" + sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote) + "\") is incorrect.");
//                                bErrorLog=true;
//                            }
//                        }
//                        else {                          // Tag wrongly formed. 
//                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
//                            bErrorLog=true;
//                        }
//                        pwOutputFile.println(sLine);
//            // NIC code. 
//                    } else if (sLineBeginning.equals("[NIC \"")) {
//                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
//                            if (!pgnCurrentGame.setNIC(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote))) {
//                                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : NIC code (\"" + sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote) + "\") is incorrect.");
//                                bErrorLog=true;
//                            }
//                        }
//                        else {                          // Tag wrongly formed. 
//                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
//                            bErrorLog=true;
//                        }
//                        pwOutputFile.println(sLine);
//            // Opening. 
//                    } else if (sLineBeginning.equals("[Opening \"")) {
//                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
//                            pgnCurrentGame.setOpening(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));
//                        }
//                        else {                          // Tag wrongly formed. 
//                            pgnCurrentGame.setOpening("");
//                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
//                            bErrorLog=true;
//                        }
//                        pwOutputFile.println(sLine);
//            // Else. 
//                    } else {
//                        pwOutputFile.println(sLine);
//                    }
//                }
//                else {
//                    if (pgnCurrentGame.getFEN().length()!=0) {
//                        if (sLine.matches(" *\\d*\\. *[NBRQK]?[a-h]?[1-8]?x?[a-h][1-8].*") || sLine.matches(" *\\d*\\. *O-O.*")) {
//                            bErrorLog=checkEndOfGameConstraints(pgnCurrentGame, nGameNumber, nLineNumber, pwLogFile, bErrorLog);
//                            bErrorLog=checkUserConstraints(pgnCurrentGame, asEvent, pwLogFile, sUserName, nGameNumber, nLineNumber, bErrorLog);
//                            bMoveSectionFound=true;
//                        }
//                    }
//                    else {
//                        if (sLine.matches(" *1\\. *N?[a-h][34].*")) {
//                            bErrorLog=checkEndOfGameConstraints(pgnCurrentGame, nGameNumber, nLineNumber, pwLogFile, bErrorLog);
//                            bErrorLog=checkUserConstraints(pgnCurrentGame, asEvent, pwLogFile, sUserName, nGameNumber, nLineNumber, bErrorLog);
//                            bMoveSectionFound=true;
//                        }
//                    }
//                    pwOutputFile.println(sLine);
//                }
//            }
//            
////CLOSE FILES.             
//            brInputFile.close();                
//            brInputFile = null;
//            pwOutputFile.close();
//            pwOutputFile=null;
//        }
//        catch (java.io.FileNotFoundException e) {
//        	javax.swing.JOptionPane.showMessageDialog(null, "Input file (\"" + sInputFile + "\") does not exist.");
//        }
//        catch (java.io.IOException e) {
//           e.printStackTrace();
//        }         
//        return bErrorLog;
//    }

    private ArrayList<PGNGame> readPGNFile(PlayerRecord[] aprPlayerFRBE, String[] asWithdrawn, OLD_FIDERecord[] afrFIDERating, String[] asCity, String[] asEvent, String[] asCountry, String sInputFile, String sOutputFile, StringBuilder sbErrorLog, boolean bAddFRBE, boolean bAddFIDE, boolean bAddTitle, String sUserName) {
        String sLine;
        int nLineNumber=0;
        int nGameNumber=0;
        int nFirstDoubleQuote;
        int nSecondDoubleQuote;
        String sLineBeginning="";
        boolean bMoveSectionFound=false;
        String sLastCorrectValue="";
        PGNGame pgnCurrentGame=new PGNGame();
        String sPreviousDate="0000.00.00";
        ArrayList<PGNGame> arrChessGame = new ArrayList<PGNGame>();
        boolean bAtLeastOneGameRead = false;
    
        try {
// OPEN FILE. 
            BufferedReader brInputFile=new BufferedReader(new FileReader(sInputFile));
 
// READ-WRITE FILES.             
            while ((sLine=brInputFile.readLine()) != null) {
                nLineNumber++;
                if ((nFirstDoubleQuote=sLine.indexOf('\"'))>-1) {
                    sLineBeginning=sLine.substring(0,nFirstDoubleQuote+1);
            // Event. 
                    if (sLineBeginning.equals("[Event \"")) {
                        if (nGameNumber++>0 && !bMoveSectionFound) {
                            checkEndOfGameConstraints(pgnCurrentGame, nGameNumber, nLineNumber, sbErrorLog);
                            checkUserConstraints(pgnCurrentGame, asEvent, sbErrorLog, sUserName, nGameNumber, nLineNumber);
                            sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : move section not found." + END_OF_LINE);
                        }
                        if (bAtLeastOneGameRead) {
                        	arrChessGame.add(pgnCurrentGame);
                        	pgnCurrentGame = new PGNGame();
                        }
                        else {
                        	pgnCurrentGame.reset();
                        	bAtLeastOneGameRead=true;
                        }
                        bMoveSectionFound=false;
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {
                            pgnCurrentGame.setEvent(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));
                        }
                        else {
                        	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed." + END_OF_LINE);
                        }
            // Site. 
                    } else if (sLineBeginning.equals("[Site \"")) {
                        String sTagValue;
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {
                            sTagValue=sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote);
                        }
                        else {
                            sTagValue="";
                            sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed." + END_OF_LINE);
                        }
                        if (!sTagValue.equals(sLastCorrectValue) && !sTagValue.equals("?")) {
                            // Country code. 
                            if (sTagValue.matches(".* [A-Z][A-Z][A-Z]")) {
                                if (sTagValue.endsWith(" BEL")) {    // Belgium. 
                                    // Check city. 
                                    if (Arrays.binarySearch(asCity,sTagValue.substring(0,sTagValue.length()-4))>-1) {
                                        pgnCurrentGame.setSite(sTagValue);
                                        sLastCorrectValue=sTagValue;
                                    }
                                    else {
                                    	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : city unknown (\"" + sTagValue + "\")." + END_OF_LINE);
                                    }
                                }
                                else {                              // Foreign country. 
                                        // Check country acronym. 
                                        if (Arrays.binarySearch(asCountry,sTagValue.substring(sTagValue.length()-3))>-1) {
                                            pgnCurrentGame.setSite(sTagValue);
                                            sLastCorrectValue=sTagValue;
                                        }
                                        else {
                                        	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : country code incorrect (\"" + sTagValue + "\")." + END_OF_LINE);
                                        }
                                }
                            }
                            else {
                            	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : country code missing or incorrect (\"" + sTagValue + "\")." + END_OF_LINE);
                            }
                        }
                        else {
                            sLastCorrectValue="?";
                        }
            // Date. 
                    } else if (sLineBeginning.equals("[Date \"")) {
                    	//TODO : to check game date >= event date.
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {
                            if (!pgnCurrentGame.setDate(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote))) {
                            	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : invalid date (\"" + sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote) + "\")." + END_OF_LINE);
                            }
                            else {
                                String sDate;
                                if (sPreviousDate.replace('?','0').compareTo((sDate=pgnCurrentGame.getDate()).replace('?','0'))>0) {
	                            	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : date of current game (\"" + sDate + "\") is earlier to date of previous game (\"" + sPreviousDate + "\")." + END_OF_LINE);
                                }
                                sPreviousDate=sDate;
                            }
                        }
                        else {
                        	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed." + END_OF_LINE);
                        }
            // White. 
                    } else if (sLineBeginning.equals("[White \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) { // PGN tag well formed. 
                            pgnCurrentGame.setWhite(removeAccents(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote)).toUpperCase());
                            // Add Belgian rating.
                            int nFRBERowID=-1;
                            if (bAddFRBE && aprPlayerFRBE.length!=0) {
                                nFRBERowID=Arrays.binarySearch(aprPlayerFRBE,new PlayerRecord(pgnCurrentGame.getWhite()));
                                if (nFRBERowID>-1) {     // Player found in Belgian list. 
                                    pgnCurrentGame.addCustomTagPair("WhiteFRBEKBSB", aprPlayerFRBE[nFRBERowID].getElo_Calcul());	// pgnCurrentGame.setWhiteFRBEKBSB(aprPlayerFRBE[nFRBERowID].getElo_Calcul());
                                }
                                else {              // Player not found in Belgian list. 
                                    pgnCurrentGame.addCustomTagPair("WhiteFRBEKBSB", "?"); // pgnCurrentGame.setWhiteFRBEKBSB("?");
                                    // Withdrawn player ? 
                                    if (Arrays.binarySearch(asWithdrawn,pgnCurrentGame.getWhite())>-1) {
                                    	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : player withdrawn (\"" + pgnCurrentGame.getWhite() + "\")." + END_OF_LINE);
                                    }
                                    else {
                                    	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : player not in Belgian rating list (\"" + pgnCurrentGame.getWhite() + "\")." + END_OF_LINE);
                                    }
                                }
                            }
                            // Add FIDE rating and title. 
                            if (bAddFIDE && afrFIDERating.length!=0) {
                                int nTemp=Arrays.binarySearch(afrFIDERating,new FIDEPlayer(pgnCurrentGame.getWhite()));
                                if (nTemp>-1) { // Player found in FIDE list.
                                    pgnCurrentGame.setWhiteElo(afrFIDERating[nTemp].getELO());
                                    pgnCurrentGame.setWhiteTitle(afrFIDERating[nTemp].getTITLE());
                                }
                                else {          // Player not found in FIDE list.
                                    if (nFRBERowID>-1 && !aprPlayerFRBE[nFRBERowID].getFederation().endsWith("*")) {    // Player member of FRBE.
                                        pgnCurrentGame.setWhiteElo("-");
                                        pgnCurrentGame.setWhiteTitle("-");
                                    }
                                    else {  // Player not member of FRBE.
                                        pgnCurrentGame.setWhiteElo("?");
                                        pgnCurrentGame.setWhiteTitle("?");
                                        sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : FIDE rating not found for  \"" + pgnCurrentGame.getWhite() + "\"." + END_OF_LINE);
                                    }
                                }
                            }
                        }
                        else {  // PGN tag wrongly formed. 
                            pgnCurrentGame.setWhite("?");
                            if (bAddFRBE && aprPlayerFRBE.length!=0) {
                                pgnCurrentGame.addCustomTagPair("WhiteFRBEKBSB", "?");	// pgnCurrentGame.setWhiteFRBEKBSB("?");
                            }
                            if (bAddFIDE && afrFIDERating.length!=0) {
                                pgnCurrentGame.setWhiteElo("?");
                            }
                            if (bAddTitle && afrFIDERating.length!=0) {
                                pgnCurrentGame.setWhiteTitle("?");
                            }
                            sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed."  + END_OF_LINE);
                        }
            // Black. 
                    } else if (sLineBeginning.equals("[Black \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) { // PGN tag well formed. 
                            pgnCurrentGame.setBlack(removeAccents(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote)).toUpperCase());
                            // Add Belgian rating.
                            int nFRBERowID=-1;
                            if (bAddFRBE && aprPlayerFRBE.length!=0) {
                                nFRBERowID=Arrays.binarySearch(aprPlayerFRBE,new PlayerRecord(pgnCurrentGame.getBlack()));
                                if (nFRBERowID>-1) {     // Player found in Belgian list. 
                                    pgnCurrentGame.addCustomTagPair("BlackFRBEKBSB", aprPlayerFRBE[nFRBERowID].getElo_Calcul());	// pgnCurrentGame.setBlackFRBEKBSB(aprPlayerFRBE[nFRBERowID].getElo_Calcul());
                                }
                                else {              // Player not found in Belgian list. 
                                    pgnCurrentGame.addCustomTagPair("BlackFRBEKBSB", "?");	// pgnCurrentGame.setBlackFRBEKBSB("?");
                                    // Withdrawn player ? 
                                    if (Arrays.binarySearch(asWithdrawn,pgnCurrentGame.getBlack())>-1) {
                                    	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : player withdrawn (\"" + pgnCurrentGame.getBlack() + "\")." + END_OF_LINE);
                                    }
                                    else {
                                    	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : player not in Belgian rating list (\"" + pgnCurrentGame.getBlack() + "\")." + END_OF_LINE);
                                    }
                                }
                            }
                            // Add FIDE rating and title. 
                            if (bAddFIDE && afrFIDERating.length!=0) {
                                int nTemp=Arrays.binarySearch(afrFIDERating,new FIDEPlayer(pgnCurrentGame.getBlack()));
                                if (nTemp>-1) { // Player found in FIDE list.
                                    pgnCurrentGame.setBlackElo(afrFIDERating[nTemp].getELO());
                                    pgnCurrentGame.setBlackTitle(afrFIDERating[nTemp].getTITLE());
                                }
                                else {          // Player not found in FIDE list.
                                    if (nFRBERowID>-1 && !aprPlayerFRBE[nFRBERowID].getFederation().endsWith("*")) {    // Player member of FRBE.
                                        pgnCurrentGame.setBlackElo("-");
                                        pgnCurrentGame.setBlackTitle("-");
                                    }
                                    else {  // Player not member of FRBE.
                                        pgnCurrentGame.setBlackElo("?");
                                        pgnCurrentGame.setBlackTitle("?");
                                        sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : FIDE rating not found for  \"" + pgnCurrentGame.getBlack() + "\"." + END_OF_LINE);
                                    }
                                }
                            }
                        }
                        else {  // PGN tag wrongly formed. 
                            pgnCurrentGame.setBlack("?");
                            if (bAddFRBE && aprPlayerFRBE.length!=0) {
                                pgnCurrentGame.addCustomTagPair("BlackFRBEKBSB", "?");	// pgnCurrentGame.setBlackFRBEKBSB("?");
                            }
                            if (bAddFIDE && afrFIDERating.length!=0) {
                                pgnCurrentGame.setBlackElo("?");
                            }
                            if (bAddTitle && afrFIDERating.length!=0) {
                                pgnCurrentGame.setBlackTitle("?");
                            }
                            sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed." + END_OF_LINE);
                        }
            // Result. 
                    } else if (sLineBeginning.equals("[Result \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // PGN tag well formed. 
//                            if (!pgnCurrentGame.setResult(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote))) {
//                            	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : result (\"" + sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote) + "\") is incorrect." + END_OF_LINE);
//                            }
                            pgnCurrentGame.setResult(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));
                            
                        }
                        else {                          // PGN tag wrongly formed. 
                        	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed." + END_OF_LINE);
                        }
            // White's FRBE. 
                    } else if (sLineBeginning.equals("[WhiteFRBEKBSB \"")) {
                        if (!sLine.equals("[WhiteFRBEKBSB \"" + pgnCurrentGame.getCustomTagPair("WhiteFRBEKBSB") + "\"]")) {	// if (!sLine.equals("[WhiteFRBEKBSB \"" + pgnCurrentGame.getWhiteFRBEKBSB() + "\"]")) {
                        	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : national rating differs from database (\"" + pgnCurrentGame.getWhite() + "\", " + pgnCurrentGame.getCustomTagPair("WhiteFRBEKBSB") + ")." + END_OF_LINE);	// sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : national rating differs from database (\"" + pgnCurrentGame.getWhite() + "\", " + pgnCurrentGame.getWhiteFRBEKBSB() + ")." + END_OF_LINE);
                        }
            // Black's FRBE. 
                    } else if (sLineBeginning.equals("[BlackFRBEKBSB \"")) {
                        if (!sLine.equals("[BlackFRBEKBSB \"" + pgnCurrentGame.getCustomTagPair("BlackFRBEKBSB") + "\"]")) {	// if (!sLine.equals("[BlackFRBEKBSB \"" + pgnCurrentGame.getBlackFRBEKBSB() + "\"]")) {
                        	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : National rating differs from database (\"" + pgnCurrentGame.getBlack() + "\", " + pgnCurrentGame.getCustomTagPair("BlackFRBEKBSB") + ")." + END_OF_LINE);	// sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : National rating differs from database (\"" + pgnCurrentGame.getBlack() + "\", " + pgnCurrentGame.getBlackFRBEKBSB() + ")." + END_OF_LINE);
                        }
            // White's Elo. 
                    } else if (sLineBeginning.equals("[WhiteElo \"")) {
                        if (!sLine.equals("[WhiteElo \"" + pgnCurrentGame.getWhiteElo() + "\"]")) {
                        	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : International rating differs from database (\"" + pgnCurrentGame.getWhite() + "\", " + pgnCurrentGame.getWhiteElo() + ")." + END_OF_LINE);
                        }
            // Black's Elo. 
                    } else if (sLineBeginning.equals("[BlackElo \"")) {
                        if (!sLine.equals("[BlackElo \"" + pgnCurrentGame.getBlackElo() + "\"]")) {
                        	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : International rating differs from database (\"" + pgnCurrentGame.getBlack() + "\", " + pgnCurrentGame.getBlackElo() + ")." + END_OF_LINE);
                        }
            // White's team. 
                    } else if (sLineBeginning.equals("[WhiteTeam \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
                            pgnCurrentGame.addCustomTagPair("WhiteTeam", sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));	// pgnCurrentGame.setWhiteTeam(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));
                        }
                        else {                          // Tag wrongly formed. 
                            pgnCurrentGame.addCustomTagPair("WhiteTeam", "");	// pgnCurrentGame..setWhiteTeam("");
                            sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed." + END_OF_LINE);
                        }
            // Black's team. 
                    } else if (sLineBeginning.equals("[BlackTeam \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // PGN tag well formed. 
                            pgnCurrentGame.addCustomTagPair("BlackTeam", sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));	// pgnCurrentGame.setBlackTeam(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));
                        }
                        else {                          // PGN tag wrongly formed. 
                            pgnCurrentGame.addCustomTagPair("BlackTeam", "");	// pgnCurrentGame.setBlackTeam("");
                            sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed." + END_OF_LINE);
                        }
            // Setup. 
                    } else if (sLineBeginning.equals("[SetUp \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
                            String sTemp=sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote);
                            if (!(sTemp.equals("0") || sTemp.equals("1"))) {
                            	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : wrong value for SetUp tag  (\"" + sTemp + "\")." + END_OF_LINE);
                            }
                        }
                        else {                          // Tag wrongly formed. 
                        	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed." + END_OF_LINE);
                        }
            // FEN. 
                    } else if (sLineBeginning.equals("[FEN \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
                            pgnCurrentGame.setFEN(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));
                        }
                        else {                          // Tag wrongly formed. 
                        	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed." + END_OF_LINE);
                            pgnCurrentGame.setFEN("");
                            sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed." + END_OF_LINE);
                        }
            // Time control. 
                    } else if (sLineBeginning.equals("[TimeControl \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
                            pgnCurrentGame.setTimeControl(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));
                        }
                        else {                          // Tag wrongly formed. 
                            pgnCurrentGame.setTimeControl("");
                            sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed." + END_OF_LINE);
                        }
            // ECO code. 
                    } else if (sLineBeginning.equals("[ECO \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
                            if (!pgnCurrentGame.setECO(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote))) {
                            	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : ECO code (\"" + sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote) + "\") is incorrect." + END_OF_LINE);
                            }
                        }
                        else {                          // Tag wrongly formed. 
                        	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed." + END_OF_LINE);
                        }
            // NIC code. 
                    } else if (sLineBeginning.equals("[NIC \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
                            if (!pgnCurrentGame.setNIC(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote))) {
                            	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : NIC code (\"" + sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote) + "\") is incorrect." + END_OF_LINE);
                            }
                        }
                        else {                          // Tag wrongly formed. 
                        	sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed." + END_OF_LINE);
                        }
            // Opening. 
                    } else if (sLineBeginning.equals("[Opening \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
                            pgnCurrentGame.setOpening(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));
                        }
                        else {                          // Tag wrongly formed. 
                            pgnCurrentGame.setOpening("");
                            sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed." + END_OF_LINE);
                        }
            // Else. 
                    } else {
                        sbErrorLog.append("Game " + nGameNumber + ", line " + nLineNumber + " : unidentified line." + END_OF_LINE);
                    }
                }
                else {
                    if (pgnCurrentGame.getFEN().length()!=0) {
                        if (sLine.matches(" *\\d*\\. *[NBRQK]?[a-h]?[1-8]?x?[a-h][1-8].*") || sLine.matches(" *\\d*\\. *O-O.*")) {
                            checkEndOfGameConstraints(pgnCurrentGame, nGameNumber, nLineNumber, sbErrorLog);
                            checkUserConstraints(pgnCurrentGame, asEvent, sbErrorLog, sUserName, nGameNumber, nLineNumber);
                            bMoveSectionFound=true;
                        }
                    }
                    else {
                        if (sLine.matches(" *1\\. *N?[a-h][34].*")) {
                            checkEndOfGameConstraints(pgnCurrentGame, nGameNumber, nLineNumber, sbErrorLog);
                            checkUserConstraints(pgnCurrentGame, asEvent, sbErrorLog, sUserName, nGameNumber, nLineNumber);
                            bMoveSectionFound=true;
                        }
                    }
                }
            }
            
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
        
        return arrChessGame;
    }

    
    
/*    private boolean processPGNFile(PlayerRecord[] aprPlayerFRBE, String[] asWithdrawn, FIDERecord[] afrFIDERating, String[] asCity, String[] asEvent, String[] asCountry, String sInputFile, String sOutputFile, PrintWriter pwLogFile, boolean bAddFRBE, boolean bAddFIDE, boolean bAddTitle, String sUserName, boolean bErrorLog) {
        String sLine;
        int nLineNumber=0;
        int nGameNumber=0;
        int nFirstDoubleQuote;
        int nSecondDoubleQuote;
        String sLineBeginning="";
        boolean bMoveSectionFound=false;
        String sLastCorrectValue="";
        ChessGame pgnCurrentGame=new ChessGame();
        String sPreviousDate="0000.00.00";
        
    
        try {
// OPEN FILES. 
            BufferedReader brInputFile=new BufferedReader(new FileReader(sInputFile));
            PrintWriter pwOutputFile=new PrintWriter(new BufferedWriter(new FileWriter(sOutputFile, false)));
 
// READ-WRITE FILES.             
            while ((sLine=brInputFile.readLine()) != null) {
                nLineNumber++;
                if ((nFirstDoubleQuote=sLine.indexOf('\"'))>-1) {
                    sLineBeginning=sLine.substring(0,nFirstDoubleQuote+1);
            // Event. 
                    if (sLineBeginning.equals("[Event \"")) {
                        if (nGameNumber++>0 && !bMoveSectionFound) {
                            bErrorLog=checkEndOfGameConstraints(pgnCurrentGame, nGameNumber, nLineNumber, pwLogFile, bErrorLog);
                            bErrorLog=checkUserConstraints(pgnCurrentGame, asEvent, pwLogFile, sUserName, nGameNumber, nLineNumber, bErrorLog);
                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : move section not found.");
                            bErrorLog=true;
                        }
                        pgnCurrentGame.reset();
                        bMoveSectionFound=false;
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {
                            pgnCurrentGame.setEvent(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));
                        }
                        else {
                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
                            bErrorLog=true;
                        }
                        pwOutputFile.println(sLine);
            // Site. 
                    } else if (sLineBeginning.equals("[Site \"")) {
                        String sTagValue;
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {
                            sTagValue=sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote);
                        }
                        else {
                            sTagValue="";
                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
                            bErrorLog=true;
                        }
                        if (!sTagValue.equals(sLastCorrectValue) && !sTagValue.equals("?")) {
                            // Country code. 
                            if (sTagValue.matches(".* [A-Z][A-Z][A-Z]")) {
                                if (sTagValue.endsWith(" BEL")) {    // Belgium. 
                                    // Check city. 
                                    if (Arrays.binarySearch(asCity,sTagValue.substring(0,sTagValue.length()-4))>-1) {
                                        pgnCurrentGame.setSite(sTagValue);
                                        sLastCorrectValue=sTagValue;
                                    }
                                    else {
                                        pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : city unknown (\"" + sTagValue + "\").");
                                        bErrorLog=true;
                                    }
                                }
                                else {                              // Foreign country. 
                                        // Check country acronym. 
                                        if (Arrays.binarySearch(asCountry,sTagValue.substring(sTagValue.length()-3))>-1) {
                                            pgnCurrentGame.setSite(sTagValue);
                                            sLastCorrectValue=sTagValue;
                                        }
                                        else {
                                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : country code incorrect (\"" + sTagValue + "\").");
                                            bErrorLog=true;
                                        }
                                }
                            }
                            else {
                                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : country code missing or incorrect (\"" + sTagValue + "\").");
                                bErrorLog=true;
                            }
                        }
                        else {
                            sLastCorrectValue="?";
                        }
                        pwOutputFile.println(sLine);
            // Date. 
                    } else if (sLineBeginning.equals("[Date \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {
                            if (!pgnCurrentGame.setDate(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote))) {
                                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : invalid date (\"" + sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote) + "\").");
                                bErrorLog=true;
                            }
                            else {
                                String sDate;
                                if (sPreviousDate.replace('?','0').compareTo((sDate=pgnCurrentGame.getDate()).replace('?','0'))>0) {
                                   pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : date of current game (\"" + sDate + "\") is earlier to date of previous game (\"" + sPreviousDate + "\").");
                                   bErrorLog=true;
                                }
                                sPreviousDate=sDate;
                            }
                        }
                        else {
                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
                            bErrorLog=true;
                        }
                        pwOutputFile.println(sLine);
            // White. 
                    } else if (sLineBeginning.equals("[White \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) { // PGN tag well formed. 
                            pgnCurrentGame.setWhite(removeAccents(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote)).toUpperCase());
                            // Add Belgian rating.
                            int nFRBERowID=-1;
                            if (bAddFRBE && aprPlayerFRBE.length!=0) {
                                nFRBERowID=Arrays.binarySearch(aprPlayerFRBE,new PlayerRecord(pgnCurrentGame.getWhite()));
                                if (nFRBERowID>-1) {     // Player found in Belgian list. 
                                    pgnCurrentGame.setWhiteFRBEKBSB(aprPlayerFRBE[nFRBERowID].getElo_Calcul());
                                }
                                else {              // Player not found in Belgian list. 
                                    pgnCurrentGame.setWhiteFRBEKBSB("?");
                                    // Withdrawn player ? 
                                    if (Arrays.binarySearch(asWithdrawn,pgnCurrentGame.getWhite())>-1) {
                                        pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : player withdrawn (\"" + pgnCurrentGame.getWhite() + "\").");
                                        bErrorLog=true;
                                    }
                                    else {
                                        pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : player not in Belgian rating list (\"" + pgnCurrentGame.getWhite() + "\").");
                                        bErrorLog=true;
                                    }
                                }
                            }
                            // Add FIDE rating and title. 
                            if (bAddFIDE && afrFIDERating.length!=0) {
                                int nTemp=Arrays.binarySearch(afrFIDERating,new FIDERecord(pgnCurrentGame.getWhite()));
                                if (nTemp>-1) { // Player found in FIDE list.
                                    pgnCurrentGame.setWhiteElo(afrFIDERating[nTemp].getELO());
                                    if (afrFIDERating[nTemp].getTITLE().length()!=0) {
                                        pgnCurrentGame.setWhiteTitle(afrFIDERating[nTemp].getTITLE());
                                    }
                                    else {
                                        pgnCurrentGame.setWhiteTitle("-");
                                    }
                                }
                                else {          // Player not found in FIDE list.
                                    if (nFRBERowID>-1 && !aprPlayerFRBE[nFRBERowID].getFederation().endsWith("*")) {    // Player member of FRBE.
                                        pgnCurrentGame.setWhiteElo("-");
                                        pgnCurrentGame.setWhiteTitle("-");
                                    }
                                    else {  // Player not member of FRBE.
                                        pgnCurrentGame.setWhiteElo("?");
                                        pgnCurrentGame.setWhiteTitle("?");
                                        pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : FIDE rating not found for  \"" + pgnCurrentGame.getWhite() + "\".");
                                        bErrorLog=true;
                                    }
                                }
                            }
                        }
                        else {  // PGN tag wrongly formed. 
                            pgnCurrentGame.setWhite("?");
                            if (bAddFRBE && aprPlayerFRBE.length!=0) {
                                pgnCurrentGame.setWhiteFRBEKBSB("?");
                            }
                            if (bAddFIDE && afrFIDERating.length!=0) {
                                pgnCurrentGame.setWhiteElo("?");
                            }
                            if (bAddTitle && afrFIDERating.length!=0) {
                                pgnCurrentGame.setWhiteTitle("?");
                            }
                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
                            bErrorLog=true;
                        }
                        pwOutputFile.println(sLine);
            // Black. 
                    } else if (sLineBeginning.equals("[Black \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) { // PGN tag well formed. 
                            pgnCurrentGame.setBlack(removeAccents(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote)).toUpperCase());
                            // Add Belgian rating.
                            int nFRBERowID=-1;
                            if (bAddFRBE && aprPlayerFRBE.length!=0) {
                                nFRBERowID=Arrays.binarySearch(aprPlayerFRBE,new PlayerRecord(pgnCurrentGame.getBlack()));
                                if (nFRBERowID>-1) {     // Player found in Belgian list. 
                                    pgnCurrentGame.setBlackFRBEKBSB(aprPlayerFRBE[nFRBERowID].getElo_Calcul());
                                }
                                else {              // Player not found in Belgian list. 
                                    pgnCurrentGame.setBlackFRBEKBSB("?");
                                    // Withdrawn player ? 
                                    if (Arrays.binarySearch(asWithdrawn,pgnCurrentGame.getBlack())>-1) {
                                        pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : player withdrawn (\"" + pgnCurrentGame.getBlack() + "\").");
                                        bErrorLog=true;
                                    }
                                    else {
                                        pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : player not in Belgian rating list (\"" + pgnCurrentGame.getBlack() + "\").");
                                        bErrorLog=true;
                                    }
                                }
                            }
                            // Add FIDE rating and title. 
                            if (bAddFIDE && afrFIDERating.length!=0) {
                                int nTemp=Arrays.binarySearch(afrFIDERating,new FIDERecord(pgnCurrentGame.getBlack()));
                                if (nTemp>-1) { // Player found in FIDE list.
                                    pgnCurrentGame.setBlackElo(afrFIDERating[nTemp].getELO());
                                    if (afrFIDERating[nTemp].getTITLE().length()!=0) {
                                        pgnCurrentGame.setBlackTitle(afrFIDERating[nTemp].getTITLE());
                                    }
                                    else {
                                        pgnCurrentGame.setBlackTitle("-");
                                    }
                                }
                                else {          // Player not found in FIDE list.
                                    if (nFRBERowID>-1 && !aprPlayerFRBE[nFRBERowID].getFederation().endsWith("*")) {    // Player member of FRBE.
                                        pgnCurrentGame.setBlackElo("-");
                                        pgnCurrentGame.setBlackTitle("-");
                                    }
                                    else {  // Player not member of FRBE.
                                        pgnCurrentGame.setBlackElo("?");
                                        pgnCurrentGame.setBlackTitle("?");
                                        pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : FIDE rating not found for  \"" + pgnCurrentGame.getBlack() + "\".");
                                        bErrorLog=true;
                                    }
                                }
                            }
                        }
                        else {  // PGN tag wrongly formed. 
                            pgnCurrentGame.setBlack("?");
                            if (bAddFRBE && aprPlayerFRBE.length!=0) {
                                pgnCurrentGame.setBlackFRBEKBSB("?");
                            }
                            if (bAddFIDE && afrFIDERating.length!=0) {
                                pgnCurrentGame.setBlackElo("?");
                            }
                            if (bAddTitle && afrFIDERating.length!=0) {
                                pgnCurrentGame.setBlackTitle("?");
                            }
                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
                            bErrorLog=true;
                        }
                        pwOutputFile.println(sLine);
            // Result. 
                    } else if (sLineBeginning.equals("[Result \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // PGN tag well formed. 
                            if (!pgnCurrentGame.setResult(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote))) {
                                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : result (\"" + sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote) + "\") is incorrect.");
                                bErrorLog=true;
                            }
                        }
                        else {                          // PGN tag wrongly formed. 
                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
                            bErrorLog=true;
                        }
                        pwOutputFile.println(sLine);
                        if (bAddTitle) {
                            pwOutputFile.println("[WhiteTitle \"" + pgnCurrentGame.getWhiteTitle() + "\"]");
                            pwOutputFile.println("[BlackTitle \"" + pgnCurrentGame.getBlackTitle() + "\"]");
                        }
                        if (bAddFIDE) {
                            pwOutputFile.println("[WhiteElo \"" + pgnCurrentGame.getWhiteElo() + "\"]");
                            pwOutputFile.println("[BlackElo \"" + pgnCurrentGame.getBlackElo() + "\"]");
                        }
                        if (bAddFRBE) {
                            pwOutputFile.println("[WhiteFRBEKBSB \"" + pgnCurrentGame.getWhiteFRBEKBSB() + "\"]");
                            pwOutputFile.println("[BlackFRBEKBSB \"" + pgnCurrentGame.getBlackFRBEKBSB() + "\"]");
                        }
            // White's FRBE. 
                    } else if (sLineBeginning.equals("[WhiteFRBEKBSB \"")) {
                        if (!sLine.equals("[WhiteFRBEKBSB \"" + pgnCurrentGame.getWhiteFRBEKBSB() + "\"]")) {
                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : National rating differs from database (\"" + pgnCurrentGame.getWhite() + "\", " + pgnCurrentGame.getWhiteFRBEKBSB() + ").");
                            bErrorLog=true;
                            pwOutputFile.println(sLine);
                        }
            // Black's FRBE. 
                    } else if (sLineBeginning.equals("[BlackFRBEKBSB \"")) {
                        if (!sLine.equals("[BlackFRBEKBSB \"" + pgnCurrentGame.getBlackFRBEKBSB() + "\"]")) {
                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : National rating differs from database (\"" + pgnCurrentGame.getBlack() + "\", " + pgnCurrentGame.getBlackFRBEKBSB() + ").");
                            bErrorLog=true;
                            pwOutputFile.println(sLine);
                        }
            // White's Elo. 
                    } else if (sLineBeginning.equals("[WhiteElo \"")) {
                        if (!sLine.equals("[WhiteElo \"" + pgnCurrentGame.getWhiteElo() + "\"]")) {
                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : International rating differs from database (\"" + pgnCurrentGame.getWhite() + "\", " + pgnCurrentGame.getWhiteElo() + ").");
                            bErrorLog=true;
                            pwOutputFile.println(sLine);
                        }
            // Black's Elo. 
                    } else if (sLineBeginning.equals("[BlackElo \"")) {
                        if (!sLine.equals("[BlackElo \"" + pgnCurrentGame.getBlackElo() + "\"]")) {
                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : International rating differs from database (\"" + pgnCurrentGame.getBlack() + "\", " + pgnCurrentGame.getBlackElo() + ").");
                            bErrorLog=true;
                            pwOutputFile.println(sLine);
                        }
            // White's team. 
                    } else if (sLineBeginning.equals("[WhiteTeam \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
                            pgnCurrentGame.setWhiteTeam(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));
                        }
                        else {                          // Tag wrongly formed. 
                            pgnCurrentGame.setWhiteTeam("");
                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
                            bErrorLog=true;
                        }
                        pwOutputFile.println(sLine);
            // Black's team. 
                    } else if (sLineBeginning.equals("[BlackTeam \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // PGN tag well formed. 
                            pgnCurrentGame.setBlackTeam(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));
                        }
                        else {                          // PGN tag wrongly formed. 
                            pgnCurrentGame.setBlackTeam("");
                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
                            bErrorLog=true;
                        }
                        pwOutputFile.println(sLine);
            // Setup. 
                    } else if (sLineBeginning.equals("[SetUp \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
                            String sTemp=sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote);
                            if (!pgnCurrentGame.setSetUp(sTemp)) {
                                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : wrong value for SetUp tag  (\"" + sTemp + "\").");
                                bErrorLog=true;
                            }
                        }
                        else {                          // Tag wrongly formed. 
                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
                            bErrorLog=true;
                        }
                        pwOutputFile.println(sLine);
            // FEN. 
                    } else if (sLineBeginning.equals("[FEN \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
                            pgnCurrentGame.setFEN(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));
                        }
                        else {                          // Tag wrongly formed. 
                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
                            bErrorLog=true;
                            pgnCurrentGame.setFEN("");
                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
                            bErrorLog=true;
                        }
                        pwOutputFile.println(sLine);
            // Time control. 
                    } else if (sLineBeginning.equals("[TimeControl \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
                            pgnCurrentGame.setTimeControl(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));
                        }
                        else {                          // Tag wrongly formed. 
                            pgnCurrentGame.setTimeControl("");
                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
                            bErrorLog=true;
                        }
                        pwOutputFile.println(sLine);
            // ECO code. 
                    } else if (sLineBeginning.equals("[ECO \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
                            if (!pgnCurrentGame.setECO(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote))) {
                                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : ECO code (\"" + sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote) + "\") is incorrect.");
                                bErrorLog=true;
                            }
                        }
                        else {                          // Tag wrongly formed. 
                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
                            bErrorLog=true;
                        }
                        pwOutputFile.println(sLine);
            // NIC code. 
                    } else if (sLineBeginning.equals("[NIC \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
                            if (!pgnCurrentGame.setNIC(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote))) {
                                pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : NIC code (\"" + sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote) + "\") is incorrect.");
                                bErrorLog=true;
                            }
                        }
                        else {                          // Tag wrongly formed. 
                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
                            bErrorLog=true;
                        }
                        pwOutputFile.println(sLine);
            // Opening. 
                    } else if (sLineBeginning.equals("[Opening \"")) {
                        if ((nSecondDoubleQuote=sLine.indexOf('\"',nFirstDoubleQuote+1))>-1) {    // Tag well formed. 
                            pgnCurrentGame.setOpening(sLine.substring(nFirstDoubleQuote+1,nSecondDoubleQuote));
                        }
                        else {                          // Tag wrongly formed. 
                            pgnCurrentGame.setOpening("");
                            pwLogFile.println("Game " + nGameNumber + ", line " + nLineNumber + " : tag improperly closed.");
                            bErrorLog=true;
                        }
                        pwOutputFile.println(sLine);
            // Else. 
                    } else {
                        pwOutputFile.println(sLine);
                    }
                }
                else {
                    if (pgnCurrentGame.getFEN().length()!=0) {
                        if (sLine.matches(" *\\d*\\. *[NBRQK]?[a-h]?[1-8]?x?[a-h][1-8].*") || sLine.matches(" *\\d*\\. *O-O.*")) {
                            bErrorLog=checkEndOfGameConstraints(pgnCurrentGame, nGameNumber, nLineNumber, pwLogFile, bErrorLog);
                            bErrorLog=checkUserConstraints(pgnCurrentGame, asEvent, pwLogFile, sUserName, nGameNumber, nLineNumber, bErrorLog);
                            bMoveSectionFound=true;
                        }
                    }
                    else {
                        if (sLine.matches(" *1\\. *N?[a-h][34].*")) {
                            bErrorLog=checkEndOfGameConstraints(pgnCurrentGame, nGameNumber, nLineNumber, pwLogFile, bErrorLog);
                            bErrorLog=checkUserConstraints(pgnCurrentGame, asEvent, pwLogFile, sUserName, nGameNumber, nLineNumber, bErrorLog);
                            bMoveSectionFound=true;
                        }
                    }
                    pwOutputFile.println(sLine);
                }
            }
            
//CLOSE FILES.             
            brInputFile.close();                
            brInputFile = null;
            pwOutputFile.close();
            pwOutputFile=null;
        }
        catch (java.io.FileNotFoundException e) {
        	javax.swing.JOptionPane.showMessageDialog(null, "Input file (\"" + sInputFile + "\") does not exist.");
        }
        catch (java.io.IOException e) {
           e.printStackTrace();
        }         
        return bErrorLog;
    }
*/

    /** Main method of this class. */
    public void completePGNFile() {
        final String CITY_FILE="/home/michael/sources/java/playerDBF/zipcode_alpha.xml";
        final String COUNTRY_FILE="/home/michael/sources/java/playerDBF/countries.txt";
        final String EVENT_FILE="/home/michael/sources/java/playerDBF/events.txt";
        final String FIDE_FILE ="/home/michael/sources/java/playerDBF/APR04FRL.TXT";
        final String PGN_FILE="/home/michael/pgn/deleteme.pgn";
        final String OUTPUT_FILE="/home/michael/pgn/deleteme.out";
        final String LOG_FILE="/home/michael/pgn/deleteme.log";
        final String USER_NAME="George, MichaÃ¯Â¿Â½l";
//        boolean bErrorLog=false;

       
        try {
            // FETCH DATA. 
            // Load country acronyms. 
            System.out.println("Loading countries.");
            String[] asCountry=retrieveCountries(COUNTRY_FILE);
            Arrays.sort(asCountry);
            // Load city names. 
            System.out.println("Loading cities.");
            String[] asCity=retrieveCities(CITY_FILE);
            Arrays.sort(asCity);
            // Load event names. 
            System.out.println("Loading events.");
            String[] asEvent=retrieveEvents(EVENT_FILE);
            Arrays.sort(asEvent);
            // Load Belgian ratings. 
            System.out.println("Loading Belgian ratings...");
            PlayerRecord[] aprPlayerFRBE=retrieveFRBERatings();
            Arrays.sort(aprPlayerFRBE);
            // Load withdrawn players. 
            System.out.println("Loading withdrawn players...");
            String[] asWithdrawn=retrieveWithdrawnPlayers();
            Arrays.sort(asWithdrawn);
            // Load FIDE ratings. 
            System.out.println("Loading FIDE ratings.....");
            OLD_FIDERecord[] afrFIDERating=retrieveFIDERatings(FIDE_FILE);
            Arrays.sort(afrFIDERating);
            // Check that all Belgian players in FIDE rating list are in FRBE rating list. 
            StringBuilder sbErrorLog = new StringBuilder();
//            confrontFIDEWithFRBE(afrFIDERating,aprPlayerFRBE, pwLogFile, bErrorLog);
            confrontFIDEWithFRBE(afrFIDERating,aprPlayerFRBE, sbErrorLog);

            
// INFORM USER OF NOT FETCHED DATA.
            String sMessage="The following data were not loaded and won't, therefore, not be checked:\r\n";

            // Country acronyms. 
            if (asCountry==null) {
            	sMessage+="- country acronyms,\r\n";
            }
            // City names. 
            if (asCity==null) {
            	sMessage+="- city names,\r\n";
            }
            // Event names. 
            if (asEvent==null) {
            	sMessage+="- event names,\r\n";
            }
            // Belgian ratings. 
            if (aprPlayerFRBE==null) {
            	sMessage+="- belgian ratings,\r\n";
            }
            // Withdrawn players. 
            if (asWithdrawn==null) {
            	sMessage+="- withdrawn players,\r\n";
            }
            // FIDE ratings. 
            if (afrFIDERating==null) {
            	sMessage+="- FIDE ratings,\r\n";
            }
            javax.swing.JOptionPane.showMessageDialog(null,sMessage); 

            
// PROCESS PGN FILE. 
            System.out.println("Processing games.");
//            bErrorLog=readPGNFile(aprPlayerFRBE, asWithdrawn, afrFIDERating, asCity, asEvent, asCountry, PGN_FILE, OUTPUT_FILE, pwLogFile, true, true, true, USER_NAME, bErrorLog);
            ArrayList<PGNGame> arr = readPGNFile(aprPlayerFRBE, asWithdrawn, afrFIDERating, asCity, asEvent, asCountry, PGN_FILE, OUTPUT_FILE, sbErrorLog, true, true, true, USER_NAME);
    
            
// ERROR LOG. 
            if (sbErrorLog.length()!=0) {
                // WRITE DOWN ERROR LOG. 
                PrintWriter pwLogFile = new PrintWriter(new BufferedWriter(new FileWriter(LOG_FILE, false)));
                pwLogFile.print(sbErrorLog);
                pwLogFile.close();
                pwLogFile = null;
            	javax.swing.JOptionPane.showMessageDialog(null, "Error(s) found. Please refer to " + LOG_FILE + ".");
            }
            sbErrorLog = null;
        }
        catch (java.io.FileNotFoundException e) {
        	javax.swing.JOptionPane.showMessageDialog(null, "Unable to create log file.");
        }
        catch (java.io.IOException e) {
           e.printStackTrace();
        }
    }
}
