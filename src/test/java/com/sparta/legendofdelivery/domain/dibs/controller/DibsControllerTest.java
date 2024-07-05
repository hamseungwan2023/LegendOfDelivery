package com.sparta.legendofdelivery.domain.dibs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.legendofdelivery.MockSpringSecurityFilter;
import com.sparta.legendofdelivery.domain.dibs.entity.DibsRank;
import com.sparta.legendofdelivery.domain.dibs.mapper.DibsRankMapper;
import com.sparta.legendofdelivery.domain.dibs.service.DibsService;
import com.sparta.legendofdelivery.domain.user.entity.User;
import com.sparta.legendofdelivery.domain.user.entity.UserRole;
import com.sparta.legendofdelivery.domain.user.security.UserDetailsImpl;
import com.sparta.legendofdelivery.global.config.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = DibsController.class, excludeFilters =
        {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        })
public class DibsControllerTest {

    private MockMvc mvc;

    private Principal principal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DibsService dibsService;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
        getPrincipal();
    }

    private void getPrincipal() {

        User user = new User();
        user.setUserId("testname1234");
        user.setRole(UserRole.USER);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        principal = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }
//
//    @Test
//    @DisplayName("가게 찜 등록 성공 테스트")
//    void addDibs() throws Exception {
//
//        //given
//        Long storeId = 1L;
//        MessageResponse response = new MessageResponse(200, "가게 찜에 성공했습니다.");
//
//        when(dibsService.addDibs(eq(storeId), any(User.class))).thenReturn(response);
//
//        // when/then
//        mvc.perform(post("/api/dibs/{storeId}", storeId)
//                .contentType(MediaType.APPLICATION_JSON)
//                .principal(principal))
//                .andExpect(status().isOk());
//
//    }

    @Test
    void deleteDibs() {
    }

    @Test
    void getAllDibsByUser() {
    }

    @Test
    @DisplayName("찜 많은 TOP10 가게 조회")
    void getAllDibsByUserTop10() throws Exception {
        //given
        List<DibsRankMapper> dibsRank = new ArrayList<>();
        dibsRank.add(new DibsRank(1, "Store 1"));
        dibsRank.add(new DibsRank(2, "Store 2"));
        dibsRank.add(new DibsRank(10, "Store 3"));
        dibsRank.add(new DibsRank(3, "Store 4"));
        dibsRank.add(new DibsRank(4, "Store 5"));
        dibsRank.add(new DibsRank(5, "Store 6"));
        dibsRank.add(new DibsRank(6, "Store 7"));
        dibsRank.add(new DibsRank(7, "Store 8"));
        dibsRank.add(new DibsRank(7, "Store 9"));
        dibsRank.add(new DibsRank(8, "Store 10"));

        when(dibsService.getDibsRank()).thenReturn(dibsRank);
        //when
        //then
        mvc.perform(get("/api/stores/dibs/rank")
                        .principal(principal))
                .andExpect(status().isOk())
                .andDo(print());
    }

}
