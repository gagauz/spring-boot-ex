package hello.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlUtils {

    private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    public static void saveXmlDocument(Document doc, File file) throws TransformerException {
        doc.getDocumentElement().normalize();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        Result output = new StreamResult(file);
        Source input = new DOMSource(doc);
        transformer.transform(input, output);
    }

    public static void parseDocument(final String string, final String nodeName, final Consumer<Node> handler) throws Exception {
        Document doc = readXml(new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8)));
        NodeList nList = doc.getElementsByTagName(nodeName);
        int length = nList.getLength();
        for (int temp = 0; temp < length; temp++) {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                handler.accept(node);
            }
            if (nList.getLength() < length) {
                temp--;
                length = nList.getLength();
            }
        }
    }

    public static void parseDocument(final File file, final String nodeName, final Predicate<Node> handler) throws Exception {
        Document doc = readXmlFile(file);
        NodeList nList = doc.getElementsByTagName(nodeName);
        boolean save = false;
        int length = nList.getLength();
        for (int temp = 0; temp < length; temp++) {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                save |= handler.test(node);
            }
            if (nList.getLength() < length) {
                temp--;
                length = nList.getLength();
            }
        }
        if (save) {
            doc.getDocumentElement().normalize();
            saveXmlDocument(doc, file);
        }
    }

    public static void parseNode(final Node node, final String nodeName, final Consumer<Node> handler) throws Exception {
        final NodeList nList = node.getChildNodes();
        final int length = nList.getLength();
        for (int temp = 0; temp < length; temp++) {
            final Node childNode = nList.item(temp);
            if (childNode.getNodeName().equalsIgnoreCase(nodeName)) {
                handler.accept(childNode);
            } else if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                parseNode(childNode, nodeName, handler);
            }
        }
    }

    public static synchronized Document readXmlFile(final File file) throws ParserConfigurationException, SAXException, IOException {
        return readXml(new FileInputStream(file));
    }

    public static synchronized Document readXml(final InputStream stream) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(stream);
        doc.getDocumentElement().normalize();
        return doc;
    }

    public static synchronized void doWithXmlFile(File file, Predicate<Document> handler)
            throws ParserConfigurationException, SAXException, IOException, TransformerException {
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        if (handler.test(doc)) {
            saveXmlDocument(doc, file);
        }
    }
}
