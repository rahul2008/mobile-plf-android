package cdp.philips.com.mydemoapp.characteristics;

import com.google.gson.Gson;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.CharacteristicsDetail;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.List;

import cdp.philips.com.mydemoapp.pojo.UserCharacteristics;

class CharacteristicsDialogPresenter {

    private DataServicesManager mDataServicesManager;
    private Characteristics mCharacteristics;

    CharacteristicsDialogPresenter() {
        mDataServicesManager = DataServicesManager.getInstance();
        mCharacteristics = mDataServicesManager.createCharacteristics();
    }

    void createOrUpdateCharacteristics(String userCharacteristics) {
        UserCharacteristics mUserCharacteristics = parseUserCharacteristics(userCharacteristics);
        for (int i = 0; i < mUserCharacteristics.getCharacteristics().size(); i++) {
            String type = mUserCharacteristics.getCharacteristics().get(i).getType();
            String value = mUserCharacteristics.getCharacteristics().get(i).getValue();
            CharacteristicsDetail characteristicsDetail = mDataServicesManager.createCharacteristicsDetails(mCharacteristics, type, value, 0, null);
            saveUserCharacteristicsToLocalDBRecursively(characteristicsDetail, mUserCharacteristics.getCharacteristics().get(i).getCharacteristics());
        }
        mDataServicesManager.updateCharacteristics(mCharacteristics);
    }

    private UserCharacteristics parseUserCharacteristics(String userCharacteristics) {
        return new Gson().fromJson(userCharacteristics, UserCharacteristics.class);
    }

    private void saveUserCharacteristicsToLocalDBRecursively(CharacteristicsDetail parentCharacteristicsDetail, List<cdp.philips.com.mydemoapp.pojo.Characteristics> characteristicsList) {
        if (characteristicsList.size() > 0) {
            for (int i = 0; i < characteristicsList.size(); i++) {
                String type = characteristicsList.get(i).getType();
                String value = characteristicsList.get(i).getValue();
                CharacteristicsDetail childCharacteristicsDetail = mDataServicesManager.createCharacteristicsDetails(mCharacteristics, type, value, 0, parentCharacteristicsDetail);
                parentCharacteristicsDetail.setCharacteristicsDetail(childCharacteristicsDetail);
                saveUserCharacteristicsToLocalDBRecursively(childCharacteristicsDetail, characteristicsList.get(i).getCharacteristics());
            }
        }
    }
}
