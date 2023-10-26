package sample;

import static sample.Constants.AUF_O;
import static sample.Constants.AUF_V;
import static sample.Constants.ZU_O;
import static sample.Constants.ZU_V;

class StellFwd {

    int value;

    public StellFwd(int value) {
        this.value = value;
    }

    void reset() {
        value &= ~(ZU_O | ZU_V | AUF_O | AUF_V);
    }

    boolean isAuf() {
        return (value & (AUF_O | AUF_V)) != 0;
    }

    boolean isZu() {
        return (value & (ZU_O | ZU_V)) != 0;
    }

    void setAufO() {
        value |= AUF_O;
    }

    void setAufV() {
        value |= AUF_V;
    }

    void setZuV() {
        value |= ZU_V;
    }

}
