package com.example.universitydirectoryapplication.viewModel

import com.example.universitydirectoryapplication.data.model.AppDispatcher
import com.example.universitydirectoryapplication.data.model.University
import com.example.universitydirectoryapplication.data.network.UniversityRepository
import com.example.universitydirectoryapplication.ui.screen.search.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

val university = listOf(
    University("Harvard University", "United States", listOf("https://www.harvard.edu"))
)

@ExperimentalCoroutinesApi
class UniversityTestViewModel {

    @get:Rule
    val universityTestRule = UniversityTestRule()
    private val universityRepository = mock<UniversityRepository>()
    private val testDispatcher = AppDispatcher(UnconfinedTestDispatcher())
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        viewModel = SearchViewModel(universityRepository,testDispatcher)
    }

    @Test
    fun testFetchUniversities_success() = runTest {
        whenever(universityRepository.getUniversityByCountry("United States")).thenReturn(university)
        viewModel.searchUniversity("United States")
        val universities = viewModel.searchUniversity.value
        assertEquals(university, universities)
    }

    @Test
    fun testFetchUniversities_failure() = runTest {
        whenever(universityRepository.getUniversityByCountry("United States")).thenReturn(emptyList())
        viewModel.searchUniversity("United States")
        val universities = viewModel.searchUniversity.value
        assertTrue(universities.isEmpty())
    }

    @Test
    fun testLoadingState_duringSearch() = runTest {
        whenever(universityRepository.getUniversityByCountry("United States")).thenReturn(university)

        val job = launch {
            viewModel.searchUniversity("United States")
        }
        assertTrue(viewModel.isLoading.value)
        // Wait for the search to complete
        advanceUntilIdle()
        job.join()
        assertFalse(viewModel.isLoading.value)
    }
}