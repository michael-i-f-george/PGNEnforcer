# PGNEnforcer
Helper to record a chess game fully respectful of the PGN standard

# Details

* **Events.java** contains only getters and setters, for the following properties:
  * Name,
  * Site,
  * StartDate,
  * EndDate (Date of the last game),
  * LastRound (How many rounds in this event?),
  * UserTeam (in a team event, the player plays always in the same team),
  * Time (!?),
  * TimeControl,
  * Section,
  * DayOfPlay ("Saturday", "Sunday",...).


* FIDEColumn.java
* FIDEPlayer.java
* FIDEPlayerList.java
* FIDERatingList.java
* GameResult.java
* HSQLDB.java
* Main.java
* NewGamePanel.java
* PGNGame.java
* PGNStream.java, allows
  * to read a PGN file,
  * to write a PGN file,
  * to export a PGN to LaTeX,
* Performance.java
* PlayerDBF.java
* PlayerDBF.java.8859.txt
* PlayerRecord.java
* RatedPGNGame.java
* Rating.java
* TagsPanel.java
* XMLEventsHandler.java
* XMLFIDERatingsHandler.java
* tinySQL-2.26.jar
