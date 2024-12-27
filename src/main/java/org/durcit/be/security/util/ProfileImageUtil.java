package org.durcit.be.security.util;

public class ProfileImageUtil {
    public static String generateRandomProfileImage(String seed) {
        // DiceBear API를 사용하여 랜덤 이미지 URL 반환
        return "https://api.dicebear.com/7.x/bottts/png?seed=" + seed;
    }
}
