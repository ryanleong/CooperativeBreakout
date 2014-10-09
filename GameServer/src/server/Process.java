package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Process extends Thread {

	String requestString = "";
	DatagramSocket serverSocket;
	DatagramPacket receivePacket;

	public Process(String requestString, DatagramSocket serverSocket, DatagramPacket receivePacket) {
		this.requestString = requestString;
		this.serverSocket = serverSocket;
		this.receivePacket = receivePacket;
	}

	@Override
	public void run() {
		JSONObject requestObject = null;
		byte[] sendData = new byte[1024];

		// Parse string to JSONObject
		try {
			System.out.println(requestString);
			requestObject = (JSONObject) new JSONParser().parse(requestString
					.trim());
		} catch (ParseException pe) {
			System.out.println(": position " + pe.getPosition());
			System.out.println(pe);
			pe.printStackTrace();
			return;
		}

		// Handle request
		if (requestObject.get("request_type").equals("get_map"))
			sendData = readFromFile(requestObject.get("map_name").toString())
					.getBytes();
		else if (requestObject.get("request_type").equals("get_map_list"))
			sendData = readFromFile("maplist").getBytes();
		else if (requestObject.get("request_type").equals("write_score")) {
			if (writeScore(requestObject)) {
				sendData = ("{\"response\" : \"success\", \"score\" :"
						+ requestObject.get("score") + ", \"name\": \""
						+ requestObject.get("name") + "\"}").getBytes();
			} else
				sendData = "{\"error\": \"Failed to save highscore.\"}"
						.getBytes();
		} else
			sendData = prepareScores().getBytes();

		// Send response
		InetAddress IPAddress = receivePacket.getAddress();
		int port = receivePacket.getPort();
		DatagramPacket sendPacket = new DatagramPacket(sendData,
				sendData.length, IPAddress, port);
		try {
			serverSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Served " + receivePacket.getAddress() + " with "
				+ requestObject.get("request_type"));
	}

	/*
	 * Read raw JSON from file
	 */
	private String readFromFile(String filename) {
		BufferedReader br = null;
		String mapData = "";

		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(filename));

			while ((sCurrentLine = br.readLine()) != null)
				mapData += sCurrentLine;
		} catch (IOException e) {
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
			}
		}
		// Return error message if no such file
		if (mapData.equals(""))
			return "{\"error\": \"No such map.\"}";

		return mapData.replace("\t", "");
	}

	/*
	 * Stores and writes scores to file
	 */
	@SuppressWarnings("unchecked")
	private static boolean writeScore(JSONObject newScore) {
		boolean isWritten = false;
		JSONArray scores = (JSONArray) Server.highscores.get("scores");
		JSONArray sortedScores = new JSONArray();
		boolean isLargest = true;

		// Store new score into list of scores
		for (int i = 0; i < scores.size(); i++) {
			JSONObject temp = (JSONObject) scores.get(i);

			if ((long) newScore.get("score") < (long) temp.get("score")) {
				JSONObject newScoreObject = new JSONObject();
				newScoreObject.put("name", newScore.get("name"));
				newScoreObject.put("score", newScore.get("score"));

				sortedScores.add(newScoreObject);
				isLargest = false;
			}

			if (!temp.get("name").equals(newScore.get("name")))
				sortedScores.add(scores.get(i));
		}

		// If new score is the largest
		if (isLargest) {
			JSONObject newScoreObject = new JSONObject();
			newScoreObject.put("name", newScore.get("name"));
			newScoreObject.put("score", newScore.get("score"));

			sortedScores.add(newScoreObject);
		}

		// Append JSON name
		JSONObject outputObject = new JSONObject();
		outputObject.put("name", "highscores");
		outputObject.put("scores", sortedScores);
		Server.highscores = outputObject;

		// Write to file
		try {
			File file = new File("highscores");
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(outputObject.toString());
			bw.close();
			isWritten = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isWritten;
	}
	
	/*
	 * Prepare scores to be sent on request
	 */
	@SuppressWarnings("unchecked")
	private static String prepareScores() {
		JSONArray top10 = new JSONArray();
		JSONArray scores = (JSONArray) Server.highscores.get("scores");

		// Get top 10 scores
		for (int i = 0; i < 10; i++) {
			try {
				top10.add(scores.get(i));
			} catch (Exception e) {
				break;
			}
		}

		// Appended with metadata
		JSONObject outputObject = new JSONObject();
		outputObject.put("name", "highscores");
		outputObject.put("scores", top10);

		return outputObject.toString();
	}
}
