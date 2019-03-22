package com.samalex.slucapstone;

public class InterventionDisplayData {
    private boolean showLiveReport;
    private boolean showMorningReport;

    public InterventionDisplayData(boolean showLiveReport, boolean showMorningReport) {
        this.showLiveReport = showLiveReport;
        this.showMorningReport = showMorningReport;
    }

    public boolean isShowLiveReport() {
        return showLiveReport;
    }

    public boolean isShowMorningReport() {
        return showMorningReport;
    }
}
