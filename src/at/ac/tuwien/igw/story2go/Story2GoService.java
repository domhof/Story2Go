package at.ac.tuwien.igw.story2go;

import java.io.IOException;

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
	private static final String USER_LEFT_TRACK_WAV = "user_left_track.wav";

	private static final int LOCATION_TOLERANCE = 30;

	public static final String TAG = Story2GoService.class.getSimpleName();

	private static final Double MAX_DEVIATION_FROM_PATH = 50.0;

	private LocationManager locationManager;
	private Location currentLocation;

	private boolean gpsProviderEnabled = false;
	private boolean networkProviderEnabled = false;

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
		SharedData.setMediaPlayer(new MediaPlayer());
		SharedData.getMediaPlayer().setWakeMode(getApplicationContext(),
				PowerManager.PARTIAL_WAKE_LOCK);
		// mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	}

	private void playAudio(String filename, boolean abort) {
		if (abort && SharedData.getMediaPlayer().isPlaying())
			return;

		try {
			Log.d(TAG, "Playing audio file: " + filename);
			SharedData.getMediaPlayer().reset();
			SharedData.getMediaPlayer().setDataSource(
					Environment.getExternalStorageDirectory() + "/" + filename);
			SharedData.getMediaPlayer().prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		SharedData.getMediaPlayer().start();
	}

	/**
	 * Service
	 */

	@Override
	public void onCreate() {
		super.onCreate();

		initLocationManager();
		// initLocationAudio();
		initMediaPlayer();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		locationManager.removeUpdates(this);
		SharedData.getMediaPlayer().release();
		SharedData.setMediaPlayer(null);
	}

	/**
	 * LocationListener
	 */

	@Override
	public void onLocationChanged(Location location) {
		this.currentLocation = location;
		Log.d(TAG, "Current location: " + location);

		Story2GoListener story2GoListener = SharedData.getStory2GoListener();
		story2GoListener.onLocationUpdated(location);

		this.warnUserIfNotOnTrack();
		this.playAudioIfInRangeOfNextLocation();
	}

	private void warnUserIfNotOnTrack() {
		if (isUserOffTrack()) {
			Log.d(TAG, "User left track!");
			if (SharedData.getMediaPlayer().isPlaying()) {
				SharedData.getMediaPlayer().stop();
				playAudio(USER_LEFT_TRACK_WAV, true);
				SharedData.setNextLocationToLocationBefore();
			}
		}
	}

	private boolean isUserOffTrack() {
		LocationAudio lastLocation = SharedData.getLastLocation();
		LocationAudio nextLocation = SharedData.getNextLocation();
		if (lastLocation == null || nextLocation == null) {
			Log.d(TAG, "lastLocation or nextLocation is null.");
			return true;
		}

		Double c1 = this.currentLocation.getLatitude();
		Double c2 = this.currentLocation.getLongitude();
		Double a1 = lastLocation.getLocation().getLatitude();
		Double a2 = lastLocation.getLocation().getLongitude();
		Double b1 = nextLocation.getLocation().getLatitude();
		Double b2 = nextLocation.getLocation().getLongitude();
		Double dist = this.calcDistance(c1, c2, a1, a2, b1, b2);
		Log.d(TAG, "Distance:\n" + "\nc1: " + c1 + "\nc2: " + c2 + "\na1: "
				+ a1 + "\na2: " + a2 + "\nb1: " + b1 + "\nb2: " + b2
				+ "\nresult:" + dist.toString());
		return (dist > MAX_DEVIATION_FROM_PATH);
	}

	private Double calcDistance(Double c1, Double c2, Double a1, Double a2,
			Double b1, Double b2) {
		Double ab1 = b1 - a1;
		Double ab2 = b2 - a2;
		Double t = (ab1 * c1 + ab2 * c2 - ab1 * a1 - ab2 * a2)
				/ (Math.pow(ab1, 2) + Math.pow(ab2, 2));
		Double p1 = a1 + t * ab1;
		Double p2 = a2 + t * ab2;
		return Math.sqrt(Math.pow(p1 - c1, 2) + Math.pow(p2 - c2, 2));
	}

	private void playAudioIfInRangeOfNextLocation() {
		LocationAudio nextLocationAudio = SharedData.getNextLocation();

		Log.d(TAG, "Next location: " + nextLocationAudio
				+ "\ncurrent loation: " + this.currentLocation);

		if (nextLocationAudio != null
				&& this.currentLocation.distanceTo(nextLocationAudio.location) < LOCATION_TOLERANCE) {
			playAudio(nextLocationAudio.audioFile, false);
			SharedData.nextLocationPassed();
			Story2GoListener story2GoListener = SharedData
					.getStory2GoListener();
			story2GoListener.onTriggerPassed();
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

	public boolean isGpsProviderEnabled() {
		return gpsProviderEnabled;
	}

	public boolean isNetworkProviderEnabled() {
		return networkProviderEnabled;
	}
}
