package com.itzhongzi.videoedu.mapper;

import com.itzhongzi.videoedu.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    /**
     * 根绝ID 查询 user
     * @param userId
     * @return
     */
    @Select("select * from user where id = #{id}")
    User findByid(@Param("id") int userId);

    @Select("select * from user where id = #{openid}")
    User findByopenid(@Param("openid") String openid);

    /**
     * 保存 user 信息
     * @param user
     * @return
     */
    @Insert("INSERT INTO `video_education`.`user`(`openid`, `name`, `head_img`, `phone`, `sign`, `sex`, `city`, `create_time`) " +
            "VALUES (#{openid},#{name},#{headImg},#{phone},#{sign},#{sex},#{city},#{createTime});")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int save(User user);
}
