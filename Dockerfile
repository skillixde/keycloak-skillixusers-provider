FROM jboss/keycloak:${version.keycloak}

COPY target/classes/startup-scripts/* /opt/jboss/startup-scripts/
COPY target/*.jar /opt/jboss/keycloak/modules/de/skillix/keycloak-skillixusers-provider/provider/main/
COPY target/classes/module.xml /opt/jboss/keycloak/modules/de/skillix/keycloak-skillixusers-provider/provider/main/

