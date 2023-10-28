package sample;

import static sample.Constants.*;
import static sample.Globals.*;

public class Sample {

    // private static variables
    private static final SampleStruct self = new SampleStruct();

    static class PraeWirkStruct {
        final int regDiff;
        int auf;
        int zu;

        PraeWirkStruct(int regDiff) {
            this.regDiff = regDiff;
        }
    }

    public static void theFunctionToTest() {
        AllKindOfControlsR allKindOfControls = new AllKindOfControlsR(AutoIbsOk, RegMode, BinSteuer);
        StellFwd stellFwd = new StellFwd(StellFwd);
        NImpuls nImpuls = new NImpuls(NImpuls);
        NRegFktStruct nRegFkt = NRegFktStruct.create(NRegFkt);
        ZwspSourceStructR zwspSource = new ZwspSourceStructR(AnsprAufO, AnsprZuO, AnsprBand, AnsprHyst, SollwertRev, Nerker1, WirkFall);
        AnsprStruct anspr = AnsprStruct.create(AnsprAufV, AnsprZuV);

        if (allKindOfControls.doNotTouchIt()) {
            stellFwd.reset();

        } else {
            NRegFktStruct.setTotzoneAltIfNeeded(nRegFkt, nImpuls);

            SampleZwspStructR zwsp = SampleZwspStructR.create(zwspSource, self, RegDiff);

            boolean aroundRegDiff = SampleZwspStructR.isAroundRegDiff(zwsp);
            if (aroundRegDiff) {
                nImpuls.setTotzone();
            } else {
                nImpuls.resetTotzone();
            }

            AnsprStruct.limitWithZwsp(anspr, zwsp);

            PraeWirkStruct praeWirk = createFrom(anspr, !aroundRegDiff, nImpuls, RegDiff);

            // --- this block is only to set the stellFwd "result"
            stellFwd.reset();
            if (praeWirk.regDiff < praeWirk.zu) {
                stellFwd.setZuV();
            } else {
                if (praeWirk.regDiff > praeWirk.auf) {
                    stellFwd.setAufV();
                } else {
                    if (RegDiffSch > zwsp.aufO) {
                        if (RegDiff > zwsp.aufO) {
                            if (RegDiffSch > anspr.aufV) {
                                stellFwd.setAufV();
                            } else {
                                stellFwd.setAufO();
                                // this is not covered!
                            }
                        }
                    }
                }
            }
            // --- this block is only to set the stellFwd "result"

            // set for next time
            if (equalsAnspr(praeWirk, anspr)) {
                SampleIstwStruct.istwSetFrom(self.istw, nImpuls.isTotzone(), StellIstRev);
            }
            self.zustand.setFrom(stellFwd, nImpuls.isTotzone());
        }

        Globals.StellFwd = stellFwd.value;
        Globals.NImpuls = nImpuls.value;
        Globals.NRegFkt = nRegFkt.value;
        Globals.AnsprAufV = anspr.aufV;
        Globals.AnsprZuV = anspr.zuV;
    }

    static PraeWirkStruct createFrom(AnsprStruct anspr, boolean useAnsprAsBase, NImpuls nImpuls, int regDiff) {
        PraeWirkStruct praeWirk = new PraeWirkStruct(regDiff);
        if (useAnsprAsBase) {
            praeWirk.auf = anspr.aufV;
            praeWirk.zu = anspr.zuV;
        }
        if (nImpuls.isTyGrenz1()) {
            praeWirk.auf = PRAE_WIRK_1;
            praeWirk.zu = -PRAE_WIRK_1;
        }
        if (nImpuls.isTyGrenz2()) {
            praeWirk.auf = PRAE_WIRK_2;
            praeWirk.zu = -PRAE_WIRK_2;
        }
        if (anspr.aufV > praeWirk.auf) {
            praeWirk.auf = anspr.aufV;
        }
        if (anspr.zuV < praeWirk.zu) {
            praeWirk.zu = anspr.zuV;
        }
        return praeWirk;
    }

    static boolean equalsAnspr(PraeWirkStruct praeWirk, AnsprStruct anspr) {
        return praeWirk.auf == anspr.aufV || praeWirk.zu == anspr.zuV;
    }

}
