package com.github.undancer.vfs.provider.oss;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;

/**
 * Created by undancer on 15/8/25.
 */
public class OSSUtil {
    public static void initOSSProvider(String accessKeyId, String accessKeySecret) throws FileSystemException {
        StaticUserAuthenticator userAuthenticator = new StaticUserAuthenticator(null, accessKeyId, accessKeySecret);
        final FileSystemOptions options = OSSFileProvider.getDefaultFileSystemOptions();
        DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(options, userAuthenticator);
    }

}
