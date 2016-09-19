package com.philips.platform.appinfra.rest;

/**
 * Created by 310238114 on 9/16/2016.
 */


import android.content.Context;

import com.android.volley.Cache;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;
import com.philips.platform.appinfra.rest.DiskBasedCache.CacheHeader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DiskBasedCacheTest extends MockitoTestCase {
   private Context context;
   private AppInfra mAppInfra;

   @Override
   protected void setUp() throws Exception {
      super.setUp();
      context = getInstrumentation().getContext();
      assertNotNull(context);
      mAppInfra = new AppInfra.Builder().build(context);

   }

   public void testInitialize(){
      Cache cache = new DiskBasedCache(getCacheDir(), 1024, mAppInfra); //
      assertNotNull(cache);
      cache.initialize();
      Cache.Entry e = new Cache.Entry();
      e.data="sample data".getBytes();
      cache.put("key",e);
      assertNotNull(cache.get("key").data);
      //assertEquals(e.data,cache.get("key").data);

   }
    // Simple end-to-end serialize/deserialize test.
     public void testCacheHeaderSerialization() throws Exception {
        Cache.Entry e = new Cache.Entry();
        e.data = new byte[8];
        e.serverDate = 1234567L;
        e.lastModified = 13572468L;
        e.ttl = 9876543L;
        e.softTtl = 8765432L;
        e.etag = "etag";
        e.responseHeaders = new HashMap<String, String>();
        e.responseHeaders.put("fruit", "banana");
        CacheHeader first = new CacheHeader("my-magical-key", e);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        first.writeHeader(baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        CacheHeader second = CacheHeader.readHeader(bais);
         assertEquals(first.key, second.key);
        assertEquals(first.serverDate, second.serverDate);
        assertEquals(first.lastModified, second.lastModified);
        assertEquals(first.ttl, second.ttl);
        assertEquals(first.softTtl, second.softTtl);
        assertEquals(first.etag, second.etag);
        assertEquals(first.responseHeaders, second.responseHeaders);
    }
     public void testSerializeInt() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DiskBasedCache.writeInt(baos, 0);
        DiskBasedCache.writeInt(baos, 19791214);
        DiskBasedCache.writeInt(baos, -20050711);
        DiskBasedCache.writeInt(baos, Integer.MIN_VALUE);
        DiskBasedCache.writeInt(baos, Integer.MAX_VALUE);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        assertEquals(DiskBasedCache.readInt(bais), 0);
        assertEquals(DiskBasedCache.readInt(bais), 19791214);
        assertEquals(DiskBasedCache.readInt(bais), -20050711);
        assertEquals(DiskBasedCache.readInt(bais), Integer.MIN_VALUE);
        assertEquals(DiskBasedCache.readInt(bais), Integer.MAX_VALUE);
    }
     public void testSerializeLong() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DiskBasedCache.writeLong(baos, 0);
        DiskBasedCache.writeLong(baos, 31337);
        DiskBasedCache.writeLong(baos, -4160);
        DiskBasedCache.writeLong(baos, 4295032832L);
        DiskBasedCache.writeLong(baos, -4314824046L);
        DiskBasedCache.writeLong(baos, Long.MIN_VALUE);
        DiskBasedCache.writeLong(baos, Long.MAX_VALUE);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        assertEquals(DiskBasedCache.readLong(bais), 0);
        assertEquals(DiskBasedCache.readLong(bais), 31337);
        assertEquals(DiskBasedCache.readLong(bais), -4160);
        assertEquals(DiskBasedCache.readLong(bais), 4295032832L);
        assertEquals(DiskBasedCache.readLong(bais), -4314824046L);
        assertEquals(DiskBasedCache.readLong(bais), Long.MIN_VALUE);
        assertEquals(DiskBasedCache.readLong(bais), Long.MAX_VALUE);
    }
     public void testSerializeString() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DiskBasedCache.writeString(baos, "");
        DiskBasedCache.writeString(baos, "This is a string.");
        DiskBasedCache.writeString(baos, "ファイカス");
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        assertEquals(DiskBasedCache.readString(bais), "");
        assertEquals(DiskBasedCache.readString(bais), "This is a string.");
        assertEquals(DiskBasedCache.readString(bais), "ファイカス");
    }
     public void testSerializeMap() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Map<String, String> empty = new HashMap<String, String>();
        DiskBasedCache.writeStringStringMap(empty, baos);
        DiskBasedCache.writeStringStringMap(null, baos);
        Map<String, String> twoThings = new HashMap<String, String>();
        twoThings.put("first", "thing");
        twoThings.put("second", "item");
        DiskBasedCache.writeStringStringMap(twoThings, baos);
        Map<String, String> emptyKey = new HashMap<String, String>();
        emptyKey.put("", "value");
        DiskBasedCache.writeStringStringMap(emptyKey, baos);
        Map<String, String> emptyValue = new HashMap<String, String>();
        emptyValue.put("key", "");
        DiskBasedCache.writeStringStringMap(emptyValue, baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        assertEquals(DiskBasedCache.readStringStringMap(bais), empty);
        assertEquals(DiskBasedCache.readStringStringMap(bais), empty); // null reads back empty
        assertEquals(DiskBasedCache.readStringStringMap(bais), twoThings);
        assertEquals(DiskBasedCache.readStringStringMap(bais), emptyKey);
        assertEquals(DiskBasedCache.readStringStringMap(bais), emptyValue);
    }

    public void testPublicMethods() throws Exception {
       Cache cache = new DiskBasedCache(getCacheDir(), 1024, mAppInfra); //
       assertNotNull(cache);
       /* // Catch-all test to find API-breaking changes.
        assertNotNull(DiskBasedCache.class.getConstructor(File.class, int.class));
       // assertNotNull(DiskBasedCache.class.getConstructor(File.class));
        assertNotNull(DiskBasedCache.class.getMethod("getFileForKey", String.class));*/
    }
   private File getCacheDir(){
      return  mAppInfra.getAppInfraContext().getDir("CacheDir", Context.MODE_PRIVATE);
   }
}