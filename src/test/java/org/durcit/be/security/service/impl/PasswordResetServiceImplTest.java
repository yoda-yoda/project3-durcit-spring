package org.durcit.be.security.service.impl;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.dto.PasswordResetRequest;
import org.durcit.be.security.repository.MemberRepository;
import org.durcit.be.security.service.MemberService;
import org.durcit.be.security.util.MockSecurityUtil;
import org.durcit.be.system.exception.auth.InvalidChkPasswordWithNewPassword;
import org.durcit.be.system.exception.auth.InvalidPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordResetServiceImplTest {

    @InjectMocks
    private PasswordResetServiceImpl passwordResetService;

    @Mock
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Member testMember;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testMember = Member.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .build();
        MockSecurityUtil.mockSecurityContext(1L);
    }

    @Test
    @DisplayName("비밀번호 재설정 - 인증코드 이메일 발송")
    void sendVerificationCode_shouldSendEmail() {
        // given
        when(memberService.getById(1L)).thenReturn(testMember);

        // when
        passwordResetService.sendVerificationCode();

        // then
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();
        assertThat(sentMessage.getTo()).contains("test@example.com");
        assertThat(sentMessage.getSubject()).isEqualTo("Password Reset Verification Code");
        assertThat(sentMessage.getText()).contains("Your verification code is:");
    }

    @Test
    @DisplayName("비밀번호 재설정 - 올바른 인증코드 검증")
    void verifyCode_shouldReturnTrueForValidCode() {
        // given
        when(memberService.getById(1L)).thenReturn(testMember);
        passwordResetService.sendVerificationCode(); // 인증코드 생성 및 저장
        String validCode = passwordResetService.verificationStore.get(testMember.getEmail()).getCode();

        // when
        boolean result = passwordResetService.verifyCode(validCode);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("비밀번호 재설정 - 만료된 인증코드 검증 실패")
    void verifyCode_shouldReturnFalseForExpiredCode() throws InterruptedException {
        // given
        when(memberService.getById(1L)).thenReturn(testMember);
        passwordResetService.sendVerificationCode();
        Thread.sleep(5001); // 5초 후 만료 처리

        // when
        boolean result = passwordResetService.verifyCode("dummyCode");

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("비밀번호 변경 - 현재 비밀번호 불일치로 실패")
    void changePassword_shouldFailForInvalidCurrentPassword() {
        // given
        PasswordResetRequest request = new PasswordResetRequest();
        request.setCurrentPassword("wrongPassword");
        request.setNewPassword("newPassword");
        request.setConfirmPassword("newPassword");

        when(memberService.getById(1L)).thenReturn(testMember);
        when(memberRepository.findByEmail(testMember.getEmail())).thenReturn(Optional.of(testMember));
        when(passwordEncoder.matches("wrongPassword", testMember.getPassword())).thenReturn(false);

        // when / then
        assertThatThrownBy(() -> passwordResetService.changePassword(request))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    @DisplayName("비밀번호 변경 - 새 비밀번호와 확인 비밀번호 불일치로 실패")
    void changePassword_shouldFailForMismatchNewAndConfirmPassword() {
        // given
        PasswordResetRequest request = new PasswordResetRequest();
        request.setCurrentPassword("currentPassword");
        request.setNewPassword("newPassword");
        request.setConfirmPassword("differentPassword");

        when(memberService.getById(1L)).thenReturn(testMember);
        when(memberRepository.findByEmail(testMember.getEmail())).thenReturn(Optional.of(testMember));
        when(passwordEncoder.matches("currentPassword", testMember.getPassword())).thenReturn(true);

        // when / then
        assertThatThrownBy(() -> passwordResetService.changePassword(request))
                .isInstanceOf(InvalidChkPasswordWithNewPassword.class);
    }

}