FROM jboss/keycloak:${keycloak.version}

COPY build/libs/*.jar /opt/jboss/keycloak/standalone/deployments/
#COPY docker/module.xml /opt/jboss/keycloak/modules/de/skillix/keycloak-skillixusers-provider/provider/main/


COPY src/main/docker/startup-scripts/* /opt/jboss/startup-scripts/
