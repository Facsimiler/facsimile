/*
Facsimile: A Discrete-Event Simulation Library
Copyright © 2004-2025, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see
http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for inclusion
as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If your code
fails to comply with the standard, then your patches will be rejected. For further information, please visit the coding
standards at:

  http://facsim.org/Documentation/CodingStandards/
========================================================================================================================
Scala source file from the org.facsim.anim.cell package.
*/

package org.facsim.anim.cell

import org.facsim.LibResource
import org.facsim.anim.{Mesh, Point3D}

/**
Class representing _[[http://www.automod.com/ AutoMod®]] cell cone_
primitives.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Cones.html
Cones]] for further information.

@constructor Construct a new cone primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an _AutoMod® cell_ file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Cones.html
Cones]] for further information.
*/

private[cell] final class Cone(scene: CellScene, parent: Option[Set])
extends Mesh3D(scene, parent) {

/**
Cone radius.

Base side radius, measured on the X-Y plane.
*/

  private val radius = scene.readDouble(_ >= 0.0,
  LibResource(Cone.ReadDimKey, 0))

/**
Cone height (Z-dimension).

Height of the cone measured along the Z-Axis.
*/

  private val height = scene.readDouble(_ >= 0.0,
  LibResource(Cone.ReadDimKey, 1))

/**
Cone top X-axis offset.
*/

  private val xOffset = scene.readDouble(LibResource(Cone.ReadOffsetKey, 0))

/**
Cone top Y-axis offset.
*/

  private val yOffset = scene.readDouble(LibResource(Cone.ReadOffsetKey, 1))

/**
@inheritdoc

@note The origin of the cone is at the center of its base.
*/
  protected[cell] override def cellMesh: Mesh = Mesh.cone(Point3D.Origin,
  radius, Point3D(xOffset, yOffset, height), Cone.Divisions)
}

/**
Cone companion object.
*/

private object Cone {

/**
Read dimension string resource key.
*/

  val ReadDimKey = "anim.cell.Cone.readDim"

/**
Read offset string resource key.
*/

  val ReadOffsetKey = "anim.cell.Cone.readOffset"

/**
Number of divisions per cone.

The number of divisions for a fine cone in AutoMod is 16, and for a course cone
it's 8. For simplicity, we'll convert all cones to have 16 divisions.
*/

  val Divisions = 16
}