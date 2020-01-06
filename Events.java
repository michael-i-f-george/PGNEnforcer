package org.chess.pgn;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

// Some data for a given tournament never change (i.e. "Interclubs nationaux" always happen on Sundays).
// The following class helps retrieving them from a XML file.
public class Events {	// renommer en Event
	private String msName;
	private String msSite;
//	private Date mdStartDate = null;
	private String msStartDate = null;
	private Date mdEndDate = null;
	private int mnLastRound = Integer.MAX_VALUE;
	private String msTeamNameOfUser = null;
	private String msTime = null;			// Voir si on ne peut pas l'enregistrer dans un format plus adapté.
	private String msTimeControl = null;
	private String msSection = null;
	private int mnDayOfPlay;	// java.util.Calendar. (p. ex. interclubs le dimanche)

	
	public Events() {
		// Constructor.
	}
	public String getName() {
		return msName;
	}
	public void setName(String sName){
		this.msName = sName;
	}
	public String getSite() {
		return msSite;
	}
	public void setSite(String sSite){
		this.msSite = sSite;
	}
//	public Date getStartDate() {
//		return mdStartDate;
//	}
	public String getStartDate() {
		return msStartDate;
	}
	public void setStartDate(String sDate){
//		DateFormat dfTemp = new SimpleDateFormat("yyyy-MM-dd");
//		try {
//			this.mdStartDate = dfTemp.parse(sDate);
//		}
//		catch (ParseException e) {
//			e.printStackTrace();
//		}
		msStartDate = sDate.replace('-', '.');
		
	}
	public Date getEndDate() {
		return mdEndDate;
	}
	public void setEndDate(String sDate){
		DateFormat dfTemp = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.mdEndDate = dfTemp.parse(sDate);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}		
	}
	public int getLastRound() {
		return mnLastRound;
	}
	public void setLastRound(int nLastRound){
		this.mnLastRound = nLastRound;
	}
	public String getUserTeam() {
		return msTeamNameOfUser;
	}
	public void setUserTeam(String sName){
		this.msTeamNameOfUser = sName;
	}
	public String getTime() {
		return msTime;
	}
	public void setTime(String sTime){
		this.msTime = sTime;
	}
	public String getTimeControl() {
		return msTimeControl;
	}
	public void setTimeControl(String sTimeControl){
		this.msTimeControl = sTimeControl;
	}
	public String getSection() {
		return msSection;
	}
	public void setSection(String sSection){
		this.msSection = sSection;
	}
	public int getDayOfPlay() {
		return mnDayOfPlay;
	}
	public void setDayOfPlay(String sDayOfPlay){

		// TODO: improve this (locale dependant).
		String[] asDayOfWeek = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
		
		int nI=6;
		while (nI>-1 && !sDayOfPlay.equals(asDayOfWeek[nI--]));
		
		this.mnDayOfPlay = nI;
	}
	
	public String toString(){
		return "not yet available";
//		return new StringBuffer("Nom : ").append(msName).append(", ")
//			.append("Prenom : ").append(prenom).append(", ")
//			.append("Adresse : ").append(adresse)
//			.toString();
	}
}
