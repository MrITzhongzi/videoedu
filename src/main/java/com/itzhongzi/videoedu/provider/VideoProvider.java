package com.itzhongzi.videoedu.provider;

import com.itzhongzi.videoedu.domain.Video;
import org.apache.ibatis.jdbc.SQL;

public class VideoProvider {
    public String updateVideo(final Video video){
        return new SQL(){{
            UPDATE("video");

            //条件写法.
            if(video.getTitle()!=null){
                SET("title=#{title}");
            }
            if(video.getSummary()!=null){
                SET("summary=#{summary}");
            }
            if(video.getCoverImg()!=null){
                SET("cover_img=#{coverImg}");
            }
            if(video.getViewNum()!=null){
                SET("view_num=#{viewNum}");
            }
            if(video.getPrice()!=null){
                SET("price=#{price}");
            }
            if(video.getOnline()!=null){
                SET("online=#{online}");
            }
            if(video.getPoint()!=null){
                SET("point=#{point}");
            }

            WHERE("id=#{id}");
        }}.toString();
    }
}
