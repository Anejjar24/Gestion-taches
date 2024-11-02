package ma.ensa.projet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import ma.ensa.projet.beans.Task
import ma.ensa.projet.databinding.ItemTaskBinding

class TaskAdapter(
    private var tasks: List<Task>,
    private val onTaskClicked: (Task) -> Unit,
    private val onDeleteClicked: (Task) -> Unit,
    private val onTaskCompleted: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var originalTasks: List<Task> = tasks.toList()
    private var filteredTasks: List<Task> = tasks.toList()

    class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        with(holder.binding) {
            textViewTitle.text = task.title
            textViewDescription.text = task.description

            // Important: On retire temporairement le listener
            checkBoxCompleted.setOnCheckedChangeListener(null)
            checkBoxCompleted.isChecked = task.isCompleted

            checkBoxCompleted.setOnClickListener {
                val newState = !task.isCompleted
                task.isCompleted = newState
                checkBoxCompleted.isChecked = newState
                onTaskCompleted(task)
            }

            buttonDelete.setOnClickListener {
                AlertDialog.Builder(holder.itemView.context)
                    .setTitle("Confirmation")
                    .setMessage("Voulez-vous vraiment supprimer cette tâche ?")
                    .setPositiveButton("Oui") { _, _ ->
                        onDeleteClicked(task)
                    }
                    .setNegativeButton("Non", null)
                    .show()
            }

            root.setOnClickListener { onTaskClicked(task) }
        }
    }
    override fun getItemCount() = tasks.size
    fun updateTasks(newTasks: List<Task>) {
        originalTasks = newTasks.toList()
        filteredTasks = newTasks.toList()
        tasks = newTasks
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        if (query.isEmpty()) {
            filteredTasks = originalTasks
        } else {
            filteredTasks = originalTasks.filter {
                it.title.contains(query, ignoreCase = true)
            }
        }
        tasks = filteredTasks
        notifyDataSetChanged()
    }
}