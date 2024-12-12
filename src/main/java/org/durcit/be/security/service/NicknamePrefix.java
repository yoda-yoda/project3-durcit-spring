package org.durcit.be.security.service;

import lombok.Getter;

import java.util.Random;

@Getter
public enum NicknamePrefix {
    CUTE("귀여운"),
    CALM("침착한"),
    COOL("쿨한"),
    QUIET("조용한"),
    THINKING("사색하는"),
    EXCITED("신난"),
    LIVELY("활달한"),
    FRIENDLY("친근한"),
    HAPPY("행복한"),
    CURIOUS("호기심 많은"),
    SHY("수줍은"),
    BRIGHT("밝은"),
    STRONG("강한"),
    SMART("똑똑한"),
    RELAXED("느긋한"),
    KIND("친절한"),
    LOVELY("사랑스러운"),
    CHEERFUL("명랑한"),
    DREAMY("몽환적인"),
    GENTLE("온화한"),
    CHARMING("매력적인");

    private final String value;

    NicknamePrefix(String value) {
        this.value = value;
    }

    public static String getRandomPrefix() {
        NicknamePrefix[] values = NicknamePrefix.values();
        return values[new Random().nextInt(values.length)].getValue();
    }

}
