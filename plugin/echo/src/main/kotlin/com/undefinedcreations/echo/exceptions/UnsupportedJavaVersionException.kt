package com.undefinedcreations.echo.exceptions

/**
 * This exception will be throw then the system doesn't have the correct version of java installed.
 */
class UnsupportedJavaVersionException(minVersion: Int, maxVersion: Int): Exception("Can't find the correct version for BuildTools. Please Install any version from class file major version $minVersion to $maxVersion.)")