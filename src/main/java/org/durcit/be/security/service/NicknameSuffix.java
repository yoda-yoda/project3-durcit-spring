package org.durcit.be.security.service;

import lombok.Getter;

import java.util.Random;

@Getter
public enum NicknameSuffix {
    CAT("고양이"),
    DOG("강아지"),
    BOTTLE("물병"),
    MUG("머그컵"),
    DOLPHIN("돌고래"),
    SPROUT("새싹"),
    TREE("나무"),
    CLOUD("구름"),
    PILLOW("베개"),
    STAR("별"),
    SUN("해"),
    MOON("달"),
    CANDLE("촛불"),
    LAMP("램프"),
    RAIN("비"),
    FLOWER("꽃"),
    RABBIT("토끼"),
    OWL("부엉이"),
    CHICK("병아리"),
    FISH("물고기"),
    LEAF("잎사귀");

    private final String value;

    NicknameSuffix(String value) {
        this.value = value;
    }

    public static String getRandomSuffix() {
        NicknameSuffix[] values = NicknameSuffix.values();
        return values[new Random().nextInt(values.length)].getValue();
    }

}
