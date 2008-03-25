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

C# source file for the SingletonException class, and associated elements, that
are integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>Invalid use of <see cref="Singleton {SingletonBase}" />-derived
object.</summary>

<remarks>Exception thrown whenever an attempt is made to create multiple
instances of a "singleton" class; classes that are derived from <see
cref="Singleton {SingletonBase}" />.</remarks>
*/
//=============================================================================

    public sealed class SingletonException:
        OperationForbiddenException
    {

/**
<summary>Object array.</summary>

<remarks>This array is initialised by the constructor to contain the following
values:

<list type="number">
    <item>
        <description>Full name of the singleton base class.</description>
    </item>
    <item>
        <description>Full name of the first singleton class
        found.</description>
    </item>
    <item>
        <description>Full name of the second singleton class represented by the
        offending instance.</description>
    </item>
</list></remarks>
*/

        private readonly System.Object [] singletonData;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Constructor.</summary>

<remarks>Record the names of the classes involved.</remarks>

<param name="singleton">A <see cref="System.Type" /> identifying the type of
the singleton base class, supplied as the "SingletonBase" type parameter to the
<see cref="Singleton {SingletonBase}" /> class.</param>

<param name="firstSingletonInstance">A <see cref="System.Type" /> identifying
the actual type of the first singleton instance found.</param>

<param name="secondSingletonInstance">A <see cref="System.Type" /> identifying
the actual type of the offending second instance.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal SingletonException (System.Type singleton, System.Type
        firstSingletonInstance, System.Type secondSingletonInstance)
        {

/*
Argument integrity assertions.
*/

            System.Diagnostics.Debug.Assert (singleton != null);
            System.Diagnostics.Debug.Assert (firstSingletonInstance != null);
            System.Diagnostics.Debug.Assert (firstSingletonInstance ==
            singleton || firstSingletonInstance.IsSubclassOf (singleton));
            System.Diagnostics.Debug.Assert (secondSingletonInstance != null);
            System.Diagnostics.Debug.Assert (secondSingletonInstance ==
            singleton || secondSingletonInstance.IsSubclassOf (singleton));

/*
Store the appropriate information for later use.
*/

            singletonData = new System.Object []
            {
                singleton.FullName,
                firstSingletonInstance.FullName,
                secondSingletonInstance.FullName,
            };
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Explain why exception was thrown.</summary>

<remarks>Reports detailed information that allows a user to identify why the
exception was thrown.</remarks>

<value>A <see cref="System.String" /> containing the exception's
explanation.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public override string Message
        {

/*
Retrieve the compound message, format it and return it to the caller.
*/

            get
            {
                return Resource.Format ("singleton", singletonData);
            }
        }
    }
}
