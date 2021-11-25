package com.news.reader.scheduler;


import com.news.reader.image.handler.ImageHandler;
import com.news.reader.model.DownloaderStatus;
import com.news.reader.model.ImageHandlerStatus;
import com.news.reader.model.ImageItem;
import com.news.reader.repository.ImageHandlerStatusRepository;
import com.news.reader.repository.ImageItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static com.news.reader.model.DownloaderStatus.PENDING;
import static com.news.reader.model.DownloaderStatus.RETRY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageDownloadSchedulerTest {

    @Mock
    private ImageHandler imageHandler;

    @Mock
    private ImageItemRepository imageItemRepository;

    @Mock
    private ImageHandlerStatusRepository imageHandlerStatusRepository;

    @InjectMocks
    private final ImageDownloadScheduler imageDownloadScheduler = new ImageDownloadScheduler();

    @Test
    public void testImageDownloadScheduler() {

        Page<ImageHandlerStatus> pendingStatusPage = getImageHandlerStatusList(10);
        pendingStatusPage.toList().get(5).setStatus(DownloaderStatus.SUCCESS);

        when(imageHandlerStatusRepository.
                findAllByStatus(eq(PENDING), any(Pageable.class))).thenReturn(pendingStatusPage);
        when(imageHandlerStatusRepository.
                findAllByStatus(eq(RETRY), any(Pageable.class))).thenReturn(pendingStatusPage);
        imageDownloadScheduler.execute();

        verify(imageHandlerStatusRepository, times(1)).findAllByStatus(PENDING, Pageable.ofSize(10));

        verify(imageHandler, times(20)).handleFile(any(ImageHandlerStatus.class));
        verify(imageHandlerStatusRepository, times(20)).save(any(ImageHandlerStatus.class));

        verify(imageItemRepository, times(2)).save(any(ImageItem.class));
    }

    private Page<ImageHandlerStatus> getImageHandlerStatusList(int numberOfItems) {
        List<ImageHandlerStatus> imageHandlerStatuses = new ArrayList<>();

        for (int i = 0; i < numberOfItems; i++) {
            imageHandlerStatuses.add(ImageHandlerStatus.builder().status(DownloaderStatus.PENDING).build());
        }
        return new PageImpl<>(imageHandlerStatuses);
    }

}
