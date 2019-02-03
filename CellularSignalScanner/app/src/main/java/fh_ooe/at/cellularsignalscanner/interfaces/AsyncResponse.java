package fh_ooe.at.cellularsignalscanner.interfaces;

import fh_ooe.at.cellularsignalscanner.data.ScanServiceMetadata;

public interface AsyncResponse {
    void processFinish(ScanServiceMetadata scanServiceMetadata);
}
