package com.bolyartech.forge.test.dagger

import com.bolyartech.forge.server.ForgeServer
import com.bolyartech.forge.server.db.C3p0DbPool
import com.bolyartech.forge.server.db.DbConfiguration
import com.bolyartech.forge.server.db.DbPool
import com.mchange.v2.c3p0.ComboPooledDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbDaggerModule(private val dbConfig: DbConfiguration) {
    @Provides
    @Singleton
    fun provideComboPooledDataSource(): ComboPooledDataSource {
        return ForgeServer.createDataSourceHelper(dbConfig)
    }

    @Provides
    @Singleton
    internal fun provideDbPool(dbSource: ComboPooledDataSource): DbPool {
        return C3p0DbPool(dbSource)
    }
}

@Module
abstract class DbDaggerModuleBinds {

}