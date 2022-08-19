package com.zaher.navigationapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.zaher.navigationapplication.R
import com.zaher.navigationapplication.databinding.FragmentDetailsBinding
import com.zaher.navigationapplication.viewmodel.DetailsViewModel
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {
    private lateinit var viewmodel: DetailsViewModel
    var uuid: Int = 0
    lateinit var binding: FragmentDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_details, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            uuid = DetailsFragmentArgs.fromBundle(it).uuid
            // detailsTextView.text = uuid.toString()
        }
        viewmodel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        //viewmodel = ViewModelProvider.NewInstanceFactory().create(ListViewModel::class.java)
        viewmodel.fetchPost(uuid)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewmodel.Posts.observe(viewLifecycleOwner, Observer { posts ->
            posts?.let {
                binding.model = posts
            }

        })
        viewmodel.loadError.observe(viewLifecycleOwner) { error ->
            error?.let {
                errorTextView.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
        viewmodel.loading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                progressBar.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    errorTextView.visibility = View.GONE
                }
            }
        })

    }
}