package com.lms.backend.service.impl;

import com.lms.backend.exception.DataNotFoundException;
import com.lms.backend.model.entity.Account;
import com.lms.backend.model.request.AccountRequest;
import com.lms.backend.repository.AccountRepository;
import com.lms.backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
//
    @Autowired
    AccountRepository accountRepository;

    @Override
    public Account createAccount(AccountRequest accountRequset) {
        Account account = null;
        if (accountRequset == null || accountRequset.getAccountId() == 0) {
            account = new Account();
        } else {
            account = findById(accountRequset.getAccountId());
            if(account == null){
                throw new DataNotFoundException();
            }
        }
        account.setFullName(accountRequset.getFullName());
        account.setAccountId(accountRequset.getAccountId());


        return accountRepository.save(account);
    }

    @Override
    public Account findById(long accountId){
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        } else {
            return null;
        }
    }

    @Override
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username).orElse(null);
    }


    @Override
    public Account saveOrUpdate(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public Optional<Account> checkUsername(String username){
        return accountRepository.findByUsername(username);
    }
}


