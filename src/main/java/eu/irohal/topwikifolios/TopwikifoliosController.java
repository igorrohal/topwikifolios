package eu.irohal.topwikifolios;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;

@RestController
public class TopwikifoliosController {

    @Autowired
    private TopRankedFetcher topRankedFetcher;

    @Autowired
    private TopPerformanceFetcher topPerformanceFetcher;

    @RequestMapping(value = "/rank", produces = "text/plain; charset=UTF-8")
    public String topRanked(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        return topRankedFetcher.getLatest();
    }

    @RequestMapping(value = "/performance", produces = "text/plain; charset=UTF-8")
    public String topPerformance(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        return topPerformanceFetcher.getLatest();
    }

}