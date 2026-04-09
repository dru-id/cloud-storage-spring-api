package com.github.sevtech.cloud.storage.spring.service.local;

import com.amazonaws.util.IOUtils;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.github.sevtech.cloud.storage.spring.exception.NoBucketException;
import com.github.sevtech.cloud.storage.spring.model.*;
import com.github.sevtech.cloud.storage.spring.service.AbstractStorageService;
import com.github.sevtech.cloud.storage.spring.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Future;

@Slf4j
@RequiredArgsConstructor
public class LocalStorageService extends AbstractStorageService implements StorageService {

    @Value("${storage.local.base}")
    private File base;

    @Override
    public UploadFileResponse uploadFile(final UploadFileRequest request) {
        try {
            final String targetFilePath = getFilePath(request);

            log.info("Uploading file to {}/{}", base.getAbsolutePath(), targetFilePath);

            final File targetFile = new File(base, targetFilePath);

            FileUtils.copyInputStreamToFile(request.getStream(), targetFile);

            return UploadFileResponse.builder().fileName(request.getName()).status(HttpStatus.SC_OK).comment(targetFile.getAbsolutePath()).build();
        } catch (final IOException e) {
            log.warn("Error creating creating file: {}", e.getMessage());
            return UploadFileResponse.builder().fileName(request.getName()).status(HttpStatus.SC_INTERNAL_SERVER_ERROR).cause("Error creating file").exception(e).build();
        }
    }

    @Async
    @Override
    public Future<UploadFileResponse> uploadFileAsync(final UploadFileRequest request) {
        return new AsyncResult<>(uploadFile(request));
    }

    @Override
    public GetFileResponse getFile(final GetFileRequest request) {
        final File targetFile = new File(base, request.getPath());

        log.info("Reading file from {}", targetFile.getAbsolutePath());
        try {

            return GetFileResponse.builder().content(FileUtils.readFileToByteArray(targetFile)).status(HttpStatus.SC_OK).build();
        } catch (final IOException e) {
            log.error(e.getMessage(), e);
            return GetFileResponse.builder().cause(e.getMessage()).exception(e).status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public DeleteFileResponse deleteFile(final DeleteFileRequest request) {

        final File targetFile = new File(base, request.getPath());

        log.info("Deleting file from path {}", targetFile.getAbsolutePath());
        try {

            FileUtils.delete(targetFile);

            return DeleteFileResponse.builder().status(HttpStatus.SC_OK).build();
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            return DeleteFileResponse.builder().cause(e.getMessage()).exception(e).status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
    }

}
