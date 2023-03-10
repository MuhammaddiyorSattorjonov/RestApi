package com.example.restapigetandadd

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Adapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.restapigetandadd.adapters.RvAdapter
import com.example.restapigetandadd.databinding.ActivityMainBinding
import com.example.restapigetandadd.databinding.ItemDialogBinding
import com.example.restapigetandadd.models.User
import com.example.restapigetandadd.models.myTodoRequest
import com.example.restapigetandadd.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity(), RvAdapter.RvAction {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var adapter: RvAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter = RvAdapter(this)
        loadTodo()
        binding.swipe.setOnRefreshListener {
            loadTodo()
        }
        binding.floatingActionButton.setOnClickListener {
            addTodo()
        }
        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy>0){
                    binding.floatingActionButton.hide()
                }else{
                    binding.floatingActionButton.show()
                }
            }
        })

        binding.rv.adapter = adapter
    }

    @SuppressLint("ResourceType")
    private fun addTodo() {
        val dialog = AlertDialog.Builder(this).create()
        val itemDialogBinding = ItemDialogBinding.inflate(layoutInflater)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        itemDialogBinding.muddat.setOnClickListener {
            val dp = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { datePicker, mYear, mMonth, mDay ->
                itemDialogBinding.muddat.setText(""+mDay+"/"+mMonth+"/"+mYear)
            },year,month,day)
            dp.show()
        }
        itemDialogBinding.btnAdd.setOnClickListener {
            val myToDoRequest = myTodoRequest(itemDialogBinding.sarlavha.text.toString(),
                itemDialogBinding.manti.text.toString(),
                itemDialogBinding.muddat.text.toString(),
                itemDialogBinding.holat.selectedItem.toString()
            )

            itemDialogBinding.prodress.visibility = View.VISIBLE
            ApiClient.getApiService().addTodo(myToDoRequest)
                .enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        itemDialogBinding.prodress.visibility = View.GONE
                        Toast.makeText(this@MainActivity,
                            "${response.body()?.id} id bilan saqlandi",
                            Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        itemDialogBinding.prodress.visibility = View.GONE
                        Toast.makeText(this@MainActivity, "Xatolik Yuz Berdi", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            dialog.cancel()
        }
        dialog.setView(itemDialogBinding.root)
        dialog.show()
    }

    fun loadTodo() {
        ApiClient.getApiService().getAllTodo()
            .enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        binding.progress.visibility = View.GONE
                        adapter.list.clear()
                        adapter.list.addAll(response.body()!!)
                        adapter.notifyDataSetChanged()
                        binding.swipe.isRefreshing = false
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(this@MainActivity,
                        "Internetga bog'lanishni tekshiring ",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.swipe.isRefreshing = false
                }

            })
    }

    override fun deleteToDo(user: User) {
        ApiClient.getApiService().deleteToDo(user.id)
            .enqueue(object : Callback<Int> {
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "${response.code()}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<Int>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Internett bilan muammo", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    override fun updateToDo(user: User) {
        val dialog = AlertDialog.Builder(this).create()
        val itemDialogBinding = ItemDialogBinding.inflate(layoutInflater)
        dialog.setView(itemDialogBinding.root)
        itemDialogBinding.apply {
            sarlavha.setText(user.sarlavha)
            muddat.setText(user.oxirgi_muddat)
            manti.setText(user.matn)

            when (user.holat) {
                "Yangi" -> holat.setSelection(0)
                "Bajarilmoqda" -> holat.setSelection(1)
                "Tuggalandi" -> holat.setSelection(2)
            }
        }
        itemDialogBinding.btnAdd.setOnClickListener {
            user.sarlavha = itemDialogBinding.sarlavha.text.toString()
            user.matn = itemDialogBinding.manti.text.toString()
            user.holat = itemDialogBinding.holat.selectedItem.toString()
            user.oxirgi_muddat = itemDialogBinding.muddat.text.toString()

            ApiClient.getApiService().updateToDo(user.id,
                myTodoRequest(user.sarlavha, user.matn, user.oxirgi_muddat, user.holat))
                .enqueue(object : Callback<User>{
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful){
                            Toast.makeText(this@MainActivity, "Updated", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Update da hatolik", Toast.LENGTH_SHORT).show()
                    }
                })
            dialog.cancel()
        }
        dialog.show()
    }
}