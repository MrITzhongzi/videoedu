package com.itzhongzi.videoedu.service;

import com.itzhongzi.videoedu.domain.Video;

import java.util.List;

public interface VideoService {

    List<Video> findAll();
    Video findById(int id);
    int update(Video video);
    int delete(int id);
    int save(Video video);
}
