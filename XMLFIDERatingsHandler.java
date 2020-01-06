package org.chess.pgn;

import org.chess.pgn.FIDEPlayer.Activity;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;	// List and LinkedList.

public class XMLFIDERatingsHandler extends DefaultHandler {
//	private String msCurrentInnerContent="";
	private List<FIDEPlayer> mlstFIDERating;
	private FIDEPlayer mevtCurrentFIDERating;
	

	
	private StringBuffer sbNodeValue = new StringBuffer();

	
//	public List<FIDERatings> getFIDERatings(){
//		return mlstFIDERating;
//	}
	
//	public FIDERatings[] getFIDERatings(){
	public List<FIDEPlayer> getFIDERatings(){
		
//		FIDERatings[] objectArray = mlstFIDERating.toArray();
//		FIDERatings[] array = (FIDERatings[])mlstFIDERating.toArray(new FIDERatings[mlstFIDERating.size()]);
		
//		return (FIDERatings[])mlstFIDERating.toArray(new FIDERatings[mlstFIDERating.size()]);
		return mlstFIDERating;
	}
	
	public XMLFIDERatingsHandler() {
		super();
	}

//	// Appends read chars to buffer.
//	// read the inner content of a tag (i.e. not the attributes).
//	public void characters(char[] acString, int nStart, int nLength) throws SAXException {
////		javax.swing.JOptionPane.showMessageDialog(null, ">" + acString.toString() + "<");
////		javax.swing.JOptionPane.showMessageDialog(null, nStart);
////		javax.swing.JOptionPane.showMessageDialog(null, nLength);
//
//		msCurrentInnerContent = new String(acString, nStart, nLength);
//		
////		String sLecture = new String(ch, start, length);
//		if (sbBuffer != null) {
////			sbBuffer.append(sLecture);
//			sbBuffer.append(msCurrentInnerContent);
//			javax.swing.JOptionPane.showMessageDialog(null, sbBuffer.toString());
//		}
//		
//		
//	}

	// Appends read chars to buffer.
	// read the inner content of a tag (i.e. not the attributes).
	public void characters(char[] acString, int nStart, int nLength) throws SAXException {
		sbNodeValue.append(new String(acString, nStart, nLength));
	}
	
	
	// Beginning of the parsing.
	public void startDocument() throws SAXException {
		// Empty.
	}
		
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equals("playerslist")) {
			mlstFIDERating = new LinkedList<FIDEPlayer>();
		} else if (qName.equals("player")) {
			mevtCurrentFIDERating = new FIDEPlayer();
		}
//		else {
//			throw new SAXException("Unknown tag: " + qName + ".");
//		}	// End of the else if.
	}
	
	public void endElement(String uri,  String localName, String qName) throws SAXException {
		if (qName.equals("player")) {
//			javax.swing.JOptionPane.showMessageDialog(null, mevtCurrentFIDERating.toString_DEBUG());
			mlstFIDERating.add(mevtCurrentFIDERating);
			mevtCurrentFIDERating=null;
		} else if (qName.equals("flag")) {
			if (sbNodeValue.length()==0) {
				mevtCurrentFIDERating.setActivity(Activity.ACTIVE);
			}
			else if (sbNodeValue.toString().contains("i")) {
				mevtCurrentFIDERating.setActivity(Activity.INACTIVE);
    		}
    		else {
    			mevtCurrentFIDERating.setActivity(Activity.ACTIVE);
    		}
			sbNodeValue.setLength(0);
		} else if (qName.equals("birthday")) {
			try {
				mevtCurrentFIDERating.setBirthYear(Integer.parseInt(sbNodeValue.toString().trim()));
				sbNodeValue.setLength(0);
			}
			catch(NumberFormatException e) {
				mevtCurrentFIDERating.setBirthYear(-1);
			}
		} else if (qName.equals("country")) {
			mevtCurrentFIDERating.setCountryCode(sbNodeValue.toString().trim());
			sbNodeValue.setLength(0);
		} else if (qName.equals("rating")) {
			try {
//				javax.swing.JOptionPane.showMessageDialog(null, ">" + sbNodeValue.toString() + "<");
				mevtCurrentFIDERating.setElo(Integer.parseInt(sbNodeValue.toString().trim()));
				sbNodeValue.setLength(0);
			}
			catch(NumberFormatException e) {
				mevtCurrentFIDERating.setElo(-1);
			}

		} else if (qName.equals("games")) {
			try {
				mevtCurrentFIDERating.setGamesPlayed(Integer.parseInt(sbNodeValue.toString().trim()));
				sbNodeValue.setLength(0);
			}
			catch(NumberFormatException e) {
				mevtCurrentFIDERating.setGamesPlayed(-1);
			}
			
		} else if (qName.equals("fideid")) {
			mevtCurrentFIDERating.setID(Integer.parseInt(sbNodeValue.toString().trim()));
			sbNodeValue.setLength(0);
		} else if (qName.equals("k")) {
			try {
				mevtCurrentFIDERating.setK(Integer.parseInt(sbNodeValue.toString().trim()));
				sbNodeValue.setLength(0);
			}
			catch(NumberFormatException e) {
				mevtCurrentFIDERating.setK(-1);
			}

		} else if (qName.equals("name")) {
//			javax.swing.JOptionPane.showMessageDialog(null, ">" + msCurrentInnerContent + "<");
//			mevtCurrentFIDERating.setName(msCurrentInnerContent);
//			javax.swing.JOptionPane.showMessageDialog(null, ">" + sbNodeValue + "<");
			mevtCurrentFIDERating.setName(sbNodeValue.toString().trim());
			sbNodeValue.setLength(0);
		} else if (qName.equals("sex")) {
			if (sbNodeValue.toString().trim().equals("M")) {
				mevtCurrentFIDERating.setSex(FIDEPlayer.Sex.MALE);
			}
			else if (sbNodeValue.toString().trim().equals("F")) {
				mevtCurrentFIDERating.setSex(FIDEPlayer.Sex.FEMALE);
			}
			else {
				mevtCurrentFIDERating.setSex(FIDEPlayer.Sex.UNKNOWN);
			}
			sbNodeValue.setLength(0);
		} else if (qName.equals("title")) {
			mevtCurrentFIDERating.setTitle(sbNodeValue.toString().trim());
			sbNodeValue.setLength(0);
		} else if (qName.equals("w_title")) {
		} else if (qName.equals("o_title")) {
		} else if (qName.equals("rapid_rating")) {
		} else if (qName.equals("rapid_games")) {
		} else if (qName.equals("rapid_k")) {
		} else if (qName.equals("blitz_rating")) {
		} else if (qName.equals("blitz_games")) {
		} else if (qName.equals("blitz_k")) {

			
//			try {
//				evtCurrentFIDERating.setName(attributes.getValue("name"));
//				evtCurrentFIDERating.setStartDate(attributes.getValue("start_date"));
//				evtCurrentFIDERating.setEndDate(attributes.getValue("end_date"));
//				evtCurrentFIDERating.setTime(attributes.getValue("time"));
//				evtCurrentFIDERating.setSite(attributes.getValue("site"));
//				evtCurrentFIDERating.setSection(attributes.getValue("section"));
//				evtCurrentFIDERating.setLastRound(Integer.parseInt(attributes.getValue("rounds")));
//				evtCurrentFIDERating.setDayOfPlay(attributes.getValue("day_of_play"));
//				evtCurrentFIDERating.setTimeControl(attributes.getValue("time_control"));
//				evtCurrentFIDERating.setUserTeam(attributes.getValue("team_name"));
////				javax.swing.JOptionPane.showMessageDialog(null,evtCurrentFIDERating.getName());
//			}
//			catch (Exception e) {
//				throw new SAXException(e);
//			}

		}
	}
	
	// End of the parsing.
	public void endDocument() throws SAXException {
//		for (FIDERatings usrTemp : mlstFIDERating) {
//			javax.swing.JOptionPane.showMessageDialog(null,usrTemp.getName());
//		}
	}
}
