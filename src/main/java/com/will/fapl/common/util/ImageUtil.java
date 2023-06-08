package com.will.fapl.common.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageUtil {

    private static final int TIMEOUT_MILLI_SECOND = 500;
    private static final String IMAGE_MIMETYPE = "image";
    private static Tika tika = new Tika();

    public static boolean isImageUrl(String value) {
        try {
            URL url = new URL(value);
            validateConnectUrl(url);
            String mimeType = tika.detect(url);
            return mimeType.startsWith(IMAGE_MIMETYPE);
        } catch (IOException e) {
            log.warn("----------");
            log.warn(e.getMessage(), e);
            log.warn("----------");
            return false;
        }
    }

    private static void validateConnectUrl(URL url) throws IOException {
        URLConnection urlConnection = url.openConnection();
        urlConnection.setConnectTimeout(TIMEOUT_MILLI_SECOND);
        urlConnection.connect();
    }
}
