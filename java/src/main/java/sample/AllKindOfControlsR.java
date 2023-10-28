package sample;

import static sample.Constants.BO_REGLER;
import static sample.Constants.C_IBS_OK;
import static sample.Constants.N_AUTOMATIK;
import static sample.Constants.N_VALVE_DIAG;

class AllKindOfControlsR {
    private final int autoIbsOk;
    private final int regMode;
    private final int binSteuer;

    public AllKindOfControlsR(int autoIbsOk, int regMode, int binSteuer) {
        this.autoIbsOk = autoIbsOk;
        this.regMode = regMode;
        this.binSteuer = binSteuer;
    }

    boolean doNotTouchIt() {
        return (autoIbsOk == C_IBS_OK) &&
                ((regMode == N_AUTOMATIK) || (regMode == N_VALVE_DIAG)) &&
                ((binSteuer & BO_REGLER) != 0);
    }
}
