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
// Scala source file belonging to the org.facsim.types types.
//======================================================================================================================
package org.facsim

import org.facsim.types.phys.{Angle, Current, Length, LuminousIntensity, Mass, Physical, Temperature, Time}
import scala.util.Try

/** _Facsimile Measurement Library_.
 *
 *  Elements defined within this types support _[[https://en.wikipedia.org/wiki/Dimensional_analysis dimensional
 *  analysis]]_. The library supports a number of _[[https://en.wikipedia.org/wiki/Physical_quantities physical
 *  quantities]]_, such as _time_, _length_, _mass_, etc. Each _measurement_ of such a quantity can be
 *  expressed in one or more _[[https://en.wikipedia.org/wiki/Units_of_measure units of types]]_ of that quantity.
 *  For example, _time_ measurements can be expressed in units of _seconds_, _minutes_, _hours_, etc.
 *
 *  Internally, all measurements are stored in the _[[https://en.wikipedia.org/wiki/International_System_of_Units
 *  International System of Units]]_ (_SI_, from the French _Système international (d'unités)_) for the associated
 *  quantity.
 *
 *  This library was designed specifically for use within _Facsimile_ simulation models. However, it can be used as a
 *  standalone library for undertaking _dimensional analysis_ problems, as an alternative to other, similar libraries
 *  such as _[[https://to-ithaca.github.io/libra/ Libra]]_ and _[[http://www.squants.com/ Squants]]_. (These
 *  libraries did not achieve the goals of the _Facsimile_ library, leading to the development of this library.)
 *
 *  The goals of this library are as follows:
 *
 *   1. Ensure quantity _type safety_, and to prevent accidental confusion of different quantities, such as _time_
 *      and _length_.
 *   1. Ensure unit of types _type safety_, and to prevent accidental confusion of different units of the same
 *      quantity, such as _seconds_ and _minutes_ for _time_ measurements.
 *   1. To make the use of measurements in user code, whatever their quantities and units, as natural and as intuitive
 *      as the _Scala_ language permits. The library strives above all for clarity of expression.
 *   1. To allow the user, or end-user, the option to select their preferred units of types for each quantity, without
 *      needing to change any of the underlying code. That is, the units in which measurements are output must be
 *      configurable.
 *   1. To be _purely functional_ whenever possible. In particular, measurement calculations typically result in a
 *      [[scala.util.Try]] value, allowing calculation errors to be tracked without needing to throw exceptions
 *      (which would violate _referential transparency_).
 *   1. To implement these features as efficiently&mdash;in terms of processing speed and memory overhead&mdash;as
 *      possible. Note that given a choice between efficiency and clarity, the library will favor clarity: it
 *      is better to have the right answer in a little while, than the wrong answer quickly.
 *
 *  @note To prevent by-passing of the library's quantity- and unit-confusion protection, it is not possible to convert
 *  measurements into [[scala.Double]]s or _vice versa_. This is deliberate and by design.
 *
 *  @example Some examples of how the _Facsimile Measurement Library_ might be used:
 *
 *  One common approach is to use [[scala.Double]] for all measurements, regardless of quantities or units of types.
 *  Consider the following:
 *
 *  {{{
 *  val distance = 5.0 // Distance in meters
 *  val timeTaken = 10.0 // Time in seconds
 *  val meanVelocity = distance / timeTaken // Velocity in meters/second.
 *  val huh = distance + timeTaken // ???
 *  }}}
 *
 *  This compiles and runs without error, but is clearly buggy: adding length and time measurements together makes no
 *  sense and should be flagged as such. However, since all of these values have type [[scala.Double]], there's no way
 *  for the _Scala_ compiler to tell. Using the _Facsimile Measurement Library_, this code might be more safely
 *  represented as follows:
 *
 *  {{{
 *  val distance = 5.0 m
 *  val timeTaken = 10.0 s
 *  val meanVelocity = distance / timeTaken
 *  val huh = distance + timeTaken // Compile time error: cannot add a time and a distance together.
 *  }}}
 *
 *  This is because `distance` is implied to be a [[org.facsim.types.phys.Length]] with a value of 5 _meters_, with
 *  `timeTaken` implied to be a [[org.facsim.types.phys.Time]] with a value of 10 _seconds_. Because a _length_
 *  divided by a _time_ results in a _velocity_, `meanVelocity` respects this relationship. However, division
 *  operations can fail; for example, if the divisor is evaluated as zero, a _divide-by-zero_ error would result. To
 *  catch such errors, without throwing an exception and violating _referential transparency_, `meanVelocity` is
 *  implied to have the type [[scala.util.Try]][ [[org.facsim.types.phys.Velocity]] ] and ends up with a value of
 *  [[scala.util.Success]](0.5)#. Finally, the computation for
 *  `huh` fails during compilation, since there is no `+` operator allowing _length_ and _time_ values to be added
 *  together.
 *
 *  Without a great deal of extra typing, the same code is now _much_ safer.
 *
 *  Now consider the following:
 *
 *  {{{
 *  val startTime = 8.0 // Time, in hours.
 *  val duration = 10.0
 *  val endTime = startTime + duration
 *  }}}
 *  For example, if a
 *  *      [[scala.Double]] is used to store measurements of such quantities, then the user is responsible for
 *  tracking the
 *  *      quantities, particularly when those measurements are used in calculations, resulting in a number of subtle
 *   bugs.
 *
 *    For example, if a [[scala.Double]] is used to store measurements of such quantities, then the
 *  *      user is responsible for tracking the units in which the measurements are expressed&mdash;particularly when
 *   those
 *  *      measurements are combined in a single calculation. Failure to manually convert units correctly will result in
 *  *      bugs.
 *
 *  @see [[https://en.wikipedia.org/wiki/Dimensional_analysis Dimensional analysis]] on _Wikipedia_.
 *
 *  @see [[https://en.wikipedia.org/wiki/International_System_of_Units International System of Units]] on _Wikipedia_.
 *
 *  @see [[https://en.wikipedia.org/wiki/Physical_quantities Physical quantities]] on _Wikipedia_.
 *
 *  @see [[[[https://en.wikipedia.org/wiki/Units_of_measure Units of types]] on _Wikipedia_.
 *
 *  @since 0.0
 */
package object types {

  /** Plane angle physical quantity measurements. */
  type Angle = Angle.Measure

  /** Electrical current physical quantity measurements. */
  type Current = Current.Measure

  /** Length physical quantity measurements. */
  type Length = Length.Measure

  /** Luminous intensity physical quantity measurements. */
  type LuminousIntensity = LuminousIntensity.Measure

  /** Mass physical quantity measurements. */
  type Mass = Mass.Measure

  /** Temperature physical quantity measurements. */
  type Temperature = Temperature.Measure

  /** Time physical quantity measurements. */
  type Time = Time.Measure

  /** Extensions to [[scala.Double]] supporting physical quantity measurement types.
   *
   *  @constructor Convert a double value to an implicit _extended double_, permitting interaction with physical
   *  quantity measurements. As a _value type_, this constructor typically does not result in a new instance when
   *  used implicitly.
   *
   *  @param d Double value being extended.
   *
   *  @since 0.0
   */
  final implicit class ExtendedDouble(val d: Double)
  extends AnyVal {

    /** Multiplication operator.
     *
     *  @tparam T Type of physical measurement involved in the multiplication operation.
     *
     *  @param p Physical measurement being multiplied by the extended [scala.Double] value.
     *
     *  @return Returns the result of the physical measurement `p` multiplied by `d`. The result is belongs to the same
     *  physical measurement family.
     *
     *  @since 0.0
     */
    def *[T <: Physical](p: T.Measure): T.Measure = T.apply(p.v)
  }

  /** Operators for comparing values for equivalence.
   *
   *  @tparam A Type of element being compared.
   *
   *  @param lhs Element on left-hand side of an operator.
   *
   *  @since 0.0
   */
  final implicit class EquivalentOperators[A](private val lhs: A)
  extends AnyVal {

    /** Equivalence operator.
     *
     *  @param rhs right-hand side of the equivalence operator.
     *
     *  @param eqv Implicit [[Equivalent]] instance that will be used to implement the comparison operations.
     *
     *  @return `true` if `lhs` and `rhs` of the relation are equivalent; `false` if they are not equivalent.
     *
     *  @since 0.0
     */
    def ===(rhs: A)(implicit eqv: Equivalent[A]): Boolean = eqv.eqv(lhs, rhs)

    /** Non-equivalence operator.
     *
     *  @param rhs right-hand side of the equivalence operator.
     *
     *  @param eqv Implicit [[Equivalent]] instance that will be used to implement the comparison operations.
     *
     *  @return `true` if `lhs` and `rhs` of the relation are equivalent; `false` if they are not equivalent.
     *
     *  @since 0.0
     */
    def =!=(rhs: A)(implicit eqv: Equivalent[A]): Boolean = eqv.neqv(lhs, rhs)
  }

  /** Operators for ranking and ordering values.
   *
   *  @tparam A Type of element being compared or ranked.
   *
   *  @param lhs Element on left-hand side of an operator.
   *
   *  @since 0.0
   */
  final implicit class OrderOperators[A](private val lhs: A)
  extends AnyVal {

    /** _Less-than_ operator.
     *
     *  @param rhs Value on the right-hand-side of the operator.
     *
     *  @param order Implicit [[Order]] instance that will be used to implement the comparison and ranking operations.
     *
     *  @return `true` if `lhs` is less than `rhs`; `false` otherwise.
     *
     *  @since 0.0
     */
    def <(rhs: A)(implicit order: Order[A]): Boolean = order.compare(lhs, rhs) < 0

    /** _Less-than-or-equal-to_ operator.
     *
     *  @param rhs Value on the right-hand-side of the operator.
     *
     *  @param order Implicit [[Order]] instance that will be used to implement the comparison and ranking operations.
     *
     *  @return `true` if `lhs` is less than or equal to `rhs`; `false` otherwise.
     *
     *  @since 0.0
     */
    def <=(rhs: A)(implicit order: Order[A]): Boolean = order.compare(lhs, rhs) <= 0

    /** _Greater-than_ operator.
     *
     *  @param rhs Value on the right-hand-side of the operator.
     *
     *  @param order Implicit [[Order]] instance that will be used to implement the comparison and ranking operations.
     *
     *  @return `true` if `lhs` is greater than `rhs`; `false` otherwise.
     *
     *  @since 0.0
     */
    def >(rhs: A)(implicit order: Order[A]): Boolean = order.compare(lhs, rhs) > 0

    /** _Greater-than-or-equal-to_ operator.
     *
     *  @param rhs Value on the right-hand-side of the operator.
     *
     *  @param order Implicit [[Order]] instance that will be used to implement the comparison and ranking operations.
     *
     *  @return `true` if `lhs` is greater than or equal to `rhs`; `false` otherwise.
     *
     *  @since 0.0
     */
    def >=(rhs: A)(implicit order: Order[A]): Boolean = order.compare(lhs, rhs) >= 0
  }

  /** Operators for adding and subtracting values.
   *
   *  @tparam A Type of element being added and subtracted.
   *
   *  @param lhs Element on left-hand side of an operator.
   *
   *  @since 0.0
   */
  final implicit class AdditionOperators[A](private val lhs: A)
  extends AnyVal {

    /** Addition operator.
     *
     *  @param rhs right-hand side of the addition operator.
     *
     *  @param addition Implicit [[org.facsim.types.algebra.Addition]] instance that will be used to implement the
     *  addition and subtraction operations.
     *
     *  @return Value of addition of `rhs` to `lhs`, wrapped in a [[scala.util.Success]] if successful; a
     *  [[scala.util.Failure]] wrapping an exception if unsuccessful.
     *
     *  @since 0.0
     */
    def +(rhs: A)(implicit addition: Addition[A]): Try[A] = addition.add(lhs, rhs)

    /** Addition operator.
     *
     *  @param rhs right-hand side of the addition operator, wrapped in a [[scala.util.Try]].
     *
     *  @param addition Implicit [[org.facsim.types.algebra.Addition]] instance that will be used to implement the
     *  addition and subtraction operations.
     *
     *  @return Value of addition of `rhs` to `lhs`, wrapped in a [[scala.util.Success]] if successful; a
     *  [[scala.util.Failure]] wrapping an exception if unsuccessful. If `rhs` has failed, then its Failure value will
     *  be preserved.
     *
     *  @since 0.0
     */
    def +(rhs: Try[A])(implicit addition: Addition[A]): Try[A] = addition.add(lhs, rhs)

    /** Subtraction operator.
     *
     *  @param rhs right-hand side of the addition operator.
     *
     *  @param addition Implicit [[org.facsim.types.algebra.Addition]] instance that will be used to implement the
     *  addition and subtraction operations.
     *
     *  @return Value of subtraction of `rhs` from `lhs`, wrapped in a [[scala.util.Success]] if successful; a
     *  [[scala.util.Failure]] wrapping an exception if unsuccessful.
     *
     *  @since 0.0
     */
    def -(rhs: A)(implicit addition: Addition[A]): Try[A] = addition.subtract(lhs, rhs)

    /** Subtraction operator.
     *
     *  @param rhs right-hand side of the addition operator, wrapped in a [[scala.util.Try]].
     *
     *  @param addition Implicit [[org.facsim.types.algebra.Addition]] instance that will be used to implement the
     *  addition and subtraction operations.
     *
     *  @return Value of subtraction of `rhs` from `lhs`, wrapped in a [[scala.util.Success]] if successful; a
     *  [[scala.util.Failure]] wrapping an exception if unsuccessful. If `rhs` has failed, then its Failure value will
     *  be preserved.
     *
     *  @since 0.0
     */
    def -(rhs: Try[A])(implicit addition: Addition[A]): Try[A] = addition.subtract(lhs, rhs)
  }

  /** Operators for adding and subtracting values.
   *
   *  @tparam A Type of element being added and subtracted.
   *
   *  @param lhs Element on left-hand side of an operator, wrapped in a [[scala.util.Try]].
   *
   *  @since 0.0
   */
  final implicit class AdditionOperatorsT[A](private val lhs: Try[A])
  extends AnyVal {

    /** Addition operator.
     *
     *  @param rhs right-hand side of the addition operator.
     *
     *  @param addition Implicit [[org.facsim.types.algebra.Addition]] instance that will be used to implement the
     *  addition and subtraction operations.
     *
     *  @return Value of addition of `rhs` to `lhs`, wrapped in a [[scala.util.Success]] if successful; a
     *  [[scala.util.Failure]] wrapping an exception if unsuccessful. If `lhs` has failed, then its Failure value will
     *  be preserved.
     *
     *  @since 0.0
     */
    def +(rhs: A)(implicit addition: Addition[A]): Try[A] = addition.add(lhs, rhs)

    /** Addition operator.
     *
     *  @param rhs right-hand side of the addition operator, wrapped in a [[scala.util.Try]].
     *
     *  @param addition Implicit [[org.facsim.types.algebra.Addition]] instance that will be used to implement the
     *  addition and subtraction operations.
     *
     *  @return Value of addition of `rhs` to `lhs`, wrapped in a [[scala.util.Success]] if successful; a
     *  [[scala.util.Failure]] wrapping an exception if unsuccessful. If `lhs` has failed, then its Failure value will
     *  be preserved; otherwise, if `rhs` has failed, then its Failure value will be preserved.
     *
     *  @since 0.0
     */
    def +(rhs: Try[A])(implicit addition: Addition[A]): Try[A] = addition.add(lhs, rhs)

    /** Subtraction operator.
     *
     *  @param rhs right-hand side of the addition operator.
     *
     *  @param addition Implicit [[org.facsim.types.algebra.Addition]] instance that will be used to implement the
     *  addition and subtraction operations.
     *
     *  @return Value of subtraction of `rhs` from `lhs`, wrapped in a [[scala.util.Success]] if successful; a
     *  [[scala.util.Failure]] wrapping an exception if unsuccessful. If `lhs` has failed, then its Failure value will
     *  be preserved.
     *
     *  @since 0.0
     */
    def -(rhs: A)(implicit addition: Addition[A]): Try[A] = addition.subtract(lhs, rhs)

    /** Subtraction operator.
     *
     *  @param rhs right-hand side of the addition operator, wrapped in a [[scala.util.Try]].
     *
     *  @param addition Implicit [[org.facsim.types.algebra.Addition]] instance that will be used to implement the
     *  addition and subtraction operations.
     *
     *  @return Value of subtraction of `rhs` from `lhs`, wrapped in a [[scala.util.Success]] if successful; a
     *  [[scala.util.Failure]] wrapping an exception if unsuccessful. If `lhs` has failed, then its Failure value will
     *  be preserved; otherwise, if `rhs` has failed, then its Failure value will be preserved.
     *
     *  @since 0.0
     */
    def -(rhs: Try[A])(implicit addition: Addition[A]): Try[A] = addition.subtract(lhs, rhs)
  }
}
