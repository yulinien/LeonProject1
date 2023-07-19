package com.example.leonproject.dao.repository;

import com.example.leonproject.dao.entity.AccountDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "/application-test.properties")
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setup() {
        AccountDO accountDO = AccountDO.builder()
                .username("testUser")
                .password("123456789").build();
        accountRepository.save(accountDO);

    }

    @Test
    public void accountRepository_ExistsByUsername_ReturnsTrueOrFalse() {

        Boolean trueResult = accountRepository.existsByUsername("testUser");
        Boolean falseResult = accountRepository.existsByUsername("testUsers");

        assertEquals(true, trueResult);
        assertEquals(false, falseResult);
    }

    @Test
    public void accountRepository_FindAccountByUserName_ReturnAccountIfExists() {

        Optional<AccountDO> result = accountRepository.findAccountByUsername("testUser");

        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getUsername());
    }
}
