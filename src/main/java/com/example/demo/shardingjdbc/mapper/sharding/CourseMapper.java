package com.example.demo.shardingjdbc.mapper.sharding;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.shardingjdbc.entity.Course;
import org.springframework.stereotype.Repository;

/**
 * @author Jon Chan
 * @date 2021/11/6 11:10 上午
 */
@Repository
public interface CourseMapper extends BaseMapper<Course> {
}
