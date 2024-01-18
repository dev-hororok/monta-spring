package com.hororok.monta.service;

import com.hororok.monta.entity.Account;
import com.hororok.monta.repository.AccountRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    public CustomUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String email) {
        return accountRepository.findOneWithRoleByEmail(email)
                .map(account -> createMember(email, account))
                .orElseThrow(() -> new UsernameNotFoundException(email + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private org.springframework.security.core.userdetails.User createMember(String email, Account account) {

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(account.getRole().toString());
        List<GrantedAuthority> grantedAuthorities = Collections.singletonList(grantedAuthority);

        return new org.springframework.security.core.userdetails.User(account.getEmail(),
                account.getPassword(),
                grantedAuthorities);
    }
}