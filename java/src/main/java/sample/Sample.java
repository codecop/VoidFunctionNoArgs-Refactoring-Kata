package sample;

import static sample.Constants.*;
import static sample.Globals.*;

public class Sample {

    // private static variables
    private static final SampleStruct self = new SampleStruct();

    static class PraeWirk {
        int PraeAufWirk;
        int PraeZuWirk;
    }

    public static void theFunctionToTest() {
        AllKindOfControls allKindOfControls = new AllKindOfControls(AutoIbsOk, RegMode, BinSteuer);
        StellFwd stellFwd = new StellFwd(StellFwd);
        NImpuls nImpuls = new NImpuls(NImpuls);
        NRegFktStruct nRegFkt = NRegFktStruct.create(NRegFkt);
        ZwspSourceStruct zwspSource = new ZwspSourceStruct(AnsprAufO, AnsprZuO, AnsprBand, AnsprHyst, SollwertRev, Nerker1, WirkFall);

        if (allKindOfControls.doNotTouchIt()) {
            stellFwd.reset();

        } else {
            NRegFktStruct.setTotzoneAltIfNeeded(nRegFkt, nImpuls);

            SampleZwspStruct zwsp = SampleZwspStruct.create(zwspSource, self, RegDiff);

            if (SampleZwspStruct.isAroundRegDiff(zwsp)) {
                nImpuls.setTotzone();
            } else {
                nImpuls.resetTotzone();
            }

            if (AnsprZuV > zwsp.zuV) {
                AnsprZuV = zwsp.zuV;
            }
            if (AnsprAufV > zwsp.aufO) {
                AnsprAufV = zwsp.aufO;
            }

            // --- this block is only to set "praeWirk"
            PraeWirk praeWirk = new PraeWirk();
            if (!SampleZwspStruct.isAroundRegDiff(zwsp)) {
                praeWirk.PraeAufWirk = AnsprAufV;
                praeWirk.PraeZuWirk = AnsprZuV;
            }
            if (nImpuls.isTyGrenz1()) {
                praeWirk.PraeAufWirk = PRAE_WIRK_1;
                praeWirk.PraeZuWirk = -PRAE_WIRK_1;
            }
            if (nImpuls.isTyGrenz2()) {
                praeWirk.PraeAufWirk = PRAE_WIRK_2;
                praeWirk.PraeZuWirk = -PRAE_WIRK_2;
            }
            if (AnsprAufV > praeWirk.PraeAufWirk) {
                praeWirk.PraeAufWirk = AnsprAufV;
            }
            if (AnsprZuV < praeWirk.PraeZuWirk) {
                praeWirk.PraeZuWirk = AnsprZuV;
            }
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
                            if (RegDiffSch > AnsprAufV) {
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
            if (praeWirk.PraeAufWirk == AnsprAufV || praeWirk.PraeZuWirk == AnsprZuV) {
                SampleIstwStruct.istwSetFrom(self.istw, nImpuls.isTotzone(), StellIstRev);
            }
            self.zustand.setFrom(stellFwd, nImpuls.isTotzone());
        }

        Globals.StellFwd = stellFwd.value;
        Globals.NImpuls = nImpuls.value;
        Globals.NRegFkt = nRegFkt.value;
    }

}
