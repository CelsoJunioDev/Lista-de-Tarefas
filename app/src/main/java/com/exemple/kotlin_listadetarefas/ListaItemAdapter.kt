package com.exemple.kotlin_listadetarefas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.ParseException
import java.text.SimpleDateFormat

class ListaItemAdapter(private val context: Context, private val itensList: List<ListaItemModel>) : RecyclerView.Adapter<ListaItemAdapter.ViewHolder>() {

    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view){

        var listaTexto : TextView = view.findViewById(R.id.texto)
        var listaData : TextView = view.findViewById(R.id.dataAtual)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemLista = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_layout, parent, false)
        return ViewHolder(itemLista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = itensList[position]
        holder.listaTexto.setText(listItem.listaTexto)
        holder.listaData.text = formataData(listItem.listaData!!) //"!!" para nao receber valor Nulo
    }

    private fun formataData(data: String): String {

        try {
            val formatoInicial = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val recebeData = formatoInicial.parse(data)
            val formatoFinal = SimpleDateFormat("d MMM, yyyy")
            return formatoFinal.format(recebeData)
        }catch (e: ParseException){
            return ""
        }
    }

    override fun getItemCount(): Int {
        return itensList.size
    }
}