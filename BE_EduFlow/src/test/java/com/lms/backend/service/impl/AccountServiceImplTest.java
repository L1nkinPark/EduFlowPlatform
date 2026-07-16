package com.lms.backend.service.impl;

import com.lms.backend.exception.DataNotFoundException;
import com.lms.backend.model.entity.Account;
import com.lms.backend.model.request.AccountRequest;
import com.lms.backend.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setAccountId(1L);
        account.setFullName("John Doe");
        account.setUsername("johndoe");
        account.setEmail("john@example.com");
    }

    @Test
    void testCreateAccount_New() {
        AccountRequest request = new AccountRequest();
        request.setAccountId(0L);
        request.setFullName("Jane Doe");

        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        Account result = accountService.createAccount(request);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getFullName());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testCreateAccount_Update_Success() {
        AccountRequest request = new AccountRequest();
        request.setAccountId(1L);
        request.setFullName("Jane Doe Updated");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        Account result = accountService.createAccount(request);

        assertNotNull(result);
        assertEquals(1L, result.getAccountId());
        assertEquals("Jane Doe Updated", result.getFullName());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testCreateAccount_Update_NotFound() {
        AccountRequest request = new AccountRequest();
        request.setAccountId(2L);

        when(accountRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> accountService.createAccount(request));
        verify(accountRepository, times(1)).findById(2L);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void testFindById_Found() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account result = accountService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getAccountId());
    }

    @Test
    void testFindById_NotFound() {
        when(accountRepository.findById(2L)).thenReturn(Optional.empty());

        Account result = accountService.findById(2L);

        assertNull(result);
    }

    @Test
    void testFindByUsername() {
        when(accountRepository.findByUsername("johndoe")).thenReturn(Optional.of(account));

        Account result = accountService.findByUsername("johndoe");

        assertNotNull(result);
        assertEquals("johndoe", result.getUsername());
    }

    @Test
    void testSaveOrUpdate() {
        when(accountRepository.save(account)).thenReturn(account);

        Account result = accountService.saveOrUpdate(account);

        assertNotNull(result);
        assertEquals(1L, result.getAccountId());
    }

    @Test
    void testFindByEmail() {
        when(accountRepository.findByEmail("john@example.com")).thenReturn(Optional.of(account));

        Optional<Account> result = accountService.findByEmail("john@example.com");

        assertTrue(result.isPresent());
        assertEquals("john@example.com", result.get().getEmail());
    }

    @Test
    void testCheckUsername() {
        when(accountRepository.findByUsername("johndoe")).thenReturn(Optional.of(account));

        Optional<Account> result = accountService.checkUsername("johndoe");

        assertTrue(result.isPresent());
        assertEquals("johndoe", result.get().getUsername());
    }
}
