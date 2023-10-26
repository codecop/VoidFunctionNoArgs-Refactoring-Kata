package sample;

import static sample.Constants.TOTZONE;
import static sample.Constants.TY_GRENZ_1;
import static sample.Constants.TY_GRENZ_2;

class NImpuls {
    // this are 3 values, totzone, grenz1 and grenz2

    int value;

    public NImpuls(int value) {
        this.value = value;
    }

    boolean isTotzone() {
        return (value & TOTZONE) != 0;
    }

    boolean isTyGrenz1() {
        return (value & TY_GRENZ_1) != 0;
    }

    boolean isTyGrenz2() {
        return (value & TY_GRENZ_2) != 0;
    }

    void resetTotzone() {
        value &= ~TOTZONE;
    }

    void setTotzone() {
        value |= TOTZONE;
    }

}
