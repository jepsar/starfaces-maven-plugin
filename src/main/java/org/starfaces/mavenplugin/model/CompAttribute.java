/*
 * Copyright (c) Jasper de Vries.
 */
package org.starfaces.mavenplugin.model;

import java.util.Objects;
import org.starfaces.annotations.Attribute;

/**
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 */
public class CompAttribute implements Comparable<CompAttribute> {

  /**
   * LibComponent of this attribute.
   */
  private final LibComponent component;

  /**
   * Name of the attribute.
   */
  private final String name;

  /**
   * If {@code true} the attribute is required.
   */
  private final boolean required;

  /**
   * Type of the attribute.
   */
  private final Class<?> type;

  /**
   * Description of the attribute.
   */
  private final String description;

  /**
   * Create attribute.
   *
   * @param component LibComponent of this attribute.
   * @param name      Name of the attribute.
   * @param attribute Attribute annotation.
   */
  CompAttribute(LibComponent component, String name, Attribute attribute) {
    this.component = component;
    this.name = name;
    this.required = attribute.required();
    this.type = attribute.type();
    this.description = attribute.value();
  }

  /**
   * Returns {@link #component}.
   *
   * @return {@link #component}.
   */
  public LibComponent getComponent() {
    return component;
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
   * Returns {@link #required}.
   *
   * @return {@link #required}.
   */
  public boolean isRequired() {
    return required;
  }

  /**
   * Returns {@link #type}.
   *
   * @return {@link #type}.
   */
  public Class<?> getType() {
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
   * @return {@inheritDoc }
   */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 79 * hash + Objects.hashCode(this.name);
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
    final CompAttribute other = (CompAttribute) obj;
    return Objects.equals(this.name, other.getName());
  }

  /**
   * @param o {@inheritDoc }
   *
   * @return {@inheritDoc }
   */
  @Override
  public int compareTo(CompAttribute o) {
    return name.compareTo(o.getName());
  }

  /**
   * @return {@inheritDoc }
   */
  @Override
  public String toString() {
    return "CompAttribute{" + "name=" + name + ", required=" + required + ", type=" + type + '}';
  }

}
