package at.ac.tuwien.igw.story2go;

import android.location.Location;

public class LocationAudio {
	public Location location;
	public String audioFile;

	public LocationAudio() {

	}

	public LocationAudio(Location location, String audioFile) {
		this.location = location;
		this.audioFile = audioFile;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getAudioFile() {
		return audioFile;
	}

	public void setAudioFile(String audioFile) {
		this.audioFile = audioFile;
	}

	@Override
	public String toString() {
		return "LocationAudio [location=" + location.getLatitude() + ";"
				+ location.getLongitude() + ", audioFile=" + audioFile + "]";
	}
}
