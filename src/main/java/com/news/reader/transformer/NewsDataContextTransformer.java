package com.news.reader.transformer;

import com.news.reader.message.data.ImageDataContext;
import com.news.reader.message.data.NewsDataContext;
import com.rometools.rome.feed.synd.SyndEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.transformer.AbstractPayloadTransformer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class NewsDataContextTransformer extends AbstractPayloadTransformer<SyndEntry, NewsDataContext> {
    @Override
    protected NewsDataContext transformPayload(SyndEntry payload) {
        log.debug("News Syndy transform to data context");
        boolean isUpdated= payload.getUpdatedDate()!= null;
        List<ImageDataContext> imageDataContextList = getImageDataContextList(payload);
        return NewsDataContext.builder().description(payload.getDescription().getValue())
                .publishedDate(payload.getPublishedDate())
                .title(payload.getTitle()).imageDetails(imageDataContextList).link(payload.getLink())
                .isUpdated(isUpdated).build();
    }

    private List<ImageDataContext> getImageDataContextList(SyndEntry payload) {
        List<ImageDataContext> imageDataContextList = new ArrayList<>();
        if (payload.getEnclosures() != null && !payload.getEnclosures().isEmpty()) {

            payload.getEnclosures().forEach(enclosure -> imageDataContextList.add(
                    ImageDataContext.builder()
                            .imageUrl(enclosure.getUrl()).build()
            ));
        }
        return imageDataContextList;
    }
}
