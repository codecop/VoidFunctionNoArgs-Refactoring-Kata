package sample;

import static sample.Constants.*;
import static sample.Globals.*;

public class Sample {

    // private static variables
    private static final SampleIstwStruct istw = new SampleIstwStruct();
    private static final SampleZustand zustand = new SampleZustand();

    public static void theFunctionToTest() {
        AllKindOfControls allKindOfControls = new AllKindOfControls(AutoIbsOk, RegMode, BinSteuer);
        StellFwd stellFwd = new StellFwd(Globals.StellFwd);
        NImpuls nImpuls = new NImpuls(Globals.NImpuls);
        ZwspSourceStruct zwspSource = new ZwspSourceStruct(AnsprAufO, AnsprZuO, AnsprBand, AnsprHyst, SollwertRev, Nerker1, WirkFall);

        if (allKindOfControls.doNotTouchIt()) {
            stellFwd.reset();
        } else {
            if (!nImpuls.isTotzone()) {
                NRegFkt &= ~(TOTZONE_ALT);
            } else {
                NRegFkt |= TOTZONE_ALT;
            }

            SampleZwspStruct zwsp = ZwspSourceStruct.create(zwspSource, zustand, RegDiff, istw);

            if (AnsprZuV > zwsp.zuV) {
                AnsprZuV = zwsp.zuV;
            }
            if (AnsprAufV > zwsp.aufO) {
                AnsprAufV = zwsp.aufO;
            }

            stellFwd.reset();

            if ((RegDiff >= zwsp.zuV) && (RegDiff <= zwsp.aufO)) {
                nImpuls.setTotzone();
            } else {
                nImpuls.resetTotzone();
            }

            // --- this block is only to set "praeWirk"
            int PraeAufWirk = 0;
            int PraeZuWirk = 0;

            if ((RegDiff >= zwsp.zuV) && (RegDiff <= zwsp.aufO)) {
                // empty for now
            } else {
                PraeAufWirk = AnsprAufV;
                PraeZuWirk = AnsprZuV;
            }
            if (nImpuls.isTyGrenz1()) {
                PraeAufWirk = PRAE_WIRK_1;
                PraeZuWirk = -PRAE_WIRK_1;
            }
            if (nImpuls.isTyGrenz2()) {
                PraeAufWirk = PRAE_WIRK_2;
                PraeZuWirk = -PRAE_WIRK_2;
            }
            if (AnsprAufV > PraeAufWirk) {
                PraeAufWirk = AnsprAufV;
            }
            if (AnsprZuV < PraeZuWirk) {
                PraeZuWirk = AnsprZuV;
            }
            // --- this block is only to set "praeWirk"

            // --- this block is only to set the stellFwd "result"
            if (RegDiff < PraeZuWirk) {
                stellFwd.setZuV();
            } else {
                if (RegDiff > PraeAufWirk) {
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

            if (PraeAufWirk == AnsprAufV || PraeZuWirk == AnsprZuV) {
                SampleIstwStruct.istwSetFrom(istw, nImpuls.isTotzone(), StellIstRev);
            }

            zustand.setFrom(stellFwd, nImpuls);
        }

        Globals.StellFwd = stellFwd.value;
        Globals.NImpuls = nImpuls.value;
    }

}
