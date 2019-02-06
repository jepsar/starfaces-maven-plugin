/*
 * Copyright (c) Jasper de Vries.
 */
package org.starfaces.mavenplugin.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import org.starfaces.annotations.Attribute;
import org.starfaces.annotations.Description;
import org.starfaces.mavenplugin.util.Utils;

/**
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 */
public class LibComponent implements Comparable<LibComponent> {

  /**
   * Library of this component.
   */
  private final FacesLib facesLib;

  /**
   * LibComponent class.
   */
  private final Class<UIComponent> clss;

  /**
   * Name of the attribute.
   */
  private final String name;

  /**
   * LibComponent type.
   */
  private final String type;

  /**
   * Description of the component.
   */
  private final String description;

  /**
   * LibComponent attributes.
   */
  private final List<CompAttribute> attributes = new ArrayList<>();

  /**
   * Create component.
   *
   * @param facesLib Parent.
   * @param clss     Component class.
   */
  LibComponent(FacesLib facesLib, Class<UIComponent> clss) {
    this.facesLib = facesLib;
    this.clss = clss;
    FacesComponent component = clss.getAnnotation(FacesComponent.class);
    Description descriptionAnn = clss.getAnnotation(Description.class);
    this.name = Utils.coalesce(Utils.nullIfEmpty(component.tagName()),
                               Utils.lowerCaseFirst(clss.getSimpleName()));
    this.type = component.value();
    this.description = descriptionAnn == null ? null : descriptionAnn.value();
    readAttributes();
  }

  /**
   * Read the component attributes by calling {@link #readAttributes(java.lang.Class, java.util.Map) } (passing an empty
   * map) and sort them.
   */
  private void readAttributes() {
    readAttributes(clss, new HashMap<>()).forEach((k, v) -> addAttribute(k, v));
    Collections.sort(attributes);
  }

  /**
   * Reads attributes from the provided class by calling 
   * {@link Utils#propertyKeys(java.lang.ClassLoader, java.lang.Class) } and recall this method as long as a super class
   * is found.
   *
   * @param clss         Class to read attributes from.
   * @param propertyKeys Attributes read so far.
   *
   * @return Attributes read when no parent classes are found anymore.
   */
  private Map<String, Attribute> readAttributes(Class<?> clss,
                                                Map<String, Attribute> propertyKeys) {
    Map<String, Attribute> newPropertyKeys = Utils.propertyKeys(facesLib.getClassLoader(), clss);
    if (newPropertyKeys != null) {
      newPropertyKeys.putAll(propertyKeys);
    }
    else {
      newPropertyKeys = propertyKeys;
    }
    if (clss.getSuperclass() != null) {
      return readAttributes(clss.getSuperclass(), newPropertyKeys);
    }
    else {
      return newPropertyKeys;
    }
  }

  /**
   * Add attribute.
   *
   * @param name      Attribute name.
   * @param attribute Attribute annotation.
   */
  private void addAttribute(String name, Attribute attribute) {
    attributes.add(new CompAttribute(this, name, attribute));
  }

  /**
   * Returns {@link #facesLib}.
   *
   * @return {@link #facesLib}.
   */
  public FacesLib getFacesLib() {
    return facesLib;
  }

  /**
   * Returns {@link #name}.
   *
   * @return {@link #name}.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns {@link #type}.
   *
   * @return {@link #type}.
   */
  public String getType() {
    return type;
  }

  /**
   * Returns {@link #description}.
   *
   * @return {@link #description}.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns {@link #attributes}.
   *
   * @return {@link #attributes}.
   */
  public List<CompAttribute> getAttributes() {
    return Collections.unmodifiableList(attributes);
  }

  /**
   * @return {@inheritDoc }
   */
  @Override
  public int hashCode() {
    int hash = 3;
    hash = 11 * hash + Objects.hashCode(this.name);
    return hash;
  }

  /**
   * Equals based on {@link #name}.
   *
   * @param obj {@inheritDoc }
   *
   * @return {@inheritDoc }
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final LibComponent other = (LibComponent) obj;
    return Objects.equals(this.name, other.getName());
  }

  /**
   * @param o {@inheritDoc }
   *
   * @return {@inheritDoc }
   */
  @Override
  public int compareTo(LibComponent o) {
    return name.compareTo(o.getName());
  }

  /**
   * @return {@inheritDoc }
   */
  @Override
  public String toString() {
    return "LibComponent{" + "name=" + name + ", type=" + type + ", attributes=" + attributes + '}';
  }

}
