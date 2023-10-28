package sample;

import static sample.Constants.TOTZONE_ALT;

class NRegFktStruct {
    int value;

    static NRegFktStruct create(int nRegFkt) {
        NRegFktStruct self = new NRegFktStruct();
        self.value = nRegFkt;
        return self;
    }

    static void setTotzoneAltIfNeeded(NRegFktStruct self, NImpuls nImpuls) {
        if (!nImpuls.isTotzone()) {
            self.value &= ~(TOTZONE_ALT);
        } else {
            self.value |= TOTZONE_ALT;
        }
    }
}
