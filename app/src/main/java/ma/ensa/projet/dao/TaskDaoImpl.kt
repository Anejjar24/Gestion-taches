package ma.ensa.projet.dao

import android.content.ContentValues
import android.database.Cursor
import ma.ensa.projet.beans.Task
import ma.ensa.projet.utils.DatabaseHelper

class TaskDaoImpl(private val dbHelper: DatabaseHelper) : TaskDao {
   //Insère une nouvelle tâche dans la base de données
    override fun insertTask(task: Task): Long {
        val db = dbHelper.writableDatabase
        // Préparation des données à insérer
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TITLE, task.title)
            put(DatabaseHelper.COLUMN_DESCRIPTION, task.description)
            put(DatabaseHelper.COLUMN_IS_COMPLETED, if (task.isCompleted) 1 else 0)
        }
        return db.insert(DatabaseHelper.TABLE_TASKS, null, values)
    }

    // Récupère toutes les tâches de la base de données
    override fun getAllTasks(): List<Task> {
        val taskList = mutableListOf<Task>()
        val db = dbHelper.readableDatabase
        // Exécution de la requête SELECT
        val cursor = db.query(
            DatabaseHelper.TABLE_TASKS,
            null,  // null signifie toutes les colonnes
            null,  // pas de clause WHERE
            null,  // pas d'arguments pour WHERE
            null,  // pas de GROUP BY
            null,  // pas de HAVING
            null   // pas de ORDER BY
        )
        // Parcours des résultats
        with(cursor) {
            while (moveToNext()) {
                taskList.add(createTaskFromCursor(this))
            }
        }
        cursor.close()
        return taskList
    }

    // Met à jour une tâche existante
    override fun updateTask(task: Task): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TITLE, task.title)
            put(DatabaseHelper.COLUMN_DESCRIPTION, task.description)
            put(DatabaseHelper.COLUMN_IS_COMPLETED, if (task.isCompleted) 1 else 0)
        }
        return db.update(
            DatabaseHelper.TABLE_TASKS,
            values,
            "${DatabaseHelper.COLUMN_ID} = ?",  // Condition WHERE
            arrayOf(task.id.toString())         // Arguments pour WHERE
        )
    }


     // Supprime une tâche par son ID
    override fun deleteTask(taskId: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseHelper.TABLE_TASKS,
            "${DatabaseHelper.COLUMN_ID} = ?",
            arrayOf(taskId.toString())
        )
    }


     // Récupère les tâches non complétées

    override fun getPendingTasks(): List<Task> {
        return getAllTasks().filter { !it.isCompleted }
    }

    // Récupère les tâches complétées

    override fun getCompletedTasks(): List<Task> {
        return getAllTasks().filter { it.isCompleted }
    }


     // Convertit une ligne de la base de données (Cursor) en objet Task

    private fun createTaskFromCursor(cursor: Cursor): Task {
        return Task(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
            title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE)),
            description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION)),
            isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IS_COMPLETED)) == 1
        )
    }
}