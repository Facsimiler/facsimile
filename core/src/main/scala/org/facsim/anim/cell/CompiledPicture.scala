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
import scalafx.scene.Group

/**
Class representing _[[http://www.automod.com/ AutoMod®]] cell compiled
picture_ primitives.

@see [[
http://facsim.org/Documentation/Resources/AutoModCellFile/CompiledOldFormat.html
Compiled Pictures]] for further information.

@constructor Construct a new compiled picture primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an _AutoMod® cell_ file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see [[
http://facsim.org/Documentation/Resources/AutoModCellFile/CompiledOldFormat.html
Compiled Pictures]] for further information.
*/

private[cell] final class CompiledPicture(scene: CellScene,
parent: Option[Set])
extends Cell(scene, parent) {
  throw new NotImplementedError(LibResource("anim.cell.CompiledPicture"))

/**
@inheritdocs

@note CompiledPicture elements currently cannot be rendered. See Issue 8 for
further details.
*/
  private[cell] override def toNode = new Group()
}