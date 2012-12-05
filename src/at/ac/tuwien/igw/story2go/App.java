package at.ac.tuwien.igw.story2go;

import android.app.Application;
import android.content.Context;

/**
 * Helper class to provide a static way to get the app's context
 * http://stackoverflow
 * .com/questions/2002288/static-way-to-get-context-on-android
 * 
 * @author ulrich
 * 
 */
public class App extends Application {
	private static Context context;

	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
	}

	public static Context getContext() {
		return context;
	}
}