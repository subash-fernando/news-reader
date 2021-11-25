package com.news.reader.message.handler;

import com.news.reader.message.data.ImageDataContext;
import com.news.reader.model.NewsItem;
import com.news.reader.repository.NewsItemRepository;
import com.news.reader.message.data.NewsDataContext;
import com.news.reader.model.DownloaderStatus;
import com.news.reader.model.ImageHandlerStatus;
import com.news.reader.repository.ImageHandlerStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class InboundNewsContextHandler {

    @Autowired
    private NewsItemRepository newsItemRepository;

    @Autowired
    private ImageHandlerStatusRepository imageHandlerStatusRepository;

    public void handleMessage(NewsDataContext newsDataContext) {
        NewsItem newsItem = saveOrUpdate(newsDataContext);
        saveImageHandlerStatusList(newsItem, newsDataContext.getImageDetails());
    }

    private void saveImageHandlerStatusList(NewsItem newsItem, List<ImageDataContext> imageDataContextList) {
        List<ImageHandlerStatus> imageHandlerStatuses = new ArrayList<>();

        imageDataContextList.forEach(imageDataContext -> {
            ImageHandlerStatus imageHandlerStatus = composeImageHandlerStatus(newsItem, imageDataContext);
            imageHandlerStatuses.add(imageHandlerStatus);
        });

        if (!imageDataContextList.isEmpty()) {
            imageHandlerStatusRepository.saveAll(imageHandlerStatuses);
        }
    }

    private ImageHandlerStatus composeImageHandlerStatus(NewsItem newsItem, ImageDataContext imageDataContext) {
        return ImageHandlerStatus.builder().newsItem(newsItem)
                .status(DownloaderStatus.PENDING).imageRemoteUrl(imageDataContext.getImageUrl())
                .retryCount(0).build();
    }

    private NewsItem saveOrUpdate(NewsDataContext newsDataContext) {
        NewsItem newsItemToSave;
        if (newsDataContext.isUpdated()) {
            List<NewsItem> newsItemList = newsItemRepository.findAllByLink(newsDataContext.getLink());
            if (!newsItemList.isEmpty()) {
                newsItemToSave = newsItemList.get(0);
                mergeUpdate(newsItemToSave, newsDataContext);
            } else {
                newsItemToSave = composeNewsItem(newsDataContext);
            }
        } else {
            newsItemToSave = composeNewsItem(newsDataContext);
        }
        return newsItemRepository.save(newsItemToSave);
    }

    private NewsItem composeNewsItem(NewsDataContext newsDataContext) {
        return NewsItem.builder()
                .description(newsDataContext.getDescription())
                .publishedDate(newsDataContext.getPublishedDate())
                .title(newsDataContext.getTitle()).link(newsDataContext.getLink()).build();
    }

    private void mergeUpdate(NewsItem existingItem, NewsDataContext newsDataContext) {
        existingItem.setTitle(newsDataContext.getTitle());
        existingItem.setDescription(newsDataContext.getDescription());
    }
}
