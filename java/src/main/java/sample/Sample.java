package sample;

import static sample.Constants.*;
import static sample.Globals.*;

public class Sample {

    // private static variables
    private static final SampleStruct self = new SampleStruct();

    static class PraeWirkStruct {
        int PraeAufWirk;
        int PraeZuWirk;
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

            if (SampleZwspStructR.isAroundRegDiff(zwsp)) {
                nImpuls.setTotzone();
            } else {
                nImpuls.resetTotzone();
            }

            AnsprStruct.limitWithZwsp(anspr, zwsp);

            // --- this block is only to set "praeWirk"
            PraeWirkStruct praeWirk = create(nImpuls, anspr, !SampleZwspStructR.isAroundRegDiff(zwsp));
            // --- this block is only to set "praeWirk"

            // --- this block is only to set the stellFwd "result"
            stellFwd.reset();
            if (RegDiff < praeWirk.PraeZuWirk) {
                stellFwd.setZuV();
            } else {
                if (RegDiff > praeWirk.PraeAufWirk) {
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
            if (praeWirk.PraeAufWirk == anspr.aufV || praeWirk.PraeZuWirk == anspr.zuV) {
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

    static PraeWirkStruct create(NImpuls nImpuls, AnsprStruct anspr, boolean baseIt) {
        PraeWirkStruct praeWirk = new PraeWirkStruct();
        if (baseIt) {
            praeWirk.PraeAufWirk = anspr.aufV;
            praeWirk.PraeZuWirk = anspr.zuV;
        }
        if (nImpuls.isTyGrenz1()) {
            praeWirk.PraeAufWirk = PRAE_WIRK_1;
            praeWirk.PraeZuWirk = -PRAE_WIRK_1;
        }
        if (nImpuls.isTyGrenz2()) {
            praeWirk.PraeAufWirk = PRAE_WIRK_2;
            praeWirk.PraeZuWirk = -PRAE_WIRK_2;
        }
        if (anspr.aufV > praeWirk.PraeAufWirk) {
            praeWirk.PraeAufWirk = anspr.aufV;
        }
        if (anspr.zuV < praeWirk.PraeZuWirk) {
            praeWirk.PraeZuWirk = anspr.zuV;
        }
        return praeWirk;
    }

}
