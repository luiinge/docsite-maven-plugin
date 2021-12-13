package docsite.mojo;

import java.io.*;
import java.util.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public final class XmlUtil {

    private XmlUtil() {    }


    private static class DataCollector extends DefaultHandler {

        private final StringBuilder buffer = new StringBuilder();
        private final TreeMap<String, Object> result = new TreeMap<>();
        private TreeMap<String,Object> current = result;




        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            String value = buffer.toString().trim();
            System.err.println(String.format("endElement qName=%s value=%s",qName,value));
            if (value.length() > 0) {
                current.put(qName, value);
            }
            buffer.setLength(0);
        }


        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            buffer.append(ch, start, length);
        }

    }


    public static Map<String, Object> parse(InputStream inputStream) throws IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            Document document = factory.newDocumentBuilder().parse(inputStream);
            Map<String, Object> map = documentToMap(document);
            return map;
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
            //System.err.printf("%s property %s = %s%n", "-".repeat(level), key, value);

        } else if (isRegularPropertyNode(node)) {
            Element element = (Element) node;
            String key = element.getTagName();
            String value = children.item(0).getNodeValue();
            map.put(key,value);
            //System.err.printf("%s property %s = %s%n", "-".repeat(level), key, value);

        } else if (isListPropertyNode(node)) {
            Element element = (Element) node;
            String key = element.getTagName();
            List<Object> value = new ArrayList<>();
            if (list != null) {
                list.add(value);
            } else {
                map.put(key, value);
            }
            //System.err.printf("%s property list %s: %n", "-".repeat(level), key);
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                processNode(node.getChildNodes().item(i), new TreeMap<>(), value, level+1);
            }
        } else if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String key = element.getTagName();
            TreeMap<String,Object> value = new TreeMap<>();
            if (list != null) {
                list.add(value);
            } else {
                map.put(key, value);
            }
            for (int i = 0; i < element.getAttributes().getLength(); i++) {
                Attr attribute = (Attr) element.getAttributes().item(i);
                value.put(attribute.getName(), attribute.getValue());
            }
            //System.err.println("-".repeat(level)+" element "+element.getTagName());
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                processNode(node.getChildNodes().item(i), value, null, level+1);
            }
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
