package org.chess.pgn;

public class PlayerRecord implements Comparable {
    private String msNom_Prenom="";
    private String msElo_Calcul="";
    private String msPart_Calc="";
    private String msFederation="";

    public PlayerRecord() {
    }
    public PlayerRecord(String sNom_Prenom) {
        msNom_Prenom=sNom_Prenom.trim().toUpperCase();
    }
    public void setNom_Prenom(String sNom_Prenom) {
        msNom_Prenom=sNom_Prenom.trim().toUpperCase();
    }
    public String getNom_Prenom() {
        return msNom_Prenom;
    }
    public void setElo_Calcul(String strArg) {
        msElo_Calcul=strArg;
    }
    public String getElo_Calcul() {
        return msElo_Calcul;
    }
    public void setPart_Calc(String sPart_Calc) {
        msPart_Calc=sPart_Calc;
    }
    public String getPart_Calc() {
        return msPart_Calc;
    }
    public void setFederation(String sFederation) {
        msFederation=sFederation;
    }
    public String getFederation() {
        return msFederation;
    }
    public int compareTo(Object objComparedTo) {
        return msNom_Prenom.compareTo(((PlayerRecord)objComparedTo).msNom_Prenom);
    }
    public int compareTo(String sComparedTo) {
        return msNom_Prenom.compareTo(sComparedTo);
    }
}
