package docsite.util;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import docsite.*;
import docsite.util.ResourceUtil;
import org.w3c.dom.*;
import org.xml.sax.*;

public final class XmlParser {

    private static final DocumentBuilder documentBuilder = documentBuilder();

    private static DocumentBuilder documentBuilder() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            return factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new DocsiteException(e);
        }
    }




    public Map<String, Object> parse(InputStream inputStream) throws IOException {
        try {
            String xml = ResourceUtil.read(inputStream).replaceAll(">[\\s]*<","><");
            Reader reader = new StringReader(xml);
            InputSource inputSource = new InputSource(reader);
            Document document = documentBuilder.parse(inputSource);
            return documentToMap(document);
        } catch (SAXException e) {
            throw new IOException(e);
        }
    }


    private Map<String, Object> documentToMap(Document document) {
        TreeMap<String,Object> map = new TreeMap<>();
        Element node = document.getDocumentElement();
        processNode(node, map, null, 1);
        return map;
    }


    private void processNode(Node node, Map<String, Object> map, List<Object> list, int level) {

        if (!isElement(node)) {
            return;
        }

        Element element = (Element) node;

        // <property></property>
        if (!element.hasChildNodes() && !element.hasAttributes()) {
            addToContainer(map, list, element.getTagName(), "");
        }
        // <property>value</property>
        else if (childCount(node) == 1 && isText(child(node, 0)) && !element.hasAttributes()) {
            addToContainer(map, list, element.getTagName(), child(node, 0).getNodeValue());
        }
        // <property attr="a" ... >value</property>
        else if (childCount(node) == 1 && isText(child(node, 0)) && element.hasAttributes()) {
            TreeMap<String, Object> value = new TreeMap<>();
            addToContainer(map, list, element.getTagName(), value);
            attributes(node).forEach( attr -> value.put(attr.getName(), attr.getValue()) );
            value.put("value", child(node, 0).getNodeValue());
        }
        // <property><item>A</item><item>B</item>...</property?
        else if (isListPropertyNode(node)) {
            List<Object> value = new ArrayList<>();
            addToContainer(map, list, element.getTagName(), value);
            children(node).forEach( child -> processNode(child, new TreeMap<>(), value, level+1) );
        }
        // <entity attr="a"...><propertyA>...</propertyA><propertyB>...</propertyB> ..</entity>
        else {
            TreeMap<String, Object> value = new TreeMap<>();
            addToContainer(map, list, element.getTagName(), value);
            attributes(node).forEach( attr -> value.put(attr.getName(), attr.getValue()) );
            children(node).forEach( child -> processNode(child, value, null, level + 1) );
        }

    }


    private void addToContainer(
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



    private boolean isListPropertyNode(Node node) {
        if (isElement(node) && node.hasChildNodes()) {
            String childTag = "";
            int count = 0;
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                Node child = child(node , i);
                if (!isElement(child)) {
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


    private boolean isElement(Node node) {
        return node.getNodeType() == Node.ELEMENT_NODE;
    }


    private boolean isText(Node node) {
        return node.getNodeType() == Node.TEXT_NODE;
    }


    private int childCount(Node node) {
        return node.getChildNodes().getLength();
    }


    private Node child(Node node, int index) {
        return node.getChildNodes().item(index);
    }


    private Stream<Node> children (Node node) {
        return IntStream
            .rangeClosed(0, node.getChildNodes().getLength() - 1)
            .mapToObj(node.getChildNodes()::item);
    }


    private Stream<Attr> attributes (Node node) {
        return IntStream
            .rangeClosed(0, node.getAttributes().getLength() - 1)
            .mapToObj(node.getAttributes()::item)
            .map(Attr.class::cast);
    }


}
