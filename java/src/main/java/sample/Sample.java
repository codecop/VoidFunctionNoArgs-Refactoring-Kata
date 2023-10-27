package sample;

import static sample.Constants.*;
import static sample.Globals.*;

public class Sample {

    // private static variables
    private static int IstwMin;
    private static int IstwMax;

    private static final SampleZustand zustand = new SampleZustand();

    static class Foo {
        final int AnsprAufO;
        final int AnsprZuO;
        final int AnsprBand;
        final int AnsprHyst;
        final int SollwertRev;
        final int Nerker1;
        final int WirkFall;

        public Foo(int AnsprAufO, int AnsprZuO, int AnsprBand, int AnsprHyst, int SollwertRev, int Nerker1, int WirkFall) {
            this.AnsprAufO = AnsprAufO;
            this.AnsprZuO = AnsprZuO;
            this.AnsprBand = AnsprBand;
            this.AnsprHyst = AnsprHyst;
            this.SollwertRev = SollwertRev;
            this.Nerker1 = Nerker1;
            this.WirkFall = WirkFall;
        }
    }

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
            Foo foo = new Foo(AnsprAufO, AnsprZuO, AnsprBand, AnsprHyst, SollwertRev, Nerker1, WirkFall);
            SampleZwspStruct zwsp = new SampleZwspStruct();
            zwsp.aufO = foo.AnsprAufO;
            zwsp.zuV = foo.AnsprZuO;
            if (zustand.isDeadzone()) {
                zwsp.aufO = foo.AnsprAufO + foo.AnsprHyst;
                zwsp.zuV = foo.AnsprZuO - foo.AnsprHyst;

                if (
                    ((RegDiff < foo.AnsprZuO) && !zustand.wasUp() &&
                    ((foo.SollwertRev - IstwMin) > foo.AnsprZuO))
                    ||
                    ((RegDiff > foo.AnsprAufO) && !zustand.wasDown() &&
                    ((foo.SollwertRev - IstwMax) > foo.AnsprAufO))
                ) {
                    zwsp.aufO = foo.AnsprAufO + foo.AnsprBand;
                    zwsp.zuV = foo.AnsprZuO - foo.AnsprBand;
                }
            }

            if ((foo.Nerker1 & STROM_GRENZ) != 0) {
                if (foo.WirkFall == 0) {
                    zwsp.aufO = zwsp.aufO + 37;
                } else {
                    zwsp.zuV = zwsp.zuV - 37;
                }
            }
            // --- this block is only to set "Zwsp*"

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
