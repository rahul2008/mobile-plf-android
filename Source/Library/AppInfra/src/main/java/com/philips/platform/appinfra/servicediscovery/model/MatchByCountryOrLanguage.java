/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.servicediscovery.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 310238114 on 6/7/2016.
 */
public class MatchByCountryOrLanguage {
    boolean available;
    String locale;
    ArrayList<Config> configs;

    public static class Config {
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

        private String micrositeId;
        private HashMap<String , String > urls;
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
    }


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

}
