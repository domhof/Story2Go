package at.ac.tuwien.igw.story2go;

import java.util.ArrayList;

import android.media.MediaPlayer;

public class SharedData {
	private static ArrayList<LocationAudio> locations;
	private static int nextLocationIndex = 0;

	private static String username = "";

	private static Story2GoListener story2GoListener;

	private static MediaPlayer mediaPlayer;

	public static LocationAudio getNextLocation() {
		if (nextLocationIndex >= locations.size())
			return null;
		return locations.get(nextLocationIndex);
	}

	public static LocationAudio getLastLocation() {
		if (nextLocationIndex == 0)
			return null;
		else
			return locations.get(nextLocationIndex - 1);
	}

	public static void nextLocationPassed() {
		nextLocationIndex++;
	}

	public static void setNextLocationToLocationBefore() {
		if (nextLocationIndex >= 1)
			nextLocationIndex--;
	}

	public static void setLocations(ArrayList<LocationAudio> locations) {
		SharedData.locations = locations;
	}

	public static Story2GoListener getStory2GoListener() {
		return story2GoListener;
	}

	public static void setStory2GoListener(Story2GoListener story2GoListener) {
		SharedData.story2GoListener = story2GoListener;
	}

	public static ArrayList<LocationAudio> getLocations() {
		return locations;
	}

	public static MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public static void setMediaPlayer(MediaPlayer mediaPlayer) {
		SharedData.mediaPlayer = mediaPlayer;
	}

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		SharedData.username = username;
	}

}
