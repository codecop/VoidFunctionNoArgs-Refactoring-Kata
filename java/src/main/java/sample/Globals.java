package sample;
public class Globals {

    /*
        possible groups:
         AnsprAufO
         AnsprZuO
         AnsprBand
         AnsprHyst
         RegDiff
         SollwertRev
     */
    public static int AnsprAufO = 0; // only read, works with AnsprHyst, RegDiff, SollwertRev
    public static int AnsprZuO = 0; // only read, works with AnsprHyst, RegDiff, SollwertRev

    public static int AnsprAufV = 0; // read/write, works with RegDiffSch

    public static int AnsprBand = 0; // only read, works with AnsprAufO, AnsprZuO
    public static int AnsprHyst = 0; // only read, works with AnsprAufO, AnsprZuO
    public static int RegDiff = 0; // only read,works with AnsprAufO, AnsprZuO, ...

    public static int AnsprZuV = 0; // read/write,  locals ...
    public static int RegDiffSch = 0; // only read, locals ...
    public static int SollwertRev = 0; // only read, ...
    public static int StellIstRev = 0; // only read, locals
    public static int WirkFall = 0; // only read, 0 or not

    // single bit usage, only if active
    /* DONE */ public static int AutoIbsOk = 0; // only read, enum C_IBS_OK or not
    /* DONE */ public static int BinSteuer = 0; // only read, bit BO_REGLER or not
    /* DONE */ public static int RegMode = 0; // enum N_AUTOMATIC, N_VALVE or not

    // single bit usage
    public static int Nerker1 = 0; // only read, bit STROM_GRENZ or not

    // written
    public static int NRegFkt = 0; // only write: reset and set TOTZONE_ALT (= flag)
    public static int NImpuls = 0; // read/write, bit TOTZONE, TY_GRENZ1, 2 or not
    /* DONE */ public static int StellFwd = 0; // read/write, bits ZU*, AUF*
}
