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
Abstract class for primitives that can themselves store primitives.

@see [[http://facsim.org/Documentation/Resources/Sets.htmls Sets]] for further
information.

@constructor Construct a new basic set primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an _AutoMod® cell_ file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Sets.html
Sets]]
*/

private[cell] abstract class Set(scene: CellScene, parent: Option[Set])
extends Cell(scene, parent) {

/**
@inheritdoc

@note Note that sets are not rendered in a particular line style, neither do
they use material (except for inheritance by children), employ a display style
or anything else. They're actually pretty basic.
*/
  private[cell] final override def toNode = new Group {

/*
If this cell has a name, then use it as an ID.
*/

    id = name.orNull

/*
Apply the required transformations to the node.
*/

    transforms = cellTransforms

/*
Add the child cells (if any) to the group as nodes.
*/

    children = getChildren.map(_.toNode)
  }

/**
Retrieve the set's children.

@return A list of this set's child _cells_.
*/
  protected[cell] def getChildren: List[Cell]
}