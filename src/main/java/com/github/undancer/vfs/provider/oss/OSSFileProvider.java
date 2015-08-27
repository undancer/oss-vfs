package com.github.undancer.vfs.provider.oss;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.ServiceCredentials;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.AbstractOriginatingFileProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by undancer on 15/8/12.
 */
public class OSSFileProvider extends AbstractOriginatingFileProvider /*extends AbstractFileProvider*/ {

    public static final Collection<Capability> capabilities = Collections.unmodifiableCollection(
            Arrays.asList(new Capability[]{
//                    Capability.GET_TYPE,
//                    Capability.READ_CONTENT,
//                    Capability.URI,
//                    Capability.GET_LAST_MODIFIED,
//                    Capability.ATTRIBUTES,
//                    Capability.RANDOM_ACCESS_READ,
//                    Capability.DIRECTORY_READ_CONTENT,
                    Capability.LIST_CHILDREN,
            }));

    private static FileSystemOptions defaultFileSystemOptions = new FileSystemOptions();

//    public static final UserAuthenticationData.Type[] AUTHENTICATOR_TYPES = new UserAuthenticationData.Type[]
//            {
//                    UserAuthenticationData.USERNAME, UserAuthenticationData.PASSWORD
//            };


    public OSSFileProvider() {
        super();
        setFileNameParser(OSSFileNameParser.getInstance());
    }

    public static FileSystemOptions getDefaultFileSystemOptions() {
        return defaultFileSystemOptions;
    }


    protected FileSystem doCreateFileSystem(FileName name, FileSystemOptions options) throws FileSystemException {
        FileSystemOptions fsOptions = options != null ? options : getDefaultFileSystemOptions();
        OSSFileSystemConfigBuilder config = OSSFileSystemConfigBuilder.getInstance();
        OSSClient client = config.getOSSClient(fsOptions);
        if (client == null) {
            ServiceCredentials credentials = config.getServiceCredentials(fsOptions);

            String endpoint = config.getEndpoint();
            String accessKeyId = credentials.getAccessKeyId();
            String accessKeySecret = credentials.getAccessKeySecret();

            ClientConfiguration clientConfiguration = config.getClientConfiguration(fsOptions);

            if (clientConfiguration == null) {
                if (endpoint == null) {
                    client = new OSSClient(accessKeyId, accessKeySecret);
                } else {
                    client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
                }
            } else {
                client = new OSSClient(endpoint, accessKeyId, accessKeySecret, clientConfiguration);
            }

        }

        OSSFileSystem fileSystem = new OSSFileSystem((OSSFileName) name, client, fsOptions);

        if (config.getOSSClient(fsOptions) == null) {
//            fileSystem.setShutdownServiceOnClose(true);
        }
        return fileSystem;
    }

    public Collection<Capability> getCapabilities() {
        return capabilities;
    }

    public FileSystemConfigBuilder getConfigBuilder() {
        return OSSFileSystemConfigBuilder.getInstance();
    }

}
