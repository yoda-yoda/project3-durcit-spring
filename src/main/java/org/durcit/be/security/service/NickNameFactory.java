package org.durcit.be.security.service;

import java.util.Random;

public class NickNameFactory {

    public static String createNickname() {
        return NicknamePrefix.getRandomPrefix() + "_" + NicknameSuffix.getRandomSuffix() + "_" + getThreeDigitRandomNumber();
    }

    private static int getThreeDigitRandomNumber() {
        return (int) (Math.random() * 900) + 100;
    }

}
