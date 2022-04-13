package com.anffercastillo.dto;

import com.anffercastillo.models.User;

import java.util.Objects;

public class UserDTO {

  private long id;
  private String name;

  public String getPseudonym() {
    return pseudonym;
  }

  public void setPseudonym(String pseudonym) {
    this.pseudonym = pseudonym;
  }

  private String pseudonym;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public static UserDTO buildUserDTO(User user) {
    var dto = new UserDTO();
    dto.setId(user.getId());
    dto.setName(user.getName());
    dto.setPseudonym(user.getPseudonym());
    return dto;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserDTO userDTO = (UserDTO) o;
    return id == userDTO.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
