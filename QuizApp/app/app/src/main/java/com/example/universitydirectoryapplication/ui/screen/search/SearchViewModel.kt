package com.example.universitydirectoryapplication.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universitydirectoryapplication.data.model.AppDispatcher
import com.example.universitydirectoryapplication.data.model.University
import com.example.universitydirectoryapplication.data.network.UniversityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: UniversityRepository,
    private val appDispatcher:AppDispatcher
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _searchedText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchedText

    private val _searchUniversity = MutableStateFlow<List<University>>(emptyList())
    val searchUniversity: StateFlow<List<University>> = _searchUniversity

    private val _selectedCountry = MutableStateFlow("")

    init {
        searchUniversity("Afghanistan")
    }

    fun onSearchedTextChange(search: String) {
        _searchedText.value = search
    }
    fun onSearchedCountryChange(country: String) {
        _selectedCountry.value = country
    }

    fun searchUniversity(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            withContext(appDispatcher.IO) {
                try {
                    val response = repository.getUniversityByCountry(query)
                    _searchUniversity.value = response
                } catch (e: Exception) {
                    println(e.message)
                }finally {
                    _isLoading.value = false
                }
            }
        }
    }
}




//--------------------------------------------------------
//package com.example.universitydirectoryapplication.viewModel
//
//import com.example.universitydirectoryapplication.data.model.AppDispatcher
//import com.example.universitydirectoryapplication.data.model.University
//import com.example.universitydirectoryapplication.data.network.UniversityRepository
//import com.example.universitydirectoryapplication.ui.screen.search.SearchViewModel
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.TestCoroutineDispatcher
//import kotlinx.coroutines.test.advanceUntilIdle
//import kotlinx.coroutines.test.runTest
//import org.junit.Assert.assertEquals
//import org.junit.Assert.assertFalse
//import org.junit.Assert.assertTrue
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.mockito.kotlin.mock
//import org.mockito.kotlin.whenever
//
//val university = listOf(
//    University("Harvard University", "United States", listOf("https://www.harvard.edu"))
//)
//
//@ExperimentalCoroutinesApi
//class UniversityTestViewModel {
//
//    @get:Rule
//    val universityTestRule = UniversityTestRule()
//    private val universityRepository = mock<UniversityRepository>()
//    private val testDispatcher = TestCoroutineDispatcher()
//    private lateinit var viewModel: SearchViewModel
//
//    @Before
//    fun setUp() {
//        viewModel = SearchViewModel(universityRepository, AppDispatcher(testDispatcher))
//    }
//
//    @Test
//    fun testFetchUniversities_success() = runTest(testDispatcher) {
//        whenever(universityRepository.getUniversityByCountry("United States")).thenReturn(university)
//
//        viewModel.searchUniversity("United States")
//        advanceUntilIdle()
//
//        val universities = viewModel.searchUniversity.value
//        assertEquals(university, universities)
//    }
//
//    @Test
//    fun testFetchUniversities_failure() = runTest(testDispatcher) {
//        whenever(universityRepository.getUniversityByCountry("United States")).thenReturn(emptyList())
//
//        viewModel.searchUniversity("United States")
//        advanceUntilIdle()
//
//        val universities = viewModel.searchUniversity.value
//        assertTrue(universities.isEmpty())
//    }
//
//    @Test
//    fun testLoadingState_duringSearch() = runTest(testDispatcher) {
//        whenever(universityRepository.getUniversityByCountry("United States")).thenReturn(university)
//
//        // Check initial loading state
//        assertFalse(viewModel.isLoading.value)
//
//        viewModel.searchUniversity("United States")
//
//        // Check loading state during search
//        assertTrue(viewModel.isLoading.value)
//
//        // Wait for the search to complete
//        advanceUntilIdle()
//
//        // Check loading state after search completes
//        assertFalse(viewModel.isLoading.value)
//    }
//}


//-----------------------------------------------------------------------------------
//package com.example.universitydirectoryapplication.ui.screen.search
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.universitydirectoryapplication.data.model.AppDispatcher
//import com.example.universitydirectoryapplication.data.model.University
//import com.example.universitydirectoryapplication.data.network.UniversityRepository
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//
//class SearchViewModel(
//    private val universityRepository: UniversityRepository,
//    private val dispatcher: AppDispatcher
//) : ViewModel() {
//
//    private val _searchUniversity = MutableStateFlow<List<University>>(emptyList())
//    val searchUniversity: StateFlow<List<University>> = _searchUniversity
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading
//
//    fun searchUniversity(country: String) {
//        viewModelScope.launch(dispatcher.io) {
//            _isLoading.value = true
//            val result = universityRepository.getUniversityByCountry(country)
//            _searchUniversity.value = result
//            _isLoading.value = false
//        }
//    }
//}
