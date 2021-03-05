/* ln.c -- changed by Bruno Haible, 13 April 1995 */

/* `ln' program to create links between files.
   Copyright (C) 1986, 1989, 1990, 1991 Free Software Foundation, Inc.

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2, or (at your option)
   any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.  */

/* Written by Mike Parker and David MacKenzie. */

#ifdef _AIX
 #pragma alloca
#endif

#include <config.h>
#include <stdio.h>
#include <sys/types.h>
#include <getopt.h>
#include "system.h"
#include "backupfile.h"
#include "version.h"
#include "safe-lstat.h"
#include "safe-stat.h"

/* glibc2 declares basename() with an incompatible prototype. */
#define basename fu_basename

int link ();			/* Some systems don't declare this anywhere. */

#ifdef S_ISLNK
int symlink ();
char *resolve ();
#endif

/* Construct a string NEW_DEST by concatenating DEST, a slash, and
   basename(SOURCE) in alloca'd memory.  Don't modify DEST or SOURCE.  */

#define PATH_BASENAME_CONCAT(new_dest, dest, source)			\
    do									\
      {									\
	char *source_base;						\
	char *tmp_source;						\
									\
	tmp_source = (char *) alloca (strlen ((source)) + 1);		\
	strcpy (tmp_source, (source));					\
	strip_trailing_slashes (tmp_source);				\
	source_base = basename (tmp_source);				\
									\
	(new_dest) = (char *) alloca (strlen ((dest)) + 1		\
				      + strlen (source_base) + 1);	\
	stpcpy (stpcpy (stpcpy ((new_dest), (dest)), "/"), source_base);\
      }									\
    while (0)

char *basename ();
enum backup_type get_version ();
int isdir ();
int yesno ();
void error ();
void strip_trailing_slashes ();
char *stpcpy ();

static void usage ();
static int do_link ();

/* The name by which the program was run, for error messages.  */
char *program_name;

/* A pointer to the function used to make links.  This will point to either
   `link' or `symlink'. */
static int (*linkfunc) ();

/* If nonzero, make symbolic links; otherwise, make hard links.  */
static int symbolic_link;

/* If nonzero, ask the user before removing existing files.  */
static int interactive;

/* If nonzero, remove existing files unconditionally.  */
static int remove_existing_files;

/* If nonzero, list each file as it is moved. */
static int verbose;

/* If nonzero, allow the superuser to make hard links to directories. */
static int hard_dir_link;

/* If nonzero, and the specified destination is a symbolic link to a
   directory, treat it just as if it were a directory.  Otherwise, the
   command `ln --force --no-dereference file symlink-to-dir' deletes
   symlink-to-dir before creating the new link.  */
static int dereference_dest_dir_symlinks = 1;

/* If non-zero, display usage information and exit.  */
static int show_help;

/* If non-zero, print the version on standard output and exit.  */
static int show_version;

static struct option const long_options[] =
{
  {"backup", no_argument, NULL, 'b'},
  {"directory", no_argument, &hard_dir_link, 1},
  {"no-dereference", no_argument, NULL, 'n'},
  {"force", no_argument, NULL, 'f'},
  {"interactive", no_argument, NULL, 'i'},
  {"suffix", required_argument, NULL, 'S'},
  {"symbolic", no_argument, &symbolic_link, 1},
  {"verbose", no_argument, &verbose, 1},
  {"version-control", required_argument, NULL, 'V'},
  {"help", no_argument, &show_help, 1},
  {"version", no_argument, &show_version, 1},
  {NULL, 0, NULL, 0}
};

int
main (argc, argv)
     int argc;
     char **argv;
{
  int c;
  int errors;
  int make_backups = 0;
  char *version;

  version = getenv ("SIMPLE_BACKUP_SUFFIX");
  if (version)
    simple_backup_suffix = version;
  version = getenv ("VERSION_CONTROL");
  program_name = argv[0];
  linkfunc = link;
  symbolic_link = remove_existing_files = interactive = verbose
    = hard_dir_link = 0;
  errors = 0;

  while ((c = getopt_long (argc, argv, "bdfinsvFS:V:", long_options, (int *) 0))
	 != EOF)
    {
      switch (c)
	{
	case 0:			/* Long-named option. */
 	  break;
	case 'b':
	  make_backups = 1;
	  break;
	case 'd':
	case 'F':
	  hard_dir_link = 1;
	  break;
	case 'f':
	  remove_existing_files = 1;
	  interactive = 0;
	  break;
	case 'i':
	  remove_existing_files = 0;
	  interactive = 1;
	  break;
	case 'n':
	  dereference_dest_dir_symlinks = 0;
	  break;
	case 's':
#ifdef S_ISLNK
	  symbolic_link = 1;
#else
	  error (1, 0, "symbolic links are not supported on this system");
#endif
	  break;
	case 'v':
	  verbose = 1;
	  break;
	case 'S':
	  simple_backup_suffix = optarg;
	  break;
	case 'V':
	  version = optarg;
	  break;
	default:
	  usage (1);
	  break;
	}
    }

  if (show_version)
    {
      printf ("%s\n", version_string);
      exit (0);
    }

  if (show_help)
    usage (0);

  if (optind == argc)
    {
      error (0, 0, "missing file argument");
      usage (1);
    }

  if (make_backups)
    backup_type = get_version (version);

#ifdef S_ISLNK
  if (symbolic_link)
    linkfunc = symlink;
#endif

  if (optind == argc - 1)
    errors = do_link (argv[optind], ".");
  else if (optind == argc - 2)
    {
      struct stat source_stats;
      char *source;
      char *dest;
      char *new_dest;

      source = argv[optind];
      dest = argv[optind + 1];

      /* When the destination is specified with a trailing slash and the
	 source exists but is not a directory, convert the user's command
	 `ln source dest/' to `ln source dest/basename(source)'.  */

      if (dest[strlen (dest) - 1] == '/'
	  && SAFE_LSTAT (source, &source_stats) == 0
	  && !S_ISDIR (source_stats.st_mode))
	{
	  PATH_BASENAME_CONCAT (new_dest, dest, source);
	}
      else
	{
	  new_dest = dest;
	}

      errors = do_link (source, new_dest);
    }
  else
    {
      char *to;

      to = argv[argc - 1];
      if (!isdir (to))
	error (1, 0, "when making multiple links, last argument must be a directory");
      for (; optind < argc - 1; ++optind)
	errors += do_link (argv[optind], to);
    }

  exit (errors != 0);
}

/* Make a link DEST to the (usually) existing file SOURCE.
   Symbolic links to nonexistent files are allowed.
   If DEST is a directory, put the link to SOURCE in that directory.
   Return 1 if there is an error, otherwise 0.  */

static int
do_link (source, dest)
     char *source;
     char *dest;
{
  char *real_source = source;
  struct stat dest_stats;
  char *dest_backup = NULL;
  int lstat_status;

  /* Use stat here instead of lstat.
     On SVR4, link does not follow symlinks, so this check disallows
     making hard links to symlinks that point to directories.  Big deal.
     On other systems, link follows symlinks, so this check is right.  */
  if (!symbolic_link)
    {
      struct stat source_stats;

      if (SAFE_STAT (source, &source_stats) != 0)
	{
	  error (0, errno, "%s", source);
	  return 1;
	}
      if (!hard_dir_link && S_ISDIR (source_stats.st_mode))
	{
	  error (0, 0, "%s: hard link not allowed for directory", source);
	  return 1;
	}
#ifdef S_ISLNK
      if (SAFE_LSTAT (source, &source_stats) != 0)
	{
	  error (0, errno, "%s", source);
	  return 1;
	}
      /* On SVR4, link() does not follow symlinks. Therefore resolve
	 the source before making a hard link.  */
      if (S_ISLNK (source_stats.st_mode))
	{
          real_source = (char *) alloca (PATH_MAX);
	  if (!resolve (source, real_source))
	    {
	      error (0, errno, "%s", real_source);
	      return 1;
	    }
	}
#endif
    }

  if (SAFE_LSTAT (dest, &dest_stats) != 0 && errno != ENOENT)
    {
      error (0, errno, "%s", dest);
      return 1;
    }

  /* If the destination is a directory or (it is a symlink to a directory
     and the user has not specified --no-dereference), then form the
     actual destination name by appending basename (source) to the
     specified destination directory.  */
  lstat_status = SAFE_LSTAT (dest, &dest_stats);

  if (lstat_status != 0 && errno != ENOENT)
    {
      error (0, errno, "%s", dest);
      return 1;
    }

  if ((lstat_status == 0
       && S_ISDIR (dest_stats.st_mode))
#ifdef S_ISLNK
      || (dereference_dest_dir_symlinks
	  && (S_ISLNK (dest_stats.st_mode)
	  && isdir (dest)))
#endif
     )
    {
      /* Target is a directory; build the full filename. */
      char *new_dest;
      PATH_BASENAME_CONCAT (new_dest, dest, source);
      dest = new_dest;
      /* Set this to non-zero to force another call to SAFE_LSTAT
	 with the new destination.  */
      lstat_status = 1;
    }

  if (lstat_status == 0 || SAFE_LSTAT (dest, &dest_stats) == 0)
    {
      if (S_ISDIR (dest_stats.st_mode))
	{
	  error (0, 0, "%s: cannot overwrite directory", dest);
	  return 1;
	}
      if (interactive)
	{
	  fprintf (stderr, "%s: replace `%s'? ", program_name, dest);
	  if (!yesno ())
	    return 0;
	}
      else if (!remove_existing_files)
	{
	  error (0, 0, "%s: File exists", dest);
	  return 1;
	}

      if (backup_type != none)
	{
	  char *tmp_backup = find_backup_file_name (dest);
	  if (tmp_backup == NULL)
	    error (1, 0, "virtual memory exhausted");
	  dest_backup = (char *) alloca (strlen (tmp_backup) + 1);
	  strcpy (dest_backup, tmp_backup);
	  free (tmp_backup);
	  if (rename (dest, dest_backup))
	    {
	      if (errno != ENOENT)
		{
		  error (0, errno, "cannot backup `%s'", dest);
		  return 1;
		}
	      else
		dest_backup = NULL;
	    }
	}
      else if (unlink (dest) && errno != ENOENT)
	{
	  error (0, errno, "cannot remove `%s'", dest);
	  return 1;
	}
    }
  else if (errno != ENOENT)
    {
      error (0, errno, "%s", dest);
      return 1;
    }

  if (verbose)
    printf ("%s -> %s\n", real_source, dest);

  if ((*linkfunc) (real_source, dest) == 0)
    {
      return 0;
    }

  error (0, errno, "cannot %slink `%s' to `%s'",
#ifdef S_ISLNK
	 symbolic_link ? "symbolic " : "",
#else
	 "",
#endif
	 real_source, dest);

  if (dest_backup)
    {
      if (rename (dest_backup, dest))
	error (0, errno, "cannot un-backup `%s'", dest);
    }
  return 1;
}

static void
usage (status)
     int status;
{
  if (status != 0)
    fprintf (stderr, "Try `%s --help' for more information.\n",
	     program_name);
  else
    {
      printf ("\
Usage: %s [OPTION]... SOURCE [DEST]\n\
  or:  %s [OPTION]... SOURCE... DIRECTORY\n\
",
	      program_name, program_name);
      printf ("\
\n\
  -b, --backup                 make backups for removed files\n\
  -d, -F, --directory          hard link directories (super-user only)\n\
  -f, --force                  remove existing destinations\n\
  -n, --no-dereference         with --force, remove destination that is a\n\
                                 symlink to a directory\n\
  -i, --interactive            prompt whether to remove destinations\n\
  -s, --symbolic               make symbolic links, instead of hard links\n\
  -v, --verbose                print name of each file before linking\n\
  -S, --suffix=SUFFIX          override the usual backup suffix\n\
  -V, --version-control=WORD   override the usual version control\n\
      --help                   display this help and exit\n\
      --version                output version information and exit\n\
\n\
The backup suffix is ~, unless set with SIMPLE_BACKUP_SUFFIX.  The\n\
version control may be set with VERSION_CONTROL, values are:\n\
\n\
  t, numbered     make numbered backups\n\
  nil, existing   numbered if numbered backups exist, simple otherwise\n\
  never, simple   always make simple backups\n");
    }
  exit (status);
}
