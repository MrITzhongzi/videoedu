package com.itzhongzi.videoedu.service;

import com.itzhongzi.videoedu.domain.Video;
import com.itzhongzi.videoedu.mapper.VideoMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class VideoServiceTest {

    @Autowired
    private VideoService videoService;

    @Autowired
    private VideoMapper videoMapper;

    @Test
    void findAll() {
        List<Video> list = videoService.findAll();
        assertNotNull(list); //断言不为空
        for (Video video : list) {
            System.out.println(video.getTitle());
        }
    }

    @Test
    void findById() {
        Video video = videoService.findById(1);
        assertNotNull(video);
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void save() {
    }
}