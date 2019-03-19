package com.chunter.playground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.database.MatrixCursor
import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val suggestionAdapter = object : CursorAdapter(this, null, 0) {
            override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
                return LayoutInflater.from(context).inflate(R.layout.list_item_search, parent, false)
            }

            override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
                view?.findViewById<TextView>(R.id.searchResult)?.text = cursor?.getString(1)
            }
        }

        val suggestions = ArrayList<String>()

        searchView.suggestionsAdapter = suggestionAdapter
        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                searchView.setQuery(suggestions[position], false)
                searchView.clearFocus()
                return true
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.length >= 3) {
                    launch {
                        val searchResult = AnimeRepository().searchAnime(newText)

                        suggestions.clear()
                        suggestions.addAll(searchResult)

                        val columns = arrayOf(
                            BaseColumns._ID,
                            SearchManager.SUGGEST_COLUMN_TEXT_1,
                            SearchManager.SUGGEST_COLUMN_INTENT_DATA
                        )

                        val cursor = MatrixCursor(columns)

                        for (i in 0 until searchResult.size) {
                            val tmp = arrayOf(
                                Integer.toString(i),
                                searchResult[i],
                                searchResult[i]
                            )
                            cursor.addRow(tmp)
                        }
                        suggestionAdapter.swapCursor(cursor)
                    }
                }
                return false
            }
        })
    }
}
