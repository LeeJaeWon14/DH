package com.jeepchief.dh.view.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeepchief.dh.databinding.FragmentTimelineBinding
import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.model.rest.DfService
import com.jeepchief.dh.model.rest.RetroClient
import com.jeepchief.dh.model.rest.dto.ItemSearchDTO
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.view.main.fragment.BaseFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TimelineFragment : BaseFragment() {
    private var _binding: FragmentTimelineBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimelineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val service = RetroClient.getInstance().create(DfService::class.java)
        service.getSearchItemsTest("암흑", NetworkConstants.WORD_TYPE_FRONT, "minLevel:70,maxLevel:90,rarity:에픽").enqueue(object :
            Callback<ItemSearchDTO> {
            override fun onResponse(call: Call<ItemSearchDTO>, response: Response<ItemSearchDTO>) {
                if(response.isSuccessful) {
                    Log.e("response success")
                    Log.e(response.body()?.rows?.toString()!!)
                }
                else {
                    Log.e("response fail")
                    Log.e(response.raw().request().url().toString())
                }
            }

            override fun onFailure(call: Call<ItemSearchDTO>, t: Throwable) {
                Log.e("response onFailure")
                Log.e(call.request().url().toString())
            }
        })

        binding.apply {

        }
    }
}