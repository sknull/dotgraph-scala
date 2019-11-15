package de.visualdigits.dotgraph.core.util.exec

import java.io.{BufferedReader, File, InputStream, InputStreamReader}

/**
 * Helper class to execute a shell command in a spwaned thread and wait for the return.
 *
 * @param command The command to execute.
 * @param args The args to append to the command.
 */
class ShellCommand(val command: File, val args: Seq[String] = Seq()) {

  val commandLine: String = command.getCanonicalPath + " " + args.mkString(" ")

  /**
   * Executes a given shell command and returns its stdout (if any). When the process outputs something on stderr an
   * IllegalStateException containing the output as description will be thrown.<br>
   *
   * @return String
   */
  def execute: String = {
    val p = Runtime.getRuntime.exec(commandLine)
    p.waitFor
    val stdout = readStreamFromProcess(p.getInputStream)
    val stderr = readStreamFromProcess(p.getErrorStream)
    if (!stderr.isEmpty) throw new IllegalStateException(stderr)
    stdout.trim
  }

  /**
   * Converts an input stream from a command execution process into a string.
   *
   * @param stream
   * The stream to convert.
   * @return String
   */
  def readStreamFromProcess(stream: InputStream): String = {
    val sb = new StringBuilder()
    val reader = new BufferedReader(new InputStreamReader(stream))
    var line = ""
    do {
      line = reader.readLine()
      if (line != null) sb ++= line + "\n"
    } while (line != null)
    sb.toString
  }
}

object ShellCommand {
  def apply(command: File, args: String*) = new ShellCommand(command, args = args)
}