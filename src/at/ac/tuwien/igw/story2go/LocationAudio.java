package at.ac.tuwien.igw.story2go;

import android.location.Location;

public class LocationAudio {
	public Location location;
	public String audioFile;
	
	public LocationAudio(Location location, String audioFile) {
		this.location = location;
		this.audioFile = audioFile;
	}
}
