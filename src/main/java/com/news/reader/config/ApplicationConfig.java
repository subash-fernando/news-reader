package com.news.reader.config;

import com.news.reader.image.handler.impl.LocalDirectoryImageHandler;
import com.news.reader.image.handler.utils.LocalImageFileUtils;
import com.news.reader.image.handler.ImageHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Value("${download.file.retry.max.limit:4}")
    private int downloadRetryMaxLimit;


    @Bean
    public LocalImageFileUtils localImageFileUtils() {
        return new LocalImageFileUtils();
    }

    @Bean
    public ImageHandler localImageFileHandler() {
        return new LocalDirectoryImageHandler(localImageFileUtils(), downloadRetryMaxLimit);
    }

}
