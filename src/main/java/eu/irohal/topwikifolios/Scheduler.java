package eu.irohal.topwikifolios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);

    @Autowired
    private WikifolioParser wikifolioParser;

    @Scheduled(cron = "0 * * * * ?") // every minute
    public void fetchWikifolios() {
        log.trace("Scheduler: Fetch wikifolios");

        wikifolioParser.fetchWikifolios();
    }

}
