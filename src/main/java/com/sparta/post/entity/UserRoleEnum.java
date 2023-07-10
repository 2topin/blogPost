package com.sparta.post.entity;

public enum UserRoleEnum {
    USER(Authority.USER),
    ADMIN(Authority.ADMIN);

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }
    // Enum 클래스에 생성자를 만들어 생성 시 authority라는  문자열도 같이 저장하게 함
    public String getAuthority() { //  해당 사용자 역할의 권한 값을 반환
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
