package at.ac.tuwien.igw.story2go;

import java.util.ArrayList;

public class SharedData {
	private static ArrayList<LocationAudio> locations;
	private static int nextLocationIndex = 0;
	
	public static LocationAudio getNextLocation() {
		return locations.get(nextLocationIndex);
	}
	
	public static void setLocations(ArrayList<LocationAudio> locations) {
		SharedData.locations = locations;
	}
}
