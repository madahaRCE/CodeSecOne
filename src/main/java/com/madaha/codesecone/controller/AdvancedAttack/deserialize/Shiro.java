package com.madaha.codesecone.controller.AdvancedAttack.deserialize;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

@Controller
@RequestMapping("/Shiro")
public class Shiro {

    // Shiro-550
    /**
     * 漏洞信息	详情
     * 漏洞编号	CVE-2016-4437 / CNVD-2016-03869 / SHIRO-550
     * 影响版本	shiro 1.x < 1.2.5
     * 漏洞描述	如果程序未能正确配置 “remember me” 功能使用的密钥，攻击者可通过发送带有特制参数的请求利用该漏洞执行任意代码或访问受限制内容。
     * 漏洞关键字	cookie | RememberMe | 反序列化 | 硬编码 | AES
     * 漏洞补丁	Commit-4d5bb00
     * 相关链接	SHIRO-441 https://www.anquanke.com/post/id/192619
     */
    @RequestMapping(value = "/key/getKey")
    public void getShiroKey(HttpServletResponse response){
        try {
            byte[] key = new CookieRememberMeManager().getCipherKey();
            response.getWriter().write("shiro key: \n" + new String(Base64.getEncoder().encode(key)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    // map.put("/Shiro/key/**", "anon");
    // map.put("/Shiro/serialize/**", "user");


    /**
     * 1、可能会失败的原因：
     *    （1）在环境中找不到TiedMapEntry、LazyMap、ChainedTransformer、Transformer、ConstantTransformer、InvokerTransformer这几个本该在JDK环境中就存在的类。
     *    （2）调试发现，该payload在readObject()的调用中会调用到org/apache/shiro/io/ClassResolvingObjectInputStream类的resolverClass()函数，很明显ClassResolvingObjectInputStream类继承了ObjectInputStream类并重写了resolverClass()函数，所以序列化数据中使用了数组是会报错的。
     *
     * 2、生成恶意反序列化数据
     *      java -jar ysoserial-all.jar  CommonsCollections5  "calc" > shiro_poc_ser.txt
     *      java -jar ysoserial-all.jar ysoserial.exploit.JRMPListener 1234 CommonsCollections5 "calc"
     *
     * 3、运行脚本，使恶意的反序列化数据进行AES加密，并使用Base64进行编码。
     *      package com.madaha.codesecone.controller.AdvancedAttack.deserialize.Exploit.CreateClassByteCodeToBase64_Shiro#main();
     *
     * 4、最后，使用burp进行发包。
     *      成功弹出 calc.exe
     *      package com.madaha.codesecone.controller.AdvancedAttack.deserialize.Exploit.ShiroEXP.txt
     */
    @PostMapping(value = "/serialize/shiroLogin")
    @ResponseBody
    public String shiroLogin(String account, String password, Boolean rememberMe){
        Subject userSubject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(account, password, rememberMe);
        try {
            // 登录验证
            userSubject.login(token);
            return "登录成功";
        }catch (AuthenticationException e){
            e.printStackTrace();
            return "登录失败";
        }catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }

    @GetMapping(value = "/serialize/shiroLogin")
    // @ResponseBody
    public String shiroLogin(){
        return "/shiroLogin";
//        return "111";
    }





    // Shiro-721
    /**
     * 漏洞信息	详情
     * 漏洞编号	CVE-2019-12422 / CNVD-2016-07814 / SHIRO-721
     * 影响版本	shiro < 1.4.2 (1.2.5, 1.2.6, 1.3.0, 1.3.1, 1.3.2, 1.4.0-RC2, 1.4.0, 1.4.1)
     * 漏洞描述	RememberMe Cookie 默认通过 AES-128-CBC 模式加密，这种加密方式容易受到 Padding Oracle Attack 攻击，攻击者利用有效的 RememberMe Cookie 作为前缀，然后精心构造 RememberMe Cookie 值来实现反序列化漏洞攻击。
     * 漏洞关键字	反序列化 | RememberMe | Padding | CBC
     * 漏洞补丁	Commit-a801878
     * 相关链接	https://blog.skullsecurity.org/2016/12
     *          https://resources.infosecinstitute.com/topic/padding-oracle-attack-2/
     *          https://codeantenna.com/a/OwWV5Ivtsi
     */
}
