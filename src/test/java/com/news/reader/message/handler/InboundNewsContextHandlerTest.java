package com.news.reader.message.handler;


import com.news.reader.model.NewsItem;
import com.news.reader.message.data.ImageDataContext;
import com.news.reader.message.data.NewsDataContext;
import com.news.reader.model.ImageHandlerStatus;
import com.news.reader.repository.ImageHandlerStatusRepository;
import com.news.reader.repository.NewsItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InboundNewsContextHandlerTest {

    @Mock
    private NewsItemRepository newsItemRepository;

    @Mock
    private ImageHandlerStatusRepository imageHandlerStatusRepository;

    @InjectMocks
    private InboundNewsContextHandler imageDownLoadToFileDirHandler = new InboundNewsContextHandler();

    @Test
    public void testHandlerWithImages() {
        imageDownLoadToFileDirHandler.handleMessage(getNewsDataContext(true));
        verify(newsItemRepository, atMost(1)).save(any(NewsItem.class));
        verify(imageHandlerStatusRepository, atMost(3)).save(any(ImageHandlerStatus.class));
    }

    @Test
    public void testHandlerWithOutImages() {
        imageDownLoadToFileDirHandler.handleMessage(getNewsDataContext(false));
        verify(newsItemRepository, atMost(1)).save(any(NewsItem.class));
        verify(imageHandlerStatusRepository, never()).save(any(ImageHandlerStatus.class));
    }

    @Test
    public void testHandlerWhenUpdatedButNotFoundInDB() {
        NewsDataContext newsDataContext = getNewsDataContext(false);
        newsDataContext.setUpdated(true);

        when(newsItemRepository.findAllByLink(anyString())).thenReturn(new ArrayList<>());
        imageDownLoadToFileDirHandler.handleMessage(newsDataContext);
        verify(newsItemRepository, atMost(1)).findAllByLink(anyString());
        verify(newsItemRepository, atMost(1)).save(any(NewsItem.class));
        verify(imageHandlerStatusRepository, never()).save(any(ImageHandlerStatus.class));
    }

    @Test
    public void testHandlerWhenUpdatedAndDoUpdate() {
        NewsDataContext newsDataContext = getNewsDataContext(false);
        newsDataContext.setUpdated(true);

        NewsItem newsItem = NewsItem.builder().description("description").publishedDate(new Date())
                .title("Title").newsItemId(1L).link("testLinl").build();
        List<NewsItem> newsItemList = new ArrayList<>();
        newsItemList.add(newsItem);

        when(newsItemRepository.findAllByLink(anyString())).thenReturn(newsItemList);
        imageDownLoadToFileDirHandler.handleMessage(newsDataContext);
        verify(newsItemRepository, atMost(1)).findAllByLink(anyString());
        verify(newsItemRepository, atMost(1)).save(any(NewsItem.class));
        verify(imageHandlerStatusRepository, never()).save(any(ImageHandlerStatus.class));
    }

    private NewsDataContext getNewsDataContext(boolean includeImages) {
        List<ImageDataContext> imageDataContexts = new ArrayList<>();
        if (includeImages) {
            imageDataContexts.add(ImageDataContext.builder().imageUrl("https://valid.url1.jpg").build());
            imageDataContexts.add(ImageDataContext.builder().imageUrl("https://valid.url2.jpg").build());
            imageDataContexts.add(ImageDataContext.builder().imageUrl("https://valid.url3.jpg").build());
        }
        return NewsDataContext.builder().description("description").title("title").publishedDate(new Date()).
                imageDetails(imageDataContexts).link("test_link").build();
    }
}