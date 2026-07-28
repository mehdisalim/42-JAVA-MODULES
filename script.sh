#!/bin/bash
set -euo pipefail

# Works for any 42 login: uses the current user instead of a hardcoded name.
USERNAME="$(whoami)"
INSTALL_DIR="/goinfre/${USERNAME}"

MAVEN_VERSION="3.9.11"
MAVEN_DIR="${INSTALL_DIR}/apache-maven-${MAVEN_VERSION}"
MAVEN_TARBALL="apache-maven-${MAVEN_VERSION}-bin.tar.gz"
MAVEN_URL="https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/${MAVEN_TARBALL}"

# Eclipse Temurin JDK 26 (latest), macOS x64 build.
JAVA_MAJOR="26"
JAVA_TARBALL="OpenJDK${JAVA_MAJOR}U-jdk_x64_mac.tar.gz"
JAVA_URL="https://api.adoptium.net/v3/binary/latest/${JAVA_MAJOR}/ga/mac/x64/jdk/hotspot/normal/eclipse"

mkdir -p "${INSTALL_DIR}"
cd "${INSTALL_DIR}"

# --- Maven ---------------------------------------------------------------
echo "==> Downloading Maven ${MAVEN_VERSION}..."
curl -L -o "${MAVEN_TARBALL}" "${MAVEN_URL}"
ls -lh "${MAVEN_TARBALL}"

echo "==> Extracting Maven..."
tar -xzf "${MAVEN_TARBALL}"
rm "${MAVEN_TARBALL}"

# --- Java ------------------------------------------------------------------
echo "==> Downloading Java (Temurin ${JAVA_MAJOR}, macOS x64)..."
curl -L -o "${JAVA_TARBALL}" "${JAVA_URL}"
ls -lh "${JAVA_TARBALL}"

echo "==> Extracting Java..."
tar -xzf "${JAVA_TARBALL}"
rm "${JAVA_TARBALL}"

# The extracted JDK folder name varies with the exact build (e.g. jdk-21.0.x+y),
# so resolve it dynamically instead of hardcoding it.
JDK_HOME="$(find "${INSTALL_DIR}" -maxdepth 1 -type d -name 'jdk-*' | head -n1)/Contents/Home"

# --- Shell profile ---------------------------------------------------------
ZSHRC="${HOME}/.zshrc"

add_line_once() {
    local line="$1"
    grep -qxF "${line}" "${ZSHRC}" 2>/dev/null || echo "${line}" >> "${ZSHRC}"
}

echo "==> Updating ${ZSHRC}..."
add_line_once "export MAVEN_HOME=${MAVEN_DIR}"
add_line_once "export JAVA_HOME=${JDK_HOME}"
add_line_once 'export PATH="$MAVEN_HOME/bin:$JAVA_HOME/bin:$PATH"'

export MAVEN_HOME="${MAVEN_DIR}"
export JAVA_HOME="${JDK_HOME}"
export PATH="$MAVEN_HOME/bin:$JAVA_HOME/bin:$PATH"

echo "==> Done."
echo "MAVEN_HOME=${MAVEN_DIR}"
echo "JAVA_HOME=${JDK_HOME}"
mvn -version
