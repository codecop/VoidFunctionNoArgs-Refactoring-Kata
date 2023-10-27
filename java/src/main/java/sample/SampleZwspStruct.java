package sample;

import static sample.Constants.STROM_GRENZ;

/**
 * A type aka a DTO like a Struct.
 */
class SampleZwspStruct {

    int aufO;
    int zuV;

    // pure functions
    static SampleZwspStruct create(ZwspSourceStruct zwspSource, SampleStruct sample, int regDiff) {
        SampleZwspStruct zwsp = new SampleZwspStruct();
        zwsp.aufO = zwspSource.ansprAufO;
        zwsp.zuV = zwspSource.ansprZuO;
        if (sample.zustand.isDeadzone()) {
            zwsp.aufO = zwspSource.ansprAufO + zwspSource.ansprHyst;
            zwsp.zuV = zwspSource.ansprZuO - zwspSource.ansprHyst;

            if (
                ((regDiff < zwspSource.ansprZuO) && !sample.zustand.wasUp() &&
                ((zwspSource.sollwertRev - sample.istw.min) > zwspSource.ansprZuO))
                ||
                ((regDiff > zwspSource.ansprAufO) && !sample.zustand.wasDown() &&
                ((zwspSource.sollwertRev - sample.istw.max) > zwspSource.ansprAufO))
            ) {
                zwsp.aufO = zwspSource.ansprAufO + zwspSource.ansprBand;
                zwsp.zuV = zwspSource.ansprZuO - zwspSource.ansprBand;
            }
        }

        if ((zwspSource.nerker1 & STROM_GRENZ) != 0) {
            if (zwspSource.wirkFall == 0) {
                zwsp.aufO = zwsp.aufO + 37;
            } else {
                zwsp.zuV = zwsp.zuV - 37;
            }
        }
        return zwsp;
    }
}
