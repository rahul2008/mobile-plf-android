package com.philips.pins.shinelib;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SHNUserConfigurationCalculationsTest {

    private SHNUserConfigurationCalculations calculations;

    @Before
    public void setUp() {
        calculations = new SHNUserConfigurationCalculations();
    }

    @Test
    public void calculationForYoungMen() {
        Integer base = calculations.getBaseMetabolicRate(0d, 0, 20, SHNUserConfiguration.Sex.Male);
        assertThat(base).isEqualTo(SHNUserConfigurationCalculations.YOUNG_MEN_BASE);

        Integer baseWithWeight1 = calculations.getBaseMetabolicRate(1d, 0, 20, SHNUserConfiguration.Sex.Male);
        assertThat(baseWithWeight1).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.YOUNG_MEN_BASE + SHNUserConfigurationCalculations.YOUNG_MEN_WEIGHT_KG));

        Integer baseWithHeight1 = calculations.getBaseMetabolicRate(0d, 1, 20, SHNUserConfiguration.Sex.Male);
        assertThat(baseWithHeight1).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.YOUNG_MEN_BASE + SHNUserConfigurationCalculations.YOUNG_MEN_HEIGHT_M / 100d));

        Integer complex = calculations.getBaseMetabolicRate(2d, 2, 20, SHNUserConfiguration.Sex.Male);
        assertThat(complex).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.YOUNG_MEN_BASE + 2 * SHNUserConfigurationCalculations.YOUNG_MEN_HEIGHT_M / 100d + 2 * SHNUserConfigurationCalculations.YOUNG_MEN_WEIGHT_KG));
    }

    @Test
    public void calculationForYoungWoman() {
        Integer base = calculations.getBaseMetabolicRate(0d, 0, 20, SHNUserConfiguration.Sex.Female);
        assertThat(base).isEqualTo(SHNUserConfigurationCalculations.YOUNG_WOMEN_BASE);

        Integer baseWithWeight1 = calculations.getBaseMetabolicRate(1d, 0, 20, SHNUserConfiguration.Sex.Female);
        assertThat(baseWithWeight1).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.YOUNG_WOMEN_BASE + SHNUserConfigurationCalculations.YOUNG_WOMEN_WEIGHT_KG));

        Integer baseWithHeight1 = calculations.getBaseMetabolicRate(0d, 1, 20, SHNUserConfiguration.Sex.Female);
        assertThat(baseWithHeight1).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.YOUNG_WOMEN_BASE + SHNUserConfigurationCalculations.YOUNG_WOMEN_HEIGHT_M / 100d));

        Integer complex = calculations.getBaseMetabolicRate(2d, 2, 20, SHNUserConfiguration.Sex.Female);
        assertThat(complex).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.YOUNG_WOMEN_BASE + 2 * SHNUserConfigurationCalculations.YOUNG_WOMEN_HEIGHT_M / 100d + 2 * SHNUserConfigurationCalculations.YOUNG_WOMEN_WEIGHT_KG));
    }

    @Test
    public void calculationForMidMen() {
        Integer base = calculations.getBaseMetabolicRate(0d, 0, 40, SHNUserConfiguration.Sex.Male);
        assertThat(base).isEqualTo(SHNUserConfigurationCalculations.MID_MEN_BASE);

        Integer baseWithWeight1 = calculations.getBaseMetabolicRate(1d, 0, 40, SHNUserConfiguration.Sex.Male);
        assertThat(baseWithWeight1).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.MID_MEN_BASE + SHNUserConfigurationCalculations.MID_MEN_WEIGHT_KG));

        Integer baseWithHeight1 = calculations.getBaseMetabolicRate(0d, 1, 40, SHNUserConfiguration.Sex.Male);
        assertThat(baseWithHeight1).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.MID_MEN_BASE + SHNUserConfigurationCalculations.MID_MEN_HEIGHT_M / 100d));

        Integer complex = calculations.getBaseMetabolicRate(2d, 2, 40, SHNUserConfiguration.Sex.Male);
        assertThat(complex).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.MID_MEN_BASE + 2 * SHNUserConfigurationCalculations.MID_MEN_HEIGHT_M / 100d + 2 * SHNUserConfigurationCalculations.MID_MEN_WEIGHT_KG));
    }

    @Test
    public void calculationForMidWoman() {
        Integer base = calculations.getBaseMetabolicRate(0d, 0, 40, SHNUserConfiguration.Sex.Female);
        assertThat(base).isEqualTo(SHNUserConfigurationCalculations.MID_WOMEN_BASE);

        Integer baseWithWeight1 = calculations.getBaseMetabolicRate(1d, 0, 40, SHNUserConfiguration.Sex.Female);
        assertThat(baseWithWeight1).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.MID_WOMEN_BASE + SHNUserConfigurationCalculations.MID_WOMEN_WEIGHT_KG));

        Integer baseWithHeight1 = calculations.getBaseMetabolicRate(0d, 1, 40, SHNUserConfiguration.Sex.Female);
        assertThat(baseWithHeight1).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.MID_WOMEN_BASE + SHNUserConfigurationCalculations.MID_WOMEN_HEIGHT_M / 100d));

        Integer complex = calculations.getBaseMetabolicRate(2d, 2, 40, SHNUserConfiguration.Sex.Female);
        assertThat(complex).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.MID_WOMEN_BASE + 2 * SHNUserConfigurationCalculations.MID_WOMEN_HEIGHT_M / 100d + 2 * SHNUserConfigurationCalculations.MID_WOMEN_WEIGHT_KG));
    }

    @Test
    public void calculationForOldMen() {
        Integer base = calculations.getBaseMetabolicRate(0d, 0, 70, SHNUserConfiguration.Sex.Male);
        assertThat(base).isEqualTo(SHNUserConfigurationCalculations.OLD_MEN_BASE);

        Integer baseWithWeight1 = calculations.getBaseMetabolicRate(1d, 0, 70, SHNUserConfiguration.Sex.Male);
        assertThat(baseWithWeight1).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.OLD_MEN_BASE + SHNUserConfigurationCalculations.OLD_MEN_WEIGHT_KG));

        Integer baseWithHeight1 = calculations.getBaseMetabolicRate(0d, 1, 70, SHNUserConfiguration.Sex.Male);
        assertThat(baseWithHeight1).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.OLD_MEN_BASE + SHNUserConfigurationCalculations.OLD_MEN_HEIGHT_M / 100d));

        Integer complex = calculations.getBaseMetabolicRate(2d, 2, 70, SHNUserConfiguration.Sex.Male);
        assertThat(complex).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.OLD_MEN_BASE + 2 * SHNUserConfigurationCalculations.OLD_MEN_HEIGHT_M / 100d + 2 * SHNUserConfigurationCalculations.OLD_MEN_WEIGHT_KG));
    }

    @Test
    public void calculationForOldWoman() {
        Integer base = calculations.getBaseMetabolicRate(0d, 0, 70, SHNUserConfiguration.Sex.Female);
        assertThat(base).isEqualTo(SHNUserConfigurationCalculations.OLD_WOMEN_BASE);

        Integer baseWithWeight1 = calculations.getBaseMetabolicRate(1d, 0, 70, SHNUserConfiguration.Sex.Female);
        assertThat(baseWithWeight1).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.OLD_WOMEN_BASE + SHNUserConfigurationCalculations.OLD_WOMEN_WEIGHT_KG));

        Integer baseWithHeight1 = calculations.getBaseMetabolicRate(0d, 1, 70, SHNUserConfiguration.Sex.Female);
        assertThat(baseWithHeight1).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.OLD_WOMEN_BASE + SHNUserConfigurationCalculations.OLD_WOMEN_HEIGHT_M / 100d));

        Integer complex = calculations.getBaseMetabolicRate(2d, 2, 70, SHNUserConfiguration.Sex.Female);
        assertThat(complex).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.OLD_WOMEN_BASE + 2 * SHNUserConfigurationCalculations.OLD_WOMEN_HEIGHT_M / 100d + 2 * SHNUserConfigurationCalculations.OLD_WOMEN_WEIGHT_KG));
    }

    @Test
    public void calculationForChildren() {
        Integer base = calculations.getBaseMetabolicRate(0d, 0, 10, SHNUserConfiguration.Sex.Male);
        assertThat(base).isEqualTo(SHNUserConfigurationCalculations.CHILD_BASE);

        Integer baseWithWeight1 = calculations.getBaseMetabolicRate(1d, 0, 10, SHNUserConfiguration.Sex.Male);
        assertThat(baseWithWeight1).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.CHILD_BASE + SHNUserConfigurationCalculations.CHILD_WEIGHT_KG));

        Integer baseWithHeight1 = calculations.getBaseMetabolicRate(0d, 1, 10, SHNUserConfiguration.Sex.Male);
        assertThat(baseWithHeight1).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.CHILD_BASE + SHNUserConfigurationCalculations.CHILD_HEIGHT_M / 100d));

        Integer complex = calculations.getBaseMetabolicRate(2d, 2, 10, SHNUserConfiguration.Sex.Male);
        assertThat(complex).isEqualTo((int) Math.round(SHNUserConfigurationCalculations.CHILD_BASE + 2 * SHNUserConfigurationCalculations.CHILD_HEIGHT_M / 100d + 2 * SHNUserConfigurationCalculations.CHILD_WEIGHT_KG));
    }
}
