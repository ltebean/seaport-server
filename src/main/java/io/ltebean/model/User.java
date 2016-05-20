package io.ltebean.model;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Created by leo on 16/5/19.
 */
public class User {

    public long id;

    public String name;

    public String passwordHash;

    public String token;

}
