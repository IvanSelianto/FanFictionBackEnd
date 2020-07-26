package com.fanfiction.DTO;

import java.util.Objects;

public class UserDTO {
    private Long userId;
    private String editedName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEditedName() {
        return editedName;
    }

    public void setEditedName(String editedName) {
        this.editedName = editedName;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        UserDTO userDTO = (UserDTO) object;
        return Objects.equals(userId, userDTO.userId) &&
                Objects.equals(editedName, userDTO.editedName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, editedName);
    }
}
