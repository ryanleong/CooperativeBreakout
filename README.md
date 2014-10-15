Cooperative Breakout
===================

Project for the Mobile Computing Systems Programming module.

Player controls a paddle to prevent a ball from going pass them. Ball is 
bounced off paddle to destroy blocks. Player is scored based on time taken 
to destroy all blocks.

There are two modes in the game - Challenge Mode and Arcade Mode
In challenge mode, the player can challenge different levels and is able to download new maps from the server. 
In arcade mode, the player is able to upload his high score to the server if the score is in top 10 in global ranks. 


Responsibilities
===================
|        Name      	|    Student ID 	| 
|  Ryan Leong	|    395463       	|    
|  Siyuan Zhang	|    668506       	|
|  Yi Xia		|    547108       	|
|  Rui Cheng	|    664000       	|

Ryan Leong 395463
Responsibilities: 
- Designing web connection logic 
- Implementation of web server
- Testing of web server
- Making Icons and background images

Siyuan Zhang 668506
Responsibilities:
- Designing game
- Implementation of game
- Implementation of web service on game side
- Designing Screen Layout
- Implementation of local database
- Testing game

Yi Xia 547108
Responsibilities:
- Map Design
- Testing game
- Optimising UI
- Attempt of implementing Multiplayer Mode

Rui Cheng  664000
Responsibilities:
- Map design 
- Testing game
- Optimising UI
- Attempt of implementing Multiplayer Mode


Code - CooperativeBreakout
===================

Activities
---------------------------------
HelpActivity - Manage the help screen
MainActivity - Manage the main game screen
MapSelectionActivity - Manage the map selection screen
ModeSelectionActivity - Manage the mode selection screen
ScoreBoardActivity - Manage the score board screen
WelcomeActivity - Manage the welcome screen

Adapter
---------------------------------
ScoreboardAdapter - help the ScoreBoardActivity to load the records retrieved from the server.

Enum
---------------------------------
GameState - define status of the game

Fragments
---------------------------------
GlobalScoreBoardFragment - Manage the global rank board in the score board screen.
LocalScoreBoardFragment - Manage the local score records in the score board screen.

Objects
---------------------------------
Ball - the ball object in the game
Paddle - the paddle object in the game
Block - the block object in the game

Map - a JSON Object that defines the map 
MapList - a JSON Object that defines the map list
MapMeta - a JSON Object that defines the meta data of each map 
Rank - a JSON Object that defines the rank information
ScoreBoard - a JSON Object that defines the scoreboard
UploadResponse - a JSON Object that defines the response of upload request

Preference 
---------------------------------
AccountPreference - provide read and write operations of account preference
PreferenceUtils - provide basic read and write operations of preference file 

Views
---------------------------------
WorldView - the main game canvas 
ArcadeWorldView - the main game canvas for Arcade Mode game.
BreakoutGame - extends the Application. All activities can get access to the variables of it.

WebService
---------------------------------
DataManager - Provide basic web service operations
WebService - Provide all UDP  operations
HttpManager - Provide all HTTP operations (not used in this game since the server uses UDP)

Code - GameServer
===================
Process - Provide all communication operations
Server - Manage Socket connection

