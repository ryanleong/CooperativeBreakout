README
===============

This folder is an eclipse project. Import to eclipse and run.

Four different request types:
1. Get map List (Format):   {"request_type": "get_map_list"}
		Response: {"name": "Map List","maps": [{"name": "1-0","type": "local","next": "1-1"}]}


2. Get map (Format):        {"request_type": "get_map", "map_name": "MAP_NAME"}
		Response: {"name": "1-1","map": MAP_DATA_AS_ARRAY}


3. Get score (Format):      {"request_type": "get_score"}
		Response: {"name":"highscores", "scores":[{"score":3,"name":"lily"}]}

 4. Write score (Format):        {"request_type": "write_score", "name": "PLAYER_NAME", "score": 50}
 		Response: {"response" : "success", "score" :13, "name": "rick”, rank: 1}
 		
 		
 		
Response for a failed request is:
	{"error": \"ERROR_MESSAGE"}"