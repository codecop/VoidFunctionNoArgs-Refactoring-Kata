package sample;

import static sample.Constants.STATE_MOVE_DOWN;
import static sample.Constants.STATE_MOVE_UP;
import static sample.Constants.STATE_OUTSIDE_DEADZONE;
import static sample.Constants.STATE_WITHIN_DEADZONE;

class SampleZustand {
    private final int[] values = new int[]{STATE_OUTSIDE_DEADZONE, STATE_OUTSIDE_DEADZONE};

    boolean isDeadzone() {
        return current() == STATE_WITHIN_DEADZONE;
    }

    private int current() {
        return values[0];
    }

    boolean wasDown() {
        return previous() == STATE_MOVE_DOWN;
    }

    private int previous() {
        return values[1];
    }

    boolean wasUp() {
        return previous() == STATE_MOVE_UP;
    }

    void setFrom(StellFwd stellFwd, boolean totzone) {
        int newValue = current();
        if (totzone) {
            newValue = STATE_WITHIN_DEADZONE;
        }
        if (stellFwd.isZu()) {
            newValue = STATE_MOVE_DOWN;
        }
        if (stellFwd.isAuf()) {
            newValue = STATE_MOVE_UP;
        }
        remember(newValue);
    }

    private void remember(int zw) {
        if (zw != current()) {
            values[1] = current();
            values[0] = zw;
        }
    }
}
