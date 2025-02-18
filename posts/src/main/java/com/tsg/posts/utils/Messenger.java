package com.tsg.posts.utils;

import com.tsg.commons.models.enums.CodeEnum;

public interface Messenger {

    String getMessage (CodeEnum code);

    String getMessage(CodeEnum code, Object... args);
}
