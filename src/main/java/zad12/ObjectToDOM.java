package zad12;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.lang.reflect.Field;

/**
 * Created by tomasz.lelek on 18/03/15.
 */
public class ObjectToDOM implements ObjectToDOMInterface {

    private Document doc;

    @Override
    public Document getDocument(Object o) {
        Class<?> c = o.getClass();

        createDocument();

        Element root = doc.createElement("object");
        Element name = doc.createElement("className");
        Text className = doc.createTextNode(c.getSimpleName());
        Element state = doc.createElement("classState");
        name.appendChild(className);
        root.appendChild(name);


        forObject(o, c, root, state);

        doc.appendChild(root);
        return doc;
    }

    private void createDocument() {
        DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
        DocumentBuilder docB = null;
        try {
            docB = docBF.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        doc = docB.newDocument();
    }

    private void forObject(Object o, Class<?> c, Element root, Element state) {
        Field[] fields = c.getFields();
        forFields(o, c, root, state, fields);
    }

    private void forFields(Object o, Class<?> c, Element root, Element state, Field[] fields) {
        for (Field f : fields) {
            try {
                if (isFieldNotObject(f)) {
                    Element primitiveField = createPrimitiveField(o, f);
                    state.appendChild(primitiveField);
                    root.appendChild(state);
                } else {
                    Element elementName = doc.createElement(f.getName());
                    Element nameO = doc.createElement("className");
                    Attr atrI = doc.createAttribute("type");
                    atrI.setNodeValue("Object");
                    String[] split = f.getType().getCanonicalName().split("\\.");
                    Text classNameO = doc.createTextNode(split[split.length - 1]);
                    nameO.appendChild(classNameO);
                    elementName.setAttributeNode(atrI);


                    Element stateO = doc.createElement("classState");
                    elementName.appendChild(nameO);
                    root.appendChild(elementName);
                    state.appendChild(elementName);
                    forFields(f.get(o), f.getClass(), elementName, stateO, f.get(o).getClass().getFields());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            System.out.println(f);
        }
    }

    private boolean isFieldNotObject(Field f) {
        return f.getType().isPrimitive() || f.getType().equals(String.class) || f.getType().getPackage().toString().contains("java.lang");
    }

    private Element createPrimitiveField(Object o, Field f) throws IllegalAccessException {
        Element elementName = doc.createElement(f.getName());
        Text elementValue = doc.createTextNode(String.valueOf(f.get(o)));
        Attr atrI = doc.createAttribute("type");
        atrI.setNodeValue(String.valueOf(f.getType().getSimpleName()));
        elementName.appendChild(elementValue);
        elementName.setAttributeNode(atrI);
        return elementName;
    }
}

