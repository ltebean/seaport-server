package io.ltebean.api.dto;

import io.ltebean.model.Package;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ltebean on 16/5/6.
 */
public class CheckUpdatesResponse {

    public enum StatusCode {
        SUCCESS(0), INVALID_SECRET(1);
        public int value;
        private StatusCode(int value) {
            this.value = value;
        }
    }

    public int code = StatusCode.SUCCESS.value;

    public List<Package> packages = new ArrayList<>();
}
