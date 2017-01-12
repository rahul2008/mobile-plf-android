package cdp.philips.com.mydemoapp.pojo;

import java.io.Serializable;
import java.util.List;

public class UserCharacteristics implements Serializable {

    private List<Characteristics> characteristics;

    public void setCharacteristics(List<Characteristics> characteristics) {
        this.characteristics = characteristics;
    }

    public List<Characteristics> getCharacteristics() {
        return characteristics;
    }

}