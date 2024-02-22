package com.example.newsapp.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.Adapter.CategoryAdapter
import com.example.newsapp.Adapter.NewsAdapter
import com.example.newsapp.Model.Category
import com.example.newsapp.Model.Country
import com.example.newsapp.Model.DataNewsResponse
import com.example.newsapp.Model.Article
import com.example.newsapp.Network.ApiNews
import com.example.newsapp.databinding.ActivityHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity(), NewsAdapter.OnItemClickListener {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var countryAdapter: ArrayAdapter<String>
    private var countryList = mutableListOf<Country>()

    private var categoryList = mutableListOf<Category>()
    private lateinit var categoryAdapter: CategoryAdapter

    private lateinit var newsAdapter: NewsAdapter
    private var articles = mutableListOf<Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Showing Countries in Spinner
        prepareCountryList()
        countryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, countryList.map { it.countryName })
        binding.spCountry.adapter = countryAdapter

        binding.spCountry.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCountryCode = countryList[position].countryCode
                fetchCountryNews(selectedCountryCode)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        // Showing Category List in  Adapter
        categoryData()
        categoryAdapter = CategoryAdapter(this, categoryList)
        binding.rvCategory.adapter = categoryAdapter
        binding.rvCategory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        //Showing News in RecyclerView
        newsAdapter = NewsAdapter(this, articles, this)
        binding.rvNews.adapter = newsAdapter
        binding.rvNews.layoutManager = LinearLayoutManager(this)

        fetchCountryNews(countryList.first().countryCode)
    }


    private fun prepareCountryList() {
        countryList.add(Country(1, "India", "in"))
        countryList.add(Country(2, "Australia", "au"))
        countryList.add(Country(3, "China", "cn"))
        countryList.add(Country(4, "France", "fr"))
        countryList.add(Country(5, "Hong Kong", "hk"))
        countryList.add(Country(6, "Switzerland", "ch"))
        countryList.add(Country(7, "Saudi Arabia", "sa"))
        countryList.add(Country(8, "Philippines", "ph"))
        countryList.add(Country(9, "Russia", "ru"))
        countryList.add(Country(10, "Germany", "de"))
    }

    private fun categoryData() {
        categoryList.add(Category(1, "general"))
        categoryList.add(Category(2, "entertainment"))
        categoryList.add(Category(3, "business"))
        categoryList.add(Category(4, "health"))
        categoryList.add(Category(5, "science"))
        categoryList.add(Category(6, "sports"))
        categoryList.add(Category(7, "technology"))
    }

    private fun fetchCountryNews(countryCode: String) {
        ApiNews.init().getNews(countryCode).enqueue(object : Callback<DataNewsResponse> {
            override fun onResponse(call: Call<DataNewsResponse>, response: Response<DataNewsResponse>) {


                if (response.isSuccessful) {
                    var article = response.body()?.articles
                    article?.let {
                        newsAdapter.articleList = it
                        newsAdapter.notifyDataSetChanged()

                        categoryAdapter.categoryClickListener = { position, category ->
                            getCategoryNews(countryCode, position, category)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<DataNewsResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getCategoryNews(countryCode: String, position: Int, category: Category) {
        ApiNews.init().getCategoryNews(countryCode, category.category)
            .enqueue(object : Callback<DataNewsResponse> {
                override fun onResponse(call: Call<DataNewsResponse>, response: Response<DataNewsResponse>) {


                    if (response.isSuccessful) {
                        var news = response.body()?.articles
                        news?.let {
                            newsAdapter.articleList = it
                            newsAdapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onFailure(call: Call<DataNewsResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    override fun onItemClick(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}