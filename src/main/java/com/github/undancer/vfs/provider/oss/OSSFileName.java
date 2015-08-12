package com.github.undancer.vfs.provider.oss;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.provider.local.LocalFileName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by undancer on 15/8/12.
 */
public class OSSFileName extends LocalFileName {

    private String bucketName;
    private String key;

    protected OSSFileName(String scheme, String bucketName, String key, FileType type) {
        super(scheme, bucketName, key, type);
        this.bucketName = bucketName;
        this.key = key;
    }

    public Path getTempFile() {
        String checksum = DigestUtils.md5Hex(getURI());
        Path tempPath = Paths.get(FileUtils.getTempDirectoryPath(), "oss");
        if (Files.notExists(tempPath)) {
            try {
                Files.createDirectories(tempPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return tempPath.resolve(checksum);
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getKey() {
        return key;
    }

}
