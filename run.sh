#!/bin/bash

set -e

echo "📦 Building Exchange platform JAR..."
./gradlew clean bootJar

JAR_FILE=$(find build/libs -name "*.jar" | head -n 1)

if [ -z "$JAR_FILE" ]; then
  echo "❌ No JAR found in build/libs. Build failed?"
  exit 1
fi

echo "✅ JAR built: $JAR_FILE"

echo "🐳 Starting Docker Compose with build..."
docker-compose up --build