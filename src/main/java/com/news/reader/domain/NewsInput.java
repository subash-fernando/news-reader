package com.news.reader.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Builder
public class NewsInput {

    private String title;

    private Date publishedDate;

    private String description;

}
