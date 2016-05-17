package io.ltebean.mapper;



import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import io.ltebean.model.Package;

import java.util.List;

/**
 * Created by ltebean on 16/5/6.
 */

@Mapper
public interface PackageMapper {

    @Select("select * from Package where appId = #{appId}")
    List<Package> findByAppId(@Param("appId") long appId);

    @Insert("insert into Package(appId, name, version, url) values(#{appId}, #{name}, #{version}, #{url})")
    void create(Package pkg);

}
