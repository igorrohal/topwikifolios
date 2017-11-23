package eu.irohal.topwikifolios;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.net.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

@Component
public class WikifolioParser {

    private static final Logger log = LoggerFactory.getLogger(WikifolioParser.class);

    private static final String WIKIFOLIO_DETAIL_BASE = "https://www.wikifolio.com";
    private static final String WIKIFOLIOS_LIST_BASE = "https://www.wikifolio.com/dynamic/de/de/wikifoliosearch/search?tags=aktde,akteur,aktusa,akthot,aktint,etf,fonds,anlagezert,hebel&media=true&private=true&assetmanager=true&theme=true&WithoutLeverageProductsOnly=true&languageOnly=true";
    private static final List<String> WIKIFOLIOS_URLS = Arrays.asList(
            WIKIFOLIOS_LIST_BASE,
            WIKIFOLIOS_LIST_BASE + "&startValue=12",
            WIKIFOLIOS_LIST_BASE + "&startValue=24",
            WIKIFOLIOS_LIST_BASE + "&startValue=36");

    private static final char DELIMETER = ';';

    private String latestCsvContents = "";

    public void fetchWikifolios() {
        log.info("Triggered at " + LocalTime.now());

        final List<Wikifolio> allWikifolios = WIKIFOLIOS_URLS.stream()
                .map(url -> {
                    try {
                        return fetchSite(url);
                    } catch (IOException e) {
                        log.error("IOException", e);
                    }
                    return Collections.EMPTY_LIST;
                })
                .reduce((site1, site2) -> {
                    site1.addAll(site2);
                    return site1;
                }).get();

        final String csvContents = allWikifolios.stream()
                .map(w -> w.csvSerialize(DELIMETER))
                .reduce(Wikifolio.HEADER, (i, ws) -> i += "\n" + ws);

        latestCsvContents = csvContents;
    }

    private List<Wikifolio> fetchSite(final String path) throws MalformedURLException, IOException {
        final Document doc = Jsoup.parse(new URL(path), 10000);

        final Elements wikifolioOuterDivs = doc.select("div.wikifolio-preview");

        return  wikifolioOuterDivs.stream().map(elem -> {
            final Wikifolio w = new Wikifolio();
            w.setName(elem.selectFirst("span.js-wikifolio-shortdescription").text());
            final Elements textValueSpans = elem.select("div.wikifolio-preview-data-container span");
            final Iterator<Element> iter = textValueSpans.iterator();
            while(iter.hasNext()) {
                final String spanText = iter.next().text();
                if(spanText.contains("Top-wikifolio-Rangliste")) {
                    final String nextSpanText = iter.next().text();
                    w.setPointsRawData(nextSpanText);
                    continue;
                } else if(spanText.contains("Erstellungsdatum")) {
                    final String nextSpanText = iter.next().text();
                    w.setFounded(nextSpanText);
                    continue;
                } else if(spanText.contains("Performance seit Beginn")) {
                    final String nextSpanText = iter.next().text();
                    w.setFromBeginning(nextSpanText);
                    continue;
                } else if(spanText.contains("Performance 1 Monat")) {
                    final String nextSpanText = iter.next().text();
                    w.setOneMonth(nextSpanText);
                    continue;
                } else if(spanText.contains("Maximaler Verlust (bisher)")) {
                    final String nextSpanText = iter.next().text();
                    w.setMaximalLoss(nextSpanText);
                    continue;
                } else if(spanText.contains("Erstemission")) {
                    final String nextSpanText = iter.next().text();
                    w.setFirstEmission(nextSpanText);
                    continue;
                } else if(spanText.contains("Performancegeb")) {
                    final String nextSpanText = iter.next().text();
                    w.setPerformanceFee(nextSpanText);
                    continue;
                } else if(spanText.contains("ISIN")) {
                    final String nextSpanText = iter.next().text();
                    w.setIsin(nextSpanText);
                    continue;
                } else if(spanText.contains("vestiertes Kapital")) {
                    final String nextSpanText = iter.next().text();
                    w.setTotalCapital(nextSpanText);
                    continue;
                }
            }
            final String wikifolioPath = WIKIFOLIO_DETAIL_BASE + elem.selectFirst("a.wikifolio-preview-title-link").attr("href");
            try {
                fetchCurrentPrices(w, wikifolioPath);
            } catch (IOException e) {
                log.error("IOException", e);
            }

            try {
                Thread.sleep(2000); // just to be safe :)
            } catch (InterruptedException e) {
                log.error("Sleep interrupted", e);
            }

            log.info("Finished Wikifolio: " + w.getName());
            return w;
        }).collect(Collectors.toList());
    }

    private void fetchCurrentPrices(final Wikifolio wikifolio, final String wikifolioPath) throws MalformedURLException, IOException {
        final Document doc = Jsoup.parse(new URL(wikifolioPath), 10000);
        final Element firstRateElem = doc.selectFirst("h4.wikifolio-detail-performance-header");
        if(firstRateElem.parent().text().contains("Mittelkurs")) {
            wikifolio.setMittel(firstRateElem.ownText());
        } else {
            final Element bidRateElem = firstRateElem;
            wikifolio.setBid(bidRateElem.ownText());
            bidRateElem.remove();
            final Element askRateElem = doc.selectFirst("h4.wikifolio-detail-performance-header");
            wikifolio.setAsk(askRateElem.ownText());
        }
    }

    public String getLatestCsvContents() {
        return latestCsvContents;
    }
}
