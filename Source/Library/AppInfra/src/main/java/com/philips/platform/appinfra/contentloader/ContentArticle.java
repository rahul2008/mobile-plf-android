/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.appinfra.contentloader;


import java.util.ArrayList;
import java.util.List;

/*
 * Created by 310209604 on 2016-08-12.
 * Article example: https://www.philips.com/wrx/b2c/c/nl/nl/ugrow-app/home.api.v1.offset.(0).limit.(100).json
 */
public class ContentArticle implements ContentInterface {
    // region ContentInterface implementation
    @Override
    public String getId() {
        return uid;
    }

    @Override
    public boolean hasTag(String tag) {
        if (tags == null)
            return false;
        return tags.contains(tag);
    }

    @Override
    public List<Tag> getTags() {
        return tags;
    }

    @Override
    public long getVersion() {
        return modDate;
    }

    @Override
    public boolean isNewer(long version) {
        return (modDate > version);
    }

    @Override
    public boolean parseInput(String json) {
        this.uid = json.substring(7, 12);
        return true;
    }
    // endregion ContentInterface

    // region sub types
  /*  private enum RENDERTYPE {XS, S, M, L};
    private enum ASSETTYPE {IMAGE, YOUTUBEVIDEO, SCENE7VIDEO};
    private class ASSET {
        private RENDERTYPE Rendition;
        private ASSETTYPE type;
        private URL original;
        private URL thumbnail;
        private String caption;
        private String youtubeid;
        private String Scene7id;
    }*/
    // endregion sub types

    // region private members
    private String overlay;
    private String linkurl;
    private String text;
    private String description;
    private String title;
   // private URL previewimage;
   // private URL portraitimage;
    private String imageUrl;
    public List<Tag> tags = new ArrayList<Tag>();
    //private List<Map<String,String>> tags;
    private String link;
   // private ASSET[] assets;
    private long modDate;
    private String uid;

   public class Tag {
       public Boolean isVisibleOnWeb;

       public String name;

       public String key;

       public String id;

   }
}
