package com.example.notesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.adapter.NotesAdapter
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.models.model.Note
import com.example.notesapp.models.viewModel.NotesViewModel

class MainActivity : AppCompatActivity(), NotesAdapter.notesItemClickListerner, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dataBase : NoteDatabase
    lateinit var notesViewModel: NotesViewModel
    lateinit var notesAdapter: NotesAdapter
    lateinit var selectedNote : Note



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initializing the UI
        initUI()

        notesViewModel = ViewModelProvider(this)[NotesViewModel::class.java]


        notesViewModel.allNotes.observe(this) { list ->
            list?.let {
                notesAdapter.updateList(list)
            }
        }

        dataBase = NoteDatabase.getDatabase(this)
    }
    fun initUI(){
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2,LinearLayout.VERTICAL)
        notesAdapter = NotesAdapter(this, this)
        binding.recyclerView.adapter = notesAdapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if (result.resultCode == Activity.RESULT_OK){
                val note = result.data?.getSerializableExtra("note") as? Note
                if (note != null){
                    notesViewModel.insert(note)
                }
            }

        }
        binding.fbAddNote.setOnClickListener {
            val intent = Intent(this,AddNoteActivity::class.java)
            getContent.launch(intent)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null){
                    notesAdapter.filterList(newText)
                }
                return true
            }

        })
    }

    override fun onItemCLicked(note: Note) {
       val intent = Intent(this@MainActivity, AddNoteActivity::class.java)
        intent.putExtra("current_note",note)
        update.launch(intent)
    }

    override fun onLongItemClicked(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView){
        val popUp = PopupMenu(this,cardView)
        popUp.setOnMenuItemClickListener(this@MainActivity)
        popUp.inflate(R.menu.pop_up_menu)
        popUp.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete_note){
            notesViewModel.delete(selectedNote)
            return true
        }
        return true
    }
    private val update = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val note = result.data?.getSerializableExtra("note") as? Note
            if (note != null){
                notesViewModel.updateNote(note)
            }
        }
    }
}