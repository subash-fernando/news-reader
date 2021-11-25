package com.news.reader.resolvers;

import com.news.reader.model.ImageItem;
import com.news.reader.model.NewsItem;
import com.news.reader.transformer.TransformerUtil;
import com.news.reader.domain.Image;
import com.news.reader.domain.News;
import com.news.reader.repository.ImageItemRepository;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImageResolver implements GraphQLResolver<News> {

    @Autowired
    private ImageItemRepository imageItemRepository;

    public List<Image> images(News news) {
        NewsItem newsItem = TransformerUtil.transformToNewsItem(news);
        List<ImageItem> imageItemList = imageItemRepository.findAllByNewsItem(newsItem);
        return TransformerUtil.transformToImageList(imageItemList);
    }
}
