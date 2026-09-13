package fr.fortytwo.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

public class App {

    final static private String PACKAGE_NAME = "fr.fortytwo.reflection.classes";

    private static Scanner scanner;
    private static Class<?> clazz;
    private static Field[] fields;
    private static Method[] methods;
    private static Object createdObject;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        final String classesPrompt = """
                Classes:
                User
                Car
                ---------------------
                Enter class name:
                -> """;
        final String createObjectPrompt = """
                ---------------------
                Let's create an object.""";

        try {

            System.out.print(classesPrompt);
            final String className = scanner.nextLine().trim();

            displayClassMetaData(className);

            System.out.println(createObjectPrompt);
            final Object newObject = createObject();
            if (newObject != null) {
                System.out.println("Object created: " + newObject.toString());
                updateObject(newObject);
                callMethod(newObject);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    private static void displayClassMetaData(final String className) throws ClassNotFoundException {
        System.out.println("---------------------");
        clazz = Class.forName(PACKAGE_NAME + "." + className);
        fields = clazz.getDeclaredFields();
        System.out.println("fields:");
        for (Field field : fields) {
            System.out.println("\t" + field.getType().getSimpleName() + " " + field.getName());
        }
        methods = clazz.getDeclaredMethods();
        System.out.println("methods:");
        for (Method method : methods) {
            System.out.print("\t" + method.getReturnType().getSimpleName() + " " + method.getName());
            final Parameter[] methodParameters = method.getParameters();
            final StringJoiner joiner = new StringJoiner(", ", "(", ")");
            for (Parameter parameter : methodParameters) {
                joiner.add(parameter.getType().getSimpleName());
            }
            System.out.println(joiner);
        }
    }

    private static Object createObject() {
        final Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == fields.length) {
                List<Object> constructorParameters = new ArrayList<>();
                for (Field field : fields) {
                    System.out.println(field.getName() + ":");
                    System.out.print("-> ");
                    Object parameterValue = readExactType(field.getType().getSimpleName());
                    if (parameterValue != null)
                        constructorParameters.add(parameterValue);
                }
                try {
                    createdObject = constructor.newInstance(constructorParameters.toArray());
                    if (createdObject != null)
                        return createdObject;
                } catch (final ReflectiveOperationException ex) {
                    System.err.println("Unable to create an Object! cause: " + ex.getMessage());
                }
            }
        }
        return null;
    }
    
    private static void updateObject(Object obj) throws ReflectiveOperationException {
        System.out.println("---------------------");
        System.out.println("Enter name of the field for changing:");
        System.out.print("-> ");
        String fieldName = scanner.nextLine().trim();
        
        Field fieldToUpdate = null;
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                fieldToUpdate = field;
                break;
            }
        }
        
        if (fieldToUpdate != null) {
            fieldToUpdate.setAccessible(true);
            System.out.println("Enter " + fieldToUpdate.getType().getSimpleName() + " value:");
            System.out.print("-> ");
            Object value = readExactType(fieldToUpdate.getType().getSimpleName());
            fieldToUpdate.set(obj, value);
            System.out.println("Object updated: " + obj.toString());
        } else {
            System.out.println("Field not found.");
        }
    }

    private static void callMethod(Object obj) throws ReflectiveOperationException {
        System.out.println("---------------------");
        System.out.println("Enter name of the method for call:");
        System.out.print("-> ");
        String methodNameWithArgs = scanner.nextLine().trim();
        
        String methodName = methodNameWithArgs.contains("(") 
                ? methodNameWithArgs.substring(0, methodNameWithArgs.indexOf("(")) 
                : methodNameWithArgs;
                
        Method methodToCall = null;
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                StringJoiner sj = new StringJoiner(",", "(", ")");
                for (Parameter p : method.getParameters()) {
                    sj.add(p.getType().getSimpleName());
                }
                String sig = method.getName() + sj.toString();
                if (methodNameWithArgs.equals(sig) || methodNameWithArgs.equals(method.getName())) {
                    methodToCall = method;
                    break;
                }
            }
        }
        
        if (methodToCall != null) {
            methodToCall.setAccessible(true);
            Parameter[] parameters = methodToCall.getParameters();
            List<Object> args = new ArrayList<>();
            for (Parameter parameter : parameters) {
                System.out.println("Enter " + parameter.getType().getSimpleName() + " value:");
                System.out.print("-> ");
                args.add(readExactType(parameter.getType().getSimpleName()));
            }
            
            Object result = methodToCall.invoke(obj, args.toArray());
            
            if (!methodToCall.getReturnType().getSimpleName().equals("void")) {
                System.out.println("Method returned:");
                System.out.println(result);
            }
        } else {
            System.out.println("Method not found.");
        }
    }

    private static Object readExactType(final String fieldType) {
        String input = scanner.nextLine().trim();
        switch (fieldType) {
            case "int":
            case "Integer":
                return Integer.parseInt(input);
            case "String":
                return input;
            case "double":
            case "Double":
                return Double.parseDouble(input);
            case "long":
            case "Long":
                return Long.parseLong(input);
            case "boolean":
            case "Boolean":
                return Boolean.parseBoolean(input);
            default:
                return input;
        }
    }

}
