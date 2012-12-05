package at.ac.tuwien.igw.story2go;

import java.util.ArrayList;

public class SharedData {
	private static ArrayList<LocationAudio> locations;
	private static int nextLocationIndex = 0;

	private static Story2GoListener story2GoListener;

	public static LocationAudio getNextLocation() {
		return locations.get(nextLocationIndex);
	}

	public static void nextLocationPassed() {
		nextLocationIndex++;
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

}
