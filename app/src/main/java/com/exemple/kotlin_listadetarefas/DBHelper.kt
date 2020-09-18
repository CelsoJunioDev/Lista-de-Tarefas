package com.exemple.kotlin_listadetarefas

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private val  DB_NAME = "lista_db"
class DBHelper (context:Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {

    val itemList : List<ListaItemModel>
    get(){
        val listaItens = ArrayList<ListaItemModel>()
        val selectQuery = "SELECT * FROM "+ ListaItemModel.TABLE_NAME +
                " ORDER BY " + ListaItemModel.DATA_COLUMN +" DESC"

        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if(cursor.moveToFirst()){
            do {
                val listaItem = ListaItemModel()
                listaItem.listaId = cursor.getInt(cursor.getColumnIndex(ListaItemModel.ID_COLUMN))
                listaItem.listaTexto = cursor.getString(cursor.getColumnIndex(ListaItemModel.TEXT_COLUMN))
                listaItem.listaData = cursor.getString(cursor.getColumnIndex(ListaItemModel.DATA_COLUMN))

                listaItens.add(listaItem)
            }while (cursor.moveToNext())
        }
        db.close()
        return listaItens
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ListaItemModel.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
                          //busca a tabela pelo nome
        db.execSQL("DROP TABLE IF EXISTS " +ListaItemModel.TABLE_NAME)
        //cria novamente
        onCreate(db)
    }

    fun insertItemLista (itemLista: String) :Long{ //recebe o texto digitado
        val db = this.writableDatabase //pega uma tabela editavel
        val values = ContentValues() //pega os valores e salva no ContentValues

        //insere o item no put. apenas um, pois os outros estão sendo inseridos automaticamente
        values.put(ListaItemModel.TEXT_COLUMN, itemLista)
        //insere no db
        val item = db.insert(ListaItemModel.TABLE_NAME, null, values)
        db.close()
        return item

    }
    fun atualizarListaItem(item: Long) : ListaItemModel{ //Função para atualizar a lista assim que adicionar um novo item.

        val db = this.writableDatabase
        //pega as colunas do banco e depois compara se o ID bate com o ID que será passado pela variavel "item"
        val cursor = db.query(ListaItemModel.TABLE_NAME, arrayOf(ListaItemModel.ID_COLUMN,
        ListaItemModel.TEXT_COLUMN, ListaItemModel.DATA_COLUMN), ListaItemModel.ID_COLUMN +"=?" //compara
            , arrayOf(item.toString()), null, null, null, null)
        cursor?.moveToFirst() // se comparar, move para o item

        val listaItem = ListaItemModel( //busca no banco o item do ID comparado
            cursor!!.getInt(cursor.getColumnIndex(ListaItemModel.ID_COLUMN)),
            cursor!!.getString(cursor.getColumnIndex(ListaItemModel.TEXT_COLUMN)),
            cursor!!.getString(cursor.getColumnIndex(ListaItemModel.DATA_COLUMN))
        )
        cursor.close()
        return listaItem
    }

    fun deleteItemLista(listaItemModel: ListaItemModel){
        val db = this.writableDatabase
        db.delete(ListaItemModel.TABLE_NAME, ListaItemModel.ID_COLUMN + " = ?",//compara o id do banco
            // com o id recebido do parametro
        arrayOf(listaItemModel.listaId.toString()))
        db.close()
    }
    fun deleteTodaLista(){
        val db = this.writableDatabase
        db.delete(ListaItemModel.TABLE_NAME, ListaItemModel.ID_COLUMN +" > ?", //pega o id, e se for > 0 ele deleta (TUDO)
        arrayOf("0"))
        db.close()
    }
}