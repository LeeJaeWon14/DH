package com.jeepchief.dh.features.auction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeepchief.dh.R
import com.jeepchief.dh.core.util.Log
import com.jeepchief.dh.databinding.FragmentAuctionBinding
import com.jeepchief.dh.databinding.LayoutAuctionDialogBinding
import com.jeepchief.dh.features.auction.adapter.AuctionAdapter
import com.jeepchief.dh.features.main.fragment.BaseFragment

class AuctionFragment : BaseFragment() {
    private var _binding: FragmentAuctionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuctionViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuctionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.auction.observe(requireActivity()) {
            Log.e("AuctionDTO is $it")
            if(it.rows.isEmpty()) {
                Toast.makeText(mContext, getString(R.string.error_msg_no_result), Toast.LENGTH_SHORT).show()
                return@observe
            }
            val dlgView = LayoutAuctionDialogBinding.inflate(layoutInflater)
            val dlg = AlertDialog.Builder(mContext).create().apply {
                setView(dlgView.root)
                setCancelable(true)
            }

            dlgView.apply {
                rvAuction.apply {
                    layoutManager = LinearLayoutManager(mContext)
                    adapter = AuctionAdapter(it.rows)
                }

                btnAuctionClose.setOnClickListener { dlg.dismiss() }
            }

            dlg.show()
        }

        binding.apply {
            fabBack.setOnClickListener { requireActivity().onBackPressed() }

            ivAuctionSearch.setOnClickListener {
                if(edtAuction.text.toString().isEmpty()) {
                    Toast.makeText(mContext, getString(R.string.error_msg_not_allow_black), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                viewModel.getAuction(edtAuction.text.toString())
                requireActivity().run {
                    getSystemService(InputMethodManager::class.java)
                        .hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("onDestroy")
        _binding = null
    }
}