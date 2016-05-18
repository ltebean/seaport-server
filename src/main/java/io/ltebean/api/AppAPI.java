package io.ltebean.api;

import io.ltebean.ServerApplication;
import io.ltebean.api.dto.CheckUpdatesRequest;
import io.ltebean.api.dto.CheckUpdatesResponse;
import io.ltebean.api.dto.InfoRequest;
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
public class AppAPI {

    private Logger logger = LoggerFactory.getLogger(AppAPI.class);

    @Autowired
    PackageMapper packageMapper;

    @Autowired
    AppMapper appMapper;

    @Autowired
    Qiniu qiniu;

    @RequestMapping(value = "/api/v1/app/info", method = RequestMethod.POST)
    public List<Package> getInfo(@RequestBody InfoRequest request) {
        logger.info("test");
        App app = appMapper.findBySecret(request.secret);
        if (app == null) {
            return new ArrayList<>();
        }
        return packageMapper.findByAppId(app.id);
    }


    @RequestMapping(value = "/api/v1/app/updates", method = RequestMethod.POST)
    public CheckUpdatesResponse checkUpdates(@RequestBody CheckUpdatesRequest request) {
        App app = appMapper.findBySecret(request.secret);
        if (app != null) {
            app.packages = packageMapper.findByAppId(app.id);
        }
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


    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/app/package")
    public String uploadPackage(@RequestParam("secret") String secret,
                                @RequestParam("packageName") String packageName,
                                @RequestParam("packageVersion") String packageVersion,
                                @RequestParam("file") MultipartFile file,
                                ServletRequest request) {


        App app = appMapper.findBySecret(secret);
        if (app == null) {
            return "App not found";
        }

        // check duplicate package
        app.packages = packageMapper.findByAppId(app.id);
        for (Package pkg : app.packages) {
            if (pkg.name.equals(packageName) && pkg.version.equals(packageVersion)) {
                return "Duplicate package";
            }
        }

        // generate temp file
        String tempLocation = "/tmp/" + UUID.randomUUID().toString();
        File tempFile = new File(tempLocation);
        try {
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(tempFile));
            FileCopyUtils.copy(file.getInputStream(), stream);
        } catch (IOException e) {
            return "Failed to upload";
        }

        // upload to qiniu
        String fileName = file.getOriginalFilename();
        boolean success = qiniu.upload(app.bucket, tempLocation, fileName);
        if (!success) {
            return "Failed to upload to Qiniu";
        }

        tempFile.delete();

        // create package
        Package pkg = new Package();
        pkg.appId = app.id;
        pkg.name = packageName;
        pkg.version = packageVersion;
        pkg.url = app.baseUrl + fileName;
        packageMapper.create(pkg);
        return "Success";

    }
}
