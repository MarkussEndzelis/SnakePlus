package score;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class LeaderboardManager {
	
	private static final String PROJECT_ID = "snakeplus-c96de";
	private static final String API_KEY = "AIzaSyDzpksNq-RCA846lqlutl2_cfCEarPY7cQ";
	private static final String BASE_URL = "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID + "/databases/(default)/documents/leaderboard";
	
	public static void submitScore(String username, String mapName, int score) {
		new Thread(() -> {
			try {
				String url = BASE_URL + "?key=" + API_KEY;
				String json = "{" + "\"fields\": {" 
					+ "\"username\": {\"stringValue\": \"" + username + "\"}," 
					+ "\"map\": {\"stringValue\": \"" + mapName + "\"},"
					+ "\"score\": {\"integerValue\": " + score + "}"
					+ "}}";
				
				HttpURLConnection conn = (HttpURLConnection)
					new URL(url).openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setDoOutput(true);
				conn.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
				conn.getResponseCode();
				conn.disconnect();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	public static String fetchTopScores(String mapName) {
		HttpURLConnection conn = null;
		try {
			String url = "https://firestore.googleapis.com/v1/projects/" + PROJECT_ID + "/databases/(default)/documents:runQuery?key=" + API_KEY;
			
			String json = "{"
					+ "\"structuredQuery\": {"
	                + "\"from\": [{\"collectionId\": \"leaderboard\"}],"
	                + "\"where\": {\"fieldFilter\": {"
	                + "\"field\": {\"fieldPath\": \"map\"},"
	                + "\"op\": \"EQUAL\","
	                + "\"value\": {\"stringValue\": \"" + mapName + "\"}"
	                + "}},"
	                + "\"orderBy\": [{\"field\": {\"fieldPath\": \"score\"}, \"direction\": \"DESCENDING\"}],"
	                + "\"limit\": 10"
	                + "}}";
			
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);
			conn.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
			
			int responseCode = conn.getResponseCode();
			InputStream is = (responseCode >= 200 && responseCode < 300) ? conn.getInputStream() : conn.getErrorStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null) {
				sb.append(line);
			}
			conn.disconnect();
			
			if (responseCode >= 200 && responseCode < 300) {
				return sb.toString();
			}else {
				System.out.println("Firestore error " + responseCode + ": " + sb.toString());
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
