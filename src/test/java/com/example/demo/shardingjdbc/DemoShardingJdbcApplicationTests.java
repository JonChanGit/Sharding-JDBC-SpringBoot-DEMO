package com.example.demo.shardingjdbc;

import com.example.demo.shardingjdbc.entity.Course;
import com.example.demo.shardingjdbc.entity.User;
import com.example.demo.shardingjdbc.mapper.sharding.CourseMapper;
import com.example.demo.shardingjdbc.mapper.alone.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class DemoShardingJdbcApplicationTests {

    @Autowired
    private CourseMapper mapper;
    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {

    }

    @Test
    void userMapperLoadTest() {
        final List<User> list = userMapper.selectList(null);
        log.info("{}",list);
    }

    @Test
    void saveTest() {
        final Course course = new Course()
                .setName("Ashe")
                .setUserId(1)
                .setStatus("S1");

        mapper.insert(course);

        final Course  c= mapper.selectById(1457246796270043137L);
        log.info("xxx {}", c);
    }
}
