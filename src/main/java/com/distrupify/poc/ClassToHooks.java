package com.distrupify.poc;

import com.distrupify.resources.SalesResource;
import jakarta.ws.rs.Path;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Optional;

public class ClassToHooks {
    // windows run: mvn clean install exec:java "-Dexec.mainClass=com.distrupify.poc.ClassToHooks"
    public static void main(String[] args) {
        Class<?> resourceClass = SalesResource.class;
        System.out.println(resourceClass.getName());

        final var classPathAnnotation = resourceClass.getAnnotation(Path.class);

        for (Method method : resourceClass.getDeclaredMethods()) {
            System.out.println("\t" + method.getName());
            final var methodPathAnnotation = method.getAnnotation(Path.class);
            if (methodPathAnnotation != null) {
                System.out.println("\t\t" + classPathAnnotation.value() + methodPathAnnotation.value());
            } else {
                System.out.println("\t\t" + classPathAnnotation.value());
            }

            for (Parameter parameter : method.getParameters()) {
                final var primitiveTypescriptType = convertToTypescriptPrimitive(parameter.getType());
                if (primitiveTypescriptType.isPresent()) {
                    System.out.println("\t\t" + getTypescriptField(parameter.getName(), primitiveTypescriptType.get()));
                } else {
                    System.out.println("\t\t" + getTypescriptField(parameter.getName(), getCustomType(parameter.getType())));
                }

            }
        }
    }

    private static String getCustomType(Class<?> customType) {
        final var split = customType.getName().split("\\.");
        return Arrays.stream(split).skip(split.length - 1).findFirst().orElse("");
    }

    private static String getTypescriptField(String name, String value) {
        return name + ": " + value + ";";
    }

    private static Optional<String> convertToTypescriptPrimitive(Class<?> parameterType) {
        if (parameterType.equals(byte.class) ||
                parameterType.equals(java.lang.Byte.class) ||
                parameterType.equals(short.class) ||
                parameterType.equals(java.lang.Short.class) ||
                parameterType.equals(int.class) ||
                parameterType.equals(java.lang.Integer.class) ||
                parameterType.equals(long.class) ||
                parameterType.equals(java.lang.Long.class) ||
                parameterType.equals(float.class) ||
                parameterType.equals(java.lang.Float.class) ||
                parameterType.equals(double.class) ||
                parameterType.equals(java.lang.Double.class)) {
            return Optional.of("number");
        }

        if (parameterType.equals(java.lang.String.class) ||
                parameterType.equals(char.class) ||
                parameterType.equals(java.lang.Character.class)) {
            return Optional.of("string");
        }

        if (parameterType.equals(java.lang.Boolean.class)) {
            return Optional.of("boolean");
        }

        return Optional.empty();
    }
}
