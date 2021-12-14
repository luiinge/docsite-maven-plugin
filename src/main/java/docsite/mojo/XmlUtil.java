package docsite.mojo;

import java.io.*;
import java.util.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public final class XmlUtil {

    private XmlUtil() {    }


        public static Map<String, Object> parse(InputStream inputStream) throws IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            Document document = factory.newDocumentBuilder().parse(inputStream);
            return documentToMap(document);
        } catch (SAXException | ParserConfigurationException e) {
            throw new IOException(e);
        }
    }


    private static Map<String, Object> documentToMap(Document document) {
        TreeMap<String,Object> map = new TreeMap<>();
        Element node = document.getDocumentElement();
        processNode(node, map, null, 1);
        return map;
    }


    private static void processNode(Node node, Map<String, Object> map, List<Object> list, int level) {

        NodeList children = node.getChildNodes();
        if (isEmptyPropertyNode(node)) {
            Element element = (Element) node;
            String key = element.getTagName();
            map.put(key,"");
        } else if (isRegularPropertyNode(node)) {
            Element element = (Element) node;
            String key = element.getTagName();
            String value = children.item(0).getNodeValue();
            map.put(key,value);
        } else if (isListPropertyNode(node)) {
            Element element = (Element) node;
            String key = element.getTagName();
            List<Object> value = new ArrayList<>();
            addToContainer(map, list, key, value);
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                processNode(node.getChildNodes().item(i), new TreeMap<>(), value, level+1);
            }
        } else if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String key = element.getTagName();
            TreeMap<String,Object> value = new TreeMap<>();
            addToContainer(map, list, key, value);
            for (int i = 0; i < element.getAttributes().getLength(); i++) {
                Attr attribute = (Attr) element.getAttributes().item(i);
                value.put(attribute.getName(), attribute.getValue());
            }
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                processNode(node.getChildNodes().item(i), value, null, level+1);
            }
        }
    }


    private static void addToContainer(
        Map<String, Object> map,
        List<Object> list,
        String key,
        Object value
    ) {
        if (list != null) {
            list.add(value);
        } else {
            map.put(key, value);
        }
    }


    private static boolean isRegularPropertyNode(Node node) {
        return
            node.getNodeType() == Node.ELEMENT_NODE &&
                node.getChildNodes().getLength() == 1 &&
                node.getChildNodes().item(0).getNodeType() == Node.TEXT_NODE;
    }

    private static boolean isEmptyPropertyNode(Node node) {
        return
            node.getNodeType() == Node.ELEMENT_NODE &&
            node.getChildNodes().getLength() == 0 &&
            node.getAttributes().getLength() == 0;
    }


    private static boolean isListPropertyNode(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE && node.hasChildNodes()) {
            String childTag = "";
            int count = 0;
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                Node child = node.getChildNodes().item(i);
                if (child.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                count++;
                if (childTag.isEmpty()) {
                    childTag = child.getNodeName();
                } else if (!childTag.equals(child.getNodeName())) {
                    return false;
                }
            }
            return count > 1 && !childTag.isEmpty();
        }
        return false;
    }

}
