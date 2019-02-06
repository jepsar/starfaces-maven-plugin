/*
 * Copyright (c) Jasper de Vries.
 */
package org.starfaces.mavenplugin.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.faces.component.UIComponent;

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
   * Library components.
   */
  private final List<LibComponent> components = new ArrayList<>();

  /**
   * Constructs with class loader.
   *
   * @param classLoader Sets {@link #classLoader}.
   */
  public FacesLib(ClassLoader classLoader) {
    this.classLoader = classLoader;
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
