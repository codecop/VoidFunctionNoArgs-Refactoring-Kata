package sample;

import static sample.Constants.PERCENT_MAX;
import static sample.Constants.PERCENT_MIN;

class SampleIstwStruct {
    int min;
    int max;

    // pure functions
    static void setFrom(SampleIstwStruct self, boolean totzone, int stellIstRev) {
        if (totzone) {
            if (stellIstRev < self.min) {
                self.min = stellIstRev;
            }
            if (stellIstRev > self.max) {
                self.max = stellIstRev;
            }
        } else {
            self.max = PERCENT_MIN;
            self.min = PERCENT_MAX;
        }
    }
}
