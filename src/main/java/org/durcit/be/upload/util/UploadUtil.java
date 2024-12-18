package org.durcit.be.upload.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.durcit.be.system.exception.upload.FileSizeExccedsMaximumLimitException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.durcit.be.system.exception.ExceptionMessage.FILE_SIZE_EXCEED_MAXIMUM_LIMIT_ERROR;

@Component
public class UploadUtil {

    private static long maxFileSize;

    @Value("${custom.s3.max-file-size}")
    public void setMaxFileSize(long maxFileSize) {
        UploadUtil.maxFileSize = maxFileSize; // 정적 변수에 주입
    }


    public static void validateFileSize(MultipartFile file) {
        if (file.getSize() > maxFileSize) {
            throw new FileSizeExccedsMaximumLimitException(FILE_SIZE_EXCEED_MAXIMUM_LIMIT_ERROR);
        }
    }

    public static String extractFileKeyFromUrl(String s3Url) {
        return s3Url.substring(s3Url.lastIndexOf("/") + 1);
    }

    public static String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }

}
