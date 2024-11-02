package ma.ensa.projet.service

import ma.ensa.projet.beans.Task
import ma.ensa.projet.dao.TaskDao


class TaskService(private val taskDao: TaskDao) {
    fun addTask(task: Task): Long {
        return taskDao.insertTask(task)
    }

    fun getAllTasks(): List<Task> {
        return taskDao.getAllTasks()
    }

    fun updateTask(task: Task): Boolean {
        return taskDao.updateTask(task) > 0
    }

    fun deleteTask(taskId: Long): Boolean {
        return taskDao.deleteTask(taskId) > 0
    }

    fun getPendingTasks(): List<Task> {
        return taskDao.getPendingTasks()
    }

    fun getCompletedTasks(): List<Task> {
        return taskDao.getCompletedTasks()
    }
}
