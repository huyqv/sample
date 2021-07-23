package wee.digital.sample.ui.fragment

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import wee.digital.sample.R
import wee.digital.sample.ui.fragment.plash.SplashFragment

@RunWith(AndroidJUnit4::class)
class SplashFragmentTest {


    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testNavigation() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val splashFragment = launchFragmentInContainer<SplashFragment>()
        splashFragment.onFragment { fragment ->
            navController.setGraph(R.navigation.main_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
    }
}