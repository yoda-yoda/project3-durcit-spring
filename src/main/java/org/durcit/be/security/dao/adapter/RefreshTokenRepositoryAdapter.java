package org.durcit.be.security.dao.adapter;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.durcit.be.security.dao.RefreshTokenBlackListRepository;
import org.durcit.be.security.dao.RefreshTokenRepository;
import org.durcit.be.security.dao.TokenRepository;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.domian.RefreshToken;
import org.durcit.be.security.domian.RefreshTokenBlackList;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryAdapter implements TokenRepository {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenBlackListRepository refreshTokenBlackListRepository;

    private final EntityManager entityManager;

    @Override
    public RefreshToken save(Member member, String token) {
        return refreshTokenRepository.save(
                RefreshToken.builder()
                        .refreshToken(token)
                        .member(member)
                        .build());
    }


    @Override
    public Optional<RefreshToken> findValidRefTokenByToken(String token) {

        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByRefreshToken(token);

        if ( refreshTokenOptional.isEmpty() ) return refreshTokenOptional;

        RefreshToken findToken = refreshTokenOptional.get();

        boolean isBanned = isBannedRefToken(findToken);

        if ( isBanned ) {
            return Optional.empty();
        } else {
            return refreshTokenOptional;
        }

    }

    @Override
    public Optional<RefreshToken> findValidRefTokenByMemberId(Long memberId) {
        return entityManager.createQuery(
                        "select rf from RefreshToken rf left join RefreshTokenBlackList rtb on rtb.refreshToken = rf where rf.member.id = :memberId and rtb.id is null"
                        , RefreshToken.class)
                .setParameter("memberId", memberId)
                .getResultStream().findFirst();
    }

    @Override
    public RefreshToken appendBlackList(RefreshToken token) {
        refreshTokenBlackListRepository.save(
                RefreshTokenBlackList.builder()
                        .refreshToken(token)
                        .build()
        );
        return token;
    }

    public boolean isBannedRefToken(RefreshToken token) {
        return refreshTokenBlackListRepository.existsByRefreshToken(token);
    }
}
