package com.news.reader.image.handler.utils;

import com.news.reader.image.handler.impl.LocalDirectoryImageHandler;
import com.news.reader.model.DownloaderStatus;
import com.news.reader.model.ImageHandlerStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ImageDownLoadToFileDirHandlerTest {

    @Mock
    private LocalImageFileUtils localImageFileUtils;

    private LocalDirectoryImageHandler localDirectoryImageDownLoadToFileDirHandler;

    @Before
    public void setUp() {
        localDirectoryImageDownLoadToFileDirHandler = new LocalDirectoryImageHandler(localImageFileUtils, 4);
    }

    @Test
    public void testHandleFileWhenValidUrl() {

        ImageHandlerStatus imageHandlerStatus = getImageHandlerStatus("https://valid.url.path.jpg");
        localDirectoryImageDownLoadToFileDirHandler.handleFile(imageHandlerStatus);
        verify(localImageFileUtils, atMost(1))
                .downloadFile(any(URL.class), Mockito.anyString());
        assertEquals(DownloaderStatus.SUCCESS, imageHandlerStatus.getStatus());
    }

    @Test
    public void testHandleFileWhenExceptionThrows() {

        ImageHandlerStatus imageHandlerStatus = getImageHandlerStatus("InvalidUrl");
        localDirectoryImageDownLoadToFileDirHandler.handleFile(imageHandlerStatus);
        verify(localImageFileUtils, never()).downloadFile(any(URL.class), Mockito.anyString());
        assertEquals(DownloaderStatus.RETRY, imageHandlerStatus.getStatus());
    }

    private ImageHandlerStatus getImageHandlerStatus(String url) {
        return ImageHandlerStatus.builder()
                .imageRemoteUrl(url)
                .retryCount(0).status(DownloaderStatus.PENDING).build();
    }
}
