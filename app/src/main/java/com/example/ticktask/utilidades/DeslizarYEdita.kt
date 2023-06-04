package com.example.ticktask.utilidades

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.widget.AppCompatDrawableManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.ticktask.R

abstract class DeslizarYEdita(contexto: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
    //Esta clase nos permite desplazar el dedo hacia la derecha y que cambie lo que se muestra

    private val editDrawable = ContextCompat.getDrawable(contexto, R.drawable.vc_editar)
    private val iconWidth = editDrawable?.intrinsicWidth ?: 0
    private val iconHeight = editDrawable?.intrinsicHeight ?: 0
    private val backgroundDrawable = ColorDrawable()
    private val backgroundColor = ContextCompat.getColor(contexto, R.color.azulin)
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    // Método que se ejecuta cuando se arrastra el item
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(c, itemView.left + dX, itemView.top.toFloat(), itemView.left.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        backgroundDrawable.color = backgroundColor
        backgroundDrawable.setBounds(
            itemView.left + dX.toInt(),
            itemView.top,
            itemView.left,
            itemView.bottom
        )
        backgroundDrawable.draw(c)

        if (iconHeight == 0 || iconWidth == 0) return

        val editIconTop = itemView.top + (itemHeight - iconHeight) / 2
        val editIconMargin = (itemHeight - iconHeight)
        val editIconLeft = itemView.left + editIconMargin - iconWidth
        val editIconRight = itemView.left + editIconMargin
        val editIconBottom = editIconTop + iconHeight

        editDrawable?.setBounds(editIconLeft, editIconTop, editIconRight, editIconBottom)
        editDrawable?.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }
}
