# FaStats
FaStats (portmanteau of the words "fast" and "stats") is an Android app designed for recording and sharing sports statistics in a team setting. The app uses a Google Firebase Realtime Database to allow users to upload data to the cloud and have other users instantly access and update data. FaStats primarily utilizes the Java and XML languages, and this project was created for the 2018 Congressional App Challenge and was the winner for California's Congressional District 26.

## User-Defined Data Types
FaStats uses numerous user-defined data types to assist in storing the sports statistics in an efficient way that is easy for different Activities to access and for the database to store. These data types can be found [here](https://github.com/njuan123/FaStats/tree/master/app/src/main/java/com/example/android/my_app).

### Team
A Team object has a name, sport, record, list of members, and list of Matches. When a new Team object is created, the lists for members and Matches are empty and can be added through the app's functionality.

### Match
A Match object has a date, opponent, status, and list of PlayerStatistics. The status displays the result of the match, if completed, or "In Progess" if not finished, and the list assigns a PlayerStatistic to each player in the team when the Match is defined.

### PlayerStatistic
A PlayerStatistic contains a name and a list of Statistics which correlates to the number of Statistics and type of Statistic that were selected when the Match was being defined. The attribute isFinished locks the ability to change the Statistics as soon as one user claims this PlayerStatistic so that only one user records the stats for a player at a time and once the user is done, that data can be viewed but not changed.

### Statistic
A Statistic object has a name for the statistic to be tracked and a count. If the Statistic is defined as type I (total tally), then the count is just one number; if the Statistic is defined as type II (made/missed), count is a list of two integers. 


## Activities
The Activity Java files correspond to a distinct page within FaStats. Each Activity is associated with an XML file located [here](https://github.com/njuan123/FaStats/tree/master/app/src/main/res/layout), where the UI of each page is defined, and it adds the functionality to each page, such as button presses, text edit boxes, and more. 

#### [LoaderActivity](https://github.com/njuan123/FaStats/blob/master/app/src/main/java/com/example/android/my_app/LoaderActivity.java)
The LoaderActivity is the page that is loaded upon opening the app, and it contains the list of Teams that the app user is a member of. Selection of a Team opens a TeamActivity.

<img src="https://github.com/njuan123/FaStats/blob/master/appimages/LoaderActivity.png" width = "250" title="LoaderActivity">

&nbsp;
#### [TeamActivity](https://github.com/njuan123/FaStats/blob/master/app/src/main/java/com/example/android/my_app/TeamActivity.java)
This is where all of the past and current Matches for a Team can be accessed. On this page, a list of Players can be accessed and updated via the "Players" button on the top right. Hitting the "Create New Match" button opens a CreateMatchActivity where a new Match can be declared. Finally, an existing Match can be accessed by clicking it, which opens a MatchActivity.

<img src="https://github.com/njuan123/FaStats/blob/master/appimages/TeamActivity.png" width = "250" title="TeamActivity">

&nbsp;
#### [CreateMatchActivity](https://github.com/njuan123/FaStats/blob/master/app/src/main/java/com/example/android/my_app/CreateMatchActivity.java)
A CreateMatchActivity is the page where a new Match for a Team can be created. The opponent and date can be selected, and the number of Statistics to be recorded for each Player, as well as the type of Statistic, are all chosen here. Once the "Create Match" button has been clicked, a new Match is defined and added to the corresponding Team, and a MatchActivity is launched.

<img src="https://github.com/njuan123/FaStats/blob/master/appimages/CreateMatchActivity.png" width = "250" title="CreateMatchActivity">

&nbsp;
#### [MatchActivity](https://github.com/njuan123/FaStats/blob/master/app/src/main/java/com/example/android/my_app/MatchActivity.java)
This page is the summary page for a particular match. To access a certain PlayerStatistic, click on a player's name, which opens either a PlayerStatActivity if the stats are yet to be recorded or a PlayerStatSummary if the stats have been completed. If someone else is currently recording stats for that player, no new Activity will open; instead, an error will pop up.

<img src="https://github.com/njuan123/FaStats/blob/master/appimages/MatchActivity.png" width = "250" title="MatchActivity">

&nbsp;
#### [PlayerStatActivity](https://github.com/njuan123/FaStats/blob/master/app/src/main/java/com/example/android/my_app/PlayerStatActivity.java)
The PlayerStatActivity page is where the user records the actual stats of a player. Each Statistic will either have one button to tally the total or two buttons, one for a make and one for a miss. Once finished, the "Done" button on the top right will mark the PlayerStatistic as complete, upload the stats to the database, and open a PlayerStatSummary to show the results. 

<img src="https://github.com/njuan123/FaStats/blob/master/appimages/PlayerStatActivity.png" width = "250" title="PlayerStatActivity">

&nbsp;
#### [PlayerStatSummary](https://github.com/njuan123/FaStats/blob/master/app/src/main/java/com/example/android/my_app/PlayerStatSummary.java)
The PlayerStatSummary Activity displays the finished statistics for a corresponding Player. Type I Statistics display the total count, while Type II Statistics display the percentage made with a circle filled with the corresponding percentage. This data is loaded from the Realtime Database and cannot be changed from this Activity. Any user in the team can access this data, not just the user who recorded the stats in the corresponding PlayerStatActivity.

<img src="https://github.com/njuan123/FaStats/blob/master/appimages/PlayerStatSummary.png" width = "250" title="PlayerStatSummary">

&nbsp;
## Google Firebase Realtime Database
In order to allow different users to access each other's data, a realtime database is used, where the information is stored in the same structure as the user-defined data types. Data is loaded to and from the database directly rather than storing data on each user's local phone so that there will not be any discrepancies when sharing data. To do this, every time a Team is created, a Match is created or finished, a Player is added, or a PlayerStatistic is finished, the data is written to the database. When a user hits a button to go to a new Activity, data that has changed is automatically updated in the database so that the user does not need to manually upload the data to the database. Furthermore, each Activity utilizes OnChangeListeners so that data on each user's device is updated when there is a change in the database.
