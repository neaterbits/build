package com.neaterbits.build.buildsystem.common.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public interface HTTPDownloader {

    public static HTTPDownloader create() {
        return new JDKDownloader();
    }
    
    InputStream download(URL url) throws IOException;
}
