package com.lms.backend.service;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.request.AccountRequest;

import java.util.Optional;

public interface AccountService {
//
    Account createAccount(AccountRequest accountRequset);

    Account findById(long accountId);


    Account findByUsername(String username);

    Account saveOrUpdate(Account account);

    Optional<Account> findByEmail(String email);

    Optional<Account>  checkUsername(String username);

    boolean existsAdmin();
}
