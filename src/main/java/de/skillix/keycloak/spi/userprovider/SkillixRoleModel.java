package de.skillix.keycloak.spi.userprovider;

import org.keycloak.models.RoleContainerModel;
import org.keycloak.models.RoleModel;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class SkillixRoleModel implements RoleModel {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public boolean isComposite() {
        return false;
    }

    @Override
    public void addCompositeRole(RoleModel role) {

    }

    @Override
    public void removeCompositeRole(RoleModel role) {

    }

    @Override
    public Stream<RoleModel> getCompositesStream(String search, Integer first, Integer max) {
        return null;
    }

    @Override
    public boolean isClientRole() {
        return false;
    }

    @Override
    public String getContainerId() {
        return null;
    }

    @Override
    public RoleContainerModel getContainer() {
        return null;
    }

    @Override
    public boolean hasRole(RoleModel role) {
        return false;
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
    public Stream<String> getAttributeStream(String name) {
        return null;
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        return null;
    }
}
