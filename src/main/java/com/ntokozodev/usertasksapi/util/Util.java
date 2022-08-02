package com.ntokozodev.usertasksapi.util;

import com.ntokozodev.usertasksapi.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    public static void logException(String method, Exception ex) {
        var message = String.format("[%s] exception: { message: {}, type: {} }", method);
        LOG.info(message, ex.getMessage(), ex.getClass().getCanonicalName());
        LOG.error("Exception", ex);
    }

    public static Long parseId(String id) {
        try {
            return Long.parseLong(id);
        } catch (Exception ex) {
            LOG.info("[parseId] failed invalid Id [{}]", id);
            throw new IllegalArgumentException(ex);
        }
    }
}
