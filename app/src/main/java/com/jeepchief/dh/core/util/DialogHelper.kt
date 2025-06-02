//package com.jeepchief.dh.core.util
//
//import android.content.Context
//import android.content.DialogInterface
//import android.graphics.drawable.Drawable
//import android.view.LayoutInflater
//import androidx.appcompat.app.AlertDialog
//import androidx.core.view.isVisible
//import androidx.recyclerview.widget.DividerItemDecoration
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.viewbinding.ViewBinding
//import com.bumptech.glide.Glide
//import com.jeepchief.dh.core.network.NetworkConstants
//import com.jeepchief.dh.core.network.dto.ItemsDTO
//import com.jeepchief.dh.databinding.LayoutDialogItemInfoBinding
//import com.jeepchief.dh.features.itemsearch.adapter.ItemStatusAdapter
//
//object DialogHelper {
//    fun itemInfoDialog(context: Context, it: ItemsDTO) : AlertDialog {
//        val dlgView = LayoutDialogItemInfoBinding.inflate(LayoutInflater.from(context))
//        val dlg = AlertDialog.Builder(context).create().apply {
//            setView(dlgView.root)
//            setCancelable(false)
//        }
//
//        dlgView.run {
//            tvItemName.run {
//                text = it.itemName.plus(" (Lv. ${it.itemAvailableLevel})")
//                setTextColor(RarityChecker.convertColor(it.itemRarity))
//            }
//            Glide.with(context)
//                .load(String.format(NetworkConstants.ITEM_URL, it.itemId))
//                .centerCrop()
//                .override(112, 112)
//                .into(ivItemInfoImage)
//
//            tvItemType.text = it.itemType.plus(" - ${it.itemTypeDetail}")
//            tvItemObtain.text = it.itemObtainInfo
//            tvItemExplation.text = it.itemExplain
//            tvItemFlavor.run {
//                if(it.itemFlavorText == "") isVisible = false
//                else text = it.itemFlavorText
//            }
//            rvItemStatus.run {
//                it.itemStatus?.let {
//                    val manager = LinearLayoutManager(context)
//                    layoutManager = manager
//                    adapter = ItemStatusAdapter(it)
//                    addItemDecoration(
//                        DividerItemDecoration(
//                            context, manager.orientation
//                    )
//                    )
//                } ?: run { isVisible = false }
//            }
//            tvItemGrowInfo.run {
//                it.growInfo?.let {
//                    var count = 1
//                    val strBuilder = StringBuilder("$count. ")
//                    it.options.forEach { option ->
//                        option.explainDetail?.let { explainDetail ->
//                            count ++
//                            if(count < 5) {
//                                strBuilder.append(explainDetail.plus("\n$count. "))
//                            } else {
//                                strBuilder.append(explainDetail)
//                            }
//                        } ?: run {
//                            // only custom epic item
//                            strBuilder.run {
//                                clear()
//                                append(option.explain)
//                            }
//                        }
//
//                    }
//                    text = strBuilder.toString()
//                } ?: run { isVisible = false }
//            }
//            btnItemInfoClose.setOnClickListener { dlg.dismiss() }
//        }
//
//        return dlg
//    }
//
//    /**
//     * 확인, 취소가 있는 기본 Dialog
//     * negative가 NULL이면 확인 버튼만 출력
//     * @param context
//     * @param title
//     * @param msg
//     * @param positive
//     * @param negative null 입력하면 취소 버튼 없음, 입력 안하면 dismiss 기능만 있는 취소 버튼
//     */
//    fun basicDialog(
//        context: Context,
//        title: String,
//        msg: String,
//        positive: DialogInterface.OnClickListener,
//        negative: DialogInterface.OnClickListener? = DialogInterface.OnClickListener { _, _ -> }
//    ) : AlertDialog.Builder {
//        val dlg = AlertDialog.Builder(context)
//            .setCancelable(false)
//            .setTitle(title)
//            .setMessage(msg)
//            .setPositiveButton("확인", positive)
//
//        negative?.let {
//            dlg.setNegativeButton("취소", it)
//        }
//
//        return dlg
//    }
//
//    /**
//     * Custom Dialog
//     * @param context
//     * @param backgroundRes 백그라운드에 적용할 Drawable resId
//     * @param view inflate 되어있는 ViewBinding 객체
//     */
//    fun customDialog(
//        context: Context,
//        backgroundRes: Any? = null,
//        view: (AlertDialog) -> ViewBinding
//    ) : AlertDialog {
//        return AlertDialog.Builder(context).create().apply {
//            setView(view.invoke(this).root)
//            setCancelable(false)
//            backgroundRes?.let {
//                when(it) {
//                    is Drawable -> window?.setBackgroundDrawable(it)
//                    is Int -> window?.setBackgroundDrawableResource(it)
//                }
//            }
//        }
//    }
//}