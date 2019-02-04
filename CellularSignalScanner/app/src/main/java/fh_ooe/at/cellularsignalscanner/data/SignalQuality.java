package fh_ooe.at.cellularsignalscanner.data;

public enum SignalQuality {
    VERY_BAD(-110), BAD(-100), AVERAGE(-80), GOOD(-75), VERY_GOOD(-70);

    private int dbm;

    private SignalQuality(int dbm){
        this.dbm = dbm;
    }

    public static SignalQuality getSignalQuality(int dbm){
            if(dbm >= VERY_GOOD.dbm){
                return VERY_GOOD;
            }else if(dbm >= GOOD.dbm){
                return GOOD;
            }else if(dbm >= AVERAGE.dbm){
                return AVERAGE;
            }else if(dbm >= BAD.dbm){
                return BAD;
            }else if(dbm >= VERY_BAD.dbm){
                return VERY_BAD;
            }
        return null;
    }
}
