package com.dio.projeto.todolist.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dio.projeto.todolist.databinding.ActivityAddTaskBinding
import com.dio.projeto.todolist.datasource.TaskDataSource
import com.dio.projeto.todolist.extensions.format
import com.dio.projeto.todolist.extensions.text
import com.dio.projeto.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddNewTaskActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.title.text = it.title
                binding.data.text = it.date
                binding.hora.text = it.hora
            }
        }

        insertListeners()
    }

    private fun insertListeners() {
        binding.data.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1

                binding.data.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.hora.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
            timePicker.addOnPositiveButtonClickListener{
                val minuto = if (timePicker.minute in 0..9) {
                    "0${timePicker.minute}"
                } else {
                    timePicker.minute
                }

                val hora = if (timePicker.hour in 0..9) {
                    "0${timePicker.hour}"
                } else {
                    timePicker.hour
                }
                binding.hora.text = "$hora:$minuto"
            }
            timePicker.show(supportFragmentManager,null)
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }

        binding.criarTarefa.setOnClickListener {
            val task = Task(
                title = binding.title.text,
                date = binding.data.text,
                hora = binding.hora.text,
                id = intent.getIntExtra(TASK_ID, 0)
                )
            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}