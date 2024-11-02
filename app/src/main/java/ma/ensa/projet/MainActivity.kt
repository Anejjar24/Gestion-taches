package ma.ensa.projet



import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import android.widget.TabHost
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ma.ensa.projet.adapter.TaskAdapter
import ma.ensa.projet.beans.Task
import ma.ensa.projet.dao.TaskDaoImpl
import ma.ensa.projet.databinding.ActivityMainBinding
import ma.ensa.projet.databinding.DialogAddTaskBinding
import ma.ensa.projet.service.TaskService
import ma.ensa.projet.utils.DatabaseHelper
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskService: TaskService
    private lateinit var menu: Menu
    private lateinit var tabHost: TabHost

    private lateinit var allTasksAdapter: TaskAdapter
    private lateinit var pendingTasksAdapter: TaskAdapter
    private lateinit var completedTasksAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialisation du service
        val dbHelper = DatabaseHelper(this)
        val taskDao = TaskDaoImpl(dbHelper)
        taskService = TaskService(taskDao)

        setupTabs()
        setupRecyclerViews()

        binding.fabAddTask.setOnClickListener {
            showAddTaskDialog()
        }
    }
    private fun setupTabs() {
        tabHost = findViewById(android.R.id.tabhost)
        tabHost.setup()

        // Onglet Toutes les tâches
        var spec = tabHost.newTabSpec("all")
        spec.setIndicator("Toutes")
        spec.setContent(R.id.tab_all_tasks)
        tabHost.addTab(spec)

        // Onglet En cours
        spec = tabHost.newTabSpec("pending")
        spec.setIndicator("En cours")
        spec.setContent(R.id.tab_pending)
        tabHost.addTab(spec)

        // Onglet Terminées
        spec = tabHost.newTabSpec("completed")
        spec.setIndicator("Terminées")
        spec.setContent(R.id.tab_completed)
        tabHost.addTab(spec)

        // Gestion du changement d'onglet
        tabHost.setOnTabChangedListener { tabId ->
            // Réinitialiser la recherche lors du changement d'onglet
            val searchItem = menu.findItem(R.id.action_search)
            val searchView = searchItem.actionView as SearchView
            searchView.setQuery("", false)
            searchView.isIconified = true

            // Mettre à jour les données de l'onglet actif
            updateCurrentTab()
        }
    }
    private fun setupRecyclerViews() {
        allTasksAdapter = TaskAdapter(
            tasks = taskService.getAllTasks(),
            onTaskClicked = { task -> showTaskDetailsDialog(task) },
            onDeleteClicked = { task -> deleteTask(task.id) },
            onTaskCompleted = { task ->
                taskService.updateTask(task)
                Handler(Looper.getMainLooper()).postDelayed({
                    updateRecyclerViews()
                }, 100)
            }
        )

        pendingTasksAdapter = TaskAdapter(
            tasks = taskService.getPendingTasks(),
            onTaskClicked = { task -> showTaskDetailsDialog(task) },
            onDeleteClicked = { task -> deleteTask(task.id) },
            onTaskCompleted = { task ->
                taskService.updateTask(task)
                Handler(Looper.getMainLooper()).postDelayed({
                    updateRecyclerViews()
                }, 100)
            }
        )

        completedTasksAdapter = TaskAdapter(
            tasks = taskService.getCompletedTasks(),
            onTaskClicked = { task -> showTaskDetailsDialog(task) },
            onDeleteClicked = { task -> deleteTask(task.id) },
            onTaskCompleted = { task ->
                taskService.updateTask(task)
                Handler(Looper.getMainLooper()).postDelayed({
                    updateRecyclerViews()
                }, 100)
            }
        )

        // Configuration des RecyclerViews avec leurs layouts
        binding.recyclerViewAllTasks.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = allTasksAdapter
        }

        binding.recyclerViewPending.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = pendingTasksAdapter
        }

        binding.recyclerViewCompleted.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = completedTasksAdapter
        }
    }

    private fun showAddTaskDialog() {
        val dialogBinding = DialogAddTaskBinding.inflate(layoutInflater)
        AlertDialog.Builder(this)
            .setTitle("Nouvelle tâche")
            .setView(dialogBinding.root)
            .setPositiveButton("Ajouter") { _, _ ->
                val title = dialogBinding.editTextTitle.text.toString()
                val description = dialogBinding.editTextDescription.text.toString()

                if (title.isNotEmpty()) {
                    val task = Task(
                        id = System.currentTimeMillis(),
                        title = title,
                        description = description
                    )
                    taskService.addTask(task)
                    updateRecyclerViews()
                }
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    fun updateRecyclerViews() {
        allTasksAdapter.updateTasks(taskService.getAllTasks())
        pendingTasksAdapter.updateTasks(taskService.getPendingTasks())
        completedTasksAdapter.updateTasks(taskService.getCompletedTasks())
    }

    private fun deleteTask(taskId: Long) {
        taskService.deleteTask(taskId)
        updateRecyclerViews()
    }

    private fun showTaskDetailsDialog(task: Task) {
        val dialogBinding = DialogAddTaskBinding.inflate(layoutInflater)

        // Pré-remplir les champs avec les données de la tâche
        dialogBinding.editTextTitle.setText(task.title)
        dialogBinding.editTextDescription.setText(task.description)

        AlertDialog.Builder(this)
            .setTitle("Détails de la tâche")
            .setView(dialogBinding.root)
            .setPositiveButton("Modifier") { _, _ ->
                val updatedTitle = dialogBinding.editTextTitle.text.toString()
                val updatedDescription = dialogBinding.editTextDescription.text.toString()

                if (updatedTitle.isNotEmpty()) {
                    // Mise à jour de la tâche dans SQLite
                    val updatedTask = task.copy(
                        title = updatedTitle,
                        description = updatedDescription
                    )
                    taskService.updateTask(updatedTask)
                    updateRecyclerViews()
                }
            }
            .setNeutralButton("Supprimer") { _, _ ->
                taskService.deleteTask(task.id)
                updateRecyclerViews()
            }
            .setNegativeButton("Fermer", null)
            .show()
    }
    private fun updateCurrentTab() {
        when (tabHost.currentTabTag) {
            "all" -> allTasksAdapter.updateTasks(taskService.getAllTasks())
            "pending" -> pendingTasksAdapter.updateTasks(taskService.getPendingTasks())
            "completed" -> completedTasksAdapter.updateTasks(taskService.getCompletedTasks())
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Sauvegardez le menu
        this.menu = menu
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Appliquer la recherche à l'adapter de l'onglet actuel
                val query = newText ?: ""
                when (tabHost.currentTabTag) {
                    "all" -> allTasksAdapter.filter(query)
                    "pending" -> pendingTasksAdapter.filter(query)
                    "completed" -> completedTasksAdapter.filter(query)
                }
                return true
            }
        })

        return true
    }

}




