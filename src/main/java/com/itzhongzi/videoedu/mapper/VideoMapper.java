package com.itzhongzi.videoedu.mapper;

import com.itzhongzi.videoedu.domain.Video;
import com.itzhongzi.videoedu.provider.VideoProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface VideoMapper {

    @Select("select * from video")
    List<Video> findAll();

    @Select("SELECT * FROM video WHERE id = #{id}")
    Video findById(int id);

//    @Update("UPDATE video SET title=#{title} WHERE id =#{id}")
    @UpdateProvider(type = VideoProvider.class, method = "updateVideo")
    int update(Video Video);


    @Delete("DELETE FROM video WHERE id =#{id}")
    int delete(int id);

    /**
     *  保存对象，获取数据库自增id
     * @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
     * keyProperty 是 video对象中的id  keyColumn 是数据库中的自增id， 我们想要获取自增的id需要加入  @Options 这个注解，如下所示
     * @param video
     * @return
     */
    @Insert("INSERT INTO `video` ( `title`, `summary`, " +
            "`cover_img`, `view_num`, `price`, `create_time`," +
            " `online`, `point`)" +
            "VALUES" +
            "(#{title}, #{summary}, #{coverImg}, #{viewNum}, #{price},#{createTime}" +
            ",#{online},#{point});")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int save(Video video);
}
