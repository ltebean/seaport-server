package io.ltebean.api;

import com.github.zafarkhaja.semver.Version;
import io.ltebean.api.dto.CheckUpdatesRequest;
import io.ltebean.api.dto.InfoRequest;
import io.ltebean.api.dto.Response;
import io.ltebean.mapper.AppMapper;
import io.ltebean.mapper.PackageMapper;
import io.ltebean.model.App;
import io.ltebean.model.Package;
import io.ltebean.uploader.Qiniu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ltebean on 16/5/6.
 */

@RestController
public class CheckUpdatesAPI {

    private Logger logger = LoggerFactory.getLogger(CheckUpdatesAPI.class);

    @Autowired
    PackageMapper packageMapper;

    @Autowired
    AppMapper appMapper;


    @RequestMapping(value = "/api/v1/check_updates", method = RequestMethod.POST)
    public Response checkUpdates(@RequestBody CheckUpdatesRequest request) {
        App app = appMapper.findBySecret(request.secret);
        if (app != null) {
            app.packages = packageMapper.findByAppId(app.id);
        }
        Response response = new Response();
        if (app == null) {
            response.code = 404;
            return response;
        }
        List<Package> packages = new ArrayList<>();
        for (CheckUpdatesRequest.PackageRequirement requirement : request.packageRequirements) {
            Package pkg = app.findPackage(requirement.name, requirement.versionRange);
            if (pkg != null) {
                packages.add(pkg);
            }
        }
        response.data = packages;
        return response;
    }
}
