package org.durcit.be.security.dao;


import org.durcit.be.security.domian.RefreshToken;
import org.durcit.be.security.domian.RefreshTokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenBlackListRepository extends JpaRepository<RefreshTokenBlackList, Long> {
    boolean existsByRefreshToken(RefreshToken refreshToken);
}
