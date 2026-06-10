package score;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class CoinManager {

	private static final String FILE = "coins.properties";
	private static final String KEY = "coins";
	
	public static int getCoins() {
		Properties props = load();
		try {
			return Integer.parseInt(props.getProperty(KEY, "0"));
		}catch(NumberFormatException e) {
			return 0;
		}
	}
	public static void addCoins(int amount) {
		int current = getCoins();
		save(current + amount);
	}
	public static boolean spend(int amount) {
		int current = getCoins();
		if(current < amount) {
			return false;
		}
		save(current - amount);
		return true;
	}
	private static void save(int amount) {
		Properties props = new Properties();
		props.setProperty(KEY, String.valueOf(amount));
		try(FileOutputStream fos = new FileOutputStream(FILE)){
			props.store(fos, "Snake+ Coins");
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
