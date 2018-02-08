package eu.irohal.topwikifolios;

import org.springframework.stereotype.Component;

@Component
public class WikifolioSerializer {

    public String topRankedHeader(final char delimiter) {
        return "Name" + delimiter +
                "Punktestand" + delimiter +
                "seit Beginn" + delimiter +
                "1 Monat" + delimiter +
                "Max.Verlust" + delimiter +
                "ISIN" + delimiter +
                "Invst.Kapital" + delimiter +
                "Bid" + delimiter +
                "Ask" + delimiter +
                "Mittel";
    }

    public String topRanked(final Wikifolio wikifolio, final char delimiter) {
        return wikifolio.getName() + delimiter
                + wikifolio.getPoints() + delimiter
                + wikifolio.getFromBeginning() + delimiter
                + wikifolio.getOneMonth() + delimiter
                + wikifolio.getMaximalLoss() + delimiter
                + wikifolio.getIsin() + delimiter
                + wikifolio.getTotalCapital() + delimiter
                + (wikifolio.getBid() != null ? wikifolio.getBid() : "") + delimiter
                + (wikifolio.getAsk() != null ? wikifolio.getAsk() : "") + delimiter
                + (wikifolio.getMittel() != null ? wikifolio.getMittel() : "");
    }

    public String topPerformanceHeader(final char delimiter) {
        return "Name" + delimiter +
                "Punktestand" + delimiter +
                "seit Beginn" + delimiter +
                "seit Emission" + delimiter +
                "seit Jahresbeginn" + delimiter +
                "1 Monat" + delimiter +
                "3 Monate" + delimiter +
                "6 Monate" + delimiter +
                "Max.Verlust" + delimiter +
                "ISIN" + delimiter +
                "Invst.Kapital" + delimiter +
                "Bid" + delimiter +
                "Ask" + delimiter +
                "Mittel";
    }

    public String topPerformance(final Wikifolio wikifolio, final char delimiter) {
        return wikifolio.getName() + delimiter
                + wikifolio.getPoints() + delimiter
                + wikifolio.getFromBeginning() + delimiter
                + wikifolio.getFromFirstEmission() + delimiter
                + wikifolio.getFromYearBeginning() + delimiter
                + wikifolio.getOneMonth() + delimiter
                + wikifolio.getThreeMonths() + delimiter
                + wikifolio.getSixMonths() + delimiter
                + wikifolio.getMaximalLoss() + delimiter
                + wikifolio.getIsin() + delimiter
                + (wikifolio.getTotalCapital() != null ? wikifolio.getTotalCapital() : "") + delimiter
                + (wikifolio.getBid() != null ? wikifolio.getBid() : "") + delimiter
                + (wikifolio.getAsk() != null ? wikifolio.getAsk() : "") + delimiter
                + (wikifolio.getMittel() != null ? wikifolio.getMittel() : "");
    }

}
