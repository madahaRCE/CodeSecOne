package com.madaha.codesecone.controller.sqli;

import com.madaha.codesecone.util.Security;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.OracleCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.Map;

/**
 * SQL注入 - JDBC注入
 *
 * 需要审计的函数：
 *    1、executeQuery
 *    2、prepareStatement
 *    3、queryForMap
 *    4、query
 */
@RestController
@RequestMapping("/SQLI/JDBC")
public class JDBC {

    Logger log = LoggerFactory.getLogger(JDBC.class);


    @Value("${spring.datasource.url}")
    private String db_url;

    @Value("${spring.datasource.username}")
    private String db_user;

    @Value("${spring.datasource.password}")
    private String db_pass;


    /**
     * JDBC语句拼接：
     *
     * @poc1 http://127.0.0.1:28888/SQLI/JDBC/vul1?id=1%27%20union%20select%201,2,user()--+
     * @decode1 http://127.0.0.1:28888/SQLI/JDBC/vul1?id=1' union select 1,2,user()--+
     *
     * @poc2 http://127.0.0.1:28888/SQLI/JDBC/vul1?id=1%27%20and%20updatexml(1,concat(0x7e,(SELECT%20user()),0x7e),1)--%20+
     * @decode2 http://127.0.0.1:28888/SQLI/JDBC/vul1?id=1' and updatexml(1,concat(0x7e,(SELECT user()),0x7e),1)--+
     *
     * @param id
     * @return
     */
    @GetMapping("/vul1")
    public String vul1(String id){
        StringBuilder result = new StringBuilder();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(db_url, db_user, db_pass);

            Statement stmt = conn.createStatement();
            String sql = "select * from users where id = '" + id + "'";

            log.info("[vul] 执行SQL语句： " + sql);
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()){
                String res_name = rs.getString("user");
                String res_pass = rs.getString("pass");
                String info = String.format("查询结果 %s: %s", res_name, res_pass);
                result.append(info);
            }

            rs.close();
            stmt.close();
            conn.close();

            // 执行，返回查询到的结果；
            return result.toString();

        } catch (Exception e){
            // 输出错误，用于报错注入
            return e.toString();
        }
    }


    /**
     * JDBC预编译：
     *    采用预编译的方法，但没使用 ? 占位，此时进行预编译也无法阻止SQL注入。
     *
     * @poc http://127.0.0.1:28888/SQLI/JDBC/vul2?id=2%20union%20select%201,2,user()--
     * @decode http://127.0.0.1:28888/SQLI/JDBC/vul2?id=2 union select 1,2,user()--
     *
     * @param id
     * @return
     */
    @GetMapping("/vul2")
    public String vul2(String id){
        StringBuilder result = new StringBuilder();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(db_url, db_user, db_pass);

            String sql = "select * from users where id = " + id;
            log.info("[vul] 执行SQL语句： " + sql);

            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()){
                String res_name = rs.getString("user");
                String res_pass = rs.getString("pass");
                String info = String.format("查询结果%n %s: %s%n", res_name, res_pass);
                result.append(info);
            }

            rs.close();
            st.close();
            conn.close();

            return result.toString();

        }catch (Exception e){
            return e.toString();
        }
    }


    /**
     * JdbcTemplate
     *    JDBCTemplate 是Spring对JDBC的封装，使其更易于使用；
     *
     * @poc http://127.0.0.1:28888/SQLI/JDBC/vul3?id=2%20and%201=1
     * @decode http://127.0.0.1:28888/SQLI/JDBC/vul3?id=2 and 1=1
     *
     * @param id
     * @return
     */
    @GetMapping("/vul3")
    public Map<String, Object> vul3(String id){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(db_url);
        dataSource.setUsername(db_user);
        dataSource.setPassword(db_pass);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String sql_vul = "select * from users where id = " + id;
        String sql_safe = "select * from users where id = ?";

        return jdbcTemplate.queryForMap(sql_vul);
    }



    /**
     * safe1：
     *
     * JDBC预编译：
     *    采用预编译的方法，使用?占位，也叫参数化的SQL。
     *
     * 注意：测试后，发现所有添加的其他单数，都会被过滤掉。
     *
     * @param id
     * @return
     */
    @GetMapping("/safe1")
    public String safe1(String id){
        StringBuilder result = new StringBuilder();

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(db_url, db_user, db_pass);

            String sql = "select * from users where id =?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, id);
            log.info("[safe] 执行SQL语句： " + st);
            ResultSet rs = st.executeQuery();

            while (rs.next()){
                String res_name = rs.getString("user");
                String res_pass = rs.getString("pass");
                String info = String.format("查询结果%n %s: %s%n", res_name, res_pass);
                result.append(info);
            }

            rs.close();
            st.close();
            conn.close();

            return result.toString();

        }catch (Exception e){
            return e.toString();
        }
    }


    /**
     * 采用黑名单过滤的方式：
     *
     * @param id
     * @return
     */
    @GetMapping("/safe2")
    public String safe2(String id){

        if(!Security.checkSql(id)){
            StringBuilder result = new StringBuilder();

            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(db_url, db_user, db_pass);

                Statement stmt = conn.createStatement();
                String sql = "select * from users where id = '" + id + "'";
                ResultSet rs = stmt.executeQuery(sql);
                log.info("[safe] 执行SQL语句： " + sql);

                while (rs.next()){
                    String res_name = rs.getString("user");
                    String res_pass = rs.getString("pass");
                    String info = String.format("查询结果%n %s: %s%n", res_name, res_pass);
                    result.append(info);
                }

                rs.close();
                stmt.close();
                conn.close();
                return result.toString();

            }catch (Exception e){
                return e.toString();
            }

        }else {
            log.warn("监测到非法注入");
            return "监测到非法注入！";
        }
    }


    /**
     * 采用ESAPI过滤：
     *
     * 解决办法找到了：
     *     解决方法：将esapi.jar添加到项目之后，还要引入ESAPI.properties和validation.properties两个文件。
     *     参考链接：https://blog.csdn.net/woqq773743943/article/details/65630452
     *
     * @param id
     * @return
     */
    @GetMapping("/safe3")
    public String safe3(String id){
        StringBuilder result = new StringBuilder();

        try {
            Codec<Character> oracleCodec = new OracleCodec();
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(db_url, db_user, db_pass);

            Statement stmt = conn.createStatement();
            String sql = "select * from users where id = '" + ESAPI.encoder().encodeForSQL(oracleCodec, id) + "'";
            log.info("[safe] 执行SQL语句： " + sql);
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()){
                String res_name = rs.getString("user");
                String res_pass = rs.getString("pass");
                String info = String.format("查询结果%n %s: %s%n", res_name, res_pass);
                result.append(info);
            }

            rs.close();
            stmt.close();
            conn.close();

            return result.toString();

        } catch (Exception e){
            return e.toString();
        }
    }

    /**
     * safe4:
     *    强制数据类型
     *
     * 注：这里设置 Integer 类型，就是必须要使用此类型，避免字符修改产生sql注入问题。
     *    从根上治病，最为有效！！！
     *
     *    payload（报错）:  http://127.0.0.1:28888/SQLI/JDBC/safe4?id=2%20union%20select%201,2,user()--+
     *    @poc（能且仅能，用Integer）： http://127.0.0.1:28888/SQLI/JDBC/safe4?id=2
     *
     * @param id
     * @return
     */
    @GetMapping("/safe4")
    public Map<String, Object> safe4(Integer id) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(db_url);
        dataSource.setUsername(db_user);
        dataSource.setPassword(db_pass);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        String sql_vul = "select * from users where id = " + id;

        return jdbcTemplate.queryForMap(sql_vul);
    }
}
