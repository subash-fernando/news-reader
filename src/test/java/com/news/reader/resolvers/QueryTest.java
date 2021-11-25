package com.news.reader.resolvers;

import com.news.reader.model.NewsItem;
import com.news.reader.domain.News;
import com.news.reader.error.message.ErrorMessages;
import com.news.reader.repository.NewsItemRepository;
import graphql.GraphQLException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QueryTest {

    @Mock
    private NewsItemRepository newsItemRepository;

    @InjectMocks
    private Query query = new Query();

    private final Date publishedDate = new Date();
    private final String location = "/tmp/location/fileName.jpg";

    private final String description = "Description";
    private final String title = "test title";
    private final Long newsId = 1L;

    @Test
    public void testNewsWhenNewsExistForId() {

        NewsItem newsItem = composeNewsItem();
        Optional<NewsItem> newsItemOptional = Optional.of(newsItem);
        when(newsItemRepository.findById(newsId)).thenReturn(newsItemOptional);
        News news = query.news(newsId);

        assertEquals(newsId, news.getNewsItemId());
        assertEquals(description, news.getDescription());
        assertEquals(title, news.getTitle());
        assertEquals(publishedDate, news.getPublishedDate());
    }

    @Test
    public void testNewsWhenNewsNotExistForId() {

        Optional<NewsItem> newsItemOptional = Optional.empty();
        when(newsItemRepository.findById(newsId)).thenReturn(newsItemOptional);
        try {
            query.news(newsId);

        } catch (GraphQLException exception) {
            assertEquals(ErrorMessages.NO_NEWS_FOR_GIVEN_ID.message(), exception.getMessage());
        }
    }


    @Test
    public void testNewsByKeyWord() {

        NewsItem newsItem = composeNewsItem();
        List<NewsItem> newsItemList = new ArrayList<>();
        newsItemList.add(newsItem);
        String keyWord = "keyWord";

        when(newsItemRepository.findByTitleContainingIgnoreCase(keyWord)).thenReturn(newsItemList);

        List<News> newsList = query.newsByKeyWord(keyWord);
        assertEquals(newsId, newsList.get(0).getNewsItemId());
        assertEquals(publishedDate, newsList.get(0).getPublishedDate());
        assertEquals(description, newsList.get(0).getDescription());
        assertEquals(title, newsList.get(0).getTitle());
    }

    private NewsItem composeNewsItem() {
        Long imageId = 1L;
        return NewsItem.builder()
                .publishedDate(publishedDate).description(description)
                .title(title).newsItemId(imageId).link("testLink").build();
    }
}
