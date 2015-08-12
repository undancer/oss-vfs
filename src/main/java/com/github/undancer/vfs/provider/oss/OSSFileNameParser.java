package com.github.undancer.vfs.provider.oss;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.provider.AbstractFileNameParser;
import org.apache.commons.vfs2.provider.UriParser;
import org.apache.commons.vfs2.provider.VfsComponentContext;

/**
 * Created by undancer on 15/8/12.
 */
public class OSSFileNameParser extends AbstractFileNameParser {

    private static final OSSFileNameParser INSTANCE = new OSSFileNameParser();


    public static OSSFileNameParser getInstance() {
        return INSTANCE;
    }

    // oss://bucketName/key
    // oss:beijing://bucketName/key
    // oss:hangzhou://bucketName/key
    public FileName parseUri(VfsComponentContext context, FileName base, String filename) throws FileSystemException {
        StringBuilder name = new StringBuilder();
        String scheme = UriParser.extractScheme(filename, name);
        UriParser.canonicalizePath(name, 0, name.length(), this);
        UriParser.fixSeparators(name);
        FileType type = UriParser.normalisePath(name);
        final String bucketName = UriParser.extractFirstElement(name);
        String path = name.toString();
        return new OSSFileName(scheme, bucketName, path, type);
    }

}
