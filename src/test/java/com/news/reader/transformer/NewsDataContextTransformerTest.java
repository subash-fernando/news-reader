package com.news.reader.transformer;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEnclosureImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.news.reader.message.data.ImageDataContext;
import com.news.reader.message.data.NewsDataContext;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class NewsDataContextTransformerTest {


    private final NewsDataContextTransformer newsDataContextTransformer = new NewsDataContextTransformer();

    @Test
    public void testTransformPayload() {

        SyndEntry syndEntry = new SyndEntryImpl();
        Date publishedDate = new Date();
        String title = "TEST_TITLE";
        String description = "Test DESCRIPTION";
        String link ="sample_link";

        populateEnclosures(syndEntry, description, title, publishedDate, link);
        NewsDataContext newsDataContext = newsDataContextTransformer.transformPayload(syndEntry);

        assertEquals(newsDataContext.getDescription(), description);
        assertEquals(newsDataContext.getTitle(), title);
        assertEquals(newsDataContext.getPublishedDate(), publishedDate);
        List<String> urlsFromRemoteMessage = syndEntry.getEnclosures().stream().map(SyndEnclosure::getUrl)
                .collect(Collectors.toList());
        List<String> urlsFromDataContext = newsDataContext.getImageDetails().stream().map(ImageDataContext::getImageUrl)
                .collect(Collectors.toList());
        assertEquals(urlsFromRemoteMessage, urlsFromDataContext);
        assertEquals(link, newsDataContext.getLink());

    }

    @NotNull
    private void populateEnclosures(SyndEntry syndEntry, String description, String title, Date publishedDate, String link) {
        SyndEnclosure enclosure1 = new SyndEnclosureImpl();
        enclosure1.setUrl("URL1");
        syndEntry.getEnclosures().add(enclosure1);

        SyndEnclosure enclosure2 = new SyndEnclosureImpl();
        enclosure2.setUrl("URL2");
        syndEntry.getEnclosures().add(enclosure2);

        SyndEnclosure enclosure3 = new SyndEnclosureImpl();
        enclosure3.setUrl("URL3");
        syndEntry.getEnclosures().add(enclosure3);

        SyndContent descriptionContent = new SyndContentImpl();
        descriptionContent.setValue(description);

        syndEntry.setDescription(descriptionContent);
        syndEntry.setTitle(title);
        syndEntry.setPublishedDate(publishedDate);

        syndEntry.setLink(link);
    }
}
