package com.philips.cl.di.dev.pa.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.Utils;

public class MapOdActivity extends BaseActivity {
	private GoogleMap mMap;
	private float centerLatF, centerLngF;
	//private String centerCity;
	//private String otherInfo[];
	private ImageView closeMapImg;
	
	private ViewGroup mapLayout;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.od_map_layout);
		
		closeMapImg = (ImageView) findViewById(R.id.oDmapInlarge); 
		closeMapImg.setImageResource(R.drawable.close_icon_2x);
		centerLatF = getIntent().getFloatExtra("centerLatF", 0.0F);
		centerLngF = getIntent().getFloatExtra("centerLngF", 0.0F);
		//centerCity = getIntent().getStringExtra("centerCity");
		//otherInfo = getIntent().getStringArrayExtra("otherInfo");
		mapLayout = (RelativeLayout) findViewById(R.id.include_map);
		if(Utils.isGooglePlayServiceAvailable()) {
			setUpMapIfNeeded();
		} else {
			mapLayout.setVisibility(View.GONE);
		}
		
		closeMapImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }
	
	private void setUpMap() {
		
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(centerLatF, centerLngF)).zoom(13) // Sets the
																		// zoom
				.bearing(0) // Sets the orientation of the camera to east
				.tilt(30) // Sets the tilt of the camera to 30 degrees
				.build(); // Creates a CameraPosition from the builder
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		
		mMap.addMarker(new MarkerOptions().position(new LatLng(centerLatF, centerLngF)));
		
		//Later we can use
		/*mMap.addMarker(new MarkerOptions().position(new LatLng(centerLatF, centerLngF)).icon(
				BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.transparent_bg, centerCity))));
		*/

		
        
    }
	
	/*//Later i will use
	private Bitmap writeTextOnDrawable(int drawableId, String text) {

	    Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
	            .copy(Bitmap.Config.ARGB_8888, true);

	    Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

	    Paint paint = new Paint();
	    paint.setStyle(Style.FILL);
	    paint.setColor(Color.BLACK);
	    paint.setTypeface(tf);
	    paint.setTextAlign(Align.CENTER);
	    paint.setTextSize(Utils.getPxWithRespectToDip(this, 12));

	    Rect textRect = new Rect();
	    paint.getTextBounds(text, 0, text.length(), textRect);

	    Canvas canvas = new Canvas(bm);

	    //Calculate the positions
	    int xPos = canvas.getWidth() / 2 - 2;     //-2 is for regulating the x position offset

	    //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
	    //int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;  
	    int yPos = canvas.getHeight() - 5 ;  
	    canvas.drawText(text, xPos, yPos, paint);

	    return  bm;
	}
*/

}
