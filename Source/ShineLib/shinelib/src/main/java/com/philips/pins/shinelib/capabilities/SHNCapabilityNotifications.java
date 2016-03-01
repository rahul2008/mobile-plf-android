/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNResultListener;

public interface SHNCapabilityNotifications extends SHNCapability {
    public  enum Type {
        EMAIL,
        IMAGE
    }

    public static class ImageSize {
        private final int width;
        private final int height;

        public ImageSize (int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }

    void showNotificationForType(Type type, byte[] imageData, SHNResultListener shnResultListener);
    void hideNotificationForType(Type type, SHNResultListener shnResultListener);
    void getMaxImageSizeForType(Type type, ResultListener<ImageSize> resultListener);
}
