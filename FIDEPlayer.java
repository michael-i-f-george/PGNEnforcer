package org.chess.pgn;

public class FIDEPlayer implements Comparable {


	public enum Activity {
		UNKNOWN,
		INACTIVE,
		ACTIVE
	}

	public enum Sex {
		UNKNOWN,
		FEMALE,
		MALE
	}
	
	public enum Season {
	   spring("Printemps"),summer("Eté"), automn("Automne"), winter("Hiver");
	
	   protected String label;
	
	   /** Constructor */
	   Season(String pLabel) {
	      this.label = pLabel;
	   }
	
	   public String getLabel() {
	      return this.label;
	   }
	}
	
	public enum Title {
//		+ correspondance titles
//		INTERNATIONAL_ORGANISER,
//		FIDE_ARBITER,
//		INTERNATIONAL_ARBITER,
		UNKNOWN("?", "?"),
		NONE("-", "-"),
		WOMAN_CANDIDATE_MASTER ("WCM", "candidat maître féminin"),
		CANDIDATE_MASTER ("CM", "candidat maître"),
		WOMAN_FIDE_MASTER ("WFM", "maître FIDE féminin"),
		FIDE_MASTER ("FM", "maître FIDE"),
		WOMAN_INTERNATIONAL_MASTER ("WIM", "maître international féminin"),
		INTERNATIONAL_MASTER("IM", "maître international"),
		WOMAN_GRANDMASTER("WGM", "grand maître féminin"),
		GRANDMASTER("GM", "grand maître");
		
		protected String msAbbreviation;
		protected String msFrench;
		
		// Constructor.
		Title(String sAbreviation, String sFrench) {
			msAbbreviation = sAbreviation;
			msFrench = sFrench;
		}
		public String getAbbreviation() {
			return msAbbreviation;
		}
		public String getFrench() {
			return msFrench;
		}
	}
	
	private int mnID = -1;
    private String msName="";
    private Title mtitTitle = Title.NONE;
    private String msCountryCode=""; 
    private int mnElo = -1;
    private int mnGamesPlayed = -1;
    private int mnBirthYear = -1;
    private Sex msexSex = Sex.UNKNOWN;
    private Activity mactActivity = Activity.UNKNOWN;
    private int mnK = -1;
    
    public FIDEPlayer() {
    	// Empty constructor.
    }
    public FIDEPlayer(String sName) {
        msName=sName.trim().toUpperCase();
        // TODO: to replace this constructor by one filling all the elements at once.
    }
//    public void setID(String sID) {
//    	msID = sID;
//    }
//    public String getID() {
//    	return msID;
//    }
    public void setID(int nID) {
    	mnID = nID;
    }
    public int getID() {
    	return mnID;
    }
    public void setName(String sName) {
        msName=sName.trim().toUpperCase();
    }
    public String getName() {
        return msName;
    }
    public void setTitle(String sTitle) {
    	sTitle=sTitle.trim();
    	
		if (sTitle.equals("")) {			// Java bans switching on a String.
    		mtitTitle=Title.NONE;
		} else if (sTitle.equals("CM") || sTitle.equals("c")) {
			mtitTitle=Title.CANDIDATE_MASTER;
		} else if (sTitle.equals("FM") || sTitle.equals("f")) {
			mtitTitle=Title.FIDE_MASTER;
		} else if (sTitle.equals("IM") || sTitle.equals("m")) {
			mtitTitle=Title.INTERNATIONAL_MASTER;    			
		} else if (sTitle.equals("GM") || sTitle.equals("g")) {
			mtitTitle=Title.GRANDMASTER;    			
		} else if (sTitle.equals("WCM") || sTitle.equals("wc")) {
			mtitTitle=Title.WOMAN_CANDIDATE_MASTER;    			
		} else if (sTitle.equals("WFM") || sTitle.equals("wf")) {
			mtitTitle=Title.WOMAN_FIDE_MASTER;    			
		} else if (sTitle.equals("WIM") || sTitle.equals("wm")) {
			mtitTitle=Title.WOMAN_INTERNATIONAL_MASTER;    			
		} else if (sTitle.equals("WGM")|| sTitle.equals("wg")) {
			mtitTitle=Title.WOMAN_GRANDMASTER;    			
		} else {
			mtitTitle=Title.UNKNOWN;
		}

    }
    public void setTitle(Title tTitle) {
        mtitTitle=tTitle;
    }
    public Title getTitle() {
        return mtitTitle;
    }
    public String getTitleAbbreviation() {
    	String sReturnValue;
    	switch (mtitTitle) {
    		case UNKNOWN:						sReturnValue="?";
    											break;
    		case NONE:							sReturnValue="-";
    											break;
    		case WOMAN_CANDIDATE_MASTER:	 	sReturnValue = "WCM";
												break;
    		case CANDIDATE_MASTER:				sReturnValue = "CM";
												break;
    		case WOMAN_FIDE_MASTER:				sReturnValue = "WFM";
												break;
    		case FIDE_MASTER:					sReturnValue = "FM";
												break;
    		case WOMAN_INTERNATIONAL_MASTER:	sReturnValue = "WIM";
												break;
    		case INTERNATIONAL_MASTER:			sReturnValue = "IM";
												break;
    		case WOMAN_GRANDMASTER: 			sReturnValue = "WGM";
												break;
    		case GRANDMASTER: 					sReturnValue = "GM";
    											break;
    		default:							sReturnValue = "?";
    											break;
    	}	
    	
    	return sReturnValue;
    }
//    public String getTitleInFrench() {
//    	String sReturnValue;
//    	switch (mtitTitle) {
//    		case UNKNOWN:						sReturnValue="?";
//    											break;
//    		case NONE:							sReturnValue="-";
//    											break;
//    		case WOMAN_CANDIDATE_MASTER:	 	sReturnValue = "candidat maître féminin";
//												break;
//    		case CANDIDATE_MASTER:				sReturnValue = "candidat maître";
//												break;
//    		case WOMAN_FIDE_MASTER:				sReturnValue = "maître FIDE féminin";
//												break;
//    		case FIDE_MASTER:					sReturnValue = "maître FIDE";
//												break;
//    		case WOMAN_INTERNATIONAL_MASTER:	sReturnValue = "maître international féminin";
//												break;
//    		case INTERNATIONAL_MASTER:			sReturnValue = "maître international";
//												break;
//    		case WOMAN_GRANDMASTER: 			sReturnValue = "grand maître féminin";
//												break;
//    		case GRANDMASTER: 					sReturnValue = "grand maître";
//    											break;
//    		default:							sReturnValue = "?";
//    											break;
//    	}	
//    	
//    	return sReturnValue;
//    }
    public void setCountryCode(String sCountry) {
    	// TODO: base country codes on IOC list (merge of current and ancients).
        msCountryCode = sCountry.trim();
    }
    public String getCountryCode() {
        return msCountryCode;
    }
    public void setElo(int nElo) {
        mnElo=nElo;
    }
    public int getElo() {
        return mnElo;
    }
    public void setK(int nK) {
    	mnK = nK;
    }
    public int getK() {
    	return mnK;
    }
    public void setBirthYear(int nYear) {
    	mnBirthYear = nYear;
    }
    public void setGamesPlayed(int nGames) {
    	mnGamesPlayed = nGames;
    }
    public int getGamesPlayed() {
    	return mnGamesPlayed;
    }
    public int getBirthYear() {
    	return mnBirthYear;
    }
    public void setSex(Sex sexSex) {
    	msexSex = sexSex;
    }
    public Sex getSex() {
    	return msexSex;
    }
    public char getSexToChar() {
    	if (msexSex==Sex.MALE) {
    		return 'M';
    	}
    	else {
    		return 'F';
    	}
    }
    public void setActivity(Activity actActivity) {
    	mactActivity = actActivity;
    }
    public Activity getActivity() {
    	return mactActivity;
    }
    @Override public String toString() {
        StringBuilder sbResult = new StringBuilder();

        sbResult.append(this.getClass().getName() + " Object {" + "\r\n");
        sbResult.append("ID: " + this.getID() + "\r\n");
        sbResult.append("Name: " + this.getName() + "\r\n");
        sbResult.append("Title: " + this.getTitleAbbreviation() + "\r\n");
        sbResult.append("IOC country code: " + this.getCountryCode() + "\r\n");
        sbResult.append("Elo: " + String.valueOf(this.getElo()) + "\r\n" );
        sbResult.append("Games played: " + String.valueOf(this.getGamesPlayed()) + "\r\n");
        sbResult.append("Birth year: " + String.valueOf(this.getBirthYear()) + "\r\n");
        if (this.getSex() == Sex.MALE) {
            sbResult.append("Sex: male\r\n");
        }
        else {
        	if (this.getSex() == Sex.FEMALE) {
        		sbResult.append("Sex: female\r\n");
        	}
        	else {
        		sbResult.append("Sex: unknown\r\n");
        	}
        }
        if (this.getActivity() == Activity.ACTIVE) {
            sbResult.append("Activity: active\r\n");
        }
        else {
        	if (this.getActivity() == Activity.INACTIVE) {
        		sbResult.append("Activity: inactive\r\n");
        	}
        	else {
        		sbResult.append("Activity: unknown\r\n");
        	}
        }
        sbResult.append("K: " + String.valueOf(this.getK()) + "\r\n" );
        sbResult.append("}");

        return sbResult.toString();
      }
    public String toString_DEBUG() {
        StringBuilder sbResult = new StringBuilder();

        sbResult.append(this.getClass().getName() + " Object {" + "\r\n");
        sbResult.append("ID: >" + this.getID() + "<\r\n");
        sbResult.append("Name: >" + this.getName() + "<\r\n");
        sbResult.append("Title: >" + this.getTitleAbbreviation() + "<\r\n");
        sbResult.append("IOC country code: >" + this.getCountryCode() + "<\r\n");
        sbResult.append("Elo: >" + String.valueOf(this.getElo()) + "<\r\n" );
        sbResult.append("Games played: >" + String.valueOf(this.getGamesPlayed()) + "<\r\n");
        sbResult.append("Birth year: >" + String.valueOf(this.getBirthYear()) + "<\r\n");
        if (this.getSex() == Sex.MALE) {
            sbResult.append("Sex: male\r\n");
        }
        else {
        	if (this.getSex() == Sex.FEMALE) {
        		sbResult.append("Sex: female\r\n");
        	}
        	else {
        		sbResult.append("Sex: unknown\r\n");
        	}
        }
        if (this.getActivity() == Activity.ACTIVE) {
            sbResult.append("Activity: active\r\n");
        }
        else {
        	if (this.getActivity() == Activity.INACTIVE) {
        		sbResult.append("Activity: inactive\r\n");
        	}
        	else {
        		sbResult.append("Activity: unknown\r\n");
        	}
        }
        sbResult.append("K: >" + String.valueOf(this.getK()) + "<\r\n" );
        sbResult.append("}");

        return sbResult.toString();
      }
    public int compareTo(Object objComparedTo) {
        return msName.compareTo(((FIDEPlayer)objComparedTo).msName);
    }    
}
