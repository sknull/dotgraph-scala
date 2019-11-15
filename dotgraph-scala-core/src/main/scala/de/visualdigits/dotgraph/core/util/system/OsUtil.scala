package de.visualdigits.dotgraph.core.util.system

import java.io.File

/**
 * Some convenience methods.
 */
object OsUtil {

  /**
   * Returns whether the current OS is a Windows flavour or not.
   * @return Boolean
   */
  def isWindoze: Boolean = {
    System.getProperty("os.name").startsWith("Windows")
  }

  /**
   * Determines the location of the dot executable by assuming the standard
   * installation paths of the respective OS.
   *
   * @return File
   */
  def dotExecutable: File = {
    if (isWindoze) new File("C:/Program Files (x86)/Graphviz2.38/bin/dot.exe")
    else new File("/usr/local/bin/dot")
  }
}
