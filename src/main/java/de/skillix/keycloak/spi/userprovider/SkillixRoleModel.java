package de.skillix.keycloak.spi.userprovider;

import lombok.RequiredArgsConstructor;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleContainerModel;
import org.keycloak.models.RoleModel;
import org.keycloak.storage.ReadOnlyException;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class SkillixRoleModel implements RoleModel {

    private final String name;
    private final RealmModel realm;
    private String description;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public void setName(String name) {
        throw new ReadOnlyException("role is read only");
    }

    @Override
    public boolean isComposite() {
        return false;
    }

    @Override
    public void addCompositeRole(RoleModel role) {
        throw new ReadOnlyException("role is read only");
    }

    @Override
    public void removeCompositeRole(RoleModel role) {
        throw new ReadOnlyException("role is read only");
    }

    @Override
    public Stream<RoleModel> getCompositesStream(String search, Integer first, Integer max) {
        return Stream.empty();
    }

    @Override
    public boolean isClientRole() {
        return false;
    }

    @Override
    public String getContainerId() {
    return realm.getId();
    }

    @Override
    public RoleContainerModel getContainer() {
        return realm;
    }

    @Override
    public boolean hasRole(RoleModel roleModel) {
        return this.equals(roleModel) || this.name.equals(roleModel.getName());
    }

    @Override
    public void setSingleAttribute(String name, String value) {
        throw new ReadOnlyException("role is read only");
    }

    @Override
    public void setAttribute(String name, List<String> values) {
        throw new ReadOnlyException("role is read only");
    }

    @Override
    public void removeAttribute(String name) {
        throw new ReadOnlyException("role is read only");
    }

    @Override
    public Stream<String> getAttributeStream(String name) {
        return Stream.empty();
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        return Map.of();
    }
}
