package at.ac.tuwien.igw.story2go;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class Story2GoService extends Service implements LocationListener {
	private static final int LOCATION_TOLERANCE = 30;

	public static final String TAG = Story2GoService.class.getSimpleName();

	private LocationManager locationManager;
	private Location currentLocation;

	private MediaPlayer mediaPlayer;

	private boolean gpsProviderEnabled = false;
	private boolean networkProviderEnabled = false;

	private void initLocationAudio() {
		// Mocked data
		ArrayList<LocationAudio> locations = new ArrayList<LocationAudio>();
		Location l1 = new Location("testlocation1");
		l1.setLatitude(20.0);
		l1.setLongitude(30.0);
		LocationAudio locationAudio1 = new LocationAudio(l1, "beep-1.mp3");
		locations.add(locationAudio1);

		Location l2 = new Location("testlocation2");
		l2.setLatitude(30.0);
		l2.setLongitude(20.0);
		LocationAudio locationAudio2 = new LocationAudio(l2, "beep-1.mp3");
		locations.add(locationAudio2);

		SharedData.setLocations(locations);
	}

	private void initLocationManager() {
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

	private void initMediaPlayer() {
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setWakeMode(getApplicationContext(),
				PowerManager.PARTIAL_WAKE_LOCK);
		// mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	}

	private void playAudio(String filename) {
		try {
			mediaPlayer.setDataSource(Environment.getExternalStorageDirectory()
					+ "/" + filename);
			mediaPlayer.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mediaPlayer.start();
	}

	/**
	 * Service
	 */

	@Override
	public void onCreate() {
		super.onCreate();

		initLocationManager();
		initLocationAudio();
		initMediaPlayer();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		locationManager.removeUpdates(this);
		mediaPlayer.release();
		mediaPlayer = null;
	}

	/**
	 * LocationListener
	 */

	@Override
	public void onLocationChanged(Location location) {
		this.currentLocation = location;
		Log.d(TAG, "Current location: " + location);

		playAudioIfInRangeOfNextLocation();
	}

	private void playAudioIfInRangeOfNextLocation() {
		LocationAudio nextLocationAudio = SharedData.getNextLocation();

		if (nextLocationAudio != null
				&& this.currentLocation.distanceTo(nextLocationAudio.location) < LOCATION_TOLERANCE) {
			playAudio(nextLocationAudio.audioFile);
			SharedData.nextLocationPassed();
		}
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
