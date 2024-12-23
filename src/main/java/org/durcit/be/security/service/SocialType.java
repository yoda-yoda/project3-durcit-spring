package org.durcit.be.security.service;

import org.durcit.be.security.domian.MemberDetails;

import java.util.Map;

public enum SocialType {
    GOOGLE {
        @Override
        public MemberDetails createMemberDetails(Map<String, Object> attributes) {
            return MemberDetails.builder()
                    .name(attributes.get("name").toString())
                    .email(attributes.get("email").toString())
                    .attributes(attributes)
                    .build();
        }
    },
    KAKAO {
        @Override
        public MemberDetails createMemberDetails(Map<String, Object> attributes) {
            Map<String, String> properties = (Map<String, String>) attributes.get("properties");
            return MemberDetails.builder()
                    .name(properties.get("nickname"))
                    .email(attributes.get("id").toString() + "kakao.com")
                    .attributes(attributes)
                    .build();
        }
    },
    NAVER {
        @Override
        public MemberDetails createMemberDetails(Map<String, Object> attributes) {
            Map<String, String> response = (Map<String, String>) attributes.get("response");
            return MemberDetails.builder()
                    .name(response.get("name"))
                    .email(response.get("email"))
                    .attributes(attributes)
                    .build();
        }
    };

    public abstract MemberDetails createMemberDetails(Map<String, Object> attributes);
}