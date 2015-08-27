package com.github.undancer.vfs.provider.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.Bucket;
import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.AbstractFileName;
import org.apache.commons.vfs2.provider.AbstractFileSystem;

import java.util.Collection;

/**
 * Created by undancer on 15/8/12.
 */
public class OSSFileSystem extends AbstractFileSystem {

    private OSS client;
    private Bucket bucket;

//    private boolean shutdownServiceOnClose = false;

    protected OSSFileSystem(OSSFileName fileName, OSS client, FileSystemOptions fileSystemOptions) throws FileSystemException {
        super(fileName, null, fileSystemOptions);
        this.client = client;
        String bucketName = fileName.getBucketName();
        try {
            if (client.doesBucketExist(bucketName)) {
                bucket = new Bucket(bucketName);
            } else {
                bucket = client.createBucket(bucketName);
            }
        } catch (OSSException | ClientException e) {
            String message = e.getMessage();
            if (message != null) {
                throw new FileSystemException(message, e);
            } else {
                throw new FileSystemException(e);
            }
        }
    }

    protected Bucket getBucket() {
        return bucket;
    }

    protected OSS getClient() {
        return client;
    }

    protected FileObject createFile(AbstractFileName name) throws Exception {
        if (name instanceof OSSFileName) {
            return new OSSFileObject((OSSFileName) name, this);
        }
        return null;
    }

    protected void addCapabilities(Collection<Capability> caps) {
        caps.addAll(OSSFileProvider.capabilities);
    }

    protected void doCloseCommunicationLink() {
        super.doCloseCommunicationLink();
    }
//
//    public void setShutdownServiceOnClose(boolean b) {
//
//    }
}
