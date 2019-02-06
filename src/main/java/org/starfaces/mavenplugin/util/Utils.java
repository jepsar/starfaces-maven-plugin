/*
 * Copyright (c) Jasper de Vries.
 */
package org.starfaces.mavenplugin.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.starfaces.annotations.Attribute;

/**
 * Utility methods.
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 */
public final class Utils {

  /**
   * Hide public constructor.
   */
  private Utils() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Returns class name for class path in base path.
   *
   * @param basePath  Base path.
   * @param classPath Class path (should end with {@code .class} and start with base path).
   *
   * @return Class name for class path in base path.
   */
  public static String className(Path basePath, Path classPath) {
    return className(basePath.toString(), classPath.toString());
  }

  /**
   * Returns class name for class path in base path.
   *
   * @param basePath  Base path.
   * @param classPath Class path (should end with {@code .class} and start with base path).
   *
   * @return Class name for class path in base path.
   */
  public static String className(String basePath, String classPath) {
    if (!classPath.endsWith(".class")) {
      throw new IllegalArgumentException("classPath should end with '.class'");
    }
    if (!classPath.startsWith(basePath)) {
      throw new IllegalArgumentException("classPath should start with basePath");
    }
    int basePathLength = basePath.length();
    int classPathLength = classPath.length();
    return classPath.substring(basePathLength + 1, classPathLength - 6).replace(File.separatorChar, '.');
  }

  /**
   * Returns class names in path.
   *
   * @param path     Path to get class names from.
   * @param maxDepth the maximum number of directory levels to search.
   *
   * @return Class names in path.
   *
   * @throws IOException If an I/O error is thrown when accessing provided path.
   */
  public static Stream<String> classNames(Path path, int maxDepth) throws IOException {
    return Files.find(path, maxDepth, (p, a) -> Utils.isClass(p))
            .map(p -> Utils.className(path, p));
  }

  /**
   * Returns the first object that is not {@code null} or the last object.
   *
   * @param <T>     Type of object.
   * @param object1 Object to return if not {@code null}.
   * @param object2 Object to return if first object is {@code null}.
   *
   * @return The first object that is not {@code null} or the last object.
   */
  public static <T> T coalesce(T object1, T object2) {
    return object1 == null ? object2 : object1;
  }

  /**
   * Returns {@code true} if path is a Java class.
   *
   * @param path Path to check.
   *
   * @return {@code true} if path is a Java class.
   */
  public static boolean isClass(Path path) {
    return path.toString().endsWith(".class");
  }

  /**
   * Returns provided string with first character converted to lower case.
   *
   * @param string String to convert.
   *
   * @return Provided string with first character converted to lower case.
   */
  public static String lowerCaseFirst(String string) {
    if (string == null) {
      return null;
    }
    if (string.length() < 2) {
      return string.toLowerCase();
    }
    return string.substring(0, 1).toLowerCase() + string.substring(1);
  }

  /**
   * Returns the provided string if it's not {@code null} or empty, else {@code null}.
   *
   * @param string String to return.
   *
   * @return The provided string if it's not {@code null} or empty, else {@code null}.
   */
  public static String nullIfEmpty(String string) {
    if (string == null || string.isEmpty()) {
      return null;
    }
    return string;
  }

  /**
   * Returns map of property name and attribute annotations. If a property is not annotated with {@link Attribute} it
   * will not be included in the map.
   *
   * @param classLoader Class loader.
   * @param clss        Class to get property keys from.
   *
   * @return Map of property name and attribute annotations.
   */
  public static Map<String, Attribute> propertyKeys(ClassLoader classLoader, Class<?> clss) {
    try {
      Class<?> propertyKeys = classLoader.loadClass(clss.getName() + "$PropertyKeys");
      Map<String, Attribute> map = new HashMap<>();
      for (Enum<?> property : (Enum[]) propertyKeys.getEnumConstants()) {
        Attribute attribute = propertyKeyAttribute(propertyKeys, property);
        if (attribute != null) {
          map.put(property.name(), attribute);
        }
      }
      return map;
    }
    catch (ClassNotFoundException ex) {
      return null;
    }
  }

  /**
   * Returns attribute annotation from attribute enumeration entry or {@code null} if not found.
   *
   * @param propertyKeys  Property keys enumeration.
   * @param attributeName Attribute name (enumeration entry) to read annotation from.
   *
   * @return Attribute annotation from attribute enumeration entry or {@code null} if not found.
   */
  private static Attribute propertyKeyAttribute(Class<?> propertyKeys, Enum<?> attributeName) {
    try {
      return propertyKeys.getField(attributeName.name()).getAnnotation(Attribute.class);
    }
    catch (NoSuchFieldException ex) {
      return null;
    }
    catch (SecurityException ex) {
      throw new IllegalStateException(ex);
    }
  }

  /**
   * Returns input stream of resource.
   *
   * @param resource Resource name to get input stream for.
   *
   * @return Input stream of resource.
   */
  public static InputStream resourceStream(String resource) {
    return Utils.class.getResourceAsStream(resource);
  }

  /**
   * Returns path converted to URL.
   *
   * @param path Path to convert to URL.
   *
   * @return Path converted to URL.
   */
  public static URL toURL(Path path) {
    try {
      return path.toUri().toURL();
    }
    catch (MalformedURLException ex) {
      throw new IllegalStateException(ex);
    }
  }

  /**
   * Returns path converted to URL.
   *
   * @param path Path to convert to URL.
   *
   * @return Path converted to URL.
   */
  public static URL toURL(String path) {
    return toURL(Paths.get(path));
  }

}
