# Use the official openjdk 21 base image
FROM openjdk:21-jdk-slim

# Install dependencies
RUN apt-get update && \
    apt-get install -y wget unzip curl && \
    rm -rf /var/lib/apt/lists/*

# Set environment variables
ENV GRADLE_VERSION=8.14.3
ENV PATH=/opt/gradle/gradle-$GRADLE_VERSION/bin:$PATH

# Download and install Gradle 8.14.3
RUN wget https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -P /tmp && \
    unzip -d /opt/gradle /tmp/gradle-${GRADLE_VERSION}-bin.zip

# Install Allure Commandline
RUN wget -O /tmp/allure.zip https://github.com/allure-framework/allure2/releases/download/2.34.0/allure-2.34.0.zip && \
    unzip /tmp/allure.zip -d /opt && \
    ln -s /opt/allure-2.34.0/bin/allure /usr/local/bin/allure

# Verify installations
RUN java -version && \
    gradle -v && \
    allure --version

# Install Git
RUN apt-get update && apt-get install -y git && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Clone repository
ARG REPO_URL=https://github.com/akotrulev/Avenga.git
RUN git clone $REPO_URL .

# Build fatJar
RUN gradle clean fatJar

# Set commands to be executed when container is started
CMD ["bash", "-c", "java -jar Avenga-all-1.0-SNAPSHOT-tests.jar -testjar Avenga-all-1.0-SNAPSHOT-tests.jar -xmlpathinjar suite.xml || true; allure generate --clean --single-file allure-results"]
