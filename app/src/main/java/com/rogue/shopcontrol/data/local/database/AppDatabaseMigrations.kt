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
