#!/bin/bash

# LightsaberVR Mod Build Script
# This script builds the mod using Forge Gradle

set -e

echo "=== LightsaberVR Build Script ==="
echo ""

# Check if gradle wrapper exists
if [ ! -f "gradlew" ]; then
    echo "Gradle wrapper not found. Downloading..."
    # Download gradle wrapper files would go here
    echo "Please run 'gradle wrapper' first or use IntelliJ IDEA to import this project."
    exit 1
fi

# Clean previous builds
echo "Cleaning previous builds..."
./gradlew clean

# Build the mod
echo ""
echo "Building mod..."
./gradlew build

# Check if build was successful
if [ -f "build/libs/lightsabersvr-*.jar" ]; then
    echo ""
    echo "=== BUILD SUCCESSFUL ==="
    echo "Output JAR: $(ls build/libs/lightsabersvr-*.jar | grep -v sources)"
    
    # Copy to download directory
    mkdir -p /home/z/my-project/download
    cp build/libs/lightsabersvr-*.jar /home/z/my-project/download/
    echo "JAR copied to /home/z/my-project/download/"
else
    echo ""
    echo "=== BUILD FAILED ==="
    echo "Check the build output above for errors."
    exit 1
fi
