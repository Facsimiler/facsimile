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
// Scala source file belonging to the org.facsim.sim.engine package.
//======================================================================================================================
package org.facsim.sim.engine

import izumi.reflect.Tag
import org.facsim.sim.model.{Action, ModelState}
import org.facsim.util.CompareEqualTo
import squants.Time

/** Event scheduling the dispatch of specified actions at a specified simulation time.
 *
 *  @tparam M Final type of the simulation's model state.
 *
 *  @note This class is considered an internal, implementation class. It is not a part of the _Facsimile_ public API,
 *  and should not be exposed to the user; the rationale for this is that it keeps the API simpler.
 *
 *  @constructor Create an event instance to ensure that an action to change the simulation state is performed at a
 *  specified time in the future.
 *
 *  @param id Unique identifier for this event. As well as uniquely identifying each event, `id` values serve to record
 *  event creation order, such that when comparing two events, that with the lower `id` value was the first of the two
 *  be scheduled.
 *
 *  @param dueAt Absolute simulation time at which the event's `action` is scheduled to occur.
 *
 *  @param priority Relative priority of this event. The lower this value, the higher the priority of the associated
 *  event. Co-incidental events will be dispatched in order of their priority.
 *
 *  @param action Action to be performed by this event when it is dispatched.
 */
private[engine] final case class Event[M <: ModelState[M]: Tag](id: Long, dueAt: Time, priority: Int = 0,
action: Action[M])
extends Ordered[Event[M]]:

  /** Compare this event to another event.
   *
   *  When comparing two events that have yet to occur, the event that compares as _less than_ the other event must
   *  always be dispatched first.
   *
   *  @note It is possible for an event that is occurring, or that has already occurred, to compare as _greater than_ an
   *  event that has yet to occur, but only if the latter was scheduled _after_ the former was dispatched. Even so,
   *  since time cannot run backwards, the latter cannot be due at an earlier time than the former.
   *
   *  @param that Event that this event is being compared to.
   *
   *  @return An integer value indicating the result of the comparison. If this value is less than zero, then this event
   *  compares as _less than_ `that` event; if this value is greater than zero, then this event compares as _greater
   *  than_ `that` event. Two events should _never_ compare as equal (unless they are the same instance), since the
   *  event's [[id]] must be unique.
   */
  override def compare(that: Event[M]): Int =

    // Compare the two events based upon their due times. If the value is non-zero, then return that result; otherwise,
    // the events are co-incident (occurring at the same simulation time) and we must look at the two events'
    // priorities.
    val dueAtOrder = dueAt.compare(that.dueAt)
    if dueAtOrder != CompareEqualTo then dueAtOrder
    else

      // Compare the two events based upon their priorities. Because priorities are higher the lower the value, we can
      // simply numerically compare the two. If the value is non-zero, then return that result; otherwise, the events
      // are co-incident AND have the same priority and we must look at the two events' identifiers to determine their
      // ordering.
      val priorityOrder = priority.compare(that.priority)
      if priorityOrder != CompareEqualTo then priorityOrder
      else

        // Compare the two events based upon their identifiers. These should not compare equal unless that event is this
        // event.
        assert(id != that.id || (this ne that), s"This event '$this' cannot equal event '$that'")
        id.compare(that.id)
