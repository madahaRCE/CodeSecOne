package com.madaha.codesecone.controller.ddos;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/DOS")
public class DOS {

    /**
     * 检查信息：正则表达式 拒绝服务攻击（RegexDos）
     *    当编写的正则表达式存在缺陷时，攻击者可以构造特殊的字符串来大量消耗系统资源，造成服务中断或停止。
     *
     * 错误实践：
     *    Regex: ([a-z]+)+
     *    Regex: (.*[a-z]){n} n >= 10
     *
     * 推荐方法：（安全正则引擎）
     *    (1) 优化Regex语句
     *    (2) 换用RE2/J线性时间复杂度引擎
     *
     *    com.google.re2j.Pattern p = con.google.re2j.Pattern.compile(REGEX);
     *    com.google.re2j.Matcher m = p.matcher(request.getParameter("input"));
     *
     */



    /**
     * @poc /DOS/regDos/vul?content=aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab
     * @payload http://127.0.0.1:28888/DOS/regDos/vul?content=aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab
     *
     * @param content
     * @return string
     */
    @GetMapping("/regDos/vul")
    public String vul(String content){
        boolean match = Pattern.matches("(a|aa)+", content);
        return String.format("正则匹配：%s，正则表达式拒绝服务攻击！", match);
    }


    /**
     * @payload http://127.0.0.1:28888/DOS/regDos/safe?content=aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab
     *
     * @param content
     * @return String
     */
    @GetMapping("/regDos/safe")
    public String safe(String content){
        boolean match = com.google.re2j.Pattern.matches("(a|aa)+", content);
        return String.format("正则匹配：%s，安全正则表达式。", match);
    }
}
