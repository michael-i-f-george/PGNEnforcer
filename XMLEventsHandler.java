package org.chess.pgn;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;	// List and LinkedList.

public class XMLEventsHandler extends DefaultHandler {
	private List<Events> mlstEvent;
	private Events evtCurrentEvent;
	private StringBuffer sbBuffer;

	
//	public List<Events> getEvents(){
//		return mlstEvent;
//	}
	
//	public Events[] getEvents(){
	public List<Events> getEvents(){
		
//		Events[] objectArray = mlstEvent.toArray();
//		Events[] array = (Events[])mlstEvent.toArray(new Events[mlstEvent.size()]);
		
//		return (Events[])mlstEvent.toArray(new Events[mlstEvent.size()]);
		return mlstEvent;
	}
	
	public XMLEventsHandler() {
		super();
	}

	// Appends read chars to buffer.
	// read the inner content of a tag (i.e. not the attributes).
	public void characters(char[] ch, int start, int length) throws SAXException {
		String sLecture = new String(ch, start, length);
		if (sbBuffer != null) {
			sbBuffer.append(sLecture);
		}
	}
	
	// Beginning of the parsing.
	public void startDocument() throws SAXException {
//		javax.swing.JOptionPane.showMessageDialog(null, "startDocument()");
		// Empty.
	}
		
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		//   <event name="Interclubs francophones" start_date="2012-04-22" end_date="2012-06-24" time="14:00:00" site="" section="" rounds="4" day_of_play="Sunday" time_control="40/7200:3600" team_name_of_user="Fontaine-l'ï¿½vï¿½que ..."/>
		if (qName.equals("events")) {
			mlstEvent = new LinkedList<Events>();
		} else if (qName.equals("event")) {
			evtCurrentEvent = new Events();
			try {
				evtCurrentEvent.setName(attributes.getValue("name"));
				evtCurrentEvent.setStartDate(attributes.getValue("start_date"));
				evtCurrentEvent.setEndDate(attributes.getValue("end_date"));
				evtCurrentEvent.setTime(attributes.getValue("time"));
				evtCurrentEvent.setSite(attributes.getValue("site"));
				evtCurrentEvent.setSection(attributes.getValue("section"));
				evtCurrentEvent.setLastRound(Integer.parseInt(attributes.getValue("rounds")));
				evtCurrentEvent.setDayOfPlay(attributes.getValue("day_of_play"));
				evtCurrentEvent.setTimeControl(attributes.getValue("time_control"));
				evtCurrentEvent.setUserTeam(attributes.getValue("team_name"));
//				javax.swing.JOptionPane.showMessageDialog(null,evtCurrentEvent.getName());
			}
			catch (Exception e) {
				throw new SAXException(e);
			}
		}
		else {
			throw new SAXException("Unknown tag: " + qName + ".");
		}	// End of the else if.
	}
	
	public void endElement(String uri,  String localName, String qName) throws SAXException {
		if (qName.equals("event")) {
			mlstEvent.add(evtCurrentEvent);
			evtCurrentEvent=null;
		}
	}
	
	// End of the parsing.
	public void endDocument() throws SAXException {
//		for (Events usrTemp : mlstEvent) {
//			javax.swing.JOptionPane.showMessageDialog(null,usrTemp.getName());
//		}
	}
}
