FROM jboss/keycloak:${version.keycloak}

COPY target/${project.build.finalName}.jar /opt/jboss/keycloak/standalone/deployments/