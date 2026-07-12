package score;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class UsernameManager {
	
	private static final String FILE = "username.properties";
	private static final String KEY = "username";
	
	public static boolean hasUsername() {
		String name = load().getProperty(KEY, "");
		return !name.trim().isEmpty();
	}
	
	public static String getUsername() {
		return load().getProperty(KEY, "Player");
	}
	
	public static void setUsername(String name) {
		Properties props = new Properties();
		props.setProperty(KEY, name);
		try (FileOutputStream fos = new FileOutputStream(FILE)){
			props.store(fos, "Snake+ Username");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Properties load() {
		Properties props = new Properties();
		try(FileInputStream fis = new FileInputStream(FILE)){
			props.load(fis);
		}catch(IOException ignored) {
			
		}
		return props;
	}
}
