//package com.weve.service;
//
//import com.weve.domain.User;
//import com.weve.domain.Worry;
//import com.weve.dto.request.CreateWorryRequest;
//import com.weve.dto.response.CreateWorryResponse;
//import com.weve.repository.UserRepository;
//import com.weve.repository.WorryRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@Transactional
//public class WorryServiceTest {
//
//    @Autowired private UserRepository userRepository;
//    @Autowired private WorryRepository worryRepository;
//    @Autowired private WorryService worryService;
//
//    private static final Logger logger = LoggerFactory.getLogger(WorryServiceTest.class);
//
//    @Test
//    @DisplayName("고민 작성 성공")
//    void createWorryTest() {
//
//        // given
//        User user = User.builder().name("주니어").isSenior(false).build();
//        userRepository.save(user);
//
//        CreateWorryRequest request = CreateWorryRequest.builder()
//                .content("content")
//                .isAnonymous(false)
//                .build();
//
//        // when
//        CreateWorryResponse response = worryService.createWorry(user.getId(), request);
//
//        // then
//        assertNotNull(response);
//        assertEquals(user.getId(), response.getWorryId());
//
//        Optional<Worry> savedWorry = worryRepository.findById(response.getWorryId());
//        assertTrue(savedWorry.isPresent());
//        assertEquals("content", savedWorry.get().getContent());
//        assertFalse(savedWorry.get().isAnonymous());
//        assertEquals(user.getId(), savedWorry.get().getJunior().getId());
//    }
//
//}
