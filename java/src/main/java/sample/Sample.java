package sample;

import static sample.Constants.*;
import static sample.Globals.*;

public class Sample {

    // private static variables
    private static int IstwMin;
    private static int IstwMax;

    private static final SampleZustand zustand = new SampleZustand();

    public static void theFunctionToTest() {
        AllKindOfControls allKindOfControls = new AllKindOfControls(AutoIbsOk, RegMode, BinSteuer);
        StellFwd stellFwd = new StellFwd(Globals.StellFwd);
        NImpuls nImpuls = new NImpuls(Globals.NImpuls);

        if (allKindOfControls.doNotTouchIt()) {
            stellFwd.reset();
        } else {
            if (!nImpuls.isTotzone()) {
                NRegFkt &= ~(TOTZONE_ALT);
            } else {
                NRegFkt |= TOTZONE_ALT;
            }

            // --- this block is only to set "Zwsp*"
            int ZwspAufO;
            int ZwspZuV;
            ZwspAufO = AnsprAufO;
            ZwspZuV = AnsprZuO;
            if (zustand.isDeadzone()) {
                ZwspAufO = AnsprAufO + AnsprHyst;
                ZwspZuV = AnsprZuO - AnsprHyst;

                if (
                        ((RegDiff < AnsprZuO) && !zustand.wasUp() &&
                                ((SollwertRev - IstwMin) > AnsprZuO))
                                ||
                                ((RegDiff > AnsprAufO) && !zustand.wasDown() &&
                                        ((SollwertRev - IstwMax) > AnsprAufO))
                ) {
                    ZwspAufO = AnsprAufO + AnsprBand;
                    ZwspZuV = AnsprZuO - AnsprBand;
                }
            }

            if ((Nerker1 & STROM_GRENZ) != 0) {
                if (WirkFall == 0) {
                    ZwspAufO = ZwspAufO + 37;
                } else {
                    ZwspZuV = ZwspZuV - 37;
                }
            }
            // --- this block is only to set "Zwsp*"

            if (AnsprZuV > ZwspZuV) {
                AnsprZuV = ZwspZuV;
            }
            if (AnsprAufV > ZwspAufO) {
                AnsprAufV = ZwspAufO;
            }

            stellFwd.reset();

            if ((RegDiff >= ZwspZuV) && (RegDiff <= ZwspAufO)) {
                nImpuls.setTotzone();
            } else {
                nImpuls.resetTotzone();
            }

            // --- this block is only to set "praeWirk"
            int PraeAufWirk = 0;
            int PraeZuWirk = 0;

            if ((RegDiff >= ZwspZuV) && (RegDiff <= ZwspAufO)) {
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
                    if (RegDiffSch > ZwspAufO) {
                        if (RegDiff > ZwspAufO) {
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
                // --- this block is only to set the "Ist*" for next time
                if (nImpuls.isTotzone()) {
                    if (StellIstRev < IstwMin) {
                        IstwMin = StellIstRev;
                    }
                    if (StellIstRev > IstwMax) {
                        IstwMax = StellIstRev;
                    }
                } else {
                    IstwMax = PERCENT_MIN;
                    IstwMin = PERCENT_MAX;
                }
                // --- this block is only to set the "Ist*"
            }

            zustand.setFrom(stellFwd, nImpuls);
        }

        Globals.StellFwd = stellFwd.value;
        Globals.NImpuls = nImpuls.value;
    }

}
