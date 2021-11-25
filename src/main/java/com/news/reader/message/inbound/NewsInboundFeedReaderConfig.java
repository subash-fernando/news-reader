package com.news.reader.message.inbound;

import com.news.reader.message.handler.InboundNewsContextHandler;
import com.news.reader.transformer.NewsDataContextTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.feed.dsl.Feed;

@Slf4j
@Configuration
public class NewsInboundFeedReaderConfig {

    @Value("${news.feed.reading.period:300000}")
    private int newsFeedReadingPeriod;

    @Value("${news.feed.reading.max.messages.per.poll:10}")
    private int maxMessagePerPoll;

    @Value("${news.feed.url:http://feeds.nos.nl/nosjournaal?format=xml}")
    private Resource feedResource;

    @Autowired
    private InboundNewsContextHandler inboundNewsContextHandler;

    @Autowired
    private NewsDataContextTransformer newsDataContextTransformer;

    @Bean
    public IntegrationFlow inboundNewsFeedFlow() {
        log.debug("Initialising inbound message channel");
        return IntegrationFlows
                .from(Feed.inboundAdapter(this.feedResource, "feedFlow"),
                        pollingChannelAdapter -> pollingChannelAdapter.poller(poller ->
                                poller.fixedDelay(newsFeedReadingPeriod).maxMessagesPerPoll(maxMessagePerPoll)
                        ))
                .transform(newsDataContextTransformer)
                .handle(inboundNewsContextHandler)
                .get();
    }
}
