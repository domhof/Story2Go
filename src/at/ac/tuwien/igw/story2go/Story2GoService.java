package at.ac.tuwien.igw.story2go;

import java.util.ArrayList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class Story2GoService extends Service implements LocationListener {
	public static final String TAG = Story2GoService.class.getSimpleName();

	private LocationManager locationManager;
	private Location currentLocation;

	private boolean gpsProviderEnabled = false;
	private boolean networkProviderEnabled = false;

	private void initLocationAudio() {
		ArrayList<LocationAudio> locations = new ArrayList<LocationAudio>();
		Location l = new Location("testlocation");
		l.setAltitude(10.0);
		l.setLongitude(20.0);
		locations.add(new LocationAudio(l, "testFile"));

		SharedData.setLocations(locations);
	}

	// @Override
	// protected void onHandleIntent(Intent intent) {
	// locationManager = (LocationManager)
	// getSystemService(Context.LOCATION_SERVICE);
	// locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
	// 0, this);
	//
	// // it is also possible to register an additional provider
	// // locationManager.requestLocationUpdates(
	// // LocationManager.NETWORK_PROVIDER, 0, 0, this);
	//
	// currentLocation = locationManager
	// .getLastKnownLocation(LocationManager.GPS_PROVIDER);
	// // initLocationAudio();
	//
	// while (true) {
	// // Location nextLocation = SharedData.getNextLocation().location;
	// // Location currentLocation = locationManager
	// // .getLastKnownLocation(LocationManager.GPS_PROVIDER);
	//
	// // float distanceTo = nextLocation.distanceTo(currentLocation);
	// // float distanceTo = 0;
	// Log.d(TAG, "Current location (from Service): " + currentLocation);
	// try {
	// Thread.sleep(1000);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	/**
	 * Service
	 */

	@Override
	public void onCreate() {
		super.onCreate();
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, this);
			gpsProviderEnabled = true;
			Log.d(TAG, "GPS Provider is enabled");
		}
		if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, this);
			networkProviderEnabled = true;
			Log.d(TAG, "Network Provider is enabled");
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		locationManager.removeUpdates(this);
	}

	/**
	 * LocationListener
	 */

	@Override
	public void onLocationChanged(Location location) {
		currentLocation = location;
		Log.d(TAG, "Current location: " + currentLocation);
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
}
