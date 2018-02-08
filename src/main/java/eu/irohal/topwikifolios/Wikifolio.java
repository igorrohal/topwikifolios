package eu.irohal.topwikifolios;

public class Wikifolio {

    private String name;
    private String points;
    private String isin;
    private String fromBeginning;
    private String fromFirstEmission;
    private String fromYearBeginning;
    private String oneMonth;
    private String threeMonths;
    private String sixMonths;
    private String maximalLoss;
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

    public String getFromFirstEmission() {
        return fromFirstEmission;
    }

    public String getThreeMonths() {
        return threeMonths;
    }

    public String getSixMonths() {
        return sixMonths;
    }

    public void setFromFirstEmission(String fromFirstEmission) {
        this.fromFirstEmission = fromFirstEmission;
    }

    public void setThreeMonths(String threeMonths) {
        this.threeMonths = threeMonths;
    }

    public void setSixMonths(String sixMonths) {
        this.sixMonths = sixMonths;
    }

    public String getFromYearBeginning() {
        return fromYearBeginning;
    }

    public void setFromYearBeginning(String fromYearBeginning) {
        this.fromYearBeginning = fromYearBeginning;
    }

    @Override
    public String toString() {
        return "Wikifolio{" +
                "name='" + name + '\'' +
                ", points='" + points + '\'' +
                ", isin='" + isin + '\'' +
                ", fromBeginning='" + fromBeginning + '\'' +
                ", fromFirstEmission='" + fromFirstEmission + '\'' +
                ", fromYearBeginning='" + fromYearBeginning + '\'' +
                ", oneMonth='" + oneMonth + '\'' +
                ", threeMonths='" + threeMonths + '\'' +
                ", sixMonths='" + sixMonths + '\'' +
                ", maximalLoss='" + maximalLoss + '\'' +
                ", totalCapital='" + totalCapital + '\'' +
                ", bid='" + bid + '\'' +
                ", ask='" + ask + '\'' +
                ", mittel='" + mittel + '\'' +
                '}';
    }
}
