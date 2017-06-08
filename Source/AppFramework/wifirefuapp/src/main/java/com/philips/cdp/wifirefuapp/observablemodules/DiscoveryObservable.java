package com.philips.cdp.wifirefuapp.observablemodules;

import com.philips.cdp.dicommclient.discovery.DiscoveryEventListener;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class DiscoveryObservable implements DiscoveryEventListener {
    @Override
    public void onDiscoveredAppliancesListChanged() {
        Observable<String> sampleObservable  = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("Discovered devices");
                e.onComplete();
            }
        });
    }
}
