package com.news.reader.resolvers;

import com.news.reader.model.NewsItem;
import com.news.reader.domain.News;
import com.news.reader.domain.NewsInput;
import com.news.reader.error.message.ErrorMessages;
import com.news.reader.repository.NewsItemRepository;
import graphql.GraphQLException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MutationTest {

    @Mock
    public NewsItemRepository newsItemRepository;

    @InjectMocks
    private Mutation mutation = new Mutation();

    private final Date publishedDate = new Date();
    private final String description = "Description";
    private final String title = "test title";
    private final Long newsId = 1L;

    @Test
    public void testCreateNewsItemInvalidTitle() {
        NewsInput newsInput = composeNewsInput();

        //test when title is empty
        newsInput.setTitle("");
        try {
            mutation.createNewsItem(newsInput);
        } catch (GraphQLException exception) {
            assertEquals(ErrorMessages.INVALID_NEWS_TITLE.message(), exception.getMessage());
        }

        //test when title is null
        newsInput.setTitle(null);

        try {
            mutation.createNewsItem(newsInput);
        } catch (GraphQLException exception) {
            assertEquals(ErrorMessages.INVALID_NEWS_TITLE.message(), exception.getMessage());
        }
    }

    @Test
    public void testCreateNewsItemInvalidDescription() {
        NewsInput newsInput = composeNewsInput();

        //test when description is empty
        newsInput.setDescription("");
        try {
            mutation.createNewsItem(newsInput);
        } catch (GraphQLException exception) {
            assertEquals(ErrorMessages.INVALID_DESCRIPTION.message(), exception.getMessage());
        }

        //test when description is null
        newsInput.setDescription(null);

        try {
            mutation.createNewsItem(newsInput);
        } catch (GraphQLException exception) {
            assertEquals(ErrorMessages.INVALID_DESCRIPTION.message(), exception.getMessage());
        }
    }

    @Test
    public void testCreateNewsItemInvalidPublishedDate() {

        NewsInput newsInput = composeNewsInput();

        //test when published date is null
        newsInput.setPublishedDate(null);

        try {
            mutation.createNewsItem(newsInput);
        } catch (GraphQLException exception) {
            assertEquals(ErrorMessages.INVALID_PUBLISHED_DATE.message(), exception.getMessage());
        }
    }

    @Test
    public void testCreateNewsItemValidInputs() {

        NewsInput newsInput = composeNewsInput();
        NewsItem newsItemWithoutId = composeNewsItem(false);
        NewsItem newsItemWithId = composeNewsItem(true);

        when(newsItemRepository.save(newsItemWithoutId)).thenReturn(newsItemWithId);
        News news = mutation.createNewsItem(newsInput);

        verify(newsItemRepository, times(1)).save(newsItemWithoutId);
        assertEquals(newsId, news.getNewsItemId());
        assertEquals(publishedDate, news.getPublishedDate());
        assertEquals(description, news.getDescription());
        assertEquals(title, news.getTitle());
    }

    @Test
    public void testUpdatedNewsItemValidInputs() {
        Date updatedDate = new Date();
        String updatedDescription = "Updated description";
        String updatedTitle = "UpdatedTitle";

        NewsInput updateRequest = NewsInput.builder().description(updatedDescription)
                .publishedDate(updatedDate).title(updatedTitle).build();

        NewsItem updatedNewsItem = NewsItem.builder().title(updatedTitle).description(updatedDescription)
                .publishedDate(updatedDate).newsItemId(newsId).link("testLink").build();

        NewsItem existingNews = composeNewsItem(true);

        when(newsItemRepository.findById(newsId)).thenReturn(Optional.of(existingNews));
        when(newsItemRepository.save(existingNews)).thenReturn(updatedNewsItem);
        News news = mutation.updateNewsItem(updateRequest, newsId);

        verify(newsItemRepository, times(1)).findById(newsId);
        verify(newsItemRepository, times(1)).save(updatedNewsItem);
        assertEquals(newsId, news.getNewsItemId());
        assertEquals(updatedDate, news.getPublishedDate());
        assertEquals(updatedDescription, news.getDescription());
        assertEquals(updatedTitle, news.getTitle());
    }

    @Test
    public void testUpdatedNewsItemWhenItemNotExist() {

        when(newsItemRepository.findById(newsId)).thenReturn(Optional.empty());
        NewsInput input = composeNewsInput();

        try {
            mutation.updateNewsItem(input, 1L);
        } catch (GraphQLException exception) {
            assertEquals(ErrorMessages.NO_NEWS_FOR_GIVEN_ID.message(), exception.getMessage());
            verify(newsItemRepository, times(1)).findById(newsId);
            verify(newsItemRepository, never()).save(any());
        }
    }

    private NewsInput composeNewsInput() {
        return NewsInput.builder().description(description).
                title(title).publishedDate(publishedDate).build();
    }

    private NewsItem composeNewsItem(boolean isNewsIdNull) {
        NewsItem newsItem = NewsItem.builder()
                .publishedDate(publishedDate).description(description)
                .title(title).link("testLink").build();
        if (isNewsIdNull) {
            newsItem.setNewsItemId(newsId);
        }
        return newsItem;
    }

}
