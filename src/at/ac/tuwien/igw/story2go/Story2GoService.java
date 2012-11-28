package at.ac.tuwien.igw.story2go;

import java.util.ArrayList;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class Story2GoService extends IntentService {
	public static final String TAG = Story2GoService.class.getSimpleName(); 
	private LocationManager locationManager;
	private LocationListener locationListener;
		
	public Story2GoService() {
		super("Story2GoService");
		Log.d(TAG, "start service");
		
		//initLocationAudio();
		
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
//		
//		locationListener = new LocationListener() {
//			@Override
//		    public void onLocationChanged(Location location) {
//				
//		    }
//
//		    public void onStatusChanged(String provider, int status, Bundle extras) {}
//
//		    public void onProviderEnabled(String provider) {}
//
//		    public void onProviderDisabled(String provider) {}
//		  };
//
//		// Register the listener with the Location Manager to receive location updates
//		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}

	private void initLocationAudio() {
		ArrayList<LocationAudio> locations = new ArrayList<LocationAudio>();
		Location l = new Location("testlocation");
		l.setAltitude(10.0);
		l.setLongitude(20.0);
		locations.add(new LocationAudio(l,"testFile"));
		
		SharedData.setLocations(locations);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
//		while (true) {
//			Location nextLocation = SharedData.getNextLocation().location;
//			//Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//			
//			//float distanceTo = nextLocation.distanceTo(currentLocation);
//			float distanceTo = 0;
//			Log.d(TAG, "next location: " + distanceTo);
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
	}

}
