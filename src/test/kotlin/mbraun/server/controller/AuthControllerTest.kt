package mbraun.server.controller

import com.fasterxml.jackson.databind.ObjectMapper
import mbraun.server.model.User
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
internal class AuthControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) {

    val baseUrl = "/api/v1"

    @Nested
    @DisplayName("POST /signUp")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class SignUp {
        @Test
        @DirtiesContext
        fun `should sign up new user`() {
            // given
            val user = User(
                email = "test@test.com",
                password = "password"
            )

            // when
            val performPostRequest = mockMvc.post("${baseUrl}/signUp") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(user)
            }

            // then
            performPostRequest.andDo { print() }.andExpect { status { isCreated() } }

        }


    }

    @Nested
    @DisplayName("POST /signIn")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class SignIn {

        @Test
        fun `should sign in signed up user`() {
            // given
            val user = User(
                email = "marco.braun2013@gmail.com",
                password = "1234"
            )

            // when
            val performPostRequest = mockMvc.post("${baseUrl}/signIn") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(user)
            }

            // then
            performPostRequest.andDo { print() }.andExpect { status { isOk() } }
        }
    }
}