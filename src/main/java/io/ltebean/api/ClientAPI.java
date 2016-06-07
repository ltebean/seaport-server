package io.ltebean.api;

import com.github.zafarkhaja.semver.Version;
import io.ltebean.api.dto.*;
import io.ltebean.mapper.AppMapper;
import io.ltebean.mapper.PackageMapper;
import io.ltebean.mapper.UserMapper;
import io.ltebean.model.App;
import io.ltebean.model.Package;
import io.ltebean.model.User;
import io.ltebean.uploader.Uploader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by ltebean on 16/5/6.
 */

@RestController
public class ClientAPI {

    private Logger logger = LoggerFactory.getLogger(ClientAPI.class);

    @Autowired
    PackageMapper packageMapper;

    @Autowired
    AppMapper appMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    Uploader uploader;

    @RequestMapping(value = "/api/v1/user/signup", method = RequestMethod.POST)
    public Response<String> signup(@RequestBody SignupRequest request) {
        User userInDB = userMapper.findByName(request.name);
        Response<String> response = new Response<>();
        if (userInDB != null) {
            response.code = 400;
            response.message = "User already exists";
            return response;
        }
        User user = new User();
        user.name = request.name;
        user.passwordHash = BCrypt.hashpw(request.password, BCrypt.gensalt());
        user.token = new Md5PasswordEncoder().encodePassword(request.name + request.password, null);
        userMapper.create(user);
        response.data = user.token;
        return response;
    }

    @RequestMapping(value = "/api/v1/user/login", method = RequestMethod.POST)
    public Response<String> login(@RequestBody LoginRequest request) {
        User user = userMapper.findByName(request.name);
        Response<String> response = new Response<>();
        if (user == null) {
            response.code = 404;
            response.message = "User not found";
            return response;
        }
        boolean  matched = BCrypt.checkpw(request.password, user.passwordHash);
        if (matched) {
            response.data = user.token;
            return response;
        } else {
            response.code = 403;
            response.message = "Invalid password";
            return response;
        }
    }

    @RequestMapping(value = "/api/v1/app", method = RequestMethod.POST)
    public Response<String> createApp(@RequestBody CreateAppRequest createAppRequest, HttpServletRequest request) {
        Response<String> response = new Response<>();
        User user = getUserFromRequest(request);
        if (user == null) {
            response.code = 403;
            response.message = "Unauthorized request";
            return response;
        }
        String appName = createAppRequest.name;

        List<App> appList = appMapper.findByUserId(user.id)
                .stream()
                .filter(a -> a.name.equals(appName))
                .collect(Collectors.toList());
        if (!appList.isEmpty()) {
            response.code = 403;
            response.message = "App already exists";
            return response;
        }
        String secret = new Md5PasswordEncoder().encodePassword(user.name + appName, null);
        App app = new App();
        app.name = appName;
        app.secret = secret;
        app.userId = user.id;

        appMapper.create(app);

        response.data = secret;
        return response;
    }


    @RequestMapping(value = "/api/v1/app/info", method = RequestMethod.POST)
    public Response<List<App>> getInfo(@RequestBody InfoRequest infoRequest, HttpServletRequest request) {
        Response<List<App>> response = new Response<>();

        User user = getUserFromRequest(request);
        if (user == null) {
            response.code = 403;
            response.message = "Unauthorized request";
            return response;
        }

        List<App> apps = appMapper.findByUserId(user.id);
        for (App app : apps) {
            app.packages = packageMapper.findByAppId(app.id);
        }
        response.data = apps;
        return response;
    }



    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/app/package")
    public Response<Map> uploadPackage(@RequestParam("appName") String appName,
                                       @RequestParam("packageName") String packageName,
                                       @RequestParam("packageVersion") String packageVersion,
                                       @RequestParam("file") MultipartFile file,
                                       HttpServletRequest request) {

        Response<Map> response = new Response<>();

        User user = getUserFromRequest(request);
        if (user == null) {
            response.code = 403;
            response.message = "Unauthorized request";
            return response;
        }

        try {
            Version v = Version.valueOf(packageVersion);
        } catch (Exception e) {
            response.code = 400;
            response.message = "invalid version number: " + packageVersion;
            return response;
        }

        List<App> appList = appMapper.findByUserId(user.id)
                .stream()
                .filter(a -> a.name.equals(appName))
                .collect(Collectors.toList());

        if (appList.isEmpty()) {
            response.code = 404;
            response.message = "App not found";
            return response;
        }

        App app = appList.get(0);

        if (app.userId != user.id) {
            response.code = 403;
            response.message = "This app does not belongs to you";
            return response;
        }

        // check duplicate package
        app.packages = packageMapper.findByAppId(app.id);
        for (Package pkg : app.packages) {
            if (pkg.name.equals(packageName) && pkg.version.equals(packageVersion)) {
                response.code = 400;
                response.message = "Duplicate package";
                return response;
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
            response.code = 500;
            response.message = "Failed to upload";
            return response;
        }

        // upload to qiniu
        String fileName =  String.format("%s@%s@%s.zip", app.name, packageName, packageVersion);
        String url = uploader.upload(tempLocation, fileName);
        if (url == null) {
            response.code = 500;
            response.message = "Failed to upload to Qiniu";
            return response;
        }

        tempFile.delete();

        // create package
        Package pkg = new Package();
        pkg.appId = app.id;
        pkg.name = packageName;
        pkg.version = packageVersion;
        pkg.url = url;
        packageMapper.create(pkg);

        response.message = "Success";
        return response;
    }

    private User getUserFromRequest(HttpServletRequest request) {
        String token = request.getHeader("X-Token");
        if (token == null) {
            return null;
        }
        return userMapper.findByToken(token);
    }
}
