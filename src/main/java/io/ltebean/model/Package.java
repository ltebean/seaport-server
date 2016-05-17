package io.ltebean.model;

import com.github.zafarkhaja.semver.Version;

/**
 * Created by ltebean on 16/5/6.
 */
public class Package {

    public long id;

    public long appId;

    public String name;

    public String version;

    public String url;

    public boolean satisfies(String versionRange) {
        Version v = Version.valueOf(version);
        return v.satisfies(versionRange);
    }



}
