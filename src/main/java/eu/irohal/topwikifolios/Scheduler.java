package eu.irohal.topwikifolios;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

@Component
public class Scheduler {

    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);

    @Autowired
    private TopRankedFetcher topRankedFetcher;

    @Autowired
    private TopPerformanceFetcher topPerformanceFetcher;

    @Scheduled(fixedRate = 1800000) // every half an hour
    public void fetchWikifolios() throws Exception {
        topPerformanceFetcher.fetch();
        topRankedFetcher.fetch();
    }

}
