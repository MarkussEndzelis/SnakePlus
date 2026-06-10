package score;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class UnlockManager {

	private static final String FILE = "unlocks.properties";
	private static final String KEY = "unlocked";
	
	public static Set<String> getUnlocked(){
		Properties props = load();
		Set<String> set = new HashSet<>();
		String raw = props.getProperty(KEY, "Classic");
		for(String s : raw.split(",")) {
			set.add(s.trim());
		}
		return set;
	}
	public static boolean isUnlocked(String skinName) {
		return getUnlocked().contains(skinName);
	}
	public static void unlock(String skinName) {
		Set<String> unlocked = getUnlocked();
		unlocked.add(skinName);
		Properties props = new Properties();
		props.setProperty(KEY, String.join(",", unlocked));
		try(FileOutputStream fos = new FileOutputStream(FILE)){
			props.store(fos, "Snake+ Unlocks");
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
