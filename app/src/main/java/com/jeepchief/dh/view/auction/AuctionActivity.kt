package com.jeepchief.dh.view.auction

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.jeepchief.dh.R
import com.jeepchief.dh.databinding.ActivityAuctionBinding
import com.jeepchief.dh.model.rest.dto.AuctionRows
import com.jeepchief.dh.util.RarityChecker
import java.text.DecimalFormat

class AuctionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuctionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.apply {
            title = "아이템 정보"
            setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.black)))
        }
        super.onCreate(savedInstanceState)

        binding = ActivityAuctionBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        supportActionBar?.hide()

        val row = intent.getSerializableExtra("AuctionRows") as AuctionRows

        binding.apply {
            tvAuctionCount.text = row.count.toString()
            tvAuctionCurrentPrice.text = makeComma(row.currentPrice.toString()).plus("골드")
            tvAuctionUnitPrice.text = makeComma(row.unitPrice.toString()).plus("골드")
            tvAuctionExpireTime.text = row.expireDate
            tvAuctionAvailableLevel.text = row.itemAvailableLevel.toString()
            tvAuctionRarity.run {
                text = row.itemRarity
                setTextColor(RarityChecker.convertColor(row.itemRarity))
            }
            tvAuctionReinforce.text = row.reinforce.toString()
            tvAuctionRefine.text = row.refine.toString()
            tvAuctionAdventureFame.text = row.adventureFame.toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_auction, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_close -> finish()
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun makeComma(price : String) : String {
        //소숫점이 존재하거나 천 단위 이하일 경우 생략
        if(price.contains(".") || price.length < 4) {
            return price
        }
        val formatter = DecimalFormat("###,###")
        return formatter.format(price.toLong())
    }
}