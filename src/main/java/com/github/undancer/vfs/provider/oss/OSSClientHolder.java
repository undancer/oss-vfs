package com.github.undancer.vfs.provider.oss;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.UserAuthenticationData;
import org.apache.commons.vfs2.util.UserAuthenticatorUtils;
import org.codehaus.plexus.util.StringUtils;

/**
 * Created by undancer on 15/8/12.
 */
public class OSSClientHolder {

    private static OSSClient client;

    public static OSSClient getClient() {
        return client;
    }

    public static void setClient(OSSClient client) {
        OSSClientHolder.client = client;
    }

    public static void init(FileSystemOptions options) {
        if (client == null) {
            UserAuthenticationData authData = null;
            try {
                authData = UserAuthenticatorUtils.authenticate(options, OSSFileProvider.AUTHENTICATOR_TYPES);

                String endpoint = OSSFileSystemConfigBuilder.getInstance().getEndpoint();
                String accessKeyId = OSSFileSystemConfigBuilder.getInstance().getAccessKeyId();
                String accessKeySecret = OSSFileSystemConfigBuilder.getInstance().getAccessKeySecret();
                ClientConfiguration config = OSSFileSystemConfigBuilder.getInstance().getConfig();

                if (StringUtils.isBlank(endpoint)) {
                    endpoint = "http://oss.aliyuncs.com/";
                }

                client = new OSSClient(endpoint, accessKeyId, accessKeySecret, config);
            } finally {
                UserAuthenticatorUtils.cleanup(authData);
            }
        }
    }
}
