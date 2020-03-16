package parser;

import com.sun.jdi.InvalidTypeException;
import converters.Converter;
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

    private static Method getMethod(Object object, String methodName, List<Object> parameterList){
        Method method;
        Class<?>[] args = new Class[parameterList.size()];
        for(Object o: parameterList){
            try {
                args[parameterList.indexOf(o)] = o.getClass();
            } catch (NullPointerException e) {
                System.err.println("Null Pointer Exception: " + methodName + " with parameters + " + parameterList.toString());
            }
        }
        try {
            method = object.getClass().getMethod(methodName, args);
            return method;
        } catch (NoSuchMethodException e) {
            System.err.println("Method does not exist: " + methodName + " with parameters + " + parameterList.toString());
            e.printStackTrace();
        }
        return null;
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
        FileInputStream fileIS = new FileInputStream(new File("resources/" + filepath));
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document xmlDocument = builder.parse(fileIS);

        xmlDocument.normalize();

        Element rootNode = xmlDocument.getDocumentElement();

        rootObject = parseNode(rootNode);

        if(rootObject instanceof Window) {
            return (Window) rootObject;
        }
        return null;
    }

    private static List<Object> convertStringToPrimitives(Object object, String value, String methodName){
        List<Object> primitiveAttributes = new ArrayList<>();
        List<String> values = new ArrayList<>();

        value = value.replaceAll(" ", "");
        while (value.contains(",")) {
            values.add(value.substring(0, value.indexOf(',')));
            value = value.substring(value.indexOf(',') + 1);
        }
        values.add(value);
        List<Method> methods = getMethodsFromName(object, methodName);
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
            if(primitiveAttributes.size() == types.length){
                return primitiveAttributes;
            }
        }
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
        Object parentObject = parentClass.getDeclaredConstructor().newInstance();

        setAttributes(parentObject, parentNode.getAttributes());

        NodeList childList = parentNode.getChildNodes();

        for(int index = 0; index < childList.getLength(); index++){
            Node childNode = childList.item(index);

            if(childNode.getNodeType() == Node.ELEMENT_NODE) {

                Object childObject = parseNode(childNode);

                if(parentObject instanceof Visual && childObject instanceof Visual) {
                    addVisual((Visual) parentObject, (Visual) childObject);
                }
                System.out.println("\nCurrent Element :" + childNode.getNodeName());
            }
        }
        return parentObject;
    }

    private static void addVisual(Visual parentObject, Visual childObject){
        parentObject.addVisual(childObject);
    }
}
