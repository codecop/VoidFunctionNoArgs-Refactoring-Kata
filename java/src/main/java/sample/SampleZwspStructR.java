package sample;

import static sample.Constants.STROM_GRENZ;

/**
 * A type aka a DTO like a Struct.
 * This is readonly.
 */
class SampleZwspStructR {

    final int aufO;
    final int zuV;

    SampleZwspStructR(int aufO, int zuV) {
        this.aufO = aufO;
        this.zuV = zuV;
    }

    // pure functions
    static SampleZwspStructR create(ZwspSourceStructR zwspSource, SampleStruct sample, int regDiff) {
        int aufO = zwspSource.ansprAufO;
        int zuV = zwspSource.ansprZuO;
        if (sample.zustand.isDeadzone()) {
            aufO = zwspSource.ansprAufO + zwspSource.ansprHyst;
            zuV = zwspSource.ansprZuO - zwspSource.ansprHyst;

            if (
                ((regDiff < zwspSource.ansprZuO) && !sample.zustand.wasUp() &&
                ((zwspSource.sollwertRev - sample.istw.min) > zwspSource.ansprZuO))
                ||
                ((regDiff > zwspSource.ansprAufO) && !sample.zustand.wasDown() &&
                ((zwspSource.sollwertRev - sample.istw.max) > zwspSource.ansprAufO))
            ) {
                aufO = zwspSource.ansprAufO + zwspSource.ansprBand;
                zuV = zwspSource.ansprZuO - zwspSource.ansprBand;
            }
        }

        if ((zwspSource.nerker1 & STROM_GRENZ) != 0) {
            if (zwspSource.wirkFall == 0) {
                aufO = aufO + 37;
            } else {
                zuV = zuV - 37;
            }
        }
        return new SampleZwspStructR(aufO, zuV);
    }

    static boolean isAroundRegDiff(SampleZwspStructR zwsp, int regDiff) {
        return (regDiff >= zwsp.zuV) && (regDiff <= zwsp.aufO);
    }
}
