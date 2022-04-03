package de.skillix.keycloak.spi.userprovider;

import lombok.RequiredArgsConstructor;
import org.keycloak.models.ClientModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RoleModel;
import org.keycloak.storage.ReadOnlyException;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class SkillixGroupModel implements GroupModel.Streams {

    private final String name;

    @Override
    public String getId() {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        throw new ReadOnlyException("group is read only");
    }

    @Override
    public void setSingleAttribute(String name, String value) {
        throw new ReadOnlyException("group is read only");
    }

    @Override
    public void setAttribute(String name, List<String> values) {
        throw new ReadOnlyException("group is read only");
    }

    @Override
    public void removeAttribute(String name) {
        throw new ReadOnlyException("group is read only");
    }

    @Override
    public String getFirstAttribute(String name) {
        return null;
    }

    @Override
    public Stream<String> getAttributeStream(String name) {
        return null;
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        return null;
    }

    @Override
    public GroupModel getParent() {
        return null;
    }

    @Override
    public String getParentId() {
        return null;
    }

    @Override
    public Stream<GroupModel> getSubGroupsStream() {
        return null;
    }

    @Override
    public void setParent(GroupModel group) {
        throw new ReadOnlyException("group is read only");
    }

    @Override
    public void addChild(GroupModel subGroup) {
        throw new ReadOnlyException("group is read only");
    }

    @Override
    public void removeChild(GroupModel subGroup) {
        throw new ReadOnlyException("group is read only");
    }

    @Override
    public Stream<RoleModel> getRealmRoleMappingsStream() {
        return null;
    }

    @Override
    public Stream<RoleModel> getClientRoleMappingsStream(ClientModel app) {
        return null;
    }

    @Override
    public boolean hasRole(RoleModel role) {
        return false;
    }

    @Override
    public void grantRole(RoleModel role) {
        throw new ReadOnlyException("group is read only");
    }

    @Override
    public Stream<RoleModel> getRoleMappingsStream() {
        return null;
    }

    @Override
    public void deleteRoleMapping(RoleModel role) {
        throw new ReadOnlyException("group is read only");
    }
}
