package hello.utils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlManipulator {

    private final File xmlFile;
    private WeakReference<Document> docRef = new WeakReference<>(null);

    public XmlManipulator(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    /**
     * Search in document nodes by specified hierarchy path and iterates over
     * them with handler
     *
     * For example: For path 'a/b/c' and document like
     *
     * <pre>
     * &lt;a&gt;
     *   &lt;b&gt;
     *     &lt;c/&gt;
     *     &lt;c/&gt;
     *     &lt;c/&gt;
     *   &lt;/b&gt;
     * &lt;/a&gt;
     * </pre>
     *
     * will iterate over all &lt;c/&gt; nodes
     *
     * @param entryPath
     *            defines hierarchy path of nodes in document
     * @param handler
     *            node consumer
     *
     * @throws Exception
     */
    public void walkNodes(String entryPath, Consumer<Node> handler) throws Exception {
        walkNodes(getDoc(), entryPath, handler, false);
    }

    /**
     * Saves the document with all applied changes
     *
     * @throws XmlManipulateException
     */
    public void saveDocument() throws XmlManipulateException {
        try {
            XmlUtils.saveXmlDocument(getDoc(), xmlFile);
        } catch (TransformerException e) {
            throw new XmlManipulateException(e);
        }
    }

    /**
     * Lookup for node specified by hierarchy path and and sets its content
     * value
     *
     * @throws Exception
     */
    public void setNodeValue(String nodePath, final String value) throws Exception {
        walkNodes(getDoc(), nodePath, (Consumer<Node>) (node -> {
            node.setTextContent(value);
        }), true);

    }

    /**
     * Creates or adds xml node with specified hierarchy path with given content
     * and attributes
     *
     * For example addNode("a/b/c", "x", "a", "b") will create
     *
     * <pre>
     * &lt;a&gt;
     *   &lt;b&gt;
     *     &lt;c a="b"&gt;x&lt;/c&gt;
     *   &lt;/b&gt;
     * &lt;/a&gt;
     * </pre>
     *
     * @param entryPath
     * @param value
     * @param attrNamesAndValues
     * @throws Exception
     */
    public void addNode(String entryPath, String value, String... attrNamesAndValues) throws Exception {
        Node node = createNodes(getDoc(), entryPath);
        if (null != value) {
            node.setTextContent(value);
        }
        NamedNodeMap attrMap = node.getAttributes();
        for (int i = 0; i < attrNamesAndValues.length; i += 2) {
            Node attr = attrMap.getNamedItem(attrNamesAndValues[i]);
            if (null == attr) {
                attr = getDoc().createAttribute(attrNamesAndValues[i]);
            }
            attr.setNodeValue(attrNamesAndValues[i + 1]);
            attrMap.setNamedItem(attr);
        }
    }

    private Document getDoc() throws XmlManipulateException {
        if (null == docRef.get()) {
            try {
                docRef = new WeakReference<>(XmlUtils.readXmlFile(xmlFile));
            } catch (DOMException | ParserConfigurationException | SAXException | IOException e) {
                throw new XmlManipulateException(e);
            }
        }
        return docRef.get();
    }

    private void walkNodes(Node parentNode, String entryPath, Consumer<Node> handler, boolean create) throws XmlManipulateException {
        final String[] nodeNames = entryPath.split("/", 2);
        List<Node> nodeList = getChildNodes(parentNode, nodeNames[0]);
        if (nodeList.isEmpty() && create) {
            Node child = getDoc().createElement(nodeNames[0]);
            parentNode.appendChild(child);
            nodeList.add(child);
        }
        for (Node node : nodeList) {
            if (nodeNames.length == 1) {
                handler.accept(node);
            } else {
                walkNodes(node, nodeNames[1], handler, create);
            }
        }
    }

    private Node createNodes(Node parentNode, String entryPath) throws Exception {
        final String[] nodeNames = entryPath.split("/", 2);
        List<Node> nodeList = getChildNodes(parentNode, nodeNames[0]);
        if (nodeList.isEmpty() || nodeNames.length == 1) {
            Node child = getDoc().createElement(nodeNames[0]);
            parentNode.appendChild(child);
            nodeList.add(child);
        }
        Node lastNode = null;
        for (Node node : nodeList) {
            if (nodeNames.length != 1) {
                return createNodes(node, nodeNames[1]);
            }
            lastNode = node;
        }
        return lastNode;
    }

    private List<Node> getChildNodes(Node parentNode, String name) {
        NodeList children = parentNode.getChildNodes();
        int length = children.getLength();
        List<Node> result = new ArrayList<>(length);
        for (int temp = 0; temp < length; temp++) {
            Node node = children.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE
                    && node.getNodeName().equalsIgnoreCase(name)) {
                result.add(node);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return xmlFile.toString();
    }

}
