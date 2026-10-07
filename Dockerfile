# Create a stage for resolving and downloading dependencies.
FROM eclipse-temurin:25-jdk AS deps

WORKDIR /build

# Copy the mvnw wrapper with executable permissions.
COPY --chmod=0755 mvnw mvnw
COPY .mvn/ .mvn/

# Download dependencies as a separate step to take advantage of Docker's caching.
# Leverage a cache mount to /root/.m2 so that subsequent builds don't have to
# re-download packages.
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=bind,source=cen4802-project-module/pom.xml,target=cen4802-project-module/pom.xml \
    --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline

# Create a stage for building the application based on the stage with downloaded dependencies.
FROM deps AS package

WORKDIR /build

COPY pom.xml .
COPY cen4802-project-module/pom.xml cen4802-project-module/
COPY cen4802-project-module/src cen4802-project-module/src
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw package

# Create a new stage for running the application that contains the minimal
# runtime dependencies for the application.
FROM eclipse-temurin:25-jre AS final

WORKDIR /app

# Create a non-privileged user that the app will run under.
# See https://docs.docker.com/go/dockerfile-user-best-practices/
ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser
USER appuser

# Copy the executable from the "package" stage.
COPY --chown=appuser:appuser --from=package build/cen4802-project-module/target/cen4802-project-module-1.0-SNAPSHOT.jar application.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "application.jar" ]