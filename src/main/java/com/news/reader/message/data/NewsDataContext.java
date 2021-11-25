package com.news.reader.message.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class NewsDataContext {
    private String title;

    private String description;

    private Date publishedDate;

    private List<ImageDataContext> imageDetails;

    private String link;

    private boolean isUpdated;
}

