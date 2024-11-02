package ma.ensa.projet.dao

import ma.ensa.projet.beans.Task

interface TaskDao {
    fun insertTask(task: Task): Long
    fun getAllTasks(): List<Task>
    fun updateTask(task: Task): Int
    fun deleteTask(taskId: Long): Int
    fun getPendingTasks(): List<Task>
    fun getCompletedTasks(): List<Task>
}