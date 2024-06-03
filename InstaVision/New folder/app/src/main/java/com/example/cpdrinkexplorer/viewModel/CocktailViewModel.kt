import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.cpdrinkexplorer.data.model.Cocktail
import com.example.cpdrinkexplorer.repository.MocktailRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MocktailViewModel : ViewModel() {
    private val repository = MocktailRepository()
    private val _mocktails = MutableStateFlow<List<Cocktail>>(emptyList())
    val mocktails: StateFlow<List<Cocktail>> = _mocktails

    private val _selectedMocktail = MutableStateFlow<Cocktail?>(null)
    val selectedMocktail: StateFlow<Cocktail?> = _selectedMocktail

    init {
        searchMocktails("vodka")
        homeMocktails("Rum")
    }

    fun homeMocktails(query: String) {
        viewModelScope.launch {
            val response = repository.homeMocktails(query)
            _mocktails.value = response.drinks
        }
    }
    fun searchMocktails(query: String) {
        viewModelScope.launch {
            val response = repository.searchMocktails(query)
            _mocktails.value = response.drinks
        }
    }

    fun selectMocktail(id: String) {
        viewModelScope.launch {
            val response = repository.getMocktailDetails(id)
            _selectedMocktail.value = response.drinks.firstOrNull()
        }
    }
}