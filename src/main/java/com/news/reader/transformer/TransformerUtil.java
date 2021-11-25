package com.news.reader.transformer;

import com.news.reader.model.NewsItem;
import com.news.reader.domain.Image;
import com.news.reader.domain.News;
import com.news.reader.domain.NewsInput;
import com.news.reader.model.ImageItem;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class TransformerUtil {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static News transformToNews(NewsItem newsItem) {
        return modelMapper.map(newsItem, News.class);
    }

    public static NewsItem transformToNewsItem(News news) {
        return modelMapper.map(news, NewsItem.class);
    }

    public static NewsItem transformToNewsItem(NewsInput newsInput) {
        return modelMapper.map(newsInput, NewsItem.class);
    }

    public static List<News> transformToNewsList(List<NewsItem> newsItemList) {
        return newsItemList
                .stream()
                .map(newsItem -> modelMapper.map(newsItem, News.class))
                .collect(Collectors.toList());
    }

    public static List<Image> transformToImageList(List<ImageItem> imageItemList) {
        return imageItemList
                .stream()
                .map(imageItem -> modelMapper.map(imageItem, Image.class))
                .collect(Collectors.toList());
    }
}
