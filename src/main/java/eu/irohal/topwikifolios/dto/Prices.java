package eu.irohal.topwikifolios.dto;

import java.math.BigDecimal;

public class Prices {

    private BigDecimal ask;
    private BigDecimal bid;
    private BigDecimal midPrice;
    private boolean showMidPrice;

    public BigDecimal getAsk() {
        return ask;
    }

    public void setAsk(BigDecimal ask) {
        this.ask = ask;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    public BigDecimal getMidPrice() {
        return midPrice;
    }

    public void setMidPrice(BigDecimal midPrice) {
        this.midPrice = midPrice;
    }

    public boolean isShowMidPrice() {
        return showMidPrice;
    }

    public void setShowMidPrice(boolean showMidPrice) {
        this.showMidPrice = showMidPrice;
    }

    @Override
    public String toString() {
        return "Prices{" +
                "ask=" + ask +
                ", bid=" + bid +
                ", midPrice=" + midPrice +
                ", showMidPrice=" + showMidPrice +
                '}';
    }
}
