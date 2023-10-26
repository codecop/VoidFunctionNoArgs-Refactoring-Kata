package sample;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sample.Constants.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class SampleTest {

    final boolean COVERAGE_MODE = false;

    @Test
    void combinationTest() throws IllegalAccessException, IOException {
        // all values
        final List<Integer> allAnsprAufO = Arrays.asList(-1, 0, 1); // copied to AnsprAufV
        final List<Integer> allAnsprAufV = Arrays.asList(-1, 0, 1);
        final List<Integer> allAnsprBand = Arrays.asList(-1, 0);
        final List<Integer> allAnsprHyst = Arrays.asList(-1, 0, 1);
        final List<Integer> allAnsprZuO = Arrays.asList(0, 1);
        final List<Integer> allAnsprZuV = Arrays.asList(0);
        final List<Integer> allAutoIbsOk = Arrays.asList(0, C_IBS_OK);
        final List<Integer> allBinSteuer = Arrays.asList(0, BO_REGLER);
        final List<Integer> allNerker1 = Arrays.asList(0, STROM_GRENZ);
        final List<Integer> allNImpuls = Arrays.asList(0, TOTZONE, TY_GRENZ_1, TY_GRENZ_2);
        final List<Integer> allRegDiff = Arrays.asList(-1, 0, 1);
        final List<Integer> allRegDiffSch = Arrays.asList(-1, 0, 1, 10);
        final List<Integer> allRegMode = Arrays.asList(0, N_AUTOMATIK, N_VALVE_DIAG);
        final List<Integer> allSollwertRev = Arrays.asList(0, 10);
        final List<Integer> allStellFwd = Arrays.asList(0); // is out param
        final List<Integer> allStellIstRev = Arrays.asList(0, 50);
        final List<Integer> allWirkFall = Arrays.asList(0, 1);

        StringBuilder totalState = new StringBuilder();
        for (int AnsprAufO : allAnsprAufO) {
            for (int AnsprAufV : allAnsprAufV) {
                for (int AnsprBand : allAnsprBand) {
                    for (int AnsprHyst : allAnsprHyst) {
                        for (int AnsprZuO : allAnsprZuO) {
                            for (int AnsprZuV : allAnsprZuV) {
                                for (int AutoIbsOk : allAutoIbsOk) {
                                    for (int BinSteuer : allBinSteuer) {
                                        for (int Nerker1 : allNerker1) {
                                            for (int NImpuls : allNImpuls) {
                                                for (int RegDiff : allRegDiff) {
                                                    for (int RegDiffSch : allRegDiffSch) {
                                                        for (int RegMode : allRegMode) {
                                                            for (int SollwertRev : allSollwertRev) {
                                                                for (int StellFwd : allStellFwd) {
                                                                    for (int StellIstRev : allStellIstRev) {
                                                                        for (int WirkFall : allWirkFall) {

                                                                            // set all globals
                                                                            Globals.AnsprAufO = AnsprAufO;
                                                                            Globals.AnsprAufV = AnsprAufV;
                                                                            Globals.AnsprBand = AnsprBand;
                                                                            Globals.AnsprHyst = AnsprHyst;
                                                                            Globals.AnsprZuO = AnsprZuO;
                                                                            Globals.AnsprZuV = AnsprZuV;
                                                                            Globals.AutoIbsOk = AutoIbsOk;
                                                                            Globals.BinSteuer = BinSteuer;
                                                                            Globals.Nerker1 = Nerker1;
                                                                            Globals.NImpuls = NImpuls;
                                                                            Globals.RegDiff = RegDiff;
                                                                            Globals.RegDiffSch = RegDiffSch;
                                                                            Globals.RegMode = RegMode;
                                                                            Globals.SollwertRev = SollwertRev;
                                                                            Globals.StellFwd = StellFwd;
                                                                            Globals.StellIstRev = StellIstRev;
                                                                            Globals.WirkFall = WirkFall;

                                                                            // run code
                                                                            Sample.theFunctionToTest();

                                                                            // capture state
                                                                            totalState.append(captureState());
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        assertTotalState(totalState.toString());
    }

    private String captureState() throws IllegalAccessException {
        StringBuilder state = new StringBuilder();
        Field[] fields = Globals.class.getDeclaredFields();
        Arrays.sort(fields, Comparator.comparing(Field::getName));
        List<String> readOnlyFields = Arrays.asList(("AnsprAufO,AnsprZuO,AnsprBand,AnsprHyst,RegDiff," +
                "RegDiffSch,SollwertRev,StellIstRev,WirkFall,AutoIbsOk,BinSteuer,Nerker1," +
                "RegMode").split(","));
        for (Field field : fields) {
            if (!Modifier.isPublic(field.getModifiers())) {
                continue;
            }
            if (readOnlyFields.contains(field.getName())) {
                continue;
            }
            //state.append(field.getName());
            //state.append(';');
            state.append(field.get(null));
            state.append(',');
        }
        state.append('\n');
        return state.toString();
    }

    private void assertTotalState(String received) throws IOException {
        // String prefix = "src/test/java/sample/totalState.";
        String prefix = "target/totalState.";
        String suffix = ".txt";
        Path receivedFile = Paths.get(prefix + "received" + suffix);

        Files.write(receivedFile, received.getBytes());

        Path approvedFile = Paths.get(prefix + "approved" + suffix);

        if (COVERAGE_MODE || !approvedFile.toFile().exists()) {
            Files.write(approvedFile, received.getBytes());
        }

        String approved = new String(Files.readAllBytes(approvedFile));
        assertEquals(approved, received);

        // success
        assertTrue(receivedFile.toFile().delete());
    }

}
