package cdp.philips.com.mydemoapp.characteristics;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.CharacteristicsDetail;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

import java.util.List;

import cdp.philips.com.mydemoapp.pojo.UserCharacteristics;

/**
 * Created by indrajitkumar on 1/17/17.
 */

public class CharacteristicsPresenter {
    private DataServicesManager mDataServicesManager;
    private Characteristics mCharacteristics;
    private final DBRequestListener dbRequestListener;

    CharacteristicsPresenter(DBRequestListener dbRequestListener) {
        this.dbRequestListener = dbRequestListener;
        mDataServicesManager = DataServicesManager.getInstance();
        mCharacteristics = mDataServicesManager.createCharacteristics();
    }

    boolean createOrUpdateCharacteristics(String userCharacteristics) {
        try {
            UserCharacteristics mUserCharacteristics = parseUserCharacteristics(userCharacteristics);
            if (mUserCharacteristics == null || mUserCharacteristics.getCharacteristics() == null)
                return false;
            for (int i = 0; i < mUserCharacteristics.getCharacteristics().size(); i++) {
                String type = mUserCharacteristics.getCharacteristics().get(i).getType();
                String value = mUserCharacteristics.getCharacteristics().get(i).getValue();
                CharacteristicsDetail characteristicsDetail = mDataServicesManager.createCharacteristicsDetails(mCharacteristics, type, value, null);
                saveUserCharacteristicsToLocalDBRecursively(characteristicsDetail, mUserCharacteristics.getCharacteristics().get(i).getCharacteristics());
            }
            mDataServicesManager.updateCharacteristics(mCharacteristics, dbRequestListener);
        } catch (JsonParseException exception) {
            return false;
        }
        return true;
    }

    @Nullable
    private UserCharacteristics parseUserCharacteristics(String userCharacteristics) {
        try {
            return new Gson().fromJson(userCharacteristics, UserCharacteristics.class);
        } catch (Exception ex) {
            DSLog.e(DSLog.LOG, "JSON Parser Exception =" + ex.getMessage());
            return null;
        }
    }

    private void saveUserCharacteristicsToLocalDBRecursively(CharacteristicsDetail parentCharacteristicsDetail, List<cdp.philips.com.mydemoapp.pojo.Characteristics> characteristicsList) {
        if (characteristicsList != null && characteristicsList.size() > 0) {
            for (int i = 0; i < characteristicsList.size(); i++) {
                String type = characteristicsList.get(i).getType();
                String value = characteristicsList.get(i).getValue();
                CharacteristicsDetail childCharacteristicsDetail = mDataServicesManager.createCharacteristicsDetails(mCharacteristics, type, value, parentCharacteristicsDetail);
                parentCharacteristicsDetail.setCharacteristicsDetail(childCharacteristicsDetail);
                saveUserCharacteristicsToLocalDBRecursively(childCharacteristicsDetail, characteristicsList.get(i).getCharacteristics());
            }
        }
    }
}
