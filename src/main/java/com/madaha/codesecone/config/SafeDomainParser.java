package com.madaha.codesecone.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

public class SafeDomainParser {

    private static Logger logger = LoggerFactory.getLogger(SafeDomainParser.class);

    public SafeDomainParser() {
        String rootTag = "domains";
        String safeDomainTag = "safedomains";
        String blockDomainTag = "blockdomains";
        String finalTag = "domain";
        String safeDomainClassPath = "url" + File.separator + "url_safe_domain.xml";
        ArrayList<String> safeDomains = new ArrayList<>();
        ArrayList<String> blockDomains = new ArrayList<>();

        try {
            // 读取resources目录下的xml文件
            ClassPathResource resource = new ClassPathResource(safeDomainClassPath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(resource.getInputStream());     // parse xml

            NodeList rootNode = doc.getElementsByTagName(rootTag);  // 解析根节点domains
            Node domainsNode = rootNode.item(0);
            NodeList child = domainsNode.getChildNodes();

            for (int i = 0; i < child.getLength(); i++) {
                Node node = child.item(i);

                // 解析 safeDomains 节点
                if (node.getNodeName().equals(safeDomainTag)) {
                    NodeList tagChild = node.getChildNodes();
                    for (int j = 0; j < tagChild.getLength(); j++) {
                        Node finalTageNode = tagChild.item(j);

                        // 解析 safeDomains 节点里的 domain 节点。
                        if (finalTageNode.getNodeName().equals(finalTag)){
                            safeDomains.add(finalTageNode.getTextContent());
                        }
                    }
                } else if (node.getNodeName().equals(blockDomainTag)) {
                    // 解析 blockDomains 节点
                    NodeList finalTagNode = node.getChildNodes();
                    for (int j = 0; j < finalTagNode.getLength(); j++) {
                        Node tagNode = finalTagNode.item(j);

                        // 解析 blockDomains 节点里的 domain 节点。
                        if (tagNode.getNodeName().equals(finalTag)) {
                            blockDomains.add(tagNode.getTextContent());
                        }
                    }
                }
            }

        } catch (Exception e){
            logger.error(e.toString());
        }

        WebConfig wc = new WebConfig();
        wc.setSafeDomains(safeDomains);
        logger.info(safeDomains.toString());
        wc.setBlockDomains(blockDomains);
    }
}
