package at.ac.tuwien.igw.story2go.config;

import java.util.ArrayList;

import android.location.Location;
import android.location.LocationManager;
import at.ac.tuwien.igw.story2go.LocationAudio;
import at.ac.tuwien.igw.story2go.util.GenericXmlParser;

public class ConfigParser extends GenericXmlParser<Story2GoConfigData> {
	private static final String TRIGGER_TAG = "trigger";
	private static final String LATITUDE_TAG = "latitude";
	private static final String LONGITUDE_TAG = "longitude";
	private static final String FILENAME_TAG = "filename";

	private ArrayList<LocationAudio> triggers = new ArrayList<LocationAudio>();
	private LocationAudio trigger;
	private double latitude, longitude;

	@Override
	protected void onStartDocument() {
	}

	@Override
	protected void onStartTag() {
		if (mParser.getName().equals(TRIGGER_TAG)) {
			trigger = new LocationAudio();
			trigger.setLocation(new Location(LocationManager.GPS_PROVIDER));
		} else if (mParser.getName().equals(LATITUDE_TAG)) {
			try {
				latitude = Double.parseDouble(getNextText());
			} catch (NumberFormatException e) {
				latitude = 0;
			}
			trigger.getLocation().setLatitude(latitude);
		} else if (mParser.getName().equals(LONGITUDE_TAG)) {
			try {
				longitude = Double.parseDouble(getNextText());
			} catch (NumberFormatException e) {
				longitude = 0;
			}
			trigger.getLocation().setLatitude(longitude);
		} else if (mParser.getName().equals(FILENAME_TAG)) {
			trigger.setAudioFile(getNextText());
		}
	}

	@Override
	protected void onEndTag() {
		if (mParser.getName().equals(TRIGGER_TAG)) {
			triggers.add(trigger);
		}
	}

	@Override
	protected void onEndDocument() {
		Story2GoConfigData config = new Story2GoConfigData();
		config.setTriggers(triggers);
		mResult = config;
	}

}
