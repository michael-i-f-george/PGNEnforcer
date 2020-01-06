package org.chess.pgn;

//import java.io.File;
import java.util.ArrayList;
//import java.util.List;




//import javax.xml.parsers.SAXParserFactory;
//import javax.xml.parsers.SAXParser;
//import org.xml.sax.helpers.DefaultHandler;
import javax.swing.*;
//
//import java.util.*;	// List and LinkedList.
//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;

//import org.chess.pgn.NewGamePanel.EventListActionListener;
//import org.xml.sax.helpers.DefaultHandler;

public class Main {

	
	
	/**
	 * @param argsw
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// VERSIONING.
		// PGN Enforcer 0.2.1.1 - 2015-05-15T12:00:00: output always Stage and Termination tags, FRBEKBSB, minutes combobox granularity set to one (instead of five);
		// PGN Enforcer 0.2.1 - 2015-05-15T12:00:00: running O.S. detection;
		// PGN Enforcer 0.2.0 - 2014-06-08T04:00:00: reads FIDE XML rating file;
		// PGN Enforcer 0.1.2 - 2014-05-31T01:27:00: result is output, Annotator, UTCTime and UTCDate are output, EOL accordind to OS and form use a internal representation of a game;
		// PGN Enforcer 0.1.1 - 2014-05-19T19:00:00: ?;
		// PGN Enforcer 0.1.0 - 2012-11-25T01:05:00: very first version");
		
		
		
		/********************
		 * PROGRAM ENTRANCE *
		 ********************/

		
//        PlayerDBF temp =new PlayerDBF();
//        System.out.println(String.valueOf(temp.getFRBE("George, Michaël")));
//        System.out.println(String.valueOf(temp.getFRBE("Marte, Sébastien")));
//        System.out.println(String.valueOf(temp.getFRBE("Duchnycz, Jaroslaw")));
//        temp=null;
//		if (true) { return; }

		
		
		
		
		
		
//		// CELBG results to HTML.
//		org.chess.pairtwo.PairTwoIO ptWhatEver = new org.chess.pairtwo.PairTwoIO();
//		ptWhatEver.outputResults(ptWhatEver.importResults("/home/michael/input.csv"), "/home/michael/output.htm");
//		ptWhatEver=null;
//		if (true) { return; }
	
		
//		// Auto-complete PGN file with Belgian ratings.
//		(new PlayerDBF()).completePGNFile();
		
		
//		(new HSQLDB()).convertFIDEList();
//		if (true) { return; }

		
//		System.out.println(((new HSQLDB()).findPlayer("George, Michaël")).toString());
//		if (true) { return; }		
		
//        try {
////          Thread.sleep(3 * 1000);
//
//
//			System.out.println(new java.util.Date() + " -- Main(): connect to DB");  
//			// LOAD DB (multi-threading).
////			HSQLDB mhsqlFIDERatings = new HSQLDB();
//	    	Class.forName("org.hsqldb.jdbcDriver").newInstance(); 		// Load the driver.
//	    	java.sql.Connection	mjdbcConnection = java.sql.DriverManager.getConnection("jdbc:hsqldb:file:" + "\\\\BIGFOOT\\perso\\michael\\workspace\\PGNPerf\\fide_rating_lists.script", "sa",  "");	// Get the connection.
//	    		    	
//			System.out.println(new java.util.Date() + " -- Main(): DB connected");  
//      }
//      catch (Exception ex) {
//      }
//      finally {
//      }
//		if (true) { return; }
		
		
	
		
		
//		// CONSTANTS.
		final String USER_NAME = "George, Michaël";
//		final String FIDE_RATING_FILE;
//		if (System.getProperty("os.name").startsWith("Windows")) {
//			FIDE_RATING_FILE="\\\\bigfoot\\perso\\michael\\workspace\\pgnperf\\players_list_xml.xml";
//		}
//		else {
//			FIDE_RATING_FILE = "/media/michael/workspace/PGNPerf/players_list_xml.xml";
//		}
		
		// Look and feel of the hosting OS.
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e) {
		}
		
		

		
		

		
//		// TEST: French wording of TimeControl.
//		ChessGame cgTemp=new ChessGame();
//		cgTemp.setTimeControl("40/7200:3600");
//		javax.swing.JOptionPane.showMessageDialog(null, cgTemp.getTimeControlInFrench());
//		cgTemp.setTimeControl("5400+30");
//		javax.swing.JOptionPane.showMessageDialog(null, cgTemp.getTimeControlInFrench());
//		cgTemp.setTimeControl("30/5400:3600");
//		javax.swing.JOptionPane.showMessageDialog(null, cgTemp.getTimeControlInFrench());
//		cgTemp.setTimeControl("300");
//		javax.swing.JOptionPane.showMessageDialog(null, cgTemp.getTimeControlInFrench());
//		cgTemp.setTimeControl("*180");
//		javax.swing.JOptionPane.showMessageDialog(null, cgTemp.getTimeControlInFrench());
//		cgTemp = null;
		
		
//			// LOAD DB (multi-threading).
//			HSQLDB hsqlFIDERatings = new HSQLDB();
//			System.out.println("Main(): about to start hsqldb");  
//
//			hsqlFIDERatings.start();
//			System.out.println("Main(): hsqldb started");  

		
	      	// CREATE THE WINDOW.
	        JFrame fraTags = new JFrame("PGN Enforcer 0.2.1.1 - 2015-06-06T16:22:00 - newgame.pgn");
	        fraTags.setSize(600, 600);
	        fraTags.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        // LOAD PGN GAME.
			ArrayList<PGNGame> lstpgnInputFile = (new PGNStream()).readPGNFile1c("newgame.in");
			// javax.swing.JOptionPane.showMessageDialog(null, System.getProperty("user.dir"));	// Display current dir.
			if (lstpgnInputFile.size()!=1) {
				javax.swing.JOptionPane.showMessageDialog(null, "More than one game found: keeping only the first.");
			}
			if (lstpgnInputFile.get(0).getAnnotator().equals("?")) {
				lstpgnInputFile.get(0).setAnnotator(USER_NAME);
			}
			
			
			
			
	        // FILL COMPONENTS.
//	        JComponent panTags = new NewGamePanel(lstpgnInputFile.get(0), new FIDEPlayerList(FIDE_RATING_FILE));
//	        JComponent panTags = new NewGamePanel(lstpgnInputFile.get(0), null);
	        JComponent panTags = new NewGamePanel(lstpgnInputFile.get(0), new FIDERatingList());
			
			fraTags.setContentPane(panTags);
	 
	        
	        // DISPLAY THE WINDOW.
	        fraTags.pack();
	        fraTags.setVisible(true);
		
		
	
		
//		javax.swing.JOptionPane.showMessageDialog(null,String.valueOf((new PlayerDBF()).getFRBE("GEORGE MICHAEL")));

	        
			System.out.println("Main(): end of main");  

	}
	
	

}
