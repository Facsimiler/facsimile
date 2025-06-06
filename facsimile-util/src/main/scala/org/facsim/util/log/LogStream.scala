//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2025, Michael J Allen.
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
// Scala source file belonging to the org.facsim.util.log package.
//======================================================================================================================
package org.facsim.util.log

import org.apache.pekko.{Done, NotUsed}
import org.apache.pekko.stream.{Materializer, QueueOfferResult}
import org.apache.pekko.stream.scaladsl.Source
import org.facsim.util.stream.DataSource
import org.facsim.util.NonPure
import scala.concurrent.Future

/** Create and manage a queued _Pekko_ source for issuing log messages.
 *
 *  @note Because the created log stream is buffered, and because it utilizes back pressure to slow down the publisher
 *  (the process that is creating the log messages), applications may appear to hang once the buffer has
 *  filled&mdash;unless the stream is connected to a buffer and run.
 *
 *  @tparam A Type of message prefix to be used with messages sent to this stream.
 *
 *  @constructor Create a new messaging source.
 *
 *  @param bufferSize Number of unprocessed log messages that can be stored in the buffer before back pressure is
 *  exerted. This value must be greater than zero and less than or equal to
 *  [[org.facsim.util.stream.DataSource.MaxBufferSize]], [[java.lang.IllegalArgumentException]] will be thrown.
 *
 *  @param materializer Stream materializer to be utilized when creating the stream.
 *
 *  @throws java.lang.IllegalArgumentException if `bufferSize` is less than 1 or greater than
 *  [[org.facsim.util.stream.DataSource.MaxBufferSize]].
 *
 *  @since 0.2
 */
final class LogStream[A](bufferSize: Int = LogStream.DefaultBufferSize)(using materializer: Materializer):

  /** Data source to be used for logging.
   */
  private val ds = new DataSource[LogMessage[A]](bufferSize)

  /** Send a message instance to the stream.
   *
   *  @note This operation will fail if the stream has been closed previously.
   *
   *  @param message Message to be sent to the stream.
   *
   *  @return Future containing the result of the message logging operation. If successful, the result can be
   *  [[org.apache.pekko.stream.QueueOfferResult.Enqueued]] if data was sent successfully,
   *  [[org.apache.pekko.stream.QueueOfferResult.Dropped]] if the data was dropped due to a buffer failure, or
   *  [[org.apache.pekko.stream.QueueOfferResult.QueueClosed]] if the queue was closed before the data could be
   *  processed. If the queue was closed before the data was sent, the result is a [[scala.util.Failure]] wrapping an
   *  [[org.apache.pekko.stream.StreamDetachedException]]. If a failure closed the queue, it will respond with a
   *  `Failure` wrapping the exception that signaled failure of the queue.
   *
   *  @since 0.2
   */
  @NonPure
  def log(message: LogMessage[A]): Future[QueueOfferResult] = ds.send(message)

  /** Report the stream to which flows and sinks can be attached.
   *
   *  @return Source of log messages. This can be connected to a sink, and run, in order to consume messages.
   *
   *  @since 0.2
   */
  def source: Source[LogMessage[A], NotUsed] = ds.source

  /** Complete all logging, and flush the stream.
   *
   *  @return Future that executes when the stream has been closed.
   *
   *  @since 0.2
   */
  @NonPure
  def close(): Future[Done] = ds.complete()

/** Message stream companion object.
 *
 *  @since 0.2
 */
object LogStream:

  /** Default log message buffer size.
   */
  private val DefaultBufferSize: Int = 100
