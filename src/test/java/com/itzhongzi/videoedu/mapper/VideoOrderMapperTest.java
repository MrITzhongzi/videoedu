package com.itzhongzi.videoedu.mapper;

import com.itzhongzi.videoedu.domain.VideoOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VideoOrderMapperTest {

    @Autowired
    private VideoOrderMapper videoOrderMapper;

    @Test
    public void insert() {
        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setDel(0);
        videoOrder.setTotalFee(111);
        videoOrder.setHeadImg("xxxx");
        videoOrder.setVideoTitle("高级教程");
        videoOrderMapper.insert(videoOrder);
        assertNotNull(videoOrder);
    }

    @Test
    public void findById() {
        VideoOrder videoOrder = videoOrderMapper.findById(1);
        assertNotNull(videoOrder);
    }

    @Test
    public void findByOutTrade() {
    }

    @Test
    public void del() {
    }

    @Test
    public void findMyOrderList() {
    }

    @Test
    public void updateVideoOrderByOutTradeNo() {
    }
}