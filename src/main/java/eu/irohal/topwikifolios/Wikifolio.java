package eu.irohal.topwikifolios;

public class Wikifolio {

    public static final String HEADER = "Name;Erstellungsdatum;Punktestand;seit Beginn;1 Monat;Max.Verlust;Erstemission;Performancegebühr;ISIN;Invst.Kapital;Bid;Ask;Mittel";

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
    private String bid;
    private String ask;
    private String mittel;

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

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getMittel() {
        return mittel;
    }

    public void setMittel(String mittel) {
        this.mittel = mittel;
    }

    public String getTotalCapital() {
        return totalCapital;
    }
    public String csvSerialize(final char delimiter) {
            return getName().replaceAll(",", " ") + delimiter
                    + getFounded() + delimiter
                    + getPoints() + delimiter
                    + getFromBeginning() + delimiter
                    + getOneMonth() + delimiter
                    + getMaximalLoss() + delimiter
                    + getFirstEmission() + delimiter
                    + getPerformanceFee() + delimiter
                    + getIsin() + delimiter
                    + getTotalCapital() + delimiter
                    + (getBid() != null ? getBid() : "") + delimiter
                    + (getAsk() != null ? getAsk() : "") + delimiter
                    + (getMittel() != null ? getMittel() : "");
    }

    public void setPointsRawData(final String pointsRawData) {
        this.points = pointsRawData
                .replace(".", "")
                .replace(" Pkt", "");
    }

}
