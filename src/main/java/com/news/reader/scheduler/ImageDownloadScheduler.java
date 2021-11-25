package com.news.reader.scheduler;

import com.news.reader.model.DownloaderStatus;
import com.news.reader.model.ImageHandlerStatus;
import com.news.reader.model.ImageItem;
import com.news.reader.repository.ImageHandlerStatusRepository;
import com.news.reader.repository.ImageItemRepository;
import com.news.reader.image.handler.ImageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Slf4j
@Configuration
@EnableScheduling
public class ImageDownloadScheduler implements FileDownloadScheduler {

    @Autowired
    private ImageHandler fileHandler;

    @Autowired
    private ImageItemRepository imageItemRepository;

    @Autowired
    private ImageHandlerStatusRepository imageHandlerStatusRepository;

    @Override
    @Scheduled(fixedRateString = "${scheduler.image.downloader.fixed.rate.milliseconds:30000}",
            initialDelayString = "${scheduler.image.downloader.fixed.delay.milliseconds:300000}")

    public void execute() {
        log.info("Image downloader scheduler start");
        downloadImages(DownloaderStatus.PENDING);
        downloadImages(DownloaderStatus.RETRY);
        log.info("Image downloader scheduler completed");
    }

    private void downloadImages(DownloaderStatus status) {
        Page<ImageHandlerStatus> pendingImages = imageHandlerStatusRepository.
                findAllByStatus(status, Pageable.ofSize(10));
        pendingImages.forEach(imageHandlerStatus -> {

            //Calling image Handler to handle image
            fileHandler.handleFile(imageHandlerStatus);
            imageHandlerStatusRepository.save(imageHandlerStatus);

            if (imageHandlerStatus.getStatus() == DownloaderStatus.SUCCESS) {
                ImageItem imageItem = composeImageItem(imageHandlerStatus);
                imageItemRepository.save(imageItem);
            }
        });
    }

    private ImageItem composeImageItem(ImageHandlerStatus imageHandlerStatus) {
        return ImageItem.builder().location(imageHandlerStatus.getImageLocation())
                .newsItem(imageHandlerStatus.getNewsItem()).uploadedDate(new Date()).build();
    }
}
