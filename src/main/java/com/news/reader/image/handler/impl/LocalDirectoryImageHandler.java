package com.news.reader.image.handler.impl;

import com.news.reader.error.message.ErrorMessages;
import com.news.reader.image.handler.utils.LocalImageFileUtils;
import com.news.reader.image.handler.ImageHandler;
import com.news.reader.model.DownloaderStatus;
import com.news.reader.model.ImageHandlerStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
public class LocalDirectoryImageHandler implements ImageHandler {

    private final LocalImageFileUtils localImageFileUtils;
    private final int downloadRetryMaxLimit;
    private final DateTimeFormatter dateTimeFormatter;

    public LocalDirectoryImageHandler(LocalImageFileUtils localImageFileUtils,
                                      @Value("${download.file.retry.max.limit:4}") int downloadRetryMaxLimit) {
        this.downloadRetryMaxLimit = downloadRetryMaxLimit;
        this.localImageFileUtils = localImageFileUtils;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    }

    public void handleFile(ImageHandlerStatus imageHandlerStatus) {

        try {
            URL url = new URL(imageHandlerStatus.getImageRemoteUrl());
            String fileName = composeFileLocation(url);
            localImageFileUtils.downloadFile(url, fileName);
            successUpdateDownloaderStatus(imageHandlerStatus, fileName);

        } catch (MalformedURLException | RuntimeException exception) {
            log.error(ErrorMessages.IMAGE_DOWNLOAD_FAILED.message(), exception.getMessage(), exception);
            failureUpdateDownloaderStatus(imageHandlerStatus);
        }
    }

    private void successUpdateDownloaderStatus(ImageHandlerStatus imageHandlerStatus, String fileName) {
        imageHandlerStatus.setImageLocation(fileName);
        imageHandlerStatus.setStatus(DownloaderStatus.SUCCESS);
    }

    private void failureUpdateDownloaderStatus(ImageHandlerStatus imageHandlerStatus) {
        imageHandlerStatus.increaseRetryCount();
        DownloaderStatus downloadedStatus = imageHandlerStatus.getRetryCount() >= downloadRetryMaxLimit ?
                DownloaderStatus.FAILED : DownloaderStatus.RETRY;
        imageHandlerStatus.setStatus(downloadedStatus);
    }

    @NotNull
    private String composeFileLocation(URL url) {
        String fileExtension = "." + FilenameUtils.getExtension(url.getPath());
        String fileParentDir = dateTimeFormatter.format(LocalDateTime.now());
        return fileParentDir + File.separator + UUID.randomUUID() + fileExtension;
    }
}
