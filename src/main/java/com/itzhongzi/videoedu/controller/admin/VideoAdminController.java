package com.itzhongzi.videoedu.controller.admin;

import com.itzhongzi.videoedu.domain.Video;
import com.itzhongzi.videoedu.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api/v1/video")
public class VideoAdminController {

    @Autowired
    private VideoService videoService;


    @DeleteMapping("del_by_id")
    public Object delById(@RequestParam(value = "video_id", required = true) int video_id){

        return videoService.delete(video_id);
    }

    @PutMapping("update_by_id")
    public Object update(@RequestBody Video video){

        return videoService.update(video);
    }

    @PostMapping("save")
    public Object save(@RequestBody Video video){
        return videoService.save(video);
    }
}
