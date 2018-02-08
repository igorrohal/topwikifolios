package eu.irohal.topwikifolios;

import static eu.irohal.topwikifolios.Configuration.CSV_DELIMITER;
import static eu.irohal.topwikifolios.Configuration.WIKIFOLIOS_TOP_RANKED_BASE;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TopRankedFetcher {

    private static final Logger log = LoggerFactory.getLogger(TopRankedFetcher.class);

    private static final List<String> WIKIFOLIOS_URLS = Arrays.asList(
            WIKIFOLIOS_TOP_RANKED_BASE,
            WIKIFOLIOS_TOP_RANKED_BASE + "&startValue=12",
            WIKIFOLIOS_TOP_RANKED_BASE + "&startValue=24",
            WIKIFOLIOS_TOP_RANKED_BASE + "&startValue=36",
            WIKIFOLIOS_TOP_RANKED_BASE + "&startValue=48");

    private String latest = "";

    @Autowired
    private WikifolioDetailsParser wikifolioDetailsParser;

    @Autowired
    private WikifolioSerializer wikifolioSerializer;

    public void fetch() {
        log.info("TopRankedFetcher triggered at " + LocalTime.now());

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
                .map(w -> wikifolioSerializer.topRanked(w, CSV_DELIMITER))
                .reduce(wikifolioSerializer.topRankedHeader(CSV_DELIMITER), (i, ws) -> i += "\n" + ws);

        latest = csvContents;
    }

    // TODO: remove duplicacy by having a single fetch-site code
    private List<Wikifolio> fetchSite(final String path) throws IOException {
        final Document doc = fetchDocument(path);
        final Elements wikifolioOuterDivs = doc.select("div.wikifolio-preview");

        return wikifolioOuterDivs.stream().map(elem -> {
            final String wikifolioId = elem.attr("data-wikifolioid");
            final String wikifolioDetailsPath = elem.selectFirst("a.wikifolio-preview-title-link").attr("href");

            final Wikifolio wikifolio = wikifolioDetailsParser.fetchWikifolio(wikifolioDetailsPath, wikifolioId);

            try {
                Thread.sleep(1000); // just to be safe :)
            } catch (InterruptedException e) {
                log.error("Sleep interrupted", e);
            }

            log.info("Finished Wikifolio: " + wikifolio.getName());
            return wikifolio;
        }).collect(Collectors.toList());
    }

    public String getLatest() {
        return latest;
    }

    private Document fetchDocument(final String url) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        final ResponseEntity<String> responseEntity = new RestTemplate().exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        return Jsoup.parse(responseEntity.getBody());
    }

}
