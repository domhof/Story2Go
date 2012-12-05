package at.ac.tuwien.igw.story2go;

import android.location.Location;

public interface Story2GoListener {
	void onLocationUpdated(Location location);
	void onTriggerPassed();
}
