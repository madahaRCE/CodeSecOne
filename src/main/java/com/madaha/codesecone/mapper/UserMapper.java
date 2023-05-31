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
     * 传统 xml 的配置，这里必须对resources/mapper/mapper.xml文件进行配置，数据库查询语句的配置。
     */
    List<User> orderBy_Vuln(String field, String sort);

    List<User> orderBy_Safe(String field);




    // 使用#{}会产生报错；  是因为 "order by" 排序的字段不能做特殊处理，所以此处存在SQL注入漏洞问题。
    @Select("select * from users order by ${field} desc")
    List<User> orderBy2_Vuln(@Param("field") String field);



    /**
     * MyBatis3 提供了新的基于注解的配置，通过注解不再需要配置繁琐的xml文件
     */
    @Select("select * from users where user like CONCAT('%', #{user}, '%')")
    List<User> queryByUser_Safe(@Param("user") String user);

    @Select("select * from users where id = ${id}")
    List<User> queryById_Safe(@Param("id") Integer id);

    @Select("select * from users where id = ${id}")
    List<User> queryById_Vuln(@Param("id") String id);



    @Select("select * from users")
    List<User> list();



    // 模糊搜索，直接 '%#{q}%' 预编译拼接 会报错！！！
    @Select("select * from users where user like '%${q}%'")
    List<User> search_Vuln(String q);

    @Select("select * from users where user like concat('%', #{q}, '%')")
    List<User> search_Safe(String q);

}
