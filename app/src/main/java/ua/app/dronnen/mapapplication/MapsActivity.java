package ua.app.dronnen.mapapplication;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity {

	private GoogleMap mMap; // Might be null if Google Play services APK is not available.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		setUpMapIfNeeded();

		wgsView = (TextView) findViewById(R.id.wgs84_coordinate);
		skView = (TextView) findViewById(R.id.sk42_coordinate);
		inverceWgsView = (TextView) findViewById(R.id.inverse_wgs84_coordinate);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	/**
	 * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
	 * installed) and the map has not already been instantiated.. This will ensure that we only ever
	 * call {@link #setUpMap()} once when {@link #mMap} is not null.
	 * <p/>
	 * If it isn't installed {@link SupportMapFragment} (and
	 * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
	 * install/update the Google Play services APK on their device.
	 * <p/>
	 * A user can return to this FragmentActivity after following the prompt and correctly
	 * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
	 * have been completely destroyed during this process (it is likely that it would only be
	 * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
	 * method in {@link #onResume()} to guarantee that it will be called.
	 */
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
					.getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	/**
	 * This is where we can add markers or lines, add listeners or move the camera. In this case, we
	 * just add a marker near Africa.
	 * <p/>
	 * This should only be called once and when we are sure that {@link #mMap} is not null.
	 */
	private void setUpMap() {

		mMap.setMyLocationEnabled(true);
		mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.getUiSettings().setMyLocationButtonEnabled(true);
		Location location = mMap.getMyLocation();
			if(location != null) {
				LatLng myLocation = new LatLng(
					location.getLatitude(),
					location.getLongitude()
			);
			mMap.addMarker(new MarkerOptions().position(myLocation).title("Marker"));
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12));
		}

		mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				mMap.clear();
				mMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.marker)));

				String wgsStr = getString(R.string.wgs84_coordinates) +
						getString(R.string.latitude)+ " " + latLng.latitude + " " +
						getString(R.string.longitude)+ " " + latLng.longitude;
				wgsView.setText(wgsStr);


				double sk42Lat = WGS84_SK42_Translator.WGS84_SK42_Lat(latLng.latitude, latLng.longitude, 0d);
				double sk42Long = WGS84_SK42_Translator.WGS84_SK42_Long(latLng.latitude, latLng.longitude, 0d);

				String sk42Str = getString(R.string.sk42_coordinates) +
						getString(R.string.latitude)+ " " + sk42Lat + " " +
						getString(R.string.longitude)+ " " + sk42Long;
				skView.setText(sk42Str);

				double inverseWgs84Lat = WGS84_SK42_Translator.SK42_WGS84_Lat(sk42Lat, sk42Long, 0d);
				double inverseWgs84Long = WGS84_SK42_Translator.SK42_WGS84_Long(sk42Lat, sk42Long, 0d);

				String invWgsStr = getString(R.string.inverse_wgs84_coordinates) +
						getString(R.string.latitude) + " " + inverseWgs84Lat + " " +
						getString(R.string.longitude) + " " + inverseWgs84Long;
				inverceWgsView.setText(invWgsStr);
			}
		});
	}

	private TextView wgsView;
	private TextView skView;
	private TextView inverceWgsView;
}
