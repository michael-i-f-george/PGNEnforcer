package org.chess.pgn;

import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
//import java.util.ArrayList;

public class NewGamePanel extends TagsPanel {



	List<Events> mlstEvent;
	
	public NewGamePanel(PGNGame pgnInternalGame) {
		super(pgnInternalGame);
		EndOfConstructor();
	}
//	public NewGamePanel(PGNGame pgnInternalGame, FIDERatingList lstFIDERating) {
//		super(pgnInternalGame, frlFIDERatings);
//		EndOfConstructor();
//	}
//	public NewGamePanel(PGNGame pgnInternalGame, FIDEPlayerList lstFIDERating) {
//		super(pgnInternalGame, lstFIDERating);
//		EndOfConstructor();
//	}
	public NewGamePanel(PGNGame pgnInternalGame, FIDERatingList lstFIDERating) {
		super(pgnInternalGame, lstFIDERating);
		EndOfConstructor();
	}
	private void EndOfConstructor() {

//		JList myList = new JList();;
		
//		SAXParserFactory fabrique = SAXParserFactory.newInstance();  
//		try {
//			SAXParser parseur = fabrique.newSAXParser();
//			File EventFile = new File("C:\\Users\\michael\\Desktop\\events.xml");
//			DefaultHandler gestionnaire = new XMLEventsHandler();
//			parseur.parse(EventFile, gestionnaire);
//			 myList = new JList();
//			 
//			 for (int nI=0; nI<lstEvent.size(); nI++) {
//				 myList.add(lstEvent.get(nI).getName(), myList);
//				 myList.add("test", myList);
//			 }
//			 myList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
//			 myList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
//			 myList.setVisibleRowCount(-1);
//			 JScrollPane listScroller = new JScrollPane(myList);
////			 listScroller.setPreferredSize(new Dimension(250, 80));
//			 
//
//			 }
//		catch(Exception e) {
//			
//		}
//		finally {
//			
//		}
//		//1. Create the frame.
//		 JFrame frame = new JFrame("FrameDemo");
//
//		 //2. Optional: What happens when the frame closes?
//		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//		 //3. Create components and put them in the frame.
//		 //...create emptyLabel...
//		 frame.getContentPane().add(myList);
//		 frame.getContentPane().add(new JButton("Button 2"));
//		 frame.getContentPane().add(new JList()));
//		 frame.getComponent(0).setLocation(50, 50);
//		 frame.getComponent(0).setEnabled(true);
//		 frame.getComponent(0).setVisible(true);
//		 frame.setSize(300, 300);
////		 frame.getContentPane().add("emptyLabel",myList);
//
//		 //4. Size the frame.
////		 frame.pack();
//
//		 //5. Show it.
//		 frame.setVisible(true);
		


		 
//		long lStartTime = System.currentTimeMillis();
//		FIDERatingList frlTest = new FIDERatingList(".\\oct12frl_k_old.txt");
//		long lFRLLoadingTime = System.currentTimeMillis()-lStartTime; 
//		
//		// TODO: to make removeFrenchAccents private or to move it to a class "utils".
//		
//		lStartTime = System.currentTimeMillis();
//		FIDEPlayer frUser = frlTest.getFIDEPlayer("George, Michaël");
//		long lTotalTime = System.currentTimeMillis()-lStartTime; 
////		javax.swing.JOptionPane.showMessageDialog(null,"FIDE rating list fast load: " + String.valueOf(lFRLLoadingTime) + " ms,\r\nrequest on FIDE rating list: " + String.valueOf(lTotalTime) + " ms,\r\n\r\n" + frUser.toString());
//		frlTest = null;
////		javax.swing.JOptionPane.showMessageDialog(null, frUser.getTitle().getFrench());
////		javax.swing.JOptionPane.showMessageDialog(null, frUser.getTitle().getAbbreviation());

		 
		
		
		

//		javax.swing.JOptionPane.showMessageDialog(null, cgaPGNFile.get(0).getAllCustomTagPairs().size());
		
		
		
		// Fill Event ComboBox with XML.
		SAXParserFactory fabrique = SAXParserFactory.newInstance();
		try {
			SAXParser parseur = fabrique.newSAXParser();
//			File EventFile = new File("C:\\Users\\michael\\Desktop\\events.xml");
			DefaultHandler gestionnaire = new XMLEventsHandler();
			parseur.parse(new File("events.xml"), gestionnaire);
			mlstEvent = ((XMLEventsHandler) gestionnaire).getEvents();
			ArrayList<String> lstTemp = new ArrayList<String>();
			for (int nI = 0; nI < mlstEvent.size(); nI++) {
//				cboEvent.addItem(mlstEvent.get(nI).getName());
				lstTemp.add(mlstEvent.get(nI).getName());
			}
//			super.setEventList(lstTemp);
	        // Create an ActionListener for the JComboBox component.
			super.setEventList(lstTemp, new EventListActionListener());
		} catch (Exception e) {
			javax.swing.JOptionPane.showMessageDialog(null, "We've got an exception.");
		}
//		cboEvent.setSelectedIndex(-1); // No item selected.
	}

	private class EventListActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            // Get the source of the component (i.e. the JComboBox).
            JComboBox comboBox = (JComboBox) event.getSource();

            // Print the selected items and the action command.
            Object selected = comboBox.getSelectedItem();
			for (int nI = 0; nI < mlstEvent.size(); nI++) {
				if (selected.equals(mlstEvent.get(nI).getName())) {
					if (mlstEvent.get(nI).getStartDate().length()!=0) {
						setEventDate(mlstEvent.get(nI).getStartDate());
						mlstEvent.get(nI).getSection();
					}
					if (mlstEvent.get(nI).getSite().length()!=0) {
						setSite(mlstEvent.get(nI).getSite());
						mlstEvent.get(nI).getSection();
					}
					if (mlstEvent.get(nI).getSection().length()!=0) {
						setSection(mlstEvent.get(nI).getSection());
					}
					if (mlstEvent.get(nI).getTime().length()!=0) {
						setTime(mlstEvent.get(nI).getTime());
					}
					if (mlstEvent.get(nI).getTimeControl().length()!=0) {
						setTimeControl(mlstEvent.get(nI).getTimeControl());
					}
					if (mlstEvent.get(nI).getUserTeam().length()!=0) {
//						setUserTeam(mlstEvent.get(nI).getSection());
					}
				}
			}
            String command = event.getActionCommand();
//            System.out.println("Action Command = " + command);

            // Detect whether the action command is "comboBoxEdited" or "comboBoxChanged"
            if ("comboBoxEdited".equals(command)) {
//                System.out.println("User has typed a string in the combo box.");
            } else if ("comboBoxChanged".equals(command)) {
//                System.out.println("User has selected an item from the combo box.");
            }
        }
    }		
	
	
}
