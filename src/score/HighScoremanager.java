package score;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class HighScoremanager {

	private static final String FILE = "hgihscores.properties";
	private static final int MAX_SCORES = 5;
	
	public static List<Integer> getScores(String mapName){
		Properties props = load();
		String raw = props.getProperty(sanitize(mapName), "");
		List<Integer> scores = new ArrayList<>();
		if(!raw.isEmpty()) {
			for(String s : raw.split(",")) {
				try {
					scores.add(Integer.parseInt(s.trim()));
				}catch(NumberFormatException ignored) {
					
				}
			}
		}
		return scores;
	}
	public static boolean isHighScore(String mapName, int score) {
		List<Integer> scores = getScores(mapName);
		return scores.size() < MAX_SCORES || score > scores.get(scores.size() - 1);
	}
	public static void saveScore(String mapName, int score) {
		List<Integer> scores = getScores(mapName);
		scores.add(score);
		scores.sort(Collections.reverseOrder());
		if(scores.size() > MAX_SCORES) {
			scores = scores.subList(0, MAX_SCORES);
		}
		
		Properties props = load();
		props.setProperty(sanitize(mapName), join(scores));
		save(props);
	}
	private static String sanitize(String name) {
		return name.toLowerCase().replaceAll("[^a-z0-9]", "_");
	}
	private static String join(List<Integer> scores) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < scores.size(); i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(scores.get(i));
		}
		return sb.toString();
	}
	private static Properties load() {
		Properties props = new Properties();
		try(FileInputStream fis = new FileInputStream(FILE)){
			props.load(fis);
		}catch(IOException ignored) {
			
		}
		return props;
	}
	private static void save(Properties props) {
		try(FileOutputStream fos = new FileOutputStream(FILE)){
			props.store(fos, "Snake+ High Scores");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
