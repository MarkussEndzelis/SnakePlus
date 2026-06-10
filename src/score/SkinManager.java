package score;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import skin.SnakeSkin;

public class SkinManager {

	private static final String FILE = "active_skin.properties";
	private static final String KEY = "skin";
	
	public static SnakeSkin getActiveSkin() {
		Properties props = new Properties();
		try(FileInputStream fis = new FileInputStream(FILE)){
			props.load(fis);
		}catch(IOException ignored) {
			
		}
		String name = props.getProperty(KEY, "Classic");
		return SkinRegistry.getByName(name);
	}
	public static void setActiveSkin(String name) {
		Properties props = new Properties();
		props.setProperty(KEY, name);
		try(FileOutputStream fos = new FileOutputStream(FILE)){
			props.store(fos, "Active skin");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
