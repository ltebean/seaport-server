package io.ltebean.api.dto;

import io.ltebean.model.Package;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ltebean on 16/5/6.
 */
public class CheckUpdatesResponse {

    public int code = 200;

    public List<Package> packages = new ArrayList<>();

}
