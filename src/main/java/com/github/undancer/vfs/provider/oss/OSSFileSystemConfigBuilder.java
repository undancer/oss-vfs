package com.github.undancer.vfs.provider.oss;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.ServiceCredentials;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemConfigBuilder;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.UserAuthenticationData;
import org.apache.commons.vfs2.util.UserAuthenticatorUtils;

import static org.apache.commons.vfs2.UserAuthenticationData.PASSWORD;
import static org.apache.commons.vfs2.UserAuthenticationData.USERNAME;
import static org.apache.commons.vfs2.util.UserAuthenticatorUtils.getData;

/**
 * Created by undancer on 15/8/12.
 */
public class OSSFileSystemConfigBuilder extends FileSystemConfigBuilder {

    private static final String ENDPOINT = OSSFileSystemConfigBuilder.class.getName() + ".ENDPOINT";
    private static final String ACCESS_KEY_ID = OSSFileSystemConfigBuilder.class.getName() + ".ACCESS_KEY_ID";
    private static final String ACCESS_KEY_SECRET = OSSFileSystemConfigBuilder.class.getName() + ".ACCESS_KEY_SECRET";
    private static final String CONFIG = OSSFileSystemConfigBuilder.class.getName() + ".CONFIG";
    private static final String OSS_CLIENT = OSSFileSystemConfigBuilder.class.getName() + ".CLIENT";

    private static final OSSFileSystemConfigBuilder INSTANCE = new OSSFileSystemConfigBuilder();

    private final static UserAuthenticationData.Type[] AUTHENTICATOR_TYPES = new UserAuthenticationData.Type[]{
            USERNAME, PASSWORD
    };

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
        return getEndpoint(OSSFileProvider.getDefaultFileSystemOptions());
    }

    public void setEndpoint(String endpoint) {
        setParam(OSSFileProvider.getDefaultFileSystemOptions(), ENDPOINT, endpoint);
    }

    public String getEndpoint(FileSystemOptions options) {
        return getString(options, ENDPOINT);
    }

//    public String getAccessKeyId() {
//        return getString(OSSFileProvider.getDefaultFileSystemOptions(), ACCESS_KEY_ID);
//    }

    public void setAccessKeyId(String accessKeyId) {
        setParam(OSSFileProvider.getDefaultFileSystemOptions(), ACCESS_KEY_ID, accessKeyId);
    }

//    public String getAccessKeySecret() {
//        return getString(OSSFileProvider.getDefaultFileSystemOptions(), ACCESS_KEY_SECRET);
//    }

    public void setAccessKeySecret(String accessKeySecret) {
        setParam(OSSFileProvider.getDefaultFileSystemOptions(), ACCESS_KEY_SECRET, accessKeySecret);
    }

    public ClientConfiguration getConfig() {
        return (ClientConfiguration) getParam(OSSFileProvider.getDefaultFileSystemOptions(), CONFIG);
    }

    public void setConfig(ClientConfiguration config) {
        setParam(OSSFileProvider.getDefaultFileSystemOptions(), CONFIG, config);
    }

    public OSSClient getOSSClient(FileSystemOptions options) {
        return (OSSClient) getParam(options, OSS_CLIENT);
    }

    public ServiceCredentials getServiceCredentials(FileSystemOptions options) {
        UserAuthenticationData authData = null;

        try {

            authData = UserAuthenticatorUtils.authenticate(options, AUTHENTICATOR_TYPES);

            String accessKeyId = UserAuthenticatorUtils.toString(getData(authData, UserAuthenticationData.USERNAME, null));
            String accessKeySecret = UserAuthenticatorUtils.toString(getData(authData, UserAuthenticationData.PASSWORD, null));

            if (StringUtils.isAnyEmpty(accessKeyId, accessKeySecret)) {
                return null;
            }
            return new ServiceCredentials(accessKeyId, accessKeySecret);
        } finally {
            UserAuthenticatorUtils.cleanup(authData);
        }
    }

    public ClientConfiguration getClientConfiguration(FileSystemOptions options) {
        return (ClientConfiguration) getParam(options, CONFIG);
    }
}
