/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.servicediscovery.model;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by 310238114 on 6/7/2016.
 */
public class MatchByCountryOrLanguage {

    ArrayList<Config> configs = new ArrayList<>();
    private boolean available;
    private String locale;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public ArrayList<Config> getConfigs() {
        return configs;
    }

    public void setConfigs(ArrayList<Config> configs) {
        this.configs = configs;
    }

    public static class Config {

        private String micrositeId;
        private HashMap<String, String> urls;
        private ArrayList<Tag> tags;

        public String getMicrositeId() {
            return micrositeId;
        }

        public void setMicrositeId(String micrositeId) {
            this.micrositeId = micrositeId;
        }

        public HashMap<String, String> getUrls() {
            return urls;
        }

        public void setUrls(HashMap<String, String> urls) {
            this.urls = urls;
        }

        public ArrayList<Tag> getTags() {
            return tags;
        }

        public void setTags(ArrayList<Tag> tags) {
            this.tags = tags;
        }

        public void parseConfigArray(JSONObject jsonObject) {
            try {
                this.micrositeId = jsonObject.optString("micrositeId");
                urls = new HashMap<>();
                JSONObject urlJSONObject = jsonObject.optJSONObject("urls");
                Iterator<String> iter = urlJSONObject.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    String value = urlJSONObject.getString(key);
                    this.urls.put(key, value);
                }

                this.tags = new ArrayList<>();

                JSONArray tagJSONArray = jsonObject.optJSONArray("tags");
                for (int tagCount = 0; tagCount < tagJSONArray.length(); tagCount++) {
                    MatchByCountryOrLanguage.Config.Tag tag = new MatchByCountryOrLanguage.Config.Tag();
                    tag.setId(tagJSONArray.optJSONObject(tagCount).optString("id"));
                    tag.setName(tagJSONArray.optJSONObject(tagCount).optString("name"));
                    tag.setKey(tagJSONArray.optJSONObject(tagCount).optString("key"));
                    this.tags.add(tag);
                }
                //setTags(tags);
            } catch (JSONException e) {
                ServiceDiscovery.Error err = new ServiceDiscovery.Error(ServiceDiscoveryInterface.
                        OnErrorListener.ERRORVALUES.SERVER_ERROR, "Parsing error");
                ServiceDiscovery result = new ServiceDiscovery();
                //result.setSuccess(false);
                result.setError(err);
            }
        }

        public static class Tag {
            private String id;
            private String name;
            private String key;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }
        }
    }

}
