package com.programming.common.common_auth;

import java.util.Set;

import static com.programming.common.common_auth.Role.DOAN_TRUONG;
import static com.programming.common.common_auth.Role.PHO_NOI;
import static com.programming.common.common_auth.Role.PHO_NGOAI;
import static com.programming.common.common_auth.Role.THU_KY;
import static com.programming.common.common_auth.Role.THU_QUY;
import static com.programming.common.common_auth.Role.HUYNH_TRUONG;
import static com.programming.common.common_auth.Role.DU_TRUONG;

public class RoleUtils {
    public static boolean isExecutiveBoard(Role role) {
        return Set.of(DOAN_TRUONG, PHO_NOI, PHO_NGOAI, THU_KY, THU_QUY).contains(role);
    }

    public static boolean isCatechist(Role role) {
        return Set.of(HUYNH_TRUONG, DU_TRUONG,
                DOAN_TRUONG, PHO_NOI, PHO_NGOAI, THU_KY, THU_QUY
        ).contains(role);
    }
}
