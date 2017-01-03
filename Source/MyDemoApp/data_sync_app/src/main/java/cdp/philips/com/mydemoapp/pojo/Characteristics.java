package cdp.philips.com.mydemoapp.pojo;

import java.util.List;

public class Characteristics {
    private String type;
    private String value;

    private List<Characteristics> characteristics;

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setCharacteristics(List<Characteristics> characteristics) {
        this.characteristics = characteristics;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public List<Characteristics> getCharacteristics() {
        return characteristics;
    }
}