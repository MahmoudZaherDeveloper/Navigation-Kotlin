package com.zaher.navigationapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaher.navigationapplication.R
import com.zaher.navigationapplication.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {
    private lateinit var viewmodel: ListViewModel
    private val dogsListAdapter = PostsListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        //viewmodel = ViewModelProvider.NewInstanceFactory().create(ListViewModel::class.java)
        viewmodel.refresh()

        recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dogsListAdapter
        }

        refreshLayout.setOnRefreshListener {
            recyclerview.visibility = View.GONE
            errorTextView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            refreshLayout.isRefreshing = false
            viewmodel.refreshByPassCash()
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewmodel.Posts.observe(viewLifecycleOwner, Observer { dogs ->
            dogs?.let {
                recyclerview.visibility = View.VISIBLE
                dogsListAdapter.updateDogsList(dogs)
            }

        })
        viewmodel.loadError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                errorTextView.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
        viewmodel.loading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                progressBar.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    errorTextView.visibility = View.GONE
                    recyclerview.visibility = View.GONE
                }
            }
        })

    }
}