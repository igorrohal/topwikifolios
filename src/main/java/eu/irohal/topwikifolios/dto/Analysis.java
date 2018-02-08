package eu.irohal.topwikifolios.dto;

import java.util.List;

public class Analysis {

    private List<AnalysisItem> keyFigures;

    public List<AnalysisItem> getKeyFigures() {
        return keyFigures;
    }

    public void setKeyFigures(List<AnalysisItem> keyFigures) {
        this.keyFigures = keyFigures;
    }

    @Override
    public String toString() {
        return "Analysis{" +
                "keyFigures=" + keyFigures +
                '}';
    }
}
