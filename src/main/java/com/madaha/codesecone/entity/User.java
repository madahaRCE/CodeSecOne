package com.madaha.codesecone.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * entry 实体类代码
 * @data 2023/03/06
 *
 * @Data:注解在类上，给类提供了set，get，equasl，toString，hashCode等方法。
 * @NoArgsConstructor：给类添加一个无参数的构造方法。
 * @AllArgsConstructor：给类添加一个全参的构造方法。
 * @Accessors(chain=true)：让该类的实体类支持链式访问呢。
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String user;
    private String pass;
}
