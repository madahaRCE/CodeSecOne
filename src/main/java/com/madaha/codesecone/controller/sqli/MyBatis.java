package com.madaha.codesecone.controller.sqli;

import com.madaha.codesecone.entity.User;
import com.madaha.codesecone.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/SQLI/MyBatis")
public class MyBatis {

    @Autowired
    private UserMapper userMapper;

    /**
     * @payload http://127.0.0.1:28888/SQLI/MyBatis/vul/id/1%20and%201=1%20#---
     */
    @GetMapping("/vul/id/{id}")
    public List<User> queryById_Vuln(@PathVariable String id){
        return userMapper.queryById_Vuln(id);
    }

    @GetMapping("/vul/search")
    public List<User> search_Vuln(@RequestParam("q") String q){
        return userMapper.search_Vuln(q);
    }

    @GetMapping("/vul/orderBy")
    public List<User> orderBy_Vuln(String field, String sort){
        return userMapper.orderBy_Vuln(field, sort);
    }

    @GetMapping("/vul/orderBy2")
    public List<User> orderBy2_Vuln(String field){
        return userMapper.orderBy2_Vuln(field);
    }



    // ---------------------------------------安全分割线-----------------------------------

    @GetMapping("/safe/query")
    public List<User> queryByUser_Safe(String user){
        return userMapper.queryByUser_Safe(user);
    }

    @GetMapping("/safe/id/{id}")
    public List<User> queryById_Safe(@PathVariable Integer id){
        return userMapper.queryById_Safe(id);
    }

    @GetMapping("/safe/list")
    public List<User> list() {
        return userMapper.list();
    }

    @GetMapping("/safe/orderBy")
    public List<User> orderBy_Safe(String field){
        return userMapper.orderBy_Safe(field);
    }

}
