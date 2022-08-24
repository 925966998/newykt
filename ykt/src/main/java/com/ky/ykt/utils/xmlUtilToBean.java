package com.ky.ykt.utils;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Value;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class xmlUtilToBean {
    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     *
     * @param strxml
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    @Value("${hexKey}")
    private String hexKey;
    public static Map doXMLParse(String strxml) throws JDOMException, IOException {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

        if (null == strxml || "".equals(strxml)) {
            return null;
        }

        Map m = new HashMap();

        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = xmlUtilToBean.getChildrenText(children);
            }

            m.put(k, v);
        }

        // 关闭流
        in.close();

        return m;
    }

    /**
     * 获取子结点的xml
     *
     * @param children
     * @return String
     */
    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(xmlUtilToBean.getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }

    public static String xmlFormat(Map<String, String> param) {
        StringBuffer xmlStr = new StringBuffer("<xml>");
        for (String key : param.keySet()) {
            xmlStr.append("<").append(key).append(">");
            xmlStr.append(param.get(key));
            xmlStr.append("</").append(key).append(">");
        }
        xmlStr.append("</xml>");
        return xmlStr.toString();
    }

    public static Map<String, String> xmlParse(String xmlStr) throws JDOMException, IOException {
        TreeMap<String, String> rtnMap = new TreeMap<String, String>();
        SAXBuilder builder = new SAXBuilder();
        Document doc = (Document) builder.build(new StringReader(xmlStr));
        // 得到根节点
        Element root = doc.getRootElement();
        String rootName = root.getName();
        rtnMap.put("root.name", rootName);
        // 调用递归函数，得到所有最底层元素的名称和值，加入map中
        convert(root, rtnMap, rootName);
        return rtnMap;
    }

    /**
     * 将 XML 字符串转换为 Java 对象
     *
     * @param clazz 要转换对象的 class
     * @param xml   待转换的 xml
     * @return 转换后的对象
     */
    public static Object xmlToBean(Class<?> clazz, String xml) {
        try {
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller us = jc.createUnmarshaller();
            return us.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 递归函数，找出最下层的节点并加入到map中，由xml2Map方法调用。
     *
     * @param e        xml节点，包括根节点
     * @param map      目标map
     * @param lastname 从根节点到上一级节点名称连接的字串
     */
    @SuppressWarnings("rawtypes")
    public static void convert(Element e, Map<String, String> map, String lastname) {
        if (e.getAttributes().size() > 0) {
            Iterator it_attr = e.getAttributes().iterator();
            while (it_attr.hasNext()) {
                Attribute attribute = (Attribute) it_attr.next();
                String attrname = attribute.getName();
                String attrvalue = e.getAttributeValue(attrname);
                // map.put( attrname, attrvalue);
                map.put(lastname + "." + attrname, attrvalue); // key 根据根节点 进行生成
            }
        }
        List children = e.getChildren();
        Iterator it = children.iterator();
        while (it.hasNext()) {
            Element child = (Element) it.next();
            /* String name = lastname + "." + child.getName(); */
            String name = child.getName();
            // 如果有子节点，则递归调用
            if (child.getChildren().size() > 0) {
                convert(child, map, lastname + "." + child.getName());
            } else {
                // 如果没有子节点，则把值加入map
                map.put(name, child.getText());
                // 如果该节点有属性，则把所有的属性值也加入map
                if (child.getAttributes().size() > 0) {
                    Iterator attr = child.getAttributes().iterator();
                    while (attr.hasNext()) {
                        Attribute attribute = (Attribute) attr.next();
                        String attrname = attribute.getName();
                        String attrvalue = child.getAttributeValue(attrname);
                        map.put(lastname + "." + child.getName() + "." + attrname, attrvalue);
                    }
                }
            }
        }
    }

    /**
     * JavaBean转换成xml
     *
     * @param obj
     * @param encoding
     * @return
     */
    public static String convertToXml(Object obj, String encoding, boolean format) {
        String result = null;
        StringWriter writer = null;
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            writer = new StringWriter();
            marshaller.marshal(obj, writer);
            result = writer.toString();
        } catch (Exception e) {
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String convertToXml(Object obj, String encoding) throws Exception {
        String result = null;

        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        // 指定是否使用换行和缩排对已编组 XML 数据进行格式化的属性名称。
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
        //marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        /*marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);*/
        /*marshaller.setProperty("com.sun.xml.bind.xmlHeaders",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");*/
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        /*result = writer.toString().replace("standalone=\"yes\"", "");*/
        result = writer.toString();
        Pattern p = Pattern.compile(">(\\s*|\n|\t|\r)<");
        Matcher m = p.matcher(result);
        result = m.replaceAll("><");

        return result;
    }

    public static String convertToXmlService(Object obj, String encoding) throws Exception {
        String result = null;

        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        // 指定是否使用换行和缩排对已编组 XML 数据进行格式化的属性名称。
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        /*marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);*/
        /*marshaller.setProperty("com.sun.xml.bind.xmlHeaders",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");*/
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        /*result = writer.toString().replace("standalone=\"yes\"", "");*/
        result = writer.toString();
        Pattern p = Pattern.compile(">(\\s*|\n|\t|\r)<");
        Matcher m = p.matcher(result);
        result = m.replaceAll("><");

        return result;
    }


}
