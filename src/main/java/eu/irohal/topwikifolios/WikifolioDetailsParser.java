package eu.irohal.topwikifolios;

import eu.irohal.topwikifolios.dto.Analysis;
import eu.irohal.topwikifolios.dto.Prices;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WikifolioDetailsParser {

    private static final Logger log = LoggerFactory.getLogger(WikifolioDetailsParser.class);

    private final static String WIKIFOLIO_ROOT_URI = "https://www.wikifolio.com";
    private final static String ANALYSIS_PATH = "/api/wikifolio/%s/analysis?country=de&language=de";
    private final static String PRICES_PATH = "/api/wikifolio/%s/price";
    private final static String AUTH_COOKIE = "theAuthCookie=FC9C244293D5589A00143CBBA8AA7A1F013FC525B91F59B175A5B326F504673143B1D6C61B970F43CF939D3D1FB22AE85B5107855E55847CBD5383274BDC4D42E1826BE1124694CEDEF12101C5B2808F00200B0FB7CA9A8934333023C89A0F41D1D846750C036A61C3404E9C73D5900E519148043B7A1C92EDA2B3880D9D861EC4E0604D89C812EDF29E16B09956D4A8FF4B526A64B473B36F9BE74A46D014D67B12DB7BE5C4DB7A36E49340B9450DD711CD54431DDE4AAC4708A3D68414B1EC3564331EFCB31DB632888F180A20CE008659C7C386DB3CB3BB58F9BD3127C2F72703C9E5D57791AA10F865E57A2702551599C6F151ABA3EAF99C072DE2AD49EC60717B05C3C2163A4D862479BC66A46656EE19347836143F07727FC8CA60BA4D96D7D3CFFEA9210E3222BD27C4AA10D85EC9B7A21BECAB835B3B1BA087021153FBD8CDF8C1E9B390CE43536BFA188B56E7DA7FC0BBEE57E4A91CF36D09778A42C8910D78A14CFC48CFCB175FC3F5C8C0E67A19834BF7060B6DD7F4EFE300A2B5048461991C71A7880C9DE446AD609F0A57E987E848DF8CA0D8EF75D38D694BD013DD81B140D61CCC6CE42E5A8F5E1C7C1F5D3AFD4A2EAF05CEBD21DF2F1277E9FED387048F3954395E3C01F719565E233058304D42547944141D9C03AC173E22917377245EE94AD4C4B17F2F4CAB1DD99BB6815907DFE8A1E22E5B79DF5CD6A2FD78024A89AB617C002684376331BAADF765A34C1044B6E6AD6B19D91B05B1B6420969F0A7A6391633E3E1FAD48F33B794CE3223330764DA98F3DC7BB0E8B73F7690499B";
    private final RestTemplate restTemplate = new RestTemplate();

    public Wikifolio fetchWikifolio(final String wikifolioDetailsPath, final String wikifolioId) {
        log.info("Fetching wikifolio " + wikifolioId);

        try {
            final Wikifolio wikifolio = fetchWikifolioAnalysis(wikifolioId);
            fetchWikifolioPrices(wikifolioId, wikifolio);
            fetchRestOfInfos(wikifolioDetailsPath, wikifolio);
            System.out.println(wikifolio);
            return wikifolio;
        } catch (Exception e) {
            log.error("Smth went wrong while fetching wikifolio details", e);
            throw new RuntimeException(e);
        }
    }

    private Wikifolio fetchWikifolioAnalysis(final String wikifolioId) {
        final String url = String.format(WIKIFOLIO_ROOT_URI + ANALYSIS_PATH, wikifolioId);
        final Analysis analysis = fetchResource(url, Analysis.class);
        final Map<String, String> analysisFiguresMap = analysis.getKeyFigures().stream().collect(
                Collectors.toMap(ai -> ai.getLabel(), ai -> ai.getValue()));

        final Wikifolio wikifolio = new Wikifolio();
        setPerformances(analysisFiguresMap, wikifolio);

        return wikifolio;
    }

    private Wikifolio fetchWikifolioPrices(final String wikifolioId, final Wikifolio wikifolio) {
        final String url = String.format(WIKIFOLIO_ROOT_URI + PRICES_PATH, wikifolioId);
        final Prices prices = fetchResource(url, Prices.class);
        setPrices(prices, wikifolio);
        return wikifolio;
    }

    private Wikifolio fetchRestOfInfos(final String wikifolioDetailsPath, final Wikifolio wikifolio) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        headers.set("Cookie", AUTH_COOKIE);
        final ResponseEntity<String> responseEntity = restTemplate.exchange(WIKIFOLIO_ROOT_URI + wikifolioDetailsPath, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        final Document doc = Jsoup.parse(responseEntity.getBody());

        wikifolio.setName(doc.select("div.js-desc-title").text());
        wikifolio.setIsin(doc.select("div.js-copy-isin").text());

        return wikifolio;
    }

    private void setPerformances(final Map<String, String> analysisFiguresMap, final Wikifolio wikifolio) {
        wikifolio.setPoints(analysisFiguresMap.get("Top-wikifolio-Rangliste").trim());
        wikifolio.setFromBeginning(analysisFiguresMap.get("Performance seit Beginn").trim());
        wikifolio.setFromFirstEmission(analysisFiguresMap.get("Performance seit Emission").trim());
        wikifolio.setFromYearBeginning(analysisFiguresMap.get("Performance seit Jahresbeginn").trim());
        wikifolio.setSixMonths(analysisFiguresMap.get("Performance 6 Monate").trim());
        wikifolio.setThreeMonths(analysisFiguresMap.get("Performance 3 Monate").trim());
        wikifolio.setOneMonth(analysisFiguresMap.get("Performance 1 Monat").trim());
        wikifolio.setMaximalLoss(analysisFiguresMap.get("Maximaler Verlust (bisher)").trim());
    }

    private void setPrices(final Prices prices, final Wikifolio wikifolio) {
        if (prices.isShowMidPrice()) {
            wikifolio.setMittel(prices.getMidPrice().toPlainString());
        } else {
            wikifolio.setAsk(prices.getAsk().toPlainString());
            wikifolio.setBid(prices.getBid().toPlainString());
        }
    }

    private <T> T fetchResource(final String url, final Class<T> clazz) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Cookie", AUTH_COOKIE);
        final ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), clazz);
        return responseEntity.getBody();
    }

}
