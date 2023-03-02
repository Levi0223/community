package com.levi.community.util;

public interface CommunityConstant {
    int ACTIVATE_SUCCESS = 0;
    int ACTIVATE_REPEAT = 1;
    int ACTIVATE_FAILURE = 2;

    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;
    int REMEMBER_EXPIRED_SECONDS = 3600 * 12 * 100;

    int ENTITY_TYPE_POST = 1;
    int ENTITY_TYPE_COMMENT = 2;
    int ENTITY_TYPE_USER = 3;
}
