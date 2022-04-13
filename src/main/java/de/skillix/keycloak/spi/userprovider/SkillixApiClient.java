package de.skillix.keycloak.spi.userprovider;

import java.util.List;

public interface SkillixApiClient {

  SkillixUser getSkillixProfileByIdentity(String identity);

  List<SkillixUser> searchSkillixProfiles(String queryParams);

}
