package com.example.kongapiservice.network.request;

import java.io.File;

public class ImageRequest {
    File file;

    public ImageRequest(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
