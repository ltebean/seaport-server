package io.ltebean.api;

import io.ltebean.api.dto.CheckUpdatesRequest;
import io.ltebean.api.dto.CheckUpdatesResponse;
import io.ltebean.mapper.AppMapper;
import io.ltebean.mapper.PackageMapper;
import io.ltebean.model.App;
import io.ltebean.model.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ltebean on 16/5/6.
 */

@RestController
public class AppAPI {

    @Autowired
    PackageMapper packageMapper;

    @Autowired
    AppMapper appMapper;

    @RequestMapping(value = "/package/all", method = RequestMethod.GET)
    public App test() {
        return findAppBySecret("test");
    }

    @RequestMapping(value = "/app/{appName}/updates", method = RequestMethod.POST)
    public CheckUpdatesResponse checkUpdates(@RequestBody CheckUpdatesRequest request, @PathVariable("appName") String appName) {
        App app = findAppBySecret(request.secret);
        CheckUpdatesResponse response = new CheckUpdatesResponse();
        if (app == null) {
            response.code = CheckUpdatesResponse.StatusCode.INVALID_SECRET.value;
            return response;
        }
        for (CheckUpdatesRequest.PackageRequirement requirement : request.packageRequirements) {
            Package pkg = app.findPackage(requirement.name, requirement.versionRange);
            if (pkg != null) {
                response.packages.add(pkg);
            }
        }
        return response;
    }

    private App findAppBySecret(String secret) {
        App app = appMapper.findBySecret(secret);
        if (app != null) {
            app.packages = packageMapper.findByAppId(app.id);
        }
        return app;
    }
}
