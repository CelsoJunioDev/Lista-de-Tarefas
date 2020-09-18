package com.exemple.kotlin_listadetarefas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var coordnatorLayout: CoordinatorLayout? = null
    private var recyclerView: RecyclerView? = null

    private var db : DBHelper? = null

    private var itemsList = ArrayList<ListaItemModel>()
    private var mAdapter : ListaItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       /* val tolbar= findViewById<View>(R.id.toolbar) as Toolbar
        tolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)
         */

        controle()

        
    }

    private fun controle() {
        coordnatorLayout = findViewById(R.id.layout_main)
        recyclerView = findViewById(R.id.recyclerMain)
        db = DBHelper(this)
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            showDialog(false, null, -1)
        }

        //Exibe os itens
        itemsList.addAll(db!!.itemList)
        mAdapter = ListaItemAdapter(this, itemsList)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter

        recyclerView!!.addOnItemTouchListener(ItemLongPressListener(this,
            recyclerView!!, object : ItemLongPressListener.ClickListener{
                override fun onClick(view: View, position: Int) {}

                override fun onLongClick(view: View?, position: Int) {
                    showActionsDialog(position)
                }
            }

        ))
    }

    private fun showActionsDialog(position: Int) {
        //passa as opções do alertDialog
        val options = arrayOf<CharSequence>(getString(R.string.editar),
    getString(R.string.excluir), getString(R.string.excluirTudo))
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.tituloOpcao))
        builder.setItems(options){dialog, itemIndex ->
            when(itemIndex){
                //se a opção clicada for 0 (editar)
                0 -> showDialog(true, itemsList[position], position)
                1 -> deletarItemLista(position)
                2 -> deletarTudo()
                else -> Toast.makeText(applicationContext, getString(R.string.toastErro), Toast.LENGTH_SHORT).show()
            }
            
        }
        builder.show()
    }

    private fun deletarTudo() {
        db!!.deleteTodaLista() //deleta tudo
        itemsList.removeAll(itemsList)
        mAdapter!!.notifyDataSetChanged()
    }

    private fun deletarItemLista(position: Int) {
        db!!.deleteItemLista(itemsList[position]) //deleta o item selecionado
        itemsList.removeAt(position) // retira da lista
        mAdapter!!.notifyItemRemoved(position) //notifica
    }

    private fun showDialog(isUpdate: Boolean, listaItemModel: ListaItemModel?, position: Int) {

        val layoutInflaterAndroid = LayoutInflater.from(applicationContext)
        //Cria a view do layout
        val view = layoutInflaterAndroid.inflate(R.layout.lista_dialog, null)

        //variavel que recebe o AlertDialog
        val userInput = AlertDialog.Builder(this@MainActivity)
        userInput.setView(view)

        val itemLista = view.findViewById<EditText>(R.id.dialogText)
        val titulo = view.findViewById<TextView>(R.id.dialogTitle)
        titulo.text = if(!isUpdate) getString(R.string.novo) else getString(R.string.editar)

        userInput
            .setCancelable(false)
            .setPositiveButton(if(isUpdate) getString(R.string.atualizar)
            else getString(R.string.salvar)) {dialogBox, id ->}
            .setNegativeButton(getString(R.string.cancelar)){dialogBox, id -> dialogBox.cancel()}

        val alertDialog = userInput.create()
        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(itemLista.text.toString())){
                Toast.makeText(this@MainActivity, getString(R.string.toastTarefa), Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }else {
                alertDialog.dismiss()
            }
            createItemLista(itemLista.text.toString())

        })
    }

    private fun createItemLista(listaText: String) {
        val item = db!!.insertItemLista(listaText) //insere nova tarefa
        val novoItem = db!!.atualizarListaItem(item) //atualiza na lista

        if (novoItem!= null){
            itemsList.add(novoItem)
            mAdapter!!.notifyDataSetChanged()
        }

    }
}