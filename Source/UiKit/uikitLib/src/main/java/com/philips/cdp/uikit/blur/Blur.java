package com.philips.cdp.uikit.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

public class Blur {


    public static Bitmap apply(Context context, Bitmap sentBitmap) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        final RenderScript rs = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            script.setRadius(5);
        } else {
            script.setRadius(10);
        }
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);

        // clean up renderscript resources
        rs.destroy();
        input.destroy();
        output.destroy();
        script.destroy();

        return bitmap;
    }
}
