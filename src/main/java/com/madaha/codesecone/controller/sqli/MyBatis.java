package com.madaha.codesecone.controller.sqli;

import com.madaha.codesecone.entity.User;
import com.madaha.codesecone.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/SQLI/MyBatis")
public class MyBatis {

    @Autowired
    private UserMapper userMapper;

    /**
     * @payload http://127.0.0.1:18888/SQLI/MyBatis/vul/id/1%20and%201=1%20#---
     * @param id
     * @return
     */
    @GetMapping("/vul/id/{id}")
    public List<User> queryById1(@PathVariable String id){
        return userMapper.queryById1(id);
    }
}
