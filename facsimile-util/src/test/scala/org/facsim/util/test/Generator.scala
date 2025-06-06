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
// Scala source file belonging to the org.facsim.util.test package.
//======================================================================================================================
package org.facsim.util.test

import org.facsim.util.types.UniChars.UniChar
import org.scalacheck.{Gen, Shrink}

/** Custom _ScalaCheck_ generators.
 *
 *  @since 0.0
 */
object Generator:

  /** Negative integers.
   *
   *  Produce integers across the entire supported range of negative integer values.
   *
   *  The `suchThat` condition is required because the generator can take values from outside the specified range,
   *  which then causes test failures. Refer to [[https://github.com/rickynils/scalacheck/issues/189 ScalaCheck issue
   *  #189]] for further information.
   */
  val negInt = Gen.choose(Integer.MIN_VALUE, -1).suchThat(_ < 0)

  /** Non-negative integers.
   *
   *  Produce integers across the entire supported range of non-negative integer values, including zero.
   */
  val nonNegInt = Gen.choose(0, Integer.MAX_VALUE)

  /** Non-positive integers.
   *
   *  Produce integers across the entire supported range of non-positive integer values, including zero.
   */
  val nonPosInt = Gen.choose(Integer.MIN_VALUE, 0)

  /** Generate any integer value.
   */
  // A range of [Integer.MinValue, Integer.MaxValue] exceeds the capacity of a signed 32-bit integer value (31 bits for
  // the value, 1 for the sign), so we get around this by having two generators and selecting one or the other at
  // random).
  val int = Gen.oneOf(negInt, nonNegInt)

  /** ISO-8859-1 characters.
   */
  private val iso8859_1Char = Gen.choose(0.toChar, 255.toChar)

  /** Strings that can be encoded using ISO-8859-1.
   */
  val iso8859_1String = Gen.listOf(iso8859_1Char).map(_.mkString)

  /** Set of valid _[[http://unicode.org/ Unicode]]_ characters.
   *
   *  Java will throw exceptions if attempts are map to create characters from invalid Unicode codepoints.
   */
  private lazy val unicodeVector = (Character.MIN_CODE_POINT to Character.MAX_CODE_POINT).filter(UniChar.isValid)

  /** _[[http://unicode.org/ Unicode]]_ characters.
   */
  private lazy val unicodeChar = Gen.oneOf(unicodeVector)

  /** Strings that can be encoded in _[[http://unicode.org/ Unicode]]_.
   */
  lazy val unicodeString = Gen.listOf(unicodeChar).map(_.flatMap(i => Character.toChars(i).nn).mkString)

  /** List of _[[http://unicode.org/ Unicode]]_ strings, which may be empty.
   */
  lazy val unicodeStringList = Gen.listOf(unicodeString)

  /** List of _[[http://unicode.org/ Unicode]]_ strings, non-empty.
   */
  lazy val unicodeStringListNonEmpty = Gen.nonEmptyListOf(unicodeString)

  /** Utility to prevent _ScalaCheck_ property shrinkage.
   *
   *  @note Shrinkage, whereby _ScalaCheck_ attempts to find the simplest case of value that causes a problem, is a
   *  brilliant concept. Unfortunately, there's a [[https://github.com/rickynils/scalacheck/issues/129 bug]] in which
   *  shrunken values violate defined constraints, so that the shrunken value fails for a completely different reason to
   *  the original failing value. To work around this, we use this function that effectively disables shrinking of
   *  failed values; the idea for this can be found [[https://github.com/scalatest/scalatest/issues/584 here]].
   *
   *  @tparam A Type of value for which shrinking is being disabled.
   *
   *  @return No shrink value, which should typically be an implicit value, for the specified type, `A`.
   */
  def noShrink[A]: Shrink[A] = Shrink.shrinkAny[A]