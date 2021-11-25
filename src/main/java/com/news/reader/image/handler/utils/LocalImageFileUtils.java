package com.news.reader.image.handler.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

@Slf4j
public class LocalImageFileUtils {

    @Value("${download.file.directory}")
    private String fileDownloadedLocation;
    @Value("${download.file.max.size.mb:4}")
    private Integer maxFileSize;

    public void downloadFile(URL url, String fileLocation) {
        log.debug("file downloading url : {} ", url);
        String fileAbsolutePath = fileDownloadedLocation + File.separator + fileLocation;
        File outPutImage = new File(fileAbsolutePath);

        if (!outPutImage.getParentFile().exists()) {
            log.info("creating directory for file saving path : {}, ", outPutImage.getParentFile());
            outPutImage.getParentFile().mkdir();
        }

        try (ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
             FileOutputStream fileOutStream = new FileOutputStream(outPutImage)) {

            log.debug("File saving to path {} :", fileAbsolutePath);
            fileOutStream.getChannel().transferFrom(readableByteChannel, 0, maxFileSize * 1024 * 1024);

        } catch (IOException iOException) {
            log.debug("error in file saving path : {} ", fileAbsolutePath);
            throw new RuntimeException(iOException);
        }
    }
}
