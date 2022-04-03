package de.skillix.keycloak.spi.userprovider;

import org.keycloak.models.ClientModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RoleModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SkillixGroupModel implements GroupModel {
    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public void setSingleAttribute(String name, String value) {

    }

    @Override
    public void setAttribute(String name, List<String> values) {

    }

    @Override
    public void removeAttribute(String name) {

    }

    @Override
    public String getFirstAttribute(String name) {
        return null;
    }

    @Override
    public List<String> getAttribute(String name) {
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
    public Set<GroupModel> getSubGroups() {
        return null;
    }

    @Override
    public void setParent(GroupModel group) {

    }

    @Override
    public void addChild(GroupModel subGroup) {

    }

    @Override
    public void removeChild(GroupModel subGroup) {

    }

    @Override
    public Set<RoleModel> getRealmRoleMappings() {
        return null;
    }

    @Override
    public Set<RoleModel> getClientRoleMappings(ClientModel app) {
        return null;
    }

    @Override
    public boolean hasRole(RoleModel role) {
        return false;
    }

    @Override
    public void grantRole(RoleModel role) {

    }

    @Override
    public Set<RoleModel> getRoleMappings() {
        return null;
    }

    @Override
    public void deleteRoleMapping(RoleModel role) {

    }
}
