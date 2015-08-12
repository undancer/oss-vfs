package com.github.undancer.vfs.provider.oss;

import com.github.undancer.vfs.provider.oss.OSSFileObject;

/**
 * Created by undancer on 15/8/12.
 */
public class OSSFileCache {

    private boolean valid = false;

    public OSSFileCache(OSSFileObject ossFileObject) {

    }


    public long getSize() {
        return 0;
    }

    public boolean isValid() {
        return valid;
    }
}
