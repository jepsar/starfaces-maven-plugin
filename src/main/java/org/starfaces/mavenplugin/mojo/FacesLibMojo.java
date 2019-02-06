/*
 * Copyright (c) Jasper de Vries.
 */
package org.starfaces.mavenplugin.mojo;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.starfaces.mavenplugin.model.FacesLib;
import org.starfaces.mavenplugin.util.Utils;

/**
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 */
abstract class FacesLibMojo extends AbstractMojo {

  /**
   * Faces library model.
   */
  private static FacesLib facesLib;

  /**
   * FreeMarker configuration.
   */
  private static final Configuration FREEMARKER_CONF;

  static {
    FREEMARKER_CONF = new Configuration(Configuration.VERSION_2_3_28);
  }

  /**
   * Returns project that triggered this plugin.
   *
   * @return Project that triggered this plugin.
   */
  abstract MavenProject getProject();

  /**
   * @return Compile class paths for the {@link #getProject() project}.
   *
   * @return Compile class paths for the {@link #getProject() project}.
   */
  abstract List<String> getCompileClassPaths();

  /**
   * Returns project's build classes path.
   *
   * @return Project's build classes path.
   */
  protected Path getBuildDirClassesPath() {
    return Paths.get(getProject().getBuild().getDirectory(), "classes");
  }

  /**
   * Returns project's build output path.
   *
   * @return Project's build output path.
   */
  protected Path getBuildDirOutPath(String... more) {
    return Paths.get(getProject().getBuild().getOutputDirectory(), more);
  }

  /**
   * Returns class loader for project's compiled classes and provided class paths.
   *
   * @return Class loader for project's compiled classes and provided class paths.
   */
  private ClassLoader getClassLoader() {
    List<URL> urls = getCompileClassPaths().stream().map(Utils::toURL).collect(Collectors.toList());
    urls.add(Utils.toURL(getBuildDirClassesPath()));
    return URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]),
                                      FacesLibMojo.class.getClassLoader());
  }

  /**
   * Classes loaded using {@link #getClassLoader() } from path {@link #getBuildDirClassesPath() } with a maximum depth
   * of 100.
   *
   * @param classLoader Class loader.
   *
   * @return Loaded classes.
   */
  private Stream<Class<?>> loadClasses(ClassLoader classLoader) {
    try {
      return Utils.classNames(getBuildDirClassesPath(), 100).map(c -> loadClass(classLoader, c));
    }
    catch (IOException ex) {
      throw new IllegalStateException(ex);
    }
  }

  /**
   * Loads class using provided class loader with provided name.
   *
   * @param classLoader Class loader to load class with.
   * @param className   Class name to load.
   *
   * @return Loaded class.
   */
  private Class<?> loadClass(ClassLoader classLoader, String className) {
    try {
      return classLoader.loadClass(className);
    }
    catch (ClassNotFoundException ex) {
      throw new IllegalStateException(ex);
    }
  }

  /**
   * Loads classes using {@link #loadClasses() }, filters them using {@link #isFacesComponent(java.lang.Class) }
   * and maps them using {@link #toUIComponentClass(java.lang.Class) }.
   *
   * @param classLoader Class loader.
   *
   * @return Stream of {@link UIComponent} classes.
   */
  private Stream<Class<UIComponent>> loadComponents(ClassLoader classLoader) {
    return loadClasses(classLoader).filter(this::isFacesComponent).map(this::toUIComponentClass);
  }

  /**
   * Returns {@code true} if the class is annotated with {@link FacesComponent}.
   *
   * @param clss Class to check for annotation.
   *
   * @return {@code true} if the class is annotated with {@link FacesComponent}.
   */
  private boolean isFacesComponent(Class<?> clss) {
    return clss.isAnnotationPresent(FacesComponent.class);
  }

  /**
   * Processes FreeMarker template to the provided writer.
   *
   * @param template FreeMarker template.
   * @param writer   Writer to write processed template output to.
   *
   * @throws IOException If any exceptions happen reading, processing or writing.
   */
  protected void process(InputStream template, OutputStreamWriter writer) throws IOException {
    Template freeMarkerTemplate = new Template(template.toString(),
                                               new InputStreamReader(template),
                                               FREEMARKER_CONF);
    try {
      freeMarkerTemplate.process(getFacesLib(), writer);
    }
    catch (TemplateException ex) {
      throw new IOException(ex);
    }
  }

  /**
   * Processes FreeMarker template to the provided writer.
   *
   * @param template FreeMarker template.
   * @param writer   Writer to write processed template output to.
   *
   * @throws IOException If any exceptions happen reading, processing or writing.
   */
  protected void process(InputStream template, Path destination) throws IOException {
    try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(destination.toFile()))) {
      process(template, writer);
    }
  }

  /**
   * Log path that was processed.
   *
   * @param path Path to log.
   */
  protected void logProcessed(Path path) {
    getLog().info(String.format("Processed: %s", path.toString()));
  }

  /**
   * Casts class to class of type {@link UIComponent}.
   *
   * @param clss Class to cast.
   *
   * @return Casted class.
   */
  @SuppressWarnings("unchecked")
  private Class<UIComponent> toUIComponentClass(Class<?> clss) {
    return (Class<UIComponent>) clss;
  }

  /**
   * @return {@link #facesLib} with will be create if {@code null}.
   *
   * @return {@link #facesLib}.
   */
  public FacesLib getFacesLib() {
    if (facesLib == null) {
      facesLib = new FacesLib(getClassLoader());
      loadComponents(facesLib.getClassLoader()).forEach(facesLib::addComponent);
      facesLib.sortComponents();
    }
    return facesLib;
  }

}
