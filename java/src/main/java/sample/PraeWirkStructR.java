package sample;

import static sample.Constants.PRAE_WIRK_1;
import static sample.Constants.PRAE_WIRK_2;

class PraeWirkStructR {
    final int auf;
    final int zu;

    PraeWirkStructR(int auf, int zu) {
        this.auf = auf;
        this.zu = zu;
    }

    static PraeWirkStructR createFrom(AnsprStruct anspr, boolean useAnsprAsBase, NImpuls nImpuls) {
        int auf = 0;
        int zu = 0;
        if (useAnsprAsBase) {
            auf = anspr.aufV;
            zu = anspr.zuV;
        }
        if (nImpuls.isTyGrenz1()) {
            auf = PRAE_WIRK_1;
            zu = -PRAE_WIRK_1;
        }
        if (nImpuls.isTyGrenz2()) {
            auf = PRAE_WIRK_2;
            zu = -PRAE_WIRK_2;
        }
        if (anspr.aufV > auf) {
            auf = anspr.aufV;
        }
        if (anspr.zuV < zu) {
            zu = anspr.zuV;
        }
        return new PraeWirkStructR(auf, zu);
    }

    static boolean equalsAnspr(PraeWirkStructR praeWirk, AnsprStruct anspr) {
        return praeWirk.auf == anspr.aufV || praeWirk.zu == anspr.zuV;
    }
}
