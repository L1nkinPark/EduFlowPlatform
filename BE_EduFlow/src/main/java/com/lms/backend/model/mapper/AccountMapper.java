package com.lms.backend.model.mapper;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.response.AccountResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountMapper {


    public AccountResponse convertToDTO(Account account) {
        if(account ==  null) {
            return null;
        }
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccountId(account.getAccountId());
        accountResponse.setFullName(account.getFullName());
        accountResponse.setEmail(account.getEmail());
        accountResponse.setPhone(account.getPhone());
        accountResponse.setUserName(account.getUsername());
        accountResponse.setBirthday(account.getBirthday());
        accountResponse.setRole(account.getRole());
        accountResponse.setStatus(account.isStatus());
        return accountResponse;
    }

    public List<AccountResponse> convertToDTO(List<Account> accountList)
    {
        if(accountList == null) {
            return null;
        }
        List <AccountResponse> accountResponseList = new ArrayList<>();
        for(Account account : accountList) {
            AccountResponse accountResponse = convertToDTO(account);
            accountResponseList.add(accountResponse);
        }
        return accountResponseList;
    }
}
