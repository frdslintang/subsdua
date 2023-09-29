package com.dicoding.coba.detail.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.coba.UserAdapter
import com.dicoding.coba.data.model.ResponseUser
import com.dicoding.coba.databinding.FragmentFollowsBinding
import com.dicoding.coba.detail.DetailViewModel
import com.dicoding.coba.result.Result

class FollowsFragment : Fragment() {

    private var binding:FragmentFollowsBinding?=null
    private val adapter by lazy {
        UserAdapter{

        }
    }

    private val viewModel by activityViewModels<DetailViewModel>()
    var type = 6
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowsBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvFollows?.apply {
            layoutManager= LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@FollowsFragment.adapter
        }


        when(type){
            FOLLOWERS->{
                viewModel.resultFollowersUser.observe(viewLifecycleOwner, this::manageResultFollows)
            }
            FOLLOWING->{
                viewModel.resultFollowingUser.observe(viewLifecycleOwner, this::manageResultFollows)
            }
        }
    }

    private fun manageResultFollows(state: Result) {
        when (state) {
            is Result.Success<*> -> {
                if (state.data is MutableList<*>) {
                    val dataList = state.data.filterIsInstance<ResponseUser.Item>()
                    adapter.setData(dataList.toMutableList())
                } else {
                    Toast.makeText(requireActivity(), "Data tidak valid", Toast.LENGTH_SHORT).show()
                }
            }
            is Result.Error -> {
                Toast.makeText(requireActivity(), state.exception.message.toString(), Toast.LENGTH_SHORT).show()
            }
            is Result.Loading -> {
                binding?.progressBar?.isVisible = state.isLoading
            }
        }
    }

    companion object {
        const val FOLLOWING = 100
        const val FOLLOWERS = 101
        fun newInstance(type: Int) = FollowsFragment()
            .apply {
                this.type = type
            }
    }
}