package sample;

import static sample.Constants.*;
import static sample.Globals.*;

public class Sample {

    // private static variables
    private static int IstwMin;
    private static int IstwMax;
    private static final int[] Zustand = new int[2];

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
            if (zustandInDeadzone()) {
                ZwspAufO = AnsprAufO + AnsprHyst;
                ZwspZuV = AnsprZuO - AnsprHyst;

                if (
                        ((RegDiff < AnsprZuO) && preZustandNotUp() &&
                                ((SollwertRev - IstwMin) > AnsprZuO))
                                ||
                                ((RegDiff > AnsprAufO) && prevZustandNotDown() &&
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

            if (PraeAufWirk == AnsprAufV || PraeZuWirk == AnsprZuV) {
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

            }

            // --- this block is only to set the local Zustand[0] and [1]
            roll(stellFwd, nImpuls);
            // --- this block is only to set the local Zustand[0] and [1]
        }

        Globals.StellFwd = stellFwd.value;
        Globals.NImpuls = nImpuls.value;
    }

    private static boolean prevZustandNotDown() {
        return Zustand[1] != STATE_MOVE_DOWN;
    }

    private static boolean zustandInDeadzone() {
        return Zustand[0] == STATE_WITHIN_DEADZONE;
    }

    private static boolean preZustandNotUp() {
        return Zustand[1] != STATE_MOVE_UP;
    }

    private static void roll(sample.StellFwd stellFwd, sample.NImpuls nImpuls) {
        int zw;
        zw = Zustand[0];
        if (nImpuls.isTotzone()) {
            zw = STATE_WITHIN_DEADZONE;
        }
        if (stellFwd.isZu()) {
            zw = STATE_MOVE_DOWN;
        }
        if (stellFwd.isAuf()) {
            zw = STATE_MOVE_UP;
        }

        if (zw != Zustand[0]) {
            Zustand[1] = Zustand[0];
            Zustand[0] = zw;
        }
    }

}
