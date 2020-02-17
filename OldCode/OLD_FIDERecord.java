package org.chess.pgn;

public class OLD_FIDERecord implements Comparable {
    private String msNAME="";
    private String msCOUNTRY=""; 
    private String msTITLE="";
    private String msELO="";
    
    public OLD_FIDERecord() {
    }
    public OLD_FIDERecord(String sName) {
        msNAME=sName.trim().toUpperCase();
    }
    public void setNAME(String sName) {
        msNAME=sName.trim().toUpperCase();
    }
    public String getNAME() {
        return msNAME;
    }
    public void setTITLE(String sTitle) {
        msTITLE=sTitle.trim();
    }
    public String getTITLE() {
        return msTITLE;
    }
    public void setCOUNTRY(String sCountry) {
        msCOUNTRY=sCountry.trim();
    }
    public String getCOUNTRY() {
        return msCOUNTRY;
    }
    public void setELO(String sElo) {
        msELO=sElo.trim();
    }
    public String getELO() {
        return msELO;
    }
    public int compareTo(Object objComparedTo) {
        return msNAME.compareTo(((OLD_FIDERecord)objComparedTo).msNAME);
    }    
}
