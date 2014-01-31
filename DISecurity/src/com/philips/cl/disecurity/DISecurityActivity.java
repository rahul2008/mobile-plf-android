package com.philips.cl.disecurity;

import android.os.Bundle;
import android.app.Activity;

public class DISecurityActivity extends Activity {
	
	//private final String TAG = DISecurityActivity.class.getSimpleName();
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_di_security);
				
		
		//diSecurity = new DiSecurity(pValue, gValue, Integer.toString(randomNum));
		//diSecurity.getKey() ;
		
		//String hellman = "43e53656454426b2c75b773965cc3e5d57a536eadc97917746d24061d574eacb4bffd7c3254d0cc80a4c6a2f286020e4f37508c8f281e4e47b9aeba3fef1be00358aa43449aa3ed94ae2acc37b8b6110ebc318c96814bd00419e90b6634a26848f1ca4cb07d4b6e9271d0e9f4f6d29c492a763397c5c73b3ff013bd36e55da7e";
		
		//String secKey = diSecurity.getSecretKey(hellman);
		//System.out.println("Sec Key==="+secKey);
		
		//String encKey = "5abe750ccc2260b9bc0e3cf3f8176d900c0d1412b0f4637920024af099be897c";
		//byte[] encrytedData = encKey.getBytes();
		//[] encData = Util.toByte(encKey);

		
		/*try {
			byte[] decrytedData = Util.getDecryptData(encData, secKey);
			
			for(byte bb : decrytedData) {
				System.out.println("bbbbb=== " +bb);
			}
			
			Log.i(TAG, "decrytedData== " + new String(decrytedData, "UTF-16"));
			//4��8xׁE�[lO�
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		/*byte[] encrytedData = null;
		try {
			encrytedData = diSecurity.getEncryptedData(
							"Hi, I am Md. Manzer Hassan from Bihar, India. I am working in Philips as a software engineer.",
							secKey);
			Log.i(TAG, "encrytedData== " + encrytedData+"     "+new String(encrytedData));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			byte[] decrytedData = diSecurity.getDecryptData(encrytedData, secKey);
			Log.i(TAG, "decrytedData== " + new String(decrytedData));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//Log.i(TAG, diffieKey);
	}

	/*private int randInt(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}*/

}
