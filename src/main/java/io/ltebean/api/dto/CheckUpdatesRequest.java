package io.ltebean.api.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ltebean on 16/5/6.
 */
public class CheckUpdatesRequest {

    public String secret;

    public List<PackageRequirement> packageRequirements = new ArrayList<>();

    public static class PackageRequirement {

        public String name;

        public String versionRange;
    }
}
