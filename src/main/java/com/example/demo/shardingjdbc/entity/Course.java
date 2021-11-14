package com.example.demo.shardingjdbc.entity;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author Jon Chan
 * @date 2021/11/6 11:07 上午
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class Course {
    private Long id;
    private String name;
    private Integer userId;
    private String status;
}
