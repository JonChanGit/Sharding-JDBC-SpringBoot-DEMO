package com.example.demo.shardingjdbc.mapper.alone;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.shardingjdbc.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @author Jon Chan
 * @date 2021/11/6 5:05 下午
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
}
