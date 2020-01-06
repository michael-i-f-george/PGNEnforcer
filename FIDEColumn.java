package org.chess.pgn;

public class FIDEColumn implements Comparable<FIDEColumn> {
    private String msName="";
    private int mnStart=0;
    private int mnEnd=0;
    private int mnLength=0;
 
    public void setName(String sName) {
        msName=sName.trim().toUpperCase();
    }
    public String getName() {
        return msName;
    }
    public void setStart(int nStart) {
        mnStart=nStart;
    }
    public int getStart() {
        return mnStart;
    }
    public void setEnd(int nEnd) {
        mnEnd=nEnd;
    }
    public int getEnd() {
        return mnEnd;
    }
    public void setLength(int nLength) {
        mnLength=nLength;
    }
    public int getLength() {
        return mnLength;
    }
    public int compareTo(FIDEColumn objComparedTo) {
        return msName.compareTo(((FIDEColumn)objComparedTo).msName);
    }
}
