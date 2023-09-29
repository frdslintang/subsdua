package com.dicoding.coba.detail

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.dicoding.coba.R
import com.dicoding.coba.data.local.ModuleDatabase
import com.dicoding.coba.data.model.ResponseDetail
import com.dicoding.coba.data.model.ResponseUser
import com.dicoding.coba.databinding.ActivityDetailBinding
import com.dicoding.coba.detail.follow.FollowsFragment
import com.dicoding.coba.result.Result
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>{
        DetailViewModel.Factory(ModuleDatabase((this)))
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val item = intent.getParcelableExtra<ResponseUser.Item>("item")
        val username = item?.login?: ""

        viewModel.resultDetailUser.observe(this) {
            when (it) {
                is Result.Success<*> -> {
                    val user = it.data as ResponseDetail
                    binding.image.load(user.avatarUrl) {
                        transformations(CircleCropTransformation())
                    }
                    binding.nama.text = user.name
                    binding.username.text = user.login
                    // Mengambil followers dan following dari objek ResponseDetailUser
                    val followersCount = user.followers
                    val followingCount = user.following

                    binding.tvFollowers.text = resources.getString(R.string.followers, followersCount)
                    binding.tvFollowing.text = resources.getString(R.string.following, followingCount)
                }

                is Result.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is Result.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }

        viewModel.getDetailUser(username)
        val fragment = mutableListOf<Fragment>(
            FollowsFragment.newInstance(FollowsFragment.FOLLOWERS),
            FollowsFragment.newInstance(FollowsFragment.FOLLOWING),
        )
        val titleFragments = mutableListOf(
            getString(R.string.tab_followers),
            getString(R.string.tab_following)
        )

        val adapter = DetailAdapter(this, fragment)
        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tab, binding.viewpager) { tab, posisi ->
            tab.text = titleFragments[posisi]
        }.attach()

        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    viewModel.getFollowers(username)
                } else {
                    viewModel.getFollowing(username)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        viewModel.getFollowers(username)
        viewModel.getFollowing(username)
        viewModel.resultSuksesFavorite.observe(this){
            binding.btnFavorite.changeIconColor(R.color.red)
        }

        viewModel.resultDeleteFavorite.observe(this){
            binding.btnFavorite.changeIconColor(R.color.black) //ubah warna ke aslinya
        }
        binding.btnFavorite.setOnClickListener{
            viewModel.setFavorite(item)
        }

        viewModel.findFavorite(item?.id?:0){
            binding.btnFavorite.changeIconColor(R.color.red)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

fun FloatingActionButton.changeIconColor(@ColorRes color:Int){
    imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, color))
}
