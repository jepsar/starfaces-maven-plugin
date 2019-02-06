/*
 * Copyright (c) Jasper de Vries.
 */
package org.starfaces.mavenplugin.mojo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.starfaces.mavenplugin.util.Utils;

/**
 *
 * @author Jasper de Vries &lt;jepsar@gmail.com&gt;
 */
@Mojo(name = "taglib-xml", requiresDependencyResolution = ResolutionScope.COMPILE)
public class TagLibXmlMojo extends FacesLibMojo {

  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject project;

  @Parameter(property = "project.compileClasspathElements", required = true, readonly = true)
  private List<String> compileClassPaths;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      try (InputStream stream = Utils.resourceStream("/taglib.xml.ftl")) {
        // TODO create setting for filename
        Path tagLibXmlPath = getBuildDirOutPath("META-INF", "TODO.taglib.xml");
        process(stream, tagLibXmlPath);
        logProcessed(tagLibXmlPath);
      }
    }
    catch (IOException ex) {
      throw new MojoExecutionException(ex.getMessage(), ex);
    }
  }

  @Override
  protected MavenProject getProject() {
    return project;
  }

  @Override
  protected List<String> getCompileClassPaths() {
    return compileClassPaths;
  }

}
