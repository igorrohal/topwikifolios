package eu.irohal.topwikifolios;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WikifolioParser {

    private static final Logger log = LoggerFactory.getLogger(WikifolioParser.class);

    private static final String WIKIFOLIOS_URL_BASE = "https://www.wikifolio.com/dynamic/de/de/wikifoliosearch/search?tags=aktde,akteur,aktusa,akthot,aktint,etf,fonds,anlagezert,hebel&media=true&private=true&assetmanager=true&theme=true&WithoutLeverageProductsOnly=true&languageOnly=true";
    private static final List<String> WIKIFOLIOS_URLS = Arrays.asList(
            WIKIFOLIOS_URL_BASE,
            WIKIFOLIOS_URL_BASE + "&startValue=12",
            WIKIFOLIOS_URL_BASE + "&startValue=24",
            WIKIFOLIOS_URL_BASE + "&startValue=36",
            WIKIFOLIOS_URL_BASE + "&startValue=48",
            WIKIFOLIOS_URL_BASE + "&startValue=60",
            WIKIFOLIOS_URL_BASE + "&startValue=72");

    	private static final String OUTPUT_FILE_PATH = "/Users/d061757/Google Drive/TopWikifolios/topwikifolios.csv";
    private static final char DELIMETER = ';';
    private static final String HEADER = "Name;Erstellungsdatum;Punktestand;seit Beginn;1 Monat;Max.Verlust;Erstemission;Performancegebühr;ISIN;Invst.Kapital";

    final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(cron = "0 0/30 * * * ?") // twice / hour
    public void fetchWikifolios() {
        final List<Wikifolio> allWikifolios = WIKIFOLIOS_URLS.stream()
                .map(url -> fetchSite(url))
                .reduce((site1, site2) -> {
                    site1.addAll(site2);
                    return site1;
                }).get();

        final String csvContents = allWikifolios.stream()
                .map(w -> w.csvSerialize(DELIMETER))
                .reduce(HEADER, (i, ws) -> i += "\n" + ws);

        writeNewFile(csvContents);
    }

    private List<Wikifolio> fetchSite(final String path) {
        final ResponseEntity<String> response = restTemplate.getForEntity(path, String.class);
        final String html = response.getBody();
//        log.info(html);
        final Document doc = Jsoup.parse(html);

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
            return w;
        }).collect(Collectors.toList());
    }

    private void writeNewFile(final String fileContents) {
        try {
            FileUtils.writeStringToFile(new File(OUTPUT_FILE_PATH), fileContents, "UTF-8");
        } catch (IOException e) {
            log.error("Writing file failed.", e);
        }
    }

}
