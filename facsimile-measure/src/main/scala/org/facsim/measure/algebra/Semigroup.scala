//======================================================================================================================
// Facsimile -- A Discrete-Event Simulation Library
// Copyright © 2004-2017, Michael J Allen.
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

// Scala source file belonging to the org.facsim.measure.algebra package.
//======================================================================================================================
package org.facsim.measure.algebra

/** Trait for a _[[https://en.wikipedia.org/wiki/Semigroup semigroup]]_.
 *
 *  @tparam A Type representing the set of values to which the semigroup is applicable.
 *
 *  @since 0.0
 */
trait Semigroup[A]
extends Any
with Serializable {

  /** Combine operation.
   *
   *  Takes two values from some set `A` and combines them into a single value.
   *
   *  @note The implementation of this operation _must_ be _[[https://en.wikipedia.org/wiki/Associative_property
   *  associative]]_. That is, `combine(combine(a, b), c)` must equal `combine(a, combine(b, c))` for all a, b and c.
   *
   *  @param a First value being combined.
   *
   *  @param b Second value being combined.
   *
   *  @return Result of the combination of `a` and `b`
   *
   *  @since 0.0
   */
  def combine(a: A, b: A): A
}