package com.exemple.kotlin_listadetarefas

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

//Classe que vai gerenciar o clicl longo! precisa receber o context, o recyclerView que vai estar sendo exibido e o ClickListener
class ItemLongPressListener (context: Context, recyclerView: RecyclerView, val clickListener: ClickListener?) :RecyclerView.OnItemTouchListener {

        //verifica qual o gesto feito
    private val gestureDetector : GestureDetector
    //inicializa o GestureDetector
    init {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener(){
           //verifica o toque simples
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }
            //verifica o toque longo (e: é a direção do swipe)
            override fun onLongPress(e: MotionEvent) {
                val child = recyclerView.findChildViewUnder(e.x, e.y)//passa a direção do swipe
                if (child != null && clickListener != null){ //se swipe e o click nao forem nulos
                    //executa o onLong, passando o item para a recycler
                    clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child))
                }
            }
        })
    }
    //IMPLEMENTH MEMBERS do (listener?)
    //toque simples
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(e.x, e.y)
        //SE pegar uma posição e houver um click e detectar um toque na tela
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)){
            clickListener.onClick(child, rv.getChildAdapterPosition(child))
        }
        return false //pra nao executar mais nada depois do click simples
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }

    interface ClickListener {
        //passa o view(item) que foi clicado e a posição
        fun onClick(view: View, position: Int)
        //passa a view e podendo ser nula
        fun onLongClick(view: View?, position: Int)
    }


}