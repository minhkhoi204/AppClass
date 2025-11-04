package com.programming.common.common_auth;

public enum Role {
    DU_TRUONG("du_truong"),
    HUYNH_TRUONG("huynh_truong"),
    DOAN_TRUONG("doan_truong"),
    PHO_NOI("pho_noi"),
    PHO_NGOAI("pho_ngoai"),
    THU_QUY("thu_quy"),
    THU_KY("thu_ky"),
    THIEU_NHI("thieu_nhi");

    private final String text;

    Role(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
