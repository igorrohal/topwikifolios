package eu.irohal.topwikifolios;

import static eu.irohal.topwikifolios.Configuration.CSV_DELIMITER;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Iterator;

@Component
public class WikifolioDetailsParser {

    private static final Logger log = LoggerFactory.getLogger(WikifolioDetailsParser.class);

    private final static String WIKIFOLIO_ROOT_URI = "https://www.wikifolio.com";
    private final static String AUTH_COOKIE = "theAuthCookie=7E21713E6C265DC603B57D84CDB9287D88D6F20251365E2975EFF883700402B5D88DE970DA51933DDF5169926C85235E90CB0955F1BA4FDD06FF2D64513D62D224A5435DE3CEFBD4DA2CACF1685D24F00726DD621B5F6A89B4B554A073FFA54EC7C5AA494E1ECDD09BCBC02DD182A8E42BD341A38AA833C1FEAE62DE38D3B674226849B085A4361333F803596E0CF91113C737191C52027C01E07AA791F0CCE8446282149D533410B455EEF809A4E9465F4274D14A4ABDCA4CC6A3D81940C76D59B4639A3D0951FA0811D5FF6A9F60981AB24B0E070E094F42887E0635FA5EF962032CE0C163E0B1104C953A1E5CF9208A7E492E7A30D9D0995F5FCE2A8020FC3006525E6AC7AA02CDDF602A9B083EF7AD9A566DA5C4CD5DEE916D2FD2D52546C505DC133BA7FAF125BA6729D3756A9B3322D6A7B990916C714001088308A964A6C06AD63B30D856C1152AA26F29E4E8FD4C15BF054732F53F207CAE8FDE4688B3760B5458BCC03964007C066C428690F67E804026D836B6E1F0BECDAF822BA545FD83629E7460A6E05E2A2031B66A317A4E7A5EE8DDF8FA5848B709C55AC9DC03AD44113C34C9B8B5DA5290BD7F7D01A573D94B0B9650F7DFCAFE59AEF24E6C9F12883228DD5B303C5D7F00E78C47003436B78AAA1819FFB4C28B26F1BF185900D38C19CEE9B6840CE5C615118DBABD79C961E9DB0AA60BD9278D458A5518AC408DF2B75B9029BFCAD03790EDD58D86EF3B3EC9D9A2FBF341B3B71C4BE45691C05F9E457525D1B6FFF791032D397C5C2F18112209C48B7D9485F62E8F15B0E942A4E9C3";
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private WikifolioSerializer wikifolioSerializer;

    public Wikifolio fetchWikifolio(final String detailsPath){
        log.info("Fetching wikifolio " + detailsPath);

        try {
            final Wikifolio wikifolio = fetchAndParseDocument(detailsPath);
            System.out.println(wikifolioSerializer.topRanked(wikifolio, CSV_DELIMITER));
            System.out.println(wikifolioSerializer.topPerformance(wikifolio, CSV_DELIMITER));
            return wikifolio;
        } catch (Exception e) {
            log.error("Smth went wrong while fetching wikifolio details", e);
            throw new RuntimeException(e);
        }
    }

    private Wikifolio fetchAndParseDocument(final String path) throws URISyntaxException, IOException {
        final Document doc = fetchDocument(WIKIFOLIO_ROOT_URI + path);
        final Wikifolio wikifolio = new Wikifolio();
        fetchPerformances(doc, wikifolio);
        fetchCurrentPrices(doc, wikifolio);
        fetchRestOfInfos(doc, wikifolio);
        return wikifolio;
    }

    private void fetchPerformances(final Document doc, final Wikifolio wikifolio) {
        final Elements detailRows = doc.select("div.detail-row");
        final Iterator<Element> iter = detailRows.iterator();

        while(iter.hasNext()) {
            final Element detailRowDiv = iter.next();
            final String detailRowText = detailRowDiv.ownText().trim();
            if(detailRowText.contains("Top-wikifolio-Rangliste")){
                final String spanText = detailRowDiv.select("span").first().text().trim();
                wikifolio.setPoints(spanText.replace(".", "").replace("Punkte", "").trim());
                continue;
            }
            if(detailRowText.contains("Performance seit Beginn")){
                final String spanText = detailRowDiv.select("span").first().text().trim();
                wikifolio.setFromBeginning(spanText);
                continue;
            }
            if(detailRowText.contains("Performance seit Emission")){
                final String spanText = detailRowDiv.select("span").first().text().trim();
                wikifolio.setFromFirstEmission(spanText);
                continue;
            }
            if(detailRowText.contains("Performance seit Jahresbeginn")){
                final String spanText = detailRowDiv.select("span").first().text().trim();
                wikifolio.setFromYearBeginning(spanText);
                continue;
            }
            if(detailRowText.contains("Performance 6 Monate")){
                final String spanText = detailRowDiv.select("span").first().text().trim();
                wikifolio.setSixMonths(spanText);
                continue;
            }
            if(detailRowText.contains("Performance 3 Monate")){
                final String spanText = detailRowDiv.select("span").first().text().trim();
                wikifolio.setThreeMonths(spanText);
                continue;
            }
            if(detailRowText.contains("Performance 1 Monat")){
                final String spanText = detailRowDiv.select("span").first().text().trim();
                wikifolio.setOneMonth(spanText);
                continue;
            }
            if(detailRowText.contains("Maximaler Verlust")){
                final String spanText = detailRowDiv.select("span").first().text().trim();
                wikifolio.setMaximalLoss(spanText);
                continue;
            }
        }
    }

    private void fetchRestOfInfos(final Document doc, final Wikifolio wikifolio) {
        // name
        wikifolio.setName(doc.select("span.wikifolio-title").text());
        // ISIN
        wikifolio.setIsin(doc.select("span.js-copy-isin").text());
        // total capital
        final Element element = doc.select("h4.wikifolio-detail-performance-header").last();
        wikifolio.setTotalCapital(element.ownText().trim());
    }

    private void fetchCurrentPrices(final Document doc, final Wikifolio wikifolio) {
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

    private Document fetchDocument(final String url) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        headers.set("Cookie", AUTH_COOKIE);
        final ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        return Jsoup.parse(responseEntity.getBody());
    }

}
