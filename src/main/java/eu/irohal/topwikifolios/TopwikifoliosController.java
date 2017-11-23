package eu.irohal.topwikifolios;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;

@RestController
public class TopwikifoliosController {

    @Autowired
    private WikifolioParser wikifolioParser;

    @RequestMapping(value = "/", produces = "text/plain; charset=UTF-8")
    public String text(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        return wikifolioParser.getLatestCsvContents();
    }

    @RequestMapping(value = "/csv", produces = "text/csv; charset=UTF-8")
    public String csv(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        return wikifolioParser.getLatestCsvContents();
    }

}