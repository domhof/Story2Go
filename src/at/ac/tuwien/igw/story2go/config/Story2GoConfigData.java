package at.ac.tuwien.igw.story2go.config;

import java.util.ArrayList;

import at.ac.tuwien.igw.story2go.LocationAudio;

public class Story2GoConfigData {
	ArrayList<LocationAudio> triggers;

	public ArrayList<LocationAudio> getTriggers() {
		return triggers;
	}

	public void setTriggers(ArrayList<LocationAudio> triggers) {
		this.triggers = triggers;
	}
}
