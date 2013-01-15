package at.ac.tuwien.igw.story2go;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import at.ac.tuwien.igw.story2go.config.ConfigLoader;
import at.ac.tuwien.igw.story2go.config.Story2GoConfigData;

public class MainActivity extends Activity implements SensorEventListener,
		Story2GoListener {
	public static final String TAG = MainActivity.class.getSimpleName();

	private SensorManager sensorManager;
	private Sensor orientationSensor;
	private CompassView compassView;
	private Location nextTriggerLocation;
	private Location currentLocation;

	/**
	 * UI elements
	 */
	private TextView textViewDistance;
	private TextView textViewBearing;
	private TextView textViewCompass;
	private TextView textViewCompassAccuracy;
	Button buttonBack;
	ImageButton buttonPlay;
	ImageButton buttonPause;
	ImageButton buttonStop;

	/**
	 * Activity events
	 */

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Prepare shared data
		Story2GoConfigData config = ConfigLoader.loadConfig();
		if (config != null) {
			SharedData.setLocations(config.getTriggers());
			if (SharedData.getLocations() == null
					|| SharedData.getLocations().size() == 0) {
				Toast.makeText(
						this,
						"Warning: there are no triggers set in the config file!",
						Toast.LENGTH_LONG).show();
				Log.w(TAG, "there are no triggers set in the config file");
			}
			Log.d(TAG, "Triggers loaded: " + SharedData.getLocations());
		} else {
			Toast.makeText(this, "Error: couldn't find the config file!",
					Toast.LENGTH_LONG).show();
			Log.e(TAG, "couldn't find the config file");
		}
		SharedData.setStory2GoListener(this);

		// Set full screen view
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		// Get UI elements
		textViewDistance = (TextView) findViewById(R.id.textViewDistance);
		textViewBearing = (TextView) findViewById(R.id.textViewBearing);
		textViewCompass = (TextView) findViewById(R.id.textViewCompass);
		textViewCompassAccuracy = (TextView) findViewById(R.id.textViewCompassAccuracy);

		buttonBack = (Button) findViewById(R.id.buttonBack);
		buttonBack.setOnClickListener(backListener);
		buttonPlay = (ImageButton) findViewById(R.id.buttonPlay);
		buttonPlay.setOnClickListener(playListener);
		buttonPause = (ImageButton) findViewById(R.id.buttonPause);
		buttonPause.setOnClickListener(pauseListener);
		buttonStop = (ImageButton) findViewById(R.id.buttonStop);
		buttonStop.setOnClickListener(stopListener);

		// Start service
		Intent intent = new Intent(this, Story2GoService.class);
		startService(intent);

		// Initialize compass
		compassView = (CompassView) findViewById(R.id.compassView);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		orientationSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ORIENTATION);

		// orientationSensor = sensorManager
		// .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		// Initialize demo locations
		nextTriggerLocation = SharedData.getNextLocation().getLocation();

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		currentLocation = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		// Try network provider if GPS position is not available
		if (currentLocation == null) {
			currentLocation = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}

		// All went wrong, assume U6 Tscherttegasse
		if (currentLocation == null) {
			currentLocation = new Location(LocationManager.GPS_PROVIDER);
			currentLocation.setLatitude(48.16571757417265d);
			currentLocation.setLongitude(16.32829959289493d);
			currentLocation.setAltitude(0);

		}

		Log.d(TAG, "=============== STORY INITIALIZED ===============");
		Log.d(TAG,
				"Current Location: "
						+ (currentLocation == null ? "null" : (currentLocation
								.getLatitude() + ";" + currentLocation
								.getLongitude())));
		Log.d(TAG,
				"Next Trigger:     "
						+ (nextTriggerLocation == null ? "null"
								: (nextTriggerLocation.getLatitude() + ";" + nextTriggerLocation
										.getLongitude())));
		Log.d(TAG, "=================================================");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this, orientationSensor,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onDestroy();
		sensorManager.unregisterListener(this);
	}

	/**
	 * Sensor events
	 */

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (currentLocation != null && nextTriggerLocation != null) {
			float direction = event.values[0];
			float bearingToNext = currentLocation
					.bearingTo(nextTriggerLocation);
			float distanceToNext = currentLocation
					.distanceTo(nextTriggerLocation);
			String accuracy = event.accuracy == SensorManager.SENSOR_STATUS_ACCURACY_HIGH ? "High"
					: event.accuracy == SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM ? "Medium"
							: event.accuracy == SensorManager.SENSOR_STATUS_ACCURACY_LOW ? "Low"
									: event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE ? "Unreliable"
											: "Unknown";

			// textViewBearing.setText("Bearing: " + bearingToNext);
			textViewDistance.setText("Distance: " + distanceToNext);
			// textViewCompass.setText("Compass: " + direction);
			textViewCompassAccuracy.setText("Compass accuracy: " + accuracy);

			// Lazy bearing calculation but might be adequate for smaller
			// distances
			compassView.updateDirection(direction);
			compassView.updateBearing(direction - bearingToNext);
		}
	}

	@Override
	public void onLocationUpdated(Location location) {
		this.currentLocation = location;
	}

	@Override
	public void onTriggerPassed() {
		LocationAudio nextLocation = SharedData.getNextLocation();
		if (nextLocation != null)
			this.nextTriggerLocation = nextLocation.location;
	}

	/**
	 * Callbacks
	 */

	private OnClickListener backListener = new OnClickListener() {
		public void onClick(View arg0) {

		}
	};

	private OnClickListener playListener = new OnClickListener() {
		public void onClick(View arg0) {
			if (SharedData.getLastLocation() != null)
				SharedData.getMediaPlayer().start();
		}
	};

	private OnClickListener pauseListener = new OnClickListener() {
		public void onClick(View arg0) {
			if (SharedData.getMediaPlayer().isPlaying())
				SharedData.getMediaPlayer().pause();
		}
	};

	private OnClickListener stopListener = new OnClickListener() {
		public void onClick(View arg0) {
			if (SharedData.getMediaPlayer().isPlaying())
				SharedData.getMediaPlayer().stop();
		}
	};

}
