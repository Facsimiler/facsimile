﻿/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2008, Michael J Allen.

This program is free software: you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation, either version 3 of the License, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program.  If not, see <http://www.gnu.org/licenses/>.

The developers welcome all comments, suggestions and offers of assistance.
For further information, please visit the project home page at:

    http://www.facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

    http://www.facsim.org/Documentation/CodingStandards/
===============================================================================
$Id$

C# source file for the TextResourceVerifier class, and associated elements,
that are integral members of the Facsimile.Tools.VerifyFile namespace.
===============================================================================
*/

namespace Facsimile.Tools.VerifyFile
{

//=============================================================================
/**
<summary>Text resource file verification class.</summary>
*/
//=============================================================================

    public sealed class TextResourceVerifier:
        Utf8FileVerifier
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>File constructor.</summary>

<remarks>Prepare the file for verification.</remarks>

<param name="fileName">The name of the file, including any path information
(relative or absolute) required to locate it.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public TextResourceVerifier (string fileName):
            base (fileName)
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Maximum permitted line length.</summary>

<remarks>Identifies the maximum permitted line length for this type of file.

<para>Override this property to identify a line length other than the default
value of 80.  Supporting line lengths greater than 80 is discouraged,
however.</para></remarks>

<value>A <see cref="System.UInt64" /> value identifying the maximum number of
characters that may be present on a single line within this type of text
file.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected sealed override ulong MaximumLineLength
        {
            get
            {
                return ulong.MaxValue;
            }
        }
    }
}
