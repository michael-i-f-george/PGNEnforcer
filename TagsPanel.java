package org.chess.pgn;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

//import javax.xml.parsers.SAXParserFactory;
//import javax.xml.parsers.SAXParser;
import org.chess.pgn.FIDEPlayer.Title;




import java.sql.Connection;
import java.sql.DriverManager;
//import org.xml.sax.helpers.DefaultHandler;
//import java.io.File;
import java.text.*;
import java.util.*; // List and LinkedList.

//import net.sourceforge.jdatepicker.*;
import com.toedter.calendar.JDateChooser;

import java.beans.PropertyChangeEvent;		// Needed by the JDateChooser.
import java.beans.PropertyChangeListener;	// Needed by the JDateChooser.

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.List;

import org.jdesktop.swingx.autocomplete.*;


public class TagsPanel extends JPanel implements ActionListener, DocumentListener, FocusListener, PropertyChangeListener {

	// MEMBER VARIABLES
//	private FIDERatingList mfrlFIDERatings = new FIDERatingList("may12frl_k.txt");
	private FIDERatingList mfrlFIDERatings = null;
//	private FIDEPlayerList mlstFIDERating = null;
//	private FIDEPlayer mfpWhite = null;
//	private FIDEPlayer mfpBlack = null;

	
//	private Connection mjdbcConnection=null;
//	private final String DATABASE_PATH ="\\\\bigfoot\\perso\\michael\\workspace\\pgnperf\\fide_rating_lists";
	HSQLDB mhsqlFIDERatings;

	// TODO: to grey unmodified field (make spoting easier for user).
	
	// Visual components.
	// TODO: initialize variables in a reset() (reusable) method, instead of here.
	private JComboBox mcboEvent;
//	private JTextField mtxtSite;
	private JComboBox mcboSite;
	private JTextField mtxtDate = new JTextField("????.??.??", 10);
//	private JSpinner mspiRound;
	private JComboBox mcboRound = new JComboBox(new String[]{"?", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"} );
	private JTextField mtxtWhite = new JTextField("?", 15);
	private JTextField mtxtBlack = new JTextField("?", 15);
	private JRadioButton mradResultUndefined = new JRadioButton("*");
	private JRadioButton mradResultWhiteWin = new JRadioButton("1-0");
	private JRadioButton mradResultDraw = new JRadioButton("½-½");
	private JRadioButton mradResultBlackWin = new JRadioButton("0-1");	// TODO: !!! to append result to move section.
	private JTextField mtxtAnnotator = new JTextField("?", 15);
	private JTextField mtxtBlackElo = new JTextField("?", 6);
	private JButton mbtnBlackElo;		// Buttons are made global to respond to events.
	private JTextField mtxtBlackNA = new JTextField("?", 15);
	private JComboBox mcboBlackTitle;
	private JButton mbtnBlackTitle;
	private JComboBox mcboBlackType = new JComboBox(new String[]{"human", "program", "?"});
	private JComboBox mcboBoard = new JComboBox(new String[]{"?", "-", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"} );
	private JTextField mtxtECO = new JTextField("?", 6);
	private JTextField mtxtEventDate = new JTextField("????.??.??", 10);
	private JTextField mtxtEventSponsor = new JTextField("?", 15);
	private JTextField mtxtFEN = new JTextField("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 15);
	private JComboBox mcboMode = new JComboBox(new String[]{"OTB", "PM", "EM", "ICS", "TC", "?"});
	private JTextField mtxtNIC = new JTextField("?", 10);
	private JTextField mtxtOpening = new JTextField("?", 15);
	private JTextField mtxtPlyCount = new JTextField("0", 6);
	private JTextField mtxtSection = new JTextField("?", 15);
	private JTextField mtxtStage = new JTextField("?", 15);
	private JTextField mtxtSubVariation = new JTextField("?", 15);
	private JComboBox mcboTermination = new JComboBox(new String[]{"abandoned", "adjudication", "death",
			"emergency", "normal", "rules infraction", "time forfeit", "unterminated"} );
//	private JSpinner mspiTime;
	JComboBox mcboTimeHour = new JComboBox(new String[]{"??", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"} );
	JComboBox mcboTimeMinute = new JComboBox(new String[]{"??", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"} );
	JComboBox mcboTimeSecond = new JComboBox(new String[]{"??", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"} );
	private JTextField mtxtTimeControl = new JTextField("?", 15);	// TODO: to implement a TimeControl panel.
	private JTextField mtxtVariation = new JTextField("?", 15);
	private JTextField mtxtWhiteElo = new JTextField("?", 6);
	private JButton mbtnWhiteElo;
	private JTextField mtxtWhiteNA = new JTextField("?", 15);
	private JComboBox mcboWhiteTitle;
	private JButton mbtnWhiteTitle;
	private JComboBox mcboWhiteType = new JComboBox(new String[]{"human", "program", "?"});
//	private String msMovetext = "*";
	private JButton mbtnExport;
	private JButton mbtnCancel;

	// TODO: to display move section.
	
	private JDateChooser dchDate;
	private JDateChooser dchEventDate;
	
	private PGNGame mpgnInternalGame;
	
//	private JTextField mtxtTime;
	
	public TagsPanel(PGNGame pgnInternalGame) { // Constructor.

		super(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		setLayout(new GridBagLayout());
		GridBagConstraints gbaConstraints = new GridBagConstraints();
		int nRow = 0;
		int nColumn = 0;

		mpgnInternalGame = pgnInternalGame;
		
		// 7TR: EVENT.
		JLabel lblEvent = new JLabel("Event: ");
		gbaConstraints.anchor = GridBagConstraints.WEST;
		mcboEvent = new JComboBox();
//		mcboEvent.addActionListener(this);
//		gbaConstraints.gridx = nColumn + 1;
//		gbaConstraints.gridy = nRow++;
//		add(mcboEvent, gbaConstraints);
//		// Fill site ComboBox with XML.
//		SAXParserFactory fabrique = SAXParserFactory.newInstance();
//		try {
//			SAXParser parseur = fabrique.newSAXParser();
//			File EventFile = new File("C:\\Users\\michael\\Desktop\\events.xml");
//			DefaultHandler gestionnaire = new XMLEventsHandler();
//			parseur.parse(EventFile, gestionnaire);
//			mlstEvent = ((XMLEventsHandler) gestionnaire).getEvents();
//			for (int nI = 0; nI < mlstEvent.size(); nI++) {
//				cboEvent.addItem(mlstEvent.get(nI).getName());
//			}
//		} catch (Exception e) {
//			javax.swing.JOptionPane.showMessageDialog(null,
//					"We've got an exception.");
//		}
//		cboEvent.setSelectedIndex(-1); // No item selected.

		// 7TR: SITE.
		JLabel lblSite = new JLabel("Site: ");
//		mtxtSite = new JTextField(15);
//		mcboSite = new JComboBox();
		
		
//    	ArrayList<String> lstTemp = retrieveTownList("BEL"); 
//		String[] elements = new String[lstTemp.size()];
//		lstTemp.toArray(elements);
////		elements = (String[]) (retrieveTownList("BEL")).toArray();
//		lstTemp = null;
//		mcboSite = new JComboBox(elements);
		mcboSite = new JComboBox(retrieveTownList("BEL").toArray());
		mcboSite.insertItemAt("?", 0);
		mcboSite.setSelectedIndex(0);
		AutoCompleteDecorator.decorate(mcboSite);
//		System.out.println("Is editable - " + mcboSite.isEditable() + ". Surprise!");
//		SwingUtilities.invokeLater(new Runnable() {  
//		    public void run() {  
////				Object[] elements = new Object[] { "Ester", "Jordi", "Jordina", "Jorge", "Sergi" };
////				String[] elements = new String[] { "Ester", "Jordi", "Jordina", "Jorge", "Sergi" };
//				
//		    	ArrayList<String> lsAnnottTemp = retrieveTownList("BEL"); 
//				String[] elements = new String[lstTemp.size()];
//				lstTemp.toArray(elements);
//				lstTemp = null;
//				if (elements != null) {
//					AutoCompleteSupport<String> support = AutoCompleteSupport.install(mcboSite, GlazedLists.eventListOf(elements));
//					support.setStrict(false); // empÃªche la sÃ©lection d'un objet inexistant dans la liste
//			        support.setCorrectsCase(true); // Accepte les caractÃ¨res min/maj dans la saisie
//				}
//		    }  
//		}); 

		// IAMHERE
		
		
		// 7TR: DATE.
//		JLabel lblDate = new JLabel("Date: ");
//		mtoeDate = new com.toedter.calendar.JDateChooser();
//		mtoeDate.getJCalendar().setTodayButtonVisible(true);
//		mtoeDate.getJCalendar().setNullDateButtonVisible(true);
//		mtoeDate.getJCalendar().setDecorationBordersVisible(false);
//		mtoeDate.getJCalendar().getDayChooser().setDayBordersVisible(false);
//		mtoeDate.getJCalendar().setWeekOfYearVisible(false);
////		mtoeDate.setDateFormatString("EEEE, dd MMMM yyyy");
//		mtoeDate.setDateFormatString("  yyyy.MM.dd");
		// JLabel
		JLabel lblDate = new JLabel("Date: ");
		// JTextField
//		mtxtDate = new JTextField(6);
		mtxtDate.addFocusListener(this);
		// JDateChooser
		dchDate = new com.toedter.calendar.JDateChooser();
//		JDateChooser dchDate = new com.toedter.calendar.JDateChooser();
		dchDate.getJCalendar().setTodayButtonVisible(true);
		dchDate.getJCalendar().setNullDateButtonVisible(true);
		dchDate.getJCalendar().setDecorationBordersVisible(false);
		dchDate.getJCalendar().getDayChooser().setDayBordersVisible(false);
		dchDate.getJCalendar().setWeekOfYearVisible(false);
		// TODO: rather than setting it invisible, try to remove the JDateChooser.
		dchDate.setVisible(false);
		dchDate.getDateEditor().addPropertyChangeListener(this);
		this.add(dchDate);
		// JButton
		JButton btnDate = dchDate.getCalendarButton();
		
		// 7TR: ROUND.
//		JLabel lblRound = new JLabel("Round: ");
//		mspiRound = new JSpinner();
//		JSpinner.NumberEditor spiEditor = new JSpinner.NumberEditor(mspiRound);
//		mspiRound.setEditor(spiEditor);
//		spiEditor.getModel().setMinimum(1);
//		spiEditor.getModel().setMaximum(100);
//		// spinnerEditor.getModel().setStepSize(5); // Pas.
//		spiEditor.getModel().setValue(1);
		mcboRound.setEditable(true);
		((JTextField)mcboRound.getEditor().getEditorComponent()).setColumns(2);	// Correct width of the JComboBox. 

		// 7TR: WHITE.
//		JLabel lblWhite = new JLabel("White: ");
//		mtxtWhite.addActionListener(this);
		mtxtWhite.addFocusListener(this);
		mtxtWhite.getDocument().addDocumentListener(this);

		// 7TR: BLACK.
//		JLabel lblBlack = new JLabel("Black: ");
		mtxtBlack.addFocusListener(this);

		// 7TR: RESULT
		mradResultUndefined.setMnemonic(KeyEvent.VK_B);
		mradResultUndefined.setActionCommand("*");
		mradResultUndefined.setSelected(true);
		mradResultWhiteWin.setMnemonic(KeyEvent.VK_B);
		mradResultWhiteWin.setActionCommand("*");
		mradResultDraw.setMnemonic(KeyEvent.VK_B);
		mradResultDraw.setActionCommand("*");
		mradResultBlackWin.setMnemonic(KeyEvent.VK_B);
		mradResultBlackWin.setActionCommand("*");
		// Group radio buttons.
		ButtonGroup grpResult = new ButtonGroup();
		grpResult.add(mradResultUndefined);
		grpResult.add(mradResultWhiteWin);
		grpResult.add(mradResultDraw);
		grpResult.add(mradResultBlackWin);
		// Register listener for radio buttons.
//		radUndefined.addActionListener(this);
//		radWhiteWins.addActionListener(this);
//		radDraw.addActionListener(this);
//		radBlackWins.addActionListener(this);
		// Add radio buttons to form.

		
		// 7TR: Grouping in bordered panel.
		JPanel panSevenTagRoster = new JPanel();
		panSevenTagRoster.setBorder(BorderFactory.createTitledBorder("Seven Tag Roster"));
//		gbaConstraints.fill = GridBagConstraints.HORIZONTAL;
//		gbaConstraints.fill = GridBagConstraints.NONE;
		int nSevenTagRosterRow = 0;
		panSevenTagRoster.setLayout(new GridBagLayout());
		GridBagConstraints gbaSevenTagRosterConstraints = new GridBagConstraints();
		gbaSevenTagRosterConstraints.anchor = GridBagConstraints.WEST;
		// 7TR: Event.
		gbaSevenTagRosterConstraints.gridx = 0;
		gbaSevenTagRosterConstraints.gridy = nSevenTagRosterRow;
		panSevenTagRoster.add(lblEvent, gbaSevenTagRosterConstraints);
		gbaSevenTagRosterConstraints.gridx = 1;
		gbaSevenTagRosterConstraints.gridy = nSevenTagRosterRow++;
		gbaSevenTagRosterConstraints.gridwidth = 4;
		panSevenTagRoster.add(mcboEvent, gbaSevenTagRosterConstraints);
		// 7TR: Site.
		gbaSevenTagRosterConstraints.gridwidth = 1;
		gbaSevenTagRosterConstraints.gridx = 0;
		gbaSevenTagRosterConstraints.gridy = nSevenTagRosterRow;
		panSevenTagRoster.add(lblSite, gbaSevenTagRosterConstraints);
		gbaSevenTagRosterConstraints.gridx = 1;
		gbaSevenTagRosterConstraints.gridy = nSevenTagRosterRow++;
		gbaSevenTagRosterConstraints.gridwidth = 4;
//		panSevenTagRoster.add(mtxtSite, gbaSevenTagRosterConstraints);
		panSevenTagRoster.add(mcboSite, gbaSevenTagRosterConstraints);
		// 7TR: Date 
		gbaSevenTagRosterConstraints.gridwidth = 1;
		gbaSevenTagRosterConstraints.gridx = 0;
		gbaSevenTagRosterConstraints.gridy = nSevenTagRosterRow;
		panSevenTagRoster.add(lblDate, gbaSevenTagRosterConstraints);
		gbaSevenTagRosterConstraints.gridx = 1;
		gbaSevenTagRosterConstraints.gridy = nSevenTagRosterRow;
//		gbaSevenTagRosterConstraints.gridwidth = 4;
//		panSevenTagRoster.add(mtoeDate, gbaSevenTagRosterConstraints);
		gbaSevenTagRosterConstraints.gridwidth = 4;
		panSevenTagRoster.add(mtxtDate, gbaSevenTagRosterConstraints);
		gbaSevenTagRosterConstraints.gridwidth = 1;
		
//		gbaSevenTagRosterConstraints.gridwidth = 1;
		gbaSevenTagRosterConstraints.gridx = 5;
		gbaSevenTagRosterConstraints.gridy = nSevenTagRosterRow++;
//		panSevenTagRoster.add(mtoeDate, gbaSevenTagRosterConstraints);
		panSevenTagRoster.add(btnDate, gbaSevenTagRosterConstraints);
		// 7TR: Round.
		gbaSevenTagRosterConstraints.gridx = 0;
		gbaSevenTagRosterConstraints.gridy = nSevenTagRosterRow;
		panSevenTagRoster.add(new JLabel("Round: "), gbaSevenTagRosterConstraints);
		gbaSevenTagRosterConstraints.gridx = 1;
		gbaSevenTagRosterConstraints.gridy = nSevenTagRosterRow++;
		gbaSevenTagRosterConstraints.gridwidth = 4;
//		panSevenTagRoster.add(mspiRound, gbaSevenTagRosterConstraints);
		panSevenTagRoster.add(mcboRound, gbaSevenTagRosterConstraints);
		// 7TR: White.
		gbaSevenTagRosterConstraints.gridwidth = 1;
		gbaSevenTagRosterConstraints.gridx = 0;
		gbaSevenTagRosterConstraints.gridy = nSevenTagRosterRow;
		panSevenTagRoster.add(new JLabel("White: "), gbaSevenTagRosterConstraints);
		gbaSevenTagRosterConstraints.gridx = 1;
		gbaSevenTagRosterConstraints.gridy = nSevenTagRosterRow++;
		gbaSevenTagRosterConstraints.gridwidth = 4;
		panSevenTagRoster.add(mtxtWhite, gbaSevenTagRosterConstraints);
		// 7TR: Black
		gbaSevenTagRosterConstraints.gridwidth = 1;
		gbaSevenTagRosterConstraints.gridx = 0;
		gbaSevenTagRosterConstraints.gridy = nSevenTagRosterRow;
		panSevenTagRoster.add(new JLabel("Black: "), gbaSevenTagRosterConstraints);
		gbaSevenTagRosterConstraints.gridx = 1;
		gbaSevenTagRosterConstraints.gridy = nSevenTagRosterRow++;
		gbaSevenTagRosterConstraints.gridwidth = 4;
		panSevenTagRoster.add(mtxtBlack, gbaSevenTagRosterConstraints);
		// 7TR: Result.
		gbaSevenTagRosterConstraints.gridwidth = 1;
		gbaSevenTagRosterConstraints.gridx = 0;
		gbaSevenTagRosterConstraints.gridy = nSevenTagRosterRow;
		panSevenTagRoster.add(new JLabel("Result: "), gbaSevenTagRosterConstraints);
		gbaSevenTagRosterConstraints.gridx = 1;
		gbaSevenTagRosterConstraints.gridy = nSevenTagRosterRow;
		panSevenTagRoster.add(mradResultUndefined, gbaSevenTagRosterConstraints);
		gbaSevenTagRosterConstraints.gridx = 2;
		gbaSevenTagRosterConstraints.gridy = nSevenTagRosterRow;
		panSevenTagRoster.add(mradResultWhiteWin, gbaSevenTagRosterConstraints);
		gbaSevenTagRosterConstraints.gridx = 3;
		gbaSevenTagRosterConstraints.gridy = nSevenTagRosterRow;
		panSevenTagRoster.add(mradResultDraw, gbaSevenTagRosterConstraints);
		gbaSevenTagRosterConstraints.gridx = 4;
		gbaSevenTagRosterConstraints.gridy = nSevenTagRosterRow;
		panSevenTagRoster.add(mradResultBlackWin, gbaSevenTagRosterConstraints);
		// Add 7TR to pannel.
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.gridwidth = 3;
		gbaConstraints.gridheight = 9;
		add(panSevenTagRoster, gbaConstraints);
		gbaConstraints.gridwidth = 1;
		gbaConstraints.gridheight = 1;
		nRow+=7;	// Height of the "Seven Tag Roster" panel.
		
		// TIME.

		// GregorianCalendar calTest1 = new GregorianCalendar(2012,
		// Calendar.MAY, 26, 14, 00, 00);

		// Date datInitTime = null;
		// Date datMinTime = null;
		// Date datMaxTime = null;
		// try {
		// datInitTime = (new
		// SimpleDateFormat("dd/MM/yyyy hh:mm")).parse("26/05/2012 14:00");
		// datMinTime = (new
		// SimpleDateFormat("dd/MM/yyyy hh:mm")).parse("26/05/2012 00:00");
		// datMaxTime = (new
		// SimpleDateFormat("dd/MM/yyyy hh:mm")).parse("26/05/2012 23:59");
		// }
		// catch (ParseException e) {
		// e.printStackTrace();
		// }
		// calDate.add(Calendar.YEAR, 200);
		//
		// SpinnerDateModel modTime = new SpinnerDateModel(datInitTime,
		// datMinTime, datMaxTime, 1);
		// JSpinner spiTime = new JSpinner(modTime);
		// spiTime.setEditor(new JSpinner.DateEditor(spiTime, "HH:mm"));
		// gbaConstraints.gridx = nColumn + 1;
		// gbaConstraints.gridy = 10;
		// add(spiTime, gbaConstraints);

		// 1)
//		Date datInitialTime = (new GregorianCalendar(2012, Calendar.MAY, 26, 14, 00, 00)).getTime();
//		Calendar calInitialTime = new GregorianCalendar(2012, Calendar.MAY, 26, 14, 00, 00);
//		calInitialTime = Calendar.getInstance();
//		datInitialTime = calInitialTime.getTime();
//		calInitialTime.add(Calendar.DAY_OF_MONTH, 1);
//		Date datMaximalTime = calInitialTime.getTime();
//		calInitialTime.add(Calendar.DAY_OF_MONTH, -2);
//		Date datMinimalTime = calInitialTime.getTime();
//		SpinnerDateModel sm = new SpinnerDateModel(datInitialTime, datMinimalTime, datMaximalTime, Calendar.MINUTE);
//		JSpinner spinner = new JSpinner(sm);
//		JSpinner.DateEditor de = new JSpinner.DateEditor(spinner, "HH:mm");
//		spinner.setEditor(de);
//		gbaConstraints.gridx = nColumn + 1;
//		gbaConstraints.gridy = nRow++;
//		add(spinner, gbaConstraints);


		// 2)
		// GregorianCalendar A = new GregorianCalendar();
		// A.setTimeInMillis(0);
		// GregorianCalendar B = A;
		// B.add(Calendar.HOUR_OF_DAY, 1);
		// GregorianCalendar C=A;
		// C.add(Calendar.HOUR_OF_DAY, 3);
		// // javax.swing.JOptionPane.showMessageDialog(null,(new
		// SimpleDateFormat("dd/MM/yyyy HH:mm")).format(A.getTime()));
		// // javax.swing.JOptionPane.showMessageDialog(null,(new
		// SimpleDateFormat("dd/MM/yyyy HH:mm")).format(B.getTime()));
		// // javax.swing.JOptionPane.showMessageDialog(null,(new
		// SimpleDateFormat("dd/MM/yyyy HH:mm")).format(C.getTime()));
		// // C.add(Calendar.SECOND, 23 * 3600 + 59 * 60 + 59);
		// SpinnerDateModel MyModel = new SpinnerDateModel(B.getTime(),
		// A.getTime(), C.getTime(), Calendar.MINUTE);
		// JSpinner MySpinner = new JSpinner(MyModel);
		// JSpinner.DateEditor MyEditor = new JSpinner.DateEditor(MySpinner,
		// "HH:mm");
		// spinner.setEditor(MyEditor);
		// gbaConstraints.gridx = nColumn + 1;
		// gbaConstraints.gridy = 14;
		// add(MySpinner, gbaConstraints);

		// Calendar start = GregorianCalendar.getInstance();
		// Calendar end = GregorianCalendar.getInstance();
		// start.clear();
		// end.clear();
		// start.set(Calendar.YEAR, 2010);
		// end.set(Calendar.YEAR, 2010);
		// end.add(Calendar.HOUR_OF_DAY, 12);
		// SpinnerDateModel m1 = new SpinnerDateModel(start.getTime(),
		// start.getTime(), end.getTime(), Calendar.MILLISECOND);
		// SpinnerDateModel m2 = new SpinnerDateModel(start.getTime(),
		// start.getTime(), end.getTime(), Calendar.MILLISECOND);
		// JSpinner workingSpinner = new JSpinner(m1);
		// workingSpinner.setEditor(new JSpinner.DateEditor(workingSpinner,
		// "yyyy HH:mm:ss.SSS"));
		// JSpinner notWorkingSpinner = new JSpinner(m2);
		// notWorkingSpinner.setEditor(new
		// JSpinner.DateEditor(notWorkingSpinner, "HH:mm:ss.SSS"));
		// gbaConstraints.gridx = nColumn + 1;
		// gbaConstraints.gridy = 12;
		// add(workingSpinner, gbaConstraints);
		// gbaConstraints.gridx = nColumn + 1;
		// gbaConstraints.gridy = 13;
		// add(notWorkingSpinner, gbaConstraints);

		
		nRow++;	// Avoid being hidden by 7TR frame border. 
		// ANNOTATOR.
		JLabel lblAnnotator = new JLabel("Annotator: ");
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(lblAnnotator, gbaConstraints);
		mtxtAnnotator.addFocusListener(this);
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.gridwidth = 2;
		gbaConstraints.anchor = GridBagConstraints.WEST;
		add(mtxtAnnotator, gbaConstraints);
		gbaConstraints.gridwidth = 1;
		
		// BLACKELO.
		// JLabel.
		JLabel lblBlackElo = new JLabel("BlackElo: ");
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(lblBlackElo, gbaConstraints);
		// JTextField.
		mtxtBlackElo.addFocusListener(this);
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow;
		gbaConstraints.anchor = GridBagConstraints.WEST;
		add(mtxtBlackElo, gbaConstraints);
		// JButton.
		mbtnBlackElo = new JButton("Find");
//		mbtnBlackElo.setEnabled(false);
		mbtnBlackElo.addActionListener(this);
		gbaConstraints.gridx = nColumn + 2;
		gbaConstraints.gridy = nRow++;
		add(mbtnBlackElo, gbaConstraints);

		// BLACKNA.
		// JLabel
//		JLabel lblBlackNA = new JLabel("BlackNA: ");
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(new JLabel("BlackNA: "), gbaConstraints);
		// JTextField
//		mtxtBlackNA = new JTextField(15);
		mtxtBlackNA.addFocusListener(this);
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.gridwidth = 2;
		gbaConstraints.anchor = GridBagConstraints.WEST;
		add(mtxtBlackNA, gbaConstraints);
		gbaConstraints.gridwidth = 1;
		
		// BLACKTITLE.
		// JLabel.
		JLabel lblBlackTitle = new JLabel("BlackTitle: ");
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(lblBlackTitle, gbaConstraints);
		// JComboBox.
		mcboBlackTitle = new JComboBox();
//		cboBlackTitle.addActionListener(this);
		Title[] titTemp = Title.values();
		for (int nI = 0; nI < titTemp.length; nI++) {
//			cboBlackTitle.addItem(titTemp[nI].getFrench());
			mcboBlackTitle.addItem(titTemp[nI].getAbbreviation());
		}
		titTemp = null;
		mcboBlackTitle.setSelectedIndex(0); // No item selected.
		mcboBlackTitle.setEditable(true);
		((JTextField)mcboBlackTitle.getEditor().getEditorComponent()).setColumns(2);	// Correct width of the JComboBox. 
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow;
		gbaConstraints.gridwidth = 2;
		add(mcboBlackTitle, gbaConstraints);
		gbaConstraints.gridwidth = 1;
		// JButton.
		mbtnBlackTitle = new JButton("Find");
//		mbtnBlackTitle.setEnabled(false);
		mbtnBlackTitle.addActionListener(this);
		gbaConstraints.gridx = nColumn + 2;
		gbaConstraints.gridy = nRow++;
		add(mbtnBlackTitle, gbaConstraints);

		// BLACKTYPE.
		// JLabel.
		JLabel lblBlackType = new JLabel("BlackType: ");
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(lblBlackType, gbaConstraints);
		// JCombobox.
		mcboBlackType.setEditable(true);
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.gridwidth = 2;
		add(mcboBlackType, gbaConstraints);
		gbaConstraints.gridwidth = 1;
		
		// BOARD.
		// JLabel
//		JLabel lblBoard = new JLabel("Board: ");
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(new JLabel("Board: "), gbaConstraints);
		// JTextField
		mcboBoard.setEditable(true);
		mcboBoard.addFocusListener(this);
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.anchor = GridBagConstraints.WEST;
		add(mcboBoard, gbaConstraints);

		// ECO.
		// JLabel
//		JLabel lblECO = new JLabel("ECO: ");
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(new JLabel("ECO: "), gbaConstraints);
		// JTextField
		mtxtECO.addFocusListener(this);
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.anchor = GridBagConstraints.WEST;
		add(mtxtECO, gbaConstraints);

		// EVENTDATE.
		// JLabel
//		JLabel deleteme = new JLabel("EventDate: ");
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(new JLabel("EventDate: "), gbaConstraints);
		// JTextField
		mtxtEventDate.addFocusListener(this);
		gbaConstraints.gridx = nColumn+1;
		gbaConstraints.gridy = nRow;
		add(mtxtEventDate, gbaConstraints);
		// JDateChooser
		dchEventDate = new com.toedter.calendar.JDateChooser();
//		JDateChooser dchEventDate = new com.toedter.calendar.JDateChooser();
		dchEventDate.getJCalendar().setTodayButtonVisible(true);
		dchEventDate.getJCalendar().setNullDateButtonVisible(true);
		dchEventDate.getJCalendar().setDecorationBordersVisible(false);
		dchEventDate.getJCalendar().getDayChooser().setDayBordersVisible(false);
		dchEventDate.getJCalendar().setWeekOfYearVisible(false);
		// TODO: rather than setting it invisible, try to remove the JDateChooser.
		dchEventDate.setVisible(false);
		dchEventDate.getDateEditor().addPropertyChangeListener(this);
		this.add(dchEventDate);
		// JButton
		JButton btnEventDate = dchEventDate.getCalendarButton();
		gbaConstraints.gridx = nColumn+2;
		gbaConstraints.gridy = nRow++;
		add(btnEventDate, gbaConstraints);
		
		// EVENTSPONSOR.
		// JLabel
//		JLabel lblEventSponsor = new JLabel("EventSponsor: ");
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(new JLabel("EventSponsor: "), gbaConstraints);
		// JTextField
//		mtxtEventSponsor = new JTextField(15);
		mtxtEventSponsor.addFocusListener(this);
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.anchor = GridBagConstraints.WEST;
		gbaConstraints.gridwidth = 2;
		add(mtxtEventSponsor, gbaConstraints);
		gbaConstraints.gridwidth = 1;
		
		// FEN.
		// JLabel
//		JLabel lblFEN = new JLabel("FEN: ");
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(new JLabel("FEN: "), gbaConstraints);
		// JTextField
		mtxtFEN.setEditable(false);
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.anchor = GridBagConstraints.WEST;
		gbaConstraints.gridwidth = 2;
		add(mtxtFEN, gbaConstraints);
		gbaConstraints.gridwidth = 1;
		
		// MODE.
		// JLabel
//		JLabel lblMode = new JLabel("Mode: ");
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(new JLabel("Mode: "), gbaConstraints);
		// JComboBox.
		mcboMode.setEditable(true);
		((JTextField)mcboMode.getEditor().getEditorComponent()).setColumns(2);	// Correct width of the JComboBox. 
//		cboMode.addActionListener(this);
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.gridwidth = 2;
		add(mcboMode, gbaConstraints);
		gbaConstraints.gridwidth = 1;
//		cboMode.setSelectedIndex(0);

		// NIC.
		// JLabel
		JLabel lblNIC = new JLabel("NIC: ");
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(lblNIC, gbaConstraints);
		// JTextField
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.anchor = GridBagConstraints.WEST;
		gbaConstraints.gridwidth = 2;
		add(mtxtNIC, gbaConstraints);
		gbaConstraints.gridwidth = 1;

		// OPENING.
		// JLabel
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(new JLabel("Opening: "), gbaConstraints);
		// JTextField
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.anchor = GridBagConstraints.WEST;
		gbaConstraints.gridwidth = 2;
		add(mtxtOpening, gbaConstraints);
		gbaConstraints.gridwidth = 1;

		// PLYCOUNT.
		// JLabel.
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(new JLabel("PlyCount: "), gbaConstraints);
		// JTextField.
		mtxtPlyCount.setEditable(false);
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.anchor = GridBagConstraints.WEST;
		add(mtxtPlyCount, gbaConstraints);


		
		// EXPORT BUTTON.
		mbtnExport = new JButton("Export");
		mbtnExport.addActionListener(this);
		gbaConstraints.gridx = nColumn + 2;
		gbaConstraints.gridy = nRow;
		gbaConstraints.anchor = GridBagConstraints.EAST;
		add(mbtnExport, gbaConstraints);
		
		// CANCEL BUTTON.
		mbtnCancel = new JButton("Cancel");
		mbtnCancel.addActionListener(this);
		gbaConstraints.gridx = nColumn + 3;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.anchor = GridBagConstraints.WEST;
		add(mbtnCancel, gbaConstraints);

		
		
		
		// COLUMN BREAK.
		nColumn+=3;
		nRow = 0;

		
		
		// SECTION.
		// JLabel
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(new JLabel("Section: "), gbaConstraints);
		// JTextField
		mtxtSection.addFocusListener(this);
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.anchor = GridBagConstraints.WEST;
		gbaConstraints.gridwidth = 2;
		add(mtxtSection, gbaConstraints);
		gbaConstraints.gridwidth = 1;
		
		// SETUP.
		// JLabel.
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(new JLabel("SetUp: "), gbaConstraints);
		// Radio buttons.
		JRadioButton rad0 = new JRadioButton("0");
		rad0.setMnemonic(KeyEvent.VK_B);
		rad0.setActionCommand("0");
		rad0.setSelected(true);
		JRadioButton rad1 = new JRadioButton("1");
		rad1.setMnemonic(KeyEvent.VK_B);
		rad1.setActionCommand("1");
		// Group radio buttons.
		ButtonGroup grpSetUp = new ButtonGroup();
		grpSetUp.add(rad0);
		grpSetUp.add(rad1);
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow;
		gbaConstraints.anchor = GridBagConstraints.WEST;
		add(rad0, gbaConstraints);
		gbaConstraints.gridx = nColumn + 2;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.anchor = GridBagConstraints.WEST;
		add(rad1, gbaConstraints);
		
		// STAGE.
		// JLabel
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(new JLabel("Stage: "), gbaConstraints);
		// JTextField
		mtxtStage.addFocusListener(this);
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.anchor = GridBagConstraints.WEST;
		gbaConstraints.gridwidth = 2;
		add(mtxtStage, gbaConstraints);
		gbaConstraints.gridwidth = 1;
		
		// SUBVARIATION.
		// JLabel
//		JLabel lblSubVariation = new JLabel("SubVariation: ");
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(new JLabel("SubVariation: "), gbaConstraints);
		// JTextField
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.anchor = GridBagConstraints.WEST;
		gbaConstraints.gridwidth = 2;
		add(mtxtSubVariation, gbaConstraints);
		gbaConstraints.gridwidth = 1;
		
		// TERMINATION.
		// JLabel
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(new JLabel("Termination: "), gbaConstraints);
		// JComboBox.
//		cboTermination.addActionListener(this);
		mcboTermination.setSelectedIndex(0);
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.gridwidth = 5;
		add(mcboTermination, gbaConstraints);
		gbaConstraints.gridwidth = 1;
		mcboTermination.setSelectedIndex(4);

		// TIME
		// JLabel
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(new JLabel("Time: "), gbaConstraints);
		// SpinnerDateModel
//		GregorianCalendar calEpoch = new GregorianCalendar(1972, Calendar.MAY, 13, 00, 00, 00);
//		calEpoch.setTimeInMillis(0);
//		Date datMinDate = calEpoch.getTime();
//		calEpoch.add(Calendar.HOUR_OF_DAY, 14);
//		Date datIniDate = calEpoch.getTime();
//		calEpoch.add(Calendar.HOUR_OF_DAY, 9);
//		calEpoch.add(Calendar.SECOND, -1);
//		Date datMaxDate = calEpoch.getTime();
//		// javax.swing.JOptionPane.showMessageDialog(null,(new
//		// SimpleDateFormat("dd/MM/yyyy HH:mm")).format(datMinDate));
//		// javax.swing.JOptionPane.showMessageDialog(null,(new
//		// SimpleDateFormat("dd/MM/yyyy HH:mm")).format(datIniDate));
//		// javax.swing.JOptionPane.showMessageDialog(null,(new
//		// SimpleDateFormat("dd/MM/yyyy HH:mm")).format(datMaxDate));
//		SpinnerDateModel modTest = new SpinnerDateModel(datIniDate, datMinDate, datMaxDate, Calendar.MINUTE);
//		mspiTime = new JSpinner(modTest);
//		JSpinner.DateEditor ediTest = new JSpinner.DateEditor(mspiTime, "HH:mm:ss");
//		ediTest.getTextField().setColumns(3);
//		mspiTime.setEditor(ediTest);
//		gbaConstraints.gridx = nColumn + 1;
//		gbaConstraints.gridy = nRow++;
//		add(mspiTime, gbaConstraints);
		
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow;
		add(mcboTimeHour, gbaConstraints);
		gbaConstraints.gridx = nColumn + 2;
		gbaConstraints.gridy = nRow;
		gbaConstraints.anchor = GridBagConstraints.EAST;
		add(new JLabel(":"), gbaConstraints);
		mcboTimeMinute.setEditable(true);
		((JTextField)mcboTimeMinute.getEditor().getEditorComponent()).setColumns(1);	// Correct width of the JComboBox. 
		gbaConstraints.gridx = nColumn + 3;
		gbaConstraints.gridy = nRow;
		add(mcboTimeMinute, gbaConstraints);
		mcboTimeSecond.setEditable(true);
		((JTextField)mcboTimeSecond.getEditor().getEditorComponent()).setColumns(1);	// Correct width of the JComboBox. 
		gbaConstraints.gridx = nColumn + 4;
		gbaConstraints.gridy = nRow;
		add(new JLabel(":"), gbaConstraints);
		gbaConstraints.gridx = nColumn + 5;
		gbaConstraints.gridy = nRow++;
		add(mcboTimeSecond, gbaConstraints);
		
		// TIMECONTROL.
		// JLabel
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(new JLabel("TimeControl: "), gbaConstraints);
		// JTextField
		mtxtTimeControl.addFocusListener(this);
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.anchor = GridBagConstraints.WEST;
		gbaConstraints.gridwidth = 5;
		add(mtxtTimeControl, gbaConstraints);
		gbaConstraints.gridwidth = 1;

		// UTCDATE.
		// JLabel
		JLabel lblUTCDate = new JLabel("UTCDate: ");
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow++;
		add(lblUTCDate, gbaConstraints);

		// UTCTIME.
		// JLabel
		JLabel lblUTCTime = new JLabel("UTCTime: ");
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow++;
		add(lblUTCTime, gbaConstraints);
	
		// VARIATION.
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(new JLabel("Variation: "), gbaConstraints);
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.anchor = GridBagConstraints.WEST;
		gbaConstraints.gridwidth = 5;
		add(mtxtVariation, gbaConstraints);
		gbaConstraints.gridwidth = 1;

		// WHITEELO.
		// JLabel.
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(new JLabel("WhiteElo: "), gbaConstraints);
		// JTextbox.
		mtxtWhiteElo.addFocusListener(this);
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow;
		gbaConstraints.anchor = GridBagConstraints.WEST;
		gbaConstraints.gridwidth = 2;
		add(mtxtWhiteElo, gbaConstraints);
		gbaConstraints.gridwidth = 1;
		// JButton.
		mbtnWhiteElo = new JButton("Find");
//		mbtnWhiteElo.setEnabled(false);
		mbtnWhiteElo.addActionListener(this);
		gbaConstraints.gridx = nColumn + 3;
		gbaConstraints.gridy = nRow++;
		add(mbtnWhiteElo, gbaConstraints);

		// WHITENA.
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(new JLabel("WhiteNA: "), gbaConstraints);
		mtxtWhiteNA.addFocusListener(this);
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.anchor = GridBagConstraints.WEST;
		gbaConstraints.gridwidth = 5;
		add(mtxtWhiteNA, gbaConstraints);
		gbaConstraints.gridwidth = 1;
		
		// WHITETITLE.
		// Label.
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		gbaConstraints.gridwidth = 2;
		add(new JLabel("WhiteTitle: "), gbaConstraints);
		gbaConstraints.gridwidth = 1;
		// JComboBox.
		mcboWhiteTitle = new JComboBox();
//		cboWhiteTitle.addActionListener(this);
		titTemp = Title.values();
		for (int nI = 0; nI < titTemp.length; nI++) {
//			cboWhiteTitle.addItem(titTemp[nI].getFrench());
			mcboWhiteTitle.addItem(titTemp[nI].getAbbreviation());
		}
		titTemp = null;
		mcboWhiteTitle.setSelectedIndex(0); // No item selected.
		mcboWhiteTitle.setEditable(true);
		((JTextField)mcboWhiteTitle.getEditor().getEditorComponent()).setColumns(2);	// Correct width of the JComboBox. 
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow;
		add(mcboWhiteTitle, gbaConstraints);
		// JButton
		mbtnWhiteTitle = new JButton("Find");
//		mbtnWhiteTitle.setEnabled(false);
		mbtnWhiteTitle.addActionListener(this);
		gbaConstraints.gridx = nColumn + 3;
		gbaConstraints.gridy = nRow++;
		add(mbtnWhiteTitle, gbaConstraints);

		// WHITETYPE.
		// JLabel
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow;
		add(new JLabel("WhiteType: "), gbaConstraints);
		// JComboBox
		mcboWhiteType.setEditable(true);
		gbaConstraints.gridx = nColumn + 1;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.gridwidth = 5;
		add(mcboWhiteType, gbaConstraints);
		gbaConstraints.gridwidth = 1;

		
		
		
		// CUSTOM TAG PAIRS.
//		JPanel panCustomTagPairs = new JPanel();
//		panCustomTagPairs.setBorder(BorderFactory.createTitledBorder("Custom Tag Pairs"));
		Object[][] data = {
			    {"BlackFRBEKBSB", "?", "What ever!"},
			    {"BlackTeam", "?", "Rowing"},
			    {"WhiteFRBEKBSB", "?","Snowboarding"},
			    {"WhiteTeam", "?", "Rowing"}
			};
		JTable mtabCustomTagPairs = new JTable(data, new String[]{"Name", "Value", "Delete"});
		mtabCustomTagPairs.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
//		mtabCustomTagPairs.setDefaultRenderer(Object.class, new CustomTagPairsTableCellRenderer());
		
		mtabCustomTagPairs.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (column == 0) {
					c.setFont(c.getFont().deriveFont(Font.BOLD));
				}
				return c;
			}	
		});
		gbaConstraints.gridx = nColumn;
		gbaConstraints.gridy = nRow++;
		gbaConstraints.gridwidth = 6;
		gbaConstraints.gridheight = 5;
		JScrollPane spaCustomTagPairs = new JScrollPane(mtabCustomTagPairs);
		spaCustomTagPairs.setBorder(BorderFactory.createTitledBorder("Custom Tag Pairs"));
		spaCustomTagPairs.setSize(250, 250);
		spaCustomTagPairs.setPreferredSize(new Dimension(250, 87));
		spaCustomTagPairs.setMinimumSize(new Dimension(250, 87));
		add(spaCustomTagPairs, gbaConstraints);
		mtabCustomTagPairs.setFillsViewportHeight(true);
		gbaConstraints.gridwidth = 1;
		gbaConstraints.gridheight = 1;
		nRow++;
		nRow++;
		
		
//		JPanel panCustomTagPairs = new JPanel();
//		panCustomTagPairs.setBorder(BorderFactory.createTitledBorder("Custom Tag Pairs"));
////		gbaConstraints.gridx = 3;
////		gbaConstraints.gridy = nRow = 0;
//		gbaConstraints.gridx = nColumn;
//		gbaConstraints.gridy = nRow;
//		gbaConstraints.gridwidth = 2;
//		gbaConstraints.gridheight = 5;
//		add(panCustomTagPairs, gbaConstraints);
//		gbaConstraints.gridwidth = 1;
//		gbaConstraints.gridheight = 1;
//		panCustomTagPairs.setLayout(new GridBagLayout());
//		GridBagConstraints gbaCustomTagPairsConstraints = new GridBagConstraints();
//		int nCustomTagPairsRow = 0;
//		// WhiteFRBEKBSB.
//		JLabel lblWhiteFRBEKBSB = new JLabel("WhiteFRBEKBSB: ");
//		gbaCustomTagPairsConstraints.gridx = 0;
//		gbaCustomTagPairsConstraints.gridy = nCustomTagPairsRow;
//		panCustomTagPairs.add(lblWhiteFRBEKBSB, gbaCustomTagPairsConstraints);
//		JTextField txtWhiteFRBEKBSB = new JTextField(5);
//		gbaCustomTagPairsConstraints.gridx = 1;
//		gbaCustomTagPairsConstraints.gridy = nCustomTagPairsRow++;
//		panCustomTagPairs.add(txtWhiteFRBEKBSB, gbaCustomTagPairsConstraints);
//		// BlackFRBEKBSB.
//		JLabel lblBlackFRBEKBSB = new JLabel("BlackFRBEKBSB: ");
//		gbaCustomTagPairsConstraints.gridx = 0;
//		gbaCustomTagPairsConstraints.gridy = nCustomTagPairsRow;
//		panCustomTagPairs.add(lblBlackFRBEKBSB, gbaCustomTagPairsConstraints);
//		JTextField txtBlackFRBEKBSB = new JTextField(5);
//		gbaCustomTagPairsConstraints.gridx = 1;
//		gbaCustomTagPairsConstraints.gridy = nCustomTagPairsRow++;
//		panCustomTagPairs.add(txtBlackFRBEKBSB, gbaCustomTagPairsConstraints);
//		// WhiteTeam.
//		JLabel lblWhiteTeam = new JLabel("WhiteTeam: ");
//		gbaCustomTagPairsConstraints.gridx = 0;
//		gbaCustomTagPairsConstraints.gridy = nCustomTagPairsRow;
//		panCustomTagPairs.add(lblWhiteTeam, gbaCustomTagPairsConstraints);
//		JTextField txtWhiteTeam = new JTextField(5);
//		gbaCustomTagPairsConstraints.gridx = 1;
//		gbaCustomTagPairsConstraints.gridy = nCustomTagPairsRow++;
//		panCustomTagPairs.add(txtWhiteTeam, gbaCustomTagPairsConstraints);
//		// BlackTeam.
//		JLabel lblBlackTeam = new JLabel("BlackTeam: ");
//		gbaCustomTagPairsConstraints.gridx = 0;
//		gbaCustomTagPairsConstraints.gridy = nCustomTagPairsRow;
//		panCustomTagPairs.add(lblBlackTeam, gbaCustomTagPairsConstraints);
//		JTextField txtBlackTeam = new JTextField(5);
//		gbaCustomTagPairsConstraints.gridx = 1;
//		gbaCustomTagPairsConstraints.gridy = nCustomTagPairsRow++;
//		panCustomTagPairs.add(txtBlackTeam, gbaCustomTagPairsConstraints);
//		gbaCustomTagPairsConstraints = null;
		
		
		// TEST JSPINNER WITH NUMBERS.
//		JSpinner m_numberSpinner;
//		SpinnerNumberModel m_numberSpinnerModel;
//		m_numberSpinnerModel = new SpinnerNumberModel(0, 0, 59, 1);
//		m_numberSpinner = new JSpinner(m_numberSpinnerModel);
//		add(m_numberSpinner);
//		gbaConstraints.gridx = nColumn + 1;
//		gbaConstraints.gridy = 13;
//		add(m_numberSpinner, gbaConstraints);

		DateFormatSymbols symbols = new DateFormatSymbols(Locale.FRENCH);

		// String months[] = symbols.getMonths(); // symbols.getWeekdays()
//		java.util.List<String> lmonths = new ArrayList<String>(); // symbols.getWeekdays()
		String[] months = symbols.getMonths();
		int nLongestMonth = 0;
		for (int nI = 0; nI < months.length; nI++) {
			if (months[nI].length() > nLongestMonth) {
				nLongestMonth = months[nI].length();
			}
		}
		// String deleteme;
		// lmonths.add("                                                                                             ");
		// // lmonths.add(deleteme.)
		// // Collections.addAll(lmonths, months);
		// SpinnerModel model1 = new SpinnerListModel(lmonths);
		// JSpinner spinner1 = new JSpinner(model1);
		// // spinner1.set .WIDTH(nLongestMonth);
		// gbaConstraints.gridx = 2;
		// gbaConstraints.gridy = 17;
		// add(spinner1, gbaConstraints);
		//
		//

		// com.toedter.calendar.JCalendar MyJCalendar = new
		// com.toedter.calendar.JCalendar();
		// MyJCalendar.setTodayButtonVisible(true);
		// MyJCalendar.setNullDateButtonVisible(true);
		// MyJCalendar.setDecorationBordersVisible(false);
		// MyJCalendar.getDayChooser().setDayBordersVisible(false);
		// gbaConstraints.gridx = 2;
		// gbaConstraints.gridy = 19;
		// add(MyJCalendar, gbaConstraints);

		// net.sourceforge.jdatepicker.JDatePanel WhateEver = new
		// net.sourceforge.jdatepicker.JDatePanel();
		// JDatePicker whatEver = new JDatePicker();
		// com.toedter.calendar.JCalendar MyJCalendar = new
		// com.toedter.calendar.JCalendar();
		// MyJCalendar.setTodayButtonVisible(true);
		// MyJCalendar.setDecorationBordersVisible(false);
		// MyJCalendar.getDayChooser().setDayBordersVisible(false);
		// gbaConstraints.gridx = 2;
		// gbaConstraints.gridy = 19;
		// add(MyJCalendar, gbaConstraints);

		this.setOpaque(true); // Content panes must be opaque. // Try to move
								// this line at the beginning (to make it
								// clearer).
		this.setSize(600, 600);

		
		
		this.setAnnotator(mpgnInternalGame.getAnnotator());
		this.setECO(mpgnInternalGame.getECO());
		this.setNIC(mpgnInternalGame.getNIC());
		this.setOpening(mpgnInternalGame.getOpening());
		this.setVariation(mpgnInternalGame.getVariation());
		this.setSubVariation(mpgnInternalGame.getSubVariation());
		this.setPlyCount(mpgnInternalGame.getPlyCount());
//		this.setMovetext(mpgnInternalGame.getMovetext());
		
		
		
		
//        //
//        // Create an ActionListener for the JComboBox component.
//        //
//        mcboEvent.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent event) {
//                //
//                // Get the source of the component, which is our combo
//                // box.
//                //
//                JComboBox comboBox = (JComboBox) event.getSource();
// 
//                //
//                // Print the selected items and the action command.
//                //
//                Object selected = comboBox.getSelectedItem();
//                System.out.println("Selected Item  = " + selected);
//                String command = event.getActionCommand();
//                System.out.println("Action Command = " + command);
// 
//                //
//                // Detect whether the action command is "comboBoxEdited"
//                // or "comboBoxChanged"
//                //
//                if ("comboBoxEdited".equals(command)) {
//                    System.out.println("User has typed a string in " +
//                            "the combo box.");
//                } else if ("comboBoxChanged".equals(command)) {
//                    System.out.println("User has selected an item " +
//                            "from the combo box.");
//                }
//            }
//        });

		
		
		
		
//        mbtnWhiteElo.setEnabled(false);
//        System.out.println("mbtnWhiteElo.setEnabled(false)");
        
        
        
//        SwingWorker<Void, Void> wrkLoadRatings = new SwingWorker<Void, Void>() {
//            @Override
//            protected Void doInBackground() throws Exception {
//                try {
////                    Thread.sleep(3 * 1000);
//
//
//        			System.out.println(new java.util.Date() + " -- doInBackground(): " + ", load DB");  
//        			// LOAD DB (multi-threading).
//        			mhsqlFIDERatings = new HSQLDB();
////        	    	Class.forName("org.hsqldb.jdbcDriver").newInstance(); 		// Load the driver.
////        	    	if (mjdbcConnection==null) {
////        	    		mjdbcConnection = DriverManager.getConnection("jdbc:hsqldb:file:" + DATABASE_PATH, "sa",  "");	// Get the connection.
////        	    	}
//        			System.out.println(new java.util.Date() + " -- doInBackground(): " + ", DB loaded");  
//                }
//                catch (Exception ex) {
//                }
//                finally {
//                }
//
//                return null;
//            }                    
//        };
		
//        wrkLoadRatings.addPropertyChangeListener(new PropertyChangeListener() {
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                System.out.println("Event " + evt + " name" + evt.getPropertyName() + " value " + evt.getNewValue());
//                if ("DONE".equals(evt.getNewValue().toString())) {
//					mbtnBlackElo.setEnabled(true);
//					mbtnWhiteElo.setEnabled(true);
//        			mbtnBlackTitle.setEnabled(true);
//        			mbtnWhiteTitle.setEnabled(true);
//                }
//            }
//        });

//        wrkLoadRatings.execute();
		
		
		
		
		
	}

//	public TagsPanel(PGNGame pgnInternalGame, FIDEPlayerList lstFIDERating) {
//		this(pgnInternalGame);
//		mlstFIDERating = lstFIDERating;
//	}
	
	public TagsPanel(PGNGame pgnInternalGame, FIDERatingList lstFIDERating) {
		this(pgnInternalGame);
		mfrlFIDERatings = lstFIDERating;
	}
	

    private ArrayList<String> retrieveTownList(String sIOCCode) {

		ArrayList<String> lstReturnValue = null;
    	String sLine;
    	BufferedReader brTownList;
    	
        try {
        	String sFileName = "towns_" + sIOCCode.toLowerCase() + ".txt" ;
        	if ((new File(sFileName)).exists()) {
        		lstReturnValue = new ArrayList<String>();
	            brTownList = new BufferedReader(new FileReader(sFileName));
	            while ((sLine=brTownList.readLine())!=null) {
	            	lstReturnValue.add(sLine + " " + sIOCCode);
	            }
	            brTownList.close();                
        	}
            sFileName = null;
        }
//        catch (java.io.FileNotFoundException e) {
//		      e.printStackTrace();
//        }
        catch (java.io.IOException e) {
            lstReturnValue = null;        
        }
        finally {
            brTownList = null;
        }
        
        return lstReturnValue;
    }
	
	public void TagsPanel2() { // Constructor.
	// super(new BorderLayout());
	// super(null);
		setLayout(null);

		com.toedter.calendar.JDateChooser llfdgfg = new com.toedter.calendar.JDateChooser();
		llfdgfg.getJCalendar().setTodayButtonVisible(true);
		llfdgfg.getJCalendar().setNullDateButtonVisible(true);
		llfdgfg.getJCalendar().setDecorationBordersVisible(false);
		llfdgfg.getJCalendar().getDayChooser().setDayBordersVisible(false);
		llfdgfg.getJCalendar().setWeekOfYearVisible(false);
		// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		// try {
		// llfdgfg.setDate(sdf.parse("14/12/2012"));
		// }
		// catch(Exception e) {
		// }
		javax.swing.JOptionPane.showMessageDialog(null,
				String.valueOf(llfdgfg.getPreferredSize().toString()));
		llfdgfg.setDateFormatString("EEEE, dd MMMM yyyy");
		javax.swing.JOptionPane.showMessageDialog(null,
				String.valueOf(llfdgfg.getPreferredSize().toString()));

		// String days[] = {"dimanche, 30 septembre 2012"};
		// String days[] = {"Mmmmmmmm, 99 Mmmmmmmmm 9999"};
		// TO DO: to find a way to make it works whatever the locale.
		String days[] = { "nnnnnnnnnnnnnnnnnnnnnnnnnnn" };
		// SpinnerModel model1 = new SpinnerListModel(days);
		// JSpinner spinner1 = new JSpinner(new SpinnerListModel(days));
		// javax.swing.JOptionPane.showMessageDialog(null, (new JSpinner(new
		// SpinnerListModel(days))).getPreferredSize().toString());
		// (int)spinner1.getPreferredSize().getWidth()+1;
		// add(spinner1);
		javax.swing.JOptionPane.showMessageDialog(null,	"largeur du CalendarButton : " + llfdgfg.getCalendarButton().getWidth());

		llfdgfg.setPreferredSize(new Dimension((int) (new JSpinner(
				new SpinnerListModel(days))).getPreferredSize().getWidth()
				+ llfdgfg.getCalendarButton().getWidth(), (int) llfdgfg
				.getPreferredSize().getHeight()));

		// javax.swing.JOptionPane.showMessageDialog(null, (new JSpinner(new
		// SpinnerListModel(days))).getPreferredSize().toString());

		// llfdgfg.getDateEditor().setDateFormatString("   EEEE, dd MMMM yyyy");
		// llfdgfg.setMinimumSize(llfdgfg.getMaximumSize());
		// llfdgfg.setPreferredSize(new Dimension(300, 300));
//		com.toedter.calendar.JSpinnerDateEditor jjdsf = new com.toedter.calendar.JSpinnerDateEditor();
		add(llfdgfg);
		llfdgfg.setBounds(25, 5, (int) llfdgfg.getPreferredSize().getWidth() + 1, (int) llfdgfg
						.getPreferredSize().getHeight() + 1);
		llfdgfg.revalidate();

		javax.swing.JOptionPane.showMessageDialog(null, String.valueOf(llfdgfg.getMinimumSize().toString()));
		javax.swing.JOptionPane.showMessageDialog(null, String.valueOf(llfdgfg.getMaximumSize().toString()));
		javax.swing.JOptionPane.showMessageDialog(null, String.valueOf(llfdgfg.getPreferredSize().toString()));

//		Calendar calDate = Calendar.getInstance();
		// Date initDate = calDate.getTime();
		Date initDate = (new GregorianCalendar(2012, Calendar.DECEMBER, 26, 00, 00, 00)).getTime();
		// try {
		// initDate = sdf.parse("14/12/2012");
		// }
		// catch(Exception e) {
		// }
		// calDate.add(Calendar.YEAR, -100);
		// Date earliestDate = calDate.getTime();
		// calDate.add(Calendar.YEAR, 200);
		// Date latestDate = calDate.getTime();
		// SpinnerDateModel modDate = new SpinnerDateModel(initDate,
		// earliestDate, latestDate, Calendar.DAY_OF_MONTH);
		SpinnerDateModel modDate = new SpinnerDateModel(initDate, initDate, initDate, Calendar.DAY_OF_MONTH);
		JSpinner spiDate = new JSpinner(modDate);
		spiDate.setEditor(new JSpinner.DateEditor(spiDate, "EEEE, dd MMMM yyyy"));
		spiDate.setBounds(50, 50, (int) spiDate.getPreferredSize().getWidth() + 1, (int) spiDate.getPreferredSize().getHeight() + 1);
		add(spiDate);

		// Calendar calInitialTime = new GregorianCalendar(2012,
		// Calendar.DECEMBER, 26, 00, 00, 00);

		// add(jjdsf);

		// JPanel spinnerBack = new JPanel();
		// spinnerBack.setSize(600, 600);
		// spinnerBack.add(llfdgfg);
		// jjdsf.setDateFormatString("EEEE, dd MMMM yyyy");
		// jjdsf.setSize(200, 200);
		// spinnerBack.add(jjdsf);
		// add(spinnerBack);

		// ((JSpinner.DefaultEditor)llfdgfg.getT.getTextField().
		// .getEditor()).getTextField().setColumns(10);

		// // get metrics from the graphics
		// // FontMetrics metrics =
		// java.awt.Graphics.getFontMetrics(llfdgfg.getFont());
		// int metrics =
		// java.awt.Graphics.getFontMetrics(llfdgfg.getFont()).stringWidth("Jeudi, 28 juin 2012");
		// // get the height of a line of text in this
		// // font and render context
		// int hgt = metrics.getHeight();
		// // get the advance of my text in this font
		// // and render context
		// int adv = metrics.stringWidth();
		// // calculate the size of a box to hold the
		// // text with some padding.
		// Dimension size = new Dimension(adv+2, hgt+2);

		
	}

	// public void actionPerformed(ActionEvent e) { // Event handler for a click
	// on the ComboBox.
	// JComboBox cboTemp = (JComboBox)e.getSource();
	// String sSelected = (String)cboTemp.getSelectedItem();
	// }
	// Enter pressed.
//	public void actionPerformed(ActionEvent evtAction) {
//		Object objSource = evtAction.getSource();
//
//		if (objSource == mtxtWhite) {
//			javax.swing.JOptionPane
//					.showMessageDialog(null, "White JTextField.");
//		}
//		// else {
//		// javax.swing.JOptionPane.showMessageDialog(null,
//		// "not White JTextField.");
//		//
//		// }
//	}

	// Text modified
	public void insertUpdate(DocumentEvent evtDocument) {
		// javax.swing.JOptionPane.showMessageDialog(null, "insertUpdate");
	}

	public void removeUpdate(DocumentEvent evtDocument) {
//		javax.swing.JOptionPane.showMessageDialog(null, "removeUpdate");
	}

	public void changedUpdate(DocumentEvent evtDocument) {
		javax.swing.JOptionPane.showMessageDialog(null, "changedUpdate");
	}

	// Focus lost or gained.
	public void focusGained(FocusEvent evtFocus) {
		// Do nothing.
	}

	public void focusLost(FocusEvent evtFocus) {
		if (evtFocus.getSource() == mtxtDate) {
			if (mtxtDate.getText().trim().length() == 0) {
				mtxtDate.setText("????.??.??");
			}
		} else if (evtFocus.getSource() == mtxtWhite) {
			if (mtxtWhite.getText().trim().length() == 0) {
				mtxtWhite.setText("?");
			}
		} else if (evtFocus.getSource() == mtxtBlack) {
			if (mtxtBlack.getText().trim().length() == 0) {
				mtxtBlack.setText("?");
//				FIDEPlayer frTemp = mfrlFIDERatings.getFIDEPlayer(mtxtBlack.getText());
			}
		} else if (evtFocus.getSource() == mtxtAnnotator) {
			if (mtxtAnnotator.getText().trim().length() == 0) {
				mtxtAnnotator.setText("?");
			}
		} else if (evtFocus.getSource() == mtxtBlackElo) {
			if (mtxtBlackElo.getText().trim().length() == 0) {
				mtxtBlackElo.setText("?");
			}
		} else if (evtFocus.getSource() == mtxtBlackNA) {
			if (mtxtBlackNA.getText().trim().length() == 0) {
				mtxtBlackNA.setText("?");
			}
		} else if (evtFocus.getSource() == mcboBoard) {
			if (mcboBoard.getSelectedItem().toString().trim().length() == 0) {
				mcboBoard.setSelectedItem("?");
			}
		} else if (evtFocus.getSource() == mtxtECO) {
			if (mtxtECO.getText().trim().length() == 0) {
				mtxtECO.setText("?");
			}
		} else if (evtFocus.getSource() == mtxtEventDate) {
			if (mtxtEventDate.getText().trim().length() == 0) {
				mtxtEventDate.setText("????.??.??");
			}
		} else if (evtFocus.getSource() == mtxtEventSponsor) {
			if (mtxtEventSponsor.getText().trim().length() == 0) {
				mtxtEventSponsor.setText("?");
			}
		} else if (evtFocus.getSource() == mtxtSection) {
			if (mtxtSection.getText().trim().length() == 0) {
				mtxtSection.setText("?");
			}
			
		} else if (evtFocus.getSource() == mtxtStage) {
			if (mtxtStage.getText().trim().length() == 0) {
				mtxtStage.setText("?");
			}
		} else if (evtFocus.getSource() == mtxtTimeControl) {
			if (mtxtTimeControl.getText().trim().length() == 0) {
				mtxtTimeControl.setText("?");
			}
		} else if (evtFocus.getSource() == mtxtWhiteElo) {
			if (mtxtWhiteElo.getText().trim().length() == 0) {
				mtxtWhiteElo.setText("?");
			}
		} else if (evtFocus.getSource() == mtxtWhiteNA) {
			if (mtxtWhiteNA.getText().trim().length() == 0) {
				mtxtWhiteNA.setText("?");
			}
		}
//	} else if {evtFocus.getSource() == } {
//		if (.length() == 0) {
//			
//		}
//	}
		
	}

	public void actionPerformed(ActionEvent evtAction) {
//      topTextArea.append(e.getActionCommand() + newline);
		if (evtAction.getSource()==mbtnWhiteElo) {
//			if (mlstFIDERating!=null && mtxtWhite.getText().length()>2) {
			if (mfrlFIDERatings!=null && mtxtWhite.getText().length()>2) {
//				FIDEPlayer fpWhite = mlstFIDERating.getFIDEPlayer(mtxtWhite.getText());
				FIDEPlayer fpWhite = mfrlFIDERatings.getFIDEPlayer(mtxtWhite.getText());
				if (fpWhite!=null && fpWhite.getElo()>0) {
					mtxtWhiteElo.setText(String.valueOf(fpWhite.getElo()));
				}
				else {
					mtxtWhiteElo.setText("?");
				}
				fpWhite=null;
			}
			
			
			
//            mbtnWhiteElo.setEnabled(false);
//
//            
//            
//            
//            SwingWorker<Void, Void> wrkLoadRatings = new SwingWorker<Void, Void>() {
//                @Override
//                protected Void doInBackground() throws Exception {
//                    try {
////                        Thread.sleep(3 * 1000);
//            			// LOAD DB (multi-threading).
//            			HSQLDB hsqlFIDERatings = new HSQLDB();
//            			System.out.println("TagsPanel(): about to start hsqldb");  
//
//            			hsqlFIDERatings.start();
//            			System.out.println("TagsPanel(): hsqldb started");  
//                        
//                    }
////                    catch (InterruptedException ex) {
////                        Logger.getLogger(TestButtonbtnTask.class.getName()).log(Level.SEVERE, null, ex);
////                    }
//                    finally {
//                    }
//
//                    return null;
//                }                    
//            };

//            wrkLoadRatings.addPropertyChangeListener(new PropertyChangeListener() {
//                @Override
//                public void propertyChange(PropertyChangeEvent evt) {
//                    System.out.println("Event " + evt + " name" + evt.getPropertyName() + " value " + evt.getNewValue());
//                    if ("DONE".equals(evt.getNewValue().toString())) {
//            			System.out.println("TagsPanel(): hsqldb started");  
//                        mbtnWhiteElo.setEnabled(true);
////                        btnTask.setText("Test");
//                    }
//                }
//            });
//
//            wrkLoadRatings.execute();
			
			
			
			
			
			
		} 
		else if (evtAction.getSource()==mbtnBlackElo) {
			if (mfrlFIDERatings!=null && mtxtBlack.getText().length()>2) {
				FIDEPlayer fpBlack = mfrlFIDERatings.getFIDEPlayer(mtxtBlack.getText());
				if (fpBlack!=null && fpBlack.getElo()>0) {
					mtxtBlackElo.setText(String.valueOf(fpBlack.getElo()));
				}
				else {
					mtxtBlackElo.setText("?");
				}
				fpBlack=null;
			}
		}
		else if (evtAction.getSource()==mbtnBlackTitle) {
			if (mfrlFIDERatings!=null && mtxtBlack.getText().length()>2) {
				FIDEPlayer fpBlack = mfrlFIDERatings.getFIDEPlayer(mtxtBlack.getText());
				if (fpBlack!=null) {
					mcboBlackTitle.setSelectedItem(String.valueOf(fpBlack.getTitleAbbreviation()));
				}
				else {
					mcboBlackTitle.setSelectedItem("?");
				}
				fpBlack=null;
			}
		}
		else if (evtAction.getSource()==mbtnWhiteTitle) {
			if (mfrlFIDERatings!=null && mtxtWhite.getText().length()>2) {
				FIDEPlayer fpWhite = mfrlFIDERatings.getFIDEPlayer(mtxtWhite.getText());
				if (fpWhite!=null) {
					mcboWhiteTitle.setSelectedItem(String.valueOf(fpWhite.getTitleAbbreviation()));
				}
				else {
					mcboWhiteTitle.setSelectedItem("?");
				}
				fpWhite=null;
			}
		}
		else if (evtAction.getSource()==mbtnExport) {
			if (mradResultUndefined.isSelected()) {
				int nResponse = JOptionPane.showConfirmDialog(null,
						"Result is undefined. Would you like to continue?",
					    "PGN Enforcer", JOptionPane.YES_NO_OPTION);
				if (nResponse == JOptionPane.NO_OPTION) {
					return;
				}
			}
			
//			PGNGame pgnOutputGame = new PGNGame();
			// Event
			mpgnInternalGame.setEvent(mcboEvent.getSelectedItem().toString());
			// Site
			mpgnInternalGame.setSite(mcboSite.getSelectedItem().toString());
			// Date
			mpgnInternalGame.setDate(mtxtDate.getText());
			// Round
			mpgnInternalGame.setRound(mcboRound.getSelectedItem().toString());
			// White
			mpgnInternalGame.setWhite(mtxtWhite.getText());
			// Black
			mpgnInternalGame.setBlack(mtxtBlack.getText());
			// Result
			if (mradResultWhiteWin.isSelected()) {
				mpgnInternalGame.setResult("1-0");
			}
			else if (mradResultBlackWin.isSelected()) {
				mpgnInternalGame.setResult("0-1");
			}
			else if (mradResultDraw.isSelected()) {
				mpgnInternalGame.setResult("1/2-1/2");
			}
			else {
				mpgnInternalGame.setResult("*");
			}
			// Annotator
			mpgnInternalGame.setAnnotator(mtxtAnnotator.getText());
			// BlackElo
			mpgnInternalGame.setBlackElo(mtxtBlackElo.getText());
			// BlackNA
			mpgnInternalGame.setBlackNA(mtxtBlackNA.getText());
			// BlackTitle
			mpgnInternalGame.setBlackTitle(mcboBlackTitle.getSelectedItem().toString());
			// BlackType
			mpgnInternalGame.setBlackType(mcboBlackType.getSelectedItem().toString());
			// Board
			mpgnInternalGame.setBoard(mcboBoard.getSelectedItem().toString());
			// ECO
			mpgnInternalGame.setECO(mtxtECO.getText());
			// EventDate
			mpgnInternalGame.setEventDate(mtxtEventDate.getText());
			// EventSponsor
			mpgnInternalGame.setEventSponsor(mtxtEventSponsor.getText());
			// FEN
			mpgnInternalGame.setFEN(mtxtFEN.getText());
			// Mode
			mpgnInternalGame.setMode(mcboMode.getSelectedItem().toString());
			// NIC
			mpgnInternalGame.setNIC(mtxtNIC.getText());
			// Opening
			mpgnInternalGame.setOpening(mtxtOpening.getText());
			// PlyCount
				// Do nothing: PlyCount is set in setMoveSection().
			// Section
			mpgnInternalGame.setSection(mtxtSection.getText());
			// SetUp
				// Do nothing: SetUp is set in setFEN().
			// Stage
			mpgnInternalGame.setStage(mtxtStage.getText());
			// SubVariation
			mpgnInternalGame.setSubVariation(mtxtSubVariation.getText());
			// Termination
			mpgnInternalGame.setTermination(mcboTermination.getSelectedItem().toString());
			// Time
			mpgnInternalGame.setTime(	mcboTimeHour.getSelectedItem().toString() +
									":" + mcboTimeMinute.getSelectedItem().toString() +
									":" + mcboTimeSecond.getSelectedItem().toString());
			// TimeControl
			mpgnInternalGame.setTimeControl(mtxtTimeControl.getText());
			// UTCDate
			// UTCTime
			// Variation
			mpgnInternalGame.setVariation(mtxtVariation.getText());
			// WhiteElo
			mpgnInternalGame.setWhiteElo(mtxtWhiteElo.getText());
			// WhiteNA
			mpgnInternalGame.setWhiteNA(mtxtWhiteNA.getText());
			// WhiteTitle
			mpgnInternalGame.setWhiteTitle(mcboWhiteTitle.getSelectedItem().toString());
			// WhiteType
			mpgnInternalGame.setWhiteType(mcboWhiteType.getSelectedItem().toString());
			// Move section
//			mpgnInternalGame.setMovetext(msMovetext);
//			mpgnInternalGame.setMovetextResult(mpgnInternalGame.getResult());
			
			// WRITE GAME TO TEXT FILE.
			ArrayList<PGNGame> apgnOutputGame = new ArrayList<PGNGame>(1);
			apgnOutputGame.add(mpgnInternalGame);
			(new PGNStream()).writePGN(apgnOutputGame);
			apgnOutputGame = null;
		}
	}
    @Override
    public void propertyChange(PropertyChangeEvent evtPropertyChange) {
        if ("date".equals(evtPropertyChange.getPropertyName())) {
        	if (evtPropertyChange.getSource()==dchDate.getDateEditor()) {
            	Date datDate = (Date)evtPropertyChange.getNewValue();
            	if (datDate!=null) {
                	mtxtDate.setText((new SimpleDateFormat("yyyy.MM.dd")).format(datDate));
            	}
            	else {
                	mtxtDate.setText("????.??.??");
            	}
        	} else if (evtPropertyChange.getSource()==dchEventDate.getDateEditor()) {
            	Date datEventDate = (Date)evtPropertyChange.getNewValue();
            	if (datEventDate!=null) {
                	mtxtEventDate.setText((new SimpleDateFormat("yyyy.MM.dd")).format(datEventDate));
            	}
            	else {
                	mtxtEventDate.setText("????.??.??");
            	}
        	}
        		
        }
    }
	
	protected void setAnnotator(String sAnnotator) {
		mtxtAnnotator.setText(sAnnotator);
	}
	protected void setEventDate(String sDate) {
    	mtxtEventDate.setText(sDate);
    }
	protected void setSite(String sSite) {
		mcboSite.setSelectedItem(sSite);
	}
	protected void setECO(String sECO) {
		mtxtECO.setText(sECO);
	}
	protected void setNIC(String sNIC) {
		mtxtNIC.setText(sNIC);
	}
	protected void setOpening(String sOpening) {
		mtxtOpening.setText(sOpening);
	}
	protected void setPlyCount(String sPlyCount) {
		mtxtPlyCount.setText(sPlyCount);
	}
	protected void setVariation(String sVariation) {
		mtxtVariation.setText(sVariation);
	}
	protected void setSubVariation(String sSubVariation) {
		mtxtSubVariation.setText(sSubVariation);
	}
	protected void setSection(String sSection) {
		mtxtSection.setText(sSection);
	}
	protected void setTime(String sTime) {
		// TODO: to insure sTime is of correct format.
//		mspiTime.setValue(sTime);
		mcboTimeHour.setSelectedItem(sTime.substring(0,2));
		mcboTimeMinute.setSelectedItem(sTime.substring(3,5));
		mcboTimeSecond.setSelectedItem(sTime.substring(6,8));
	}
	protected void setTimeControl(String sTimeControl) {
		mtxtTimeControl.setText(sTimeControl);
	}
//	public void setMovetext(String sMovetext) {
//		msMovetext = sMovetext;
//	}
	public void setEventList(ArrayList<String> lstsEvent, ActionListener lsrEvent) {
		int  nListSize=lstsEvent.size();
		
		int nI=0;
		while (nI < nListSize) {
			mcboEvent.addItem(lstsEvent.get(nI));
			nI++;
		}
		if (lsrEvent != null) {
			mcboEvent.addActionListener(lsrEvent);
		}
	}
	public void setEventList(ArrayList<String> lstsEvent) {
		setEventList(lstsEvent, null);
	}
	public void finalize() {
//		mlstFIDERating = null;
		mfrlFIDERatings = null;
	}
}
