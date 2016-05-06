package io.ltebean.model;


import com.github.zafarkhaja.semver.Version;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ltebean on 16/5/6.
 */
public class App {

    public long id;

    public String name;

    public String secret;

    public long userId;

    public List<Package> packages = new ArrayList<>();

    public Package findPackage(final String name, String versionRange) {
        List<Package> availableOnes = packages.stream()
                .filter(p -> p.name.equals(name) && p.satisfies(versionRange))
                .sorted((p1, p2) -> -Version.valueOf(p1.version).compareTo(Version.valueOf(p2.version)))
                .collect(Collectors.toList());
        if (availableOnes.isEmpty()) {
            return null;
        } else {
            return availableOnes.get(0);
        }

    }

}
