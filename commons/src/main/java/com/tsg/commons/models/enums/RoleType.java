package com.tsg.commons.models.enums;

public enum RoleType {

	  USER;

	  private static final String ROLE_PREFIX = "ROLE_";

	  public String getFullRoleName() {
	    return ROLE_PREFIX + this.name();
	  }
}