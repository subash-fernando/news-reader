package com.news.reader.message.data;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ImageDataContext {
    private String imageUrl;

    private String imageName;
}
