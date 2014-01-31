package com.philips.cl.disecurity.disecuritysample;

import com.philips.cl.disecurity.DISecurity;
import com.philips.cl.disecurity.KeyDecryptListener;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

	public class DISecuritySampleActivity extends Activity implements KeyDecryptListener {
	
	private static final String TAG = DISecuritySampleActivity.class.getSimpleName();
	
	public final static String pValue = "B10B8F96A080E01DDE92DE5EAE5D54EC52C99FBCFB06A3C6"
			+ "9A6A9DCA52D23B616073E28675A23D189838EF1E2EE652C0"
			+ "13ECB4AEA906112324975C3CD49B83BFACCBDD7D90C4BD70"
			+ "98488E9C219A73724EFFD6FAE5644738FAA31A4FF55BCCC0"
			+ "A151AF5F0DC8B4BD45BF37DF365C1A65E68CFDA76D4DA708"
			+ "DF1FB2BC2E4A4371";

	public final static String gValue = "A4D1CBD5C3FD34126765A442EFB99905F8104DD258AC507F"
			+ "D6406CFF14266D31266FEA1E5C41564B777E690F5504F213"
			+ "160217B4B01B886A5E91547F9E2749F4D7FBD7D3B9A92EE1"
			+ "909D0D2263F80A76A6A24C087A091F531DBF0A0169B6A28A"
			+ "D662A4D18E73AFA32D779D5918D08BC8858F4DCEF97C2A24"
			+ "855E6EEB22B3B2E5";
	private DISecurity diSecurity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disecurity_sample);
		
		diSecurity = new DISecurity(pValue, gValue, this);
		diSecurity.initializeKey("http://192.168.1.1/di/v1/products/0/security", "devId01");
	}

	@Override
	public void keyDecrypt(String key) {
		Log.i(TAG, "key= "+key);
		Log.i(TAG, "Maps of keys= "+ DISecurity.securityHashtable);
	}

}
