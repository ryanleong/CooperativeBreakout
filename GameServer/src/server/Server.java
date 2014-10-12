/*
 * 
 * Map and Highscore server for Cooperative breakout game
 * 
 * Four different request types
 * 1. Get map List (Format):   {"request_type": "get_map_list"}
 * 2. Get map (Format):        {"request_type": "get_map", "map_name": "MAP_NAME"}
 * 3. Get score (Format):      {"request_type": "get_score"}
 * 4. Get map (Format):        {"request_type": "write_score", "name": "PLAYER_NAME", "score": 50}
 * 
 */

package server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Server {

	static JSONObject highscores = null;

	public static void main(String args[]) throws Exception {
		
		DatagramSocket serverSocket = new DatagramSocket(6789);
		byte[] receiveData = new byte[1024];

		String requestString = "";

		// Initialize highscores
		highscores = readHighScores("highscores");

		System.out.println("Server running...");

		while (true) {
			receiveData = new byte[1024];

			// Receive packet
			DatagramPacket receivePacket = new DatagramPacket(receiveData,
					receiveData.length);
			serverSocket.receive(receivePacket);
			requestString = new String(receivePacket.getData());
			
			// Start thred to serve client
			new Process(requestString, serverSocket, receivePacket).start();
		}
	}


	/*
	 * Read from file and store as JSON object
	 */
	private static JSONObject readHighScores(String filename) {

		JSONObject mapData = null;
		try {
			FileReader reader = new FileReader(filename);
			mapData = (JSONObject) new JSONParser().parse(reader);
		} catch (FileNotFoundException ex) {
			return null;
		} catch (IOException ex) {
			return null;
		} catch (NullPointerException ex) {
			return null;
		} catch (org.json.simple.parser.ParseException e) {
			return null;
		}
		return mapData;
	}

}
