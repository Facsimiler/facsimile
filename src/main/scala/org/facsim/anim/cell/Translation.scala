/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2013, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile.  If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance.  For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file from the org.facsim.anim.cell package.
*/
//=============================================================================

package org.facsim.anim.cell

import org.facsim.LibResource

//=============================================================================
/**
Translation along a cell's local X, Y & Z axes.

@constructor Create a new translation instance.

@param x Translation along local X axis.

@param y Translation along local X axis.

@param z Translation along local X axis.

@since 0.0
*/
//=============================================================================

private [cell] final case class Translation (x: Double, y: Double, z: Double)
extends NotNull

//=============================================================================
/**
Translation companion object.

@since 0.0
*/
//=============================================================================

private [cell] object Translation {

//-----------------------------------------------------------------------------
/**
Read translation from ''cell'' data stream.

@param scene Scene from which the joint type is to be read.

@throws [[com.sun.j3d.loaders.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[com.sun.j3d.loaders.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@see
[http://facsim.org/Documentation/Resources/AutoModCellFile/Translation.html
Translation]]

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def read (scene: CellScene) = {

/*
Read the X axis translation from the data stream.
*/

    val x = scene.readDouble (LibResource ("anim.cell.Translation.read", 0))

/*
Read the Y axis translation from the data stream.
*/

    val y = scene.readDouble (LibResource ("anim.cell.Translation.read", 1))

/*
Read the Z axis translation from the data stream.
*/

    val z = scene.readDouble (LibResource ("anim.cell.Translation.read", 2))

/*
Convert to a translation and return.
*/

    Translation (x, y, z)
  }
}