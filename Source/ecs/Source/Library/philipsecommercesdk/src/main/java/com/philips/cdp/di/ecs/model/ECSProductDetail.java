package com.philips.cdp.di.ecs.model;

import com.philips.cdp.di.ecs.model.asset.Assets;
import com.philips.cdp.di.ecs.model.disclaimer.Disclaimers;

public class ECSProductDetail {

    Assets assets;
    Disclaimers disclaimers;

    public Assets getAssets() {
        return assets;
    }

    public void setAssets(Assets assets) {
        this.assets = assets;
    }

    public Disclaimers getDisclaimers() {
        return disclaimers;
    }

    public void setDisclaimers(Disclaimers disclaimers) {
        this.disclaimers = disclaimers;
    }
}
