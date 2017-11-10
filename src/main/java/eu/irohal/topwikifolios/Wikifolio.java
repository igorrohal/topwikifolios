package eu.irohal.topwikifolios;

public class Wikifolio {

    private String name;
    private String founded;
    private String points;
    private String isin;
    private String fromBeginning;
    private String oneMonth;
    private String maximalLoss;
    private String firstEmission;
    private String performanceFee;
    private String totalCapital;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFounded() {
        return founded;
    }

    public void setFounded(String founded) {
        this.founded = founded;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getFromBeginning() {
        return fromBeginning;
    }

    public void setFromBeginning(String fromBeginning) {
        this.fromBeginning = fromBeginning;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public void setOneMonth(String oneMonth) {
        this.oneMonth = oneMonth;
    }

    public String getOneMonth() {
        return oneMonth;
    }

    public void setMaximalLoss(String maximalLoss) {
        this.maximalLoss = maximalLoss;
    }

    public String getMaximalLoss() {
        return maximalLoss;
    }

    public void setFirstEmission(String firstEmission) {
        this.firstEmission = firstEmission;
    }

    public String getFirstEmission() {
        return firstEmission;
    }

    public void setPerformanceFee(String performanceFee) {
        this.performanceFee = performanceFee;
    }

    public String getPerformanceFee() {
        return performanceFee;
    }

    public void setTotalCapital(String totalCapital) {
        this.totalCapital = totalCapital;
    }

    public String getTotalCapital() {
        return totalCapital;
    }
    public String csvSerialize(final char delimiter) {
            return getName() + delimiter
                    + getFounded() + delimiter
                    + getPoints() + delimiter
                    + getFromBeginning() + delimiter
                    + getOneMonth() + delimiter
                    + getMaximalLoss() + delimiter
                    + getFirstEmission() + delimiter
                    + getPerformanceFee() + delimiter
                    + getIsin() + delimiter
                    + getTotalCapital();
    }

    public void setPointsRawData(final String pointsRawData) {
        this.points = pointsRawData
                .replace(".", "")
                .replace(" Pkt", "");
    }

}
