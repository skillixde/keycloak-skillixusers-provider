package de.skillix.keycloak.spi.userprovider;

import java.util.List;

public interface SkillixApiClient {

  SkillixUserApiResponse getSkillixProfileByIdentity(String identity);

  List<SkillixUserApiResponse> searchSkillixProfiles(String queryParams);

}
