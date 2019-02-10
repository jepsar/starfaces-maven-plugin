<?xml version="1.0" encoding="UTF-8"?>
<facelet-taglib
  xmlns="http://xmlns.jcp.org/xml/ns/javaee"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-facelettaglibrary_2_3.xsd"
  version="2.3">

  <description>${pluginConfig.getChild('description').value}</description>
  <namespace>${pluginConfig.getChild('namespace').value}</namespace>
  <short-name>${pluginConfig.getChild('shortName').value}</short-name>
  <#list components as component>

  <tag>
    <tag-name>${component.name}</tag-name>
    <component>
      <component-type>${component.type}</component-type>
    </component>
    <#list component.attributes as attribute>
    <attribute>
      <description><![CDATA[${attribute.description}]]></description>
      <name>${attribute.name}</name>
      <required>${attribute.required?c}</required>
      <type>${attribute.type}</type>
    </attribute>
    </#list>
  </tag>
  </#list>

</facelet-taglib>
