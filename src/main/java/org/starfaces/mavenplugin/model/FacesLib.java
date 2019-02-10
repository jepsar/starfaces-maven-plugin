/*
 * Copyright (c) Jasper de Vries.
 */
package org.starfaces.mavenplugin.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.faces.component.UIComponent;
import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 */
public class FacesLib {

  /**
   * Class loader.
   */
  private final ClassLoader classLoader;

  /**
   * Plugin configuration.
   */
  private final Xpp3Dom pluginConfig;

  /**
   * Library components.
   */
  private final List<LibComponent> components = new ArrayList<>();

  /**
   * Constructs with class loader and plugin configuration.
   *
   * @param classLoader  Sets {@link #classLoader}.
   * @param pluginConfig Sets {@link #pluginConfig}.
   */
  public FacesLib(ClassLoader classLoader, Xpp3Dom pluginConfig) {
    this.classLoader = classLoader;
    this.pluginConfig = pluginConfig;
  }

  /**
   * Add component.
   *
   * @param clss {@link UIComponent} to add.
   */
  public void addComponent(Class<UIComponent> clss) {
    components.add(new LibComponent(this, clss));
  }

  /**
   * Sorts {@link #components}.
   */
  public void sortComponents() {
    Collections.sort(components);
  }

  /**
   * Returns {@link #classLoader}.
   *
   * @return {@link #classLoader}.
   */
  public ClassLoader getClassLoader() {
    return classLoader;
  }

  /**
   * Returns {@link #pluginConfig}.
   *
   * @return {@link #pluginConfig}.
   */
  public Xpp3Dom getPluginConfig() {
    return pluginConfig;
  }

  /**
   * Returns {@link #components}.
   *
   * @return {@link #components}.
   */
  public List<LibComponent> getComponents() {
    return Collections.unmodifiableList(components);
  }

  /**
   * @return {@inheritDoc }
   */
  @Override
  public String toString() {
    return "FacesLib{" + "components=" + components + '}';
  }

}
