FROM jboss/keycloak:${version.keycloak}

COPY target/classes/startup-scripts/* /opt/jboss/keycloak/standalone/deployments/
COPY target/classes/module.xml /opt/jboss/keycloak/modules/de/skillix/keycloak-user-storage/provider/main/

COPY target/classes/startup-scripts/* /opt/jboss/startup-scripts/