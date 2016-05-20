package io.ltebean.mapper;

import io.ltebean.model.App;
import io.ltebean.model.Package;
import io.ltebean.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by leo on 16/5/19.
 */
@Mapper
public interface UserMapper {

    @Select("select * from User where name = #{name}")
    User findByName(@Param("name") String name);

    @Select("select * from User where token = #{token}")
    User findByToken(@Param("token") String token);

    @Insert("insert into User(name, passwordHash, token) values(#{name}, #{passwordHash}, #{token})")
    void create(User user);

}
