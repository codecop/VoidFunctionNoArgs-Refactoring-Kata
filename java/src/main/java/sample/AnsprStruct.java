package sample;

class AnsprStruct {
    int zuV;
    int aufV;

    static AnsprStruct create(int ansprAufV, int ansprZuV) {
        AnsprStruct anspr = new AnsprStruct();
        anspr.aufV = ansprAufV;
        anspr.zuV = ansprZuV;
        return anspr;
    }

    static void setLimitWithZwsp(AnsprStruct anspr, SampleZwspStructR zwsp) {
        if (anspr.zuV > zwsp.zuV) {
            anspr.zuV = zwsp.zuV;
        }
        if (anspr.aufV > zwsp.aufO) {
            anspr.aufV = zwsp.aufO;
        }
    }
}
