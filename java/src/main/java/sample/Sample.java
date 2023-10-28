package sample;

import static sample.Globals.*;

public class Sample {

    // private static variables
    private static final SampleStruct self = new SampleStruct();

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

            boolean aroundRegDiff = SampleZwspStructR.isAroundRegDiff(zwsp, RegDiff);
            if (aroundRegDiff) {
                nImpuls.setTotzone();
            } else {
                nImpuls.resetTotzone();
            }

            AnsprStruct.setLimitWithZwsp(anspr, zwsp);

            PraeWirkStructR praeWirk = PraeWirkStructR.createFrom(anspr, !aroundRegDiff, nImpuls);

            setStellFwd(stellFwd, praeWirk, zwsp, anspr);

            // set for next time
            if (PraeWirkStructR.equalsAnspr(praeWirk, anspr)) {
                SampleIstwStruct.setFrom(self.istw, nImpuls.isTotzone(), StellIstRev);
            }
            self.zustand.setFrom(stellFwd, nImpuls.isTotzone());
        }

        Globals.StellFwd = stellFwd.value;
        Globals.NImpuls = nImpuls.value;
        Globals.NRegFkt = nRegFkt.value;
        Globals.AnsprAufV = anspr.aufV;
        Globals.AnsprZuV = anspr.zuV;
    }

    private static void setStellFwd(StellFwd stellFwd, PraeWirkStructR praeWirk, SampleZwspStructR zwsp, AnsprStruct anspr) {
        stellFwd.reset();
        if (RegDiff < praeWirk.zu) {
            stellFwd.setZuV();
        } else {
            if (RegDiff > praeWirk.auf) {
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
    }

}
