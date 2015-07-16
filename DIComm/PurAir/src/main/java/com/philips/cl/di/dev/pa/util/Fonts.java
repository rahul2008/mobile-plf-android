package com.philips.cl.di.dev.pa.util;

import android.content.Context;
import android.graphics.Typeface;

public class Fonts {
	public static final String BOOT_STRAP_KEY_4 = "MTVlOGVjZ" ;
	public static Typeface getGillsans(Context context) {
		if(gillsans == null) {
			gillsans = Typeface.createFromAsset(context.getAssets(), "fonts/gillsanslight.ttf");
		}
		return gillsans;
	}
	public static Typeface getGillsansLight(Context context) {
		if(gillsansLight == null) {
			gillsansLight = Typeface.createFromAsset(context.getAssets(), "fonts/gillsanslight.ttf");
		}
		return gillsansLight;
	}
	public static Typeface getOpensansCondBold(Context context) {
		if(opensansCondBold == null) {
			opensansCondBold = Typeface.createFromAsset(context.getAssets(), "fonts/opensanscondbold.ttf");
		}
		return opensansCondBold;
	}
	public static Typeface getOpensansCondLight(Context context) {
		if(opensansCondLight == null) {
			opensansCondLight = Typeface.createFromAsset(context.getAssets(), "fonts/opensanscondlight.ttf");
		}
		return opensansCondLight;
	}
	public static Typeface getOpensansCondLightItalic(Context context) {
		if(opensansCondLightItalic == null) {
			opensansCondLightItalic = Typeface.createFromAsset(context.getAssets(), "fonts/opensanscondlightitalic.ttf");
		}
		return opensansCondLightItalic;
	}

    public static Typeface getCentraleSansLight(Context context) {
        if(centraleSansLight == null) {
            centraleSansLight = Typeface.createFromAsset(context.getAssets(), "fonts/centralesanslight.ttf");
        }
        return centraleSansLight;
    }

    public static Typeface getCentraleSansMedium(Context context) {
        if(centraleSansMedium == null) {
            centraleSansMedium = Typeface.createFromAsset(context.getAssets(), "fonts/centralesansmedium.ttf");
        }
        return centraleSansMedium;
    }
    
    public static Typeface getCentraleSansBook(Context context) {
        if(centraleSansBook == null) {
        	centraleSansBook = Typeface.createFromAsset(context.getAssets(), "fonts/centralesansbook.ttf");
        }
        return centraleSansBook;
    }
    
    public static Typeface getCentraleSansBold(Context context) {
        if(centraleSansBold == null) {
        	centraleSansBold = Typeface.createFromAsset(context.getAssets(), "fonts/centralesansbold.ttf");
        }
        return centraleSansBold;
    }
    
    public static Typeface getCentraleSansXBold(Context context) {
        if(centraleSansXBold == null) {
        	centraleSansXBold = Typeface.createFromAsset(context.getAssets(), "fonts/centralesansxbold.ttf");
        }
        return centraleSansXBold;
    }
	
	private static Typeface gillsans;
	private static Typeface gillsansLight;
	private static Typeface opensansCondBold;
	private static Typeface opensansCondLight;
	private static Typeface opensansCondLightItalic;
    private static Typeface centraleSansLight;
    private static Typeface centraleSansMedium;
    private static Typeface centraleSansBook;
    private static Typeface centraleSansBold;
    private static Typeface centraleSansXBold;
}
