# Map and Highscore server for Cooperative breakout game
# Four different request 
# 1. Get map List (Format):   {"request_type": "get_map_list"}
# 2. Get map (Format):        {"request_type": "get_map", "map_name": "MAP_NAME"}
# 3. Get score (Format):      {"request_type": "get_score"}
# 4. Get map (Format):        {"request_type": "write_score", "player_name": "PLAYER_NAME", "score": 50}

import socket
import time
import json
import thread

port = 9876
server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server_socket.bind(('', port))

f = open('highscores.json','w')

listOfMaps = {}
highscores = {}

# Read map file
def readMap(mapName):
    mapData = open(mapName)
    data = json.load(mapData)

    return json.dumps(data)

# Parse JSON Strings
def parseJSON(jsonString):
    try:
        decoded = json.loads(jsonString)

        return decoded
        # pretty printing of json-formatted string
        # print json.dumps(decoded, sort_keys=True)

    except (ValueError, KeyError, TypeError):
        print "JSON format error"

def addScore(name, score):

    highscores[name] = score
    f.write(json.dumps(highscores))

def processRequest(requestData):
    # Get map
    if requestData["request_type"] == "get_map":
        try:
            server_socket.sendto(listOfMaps[requestData["map_name"]], address)
        except:
            server_socket.sendto("{\"error\": \"No such map.\"}", address)
    
    # Get map list
    elif requestData["request_type"] == "get_map_list":
        server_socket.sendto(json.dumps(sorted(listOfMaps.keys())), address)

    # Get score
    elif requestData["request_type"] == "get_score":
        server_socket.sendto(json.dumps(highscores), address)

    # Write score
    elif requestData["request_type"] == "write_score":
        try:
            addScore(requestData["player_name"], requestData["score"])
            server_socket.sendto("{\"confirm\": \"Score added.\"}", address)
        except:
            server_socket.sendto("{\"error\": \"Could not add score.\"}", address)


def initialize():
    # Load highscores into Dictionary
    highscoreData = open("highscores.json")
    highscores = json.load(highscoreData)

    # Load map list
    mapListData = open("maplist.json")
    mapList = json.load(mapListData)
       
    for map in mapList["maps"]:
        listOfMaps[map["name"]] = readMap(map["filename"])

    return highscores


# Initlize server
highscores = initialize()


print "Server waiting for client on port", port

while True:
    dataFromClient, address = server_socket.recvfrom(256)

    requestData = parseJSON(dataFromClient)

    # Create two threads as follows
    try:
       thread.start_new_thread(processRequest, (requestData, ))
    except:
       print "Error: unable to start thread"

f.close()
