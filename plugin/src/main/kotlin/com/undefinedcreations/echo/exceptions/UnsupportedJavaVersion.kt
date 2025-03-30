package com.undefinedcreations.echo.exceptions

class UnsupportedJavaVersion(version: Int): Exception("Can't find the correct version for build tools ($version)") {
}