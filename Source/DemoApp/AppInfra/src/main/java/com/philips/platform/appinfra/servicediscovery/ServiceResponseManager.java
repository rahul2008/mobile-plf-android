package com.philips.platform.appinfra.servicediscovery;

import android.os.Handler;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 310238655 on 6/3/2016.
 */
public class ServiceResponseManager {

    public List readJsonStream(InputStream in) throws IOException {
        List list = null;
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readMessagesArray(reader);

        } catch (Exception e) {
            Log.i("Exception", ""+e);
        } finally{
            reader.close();
        }
        return list;
    }

    public List readMessagesArray(JsonReader reader) throws IOException {
        List messages = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }

    public Message readMessage(JsonReader reader) throws IOException {
//        long id = -1;
        String micrositeId = null;
        UrlsModel urlModel = null;
        List geo = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("micrositeId")) {
//                id = reader.nextLong();
                micrositeId = reader.nextString();
            }
//            else if (name.equals("text")) {
//                text = reader.nextString();
//            } else if (name.equals("geo") && reader.peek() != JsonToken.NULL) {
////                geo = readDoublesArray(reader);
//            }
            else if (name.equals("user")) {
                urlModel = readUrls(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Message(micrositeId, urlModel);
//        return new Message.obtain(new Handler(),id, text, geo);
    }

    public List readDoublesArray(JsonReader reader) throws IOException {
        List doubles = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
//            doubles.add(reader.nextDouble());
        }
        reader.endArray();
        return doubles;
    }
    public TagsModel readTags(JsonReader reader) throws IOException {
        String id = null;
        String nameval= null;
        String key= null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextString();
            } else if (name.equals("name")) {
                nameval = reader.nextString();
            } else if (name.equals("key")) {
                key = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new TagsModel(id, nameval, key);
    }


    public UrlsModel readUrls(JsonReader reader) throws IOException {
        String url1 = null;
        String url2= null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("ugrow.privacy")) {
                url1 = reader.nextString();
            } else if (name.equals("ugrow.terms")) {
                url2 = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new UrlsModel(url1, url2);
    }
}
