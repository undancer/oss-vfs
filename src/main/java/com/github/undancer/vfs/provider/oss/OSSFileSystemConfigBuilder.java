package com.github.undancer.vfs.provider.oss;

import com.aliyun.oss.ClientConfiguration;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemConfigBuilder;

/**
 * Created by undancer on 15/8/12.
 */
public class OSSFileSystemConfigBuilder extends FileSystemConfigBuilder {

    private static final String ENDPOINT = OSSFileSystemConfigBuilder.class.getName() + ".ENDPOINT";
    private static final String ACCESS_KEY_ID = OSSFileSystemConfigBuilder.class.getName() + ".ACCESS_KEY_ID";
    private static final String ACCESS_KEY_SECRET = OSSFileSystemConfigBuilder.class.getName() + ".ACCESS_KEY_SECRET";
    private static final String CONFIG = OSSFileSystemConfigBuilder.class.getName() + ".CONFIG";

    private static final OSSFileSystemConfigBuilder INSTANCE = new OSSFileSystemConfigBuilder();

    private OSSFileSystemConfigBuilder() {
        super("oss.");
    }

    public static OSSFileSystemConfigBuilder getInstance() {
        return INSTANCE;
    }

    protected Class<? extends FileSystem> getConfigClass() {
        return OSSFileSystem.class;
    }

    public String getEndpoint() {
        return getString(OSSFileProvider.getDefaultFileSystemOptions(), ENDPOINT);
    }

    public void setEndpoint(String endpoint) {
        setParam(OSSFileProvider.getDefaultFileSystemOptions(), ENDPOINT, endpoint);
    }

    public String getAccessKeyId() {
        return getString(OSSFileProvider.getDefaultFileSystemOptions(), ACCESS_KEY_ID);
    }

    public void setAccessKeyId(String accessKeyId) {
        setParam(OSSFileProvider.getDefaultFileSystemOptions(), ACCESS_KEY_ID, accessKeyId);
    }

    public String getAccessKeySecret() {
        return getString(OSSFileProvider.getDefaultFileSystemOptions(), ACCESS_KEY_SECRET);
    }

    public void setAccessKeySecret(String accessKeySecret) {
        setParam(OSSFileProvider.getDefaultFileSystemOptions(), ACCESS_KEY_SECRET, accessKeySecret);
    }

    public ClientConfiguration getConfig() {
        return (ClientConfiguration) getParam(OSSFileProvider.getDefaultFileSystemOptions(), CONFIG);
    }

    public void setConfig(ClientConfiguration config) {
        setParam(OSSFileProvider.getDefaultFileSystemOptions(), CONFIG, config);
    }

}
