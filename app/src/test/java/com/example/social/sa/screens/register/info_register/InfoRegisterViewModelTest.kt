package com.example.social.sa.screens.register.info_register

import app.cash.turbine.test
import com.example.social.sa.FakeDispatcher
import com.example.social.sa.fakeRepo.FakeRegisterRepo
import com.google.common.truth.Truth
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class InfoRegisterViewModelTest{
    private lateinit var infoRegisterViewModel :InfoRegisterViewModel

    @Before
    fun setUp(){
        infoRegisterViewModel = InfoRegisterViewModel(FakeDispatcher(),FakeRegisterRepo())
    }

    @Test
    fun `email is not empty` (){
        runTest {
            infoRegisterViewModel.onEvent(InfoRegisterEvent.EmailChanged("salman@gmail.com"))
            infoRegisterViewModel.state.test {
                Truth.assertThat(awaitItem().email.content).isEqualTo("salman@gmail.com")
            }
        }
    }
}