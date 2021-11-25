package com.news.reader.resolvers;

import com.news.reader.error.message.ErrorMessages;
import com.news.reader.model.NewsItem;
import com.news.reader.repository.NewsItemRepository;
import com.news.reader.domain.News;
import com.news.reader.transformer.TransformerUtil;
import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class Query implements GraphQLQueryResolver {

    @Autowired
    private NewsItemRepository newsItemRepository;

    public News news(Long id) {
        Optional<NewsItem> newsItemOptional = newsItemRepository.findById(id);
        if (newsItemOptional.isPresent()) {
            NewsItem newsItem = newsItemOptional.get();
            return TransformerUtil.transformToNews(newsItem);
        }
        throw new GraphQLException(ErrorMessages.NO_NEWS_FOR_GIVEN_ID.message());
    }

    public List<News> newsByKeyWord(String titleKeyWord) {
        List<NewsItem> newsItemList = newsItemRepository.findByTitleContainingIgnoreCase(titleKeyWord);
        return TransformerUtil.transformToNewsList(newsItemList);
    }
}
