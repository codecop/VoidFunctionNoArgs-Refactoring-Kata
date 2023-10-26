package sample;

import static sample.Constants.TOTZONE;
import static sample.Constants.TY_GRENZ_1;
import static sample.Constants.TY_GRENZ_2;

class NImpuls {

    int value;

    public NImpuls(int value) {
        this.value = value;
    }

    void reset() {
        value &= ~TOTZONE;
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

    void setTotzone() {
        value |= TOTZONE;
    }

}
