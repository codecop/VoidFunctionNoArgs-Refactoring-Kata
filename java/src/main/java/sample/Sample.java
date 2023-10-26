package sample;

import static sample.Constants.*;
import static sample.Globals.*;

public class Sample {

    // private static variables
    private static int IstwMin;
    private static int IstwMax;
    private static int[] Zustand = new int[2];

    public static void theFunctionToTest() {
        int ZwspAufO;
        int ZwspZuV;
        int zw;

        if ((AutoIbsOk == C_IBS_OK) && 
            ((RegMode == N_AUTOMATIK) || (RegMode == N_VALVE_DIAG)) && 
            ((BinSteuer & BO_REGLER) != 0)) {
            foo_reset();
        } else {
            if ((NImpuls & TOTZONE) == 0) {
                NRegFkt &= ~(TOTZONE_ALT);
            } else {
                NRegFkt |= TOTZONE_ALT;
            }
            ZwspAufO = AnsprAufO;
            ZwspZuV = AnsprZuO;
            if (Zustand[0] == STATE_WITHIN_DEADZONE) {
                ZwspAufO = AnsprAufO + AnsprHyst;
                ZwspZuV = AnsprZuO - AnsprHyst;

                if (
                     ((RegDiff < AnsprZuO) && (Zustand[1] != STATE_MOVE_UP) && 
                     ((SollwertRev - IstwMin) > AnsprZuO))
                     || 
                     ((RegDiff > AnsprAufO) && (Zustand[1] != STATE_MOVE_DOWN) && 
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

            if (AnsprZuV > ZwspZuV) {
                AnsprZuV = ZwspZuV;
            }
            if (AnsprAufV > ZwspAufO) {
                AnsprAufV = ZwspAufO;
            }

            foo_reset();
            int PraeAufWirk = 0;
            int PraeZuWirk = 0;

            if ((RegDiff >= ZwspZuV) && (RegDiff <= ZwspAufO)) {
                NImpuls |= TOTZONE;
            } else {
                NImpuls &= ~TOTZONE;
                PraeAufWirk = AnsprAufV;
                PraeZuWirk = AnsprZuV;
            }
            if ((NImpuls & TY_GRENZ_1) != 0) {
                PraeAufWirk = PRAE_WIRK_1;
                PraeZuWirk = -PRAE_WIRK_1;
            }
            if ((NImpuls & TY_GRENZ_2) != 0) {
                PraeAufWirk = PRAE_WIRK_2;
                PraeZuWirk = -PRAE_WIRK_2;
            }

            if (AnsprAufV > PraeAufWirk) {
                PraeAufWirk = AnsprAufV;
            }
            if (AnsprZuV < PraeZuWirk) {
                PraeZuWirk = AnsprZuV;
            }

            if (RegDiff < PraeZuWirk) {
                setZuV();
            } else {
                if (RegDiff > PraeAufWirk) {
                    setAufV();
                } else {
                    if (RegDiffSch > ZwspAufO) {
                        if (RegDiff > ZwspAufO) {
                            if (RegDiffSch > AnsprAufV) {
                                setAufV();
                            } else {
                                setAufO();
                                // this is not covered!
                            }
                        }
                    }
                }

            }

            if (PraeAufWirk == AnsprAufV || PraeZuWirk == AnsprZuV) {
                if ((NImpuls & TOTZONE) != 0) {
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

            zw = Zustand[0];
            if ((NImpuls & TOTZONE) != 0) {
                zw = STATE_WITHIN_DEADZONE;
            }
            if (foo_isZu()) {
                zw = STATE_MOVE_DOWN;
            }
            if (foo_isAuf()) {
                zw = STATE_MOVE_UP;
            }

            if (zw != Zustand[0]) {
                Zustand[1] = Zustand[0];
                Zustand[0] = zw;
            }
        }
    }

    static class StellFwdW {

    }

    private static void setAufO() {
        StellFwd |= AUF_O;
    }

    private static void setAufV() {
        StellFwd |= AUF_V;
    }

    private static void setZuV() {
        StellFwd |= ZU_V;
    }

    private static boolean foo_isAuf() {
        return (StellFwd & (AUF_O | AUF_V)) != 0;
    }

    private static boolean foo_isZu() {
        return (StellFwd & (ZU_O | ZU_V)) != 0;
    }

    private static void foo_reset() {
        StellFwd &= ~(ZU_O | ZU_V | AUF_O | AUF_V);
    }
}
