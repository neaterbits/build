package com.neaterbits.build.buildsystem.common.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

final class JDKDownloader implements HTTPDownloader {

    @Override
    public InputStream download(URL url) throws IOException {
        
        return url.openStream();
    }
}
