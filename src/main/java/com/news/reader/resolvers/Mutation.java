package com.news.reader.resolvers;

import com.news.reader.error.message.ErrorMessages;
import com.news.reader.model.NewsItem;
import com.news.reader.repository.NewsItemRepository;
import com.news.reader.domain.News;
import com.news.reader.domain.NewsInput;
import com.news.reader.transformer.TransformerUtil;
import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Mutation implements GraphQLMutationResolver {

    @Autowired
    public NewsItemRepository newsItemRepository;

    public News createNewsItem(NewsInput newsInput) {
        validateNewsInPut(newsInput);
        NewsItem newsItem = TransformerUtil.transformToNewsItem(newsInput);
        newsItem = newsItemRepository.save(newsItem);
        return TransformerUtil.transformToNews(newsItem);
    }

    public News updateNewsItem(NewsInput newsInput, Long newsId) {
        validateNewsInPut(newsInput);

        Optional<NewsItem> newsItemOptional = newsItemRepository.findById(newsId);
        if (newsItemOptional.isPresent()) {
            NewsItem existingNewsItem = newsItemOptional.get();
            updateFields(existingNewsItem, newsInput);
            existingNewsItem = newsItemRepository.save(existingNewsItem);
            return TransformerUtil.transformToNews(existingNewsItem);
        }
        throw new GraphQLException(ErrorMessages.NO_NEWS_FOR_GIVEN_ID.message());

    }

    private void updateFields(NewsItem existingNewsItem, NewsInput newsInput) {
        existingNewsItem.setDescription(newsInput.getDescription());
        existingNewsItem.setTitle(newsInput.getTitle());
        existingNewsItem.setPublishedDate(newsInput.getPublishedDate());
    }

    private void validateNewsInPut(NewsInput newsInput) {

        if (newsInput.getTitle() == null || newsInput.getTitle().isEmpty()) {
            throw new GraphQLException(ErrorMessages.INVALID_NEWS_TITLE.message());
        }

        if (newsInput.getDescription() == null || newsInput.getDescription().isEmpty()) {
            throw new GraphQLException(ErrorMessages.INVALID_DESCRIPTION.message());
        }

        if (newsInput.getPublishedDate() == null) {
            throw new GraphQLException(ErrorMessages.INVALID_PUBLISHED_DATE.message());
        }
    }

}
