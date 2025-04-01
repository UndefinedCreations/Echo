package com.undefinedcreations.echo.exceptions

/**
 * This exception will be throw then the system doesn't have the correct version of java installed.
 */
class UnsupportedJavaVersion(version: Int): Exception("Can't find the correct version for build tools ($version)") {
}