package sample;

import static sample.Constants.STROM_GRENZ;

class ZwspSourceStruct {
    public final int ansprAufO;
    public final int ansprZuO;
    public final int ansprBand;
    public final int ansprHyst;
    public final int sollwertRev;
    public final int nerker1;
    public final int wirkFall;

    public ZwspSourceStruct(int ansprAufO, int ansprZuO, int ansprBand, int ansprHyst, int sollwertRev, int nerker1, int wirkFall) {
        this.ansprAufO = ansprAufO;
        this.ansprZuO = ansprZuO;
        this.ansprBand = ansprBand;
        this.ansprHyst = ansprHyst;
        this.sollwertRev = sollwertRev;
        this.nerker1 = nerker1;
        this.wirkFall = wirkFall;
    }

    public static SampleZwspStruct create(ZwspSourceStruct source, SampleZustand zustand, int regDiff, SampleIstwStruct istw) {
        SampleZwspStruct zwsp = new SampleZwspStruct();
        zwsp.aufO = source.ansprAufO;
        zwsp.zuV = source.ansprZuO;
        if (zustand.isDeadzone()) {
            zwsp.aufO = source.ansprAufO + source.ansprHyst;
            zwsp.zuV = source.ansprZuO - source.ansprHyst;

            if (
                ((regDiff < source.ansprZuO) && !zustand.wasUp() &&
                ((source.sollwertRev - istw.min) > source.ansprZuO))
                ||
                ((regDiff > source.ansprAufO) && !zustand.wasDown() &&
                ((source.sollwertRev - istw.max) > source.ansprAufO))
            ) {
                zwsp.aufO = source.ansprAufO + source.ansprBand;
                zwsp.zuV = source.ansprZuO - source.ansprBand;
            }
        }

        if ((source.nerker1 & STROM_GRENZ) != 0) {
            if (source.wirkFall == 0) {
                zwsp.aufO = zwsp.aufO + 37;
            } else {
                zwsp.zuV = zwsp.zuV - 37;
            }
        }
        return zwsp;
    }
}
