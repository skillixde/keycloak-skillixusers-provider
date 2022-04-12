FROM jboss/keycloak:${version.keycloak}

COPY target/*.jar /opt/jboss/keycloak/standalone/deployments/
COPY target/classes/module.xml /opt/jboss/keycloak/standalone/deployments/