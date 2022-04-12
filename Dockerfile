FROM jboss/keycloak:16.1.0

COPY docker/startup-scripts/* /opt/jboss/startup-scripts/
COPY build/libs/*.jar /opt/jboss/keycloak/standalone/deployments/
#COPY docker/module.xml /opt/jboss/keycloak/modules/de/skillix/keycloak-skillixusers-provider/provider/main/

