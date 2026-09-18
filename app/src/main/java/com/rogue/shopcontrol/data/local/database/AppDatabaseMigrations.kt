package com.rogue.shopcontrol.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            "ALTER TABLE estabelecimentos ADD COLUMN apelido TEXT"
        )

    }

}


val MIGRATION_2_3 = object : Migration(2, 3) {

    override fun migrate(db: SupportSQLiteDatabase) {

        // Aponta as compras para o estabelecimento canônico (menor id) de cada CNPJ
        db.execSQL(
            """
            UPDATE compras
            SET estabelecimentoId = (
                SELECT MIN(e2.id) FROM estabelecimentos e2
                WHERE e2.cnpj = (
                    SELECT e1.cnpj FROM estabelecimentos e1
                    WHERE e1.id = compras.estabelecimentoId
                )
                AND e2.cnpj != ''
            )
            WHERE estabelecimentoId IN (
                SELECT id FROM estabelecimentos WHERE cnpj != ''
            )
            """
        )

        // Remove os estabelecimentos duplicados que sobraram, exceto o canônico
        db.execSQL(
            """
            DELETE FROM estabelecimentos
            WHERE cnpj != ''
            AND id NOT IN (
                SELECT MIN(id) FROM estabelecimentos
                WHERE cnpj != ''
                GROUP BY cnpj
            )
            """
        )

    }

}


val MIGRATION_3_4 = object : Migration(3, 4) {

    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            "ALTER TABLE compras ADD COLUMN origem TEXT NOT NULL DEFAULT 'NFC_E'"
        )

    }

}


val MIGRATION_4_5 = object : Migration(4, 5) {

    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            "ALTER TABLE produtos ADD COLUMN codigoBarras TEXT"
        )

        db.execSQL(
            "ALTER TABLE produtos ADD COLUMN apelido TEXT"
        )

    }

}


val MIGRATION_5_6 = object : Migration(5, 6) {

    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `renda_categorias` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `nome` TEXT NOT NULL
            )
            """
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `fontes_renda` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `nome` TEXT NOT NULL
            )
            """
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `renda` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `descricao` TEXT NOT NULL,
                `valor` REAL NOT NULL,
                `data` TEXT NOT NULL,
                `rendaCategoriaId` INTEGER NOT NULL,
                `fonteRendaId` INTEGER NOT NULL,
                `recorrente` INTEGER NOT NULL,
                `origemRecorrenteId` INTEGER,
                FOREIGN KEY(`rendaCategoriaId`) REFERENCES `renda_categorias`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
                FOREIGN KEY(`fonteRendaId`) REFERENCES `fontes_renda`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
            )
            """
        )

        val categoriasPadrao =
            listOf(
                "Salário",
                "Pensão",
                "Bônus",
                "13º",
                "Férias",
                "Freela",
                "Presente"
            )

        categoriasPadrao.forEach { nome ->

            db.execSQL(
                "INSERT INTO renda_categorias (nome) VALUES (?)",
                arrayOf(nome)
            )

        }

    }

}


val MIGRATION_6_7 = object : Migration(6, 7) {

    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            "ALTER TABLE compras ADD COLUMN tipo TEXT NOT NULL DEFAULT 'VARIAVEL'"
        )

        db.execSQL(
            "ALTER TABLE compras ADD COLUMN nome TEXT"
        )

        db.execSQL(
            "ALTER TABLE compras ADD COLUMN categoriaId INTEGER"
        )

        db.execSQL(
            "ALTER TABLE compras ADD COLUMN origemRecorrenteId INTEGER"
        )

        db.execSQL(
            "ALTER TABLE compras ADD COLUMN totalParcelas INTEGER"
        )

        db.execSQL(
            "ALTER TABLE compras ADD COLUMN dataCompetencia TEXT"
        )

        db.execSQL(
            "ALTER TABLE compras ADD COLUMN ativo INTEGER NOT NULL DEFAULT 1"
        )

    }

}


val MIGRATION_7_8 = object : Migration(7, 8) {

    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `arecebimentos` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `descricao` TEXT NOT NULL,
                `valor` REAL NOT NULL,
                `dataPrevista` TEXT NOT NULL,
                `rendaCategoriaId` INTEGER NOT NULL,
                `fonteRendaId` INTEGER NOT NULL,
                `tipo` TEXT NOT NULL DEFAULT 'PONTUAL',
                `totalParcelas` INTEGER,
                `origemRecorrenteId` INTEGER,
                `ativo` INTEGER NOT NULL DEFAULT 1,
                `pago` INTEGER NOT NULL DEFAULT 0,
                `dataPagamento` TEXT,
                `rendaGeradaId` INTEGER,
                FOREIGN KEY(`rendaCategoriaId`) REFERENCES `renda_categorias`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
                FOREIGN KEY(`fonteRendaId`) REFERENCES `fontes_renda`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
            )
            """
        )

    }

}


val MIGRATION_8_9 = object : Migration(8, 9) {

    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            "ALTER TABLE categorias ADD COLUMN grupoId INTEGER"
        )

    }

}


val MIGRATION_9_10 = object : Migration(9, 10) {

    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            "ALTER TABLE estabelecimentos ADD COLUMN categoriaId INTEGER"
        )

        db.execSQL(
            "ALTER TABLE compras ADD COLUMN dividida INTEGER NOT NULL DEFAULT 0"
        )

        db.execSQL(
            "ALTER TABLE arecebimentos ADD COLUMN compraOrigemId INTEGER"
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `divisao_conta` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `compraId` INTEGER NOT NULL,
                `fonteRendaId` INTEGER NOT NULL,
                `valor` REAL NOT NULL,
                FOREIGN KEY(`compraId`) REFERENCES `compras`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
                FOREIGN KEY(`fonteRendaId`) REFERENCES `fontes_renda`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
            )
            """
        )

    }

}
