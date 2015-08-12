package com.github.undancer.vfs.provider.oss;

import com.aliyun.oss.OSSClient;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.AbstractOriginatingFileProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by undancer on 15/8/12.
 */
public class OSSFileProvider extends AbstractOriginatingFileProvider /*extends AbstractFileProvider*/ {

    public static final UserAuthenticationData.Type[] AUTHENTICATOR_TYPES = new UserAuthenticationData.Type[]
            {
                    UserAuthenticationData.USERNAME, UserAuthenticationData.PASSWORD
            };
    static final Collection<Capability> capabilities = Collections.unmodifiableCollection(
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

    public OSSFileProvider() {
        super();
        setFileNameParser(OSSFileNameParser.getInstance());
    }

    public static FileSystemOptions getDefaultFileSystemOptions() {
        return defaultFileSystemOptions;
    }


    protected FileSystem doCreateFileSystem(FileName name, FileSystemOptions options) throws FileSystemException {
        FileSystemOptions fsOptions = options != null ? options : defaultFileSystemOptions;
        OSSClient client = OSSClientHolder.getClient();
        if (client == null) {
            OSSClientHolder.init(fsOptions);
        }

        if (name instanceof OSSFileName) {
            return new OSSFileSystem((OSSFileName) name, fsOptions);
        }
        return null;
    }

    public Collection<Capability> getCapabilities() {
        return capabilities;
    }

    public FileSystemConfigBuilder getConfigBuilder() {
        return OSSFileSystemConfigBuilder.getInstance();
    }

}
