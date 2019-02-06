/*
 * Copyright (c) Jasper de Vries.
 */
package org.starfaces.mavenplugin.util;

import java.io.File;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jasper de Vries &lt;jepsar@gmail.com&lt;
 */
public class UtilsTest extends TestCase {

  /**
   * Test of className method, of class Utils.
   */
  @Test
  public void testClassName() {
    String basePath = new File("/test").toString();
    String classPath = new File("/test/some/Random.class").toString();
    Assert.assertEquals("some.Random", Utils.className(basePath, classPath));
  }
  
}
