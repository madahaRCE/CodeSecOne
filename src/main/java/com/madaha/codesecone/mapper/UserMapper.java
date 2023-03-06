package com.madaha.codesecone.mapper;

import com.madaha.codesecone.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 标识这个类是一个数据访问层的bean，并交给spring容器管理
 *
 * 配置注解，标识此类被spring自动管理
 */
@Mapper
public interface UserMapper {
    /**
     * 传统xml 配置
     */
    List<User> orderBy(String field, String sort);


    /**
     * MyBatis3 提供了新的基于注解的配置，通过注解不再需要配置繁琐的xml文件
     */
    @Select("select * from users where id = ${id}")
    List<User> queryById1(@Param("id") String id);

}
