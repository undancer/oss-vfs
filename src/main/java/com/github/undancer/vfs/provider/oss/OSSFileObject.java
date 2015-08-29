package com.github.undancer.vfs.provider.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.provider.AbstractFileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileSystemException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;

/**
 * Created by undancer on 15/8/12.
 */
public class OSSFileObject extends AbstractFileObject {

    private static final Logger logger = LoggerFactory.getLogger(OSSFileObject.class);

    private OSSObject object;

    private String bucketName;

    private String delimiter = "/";
    private FileType fileType;

    private boolean downloaded = false;

    protected OSSFileObject(OSSFileName name, OSSFileSystem fs) {
        super(name, fs);
        this.bucketName = ((OSSFileName) getName()).getBucketName();
    }

    protected OSS getClient() {
        return ((OSSFileSystem) getFileSystem()).getClient();
    }

    protected FileType doGetType() throws Exception {
        if (fileType == null) {
            try {
                ObjectListing listing = getClient().listObjects(
                        new ListObjectsRequest(bucketName, getPrefix(getKey()), null, delimiter, 1)
                );
                if (listing.getCommonPrefixes().size() > 0 || listing.getObjectSummaries().size() > 0) {
                    fileType = FileType.FOLDER;
                }
            } catch (OSSException e) {
                this.fileType = FileType.IMAGINARY;
            }
        }
        if (fileType == null) {
            try {
                ObjectMetadata metadata = getClient().getObjectMetadata(bucketName, getKey());
                if (metadata != null) {
                    this.fileType = FileType.FILE;
                }
            } catch (OSSException e) {
                this.fileType = FileType.IMAGINARY;
            }
        }
        if (fileType == null) {
            this.fileType = FileType.IMAGINARY;
        }
        return fileType;
    }

    private String getPrefix(String prefix) {
        if (StringUtils.isNotBlank(prefix)) {
            if (!StringUtils.endsWith(prefix, delimiter)) {
                return StringUtils.join(prefix, delimiter);
            }
        }
        return prefix;
    }

    protected void doAttach() throws Exception {
//        logger.error("doAttach");
    }

    protected String[] doListChildren() throws Exception {
        Set<String> children = Sets.newLinkedHashSet();

        String prefix = getPrefix(getKey());
        String marker = null;

        do {
            ListObjectsRequest request = new ListObjectsRequest(bucketName, prefix, marker, delimiter, 100);
            System.out.println(ToStringBuilder.reflectionToString(request));
            ObjectListing objectListing = getClient().listObjects(request);
            for (String commonPrefix : objectListing.getCommonPrefixes()) {
                String key = StringUtils.removeStart(commonPrefix, prefix);
                if (StringUtils.isNoneBlank(key)) {
                    children.add(key);
                }
            }
            for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                String key = StringUtils.removeStart(objectSummary.getKey(), prefix);
                if (StringUtils.isNoneBlank(key)) {
                    children.add(key);
                }
            }
            marker = objectListing.getNextMarker();
        } while (marker != null);

        return children.toArray(new String[]{});
    }

    protected long doGetContentSize() throws Exception {
        final ObjectMetadata metadata = getClient().getObjectMetadata(bucketName, this.getKey());
        return metadata.getContentLength();
    }

    private String getKey() {
        String key = ((OSSFileName) getName()).getKey();
        return StringUtils.startsWith(key, "/") ? StringUtils.removeStart(key, "/") : key;
    }

    protected void downloadOnce() throws FileSystemException {

        if (!this.downloaded) {
            final String failedMessage = "Failed to download OSS Object %s. %s";
            final String objectPath = getName().getPath();

            try {
                GetObjectRequest request = new GetObjectRequest(bucketName, getKey());
                object = getClient().getObject(request);
//                LOGGER.info("downloading oss object : {}", objectPath);
                ObjectMetadata metadata = object.getObjectMetadata();
                InputStream inputStream = object.getObjectContent();

                if (metadata.getContentLength() > 0) {
                    ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
                    FileChannel cacheFileChannel = getCacheFileChannel();
                    cacheFileChannel.transferFrom(readableByteChannel, 0, metadata.getContentLength());
                    IOUtils.closeQuietly(cacheFileChannel);
                    IOUtils.closeQuietly(readableByteChannel);
//                    Files.setLastModifiedTime(null, FileTime.fromMillis(metadata.getLastModified()));
                } else {
                    IOUtils.closeQuietly(inputStream);
                }
            } catch (IOException e) {
                throw new FileSystemException(String.format(failedMessage, new Object[]{objectPath, e.getMessage()}));
            }
//            this.downloaded = true;
        }
    }

    protected FileChannel getCacheFileChannel() throws IOException {
        OSSFileName name = (OSSFileName) this.getName();
        Path path = name.getTempFile();
        return FileChannel.open(path
                , StandardOpenOption.CREATE
                , StandardOpenOption.READ
                , StandardOpenOption.WRITE
                , StandardOpenOption.DELETE_ON_CLOSE
        );
    }


    protected InputStream doGetInputStream() throws Exception {
        downloadOnce();
//        OSSFileCache cache = new OSSFileCache(this);
//        if (cache.isValid()) {
//
//        }
        return Channels.newInputStream(getCacheFileChannel());
    }


}
