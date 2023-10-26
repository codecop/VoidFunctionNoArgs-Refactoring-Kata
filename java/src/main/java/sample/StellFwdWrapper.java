package sample;

import static sample.Constants.AUF_O;
import static sample.Constants.AUF_V;
import static sample.Constants.ZU_O;
import static sample.Constants.ZU_V;
import static sample.Globals.StellFwd;

class StellFwdWrapper {

    void reset() {
        StellFwd &= ~(ZU_O | ZU_V | AUF_O | AUF_V);
    }

    boolean isAuf() {
        return (StellFwd & (AUF_O | AUF_V)) != 0;
    }

    boolean isZu() {
        return (StellFwd & (ZU_O | ZU_V)) != 0;
    }

    void setAufO() {
        StellFwd |= AUF_O;
    }

    void setAufV() {
        StellFwd |= AUF_V;
    }

    void setZuV() {
        StellFwd |= ZU_V;
    }

}
