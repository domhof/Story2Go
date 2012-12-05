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
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener,
		Story2GoListener {
	public static final String TAG = MainActivity.class.getSimpleName();

	private SensorManager sensorManager;
	private Sensor orientationSensor;
	private CompassView compassView;
	private Location nextTriggerLocation;
	private Location currentLocation;

	private TextView textViewDistance;
	private TextView textViewBearing;
	private TextView textViewCompass;
	private TextView textViewCompassAccuracy;

	/**
	 * Activity events
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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

		// Belvedere
		nextTriggerLocation = new Location(LocationManager.GPS_PROVIDER);
		nextTriggerLocation.setLongitude(16.38094672945924);
		nextTriggerLocation.setLatitude(48.19139554219337);
		nextTriggerLocation.setAltitude(0);

		// LocationManager locationManager = (LocationManager)
		// getSystemService(Context.LOCATION_SERVICE);
		// currentLocation = locationManager
		// .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		// Fasangasse 49
		currentLocation = new Location(LocationManager.GPS_PROVIDER);
		currentLocation.setLongitude(16.38612753202644);
		currentLocation.setLatitude(48.1899134428467);
		currentLocation.setAltitude(0);
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
		// see:
		// http://stackoverflow.com/questions/5479753/using-orientation-sensor-to-point-towards-a-specific-location
		// float azimuth = event.values[0];
		// azimuth = azimuth * 180 / (float) Math.PI;
		// GeomagneticField geoField = new GeomagneticField(Double.valueOf(
		// currentLocation.getLatitude()).floatValue(), Double.valueOf(
		// currentLocation.getLongitude()).floatValue(), Double.valueOf(
		// currentLocation.getAltitude()).floatValue(),
		// System.currentTimeMillis());
		// azimuth += geoField.getDeclination();
		// float bearing = currentLocation.bearingTo(nextTriggerLocation);
		// compassView.updateDirection(azimuth - bearing);

		float direction = event.values[0];
		float bearingToNext = currentLocation.bearingTo(nextTriggerLocation);
		float distanceToNext = currentLocation.distanceTo(nextTriggerLocation);
		String accuracy = event.accuracy == SensorManager.SENSOR_STATUS_ACCURACY_HIGH ? "High"
				: event.accuracy == SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM ? "Medium"
						: event.accuracy == SensorManager.SENSOR_STATUS_ACCURACY_LOW ? "Low"
								: event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE ? "Unreliable"
										: "Unknown";

		textViewBearing.setText("Bearing: " + bearingToNext);
		textViewDistance.setText("Distance: " + distanceToNext);
		textViewCompass.setText("Compass: " + direction);
		textViewCompassAccuracy.setText("Compass accuracy: " + accuracy);

		// Lazy bearing calculation but might be adequite for smaller distances
		compassView.updateDirection(direction);
		compassView.updateBearing(direction - bearingToNext);
	}

	@Override
	public void updateLocation(Location location) {
		this.currentLocation = location;
	}
}
