package com.news.reader.resolvers;

import com.news.reader.model.NewsItem;
import com.news.reader.domain.Image;
import com.news.reader.domain.News;
import com.news.reader.model.ImageItem;
import com.news.reader.repository.ImageItemRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ImageResolverTest {

    @Mock
    private ImageItemRepository imageItemRepository;

    @InjectMocks
    private ImageResolver imageResolver = new ImageResolver();

    private final Date uploadedDate = new Date();
    private final Long imageId = 1L;
    private final String location = "/tmp/location/fileName.jpg";

    private final String description = "Description";
    private final String title = "test title";

    @Test
    public void testImages() {

        News news = composeNews();
        NewsItem newsItem = composeNewsItem();
        List<ImageItem> imageItemList = composeImageItemList(newsItem);

        when(imageItemRepository.findAllByNewsItem(newsItem)).thenReturn(imageItemList);
        List<Image> imageList = imageResolver.images(news);

        assertEquals(imageId, imageList.get(0).getImageId());
        assertEquals(location, imageList.get(0).getLocation());
    }

    @NotNull
    private List<ImageItem> composeImageItemList(NewsItem newsItem) {
        List<ImageItem> imageItemList = new ArrayList<>();
        imageItemList.add(ImageItem.builder().uploadedDate(uploadedDate).newsItem(newsItem)
                .imageId(imageId).location(location).build());
        return imageItemList;
    }

    private NewsItem composeNewsItem() {
        return NewsItem.builder()
                .publishedDate(uploadedDate).description(description)
                .title(title).newsItemId(imageId).link("testLink").build();
    }

    private News composeNews() {
        Long newsId = 1L;
        return News.builder().newsItemId(newsId).description(description).title(title)
                .publishedDate(uploadedDate).build();
    }
}
