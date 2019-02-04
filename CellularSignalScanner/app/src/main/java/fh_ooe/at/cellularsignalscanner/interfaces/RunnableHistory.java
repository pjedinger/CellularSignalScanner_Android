package fh_ooe.at.cellularsignalscanner.interfaces;

import fh_ooe.at.cellularsignalscanner.data.ScanServiceMetadata;

public interface RunnableHistory extends Runnable{
    void processFinish(ScanServiceMetadata scanServiceMetadata);
}
