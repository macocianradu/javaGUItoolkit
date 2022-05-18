package parser;

import guiTree.Components.Decorations.Decoration;
import parser.converters.Converter;
import guiTree.Helper.Debugger;
import guiTree.Visual;
import guiTree.Window;
import org.w3c.dom.*;
import parser.converters.ConverterInterface;

import javax.xml.parsers.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class XMLParser {
    private static Map<String, Class<?>> classMap;
    private static Converter valueConverter = new Converter();

    private static void setAttributes(Object object, NamedNodeMap attributeList){
        for(int i = 0; i < attributeList.getLength(); i++){
            Node attribute = attributeList.item(i);
            String methodName = "set";
            methodName = methodName.concat(attribute.getNodeName());

            List<Method> methods = getMethodsFromName(object, methodName);
            String value = attribute.getNodeValue();
            if(methods.size() == 0) {
                String firstAttribute = methodName.substring(3).toLowerCase();
                methodName = "setAttribute";
                methods = getMethodsFromName(object, methodName);

                value = firstAttribute.concat(",").concat(value);
            }

            List<Object> parameterList = convertStringToPrimitives(value, methods);
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

    public static Visual parse(String filepath) throws Exception {
        Object rootObject;
        Debugger.log("Started", Debugger.Tag.PARSING);
        InputStream fileIS = XMLParser.class.getClassLoader().getResourceAsStream(filepath);

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Class<?>[] classes = getClasses();
        classMap = createClassTable(classes);
        Document xmlDocument = builder.parse(fileIS);

        xmlDocument.normalize();

        Element rootNode = xmlDocument.getDocumentElement();
        Debugger.log("Root element:" + rootNode.toString(), Debugger.Tag.PARSING);

        rootObject = parseNode(rootNode);

        return (Visual)rootObject;
    }

    private static List<Object> convertStringToPrimitives(String value, List<Method> methods){
        List<Object> primitiveAttributes = new ArrayList<>();
        List<String> values = new ArrayList<>();

        while (value.contains(",")) {
            values.add(value.substring(0, value.indexOf(',')));
            value = value.substring(value.indexOf(',') + 1);
        }
        values.add(value);

        for(Method method: methods){
            Class<?>[] types = method.getParameterTypes();
            if(types.length == values.size()) {
                for (int i = 0; i < types.length; i++) {
                    try {
                        primitiveAttributes.add(valueConverter.objectCreatorFactory(types[i], values.get(i)));
                    } catch (InvalidClassException | NumberFormatException e) {
                        primitiveAttributes.clear();
                        break;
                    }
                }
                if (primitiveAttributes.size() == types.length && types.length == values.size()) {
                    return primitiveAttributes;
                }
            }
        }
        System.err.println("Could not find method " + methods.get(0).getName() + " with parameters " + values);
        return null;
    }

    private static Object parseNode(Node parentNode)throws Exception{
        Class<?> parentClass = classMap.get(parentNode.getNodeName());

        Debugger.log("Parsing " + parentClass, Debugger.Tag.PARSING);
        Object parentObject = parentClass.getDeclaredConstructor().newInstance();
        Debugger.log("Constructor called successfully for " + parentObject, Debugger.Tag.PARSING);

        setAttributes(parentObject, parentNode.getAttributes());

        NodeList childList = parentNode.getChildNodes();

        for(int index = 0; index < childList.getLength(); index++){
            Node childNode = childList.item(index);

            if(childNode.getNodeType() == Node.ELEMENT_NODE) {

                Object childObject = parseNode(childNode);

                if(parentObject instanceof Visual) {
                    if(childObject instanceof Decoration) {
                        Debugger.log("Adding decoration " + childObject + " to " + parentObject, Debugger.Tag.PARSING);
                        ((Visual)parentObject).addVisual((Decoration) childObject);
                    }
                    else {
                        if (childObject instanceof Visual) {
                            Debugger.log("Adding " + childObject + " to " + parentObject, Debugger.Tag.PARSING);
                            addVisual((Visual) parentObject, (Visual) childObject);
                        }
                    }
                }
            }
        }
        return parentObject;
    }

    private static Class<?>[] getClasses()
            throws ClassNotFoundException, IOException {
        URL classRoot = XMLParser.class.getProtectionDomain().getCodeSource().getLocation();
        URL projectRoot = ClassLoader.getSystemResource("");
        File dir;
        ClassLoader classLoader = XMLParser.class.getClassLoader();

        ArrayList<Class<?>> classes = new ArrayList<>();

        String path = classRoot.getPath();
        if(path.indexOf('.') > 0) {
            if (path.substring(path.lastIndexOf('.')).equals(".jar")) {
                JarFile jarFile = new JarFile(classRoot.getPath());
                Enumeration<JarEntry> jarEntries = jarFile.entries();
                while (jarEntries.hasMoreElements()) {
                    JarEntry jarEntry = jarEntries.nextElement();
                    String filePath = jarEntry.getName();
                    if (filePath.indexOf('.') < 0) {
                        continue;
                    }
                    if (filePath.substring(filePath.lastIndexOf('.')).equals(".class")) {
                        filePath = filePath.replaceAll("/", ".");
                        classes.add(Class.forName(filePath.substring(0, filePath.lastIndexOf('.'))));
                    }
                }
            }
        }
        else {
            dir = new File(classRoot.getFile());
            classes.addAll(findClasses(dir, ""));
        }
        if(!projectRoot.getPath().equals(classRoot.getPath())){
            dir = new File(projectRoot.getFile());
            classes.addAll(findClasses(dir, ""));
        }

        return classes.toArray(new Class[0]);
    }


    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        Debugger.log("Getting Classes from Directory: " + directory.getName(), Debugger.Tag.PARSING);
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if(files == null) {
            return classes;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                if(packageName.length() == 0) {
                    classes.add(Class.forName(file.getName().substring(0, file.getName().length() - 6)));
                }
                else {
                    classes.add(Class.forName(packageName.substring(1) + '.' + file.getName().substring(0, file.getName().length() - 6)));
                }
            }
        }
        return classes;
    }

    private static Map<String, Class<?>> createClassTable(Class<?>[] classes) {
        Map<String, Class<?>> map = new HashMap<>();
        for(Class<?> c: classes) {
            if(c.getName().indexOf('.') >= 0) {
                map.put(c.getName().substring(c.getName().lastIndexOf('.') + 1), c);
            }
            else {
                map.put(c.getName(), c);
            }
        }
        return map;
    }

    public static void addConverter(ConverterInterface<?> converterInterface) {
        valueConverter.addConverter(converterInterface);
    }

    private static void addVisual(Visual parentObject, Visual childObject){
        parentObject.addVisual(childObject);
    }
}
