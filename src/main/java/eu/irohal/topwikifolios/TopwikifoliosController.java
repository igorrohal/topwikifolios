package eu.irohal.topwikifolios;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class TopwikifoliosController {

    @Autowired
    private WikifolioParser wikifolioParser;

    @RequestMapping(value = "/csv", produces = "text/csv")
    public String csv() {
        return wikifolioParser.getLatestCsvContents();
    }

}