package com.news.reader.resolvers;

import com.news.reader.model.NewsItem;
import com.news.reader.repository.NewsItemRepository;
import com.news.reader.domain.News;
import com.news.reader.transformer.TransformerUtil;
import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class Subscription implements GraphQLSubscriptionResolver {

    @Autowired
    private NewsItemRepository newsItemRepository;

    public Publisher<List<News>> news() {
        return subscriber -> Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            List<NewsItem> newsItemList = (List<NewsItem>) newsItemRepository.findAll();
            List<News> newsList = TransformerUtil.transformToNewsList(newsItemList);
            subscriber.onNext(newsList);
        }, 0, 30, TimeUnit.SECONDS);
    }
}
