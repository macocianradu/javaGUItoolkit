package parser;

import com.sun.jdi.InvalidTypeException;
import converters.Converter;
import guiTree.Helper.Debugger;
import guiTree.Visual;
import guiTree.Window;
import org.w3c.dom.*;

import javax.xml.parsers.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class XAMLParser {
    private final static String packageGuiTree = "guiTree.";
    private final static String packageComponents = "guiTree.Components.";
    private static Converter valueConverter = new Converter();

    private static void setAttributes(Object object, NamedNodeMap attributeList){
        for(int i = 0; i < attributeList.getLength(); i++){
            Node attribute = attributeList.item(i);
            String methodName = "set";
            methodName = methodName.concat(attribute.getNodeName());
            List<Object> parameterList = convertStringToPrimitives(object, attribute.getNodeValue(), methodName);
            Debugger.log("Calling " + methodName + " " + attribute.getNodeValue(), Debugger.Tag.PARSING);
            if(parameterList == null) {
                break;
            }
            Class<?>[] parameterTypes = new Class[parameterList.size()];
            for(int index = 0; index < parameterList.size(); index++){
                parameterTypes[index] = parameterList.get(index).getClass();
            }
            try {
                Method method = object.getClass().getMethod(methodName, parameterTypes);
                assert method != null;
                method.invoke(object, parameterList.toArray());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<Method> getMethodsFromName(Object object, String methodName){
       Method[] methods = object.getClass().getMethods();
       List<Method> returnMethods = new ArrayList<>();
       for(Method method: methods){
           if(method.getName().equals(methodName)){
               returnMethods.add(method);
           }
       }
       return returnMethods;
    }

    public static Window parse(String filepath) throws Exception {
        Object rootObject;
        Debugger.log("Started", Debugger.Tag.PARSING);
        FileInputStream fileIS = new FileInputStream(new File("resources/" + filepath));
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document xmlDocument = builder.parse(fileIS);

        xmlDocument.normalize();

        Element rootNode = xmlDocument.getDocumentElement();
        Debugger.log("Root element:" + rootNode.toString(), Debugger.Tag.PARSING);

        rootObject = parseNode(rootNode);

        if(rootObject instanceof Window) {
            ((Window) rootObject).repaint();
            Thread windowThread = new Thread((Window) rootObject);
            windowThread.setName("Painting Thread");
            windowThread.start();
            return (Window) rootObject;
        }
        return null;
    }

    private static List<Object> convertStringToPrimitives(Object object, String value, String methodName){
        List<Object> primitiveAttributes = new ArrayList<>();
        List<String> values = new ArrayList<>();

        while (value.contains(",")) {
            values.add(value.substring(0, value.indexOf(',')));
            value = value.substring(value.indexOf(',') + 1);
        }
        values.add(value);
        List<Method> methods = getMethodsFromName(object, methodName);
        if(methods.size() == 0) {
            System.out.println("Could not find method " + methodName);
        }
        for(Method method: methods){
            Class<?>[] types = method.getParameterTypes();
            for(int i = 0; i < types.length; i++){
                try{
                    primitiveAttributes.add(valueConverter.objectCreatorFactory(types[i], values.get(i)));
                } catch (InvalidTypeException | NumberFormatException e){
                    primitiveAttributes.clear();
                    break;
                }
            }
            if(primitiveAttributes.size() == types.length && types.length == values.size()){
                return primitiveAttributes;
            }
        }
        System.err.println("Could not find method " + methodName + " with parameters " + values);
        return null;
    }

    private static Object parseNode(Node parentNode)throws Exception{
        Class<?> parentClass;
        try {
            parentClass = Class.forName(packageComponents.concat(parentNode.getNodeName()));
        }
        catch (ClassNotFoundException e) {
            parentClass = Class.forName(packageGuiTree.concat(parentNode.getNodeName()));
        }
        Debugger.log("Parsing " + parentClass, Debugger.Tag.PARSING);
        Object parentObject = parentClass.getDeclaredConstructor().newInstance();
        Debugger.log("Constructor called succesfuly for " + parentObject, Debugger.Tag.PARSING);

        setAttributes(parentObject, parentNode.getAttributes());

        NodeList childList = parentNode.getChildNodes();

        for(int index = 0; index < childList.getLength(); index++){
            Node childNode = childList.item(index);

            if(childNode.getNodeType() == Node.ELEMENT_NODE) {

                Object childObject = parseNode(childNode);

                if(parentObject instanceof Visual && childObject instanceof Visual) {
                    Debugger.log("Adding " + childObject + " to " + parentObject, Debugger.Tag.PARSING);
                    addVisual((Visual) parentObject, (Visual) childObject);
                }
            }
        }
        return parentObject;
    }

    private static void addVisual(Visual parentObject, Visual childObject){
        parentObject.addVisual(childObject);
    }
}
