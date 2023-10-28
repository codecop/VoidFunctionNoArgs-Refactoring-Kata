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

    static class AnsprStruct {
        int AnsprZuV;
        int AnsprAufV;
    }

    public static void theFunctionToTest() {
        AllKindOfControls allKindOfControls = new AllKindOfControls(AutoIbsOk, RegMode, BinSteuer);
        StellFwd stellFwd = new StellFwd(StellFwd);
        NImpuls nImpuls = new NImpuls(NImpuls);
        NRegFktStruct nRegFkt = NRegFktStruct.create(NRegFkt);
        ZwspSourceStruct zwspSource = new ZwspSourceStruct(AnsprAufO, AnsprZuO, AnsprBand, AnsprHyst, SollwertRev, Nerker1, WirkFall);
        AnsprStruct foo = new AnsprStruct();
        foo.AnsprAufV = AnsprAufV;
        foo.AnsprZuV = AnsprZuV;

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

            if (foo.AnsprZuV > zwsp.zuV) {
                foo.AnsprZuV = zwsp.zuV;
            }
            if (foo.AnsprAufV > zwsp.aufO) {
                foo.AnsprAufV = zwsp.aufO;
            }

            // --- this block is only to set "praeWirk"
            PraeWirkStruct praeWirk = new PraeWirkStruct();
            if (!SampleZwspStruct.isAroundRegDiff(zwsp)) {
                praeWirk.PraeAufWirk = foo.AnsprAufV;
                praeWirk.PraeZuWirk = foo.AnsprZuV;
            }
            if (nImpuls.isTyGrenz1()) {
                praeWirk.PraeAufWirk = PRAE_WIRK_1;
                praeWirk.PraeZuWirk = -PRAE_WIRK_1;
            }
            if (nImpuls.isTyGrenz2()) {
                praeWirk.PraeAufWirk = PRAE_WIRK_2;
                praeWirk.PraeZuWirk = -PRAE_WIRK_2;
            }
            if (foo.AnsprAufV > praeWirk.PraeAufWirk) {
                praeWirk.PraeAufWirk = foo.AnsprAufV;
            }
            if (foo.AnsprZuV < praeWirk.PraeZuWirk) {
                praeWirk.PraeZuWirk = foo.AnsprZuV;
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
                            if (RegDiffSch > foo.AnsprAufV) {
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
            if (praeWirk.PraeAufWirk == foo.AnsprAufV || praeWirk.PraeZuWirk == foo.AnsprZuV) {
                SampleIstwStruct.istwSetFrom(self.istw, nImpuls.isTotzone(), StellIstRev);
            }
            self.zustand.setFrom(stellFwd, nImpuls.isTotzone());
        }

        Globals.StellFwd = stellFwd.value;
        Globals.NImpuls = nImpuls.value;
        Globals.NRegFkt = nRegFkt.value;
        Globals.AnsprAufV = foo.AnsprAufV;
        Globals.AnsprZuV = foo.AnsprZuV;
    }

}
