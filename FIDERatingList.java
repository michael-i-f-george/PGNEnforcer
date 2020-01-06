package org.chess.pgn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.*;
import java.text.*;

import org.chess.pgn.FIDEPlayer.Sex;
import org.chess.pgn.FIDEPlayer.Title;

//public class HSQLDB extends java.lang.Thread{
public class FIDERatingList {
	
	private Connection mjdbcConnection=null;
	private final String DATABASE_PATH ="\\\\BIGFOOT\\perso\\michael\\workspace\\PGNPerf\\fide_rating_lists";

	
	
	FIDERatingList() {	// Constructor.
		try {

			// OPEN HSQLDB DATABASE.
			System.out.println(new java.util.Date() + " -- HSQLDB constructor: connecting DB");  
	    	Class.forName("org.hsqldb.jdbcDriver").newInstance(); 		// Load the driver.
	    	if (mjdbcConnection==null) {
	    		mjdbcConnection = DriverManager.getConnection("jdbc:hsqldb:file:" + DATABASE_PATH, "sa",  "");	// Get the connection.
	    	}
			System.out.println(new java.util.Date() + " -- HSQLDB constructor: DB connected");  
        }
        catch( Exception e ) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        finally {
        }
	}
	
//	public void run() {
//		
//		try {
//			System.out.println("HSQLDB(): " + new java.util.Date() + ", load driver");  
//	    	// OPEN HSQLDB DATABASE.
//	    	Class.forName("org.hsqldb.jdbcDriver").newInstance(); 		// Load the driver.
////	    	java.lang.Thread.sleep(500);
//	    	if (mjdbcConnection==null) {
//	    		mjdbcConnection = DriverManager.getConnection("jdbc:hsqldb:file:" + DATABASE_PATH, "sa",  "");	// Get the connection.
//	    	}
//	    	
//			System.out.println("HSQLDB():" + new java.util.Date() + ", driver loaded");  
//        }
//        catch( Exception e ) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
//        finally {
//        }
//
//		
//	}
	
	public void finalize() {	// Destructor.

		try {
			// CLOSE CONNECTION TO DATABASE.
	    	Statement statement = mjdbcConnection.createStatement();
	     	statement = mjdbcConnection.createStatement();
	    	statement.executeQuery("SHUTDOWN");
	    	statement.close();
	    	mjdbcConnection.close();
	    }
	    catch( Exception e ) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
	    finally {
	    }
	}
	
    /**
     * Returns a PlayerRecord array containing the rating of each player. 
     * The URL argument must specify an absolute {@link URL}. The name
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
    public int convertFIDEList() {
    	int nReturnValue=-1;
		final int BATCH_SIZE = 1000;
    	int nCount = 0;
    	
    	
    	

    	
		List<FIDEPlayer> lstfidePlayer = (new FIDEPlayerList("\\\\bigfoot\\perso\\michael\\workspace\\pgnperf\\players_list_xml.xml")).getList();

    	
        try {
        	
        	// OPEN HSQLDB DATABASE.
        	Class.forName("org.hsqldb.jdbcDriver").newInstance(); 		// Load the driver.
        	Connection connexion = DriverManager.getConnection("jdbc:hsqldb:file:\\\\bigfoot\\perso\\michael\\workspace\\pgnperf\\fide_rating_lists", "sa",  "");	// Get the connection.
        	// TODO: could be faster to open base as below and save (memory) content using statement.executeUpdate("SCRIPT \\\\bigfoot\\perso\\michael\\workspace\\pgnperf\\fide_rating_lists.script");
        	//        	Connection connexion = DriverManager.getConnection("jdbc:hsqldb:mem:fide_rating_lists", "sa",  "");	// Get the connection.
        	Statement statement = connexion.createStatement();
        	statement.executeUpdate("SET FILES LOG TRUE;");
        	
        	ResultSet resultat;
        	
            // CREATE TABLE.
        	resultat = statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.SYSTEM_TABLES WHERE TABLE_NAME = 'frl_20150515'");
        	if (resultat.next()) {
//        		javax.swing.JOptionPane.showMessageDialog(null, "table exist");
        		statement.executeUpdate("DROP TABLE frl_20150515");
            }
            else {
//        		javax.swing.JOptionPane.showMessageDialog(null, "table does not exist");
            }

//            statement.executeUpdate("CREATE TABLE IF NOT EXISTS frl_20150515 (" +
//            	"birth_year INT, " +
//        		"country_code VARCHAR(3), " +
//        		"elo INT, " +
//        		"id INT, " +
//        		"k INT, "	+
//        		"name VARCHAR(35), " +
//        		"sex CHAR(1), " +
//        		"title VARCHAR(3)" +
//    		")");
            statement.executeUpdate("CREATE CACHED TABLE IF NOT EXISTS frl_20150515 (" +
                	"birth_year INT, " +
            		"country_code VARCHAR(3), " +
            		"elo INT, " +
            		"id INT, " +
            		"k INT, "	+
            		"name VARCHAR(35), " +
            		"sex CHAR(1), " +
            		"title VARCHAR(3)" +
        		")");
        	PreparedStatement ps = connexion.prepareStatement("INSERT INTO frl_20150515 VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

            
            // INSERT.
        	for (FIDEPlayer fideCurrentPlayer : lstfidePlayer) {
        	    ps.setInt(1, fideCurrentPlayer.getBirthYear());
        	    ps.setString(2, fideCurrentPlayer.getCountryCode());
        	    ps.setInt(3, fideCurrentPlayer.getElo());
        	    ps.setInt(4, fideCurrentPlayer.getID());
        	    ps.setInt(5, fideCurrentPlayer.getK());
        	    ps.setString(6, fideCurrentPlayer.getName());
        	    ps.setString(7, String.valueOf(fideCurrentPlayer.getSexToChar()));
        	    ps.setString(8, fideCurrentPlayer.getTitleAbbreviation());
        	    ps.addBatch();
        	    if(++nCount % BATCH_SIZE == 0) {
        	        ps.executeBatch();
//                    System.out.println(String.valueOf(nCount) + ": " + fideCurrentPlayer.getName());
        	    }
        	}
        	ps.executeBatch(); // insert remaining records
        	ps.close();
            
            // CLOSE CONNECTION TO DATABASE.
         	statement = connexion.createStatement();
        	statement.executeQuery("SHUTDOWN");
        	statement.close();
        	connexion.close();

        }
        catch( Exception e ) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            finalize();
        }
        finally {
        }
        
      
        
     // RETURN VALUE.           
        return nReturnValue;
    	
    }

//    public FIDEPlayer findPlayer(String sPlayerFullName) {
    public FIDEPlayer getFIDEPlayer(String sPlayerFullName) {

    	// REMOVE ACCENTS AND CONVERT TO UPPERCASE.
    	sPlayerFullName = Normalizer.normalize(sPlayerFullName, Normalizer.Form.NFD);
    	sPlayerFullName = sPlayerFullName.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");    	
    	sPlayerFullName = sPlayerFullName.toUpperCase();


    	FIDEPlayer fideResult=null;
    	
    	
        try {
//        	Statement statement = mjdbcConnection.createStatement();
        	
        	
        	//TODO: multiple choice when multiple results found.
            
        	String sTitle;
        	ResultSet resultat = (mjdbcConnection.createStatement()).executeQuery(
        		"SELECT * " +
        		"  FROM frl_20150515 " +
        		" WHERE name='" + sPlayerFullName + "' " +
        		"   AND (country_code='BEL' OR country_code='FRA')"
    		);
            while(resultat.next()){
            	
            	if (resultat.getString(1)!=null) {
            		fideResult =new FIDEPlayer();
            		fideResult.setBirthYear(resultat.getInt(1));
            		fideResult.setCountryCode(resultat.getString(2));
            		fideResult.setElo(resultat.getInt(3));
            		fideResult.setID(resultat.getInt(4));
            		fideResult.setK(resultat.getInt(5));
            		fideResult.setName(resultat.getString(6));
            		if (resultat.getString(7)=="M") {
                		fideResult.setSex(Sex.MALE);
            		}
            		else if (resultat.getString(7)=="F") {
                		fideResult.setSex(Sex.FEMALE);
            		}
            		sTitle = resultat.getString(8);
            		if (sTitle.equals("  ")) {			// Java bans switching on a String.
            			fideResult.setTitle(Title.NONE);
            		} else if (sTitle.equals("c ")) {
            			fideResult.setTitle(Title.CANDIDATE_MASTER);
            		} else if (sTitle.equals("f ")) {
            			fideResult.setTitle(Title.FIDE_MASTER);
            		} else if (sTitle.equals("m ")) {
            			fideResult.setTitle(Title.INTERNATIONAL_MASTER);    			
            		} else if (sTitle.equals("g ")) {
            			fideResult.setTitle(Title.GRANDMASTER);    			
            		} else if (sTitle.equals("wc")) {
            			fideResult.setTitle(Title.WOMAN_CANDIDATE_MASTER);    			
            		} else if (sTitle.equals("wf")) {
            			fideResult.setTitle(Title.WOMAN_FIDE_MASTER);    			
            		} else if (sTitle.equals("wm")) {
            			fideResult.setTitle(Title.WOMAN_INTERNATIONAL_MASTER);    			
            		} else if (sTitle.equals("wg")) {
            			fideResult.setTitle(Title.WOMAN_GRANDMASTER);    			
            		} else {
            			fideResult.setTitle(Title.NONE);
            		}
            	}
            }

            

        }
        catch( Exception e ) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            finalize();
        }
        finally {
        }
        
      
        
     // RETURN VALUE.           
        return fideResult;
    	
    }
    

    
    
}
