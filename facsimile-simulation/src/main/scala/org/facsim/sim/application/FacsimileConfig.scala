//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2023, Michael J Allen.
//
// This file is part of Facsimile.
//
// Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
// version.
//
// Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
// warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
// details.
//
// You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see:
//
//   http://www.gnu.org/licenses/lgpl.
//
// The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
// project home page at:
//
//   http://facsim.org/
//
// Thank you for your interest in the Facsimile project!
//
// IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for
// inclusion as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If
// your code fails to comply with the standard, then your patches will be rejected. For further information, please
// visit the coding standards at:
//
//   http://facsim.org/Documentation/CodingStandards/
//======================================================================================================================

//======================================================================================================================
// Scala source file belonging to the org.facsim.sim.application package.
//======================================================================================================================
package org.facsim.sim.application

import com.typesafe.config.{Config, ConfigFactory}
import java.io.File
import org.facsim.util.log.{Severity, WarningSeverity}
import squants.time.{Seconds, Time}

/** Configuration of the ''Facsimile'' application and simulation run.
 *
 *  ''Facsimile'' has a number of standard command line options whose state is represented by the final instance of this
 *  class.
 *
 *  Command line configuration indicates whether a ''GUI'' animation will be provided, the name of the ''HOCON''
 *  simulation configuration file to be used to configure this simulation run, etc.
 *
 *  @note For further details of the command line options available, run the simulation application with the single
 *  command line option, `--help`.
 *
 *  @constructor Create a new Facsimile configuration instance.
 *
 *  @param configFile ''HOCON'' configuration file to be used to configure the simulation, wrapped in `[[scala.Some
 *  Some]]`, or `[[scala.None None]]` if the simulation is to solely use command line ''Java''-style configuration
 *  properties and/or default configuration values.
 *
 *  @param logFile File into which log information should be written, wrapped in `[[scala.Some  Some]]`, or
 *  `[[scala.None None]]` if the simulation should not write any log information.
 *
 *  @param logLevel Default log severity level when recording log messages; only log messages at or above the indicated
 *  severity level will be written into the log file. If `useGui` is false, this also defines the log level for writing
 *  log messages to the standard output.
 *
 *  @param reportFile Simulation report file to be generated by this run, wrapped in `[[scala.Some Some]]`, or
 *  `[[scala.None None]]` if the simulation is not to issue a report for this run.
 *
 *  @param runModel Flag indicating whether the program should run this simulation. Typically, this will not happen if
 *  the user has requested the program version or help information; otherwise the simulation should be run.
 *
 *  @param showVersion Flag indicating whether program version information should be displayed at the start of the run.
 *  By default, no version information will be displayed.
 *
 *  @param showUsage Flag indicating whether program usage information should be displayed at the start of the run. By
 *  default, no usage information will be displayed.
 *
 *  @param useGUI Flag indicating whether a ''graphical user interface'' (''GUI'') is to be utilized to control and view
 *  the simulation.
 */
private[application] final case class FacsimileConfig(configFile: Option[File] = None, logFile: Option[File] = None,
logLevel: Severity = WarningSeverity, reportFile: Option[File] = None, runModel: Boolean = true,
showVersion: Boolean = false, showUsage: Boolean = false, useGUI: Boolean = true) {

  /** Retrieve this simulation run's parameters.
   *
   *  @note Evaluating the configuration may result in an exception being thrown.
   */
  private lazy val params: Config = {

    // Load the default configuration.
    val default = ConfigFactory.load()

    // Retrieve the custom configuration from the named file, using the default configuration as a fallback.
    // Alternatively, if a custom configuration file was not specified, just use the default configuration.
    configFile.fold(default)(cf => ConfigFactory.parseFile(cf).withFallback(default))
  }

  /** Report the configuration.
   *
   *  @return Specified configuration.
   */
  def parameters: Config = params

  /** Retrieve a parameter value as a string, converting to a time value.
   *
   *  @param path Path defining location of the parameter in the configuration.
   *
   *  @return Parameter parsed as a time value.
   *
   *  @throws com.typesafe.config.ConfigException if `path` does not identify a parameter, or if it cannot be resolved
   *  as a string.
   */
  private def timeParameter(path: String): Time = {

    // Retrieve the value as a string.
    val t = params.getString(path)

    // Convert the result to a time and return.
    Time.parseString(t).get
  }

  /** Report the configured warm-up duration.
   *
   *  @return Configured simulation warm-up duration.
   *
   *  @throws com.typesafe.config.ConfigException if corresponding configuration item cannot be identified.
   */
  def warmUpDuration: Time = timeParameter(FacsimileConfig.WarmUpDurationName) ensuring(_ > Seconds(0.0))

  /** Report the configured snap duration.
   *
   *  @return Configured simulation snap duration.
   *
   *  @throws com.typesafe.config.ConfigException if corresponding configuration item cannot be identified.
   */
  def snapDuration: Time = timeParameter(FacsimileConfig.SnapDurationName) ensuring(_ > Seconds(0.0))

  /** Report the configured snap count.
   *
   *  @return Configured simulation snap count.
   *
   *  @throws com.typesafe.config.ConfigException if corresponding configuration item cannot be identified.
   */
  def snapCount: Int = params.getInt(FacsimileConfig.SnapCountName) ensuring(_ > 0)
}

/** Facsimile configuration companion object. */
private object FacsimileConfig {

  /** Base name for all ''facsimile-simulation'' configuration settings. */
  private val BaseName = "facsimile.simulation."

  /** Name of the warm-up period duration parameter. */
  private val WarmUpDurationName = s"${BaseName}warm-up-duration"

  /** Name of the snap duration parameter. */
  private val SnapDurationName = s"${BaseName}snap-duration"

  /** Name of the snap count parameter. */
  private val SnapCountName = s"${BaseName}snap-count"
}