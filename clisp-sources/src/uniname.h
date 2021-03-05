/* Association between Unicode characters and their names.
   Copyright (C) 2000-2002 Free Software Foundation, Inc.

   This program is free software; you can redistribute it and/or modify it
   under the terms of the GNU Library General Public License as published
   by the Free Software Foundation; either version 2, or (at your option)
   any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Library General Public License for more details.

   You should have received a copy of the GNU Library General Public
   License along with this program; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
   USA.  */

#ifndef _UNINAME_H
#define _UNINAME_H

#include "unitypes.h"

/* Required size of buffer for a Unicode character name.  */
#define UNINAME_MAX 256

/* Looks up the name of a Unicode character, in uppercase ASCII.
   Returns the filled buf, or NULL if the character does not have a name.  */
extern char * unicode_character_name (ucs4_t uc, char *buf);

/* Looks up the Unicode character with a given name, in upper- or lowercase
   ASCII.  Returns the character if found, or UNINAME_INVALID if not found.  */
extern ucs4_t unicode_name_character (const char *name);
#define UNINAME_INVALID ((ucs4_t) 0xFFFF)

#endif /* _UNINAME_H */
