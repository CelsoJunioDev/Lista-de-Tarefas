package com.exemple.kotlin_listadetarefas

class ListaItemModel {
    var listaId =0
    var listaTexto: String? = null
    var listaData: String? = null
    constructor()

    constructor(listaId: Int, listaTexto: String?, listaData: String?) {
        this.listaId = listaId
        this.listaTexto = listaTexto
        this.listaData = listaData
    }

    //monta a criação da tabelaaaaaa

    companion object{
        val TABLE_NAME = "tarefas_db"

        val ID_COLUMN = "id"
        val TEXT_COLUMN = "tarefa"
        val DATA_COLUMN = "Data"

        val CREATE_TABLE = (
                "CREATE TABLE "
                + TABLE_NAME +"("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TEXT_COLUMN + " TEXT, "
                + DATA_COLUMN +" DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")"
                )


    }
}