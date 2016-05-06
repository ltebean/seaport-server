package io.ltebean.mapper;

import io.ltebean.model.App;
import io.ltebean.model.Package;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by ltebean on 16/5/6.
 */
@Mapper
public interface AppMapper {

    @Select("select * from App where secret = #{secret}")
    App findBySecret(@Param("secret") String secret);

}
