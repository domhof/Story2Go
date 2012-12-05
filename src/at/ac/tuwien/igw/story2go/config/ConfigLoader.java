package at.ac.tuwien.igw.story2go.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.os.Environment;
import android.util.Log;

public class ConfigLoader {
	private static final String TAG = ConfigLoader.class.getSimpleName();
	private static final String CONFIG_FILE_NAME = "story2go_config.xml";

	public static Story2GoConfigData loadConfig() {
		String externalStoragePath = Environment
				.getExternalStoragePublicDirectory("").getAbsolutePath() + "/";
		File configFile = new File(externalStoragePath + CONFIG_FILE_NAME);

		try {
			FileInputStream fis = new FileInputStream(configFile);
			ConfigParser cp = new ConfigParser();
			return cp.parse(fis);
		} catch (FileNotFoundException e) {
			Log.w(TAG, "config file not found");
			return null;
		}
	}

}
